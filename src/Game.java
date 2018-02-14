import org.apache.commons.text.WordUtils;

class Game {

    static void launch() {
        startUp();
        gameIntro();
        startGame();
    }

    static void startUp() {
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
    }

    static void gameIntro() {
        System.out.println("================================================================================");
        System.out.println("                                  Introduction                                  ");
        System.out.println("================================================================================");
        System.out.println("Welcome to the Jungle!");
        System.out.println(WordUtils.wrap("Command your animals to capture all the other player's animals or their den (marked with \"O\") to win!",80));
        System.out.println(WordUtils.wrap("Animals on the red side are marked with CAPITAL letters, and small letters on the black side.",80));
        System.out.println(WordUtils.wrap("Starting from red, each turn a player can move one of their animals by one square horizontally or vertically (a special rule for Lions and Tigers will be explained later).", 80));
        System.out.println(WordUtils.wrap("Higher ranking animals can capture lower or identical ranking animals by moving into their square, except for Rats, which can capture Elephants and Elephants can't capture Rats.", 80));
        System.out.println(WordUtils.wrap("The animal ranking, from highest to lowest, along with their markers in the game is:", 80));
        System.out.println("Rank|Marker|Animal");
        System.out.println("  8    E    Elephant");
        System.out.println("  7    L    Lion");
        System.out.println("  6    T    Tiger");
        System.out.println("  5    J    Jaguar");
        System.out.println("  4    W    Wolf");
        System.out.println("  3    D    Dog");
        System.out.println("  2    C    Cat");
        System.out.println("  1    R    Rat");
        System.out.println(WordUtils.wrap("There are three traps (marked with \"XXX\") placed next to each side's den. You can capture any enemy animals in your dens using any animals regardless of rank.", 80));
        System.out.println(WordUtils.wrap("Two areas of water (marked with \"~~~\") are located in the center of the board. Only Rats and Dogs may enter the water and engage other animals in the water.", 80));
        System.out.println(WordUtils.wrap("However, they can't enter a water square from land or a land square from water if there is another animal (of any side) on that square.", 80));
        System.out.println(WordUtils.wrap("Lions and Tigers may jump over the water horizontally or vertically, but they can't do so if there is any other animal on their path.", 80));
        System.out.println(WordUtils.wrap("Finally, animals may not move into their own den. If an enemy animal moves into the den, the player loses.", 80));
        System.out.println("================================================================================");
    }

    static void startGame() {
        Board board = Board.createBoard();
        System.out.println(board);
    }
}
