package tech.gregori.business;

public class NumberGuesser {
    private int number;

    public void setNumber(int number) {
        this.number = number;
    }

    public int checkGuess(int guess) {
        return Integer.compare(guess, number);
    }
}
