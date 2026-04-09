package Logica;

import java.util.Scanner;

public class Player {
    private char symbol; // 'X' או 'O'
    private int score;

    public Player(char symbol) {
        this.symbol = symbol;
        this.score = 0;
    }

    public void addPoint() {
        score++;
    }

    public int getScore() { // המתודה שהייתה חסרה
        return score;
    }

    public char getSymbol() {
        return symbol;
    }
}

