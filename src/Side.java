enum Side {
    RED, BLACK, NONE;

    Piece[] pieces = new Piece[8];
    int count = 0;

    void addPiece(Piece piece) {
        pieces[count] = piece;
        count++;
    }

    void pieceKilled() {
        count--;
    }
}
