package com.mycompany.ejerciciospropuestos;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalTime;
import java.util.Random;

public class EjerciciosPropuestosServidor {

    public static void main(String[] args) {
        
        System.out.println("Directorio de trabajo actual: " + System.getProperty("user.dir"));
        
        int port = 1234;

        try (ServerSocket server = new ServerSocket(port)) {
            System.out.println("Servidor iniciado en el puerto " + port + ".");

            while (true) {
                Socket client = server.accept();
                System.out.println("Cliente conectado: " + client.getInetAddress());

                BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));
                PrintWriter output = new PrintWriter(client.getOutputStream(), true);

                String request = input.readLine();
                System.out.println("Solicitud del cliente: " + request);

                if (request == null) {
                    client.close();
                    continue;
                }

                // Echo Server
                if (request.startsWith("ECHO")) {
                    String message = request.substring(5);
                    output.println("Eco: " + message);
                }

                // Cálculo de operaciones matemáticas
                else if (request.matches("\\d+[\\+\\-\\*/]\\d+")) {
                    try {
                        String[] parts;
                        double result = 0;

                        if (request.contains("+")) {
                            parts = request.split("\\+");
                            result = Double.parseDouble(parts[0]) + Double.parseDouble(parts[1]);
                        } else if (request.contains("-")) {
                            parts = request.split("-");
                            result = Double.parseDouble(parts[0]) - Double.parseDouble(parts[1]);
                        } else if (request.contains("*")) {
                            parts = request.split("\\*");
                            result = Double.parseDouble(parts[0]) * Double.parseDouble(parts[1]);
                        } else if (request.contains("/")) {
                            parts = request.split("/");
                            result = Double.parseDouble(parts[0]) / Double.parseDouble(parts[1]);
                        }

                        output.println("Resultado: " + result);
                    } catch (Exception e) {
                        output.println("Error en la operación");
                    }
                }

                // Transmisión de archivos
                else if (request.startsWith("GET_FILE")) {
                    String fileName = request.substring(9).trim();
                    try {
                        Path filePath = Path.of(fileName);
                        if (Files.exists(filePath)) {
                            String content = Files.readString(filePath);
                            output.println(content);
                        } else {
                            output.println("Error: El archivo no existe");
                            System.out.println("Error: El archivo no existe en la ruta especificada");
                        }
                    } catch (Exception e) {
                        output.println("Error: No se pudo leer el archivo");
                        e.printStackTrace();
                    }
                }


                // Generación de frases aleatorias
                else if (request.equals("RANDOM_PHRASE")) {
                    String[] phrases = {
                        "La práctica hace al maestro.",
                        "No dejes para mañana lo que puedes hacer hoy.",
                        "El conocimiento es poder.",
                        "Nunca es tarde para aprender."
                    };
                    Random random = new Random();
                    output.println(phrases[random.nextInt(phrases.length)]);
                }

                // Hora del servidor
                else if (request.equals("TIME")) {
                    LocalTime time = LocalTime.now();
                    output.println("La hora actual es: " + time);
                }

                // Conversión de texto
                else if (request.startsWith("CONVERT")) {
                    String[] parts = request.split(" ", 3);
                    if (parts.length == 3) {
                        String operation = parts[1];
                        String text = parts[2];
                        switch (operation) {
                            case "UPPER":
                                output.println(text.toUpperCase());
                                break;
                            case "LOWER":
                                output.println(text.toLowerCase());
                                break;
                            default:
                                output.println("Operación desconocida");
                        }
                    } else {
                        output.println("Formato incorrecto. Uso: CONVERT [UPPER|LOWER] texto");
                    }
                }

                // Información del sistema
                else if (request.equals("INFO")) {
                    String hostName = client.getInetAddress().getHostName();
                    String hostAddress = client.getInetAddress().getHostAddress();
                    String os = System.getProperty("os.name");
                    output.println("Host: " + hostName + ", IP: " + hostAddress + ", OS: " + os);
                }

                // Solicitud no reconocida
                else {
                    output.println("Comando no reconocido");
                }

                client.close();
                System.out.println("Cliente desconectado.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}