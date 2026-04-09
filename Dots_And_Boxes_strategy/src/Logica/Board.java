package Logica;

public class Board {
    private int rows, cols;
    private boolean[][] horizontalLines, verticalLines;
    private char[][] boxes;
    
    private Move lastMove;

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

    public boolean addLine(Move move) {
        if (move.type == 'H') {
            if (horizontalLines[move.row][move.col]) return false;
            horizontalLines[move.row][move.col] = true;
        } else {
            if (verticalLines[move.row][move.col]) return false;
            verticalLines[move.row][move.col] = true;
        }
        this.lastMove = move;
        return true;
    }

    public Move getLastMove() { return lastMove; }

    public int countSides(int r, int c) {
        int count = 0;
        if (horizontalLines[r][c]) count++;
        if (horizontalLines[r + 1][c]) count++;
        if (verticalLines[r][c]) count++;
        if (verticalLines[r][c + 1]) count++;
        return count;
    }

    public Move getMissingMove(int r, int c) {
        if (!horizontalLines[r][c]) return new Move('H', r, c);
        if (!horizontalLines[r + 1][c]) return new Move('H', r + 1, c);
        if (!verticalLines[r][c]) return new Move('V', r, c);
        if (!verticalLines[r][c + 1]) return new Move('V', r, c + 1);
        return null;
    }

    public void removeLine(Move move) {
        if (move.type == 'H') horizontalLines[move.row][move.col] = false;
        else verticalLines[move.row][move.col] = false;
    }

    public int checkBoxes(Move move, char symbol) {
        int count = 0;
        int r = move.row, c = move.col;
        if (move.type == 'H') {
            if (r > 0 && isBoxClosed(r - 1, c)) { boxes[r - 1][c] = symbol; count++; }
            if (r < rows && isBoxClosed(r, c)) { boxes[r][c] = symbol; count++; }
        } else {
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
            for (int j = 0; j < cols; j++) if (boxes[i][j] == ' ') return false;
        return true;
    }

    public int getRows() { return rows; }
    public int getCols() { return cols; }
    public boolean[][] getHorizontalLines() { return horizontalLines; }
    public boolean[][] getVerticalLines() { return verticalLines; }
    public char[][] getBoxes() { return boxes; }
    
    // --- אלו שתי המתודות שהיו חסרות וגרמו לשגיאה ---
    public boolean hasHorizontalLine(int r, int c) { return horizontalLines[r][c]; }
    public boolean hasVerticalLine(int r, int c) { return verticalLines[r][c]; }
}