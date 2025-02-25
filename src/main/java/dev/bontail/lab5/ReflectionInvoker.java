package main.java.dev.bontail.lab5;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;


public class ReflectionInvoker {
    public final Class<?> cls;
    private final boolean checkOneFieldByTime;
    private final ArrayList<Field> fields = new ArrayList<>();
    private int currentFieldIndex = 0;

    public ReflectionInvoker(Class<?> cls, boolean checkOneFieldByTime) {
        this.cls = cls;
        this.checkOneFieldByTime = checkOneFieldByTime;
        if (checkOneFieldByTime) {
            this.fields.addAll(this.getCheckedFields());
        }
    }

    public InvalidField checkArg(String arg) {
        if (this.checkOneFieldByTime) {
            if (this.validateArg(arg, this.fields.get(this.currentFieldIndex))) {
                this.currentFieldIndex++;
                return null;
            } else {
                return new InvalidField(this.currentFieldIndex, this.fields.get(this.currentFieldIndex).getName());
            }
        }
        return null;
    }

    public String getCurrentFieldName() {
        if (this.checkOneFieldByTime) {
            if (this.currentFieldIndex < this.fields.size()) {
                return this.fields.get(this.currentFieldIndex).getName();
            }
        }
        return null;
    }

    private ArrayList<Field> getBuiltinFields() {
        ArrayList<Field> fields = new ArrayList<>();
        for (Field field : this.cls.getDeclaredFields()) {
            if (!field.getType().isEnum() && field.getType().getName().startsWith("main")) {
                continue;
            }
            fields.add(field);
        }
        return fields;
    }

    public ArrayList<Field> getRecursiveFields() {
        ArrayList<Field> fields = new ArrayList<>();
        for (Field field : this.cls.getDeclaredFields()) {
            if (!field.getType().getName().startsWith("main") || field.getType().isEnum()) {
                continue;
            }
            fields.add(field);
        }
        return fields;
    }

    public ArrayList<Field> getCheckedFields() {
        ArrayList<Field> fields = new ArrayList<>();
        for (Field field : this.getBuiltinFields()) {
            Method checker = this.getCheckerByField(field);
            if (checker != null) {
                fields.add(field);
            }
        }
        return fields;
    }

    public Method getCheckerByField(Field field) {
        String methodName = "check" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
        Method method = null;
        try {
            method = this.cls.getMethod(methodName, field.getType());
        } catch (SecurityException | NoSuchMethodException e) {
        }
        return method;
    }

    public Object castToFieldType(String value, Class<?> fieldType) throws InvocationTargetException, IllegalAccessException {
        if (value == null) {
            return null;
        }

        Object castedValue = null;

        if (fieldType.equals(Float.class)) {
            castedValue = Float.parseFloat(value);
        } else if (fieldType.equals(Double.class)) {
            castedValue = Double.parseDouble(value);
        } else if (fieldType.equals(Long.class)) {
            castedValue = Long.parseLong(value);
        } else if (fieldType.equals(Integer.class)) {
            castedValue = Integer.parseInt(value);
        } else if (fieldType.equals(Short.class)) {
            castedValue = Short.parseShort(value);
        } else if (fieldType.equals(Byte.class)) {
            castedValue = Byte.parseByte(value);
        } else if (fieldType.equals(Boolean.class)) {
            castedValue = Boolean.parseBoolean(value);
        }
        if (fieldType.equals(String.class)) {
            castedValue = value;
        }
        if (fieldType.isEnum()) {
            Method valueOf = null;
            try {
                valueOf = fieldType.getMethod("valueOf", String.class);
            } catch (NoSuchMethodException e) {
                return null;
            }
            valueOf.setAccessible(true);
            castedValue = valueOf.invoke(null, value);
        }
        return castedValue;
    }

    private boolean validateArg(String arg, Field field) {
        Method method = getCheckerByField(field);
        if (method == null) {
            return true;
        }

        Object castedValue = null;
        try {
            castedValue = this.castToFieldType(arg, field.getType());
        } catch (InvocationTargetException e) {
            if (e.getCause().getClass().equals(IllegalArgumentException.class)) {
                return false;
            }
        } catch (Throwable e) {
            return false;
        }
        try {
            return (boolean) method.invoke(null, castedValue);
        } catch (IllegalAccessException | InvocationTargetException e) {
            return false;
        }
    }

    public InvalidField checkArgs(String[] args) {
        ArrayList<Field> fields = this.getCheckedFields();
        int argIndex = 0;
        for (Field field : fields) {
            try {
                boolean isValidArg = this.validateArg(args[argIndex], field);
                if (!isValidArg) {
                    return new InvalidField(argIndex, field.getName());
                }
                argIndex++;
            } catch (IllegalArgumentException e) {
                return new InvalidField(argIndex, field.getName());
            }
        }

        return null;
    }

    private Object[] castArgs(Object[] args){
        Object[] castedArgs = new Object[args.length];
        int i = 0;
        try {
            for (Field field: this.getCheckedFields()){
                castedArgs[i] = this.castToFieldType((String) args[i], field.getType());
                i++;
            }
            for (; i < args.length; i++) {
                castedArgs[i] = args[i];
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        return castedArgs;
    }

    public Object createInstance(Object[] args, ArrayList<Class<?>> additionalTypes) {
        try {
            Object[] castedArgs = castArgs(args);
            Class<?>[] argTypes = new Class<?>[castedArgs.length];
            int i = 0;
            for (Field field : this.getCheckedFields()) {
                argTypes[i] = field.getType();
                i++;
            }
            for (Class<?> type: additionalTypes) {
                argTypes[i] = type;
                i++;
            }
            Constructor<?> constructor = this.cls.getDeclaredConstructor(argTypes);

            return constructor.newInstance(castedArgs);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}