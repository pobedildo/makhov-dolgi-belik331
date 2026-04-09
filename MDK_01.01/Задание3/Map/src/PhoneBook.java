import java.util.*;

public class PhoneBook {
    private final Map<String, String> nameToPhone = new HashMap<>();
    private final Map<String, String> phoneToName = new HashMap<>();

    public static void main(String[] args) {
        PhoneBook phoneBook = new PhoneBook();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Телефонная книга. Введите имя, номер, LIST или EXIT:");

        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("EXIT")) break;

            if (input.equalsIgnoreCase("LIST")) {
                phoneBook.listAll();
                continue;
            }

            // Проверяем, является ли ввод номером (простая проверка: содержит только цифры, +, -, пробелы)
            if (isPhoneNumber(input)) {
                phoneBook.processByPhone(input);
            } else {
                phoneBook.processByName(input);
            }
        }
        scanner.close();
    }

    // Простейшая проверка на номер: после удаления разрешённых символов остаются только цифры
    private static boolean isPhoneNumber(String s) {
        String cleaned = s.replaceAll("[\\s\\-+]", "");
        return cleaned.matches("\\d+");
    }

    private void processByName(String name) {
        if (nameToPhone.containsKey(name)) {
            // Существующее имя – выводим информацию
            String phone = nameToPhone.get(name);
            System.out.println("Имя: " + name + ", Телефон: " + phone);
        } else {
            // Новое имя – просим ввести номер
            System.out.print("Введите номер телефона для " + name + ": ");
            Scanner sc = new Scanner(System.in);
            String phone = sc.nextLine().trim();
            if (!isPhoneNumber(phone)) {
                System.out.println("Некорректный номер. Операция отменена.");
                return;
            }
            // Проверяем, не занят ли номер
            if (phoneToName.containsKey(phone)) {
                System.out.println("Этот номер уже принадлежит " + phoneToName.get(phone));
                return;
            }
            nameToPhone.put(name, phone);
            phoneToName.put(phone, name);
            System.out.println("Контакт добавлен.");
        }
    }

    private void processByPhone(String phone) {
        if (phoneToName.containsKey(phone)) {
            // Существующий номер – выводим информацию
            String name = phoneToName.get(phone);
            System.out.println("Телефон: " + phone + ", Имя: " + name);
        } else {
            // Новый номер – просим ввести имя
            System.out.print("Введите имя для номера " + phone + ": ");
            Scanner sc = new Scanner(System.in);
            String name = sc.nextLine().trim();
            if (name.isEmpty()) {
                System.out.println("Имя не может быть пустым.");
                return;
            }
            // Проверяем, не занято ли имя
            if (nameToPhone.containsKey(name)) {
                System.out.println("Это имя уже используется для номера " + nameToPhone.get(name));
                return;
            }
            nameToPhone.put(name, phone);
            phoneToName.put(phone, name);
            System.out.println("Контакт добавлен.");
        }
    }

    private void listAll() {
        if (nameToPhone.isEmpty()) {
            System.out.println("Телефонная книга пуста.");
            return;
        }
        // Сортируем имена по алфавиту
        List<String> sortedNames = new ArrayList<>(nameToPhone.keySet());
        Collections.sort(sortedNames);
        System.out.println("Абоненты:");
        for (String name : sortedNames) {
            System.out.println(name + " : " + nameToPhone.get(name));
        }
    }
}