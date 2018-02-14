enum Animal {

    RAT(1), CAT(2), DOG(3), WOLF(4), JAGUAR(5), TIGER(6), LION(7), ELEPHANT(8);

    private int rank;

    Animal(int rank) {
        this.rank = rank;
    }

    int getRank() {
        return rank;
    }

    boolean canBeat(Animal that) {
        if (this.rank == 1 && that.rank == 8) {
            return true;
        } else if (this.rank == 8 && that.rank == 1) {
            return false;
        } else if (this.rank >= that.rank) {
            return true;
        } else return false;
    }

    public String toString() {
        if (rank == 1) {
            return "R";
        } else if (rank == 2) {
            return "C";
        } else if (rank == 3) {
            return "D";
        } else if (rank == 4) {
            return "W";
        } else if (rank == 5) {
            return "J";
        } else if (rank == 6) {
            return "T";
        } else if (rank == 7) {
            return "L";
        } else {
            return "E";
        }
    }
}
