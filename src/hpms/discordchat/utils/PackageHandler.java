package hpms.discordchat.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.bukkit.Bukkit;

public final class PackageHandler {
	
	private static PackageStorage NMSPackage;
	private static PackageStorage OGCPackage;
	
	static {
			Class<?> OGCClass = Bukkit.getServer().getClass();
			OGCPackage = new PackageStorage(OGCClass.getPackage().getName());
			try {
				Method getHandle = OGCClass.getMethod("getHandle");
				NMSPackage = new PackageStorage(getHandle.getReturnType().getPackage().getName());
			}catch(NoSuchMethodException e) {
				e.printStackTrace();
			}
	}
	
	private PackageHandler() {}
	
	public static PackageStorage getNMSPackage() {
		return NMSPackage;
	}
	
	public static PackageStorage getOGCPackage() {
		return OGCPackage;
	}
	
	public static Class<?> getNMSClass(String className) {
		Class<?> clazz = getNMSPackage().getClass(className);
		return clazz;
	}
	
	public static Class<?> getOGCClass(String className) {
		Class<?> clazz = getOGCPackage().getClass(className);
		return clazz;
	}
	
	public static Object getField(Field f, Object object_to_get_from) {
		Object o = null;
		try {
			f.setAccessible(true);
			o = f.get(object_to_get_from);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return o;
			
	}
	public static Object invokeMethod(Method method,Object invoker,Object... args) {
		try {
			return method.invoke(invoker, args);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Constructor<?> getNMSConstructor(String className,Class<?>... params) {
		Constructor<?> constructor = null;
		Class<?> clazz = getNMSClass(className);
		try {
			constructor = clazz.getConstructor(params);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		return constructor;
	}
	
	public static Constructor<?> getOGCConstructor(String className,Class<?>... params) {
		Constructor<?> constructor = null;
		Class<?> clazz = getOGCClass(className);
		try {
			constructor = clazz.getConstructor(params);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		return constructor;
	}
	
	public static Method getNMSMethod(String className,String methodName,Class<?>... params) {
		Method method = null;
		Class<?> clazz = getNMSClass(className);
		try {
			method = clazz.getDeclaredMethod(methodName, params);
			method.setAccessible(true);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		return method;
	}
	
	public static Method getOGCMethod(String className,String methodName,Class<?>... params) {
		Method method = null;
		Class<?> clazz = getOGCClass(className);
		try {
			method = clazz.getDeclaredMethod(methodName, params);
			method.setAccessible(true);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		return method;
	}
	
}
