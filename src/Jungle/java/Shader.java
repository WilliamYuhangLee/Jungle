package Jungle.java;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import static org.lwjgl.opengl.GL20.*;

public class Shader {
    public int Program;

    public Shader(String vertexShaderPath,String fragmentShaderPath){
        String vertexShaderCode = getFile(vertexShaderPath);
        String fragmentShaderCode = getFile(fragmentShaderPath);
        int vertex;
        int fragment;

        // Vertex Shader

        vertex = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertex, vertexShaderCode);
        glCompileShader(vertex);
        checkCompileErrors(vertex, "VERTEX");
        // Fragment Shader
        fragment = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragment, fragmentShaderCode);
        glCompileShader(fragment);
        checkCompileErrors(fragment, "FRAGMENT");


        // Shader Program
        Program = glCreateProgram();
        glAttachShader(Program, vertex);
        glAttachShader(Program, fragment);
        glLinkProgram(Program);
        checkCompileErrors(Program, "PROGRAM");
        glDeleteShader(vertex);
        glDeleteShader(fragment);
    }

    public static void checkCompileErrors (int program,String type){
        if(type.equals("PROGRAM")){
            String infoLog = glGetProgramInfoLog(program);
            if(infoLog.length()!=0){
                System.err.println(type+" shader error !!!!");
                System.err.println(infoLog);
                System.err.println();
            }
        }
        else {
            String infoLog = glGetShaderInfoLog(program);
            if(infoLog.length()!=0){
                System.err.println(type+" shader error !!!!");
                System.err.println(infoLog);
                System.err.println();
            }
        }
    }

    public void Use(){
        glUseProgram(Program);
    }

    private String getFile(String fileName) {

        StringBuilder result = new StringBuilder("");

        //Get file from resources folder
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(fileName).getFile());

        try (Scanner scanner = new Scanner(file)) {

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                result.append(line).append("\n");
            }

            scanner.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return result.toString();

    }

}
