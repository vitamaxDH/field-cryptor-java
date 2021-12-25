package com.max.fieldcryptor;

import com.max.fieldcryptor.annot.FieldCrypto;

import java.util.Objects;

@FieldCrypto
public class Car {

    private String vendor;

    private String designer;

    @FieldCrypto(exclude = true)
    private String madeIn;

    private int numberOfWheels;

    public Car() {}

    public Car(String vendor, String designer, String madeIn, int numberOfWheels) {
        this.vendor = vendor;
        this.designer = designer;
        this.madeIn = madeIn;
        this.numberOfWheels = numberOfWheels;
    }

    public String getVendor() {
        return vendor;
    }

    public String getDesigner() {
        return designer;
    }

    public String getMadeIn() {
        return madeIn;
    }

    public int getNumberOfWheels() {
        return numberOfWheels;
    }

    @Override
    public String toString() {
        return "Car{" +
                "vendor='" + vendor + '\'' +
                ", designer='" + designer + '\'' +
                ", madeIn='" + madeIn + '\'' +
                ", numberOfWheels=" + numberOfWheels +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return numberOfWheels == car.numberOfWheels && Objects.equals(vendor, car.vendor) && Objects.equals(designer, car.designer) && Objects.equals(madeIn, car.madeIn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vendor, designer, madeIn, numberOfWheels);
    }
}
