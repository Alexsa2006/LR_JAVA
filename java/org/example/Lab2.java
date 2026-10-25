import java.util.Scanner;
import java.util.Random;

public class Lab2 {

    // Константы диапазона случайных чисел
    private static final int RANDOM_MIN = 1;
    private static final int RANDOM_MAX = 100;

    // Максимальный размер матрицы
    private static final int MAX_SIZE = 20;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Лабораторная работа №2 ===");
        System.out.println("Работа с матрицей целых чисел");

        // Ввод размеров матрицы
        int rows = readSize(scanner, "Введите высоту матрицы (1-20): ");
        int columns = readSize(scanner, "Введите ширину матрицы (1-20): ");

        int[][] matrix = new int[rows][columns];

        // Выбор способа заполнения
        int choice = readChoice(scanner);

        if (choice == 1) {
            fillManually(matrix, scanner);
        } else {
            fillRandomly(matrix);
        }

        // Вывод матрицы
        System.out.println("\nПолученная матрица:");
        printMatrix(matrix);

        // Поиск минимального и максимального элемента
        int min = findMin(matrix);
        int max = findMax(matrix);

        // Среднее арифметическое
        double arithmeticMean = calculateArithmeticMean(matrix);

        // Среднее геометрическое
        double geometricMean = calculateGeometricMean(matrix);

        // Вывод результатов
        System.out.println("\nРезультаты:");
        System.out.println("Минимальный элемент: " + min);
        System.out.println("Максимальный элемент: " + max);
        System.out.printf("Среднее арифметическое: %.2f%n", arithmeticMean);

        if (geometricMean >= 0) {
            System.out.printf("Среднее геометрическое: %.2f%n", geometricMean);
        } else {
            System.out.println("Среднее геометрическое невозможно вычислить.");
            System.out.println("Для него все элементы матрицы должны быть положительными.");
        }

        scanner.close();
    }

    // Ввод размера матрицы
    public static int readSize(Scanner scanner, String message) {
        int size;

        do {
            System.out.print(message);
            size = scanner.nextInt();

            if (size < 1 || size > MAX_SIZE) {
                System.out.println("Ошибка! Размер должен быть от 1 до 20.");
            }

        } while (size < 1 || size > MAX_SIZE);

        return size;
    }

    // Выбор способа заполнения
    public static int readChoice(Scanner scanner) {
        int choice;

        do {
            System.out.println("\nВыберите способ создания матрицы:");
            System.out.println("1 - Ввести элементы с клавиатуры");
            System.out.println("2 - Заполнить случайными числами");

            System.out.print("Ваш выбор: ");
            choice = scanner.nextInt();

            if (choice != 1 && choice != 2) {
                System.out.println("Ошибка! Выберите 1 или 2.");
            }

        } while (choice != 1 && choice != 2);

        return choice;
    }

    // Заполнение матрицы вручную
    public static void fillManually(int[][] matrix, Scanner scanner) {
        System.out.println("\nВведите элементы матрицы:");

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.print("Элемент [" + i + "][" + j + "]: ");
                matrix[i][j] = scanner.nextInt();
            }
        }
    }

    // Заполнение матрицы случайными числами
    public static void fillRandomly(int[][] matrix) {
        Random random = new Random();

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                matrix[i][j] = random.nextInt(
                        RANDOM_MAX - RANDOM_MIN + 1
                ) + RANDOM_MIN;
            }
        }
    }

    // Вывод матрицы
    public static void printMatrix(int[][] matrix) {
        for (int[] row : matrix) {
            for (int element : row) {
                System.out.printf("%5d", element);
            }
            System.out.println();
        }
    }

    // Поиск минимального элемента
    public static int findMin(int[][] matrix) {
        int min = matrix[0][0];

        for (int[] row : matrix) {
            for (int element : row) {
                if (element < min) {
                    min = element;
                }
            }
        }

        return min;
    }

    // Поиск максимального элемента
    public static int findMax(int[][] matrix) {
        int max = matrix[0][0];

        for (int[] row : matrix) {
            for (int element : row) {
                if (element > max) {
                    max = element;
                }
            }
        }

        return max;
    }

    // Расчет среднего арифметического
    public static double calculateArithmeticMean(int[][] matrix) {
        long sum = 0;
        int count = 0;

        for (int[] row : matrix) {
            for (int element : row) {
                sum += element;
                count++;
            }
        }

        return (double) sum / count;
    }

    // Расчет среднего геометрического
    public static double calculateGeometricMean(int[][] matrix) {
        double sumLogarithms = 0;
        int count = 0;

        for (int[] row : matrix) {
            for (int element : row) {

                // Среднее геометрическое для отрицательных
                // и нулевых значений в обычном виде не вычисляется
                if (element <= 0) {
                    return -1;
                }

                sumLogarithms += Math.log(element);
                count++;
            }
        }

        return Math.exp(sumLogarithms / count);
    }
}
