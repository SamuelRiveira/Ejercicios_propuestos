package com.mycompany.ejerciciospropuestoscliente;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class EjerciciosPropuestosCliente {
    public static void main(String[] args) {
        String host = "localhost";
        int port = 1234;

        try (Socket socket = new Socket(host, port);
             PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            System.out.println("Conectado al servidor " + host + " en el puerto " + port + ".");
            Scanner scanner = new Scanner(System.in);
            boolean running = true;

            while (running) {
                System.out.println("Selecciona una opción:");
                System.out.println("1. Echo");
                System.out.println("2. Operaciones matemáticas");
                System.out.println("3. Transmisión de archivo");
                System.out.println("4. Frase aleatoria");
                System.out.println("5. Hora del servidor");
                System.out.println("6. Conversión de texto");
                System.out.println("7. Información del sistema");
                System.out.println("8. Salir");

                int choice = Integer.parseInt(scanner.nextLine());

                switch (choice) {
                    case 1:
                        System.out.print("Escribe un mensaje para eco: ");
                        String echoMessage = scanner.nextLine();
                        output.println("ECHO " + echoMessage);
                        System.out.println("Respuesta del servidor: " + input.readLine());
                        break;

                    case 2:
                        System.out.print("Escribe una operación matemática (e.g., 5+3): ");
                        String operation = scanner.nextLine();
                        output.println(operation);
                        System.out.println("Respuesta del servidor: " + input.readLine());
                        break;

                    case 3:
                        System.out.print("Escribe el nombre del archivo a solicitar: ");
                        String fileName = scanner.nextLine();
                        output.println("GET_FILE " + fileName);
                        System.out.println("Respuesta del servidor: ");
                        String line;
                        while ((line = input.readLine()) != null && !line.isEmpty()) {
                            System.out.println(line);
                        }
                        break;

                    case 4:
                        output.println("RANDOM_PHRASE");
                        System.out.println("Respuesta del servidor: " + input.readLine());
                        break;

                    case 5:
                        output.println("TIME");
                        System.out.println("Respuesta del servidor: " + input.readLine());
                        break;

                    case 6:
                        System.out.print("Escribe el texto a convertir: ");
                        String text = scanner.nextLine();
                        System.out.print("Escribe la operación (UPPER o LOWER): ");
                        String conversion = scanner.nextLine();
                        output.println("CONVERT " + conversion + " " + text);
                        System.out.println("Respuesta del servidor: " + input.readLine());
                        break;

                    case 7:
                        output.println("INFO");
                        System.out.println("Respuesta del servidor: " + input.readLine());
                        break;

                    case 8:
                        running = false;
                        break;

                    default:
                        System.out.println("Opción no válida. Intenta de nuevo.");
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
