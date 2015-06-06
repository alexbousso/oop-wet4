package cs236703.spring2015.hw4.solution;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
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
		invokeMethodByAnnotation(testClass, newObject, OOPSetup.class);
		
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
			invokeBeforeMethods(mp.method.getName(), testClass, newObject);
			OOPResult result;
			try {
				mp.method.invoke(newObject);
				result = new OOPResultImpl(null, OOPResult.OOPTestResult.SUCCESS);
				resultMap.put(mp.method.getName(), result);
			} catch(InvocationTargetException targetEx) {
				// Upon remote exception, check the exception type and set the result
				if(targetEx.getCause() instanceof OOPAssertionError) {
					result = new OOPResultImpl(targetEx.getCause().getMessage(), 
							OOPResult.OOPTestResult.FAILURE);
				} else {
					result = new OOPResultImpl(targetEx.getCause().getMessage(),
							OOPResult.OOPTestResult.ERROR);
				}
				resultMap.put(mp.method.getName(), result);
			} catch(Exception e) {}
			invokeAfterMethods(mp.method.getName(), testClass, newObject);
		}
		
		return new OOPTestSummary(resultMap); 
	}
	
	private static boolean invokeBeforeMethods(String methodName, Class<?> testClass, Object testObject) {
		Object backup;
		boolean didBackup = false;
		
		for (Class<?> c : testClass.getInterfaces()) {
			if (c.getName().equals("Cloneable")) {
				try {
					backup = testClass.getMethod("clone").invoke(testObject);
					didBackup = true;
				} catch (Exception e) {}
			}
		}
		
		if (didBackup == false) {
			try {
				Constructor<?> c = testClass.getConstructor(testClass);
				backup = c.newInstance(testObject);
				didBackup = true;
			} catch (Exception e) {}
		}
		
		for(Method method : testClass.getMethods()) {
			if(method.isAnnotationPresent(OOPBefore.class)) {
				OOPBefore annotation = method.getAnnotation(OOPBefore.class);
				
				for(String s : annotation.value()) {
					if(s.equals(methodName)) {
						try {
							method.invoke(testObject);
						} catch(Exception e) {
							return false;
						}
					}
				}
			}
		}
		return true;
	}
	
//	private static Object backupObject(Class<?> testClass, Object testObject) {
//		
//	}
	
	private static void invokeAfterMethods(String methodName, Class<?> testClass, Object testObject) {
		for(Method method : testClass.getMethods()) {
			if(method.isAnnotationPresent(OOPAfter.class)) {
				OOPAfter annotation = method.getAnnotation(OOPAfter.class);
				
				for(String s : annotation.value()) {
					if(s.equals(methodName)) {
						try {
							method.invoke(testObject);
						} catch(Exception e) {}
					}
				}
			}
		}
	}

	private static void invokeMethodByAnnotation(Class<?> testClass, Object testObject,
			Class<? extends Annotation> annotationType) {
		for(Method method : testClass.getMethods()) {
			if(method.isAnnotationPresent(annotationType)) {
				try {
					method.invoke(testObject);
				} catch (Exception e) {}
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
	
}