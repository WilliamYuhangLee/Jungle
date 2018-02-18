package Jungle.java;

import static org.lwjgl.opengl.GL11.*;

public class Utils {
    public static final int SIZE_OF_FLOAT = 4;
    public static void printGLError(){
        int err=glGetError();
        String error="";
        switch(err) {
            case GL_INVALID_OPERATION:      error="INVALID_OPERATION";      break;
            case GL_INVALID_ENUM:           error="INVALID_ENUM";           break;
            case GL_INVALID_VALUE:          error="INVALID_VALUE";          break;
            case GL_OUT_OF_MEMORY:          error="OUT_OF_MEMORY";          break;
            case GL_STACK_UNDERFLOW:        error="GL_STACK_UNDERFLOW";     break;
            case GL_STACK_OVERFLOW:         error="GL_STACK_OVERFLOW";      break;
        }

        if(err!=GL_NO_ERROR){
            System.err.println("GL Error :  "+error);
        }
    }

    private static boolean haveSetScreenWH = false;
    private static int SCREEN_WIDTH;
    private static int SCREEN_HEIGHT;

    public static void setScreenWH(int w, int h){
        if(!haveSetScreenWH){
            haveSetScreenWH=true;
            SCREEN_WIDTH=w;
            SCREEN_HEIGHT=h;
        }
    }

    public static int getScreenWidth(){
        return SCREEN_WIDTH;
    }

    public static int getScreenHeight(){
        return SCREEN_HEIGHT;
    }

    public static double normalizeX(double lastX){
        return 2*(lastX-SCREEN_WIDTH/2)/SCREEN_WIDTH;
    }

    public static double normalizeY(double lastY){
        return 2*(SCREEN_HEIGHT/2-lastY)/SCREEN_HEIGHT;
    }
}
