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
		if(expected == null && actual == null) {
			return;
		}
		
		if(expected == null || actual == null) {
			throw new OOPAssertionError(expected, actual);
		}
		
		if(!(expected.equals(actual))) {
			throw new OOPAssertionError(expected, actual);
		}
	}
	
	public static OOPTestSummary runClass(Class<?> testClass) {
		// Test whether testClass is null
		if(testClass == null) {
			throw new IllegalArgumentException();
		}
		
		// Test whether testClass has the OOPTestClass annotation
		if(!(testClass.isAnnotationPresent(OOPTestClass.class))) {
			throw new IllegalArgumentException();
		}
		
		// Set a flag denoting whether the invocation order is important
		boolean invokeByOrder = (testClass.getAnnotation(OOPTestClass.class)).value();

		// Find the empty constructor and run it to instantiate the class
		Constructor<?>[] constArray = testClass.getConstructors();
		Object newObject = new Object();
		for(Constructor<?> con : constArray) {
			if(con.getParameterTypes().length == 0) {
				try {
					newObject = con.newInstance();
				} catch (Exception e) {}
			}
		}
		
		// Find the OOPSetup methods and invoke them
		List<Method> setupMethods = getBeforeAfterSetupMethods(testClass, OOPSetup.class);
		for(Method method : setupMethods) {
			try {
				method.invoke(newObject);
			} catch(Exception e) {}
		}

		// Find the test methods
		List<MethodPair> allTestMethods = new ArrayList<>();
		for(Method method : testClass.getMethods()) {
			if(method.isAnnotationPresent(OOPTest.class)) {
				OOPTest annotation = method.getAnnotation(OOPTest.class);
				allTestMethods.add(new MethodPair(annotation.order(), method));
			}
		}
		
		// Order the methods if required by the annotation parameter
		if(invokeByOrder) {
			Collections.sort(allTestMethods);
		}
		
		// Initialize a result map, later to be passed to OOPTestSummary
		Map<String, OOPResult> resultMap = new HashMap<>();
		
		// Invoke all the test methods, and invoke the before and after methods if required
		for(MethodPair mp : allTestMethods) {
			Object backup = new Object();
			OOPResult result = new OOPResultImpl();
			
			try {
				backup = invokeBeforeMethods(mp.method.getName(), testClass, newObject);
			} catch (Exception e) {
				result = new OOPResultImpl(e.getMessage(), OOPResult.OOPTestResult.ERROR);
				resultMap.put(mp.method.getName(), result);
				continue;
			}
			
			try {
				mp.method.invoke(newObject);
				result = new OOPResultImpl(null, OOPResult.OOPTestResult.SUCCESS);
			} catch(InvocationTargetException targetEx) {
				// Upon remote exception, check the exception type and set the result
				OOPTest annotation = mp.method.getAnnotation(OOPTest.class);
				
				if(annotation.test_throws() && (targetEx.getCause().getClass().equals(annotation.exc()))) {
					result = new OOPResultImpl(null, OOPResult.OOPTestResult.SUCCESS);
				} else if(targetEx.getCause() instanceof OOPAssertionError) {
					result = new OOPResultImpl(targetEx.getCause().getMessage(), 
							OOPResult.OOPTestResult.FAILURE);
				} else {
					result = new OOPResultImpl(targetEx.getCause().getMessage(),
							OOPResult.OOPTestResult.ERROR);
				}
			} catch(Exception e) {}
			
			try {
				invokeAfterMethods(mp.method.getName(), testClass, new Box(newObject), backup);
			} catch (Exception e) {
				result = new OOPResultImpl(e.getMessage(), OOPResult.OOPTestResult.ERROR);
			}
			
			resultMap.put(mp.method.getName(), result);
		}
		
		return new OOPTestSummary(resultMap); 
	}
	
	private static Object invokeBeforeMethods(String methodName, Class<?> testClass,
			Object testObject) throws Exception {
		Object backup = backupObject(testClass, testObject);
		
		List<Method> methodsToRun = getBeforeAfterSetupMethods(testClass, OOPBefore.class);
		for(Method method : methodsToRun){
			OOPBefore annotation = method.getAnnotation(OOPBefore.class);
			for(String s : annotation.value()) {
				if(s.equals(methodName)) {
					try {
						method.invoke(testObject);
					} catch(Exception e) {
						testObject = backup;
						throw e;
					}
				}
			}
		}
		
		return backup;
	}
	
	/**
	 * Returns a list of before/after methods belonging to a test class, in the required order
	 * according to the annotation type (meaning that for OOPBefore, methods will be ordered
	 * from topmost superclass to the class itself, and for OOPAfter, they will be ordered
	 * in reverse)
	 * @param testClass The class from which to start the lookup
	 * @param type The type of annotation to seek
	 * @return A list of before/after methods
	 */
	private static List<Method> getBeforeAfterSetupMethods(Class<?> testClass, 
			Class<? extends Annotation> type) {
		List<Method> returnList = new ArrayList<>();
		Class<?> current = testClass;
		
		// Extract the relevant methods from the class, and proceed to its superclass,
		// until you've reached Object, which is the root of all objects
		while(current != Object.class) {
			// Get the methods of the current class
			for(Method m : current.getMethods()) {
				if(m.isAnnotationPresent(type)) {
					returnList.add(m);
				}
			}
			
			// Proceed to the superclass
			current = current.getSuperclass();
		}
		
		// The list is ordered from the current class to the topmost superclass.
		// For before methods, reverse the list
		if(type.equals(OOPBefore.class) || type.equals(OOPSetup.class)) {
			Collections.reverse(returnList);
		}
		
		return returnList;
	}
	
	private static Object backupObject(Class<?> testClass, Object testObject) {
		Object backup = new Object();
		try {
			backup = testClass.newInstance();
		} catch (Exception e) {}
		
		for (Field field : testClass.getDeclaredFields()) {
			field.setAccessible(true);
			boolean didBackup = false;
			
			for (Class<?> i : field.getDeclaringClass().getInterfaces()) {
				if (i.getName().equals("Cloneable")) {
					try {
						Object fieldBackup = field.getDeclaringClass().getMethod("clone").invoke(testObject);
						field.set(backup, fieldBackup);
						didBackup = true;
						break;
					} catch (Exception e) {}
				}
			}
			
			if (didBackup) continue;
			
			try {
				Constructor<?> c = field.getDeclaringClass().getConstructor(testClass);
				field.set(backup, c.newInstance(field.get(testObject)));
				continue;
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			try {
				field.set(backup, field.get(testObject));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return backup;
	}
	
	private static void invokeAfterMethods(String methodName, Class<?> testClass, Object testObject,
			Object originalBackup) throws Exception {
		
		List<Method> methodsToRun = getBeforeAfterSetupMethods(testClass, OOPAfter.class);
		
		for(Method method : methodsToRun) {
			OOPAfter annotation = method.getAnnotation(OOPAfter.class);
			for(String s : annotation.value()) {
				if(s.equals(methodName)) {
					try {
						method.invoke(testObject);
					} catch(Exception e) {
						testObject = originalBackup;
						throw e;
					}
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

	private static class Box {
		Object ref;
		public Box(Object ref) { this.ref = ref; }
		public Object get() { return this.ref; }
		public void set(Object ref) { this.ref = ref; }
	}
	
}
