import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Cliente {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private Scanner scanner;

    public Cliente(String host, int port) throws IOException {
        socket = new Socket(host, port);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        scanner = new Scanner(System.in);
    }

    public void jugar() throws IOException {
        System.out.println(in.readLine()); // Mensaje de bienvenida

        // El primer cliente escoge la palabra, el segundo intenta adivinar
        String role = in.readLine();
        System.out.println(role);

        if (role.equals("Escoja una palabra para que su oponente adivine:")) {
            System.out.print("Introduce una palabra: ");
            String palabra = scanner.nextLine();
            out.println(palabra);
        }

        while (true) {
            System.out.println(in.readLine()); // Turno del jugador
            System.out.print("Introduce una palabra: ");
            String guess = scanner.nextLine();
            out.println(guess);

            String respuesta = in.readLine();
            System.out.println("Resultado: " + respuesta);

            if (respuesta.equals("VICTORIA")) {
                System.out.println("¡Has ganado!");
                break;
            }
        }
        cerrar();
    }

    private void cerrar() throws IOException {
        in.close();
        out.close();
        socket.close();
    }

    public static void main(String[] args) throws IOException {
        Cliente cliente = new Cliente("localhost", 5225);
        cliente.jugar();
    }
}
