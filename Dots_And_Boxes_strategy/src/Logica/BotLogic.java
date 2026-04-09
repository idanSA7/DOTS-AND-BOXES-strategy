package Logica;

import java.util.*;

public class BotLogic {
    private Board board;
    private String difficulty;
    private Random random = new Random();

    public BotLogic(Board board, String difficulty) {
        this.board = board;
        this.difficulty = difficulty;
    }

    public Move makeMove() {
        if (difficulty.equalsIgnoreCase("Easy")) {
            List<Move> all = getAllPossibleMoves();
            return all.get(random.nextInt(all.size()));
        }

        Move win = findClosingMove();
        if (win != null) return win;

        Move safe = findSafeMove();
        if (safe != null) return safe;

        List<Move> all = getAllPossibleMoves();
        return all.get(random.nextInt(all.size()));
    }

    private Move findClosingMove() {
        for (int r = 0; r < board.getRows(); r++)
            for (int c = 0; c < board.getCols(); c++)
                if (board.countSides(r, c) == 3) return board.getMissingMove(r, c);
        return null;
    }

    private Move findSafeMove() {
        List<Move> possible = getAllPossibleMoves();
        List<Move> safe = new ArrayList<>();
        for (Move m : possible) {
            if (isMoveSafe(m)) safe.add(m);
        }
        return safe.isEmpty() ? null : safe.get(random.nextInt(safe.size()));
    }

    private boolean isMoveSafe(Move m) {
        int r = m.row, c = m.col;
        if (m.type == 'H') {
            if (r > 0 && board.countSides(r - 1, c) == 2) return false;
            if (r < board.getRows() && board.countSides(r, c) == 2) return false;
        } else {
            if (c > 0 && board.countSides(r, c - 1) == 2) return false;
            if (c < board.getCols() && board.countSides(r, c) == 2) return false;
        }
        return true;
    }

    private List<Move> getAllPossibleMoves() {
        List<Move> moves = new ArrayList<>();
        for (int r = 0; r <= board.getRows(); r++)
            for (int c = 0; c < board.getCols(); c++)
                if (!board.hasHorizontalLine(r, c)) moves.add(new Move('H', r, c));
        for (int r = 0; r < board.getRows(); r++)
            for (int c = 0; c <= board.getCols(); c++)
                if (!board.hasVerticalLine(r, c)) moves.add(new Move('V', r, c));
        return moves;
    }
}