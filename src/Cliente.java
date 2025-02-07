// Cliente.java (Corregido)
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Cliente {
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Scanner scanner;

    public Cliente(String host, int port) throws IOException {
        socket = new Socket(host, port);
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
        scanner = new Scanner(System.in);
    }

    public void jugar() throws IOException, ClassNotFoundException {
        System.out.println((String) in.readObject()); // Mensaje de bienvenida
        String role = (String) in.readObject();
        System.out.println(role);

        if (role.equals("Escoja una palabra para que su oponente adivine:")) {
            System.out.print("Introduce una palabra: ");
            String palabra = scanner.nextLine();
            out.writeObject(palabra);
            out.flush();
        }

        while (true) {
            System.out.println((String) in.readObject()); // Turno del jugador
            System.out.print("Introduce una palabra: ");
            String guess = scanner.nextLine();
            out.writeObject(new Jugada(guess, "", 0));
            out.flush();

            Jugada respuesta = (Jugada) in.readObject();
            System.out.println("Resultado: " + respuesta.getResultado());

            if (respuesta.getResultado().equals("VICTORIA")) {
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

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Cliente cliente = new Cliente("localhost", 5225);
        cliente.jugar();
    }
}
