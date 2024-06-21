package com.scientia.utils;

import java.awt.Image;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import javax.swing.ImageIcon;

public class ImageUtils {

    public static ImageIcon convertToImageIcon(InputStream inputStream) {
        try {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int nRead;
            byte[] data = new byte[16384];
            while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }
            buffer.flush();
            byte[] imageBytes = buffer.toByteArray();
            ImageIcon imageIcon = new ImageIcon(imageBytes);
            Image img = imageIcon.getImage();
            Image newImg = img.getScaledInstance(200, 200, Image.SCALE_SMOOTH); // Escala según sea necesario
            return new ImageIcon(newImg);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
