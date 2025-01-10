import java.util.Objects;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Cliente a = new Cliente();

        a.palabra="huevo";
        boolean correcto = false;

        while (!correcto){
            System.out.println("Try to guess the word:");
            String guess = sc.nextLine();

            correcto = (Objects.equals(a.comprobar(guess), "ooooo"));
            System.out.println(a.respuesta);
        }

    }
}
