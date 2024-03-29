package Jungle.java;

import org.apache.commons.text.WordUtils;

import java.util.Scanner;

class Game {

    Board board=Board.createBoard();
    void launch() {
        startUp();
        gameIntro();
        System.out.println("\nPress ENTER key to start the game.");
        try {
            System.in.read();
        } catch (Exception e) {}
        startGame();
    }

    private  void startUp() {
        System.out.println("===================================");
        System.out.println("||    = =  = =  =  ==  =    ==== ||");
        System.out.println("||    = =  = == = =  = =    =    ||");
        System.out.println("||    = =  = == = =    =    ==== ||");
        System.out.println("||    = =  = = == = == =    =    ||");
        System.out.println("|| =  = =  = = == =  = =    =    ||");
        System.out.println("||  ==   ==  =  =  ==  ==== ==== ||");
        System.out.println("===================================");
        System.out.println("||          Developed by         ||");
        System.out.println("||     William Lee & Frank Lu    ||");
        System.out.println();
    }

    private  void gameIntro() {
        System.out.println("================================================================================");
        System.out.println("                                  Introduction                                  ");
        System.out.println("================================================================================");
        System.out.println("Welcome to the Jungle!\n");
        System.out.println(WordUtils.wrap("Command your animals to capture all the other player's animals or their den to win!\n", 80));
        System.out.println("Game rules:");
        System.out.println("The Jungle game board consists of 7 columns and 9 rows of squares.");
        System.out.println("Animals of two sides, red and black, move on the square spaces.");
        System.out.println(WordUtils.wrap("Animals on the red side are marked with CAPITAL letters, and animals on the black side with small letters.", 80));
        System.out.println(WordUtils.wrap("Starting from red, each turn a player can move one of their animals by one square horizontally or vertically (a special rule for Lions and Tigers will be explained later).", 80));
        System.out.println(WordUtils.wrap("Higher ranking animals can capture LOWER OR IDENTICAL ranking animals by moving into their square, except for Rats, which can capture Elephants and Elephants can't capture Rats.", 80));
        System.out.println(WordUtils.wrap("The animal ranking, from highest to lowest, along with animal markers in the game is:", 80));
        printRanking();
        System.out.println("(you can type \"ranking\" in the game to show this again)");
        System.out.println(WordUtils.wrap("There are three traps (marked with \"XXX\") placed next to each side's den. You can capture any enemy animals in your dens using any of your animals regardless of rank.", 80));
        System.out.println(WordUtils.wrap("Two areas of water (marked with \"~~~\") are located in the center of the board. Only Rats and Dogs may enter the water and engage other animals in the water.", 80));
        System.out.println(WordUtils.wrap("However, they can't enter a water square from land or a land square from water if there is another animal (of any side) on that square.", 80));
        System.out.println(WordUtils.wrap("Lions and Tigers may jump over the water horizontally or vertically, but they can't do so if there is any other animal in their path.", 80));
        System.out.println(WordUtils.wrap("Finally, animals may not move into their own den (marked with \"O\"). If an enemy animal moves into a player's den, the player loses.", 80));
    }

    private  void printRanking() {
        System.out.println("Rank|Marker|Animal");
        System.out.println("  8   E/e   Elephant");
        System.out.println("  7   L/l   Lion");
        System.out.println("  6   T/t   Tiger");
        System.out.println("  5   J/j   Jaguar");
        System.out.println("  4   W/w   Wolf");
        System.out.println("  3   D/d   Dog");
        System.out.println("  2   C/c   Cat");
        System.out.println("  1   R/r   Rat");
    }

    private  void startGame() {
        System.out.println("================================================================================");
        System.out.println("                                  GAME STARTS                                   ");
        System.out.println("================================================================================");
        //board = Board.createBoard();
        Side playersTurn = Side.RED;
        Scanner sc = new Scanner(System.in);
        while (!Side.RED.isVictorious() && !Side.BLACK.isVictorious()) {
            System.out.println("==============" + ((playersTurn == Side.RED)? "":"=="));
            System.out.println("|| " + playersTurn + " turn ||");
            System.out.println("==============" + ((playersTurn == Side.RED)? "":"=="));
            boolean turnFinished = false;
            Turn:
            while (!turnFinished) {
                System.out.print(board.print());
                boolean validAnimal = false;
                String chosenAnimal;
                Piece chosenPiece = null;
                Locating:
                while (!validAnimal) {
                    System.out.println("(Choose from" + playersTurn.printAlivePieces() + ")");
                    System.out.print("Please select an animal to move: ");
                    chosenAnimal = sc.nextLine();
                    if (chosenAnimal.equals("ranking")) {
                        printRanking();
                        continue Locating;
                    }
                    if (chosenAnimal.equals("E") || chosenAnimal.equals("e") ||
                            chosenAnimal.equals("L") || chosenAnimal.equals("l") ||
                            chosenAnimal.equals("T") || chosenAnimal.equals("t") ||
                            chosenAnimal.equals("J") || chosenAnimal.equals("j") ||
                            chosenAnimal.equals("W") || chosenAnimal.equals("w") ||
                            chosenAnimal.equals("D") || chosenAnimal.equals("d") ||
                            chosenAnimal.equals("C") || chosenAnimal.equals("c") ||
                            chosenAnimal.equals("R") || chosenAnimal.equals("r")) {
                        if (playersTurn == Side.RED && (chosenAnimal.equals("E") || chosenAnimal.equals("L") || chosenAnimal.equals("T") || chosenAnimal.equals("J") || chosenAnimal.equals("W") || chosenAnimal.equals("D") || chosenAnimal.equals("C") || chosenAnimal.equals("R"))
                                || playersTurn == Side.BLACK && (chosenAnimal.equals("e") || chosenAnimal.equals("l") || chosenAnimal.equals("t") || chosenAnimal.equals("j") || chosenAnimal.equals("w") || chosenAnimal.equals("d") || chosenAnimal.equals("c") || chosenAnimal.equals("r"))) {
                            boolean foundAnimalAlive = false;
                            for (int i = 0; i < playersTurn.alivePieces.size(); i++) {
                                if (chosenAnimal.equals(playersTurn.alivePieces.get(i).print())) {
                                    foundAnimalAlive = true;
                                    chosenPiece = playersTurn.alivePieces.get(i);
                                    break;
                                }
                            }
                            if (foundAnimalAlive) {
                                if (chosenPiece.canMove()) {
                                    validAnimal = true;
                                } else {
                                    System.out.println("The animal you have chosen can not move to any square. Please select another one.");
                                }
                            } else {
                                System.out.println("You can only select an animal that hasn't been captured!");
                            }
                        } else {
                            System.out.println("You can only select one of YOUR animals!");
                        }
                    } else {
                        System.out.println("Please choose a valid animal marker from the list below!");
                    }
                }
                System.out.println("You have selected your " + chosenPiece.getAnimal() + " at " + chosenPiece.location.printCoordinates() + ".");
                Square target = null;
                Targeting:
                while (true) {
                    System.out.println("(Choose from" + chosenPiece.printAccessibleSquares() + ")");
                    System.out.println("(If you want to select another animal, type \"reselect\")");
                    System.out.print("Please enter the coordinates of a square that you want it to move to: ");
                    String chosenCoordinates = sc.nextLine();
                    if (chosenCoordinates.equals("reselect")) {
                        continue Turn;
                    } else if (chosenCoordinates.equals("ranking")) {
                        printRanking();
                        continue Targeting;
                    } else {
                        for (Square square: chosenPiece.accessibleSquares()) {
                            if (chosenCoordinates.equals(square.printCoordinates())) {
                                target = square;
                                break Targeting;
                            }
                        }
                        System.out.println("Please select a valid target square from the list below!");
                    }
                }
                chosenPiece.moveTo(target);
                turnFinished = true;
                if (playersTurn == Side.RED) {
                    playersTurn = Side.BLACK;
                } else {
                    playersTurn = Side.RED;
                }
            }
        }
        System.out.println("================================================================================");
        System.out.println("                                   GAME OVER                                    ");
        System.out.println("================================================================================");
    }
}
