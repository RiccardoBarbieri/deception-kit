package com.deceptionkit.mockaroo;

public class CsvType {

    private String name;
    private String value;
    private Boolean checked;

    public CsvType() {
    }

    public CsvType(String name, String value, Boolean checked) {
        this.name = name;
        this.value = value;
        this.checked = checked;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    @Override
    public String toString() {
        return "CsvType{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                ", checked=" + checked +
                '}';
    }
}
