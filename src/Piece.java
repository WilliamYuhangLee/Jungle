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

/* bug fixing, replaced by a new one below
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
            if (!(dx == 0 && dy == 4) && !(dy == 0 && dx == 3))  { // needs debug here.
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
*/

    boolean canMoveTo(Square target) { // new canMoveTo() method
        if (this.location.isSame(target)) {
            //1. if same square, can't move
            return false;
        } else if (target.isDen() && target.getSide() == this.side) {
            //2. if target square is den of own side
            return false;
        } else if (target.hasPiece()) {
            //3. if target square has piece: enemy or ally
            if (target.piece.side == this.side) {
                //3.1 if ally, can't move
                return false;
            } else {
                //3.2 if enemy: can or can't be beaten
                if (this.animal.canBeat(target.piece.animal)) {
                    //3.2.1 enemy can be beaten
                    ; // no effect
                } else {
                    //3.2.2 enemy can't be beaten: could it accidentally be in a trap?
                    if (target.isTrap()) {
                        //3.2.2.1 enemy can't be beaten but is in a trap: ours or theirs?
                        if (target.getSide() == this.side) {
                            //3.2.2.1.1 unbeatable enemy is in our trap
                            ; // no effect
                        } else {
                            //3.2.2.1.2 unbeatable enemy is not in a trap of ours: can't move
                            return false;
                        }
                    } else {
                        //3.2.2.2 enemy can't be beaten and is not in a trap
                        return false;
                    }
                }
            }
        }
        // in the event of having a piece in target, there are still more checks to do
        if (this.animal == Animal.RAT || this.animal == Animal.DOG) {
            //4.1 if our piece is a Rat or a Dog: land <--> water?
            if (this.location.isWater() != target.isWater()) {
                //4.1.1 if moving from a land square to a water square or vice versa
                if (target.hasPiece()) {
                    //4.1.1.1 and the target square has a piece
                    return false;
                } else {
                    //4.1.1.2 if not, go ahead
                    ; //no effect
                }
            } else {
                //4.1.2 if moving from land to land or water to water
                ; // no effect
            }
        } else {
            //4.2 if our piece is not a Rat or Dog: is target square water?
            if (target.isWater()) {
                //4.2.1 our piece is not a Rat or Dog, and the target is water, can't move
                return false;
            }
        }
        int x0 = this.location.getRow();
        int x1 = target.getRow();
        int y0 = this.location.getCol();
        int y1 = target.getCol();
        int dx = Math.abs(x0 - x1);
        int dy = Math.abs(y0 - y1);
        boolean movingToAdjacentSquare = dx == 0 && dy == 1 || dx == 1 && dy ==0;
        if (this.animal == Animal.LION || this.animal == Animal.TIGER) {
            //5.1 if our piece is a Lion or a Tiger: from bank to bank?
            if (this.location.isBank() && target.isBank()) {
                //5.1.1 from bank to bank: jumping straight over river?
                if (dx == 4 && dy == 0 || dx == 0 && dy == 3) {
                    //5.1.1.1 jumping straight over river
                    ; // no effect
                } else {
                    //5.1.1.2 not jumping straight over river: moving to an adjacent square?
                    if (movingToAdjacentSquare) {
                        //5.1.1.2.1 moving to an adjacent square
                        ; // no effect
                    } else {
                        //5.1.1.2.2 not moving to an adjacent square, can't move
                        return false;
                    }
                }
            } else {
                //5.1.2 not from bank to bank: moving to an adjacent square?
                if (movingToAdjacentSquare) {
                    //5.1.2.1 moving to an adjacent square
                    ; // no effect
                } else {
                    //5.1.2.2 not moving to an adjacent square, can't move
                    return false;
                }
            }
        } else {
            //5.2 if our piece is not a Lion or Tiger: moving to an adjacent square?
            if (movingToAdjacentSquare) {
                //5.2.1 moving to an adjacent square
                ; // no effect
            } else {
                //5.2.2 not moving to an adjacent square, can't move
                return false;
            }
        }
        return true;
    }

    boolean canMove() {
        boolean canMove = false;
        rowLoop:
        for (Square[] row: this.location.board.squares) {
            for (Square square: row) {
                if (this.canMoveTo(square)) {
                    canMove = true;
                    break rowLoop;
                }
            }
        }
        return canMove;
    }

    void moveTo(Square target) {
        if (this.canMoveTo(target)) {
            this.location.piece = null;
            if (target.hasPiece() && target.piece.side != this.side) {
                target.piece.killedBy(this);
            }
            this.location = target;
            target.piece = this;
            if (target.isDen() && target.getSide() != this.getSide()) {
                this.getSide().win();
            }
        }
    }

    void killedBy(Piece that) {
        this.location = null;
        this.isKilled = true;
        this.side.pieceKilled(this);
        if (this.side.getCount() == 0) {
            that.getSide().win();
        }
    }

    boolean isKilled() {
        return isKilled;
    }

    String print() {
        if (side == Side.RED) {
            return animal.print();
        } else if (side == Side.BLACK) {
            return animal.print().toLowerCase();
        } else {
            return "";
        }
    }
}
