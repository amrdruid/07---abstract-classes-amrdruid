package com.redi.j2.utils;

import java.lang.reflect.*;

import static java.lang.Class.forName;

public abstract class ReflectionProxy {

    private Object target;

    public ReflectionProxy(Object... args) {

        this.target = instantiateTarget(args);
    }

    public ReflectionProxy(Object target) {
        this.target = target;
    }


    public abstract String getTargetClassName();


    public Object getTarget() {
        return target;
    }

    public boolean existsClass() {
        return getTargetClass() != null;
    }

    public boolean implementsInterface(Class<?> anInterface) {
        Class<?> targetClass = getTargetClass();
        if (targetClass == null || anInterface == null) {
            return false;
        }
        return anInterface.isAssignableFrom(targetClass);
    }

    public boolean hasProperty(String name) {
        Class<?> targetClass = getTargetClass();
        if (targetClass == null || name == null) {
            return false;
        }
        try {
            Field f = targetClass.getDeclaredField(name);
            return f != null;
        } catch (NoSuchFieldException e) {
            return false;
        }
    }

    public boolean isPropertyOfType(String name, Class<?> type) {
        return isPropertyOfType(name, type, null);
    }

    public boolean isPropertyOfType(String name, Class<?> type, Class<?> parameterizedType) {
        Class<?> targetClass = getTargetClass();
        if (targetClass == null || name == null || type == null) {
            return false;
        }
        try {
            Field f = targetClass.getDeclaredField(name);
            if(!f.getType().equals(type)) {
                return false;
            }
            if(parameterizedType == null) {
                return true;
            }
            if(! (f.getGenericType() instanceof ParameterizedType)) {
                return false;
            }
            ParameterizedType pType = (ParameterizedType)f.getGenericType();
            return pType.getActualTypeArguments()[0].equals(parameterizedType);

        } catch (NoSuchFieldException e) {
            return false;
        }
    }

    public boolean isPropertyPrivate(String name) {
        Class<?> targetClass = getTargetClass();
        if (targetClass == null || name == null) {
            return false;
        }
        try {
            Field f = targetClass.getDeclaredField(name);
            return Modifier.isPrivate(f.getModifiers());
        } catch (NoSuchFieldException e) {
            return false;
        }
    }

    public boolean hasMethod(String name, Class<?>... parameterTypes) {
        Class<?> targetClass = getTargetClass();
        if (targetClass == null || name == null) {
            return false;
        }
        try {
            Method m = targetClass.getDeclaredMethod(name, parameterTypes);
            return m != null;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }

    public boolean isMethodReturnType(Class<?> returnType, String name, Class<?>... parameterTypes) {
        return isMethodReturnType(returnType, null, name, parameterTypes);
    }

    public boolean isMethodReturnType(Class<?> returnType, Class<?> parameterizedType, String name, Class<?>... parameterTypes) {
        Class<?> targetClass = getTargetClass();
        if (targetClass == null || name == null) {
            return false;
        }
        try {
            Method m = targetClass.getDeclaredMethod(name, parameterTypes);
            if(!m.getReturnType().equals(returnType)) {
                return false;
            }
            if(parameterizedType == null) {
                return true;
            }
            if(! (m.getGenericReturnType() instanceof ParameterizedType)) {
                return false;
            }
            ParameterizedType pType = (ParameterizedType)m.getGenericReturnType();
            return pType.getActualTypeArguments()[0].equals(parameterizedType);
        } catch (NoSuchMethodException e) {
            return false;
        }
    }

    public boolean isMethodPublic(String name, Class<?>... parameterTypes) {
        Class<?> targetClass = getTargetClass();
        if (targetClass == null || name == null) {
            return false;
        }
        try {
            Method m = targetClass.getDeclaredMethod(name, parameterTypes);
            return Modifier.isPublic(m.getModifiers());
        } catch (NoSuchMethodException e) {
            return false;
        }
    }

    public boolean isMethodStatic(String name, Class<?>... parameterTypes) {
        Class<?> targetClass = getTargetClass();
        if (targetClass == null || name == null) {
            return false;
        }
        try {
            Method m = targetClass.getDeclaredMethod(name, parameterTypes);
            return Modifier.isStatic(m.getModifiers());
        } catch (NoSuchMethodException e) {
            return false;
        }
    }

    public boolean hasConstructor(Class<?>... parameterTypes) {
        Class<?> targetClass = getTargetClass();
        if (targetClass == null) {
            return false;
        }
        try {
            Constructor c = targetClass.getDeclaredConstructor(parameterTypes);
            return c != null;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }

    public boolean isConstructorPublic(Class<?>... parameterTypes) {
        Class<?> targetClass = getTargetClass();
        if (targetClass == null) {
            return false;
        }
        try {
            Constructor c = targetClass.getDeclaredConstructor(parameterTypes);
            return Modifier.isPublic(c.getModifiers());
        } catch (NoSuchMethodException e) {
            return false;
        }
    }

    public boolean equals(Object obj) {
        if (target == null || !(obj instanceof ReflectionProxy)) {
            return false;
        }
        try {
            Method method = target.getClass().getMethod("equals", Object.class);
            method.setAccessible(true);
            return (boolean) method.invoke(target, ((ReflectionProxy)obj).getTarget());
        } catch (Exception e) {
            return false;
        }
    }

    public int hashCode() {
        if (target == null) {
            return 0;
        }
        try {
            Method method = target.getClass().getMethod("hashCode");
            method.setAccessible(true);
            return (int) method.invoke(target);
        } catch (Exception e) {
            return 0;
        }
    }

    public String toString() {
        return invokeMethod("toString", new Class[]{ });
    }



    protected <T> T getPropertyValue(String propertyName) {
        if (target == null || !hasProperty(propertyName)) {
            return null;
        }
        try {
            Field field = target.getClass().getDeclaredField(propertyName);
            field.setAccessible(true);
            return (T) field.get(target);
        } catch (Exception e) {
            return null;
        }
    }

    protected  <T> T invokeMethod(String methodName, Class<?>[] parameterTypes, Object... parameterValues) {
        if (target == null) {
            return null;
        }
        try {
            // getDeclaredMethod is used to get protected/private methods
            Method method = target.getClass().getDeclaredMethod(methodName, parameterTypes);
            method.setAccessible(true);
            return (T) method.invoke(target, parameterValues);
        } catch (NoSuchMethodException e) {
            try {
                // try getting it from parent class, but only public methods will work
                Method method = target.getClass().getMethod(methodName, parameterTypes);
                method.setAccessible(true);
                return (T) method.invoke(target, parameterValues);
            } catch (Exception ex) {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    public Class<?> getTargetClass() {
        try {
            return forName(this.getTargetClassName());
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    private Constructor<?>[] getAllConstructors() {
        Class<?> targetClass = getTargetClass();
        if(targetClass == null) {
            return new Constructor[]{};
        }
        return targetClass.getConstructors();
    }

    private Object instantiateTarget(Object... args) {
        Class<?> targetClass = getTargetClass();
        if (targetClass == null) {
            return null;
        }
        Constructor[] constructors = getAllConstructors();
        for (Constructor<?> c : constructors) {
            if (c.getParameterCount() == args.length) {
                try {
                    return c.newInstance(args);
                } catch (Exception e) {
                    // do nothing;
                }
            }
        }
        return null;
    }

    public boolean isAbstract() {
        Class<?> targetClass = getTargetClass();
        if (targetClass == null) {
            return false;
        }
        return Modifier.isAbstract(targetClass.getModifiers());
    }

    public boolean extendsClass(String className) {
        Class<?> targetClass = getTargetClass();
        if (targetClass == null) {
            return false;
        }
        try {
            Class<?> parentClass = Class.forName(className);
            return parentClass.isAssignableFrom(targetClass);
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public boolean isInterface() {
        Class<?> targetClass = getTargetClass();
        if (targetClass == null) {
            return false;
        }
        return targetClass.isInterface();
    }

    public boolean isMethodAbstract(String name, Class<?>... parameterTypes) {
        Class<?> targetClass = getTargetClass();
        if (targetClass == null || name == null) {
            return false;
        }
        try {
            Method m = targetClass.getDeclaredMethod(name, parameterTypes);
            return Modifier.isAbstract(m.getModifiers());
        } catch (NoSuchMethodException e) {
            return false;
        }
    }

    public boolean isMethodProtected(String name, Class<?>... parameterTypes) {
        Class<?> targetClass = getTargetClass();
        if (targetClass == null || name == null) {
            return false;
        }
        try {
            Method m = targetClass.getDeclaredMethod(name, parameterTypes);
            return Modifier.isProtected(m.getModifiers());
        } catch (NoSuchMethodException e) {
            return false;
        }
    }

    public boolean isMethodParameterFromGenericType(String methodName, Class<?>[] parameterTypes, Class<?>[] parameterizedTypes)
    {
        Class<?> targetClass = getTargetClass();
        if (targetClass == null || methodName == null || parameterTypes.length != parameterizedTypes.length) {
            System.out.println("Wrong input for isMethodParameterFromGenericType");
            return false;
        }
        try {
            Method m = targetClass.getDeclaredMethod(methodName, parameterTypes);
            for (int i = 0; i < parameterizedTypes.length; i++) {
                if (parameterizedTypes[i] == null) {
                    continue;
                }
                if(! (m.getGenericParameterTypes()[i] instanceof ParameterizedType)) {
                    return false;
                }
                ParameterizedType pType = (ParameterizedType)m.getGenericParameterTypes()[i];
                if (! pType.getActualTypeArguments()[0].equals(parameterizedTypes[i])) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}