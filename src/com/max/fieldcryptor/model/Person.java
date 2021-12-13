package com.max.fieldcryptor.model;

import com.max.fieldcryptor.annot.FieldCrypto;

public class Person {

    @FieldCrypto
    private String name;

    private int age;

    public Person() {}

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return "TestPerson{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
