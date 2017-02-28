package cn.myapps.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class PrivateClass {
	public static void main(String[] args) throws NoSuchMethodException {

		try {

			Constructor<P> c = P.class.getDeclaredConstructor();
			c.setAccessible(true);

			P p = (P) c.newInstance();

		} catch (SecurityException e) {
			e.printStackTrace();
		} // java.lang.reflect.
		catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
}

class P {
	private P() {
		System.out.println("This is construct.");
	}
	
	public void sayHello() {
		System.out.println("Hello.");
	}
}