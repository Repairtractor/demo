package com.lcc.minispring.beans.factory;

public class PropertyValue {
    public final String name;
    public final Object value;

    public PropertyValue(String name, Object value) {
        this.name = name;
        this.value = value;
    }
}
