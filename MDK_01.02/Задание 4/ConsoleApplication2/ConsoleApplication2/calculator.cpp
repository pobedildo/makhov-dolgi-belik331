#include "calculator.h"

Calculator::Calculator() : memory(0.0) {}

double Calculator::add(double a, double b) {
    return a + b;
}

double Calculator::subtract(double a, double b) {
    return a - b;
}

double Calculator::multiply(double a, double b) {
    return a * b;
}

double Calculator::divide(double a, double b) {
    if (std::fabs(b) < 1e-12)
        throw std::runtime_error("Деление на ноль невозможно!");
    return a / b;
}

double Calculator::power(double base, double exp) {
    return std::pow(base, exp);
}

double Calculator::sqrt(double x) {
    if (x < 0)
        throw std::runtime_error("Квадратный корень из отрицательного числа не определён!");
    return std::sqrt(x);
}

long long Calculator::factorial(int n) {
    if (n < 0)
        throw std::runtime_error("Факториал отрицательного числа не определён!");
    long long result = 1;
    for (int i = 2; i <= n; ++i)
        result *= i;
    return result;
}

void Calculator::memorySave(double value) {
    memory = value;
}

double Calculator::memoryRecall() {
    return memory;
}

void Calculator::memoryAdd(double value) {
    memory += value;
}

void Calculator::memoryClear() {
    memory = 0.0;
}