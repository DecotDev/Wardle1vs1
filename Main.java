import java.util.Objects;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Cliente a = new Cliente("hue");
        boolean correcto = false;
        System.out.println("Welcome to WARdle.");
        System.out.println("The word has "+a.palabra.length()+" letters. Good luck!");

        int turnos = 0;
        while (!correcto){
            turnos++;
            System.out.println("Turn:\t"+ turnos+ "\nTry to guess the word:");
            String guess = sc.nextLine();

            if (guess.length()!=a.palabra.length()){
                System.out.println("The word has "+a.palabra.length()+" letters. Try again");
                continue;
            }

            correcto = a.victoria(guess);
            System.out.println(a.respuesta);
        }
    }
}
