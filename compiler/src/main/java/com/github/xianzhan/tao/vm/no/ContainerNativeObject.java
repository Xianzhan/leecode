package com.github.xianzhan.tao.vm.no;

import com.github.xianzhan.tao.vm.TaolanNativeObject;
import com.github.xianzhan.tao.vm.TaolanObject;
import com.github.xianzhan.tao.vm.Value;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Set;

/**
 * 描述：
 *
 * @author Lee
 * @since 2017/10/16
 */
public class ContainerNativeObject extends TaolanNativeObject {
    private static Constructor<? extends TaolanObject> classConstructor;

    {
        try {
            classConstructor = ContainerNativeObject.class.getConstructor(classConstructorParams);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private final HashMap<Object, Value> container = new HashMap<>();

    public ContainerNativeObject() {
        this(TaolanNativeObject.Object, null);
    }

    public ContainerNativeObject(TaolanObject prototype, TaolanObject constructor) {
        super(prototype, constructor);
    }

    @Override
    protected Constructor<? extends TaolanObject> getChildConstructor() {
        return classConstructor;
    }

    public Value setValue(Value key, Value value) {
        return container.put(convertToKey(key), value);
    }

    public Value getValue(Value key) {
        Value value = container.get(convertToKey(key));
        if (value == null) {
            value = Value.NULL;
        }
        return value;
    }

    public Value remove(Value key) {
        Value value = container.remove(convertToKey(key));
        if (value == null) {
            value = Value.NULL;
        }
        return value;
    }

    public Value length() {
        return new Value(container.size());
    }

    public Set<Object> allKeys() {
        return container.keySet();
    }

    private Object convertToKey(Value keyValue) {
        if (keyValue.type == Value.Type.Object) {
            return keyValue.objValue;
        } else {
            return keyValue.toString();
        }
    }
}
