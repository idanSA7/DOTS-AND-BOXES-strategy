package Logica;

public class GameLogic {
    private Board board;
    private Player p1, p2, currentPlayer;
    private boolean vsComputer;
    private String difficulty;

    public GameLogic(int rows, int cols, boolean vsComputer, String difficulty) {
        this.board = new Board(rows, cols);
        this.p1 = new Player('X');
        this.p2 = new Player('O');
        this.currentPlayer = p1;
        this.vsComputer = vsComputer;
        this.difficulty = difficulty;
    }

    public boolean playTurn(Move move) {
        if (board.addLine(move)) {
            int boxesClosed = board.checkBoxes(move, currentPlayer.getSymbol());
            if (boxesClosed > 0) {
                for (int i = 0; i < boxesClosed; i++) currentPlayer.addPoint();
            } else {
                currentPlayer = (currentPlayer == p1) ? p2 : p1;
            }
            return true;
        }
        return false;
    }

    public int getP1Score() { return p1.getScore(); }
    public int getP2Score() { return p2.getScore(); }
    public Board getBoard() { return board; }
    public Player getCurrentPlayer() { return currentPlayer; }
    public Player getP1() { return p1; }
    public Player getP2() { return p2; }
    public boolean isVsComputer() { return vsComputer; }
    public String getDifficulty() { return difficulty; }
    public boolean isGameOver() { return board.allBoxesClosed(); }
}