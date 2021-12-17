package com.max.fieldcryptor.model;

import com.max.fieldcryptor.annot.FieldCrypto;

public class Person {

    @FieldCrypto
    private String name;

    private String address;

    @FieldCrypto
    private int age;

    public Person() {
    }

    public Person(String name, String address, int age) {
        this.name = name;
        this.address = address;
        this.age = age;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", age=" + age +
                '}';
    }

    public void setName(String name) {
        this.name = name;
    }
}
