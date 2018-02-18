package Jungle.java;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static Jungle.java.Utils.normalizeX;
import static Jungle.java.Utils.normalizeY;
import static Jungle.java.Utils.setScreenWH;
import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class JungleGUI {
    private long window;
    private double lastX,lastY;
    private boolean firstMouse=true;
    private Square pressingSquare = null;
    Game game;
    private void init() {
        GLFWErrorCallback.createPrint(System.err).set();

        if ( !glfwInit() )
            throw new IllegalStateException("Unable to initialize GLFW");

        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);

        window = glfwCreateWindow(625, 800, "Jungle", NULL, NULL);
        if ( window == NULL )
            throw new RuntimeException("Failed to create the GLFW window");

        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
                glfwSetWindowShouldClose(window, true);
        });

        glfwSetCursorPosCallback(window, (window,xpos,ypos)->{
            if(firstMouse)
            {
                lastX = xpos;
                lastY = ypos;
                firstMouse = false;
            }

            double xoffset = xpos - lastX;
            double yoffset = ypos - lastY;

            lastX = xpos;
            lastY = ypos;

            double normalX=normalizeX(lastX);
            double normalY=normalizeY(lastY);

            if(pressingSquare !=null){
                pressingSquare.updatePressing(normalX,normalY);
            }
        });

        glfwSetMouseButtonCallback(window, (window,button,action,mods)->{
            double normalX=normalizeX(lastX);
            double normalY=normalizeY(lastY);
            if(button==GLFW_MOUSE_BUTTON_LEFT){
                if(action==GLFW_PRESS){
                    for(Square[] squares:game.board.squares){
                        for(Square square:squares){
                            if(square.checkPress(normalX,normalY)){
                                pressingSquare=square;
                                return;
                            }
                        }
                    }
                }
                else if (action==GLFW_RELEASE){
                    pressingSquare= null;
                    for(Square[] squares:game.board.squares){
                        for(Square square:squares){
                            square.checkRelease(normalX,normalY);
                        }
                    }
                }
            }
        });

        try ( MemoryStack stack = stackPush() ) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            glfwGetWindowSize(window, pWidth, pHeight);
            setScreenWH(pWidth.get(),pHeight.get());

            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            glfwSetWindowPos(
                    window,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        }

        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);

        glfwShowWindow(window);

        GL.createCapabilities();
        game = new Game();
        //game.launch();

    }

    private void loop() {
        glClearColor(1.0f, 0.0f, 0.0f, 0.0f);
        while ( !glfwWindowShouldClose(window) ) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

            game.board.draw();
            for(Square[] squares:game.board.squares){
                for(Square square:squares){
                    square.draw();
                    if(square.piece!=null){

                        square.piece.draw();
                    }
                }
            }
            glfwSwapBuffers(window);
            glfwPollEvents();
        }
    }

    public void run() {
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");

        init();
        loop();

        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }
}
