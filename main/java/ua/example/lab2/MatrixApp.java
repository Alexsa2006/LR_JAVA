package ua.example.lab2;

import java.util.OptionalDouble;
import java.util.Random;
import java.util.Scanner;

public class MatrixApp {
    private static final int MAX_SIZE = 20;
    private static final int RANDOM_MIN = 1;
    private static final int RANDOM_MAX = 100;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int rows = readSize(scanner, "Введіть висоту матриці: ");
        int columns = readSize(scanner, "Введіть ширину матриці: ");
        int[][] matrix = createMatrix(scanner, rows, columns);

        printMatrix(matrix);
        System.out.println("Мінімум: " + findMin(matrix));
        System.out.println("Максимум: " + findMax(matrix));
        System.out.printf("Середнє арифметичне: %.2f%n", calculateArithmeticAverage(matrix));

        OptionalDouble geometricAverage = calculateGeometricAverage(matrix);
        if (geometricAverage.isPresent()) {
            System.out.printf("Середнє геометричне: %.2f%n", geometricAverage.getAsDouble());
        } else {
            System.out.println("Середнє геометричне не визначене для матриць із від'ємними значеннями.");
        }
    }

    private static int readSize(Scanner scanner, String message) {
        while (true) {
            System.out.print(message);
            int value = readInt(scanner);
            if (value >= 1 && value <= MAX_SIZE) {
                return value;
            }
            System.out.println("Розмір має бути від 1 до " + MAX_SIZE + ".");
        }
    }

    private static int[][] createMatrix(Scanner scanner, int rows, int columns) {
        System.out.print("Заповнити матрицю вручну? Введіть так або ні: ");
        String answer = scanner.next().trim().toLowerCase();

        if ("так".equals(answer) || "т".equals(answer)
                || "yes".equals(answer) || "y".equals(answer)) {
            return readMatrix(scanner, rows, columns);
        }
        return createRandomMatrix(rows, columns);
    }

    private static int[][] readMatrix(Scanner scanner, int rows, int columns) {
        int[][] matrix = new int[rows][columns];
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                System.out.print("Матриця[" + row + "][" + column + "] = ");
                matrix[row][column] = readInt(scanner);
            }
        }
        return matrix;
    }

    private static int[][] createRandomMatrix(int rows, int columns) {
        Random random = new Random();
        int[][] matrix = new int[rows][columns];
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                matrix[row][column] = RANDOM_MIN + random.nextInt(RANDOM_MAX - RANDOM_MIN + 1);
            }
        }
        return matrix;
    }

    private static int readInt(Scanner scanner) {
        while (!scanner.hasNextInt()) {
            System.out.print("Введіть ціле число: ");
            scanner.next();
        }
        return scanner.nextInt();
    }

    public static int findMin(int[][] matrix) {
        int min = matrix[0][0];
        for (int[] row : matrix) {
            for (int value : row) {
                if (value < min) {
                    min = value;
                }
            }
        }
        return min;
    }

    public static int findMax(int[][] matrix) {
        int max = matrix[0][0];
        for (int[] row : matrix) {
            for (int value : row) {
                if (value > max) {
                    max = value;
                }
            }
        }
        return max;
    }

    public static double calculateArithmeticAverage(int[][] matrix) {
        int count = 0;
        long sum = 0;
        for (int[] row : matrix) {
            for (int value : row) {
                sum += value;
                count++;
            }
        }
        return (double) sum / count;
    }

    public static OptionalDouble calculateGeometricAverage(int[][] matrix) {
        int count = 0;
        double logSum = 0.0;
        for (int[] row : matrix) {
            for (int value : row) {
                if (value < 0) {
                    return OptionalDouble.empty();
                }
                if (value == 0) {
                    return OptionalDouble.of(0.0);
                }
                logSum += Math.log(value);
                count++;
            }
        }
        return OptionalDouble.of(Math.exp(logSum / count));
    }

    private static void printMatrix(int[][] matrix) {
        System.out.println("Матриця:");
        for (int[] row : matrix) {
            for (int value : row) {
                System.out.printf("%5d", value);
            }
            System.out.println();
        }
    }
}
