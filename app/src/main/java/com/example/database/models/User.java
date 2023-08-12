package com.example.database.models;

//модель данных пользовтаеля
//она нужна чтобы адаптировать данные с базы данных под упорядоченный id внутри списков, и для удобства получения определенных
//значений из них
public class User {
    int id;
    String name;
    int age;
    String description;

    //конструктор
    public User(int id, String name, int age, String description) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.description = description;
    }

    //методы гет и сэт
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
