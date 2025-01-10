import java.util.Objects;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Cliente a = new Cliente("huevo");
        boolean correcto = false;


        while (!correcto){
            System.out.println("Try to guess the word:");
            String guess = sc.nextLine();

            if (guess.length()!=a.palabra.length()){
                System.out.println("La palabra es de "+a.palabra.length()+" letras, prueba de nuevo.");
                continue;
            }

            correcto = (Objects.equals(a.comprobar(guess), "ooooo"));
            System.out.println(a.respuesta);
        }

    }
}
