import tech.gregori.business.Game;

public class Main {
    public static void main(String[] args) {
        Game game;

        if (args.length == 2) {  // cliente (ip e porta)
            game = new Game(args[0], Integer.parseInt(args[1]));
        } else if (args.length == 1) {  // servidor (porta)
            game = new Game(Integer.parseInt(args[0]));
        } else {
            System.out.println("Uso: java -jar GuessTheNumber.jar <porta>");
            System.out.println("Uso: java -jar GuessTheNumber.jar <ip> <porta>");
            return;
        }

        game.run();
    }
}