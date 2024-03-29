package Jungle.java;

import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;

import static Jungle.java.Utils.SIZE_OF_FLOAT;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

class Piece {
    private Animal animal;
    public Side side;
    Square location;
    private boolean isKilled = false;
    //static HashMap<String,Texture2D> animalTextures=new HashMap<>();
    Piece(Animal animal, Side side) {
        this.animal = animal;
        if (side == Side.RED || side == Side.BLACK) {
            this.side = side;
            this.side.addPiece(this);
        }
        initGL();
    }

    Animal getAnimal() {
        return animal;
    }

    Side getSide() {
        return side;
    }

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
                if (dx == 4 && dy == 0 || dx == 0 && dy == 3 && x0 != 2 && x0 != 6) {
                    //5.1.1.1 jumping straight over river: is there a Rat or Dog on the path?
                    for (int i = Math.min(x0, x1); i <= Math.max(x0, x1); i++) {
                        for (int j = Math.min(y0, y1); j <= Math.max(y0 ,y1); j++) {
                            if (target.board.squares[i][j].isWater() && target.board.squares[i][j].hasPiece()) {
                                return false;
                            }
                        }
                    }
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
            System.out.println(this.side + " " + this.animal + " has moved to " + target.printCoordinates() + ".");
            this.location.piece = null;
            if (target.hasPiece() && target.piece.side != this.side) {
                target.piece.killedBy(this);
            }
            this.location = target;
            target.piece = this;
            if (target.isTrap() && target.getSide() != this.side) {
                System.out.println("Caution! " + this.side + " " + this.animal + " is only one step from " + target.getSide() + " den!");
            }
            if (target.isDen() && target.getSide() != this.side) {
                this.side.win();
                System.out.println(this.side + " has won by capturing " + target.getSide() + " den!");
            }
        }
    }

    Square[] accessibleSquares() {
        ArrayList<Square> accessibleSquares = new ArrayList<>();
        for (Square[] row: this.location.board.squares) {
            for (Square square: row) {
                if (this.canMoveTo(square)) {
                    accessibleSquares.add(square);
                }
            }
        }
        return accessibleSquares.toArray(new Square[accessibleSquares.size()]);
    }

    String printAccessibleSquares() {
        String str = "";
        for (Square square: this.accessibleSquares()) {
            str += " \"" +  square.printCoordinates() + "\"";
        }
        return str;
    }

    void killedBy(Piece that) {
        System.out.println(this.side + " " + this.animal + " at " + this.location.printCoordinates() + " has been captured!");
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




    // GUI Stuff by frank
    public int VAO,VBO,vertexCount,vPos_location,texCoords_location,positionShift_location;
    public  static Shader shader = null;
    public Texture2D texture=null;
    void initGL(){

        if (shader==null)shader =new Shader("Piece_vs.glsl", "Piece_fs.glsl");
        vPos_location=glGetAttribLocation(shader.Program,"vPos");
        texCoords_location=glGetAttribLocation(shader.Program,"texCoords");
        positionShift_location = glGetUniformLocation(shader.Program,"positionShift");

        String thisPieceName = (this.animal.toString()+"_"+this.side.toString2()).toLowerCase();
        texture= new Texture2D(thisPieceName+".png");

        float z = -0.9f;
        float xPadding = 0.05f;
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
        if(!isKilled){
            shader.Use();
            float xShift = 2f*( ( (float) (location.getCol())/ 7f ) - 0.5f );
            float yShift = 2f*(0.5f-((float) (location.getRow()+1)/9f));
            glUniform2f(positionShift_location,xShift,yShift);
            glBindVertexArray(VAO);
            texture.bind();
            glDrawArrays(GL_TRIANGLES,0,vertexCount);
            glBindVertexArray(0);
        }
    }


}
