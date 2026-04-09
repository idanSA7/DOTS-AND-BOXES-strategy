package Logica;

public class Move {
    public char type; // 'H' או 'V'
    public int row;
    public int col;

    public Move(char type, int row, int col) {
        this.type = type;
        this.row = row;
        this.col = col;
        
    }
}

