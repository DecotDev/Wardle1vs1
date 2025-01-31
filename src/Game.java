import java.util.Scanner;

public class Game {

    private int numPlayers;


    Scanner sc = new Scanner(System.in);

    public void jugar(Cliente a){
    boolean correcto = false;
        System.out.println("\nWelcome to WARdle.");
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

    public void setNumPlayers(int numPlayers) {
        this.numPlayers = numPlayers;
    }
}
