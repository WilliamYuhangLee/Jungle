package Jungle.java;

class Board {

    private static boolean hasCreated = false;
    Square[][] squares;
    Side[] sides;

    static Board createBoard() {
        if (!hasCreated) {
            hasCreated = true;
            return new Board();
        } else {
            return null;
        }
    }

    private Board() {
        squares = initBoard(this);
        sides = new Side[] {Side.RED, Side.BLACK};
    }

    private static Square[][] initBoard(Board board) {
        Square[][] squares = new Square[9][7];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 7; j++) {
                squares[i][j] = new Square(i, j, board);
            }
        }
        for (int i = 3; i <= 5; i++) {
            for (int j = 1; j <= 5; j++) {
                if (j != 3) {
                    squares[i][j] = new Square(i, j, true, board);
                }
            }
        }
        int[][] redTraps = {{0,2},{0,4},{1,3}};
        int[][] blackTraps = {{7,3},{8,2},{8,4}};
        for (int i = 0; i < redTraps.length; i++) {
            squares[redTraps[i][0]][redTraps[i][1]] = new Square(redTraps[i][0], redTraps[i][1], false, false, true, Side.RED, null, board);
        }
        for (int i = 0; i < blackTraps.length; i++) {
            squares[blackTraps[i][0]][blackTraps[i][1]] = new Square(blackTraps[i][0], blackTraps[i][1], false, false, true, Side.BLACK, null, board);
        }
        squares[0][3] = new Square(0, 3, false, true, false, Side.RED, null, board);
        squares[8][3] = new Square(8, 3, false, true, false, Side.BLACK, null, board);
        squares[0][0] = new Square(0, 0, new Piece(Animal.LION, Side.RED), board);
        squares[0][6] = new Square(0, 6, new Piece(Animal.TIGER, Side.RED), board);
        squares[1][1] = new Square(1, 1, new Piece(Animal.DOG, Side.RED), board);
        squares[1][5] = new Square(1, 5, new Piece(Animal.CAT, Side.RED), board);
        squares[2][0] = new Square(2, 0, new Piece(Animal.RAT, Side.RED), board);
        squares[2][2] = new Square(2, 2, new Piece(Animal.JAGUAR, Side.RED), board);
        squares[2][4] = new Square(2, 4, new Piece(Animal.WOLF, Side.RED), board);
        squares[2][6] = new Square(2, 6, new Piece(Animal.ELEPHANT, Side.RED), board);
        squares[8][6] = new Square(8, 6, new Piece(Animal.LION, Side.BLACK), board);
        squares[8][0] = new Square(8, 0, new Piece(Animal.TIGER, Side.BLACK), board);
        squares[7][5] = new Square(7, 5, new Piece(Animal.DOG, Side.BLACK), board);
        squares[7][1] = new Square(7, 1, new Piece(Animal.CAT, Side.BLACK), board);
        squares[6][6] = new Square(6, 6, new Piece(Animal.RAT, Side.BLACK), board);
        squares[6][4] = new Square(6, 4, new Piece(Animal.JAGUAR, Side.BLACK), board);
        squares[6][2] = new Square(6, 2, new Piece(Animal.WOLF, Side.BLACK), board);
        squares[6][0] = new Square(6, 0, new Piece(Animal.ELEPHANT, Side.BLACK), board);
        return squares;
    }

    String print() {
        String header = "   A   B   C   D   E   F   G   \n";
        String line = " +---+---+---+---+---+---+---+ \n";
        String board = header + line;
        for (int i = 0; i < squares.length; i++) {
            board += (i + 1) + "|";
            for (int j = 0; j < squares[i].length; j++) {
                board += squares[i][j].print() + "|";
            }
            board += (i + 1) + "\n" + line;
        }
        board += header;
        return board;
    }
}
