#ifndef CALCULATOR_H
#define CALCULATOR_H

#include <cmath>
#include <stdexcept>

class Calculator {
private:
    double memory;

public:
    Calculator();

    // Базовые операции
    double add(double a, double b);
    double subtract(double a, double b);
    double multiply(double a, double b);
    double divide(double a, double b);

    // Возведение в степень
    double power(double base, double exp);

    // Квадратный корень
    double sqrt(double x);

    // Факториал (только для целых неотрицательных чисел)
    long long factorial(int n);

    // Операции с памятью
    void memorySave(double value);
    double memoryRecall();
    void memoryAdd(double value);
    void memoryClear();
};

#endif