package com.example;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Users {
    private final List<User> userList = new ArrayList<>();
    private int nextId = 1;

    // Добавление пользователя
    public void addUser(String name, int age, User.Gender gender) {
        userList.add(new User(nextId++, name, age, gender));
    }

    // Вывод всех пользователей в консоль
    public void printAllUsers() {
        userList.forEach(System.out::println);
    }

    // Получить список всех пользователей (для тестов)
    public List<User> getAllUsers() {
        return new ArrayList<>(userList);
    }

    // Фильтр по полу: мужчины
    public List<User> getMaleUsers() {
        return userList.stream()
                .filter(u -> u.getGender() == User.Gender.MALE)
                .collect(Collectors.toList());
    }

    // Фильтр по полу: женщины
    public List<User> getFemaleUsers() {
        return userList.stream()
                .filter(u -> u.getGender() == User.Gender.FEMALE)
                .collect(Collectors.toList());
    }

    // Общее количество пользователей
    public int getTotalCount() {
        return userList.size();
    }

    // Средний возраст
    public double getAverageAge() {
        return userList.stream()
                .mapToInt(User::getAge)
                .average()
                .orElse(0.0);
    }
}