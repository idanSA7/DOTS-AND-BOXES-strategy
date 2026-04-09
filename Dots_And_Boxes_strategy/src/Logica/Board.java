package Logica;

public class Board {
    private int rows;
    private int cols;
    private boolean[][] horizontalLines;
    private boolean[][] verticalLines;
    private char[][] boxes;

    public Board(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        horizontalLines = new boolean[rows + 1][cols];
        verticalLines = new boolean[rows][cols + 1];
        boxes = new char[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                boxes[i][j] = ' ';
            }
        }
    }

    // Getters עבור הגרפיקה
    public int getRows() { return rows; }
    public int getCols() { return cols; }
    public boolean[][] getHorizontalLines() { return horizontalLines; }
    public boolean[][] getVerticalLines() { return verticalLines; }
    public char[][] getBoxes() { return boxes; }

    public boolean addLine(Move move) {
        if (move.type == 'H') {
            if (horizontalLines[move.row][move.col]) return false;
            horizontalLines[move.row][move.col] = true;
            return true;
        } else if (move.type == 'V') {
            if (verticalLines[move.row][move.col]) return false;
            verticalLines[move.row][move.col] = true;
            return true;
        }
        return false;
    }

    public int checkBoxes(Move move, char symbol) {
        int count = 0;
        int r = move.row;
        int c = move.col;

        if (move.type == 'H') {
            if (r > 0 && isBoxClosed(r - 1, c)) { boxes[r - 1][c] = symbol; count++; }
            if (r < rows && isBoxClosed(r, c)) { boxes[r][c] = symbol; count++; }
        } else if (move.type == 'V') {
            if (c > 0 && isBoxClosed(r, c - 1)) { boxes[r][c - 1] = symbol; count++; }
            if (c < cols && isBoxClosed(r, c)) { boxes[r][c] = symbol; count++; }
        }
        return count;
    }

    private boolean isBoxClosed(int r, int c) {
        return horizontalLines[r][c] && horizontalLines[r + 1][c] &&
               verticalLines[r][c] && verticalLines[r][c + 1] && boxes[r][c] == ' ';
    }

    public boolean allBoxesClosed() {
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                if (boxes[i][j] == ' ') return false;
        return true;
    }
}