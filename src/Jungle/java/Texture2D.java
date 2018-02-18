package Jungle.java;

import org.lwjgl.BufferUtils;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.channels.FileChannel;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBImage.*;

public class Texture2D {
    public int texture;
    public Texture2D(String imagePath){
        ByteBuffer buffer=null;
        URL url = Thread.currentThread().getContextClassLoader().getResource(imagePath);
        File file = new File(url.getFile());

        try{
            FileInputStream fis = new FileInputStream(file);
            FileChannel fc = fis.getChannel();
            buffer = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
            fc.close();
            fis.close();
        }
        catch (Exception e){
            System.err.println("Error finding texture file " +imagePath );
            System.exit(0);
        }


        IntBuffer w = BufferUtils.createIntBuffer(1);
        IntBuffer h = BufferUtils.createIntBuffer(1);
        IntBuffer comp = BufferUtils.createIntBuffer(1);

        stbi_set_flip_vertically_on_load(true);
        if (!stbi_info_from_memory(buffer, w, h, comp)) {
            System.err.println("stbi reading image failed " + stbi_failure_reason());
            System.exit(0);
        }

        ByteBuffer image = stbi_load_from_memory(buffer, w, h, comp, 4);
        if (image == null){
            System.err.println("image is null " + stbi_failure_reason());
            System.exit(0);
        }

        texture = glGenTextures();

        glBindTexture(GL_TEXTURE_2D, texture);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, w.get(), h.get(), 0, GL_RGBA, GL_UNSIGNED_BYTE, image);

        stbi_image_free(image);
    }
    public void bind(){
        glBindTexture(GL_TEXTURE_2D,texture);
    }

}
