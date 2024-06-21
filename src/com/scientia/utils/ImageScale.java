package com.scientia.utils;

import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class ImageScale {

    public static void setImageToLabel(JLabel lbl, String imagePath) {
        // Cargar la imagen desde el archivo
        ImageIcon imageIcon = new ImageIcon(imagePath);

        // Obtener el ancho y alto del JLabel
        int width = lbl.getWidth();
        int height = lbl.getHeight();

        // Escalar la imagen al tamaño del JLabel
        Image scaledImage = imageIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);

        // Crear un nuevo ImageIcon con la imagen escalada
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        // Establecer la imagen escalada en el JLabel
        lbl.setIcon(scaledIcon);
    }
}
