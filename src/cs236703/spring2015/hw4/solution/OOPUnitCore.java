package cs236703.spring2015.hw4.solution;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cs236703.spring2015.hw4.provided.OOPAssertionError;
import cs236703.spring2015.hw4.provided.OOPResult;

public class OOPUnitCore {
	public static void assertEquals(Object expected, Object actual) {
		if (expected == null && actual == null) {
			return;
		}

		if (expected == null || actual == null) {
			throw new OOPAssertionError(expected, actual);
		}

		if (!(expected.equals(actual))) {
			throw new OOPAssertionError(expected, actual);
		}
	}

	public static OOPTestSummary runClass(Class<?> testClass) {
		// Test whether testClass is null
		if (testClass == null) {
			throw new IllegalArgumentException();
		}

		// Test whether testClass has the OOPTestClass annotation
		if (!(testClass.isAnnotationPresent(OOPTestClass.class))) {
			throw new IllegalArgumentException();
		}

		// Find the empty constructor and run it to instantiate the class
		Constructor<?>[] constArray = testClass.getConstructors();
		Object newObject = new Object();
		for (Constructor<?> con : constArray) {
			if (con.getParameterTypes().length == 0) {
				try {
					con.setAccessible(true);
					newObject = con.newInstance();
				} catch (Exception e) {
				}
			}
		}

		// Find the OOPSetup methods and invoke them
		List<Method> setupMethods = getBeforeAfterSetupMethods(testClass, OOPSetup.class);
		for (Method method : setupMethods) {
			try {
				method.invoke(newObject);
			} catch (Exception e) {
			}
		}

		List<MethodPair> allTestMethods = getTestMethods(testClass);

		// Initialize a result map, later to be passed to OOPTestSummary
		Map<String, OOPResult> resultMap = new HashMap<>();

		// Invoke all the test methods, and invoke the before and after methods
		// if required
		Object backup = new Object();
		for (MethodPair mp : allTestMethods) {
			
			// Try to create a backup. If one of the fields were not backed up, throw an exception
			OOPResult result = new OOPResultImpl();
			try {
				backup = backupObject(testClass, newObject);
			} catch(Exception e) {
				result = new OOPResultImpl("The test must throw",
						OOPResult.OOPTestResult.FAILURE);
				resultMap.put(mp.method.getName(), result);
				continue;
			}
			

			// Backup the object and run all of the before methods
			try {
				invokeBeforeMethods(mp.method.getName(), testClass, newObject);
			} catch (Exception e) {
				result = new OOPResultImpl(e.getMessage(), OOPResult.OOPTestResult.ERROR);
				resultMap.put(mp.method.getName(), result);
				newObject = backup;
				continue;
			}

			// Run all tests
			try {
				mp.method.invoke(newObject);
				OOPTest annotation = mp.method.getAnnotation(OOPTest.class);
				if (annotation.test_throws()) {
					result = new OOPResultImpl("The test must throw",
							OOPResult.OOPTestResult.FAILURE);
				} else {
					result = new OOPResultImpl(null, OOPResult.OOPTestResult.SUCCESS);
				}
			} catch (InvocationTargetException targetEx) {
				// Upon remote exception, check the exception type and set the
				// result
				OOPTest annotation = mp.method.getAnnotation(OOPTest.class);

				if (annotation.test_throws()
						&& (targetEx.getCause().getClass().equals(annotation.exc()))) {
					result = new OOPResultImpl(null, OOPResult.OOPTestResult.SUCCESS);
				} else if (targetEx.getCause() instanceof OOPAssertionError) {
					result = new OOPResultImpl(targetEx.getCause().getMessage(),
							OOPResult.OOPTestResult.FAILURE);
				} else {
					result = new OOPResultImpl(targetEx.getCause().getMessage(),
							OOPResult.OOPTestResult.ERROR);
				}
			} catch (Exception e) {
			}

			// Run all after methods. Upon exception, restore the object from
			// backup
			try {
				invokeAfterMethods(mp.method.getName(), testClass, newObject);
			} catch (Exception e) {
				newObject = backup;
				result = new OOPResultImpl(e.getMessage(), OOPResult.OOPTestResult.ERROR);
			}

			resultMap.put(mp.method.getName(), result);
		}

		return new OOPTestSummary(resultMap);
	}

	private static void invokeBeforeMethods(String methodName, Class<?> testClass, Object testObject)
			throws Exception {

		List<Method> methodsToRun = getBeforeAfterSetupMethods(testClass, OOPBefore.class);
		for (Method method : methodsToRun) {
			OOPBefore annotation = method.getAnnotation(OOPBefore.class);
			for (String s : annotation.value()) {
				if (s.equals(methodName)) {
					method.invoke(testObject);
				}
			}
		}
	}

	private static List<MethodPair> getTestMethods(Class<?> testClass) {
		// Create a list of lists, each list representing the test methods in
		// the corresponding class
		List<List<MethodPair>> methodsList = new ArrayList<>();
		List<MethodPair> returnList = new ArrayList<>();
		Class<?> current = testClass;

		while (current != Object.class) {
			List<MethodPair> currentMethods = new ArrayList<>();

			// Get all the test methods from the class, in whichever order they
			// appear in
			for (Method m : current.getDeclaredMethods()) {
				if (m.isAnnotationPresent(OOPTest.class)) {
					int order = ((OOPTest) m.getAnnotation(OOPTest.class)).order();
					currentMethods.add(new MethodPair(order, m));
				}
			}

			// If the class demands order, order the list
			if (((OOPTestClass) current.getAnnotation(OOPTestClass.class)).value()) {
				Collections.sort(currentMethods);
			}

			// Add the list to the list of lists
			methodsList.add(currentMethods);
			current = current.getSuperclass();
		}

		// [3,4],[1,2]
		Collections.reverse(methodsList);

		// [1,2],[3,4]
		for (List<MethodPair> lm : methodsList) {
			returnList.addAll(lm);
		}

		// [1,2,3,4]
		return returnList;
	}

	/**
	 * Returns a list of before/after methods belonging to a test class, in the
	 * required order according to the annotation type (meaning that for
	 * OOPBefore, methods will be ordered from topmost superclass to the class
	 * itself, and for OOPAfter, they will be ordered in reverse)
	 * 
	 * @param testClass
	 *            The class from which to start the lookup
	 * @param type
	 *            The type of annotation to seek
	 * @return A list of before/after methods
	 */
	private static List<Method> getBeforeAfterSetupMethods(Class<?> testClass,
			Class<? extends Annotation> type) {
		List<Method> returnList = new ArrayList<>();
		Class<?> current = testClass;

		// Extract the relevant methods from the class, and proceed to its
		// superclass,
		// until you've reached Object, which is the root of all objects
		while (current != Object.class) {
			// Get the methods of the current class
			for (Method m : current.getDeclaredMethods()) {
				if (m.isAnnotationPresent(type)) {
					returnList.add(m);
				}
			}

			// Proceed to the superclass
			current = current.getSuperclass();
		}

		// The list is ordered from the current class to the topmost superclass.
		// For before methods, reverse the list
		if (type.equals(OOPBefore.class) || type.equals(OOPSetup.class)) {
			Collections.reverse(returnList);
		}

		return returnList;
	}

	private static Object backupObject(Class<?> testClass, Object testObject) throws Exception {
		Object backup = new Object();
		try {
			backup = testClass.newInstance();
		} catch (Exception e) {
		}

		for (Field field : testClass.getDeclaredFields()) {
			field.setAccessible(true);
			boolean didBackup = false;

			for (Class<?> i : field.getType().getInterfaces()) {
				if (i.getName().equals("java.lang.Cloneable")) {
					try {
						Object fieldBackup = field.getDeclaringClass().getMethod("clone")
								.invoke(testObject);
						field.set(backup, fieldBackup);
						didBackup = true;
						break;
					} catch (Exception e) {
					}
				}
			}

			if (didBackup)
				continue;

			try {
				Constructor<?> c = field.getDeclaringClass().getConstructor(testClass);
				field.set(backup, c.newInstance(field.get(testObject)));
				didBackup = true;
			} catch (Exception e) {
			}

			if(!didBackup) {
				try {
					field.set(backup, field.get(testObject));
					didBackup = true;
				} catch (Exception e) {}
			}
			
			if(!didBackup) {
				throw new Exception();
			}
		}

		return backup;
	}

	private static void invokeAfterMethods(String methodName, Class<?> testClass, Object testObject)
			throws Exception {

		List<Method> methodsToRun = getBeforeAfterSetupMethods(testClass, OOPAfter.class);

		for (Method method : methodsToRun) {
			OOPAfter annotation = method.getAnnotation(OOPAfter.class);
			for (String s : annotation.value()) {
				if (s.equals(methodName)) {
					method.invoke(testObject);
				}
			}
		}
	}

	private static class MethodPair implements Comparable<MethodPair> {
		private int order;
		private Method method;

		public MethodPair(int order, Method method) {
			this.order = order;
			this.method = method;
		}

		@Override
		public int compareTo(MethodPair o) {
			return this.order - o.order;
		}

	}

	public static class OOPResultImpl implements OOPResult {

		private OOPTestResult result;
		private String message;

		public OOPResultImpl() {

		}

		public OOPResultImpl(String message, OOPTestResult result) {
			this.message = message;
			this.result = result;
		}

		@Override
		public OOPTestResult getResultType() {
			return result;
		}

		@Override
		public String getMessage() {
			return message;
		}

		@Override
		public boolean equals(Object obj) {
			if (!(obj instanceof OOPResultImpl)) {
				return false;
			}
			return ((OOPResultImpl) obj).getResultType() == (this.getResultType())
					&& ((OOPResultImpl) obj).getMessage().equals(this.getMessage());
		}
	}
}
