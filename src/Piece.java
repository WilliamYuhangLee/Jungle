class Piece {
    private Animal animal;
    private Side side;
    Square location;
    private boolean isKilled = false;

    Piece(Animal animal, Side side, Square location) {
        this.animal = animal;
        if (side == Side.RED || side == Side.BLACK) {
            this.side = side;
            this.side.addPiece(this);
        }
        this.location = location;
    }

    Animal getAnimal() {
        return animal;
    }

    Side getSide() {
        return side;
    }

    boolean canMoveTo(Square target) {
        boolean temp = true;
        if (this.location.isSame(target)) {
            temp = false;
        } else if (target.isDen() && this.side == target.getSide()) {
            temp = false;
        } else if (target.hasPiece() && target.piece.side == this.side) {
            temp = false;
        } else if (target.hasPiece() && !this.animal.canBeat(target.piece.animal) && (!target.isTrap() || target.getSide() != this.side)) {
            temp = false;
        } else if (this.animal != Animal.RAT && this.animal != Animal.DOG && target.isWater()) {
            temp = false;
        } else if ((this.animal == Animal.RAT || this.animal == Animal.DOG) && (this.location.isWater() != target.isWater()) && target.hasPiece()) {
            temp = false;
        } else if (this.animal != Animal.TIGER && this.animal != Animal.LION && !this.location.isAdjacent(target)) {
            temp = false;
        } else if ((this.animal == Animal.TIGER || this.animal == Animal.LION) && !this.location.isAdjacent(target) && !(this.location.isBank() && target.isBank())) {
            temp = false;
        } else if (this.location.getRow() != target.getRow() && this.location.getCol() != target.getCol()) {
            temp = false;
        } else {
            int x0 = this.location.getRow();
            int x1 = target.getRow();
            int y0 = this.location.getCol();
            int y1 = target.getCol();
            int dx = Math.abs(x0 - x1);
            int dy = Math.abs(y0 - y1);
            if (!(dx == 0 || dx == 4) || !(dy == 0 || dy == 3)) {
                temp = false;
            } else {
                for (int i = Math.min(x0, x1); i <= Math.max(x0, x1); i++) {
                    for (int j = Math.min(y0, y1); j <= Math.min(y0, y1); j++) {
                        if (this.location.board.squares[i][j].hasPiece()) {
                            temp = false;
                        }
                    }
                }
            }
        }
        return temp;
    }

    void moveTo(Square target) {
        if (this.canMoveTo(target)) {
            this.location.piece = null;
            if (target.hasPiece() && target.piece.side != this.side) {
                target.piece.killedBy(this);
            }
            this.location = target;
            target.piece = this;
        }
    }

    void killedBy(Piece that) {
        this.location = null;
        this.isKilled = true;
        this.side.pieceKilled();
    }
}
