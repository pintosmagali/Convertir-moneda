package com.alura.conversor;

import java.util.Scanner;

public class Principal {
    private static final Servicio servicio = new Servicio();
    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        int opcion;
        do {
            menu();
            opcion = leerInt("Elija una opción válida: ");
            switch (opcion) {
                case 1 -> convertir("USD", "ARS");
                case 2 -> convertir("ARS", "USD");
                case 3 -> convertir("USD", "BRL");
                case 4 -> convertir("BRL", "USD");
                case 5 -> convertir("USD", "COP");
                case 6 -> convertir("COP", "USD");
                case 7 -> System.out.println("¡Gracias por usar el Conversor!");
                default -> System.out.println("Opción no válida. Intente de nuevo.");
            }
        } while (opcion != 7);
    }

    private static void menu() {
        System.out.println("\n********************************************");
        System.out.println("Sea bienvenido/a al Conversor de Moneda =]");
        System.out.println("1) Dólar => Peso argentino");
        System.out.println("2) Peso argentino => Dólar");
        System.out.println("3) Dólar => Real brasileño");
        System.out.println("4) Real brasileño => Dólar");
        System.out.println("5) Dólar => Peso colombiano");
        System.out.println("6) Peso colombiano => Dólar");
        System.out.println("7) Salir");
        System.out.println("********************************************\n");
    }

    private static void convertir(String desde, String hacia) {
        double monto = leerDouble("Ingrese el valor que deseas convertir: ");
        try {
            double resultado = servicio.convertir(desde, hacia, monto);
            System.out.printf("El valor %.2f [%s] corresponde al valor final de >>> %.2f [%s]%n",
                    monto, desde, resultado, hacia);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static int leerInt(String msg) {
        System.out.print(msg + " ");
        while (!sc.hasNextInt()) { System.out.print("Número válido: "); sc.next(); }
        return sc.nextInt();
    }

    private static double leerDouble(String msg) {
        System.out.print(msg + " ");
        while (!sc.hasNextDouble()) { System.out.print("Número válido: "); sc.next(); }
        return sc.nextDouble();
    }
}
