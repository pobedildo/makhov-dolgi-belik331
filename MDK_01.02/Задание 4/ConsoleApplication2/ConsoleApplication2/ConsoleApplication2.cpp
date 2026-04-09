#include <iostream>
#include <limits>
#include "calculator.h"

using namespace std;

void printMenu() {
    cout << "\n=== КАЛЬКУЛЯТОР ===\n";
    cout << "1. Сложение (+)\n";
    cout << "2. Вычитание (-)\n";
    cout << "3. Умножение (*)\n";
    cout << "4. Деление (/)\n";
    cout << "5. Возведение в степень (^)\n";
    cout << "6. Квадратный корень (√)\n";
    cout << "7. Факториал (!)\n";
    cout << "8. Сохранить в память (M+)\n";
    cout << "9. Извлечь из памяти (MR)\n";
    cout << "10. Добавить к памяти (M+ value)\n";
    cout << "11. Очистить память (MC)\n";
    cout << "0. Выход\n";
    cout << "Выберите операцию: ";
}

int main() {
    Calculator calc;
    int choice;
    double a, b, result;

    while (true) {
        printMenu();
        cin >> choice;

        if (choice == 0) break;

        try {
            switch (choice) {
            case 1:
                cout << "a + b = ?\nВведите a и b: ";
                cin >> a >> b;
                result = calc.add(a, b);
                cout << a << " + " << b << " = " << result << endl;
                break;
            case 2:
                cout << "a - b = ?\nВведите a и b: ";
                cin >> a >> b;
                result = calc.subtract(a, b);
                cout << a << " - " << b << " = " << result << endl;
                break;
            case 3:
                cout << "a * b = ?\nВведите a и b: ";
                cin >> a >> b;
                result = calc.multiply(a, b);
                cout << a << " * " << b << " = " << result << endl;
                break;
            case 4:
                cout << "a / b = ?\nВведите a и b: ";
                cin >> a >> b;
                result = calc.divide(a, b);
                cout << a << " / " << b << " = " << result << endl;
                break;
            case 5:
                cout << "base ^ exp = ?\nВведите основание и степень: ";
                cin >> a >> b;
                result = calc.power(a, b);
                cout << a << " ^ " << b << " = " << result << endl;
                break;
            case 6:
                cout << "√x = ?\nВведите x: ";
                cin >> a;
                result = calc.sqrt(a);
                cout << "√" << a << " = " << result << endl;
                break;
            case 7:
                cout << "n! = ?\nВведите целое неотрицательное n: ";
                int n;
                cin >> n;
                cout << n << "! = " << calc.factorial(n) << endl;
                break;
            case 8:
                cout << "Сохранить число в память.\nВведите число: ";
                cin >> a;
                calc.memorySave(a);
                cout << "Число " << a << " сохранено в памяти.\n";
                break;
            case 9:
                result = calc.memoryRecall();
                cout << "Число в памяти: " << result << endl;
                break;
            case 10:
                cout << "Добавить число к памяти.\nВведите число: ";
                cin >> a;
                calc.memoryAdd(a);
                cout << "К памяти прибавлено " << a << ". Теперь в памяти: " << calc.memoryRecall() << endl;
                break;
            case 11:
                calc.memoryClear();
                cout << "Память очищена.\n";
                break;
            default:
                cout << "Неверный выбор. Попробуйте снова.\n";
            }
        }
        catch (const exception& e) {
            cout << "Ошибка: " << e.what() << endl;
        }

        // Очистка потока ввода после возможного ошибочного ввода
        cin.clear();
        cin.ignore(numeric_limits<streamsize>::max(), '\n');
    }

    cout << "До свидания!\n";
    return 0;
}