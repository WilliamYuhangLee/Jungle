enum Side {
    RED, BLACK, NONE;

    private boolean isVictorious = false;
    Piece[] pieces = new Piece[8];
    private int count = 0;

    void addPiece(Piece piece) {
        pieces[count] = piece;
        count++;
    }

    void pieceKilled() {
        count--;
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

    String printAlivePieces() {
        String str = "";
        for (Piece piece : pieces) {
            if (!piece.isKilled()) {
                str += " " + piece;
            }
        }
        return str;
    }
}
