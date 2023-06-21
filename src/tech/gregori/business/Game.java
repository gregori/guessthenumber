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

    private void initialize() {
        numberGuesser = new NumberGuesser();
        cli = new CommandLineInterface();
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
    }

    private void processLocalGuess() throws IOException {
        int guess = cli.askForGuess();
        network.sendMessage(Integer.toString(guess));
        String answerStr = network.receiveMessage();
        int answer = Integer.parseInt(answerStr);
        cli.printGuessResult(answer);
    }

    private void processRemoteGuess(String guess) {
        int numberGuess = Integer.parseInt(guess);
        cli.printOpponentGuess(numberGuess);
    }

    private void processGuesses() throws IOException {
        String answer;
        String remoteGuess;
        if (this.position.equals("first")) {
            processLocalGuess();
            remoteGuess = network.receiveMessage();
            processRemoteGuess(remoteGuess);
        } else {
            remoteGuess = network.receiveMessage();
            processRemoteGuess(remoteGuess);
            processLocalGuess();
        }
    }

}
