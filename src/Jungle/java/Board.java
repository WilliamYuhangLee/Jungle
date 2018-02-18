package Jungle.java;

import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static Jungle.java.Utils.*;


import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glGetAttribLocation;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

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
        initGL();
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


    // GUI Stuff by frank
    public int VAO,VBO,vertexCount,vPos_location,texCoords_location;
    public Shader shader = new Shader("Board_vs.glsl", "Board_fs.glsl");
    public Texture2D texture = new Texture2D("jungle_board.jpg");
    void initGL(){
        float[] vertices = {
                -1f, 1f, 0f,  0f,1f,
                -1f, -1f, 0f, 0f,0f,
                1f, -1f, 0f,  1f,0f,

                1f, -1f, 0f,   1f,0f,
                1f, 1f, 0f,     1f,1f,
                -1f, 1f, 0f,  0f,1f
        };
        FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(vertices.length);
        verticesBuffer.put(vertices);
        verticesBuffer.flip();

        vertexCount = 6;
        vPos_location=glGetAttribLocation(shader.Program,"vPos");
        texCoords_location=glGetAttribLocation(shader.Program,"texCoords");


        VAO = glGenVertexArrays();
        glBindVertexArray(VAO);

        VBO = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, VBO);
        glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(vPos_location, 3, GL_FLOAT, false, SIZE_OF_FLOAT*5, 0);
        glEnableVertexAttribArray(vPos_location);
        glVertexAttribPointer(texCoords_location, 2, GL_FLOAT, false, SIZE_OF_FLOAT*5, SIZE_OF_FLOAT*3);
        glEnableVertexAttribArray(texCoords_location);
        glBindBuffer(GL_ARRAY_BUFFER, 0);


        glBindVertexArray(0);
    }

    public void draw(){
        shader.Use();
        glBindVertexArray(VAO);
        texture.bind();
        glDrawArrays(GL_TRIANGLES,0,vertexCount);
        glBindVertexArray(0);
    }

}
