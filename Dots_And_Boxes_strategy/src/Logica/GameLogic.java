package Logica;

public class GameLogic {
    private Board board;
    private Player p1, p2, currentPlayer;

    public GameLogic(int rows, int cols) {
        this.board = new Board(rows, cols);
        this.p1 = new Player('X');
        this.p2 = new Player('O');
        this.currentPlayer = p1;
    }

    public boolean playTurn(Move move) {
        if (board.addLine(move)) {
            int boxesClosed = board.checkBoxes(move, currentPlayer.getSymbol());
            if (boxesClosed > 0) {
                for (int i = 0; i < boxesClosed; i++) currentPlayer.addPoint();
                // השחקן מקבל תור נוסף - לא מחליפים
            } else {
                currentPlayer = (currentPlayer == p1) ? p2 : p1;
            }
            return true;
        }
        return false;
    }

    public Board getBoard() { return board; }
    public Player getCurrentPlayer() { return currentPlayer; }
    public Player getP1() { return p1; }
    public Player getP2() { return p2; }
    public boolean isGameOver() { return board.allBoxesClosed(); }
}