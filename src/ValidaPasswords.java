import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class ValidaPasswords {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Ingrese las contraseñas a validar (ingrese 'exit' para salir):");

        ExecutorService executorService = Executors.newCachedThreadPool();

        while (true) {
            String password = scanner.nextLine();
            if (password.equalsIgnoreCase("exit")) {
                break;
            }

            executorService.execute(new PasswordValidationTask(password));
        }

        executorService.shutdown();

        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            System.err.println("Error al esperar la finalización de los hilos: " + e.getMessage());
        }

        System.out.println("Programa finalizado.");
    }

    static class PasswordValidationTask implements Runnable {
        private final String password;
        private final Pattern pattern;

        public PasswordValidationTask(String password) {
            this.password = password;
            pattern = Pattern.compile("^(?=.*[a-z].*[a-z].*[a-z])(?=.*[A-Z].*[A-Z])(?=.*\\d)(?=.*[!@#$%^&+=]).{8,}$");
        }

        @Override
        public void run() {
            Matcher matcher = pattern.matcher(password);
            if (matcher.matches()) {
                System.out.println("La contraseña '" + password + "' es válida.");
            } else {
                System.out.println("La contraseña '" + password + "' no cumple con los requisitos.");
            }
        }
    }
}