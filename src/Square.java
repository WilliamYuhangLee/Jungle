class Square {
    private int row;
    private int col;
    private boolean water = false;
    private boolean den = false;
    private boolean trap = false;
    private Side side = Side.NONE;
    Piece piece = null;
    Board board;

    Square(int row, int col, boolean water, boolean den, boolean trap, Side side, Piece piece, Board board) {
        this.row = row;
        this.col = col;
        this.board = board;
        if (water) {
            this.water = water;
        } else if (den) {
            this.den = den;
            this.side = side;
        } else if (trap) {
            this.trap = trap;
            this.side = side;
        } else {
            this.piece = piece;
        }
    }
    Square(int row, int col, Board board) {
        this(row, col, false, board);
    }

    Square(int row, int col, boolean water, Board board) {
        this(row, col, water, false, false, Side.NONE, null, board);
    }

    Square(int row, int col, Piece piece, Board board) {
        this(row, col, false, false, false, Side.NONE, piece, board);
    }

    int getRow() {
        return row;
    }

    int getCol() {
        return col;
    }

    boolean isWater() {
        return water;
    }

    boolean isDen() {
        return den;
    }

    boolean isTrap() {
        return trap;
    }

    Side getSide() {
        return side;
    }

    boolean isAdjacent(Square that) {
        return (this.row == that.row && Math.abs(this.col - that.col) == 1 || this.col == that.col && Math.abs(this.row - that.row) == 1);
    }

    boolean isBank() {
        if ((this.row == 3 || this.row == 4 || this.row == 5) && (this.col == 0 || this.col == 3 || this.col == 6)) {
            return true;
        } else if ((this.row == 2 || this.row == 6) && (this.col >= 1 && this.col <= 5 && this.col != 3)) {
            return true;
        } else return false;
    }

    boolean isSame(Square that) {
        return this.row == that.row && this.col == that.col;
    }

    boolean hasPiece() {
        return this.piece != null;
    }

    String print() {
        String square = "";
        if (water) {
            square += "~";
        } else if (trap) {
            square += "X";
        } else {
            square += " ";
        }
        if (!hasPiece()) {
            if (water) {
                square += "~";
            } else if (trap) {
                square += "X";
            } else if (den) {
                square += "O";
            } else {
                square += " ";
            }
        } else {
            square += this.piece.print();
        }
        if (water) {
            square += "~";
        } else if (trap) {
            square += "X";
        } else {
            square += " ";
        }
        return square;
    }
}
