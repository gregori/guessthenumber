package tech.gregori.cli;

public class CommandLineInterface {
    public int askForNumber() {
        return 0;
    }

    public int askForGuess() {
        return 0;
    }

    public void printOpponentGuess(int guess) {

    }

    public void printGuessResult(String guessResult) {

    }

    public void printServerStartedInfo(int port) {
        System.out.println("Servidor iniciado na porta: " + port);
        System.out.println("Aguardando conexão...");
    }

    public void printClientStartedInfo(String ip, int port) {
        System.out.println("Conectando ao servidor IP " + ip + " porta " + port + "...");
    }

    public void printConnectedInfo() {
        System.out.println("Conectado.");
    }
}
