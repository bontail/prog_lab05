package main.java.dev.bontail.lab5.validator;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;


/**
 * Class for checking fields in class which have @Check
 * Uses reflection
 * Validate args which are String
 * Can check one Field by time
 */
public class Validator {
    public final Class<?> cls;
    private final boolean checkOneFieldByTime;
    private final ArrayList<Field> fields = new ArrayList<>();
    private int currentFieldIndex = 0;

    public Validator(Class<?> cls, boolean checkOneFieldByTime) {
        this.cls = cls;
        this.checkOneFieldByTime = checkOneFieldByTime;
        this.fields.addAll(this.getFieldsWithCheck());
    }

    public ArrayList<Field> getFieldsWithCheck(){
        ArrayList<Field> fields = new ArrayList<>();
        for (Field field : this.cls.getDeclaredFields()) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(Check.class)) {
                fields.add(field);
            }
        }
        return fields;
    }

    public String getCurrentFieldName() {
        if (this.checkOneFieldByTime) {
            if (this.currentFieldIndex < this.fields.size()) {
                return this.fields.get(this.currentFieldIndex).getName();
            }
        }
        return null;
    }

    public ArrayList<Field> getNestedFields() {
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
        if (this.checkOneFieldByTime) {
            fields.add(this.fields.get(this.currentFieldIndex));
        } else {
            return this.fields;
        }
        return fields;
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
            Method valueOf;
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
        Check annotation = field.getAnnotation(Check.class);

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
        if (castedValue == null) {
            return !annotation.notNull();
        }
        if (annotation.notEmptyString() && ((String) castedValue).isEmpty()){
            return false;
        }
        if (field.getType().isEnum() || field.getType().getSimpleName().equals("String")) {
            return true;
        }
        try {
            Method method = Check.class.getDeclaredMethod("min" + castedValue.getClass().getSimpleName());
            method.setAccessible(true);
            Object minValue = method.invoke(annotation);
            if (!validateMinValue(minValue, castedValue, field.getType())) {
                return false;
            }

            method = Check.class.getDeclaredMethod("max" + castedValue.getClass().getSimpleName());
            method.setAccessible(true);
            Object maxValue = method.invoke(annotation);
            if (!validateMaxValue(maxValue, castedValue, field.getType())) {
                return false;
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            return false;
        }
        return true;
    }

    public boolean validateMinValue(Object minValue, Object currentValue, Class<?> fieldType){
        if (fieldType.equals(Float.class)) {
            return (Float) minValue <= (Float) currentValue;
        } else if (fieldType.equals(Double.class)) {
            return (Double) minValue <= (Double) currentValue;
        } else if (fieldType.equals(Long.class)) {
            return (Long) minValue <= (Long) currentValue;
        } else if (fieldType.equals(Integer.class)) {
            return (Integer) minValue <= (Integer) currentValue;
        } else if (fieldType.equals(Short.class)) {
            return (Short) minValue <= (Short) currentValue;
        } else if (fieldType.equals(Byte.class)) {
            return (Byte) minValue <= (Byte) currentValue;
        } else {
            throw new IllegalArgumentException("Invalid field type: " + fieldType.getName());
        }
    }

    public boolean validateMaxValue(Object maxValue, Object currentValue, Class<?> fieldType){
        if (fieldType.equals(Float.class)) {
            return (Float) maxValue >= (Float) currentValue;
        } else if (fieldType.equals(Double.class)) {
            return (Double) maxValue >= (Double) currentValue;
        } else if (fieldType.equals(Long.class)) {
            return (Long) maxValue >= (Long) currentValue;
        } else if (fieldType.equals(Integer.class)) {
            return (Integer) maxValue >= (Integer) currentValue;
        } else if (fieldType.equals(Short.class)) {
            return (Short) maxValue >= (Short) currentValue;
        } else if (fieldType.equals(Byte.class)) {
            return (Byte) maxValue >= (Byte) currentValue;
        } else {
            throw new IllegalArgumentException("Invalid field type: " + fieldType.getName());
        }
    }

    public InvalidField checkArgs(String[] args) {
        ArrayList<Field> fields = this.getCheckedFields();
        int argIndex = 0;
        for (Field field : fields) {
            boolean isValidArg = this.validateArg(args[argIndex], field);
            if (!isValidArg) {
                return new InvalidField(argIndex, field.getName());
            }
            argIndex++;
        }
        this.currentFieldIndex++;
        return null;
    }

    private Object[] castArgs(Object[] args){
        Object[] castedArgs = new Object[args.length];
        int i = 0;
        try {
            for (Field field: this.fields){
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
            for (Field field : this.fields) {
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