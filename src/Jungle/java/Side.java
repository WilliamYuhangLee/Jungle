package Jungle.java;

import java.util.ArrayList;

enum Side {
    RED, BLACK, NONE;

    private boolean isVictorious = false;
    Piece[] pieces = new Piece[8];
    ArrayList<Piece> alivePieces = new ArrayList<Piece>();
    private int count = 0;

    void addPiece(Piece piece) {
        pieces[count] = piece;
        alivePieces.add(piece);
        count++;
    }

    void pieceKilled(Piece piece) {
        count--;
        alivePieces.remove(piece);
        alivePieces.trimToSize();
    }

    void win() {
        isVictorious = true;
    }

    boolean isVictorious() {
        return isVictorious;
    }

    int getCount() {
        return count;
    }

    public String toString() {
        if (this == RED) {
            return "Red";
        } else if (this == BLACK) {
            return "Black";
        } else {
            return "";
        }
    }

    public String toString2() {
        if (this == RED) {
            return "Red";
        } else if (this == BLACK) {
            return "blue";
        } else {
            return "";
        }
    }

    String printAlivePieces() {
        String str = "";
        for (Piece piece : pieces) {
            if (!piece.isKilled()) {
                str += " \"" + piece.print() + "\"";
            }
        }
        return str;
    }
}
