package com.lcc.minispring.beans;

import com.lcc.minispring.beans.PropertyValue;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class PropertyValues {

    private final List<PropertyValue> propertyValues;
    public PropertyValues() {
        this.propertyValues = new ArrayList<>();
    }

    public void addPropertyValue(PropertyValue propertyValue) {
        this.propertyValues.add(propertyValue);
    }

}
