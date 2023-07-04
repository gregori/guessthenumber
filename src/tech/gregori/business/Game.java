package tech.gregori.business;

import tech.gregori.cli.CommandLineInterface;
import tech.gregori.network.Client;
import tech.gregori.network.Network;
import tech.gregori.network.Server;

import java.io.IOException;
import java.util.Random;

public class Game {
    private Network network;
    private NumberGuesser numberGuesser;
    private CommandLineInterface cli;
    private String position;
    private boolean endGame;

    private void initialize() {
        numberGuesser = new NumberGuesser();
        cli = new CommandLineInterface();
        endGame = false;
    }

    public Game(int port) {
        initialize();
        cli.printServerStartedInfo(port);
        network = new Server(port);
    }

    public Game(String ip, int port) {
        initialize();
        cli.printClientStartedInfo(ip, port);
        network = new Client(ip, port);
        cli.printConnectedInfo();
    }

    private void getUserNumber() {
        int number = cli.askForNumber();
        numberGuesser.setNumber(number);
    }

    private void checkFirst() throws IOException {
        Random random = new Random();
        int randomNumber = random.nextInt(6) + 1;
        String response;
        if (network instanceof Client) {
            network.sendMessage(Integer.toString(randomNumber));
            this.position = network.receiveMessage();
        } else {
            String remoteNumberStr = network.receiveMessage();
            int remoteNumber = Integer.parseInt(remoteNumberStr);
            String positionResponse = remoteNumber >= randomNumber ? "first" : "second";
            this.position = positionResponse.equals("first") ? "second" : "first";
            network.sendMessage(positionResponse);
        }
    }

    private void processRemoteGuess() throws IOException {
        String remoteGuessStr = network.receiveMessage();
        int remoteGuess = Integer.parseInt(remoteGuessStr);
        network.sendMessage(Integer.toString(numberGuesser.checkGuess(remoteGuess)));
        cli.printOpponentGuess(remoteGuess);
    }

    private void processRemoteResponse(String response) {
        int responseInt = Integer.parseInt(response);
        String responseResult = responseInt == 0 ? "acertou" : responseInt < 0 ? "menor" : "maior";
        cli.printGuessResult(responseResult);
    }

    private void processLocalGuess() throws IOException {
        int guess = cli.askForGuess();
        network.sendMessage(Integer.toString(guess));
        String answerStr = network.receiveMessage();
        processRemoteResponse(answerStr);
    }

    private void processGuesses() throws IOException {
        if (this.position.equals("first")) {
            processLocalGuess();
            processRemoteGuess();
        } else {
            processRemoteGuess();
            processLocalGuess();
        }
    }

    public void run() {
        getUserNumber();
        try {
            checkFirst();
            processGuesses();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
