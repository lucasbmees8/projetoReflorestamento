package com.example.reflorestamentoproject.padrao;

public class Property {
    private String property;
    private String value;

    public Property(String property, String value) {
        this.property = property;
        this.value = value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getProperty() {
        return property;
    }

    public String getValue() {
        return value;
    }
}