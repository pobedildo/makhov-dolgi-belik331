package org.example;

/**
 * Демонстрация класса {@link Users}: список пользователей, фильтры по полу, средний возраст.
 */
public class Main {
    public static void main(String[] args) {
        Users users = new Users();

        users.addUser("Анна", 25, User.Gender.FEMALE);
        users.addUser("Иван", 30, User.Gender.MALE);
        users.addUser("Мария", 22, User.Gender.FEMALE);

        System.out.println("--- Все пользователи ---");
        users.printAllUsers();

        System.out.println("\n--- Только мужчины ---");
        users.getMaleUsers().forEach(System.out::println);

        System.out.println("\n--- Только женщины ---");
        users.getFemaleUsers().forEach(System.out::println);

        System.out.printf("%nВсего пользователей: %d%n", users.getTotalCount());
        System.out.printf("Средний возраст: %.2f%n", users.getAverageAge());
    }
}
