import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidaPasswords {

    private static final String LOG_FILE = "password_log.txt";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Ingrese las contraseñas a validar (ingrese 'exit' para salir):");

        ExecutorService executorService = Executors.newCachedThreadPool();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE, true))) {
            while (true) {
                String password = scanner.nextLine();
                if (password.equalsIgnoreCase("exit")) {
                    break;
                }

                // Lanzar un nuevo hilo para validar la contraseña
                executorService.execute(() -> validatePassword(password, writer));
            }
        } catch (IOException e) {
            System.err.println("Error al escribir en el archivo de registro: " + e.getMessage());
        }

        // Apagar el ExecutorService
        executorService.shutdown();

        try {
            // Esperar a que todos los hilos terminen
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            System.err.println("Error al esperar la finalización de los hilos: " + e.getMessage());
        }

        System.out.println("Programa finalizado.");
    }

    private static void validatePassword(String password, BufferedWriter writer) {
        try {
            Pattern pattern = Pattern.compile("^(?=.*[a-z].*[a-z].*[a-z])(?=.*[A-Z].*[A-Z])(?=.*\\d)(?=.*[!@#$%^&+=]).{8,}$");
            Matcher matcher = pattern.matcher(password);
            if (matcher.matches()) {
                String validationMessage = "La contraseña '" + password + "' es válida.\n";
                System.out.println(validationMessage);
                writer.write(validationMessage);
            } else {
                String validationMessage = "La contraseña '" + password + "' no cumple con los requisitos.\n";
                System.out.println(validationMessage);
                writer.write(validationMessage);
            }
        } catch (IOException e) {
            System.err.println("Error al escribir en el archivo de registro: " + e.getMessage());
        }
    }
}
