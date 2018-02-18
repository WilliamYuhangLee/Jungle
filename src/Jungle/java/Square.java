package Jungle.java;

import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static Jungle.java.Utils.*;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

class Square {
    private int row;
    private int col;
    private boolean water = false;
    private boolean den = false;
    private boolean trap = false;
    private Side side = Side.NONE;
    Piece piece;
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
        } else if (piece != null){
            this.piece = piece;
            this.piece.location = this;
        }
        initGL();
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

    String printCoordinates() {
        String coords = "";
        coords += (char) (65 + col);
        coords += row + 1;
        return coords;
    }

    //GUI Stuff by frank
    public boolean pressing=false;
    public boolean selected=false;
    public boolean isTarget=false;
    public void updatePressing(double x,double y){
        pressing = checkHit(x,y);
    }

    public boolean checkPress(double x,double y){
        pressing = checkHit(x,y);
        return pressing;
    }
    public void checkRelease(double x,double y){
        boolean hit = checkHit(x,y);
        if(hit){

        }
        if(pressing){
            if(!selected && piece!=null && !isTarget){
                selected=true;
                for (Square[] squares:board.squares){
                    for(Square square:squares){
                        if(square!=this) square.selected=false;
                        square.isTarget=false;
                        if(piece.canMoveTo(square)){
                            square.isTarget=true;
                        }
                    }
                }
            }
            if(isTarget){
                for (Square[] squares:board.squares){
                    for(Square square:squares){
                        if(square.selected){
                            square.piece.moveTo(this);
                        }
                        square.selected=false;
                        square.isTarget=false;
                    }
                }
            }
        }
        pressing = false;
    }

    private static int getCol(double x){
        return (int)((x/2.0+0.5)*7.0);
    }
    private static int getRow(double y){
        return (int)((0.5-y/2.0)*9.0);
    }

    private boolean checkHit(double x,double y){
        /*return y>= normalizeY(row*getScreenHeight()/9)
                && y <= normalizeY((row+1)*getScreenHeight()/9)
                && x >= normalizeX((col)*getScreenWidth()/7)
                && x <= normalizeY((col+1)*getScreenWidth()/7);*/
        int pressedCol = getCol(x);
        int pressedRow = getRow(y);
        return pressedCol == col && pressedRow==row;
    }



    public int VAO,VBO,vertexCount,vPos_location,texCoords_location,positionShift_location;
    public  static Shader shader = null;
    void initGL(){

        if (shader==null) shader =new Shader("Square_vs.glsl", "Square_fs.glsl");
        vPos_location=glGetAttribLocation(shader.Program,"vPos");
        texCoords_location=glGetAttribLocation(shader.Program,"texCoords");
        positionShift_location = glGetUniformLocation(shader.Program,"positionShift");

        float z = -0.5f;
        float xPadding = 0.f;
        float yPadding = xPadding/9f*7f;
        float[] vertices = {
                0+xPadding, 2f/9f-yPadding, z,  0f,1f,
                0+xPadding, 0+yPadding, z, 0f,0f,
                2f/7f-xPadding, 0+yPadding, z,  1f,0f,

                2f/7f-xPadding, 0+yPadding, z,   1f,0f,
                2f/7f-xPadding, 2f/9f-yPadding, z,     1f,1f,
                0+xPadding, 2f/9f-yPadding, z,  0f,1f
        };
        FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(vertices.length);
        verticesBuffer.put(vertices);
        verticesBuffer.flip();

        vertexCount = 6;



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

        float xShift = 2f*( ( (float) (getCol())/ 7f ) - 0.5f );
        float yShift = 2f*(0.5f-((float) (getRow()+1)/9f));
        if (selected){
            shader.Use();
            glUniform2f(positionShift_location,xShift,yShift);
            glBindVertexArray(VAO);
            glUniform3f(glGetUniformLocation(shader.Program,"color"),255,0,200);
            glDrawArrays(GL_TRIANGLES,0,vertexCount);
            glBindVertexArray(0);
        }
        else if (isTarget){
            shader.Use();
            glUniform2f(positionShift_location,xShift,yShift);
            glBindVertexArray(VAO);
            glUniform3f(glGetUniformLocation(shader.Program,"color"),0,255,0);
            glDrawArrays(GL_TRIANGLES,0,vertexCount);
            glBindVertexArray(0);
        }
    }


}
