package com.huawei.wireless.irecognition.model;

public class Person {

    private static final long serialVersionUID = 1L;

    private long id;

    private String name;

    private String surname;

    private boolean gender;

    private int age;

    private String image;

    public Person(){}

    public Person(long id, String image, String name, String surname, boolean gender, int age ){
        this.id = id;
        this.image = image;
        this.name = name;
        this.surname = surname;
        this.gender = gender;
        this.age = age;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public boolean getGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

