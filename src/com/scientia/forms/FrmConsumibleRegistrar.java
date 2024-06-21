package com.scientia.forms;

import com.formdev.flatlaf.FlatLightLaf;
import com.scientia.daos.DaoConsumible;
import com.scientia.entidades.Consumible;
import com.scientia.utils.ImageScale;
import com.scientia.utils.PlaceHolder;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author danim
 */
public class FrmConsumibleRegistrar extends javax.swing.JFrame {

    private String rutaAplicacion;

    public FrmConsumibleRegistrar() {
        initComponents();

        //máximizar pantalla
        this.setExtendedState(MAXIMIZED_BOTH);
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                resizeComponents();
            }
        });
        resizeComponents();
        loadDesign();

        String nomLogo = "..\\ScientiaLab\\src\\com\\scientia\\icons\\logo.png";
        System.out.println(nomLogo);
        ImageScale.setImageToLabel(lblLogo, nomLogo);

    }

    private void resizeComponents() {
        Dimension frameSize = this.getSize();
        int width = frameSize.width;
        int height = frameSize.height;
        int anchoLateral = jpLateral.getWidth();

        // Ajustar tamaños en proporción al tamaño del frame
        jpContainer.setBounds(anchoLateral, 0, (int) (width - anchoLateral), height);
        jpLateral.setBounds(0, 0, (int) (width * 0.1), height);

        jpLogo.setBounds(20, 10, (int) (width * 0.06), (int) (height * 0.09));
        jpHome.setBounds(35, 250, (int) (width * 0.055), (int) (height * 0.09));
        lblHome.setBounds(18, 0, (int) (width * 0.055), (int) (height * 0.09));

        jpRegresar.setBounds(1200, 20, (int) (width * 0.05), (int) (height * 0.08));
        lblRegresar.setBounds(13, 0, (int) (width * 0.05), (int) (height * 0.08));

        jpNombre.setBounds(15, 150, (int) (width * 0.23), (int) (height * 0.09));
        jpExistencias.setBounds(400, 150, (int) (width * 0.15), (int) (height * 0.09));
        chkDisponible.setBounds(670, 150, (int) (width * 0.23), (int) (height * 0.09));

        jpMarca.setBounds(15, 250, (int) (width * 0.23), (int) (height * 0.09));
        jpReferencia.setBounds(15, 350, (int) (width * 0.23), (int) (height * 0.09));
        jpGabinete.setBounds(15, 450, (int) (width * 0.23), (int) (height * 0.09));
        jpObservaciones.setBounds(15, 550, (int) (width * 0.5), (int) (height * 0.13));

        jpFoto.setBounds(400, 250, (int) (width * 0.25), (int) (height * 0.33));
        lblSubirFoto.setBounds(15, 30, (int) (width * 0.223), (int) (height * 0.27));

        btnAnadir.setBounds(300, 700, (int) (width * 0.15), (int) (height * 0.052));
        jpImagenLat.setBounds(width - 600, 0, (int) (width * 0.7), height);

        // Revalidar y repintar el contenedor para aplicar los cambios
        this.revalidate();
        this.repaint();
    }

    private void loadDesign() {

        try {

            URL imgHome = new URL("https://cdn-icons-png.flaticon.com/512/1946/1946433.png");
            Image homeImg = new ImageIcon(imgHome).getImage().getScaledInstance(50, 40, 0);
            ImageIcon iconoHome = new ImageIcon(homeImg);
            lblHome.setIcon(iconoHome);

            URL imgBack = new URL("https://cdn-icons-png.flaticon.com/512/6581/6581938.png");
            Image backImg = new ImageIcon(imgBack).getImage().getScaledInstance(50, 50, 0);
            ImageIcon iconoBack = new ImageIcon(backImg);
            lblRegresar.setIcon(iconoBack);

        } catch (MalformedURLException ex) {
            Logger.getLogger(FrmPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }

        PlaceHolder nombre = new PlaceHolder("Nombre", cajaNombre);
        PlaceHolder marca = new PlaceHolder("Pasco", cajaMarca);
        PlaceHolder gabinete = new PlaceHolder("0", cajaGabinete);
        PlaceHolder existencias = new PlaceHolder("0", cajaExistencias);
        PlaceHolder costo = new PlaceHolder("Observaciones...", cajaObservaciones);
        PlaceHolder acta = new PlaceHolder("NR", cajaReferencia);

    }

    private Boolean estaTodoBien() {
        Boolean bandera = true;

        String nombreConsumible = cajaNombre.getText();
        if (nombreConsumible.equals("")) {
            cajaNombre.requestFocus();
            bandera = false;
            JOptionPane.showMessageDialog(jpContainer, "Ingrese un nombre correcto", "Error", JOptionPane.ERROR_MESSAGE);
        } else {

            String marcaConsumible = cajaMarca.getText();
            if (marcaConsumible.equals("")) {
                cajaMarca.requestFocus();
                bandera = false;
                JOptionPane.showMessageDialog(jpContainer, "Ingrese una marca correcta", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                String referenciaConsumible = cajaReferencia.getText();
                if (referenciaConsumible.equals("")) {
                    cajaReferencia.requestFocus();
                    bandera = false;
                    JOptionPane.showMessageDialog(jpContainer, "Ingrese una referencia correcta", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    String observacionesConsumible = cajaObservaciones.getText();
                    if (observacionesConsumible.equals("")) {
                        cajaObservaciones.requestFocus();
                        bandera = false;
                        JOptionPane.showMessageDialog(jpContainer, "Ingrese observaciones correctas", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        if (lblSubirFoto.getIcon() == null) {
                            JOptionPane.showMessageDialog(jpContainer, "No hay una imagen seleccionada", "Error", JOptionPane.ERROR_MESSAGE);
                            bandera = false;
                        }
                    }
                }
            }
        }

        int numGabineteConsumible = Integer.parseInt(cajaGabinete.getText());
        try {
            if (numGabineteConsumible < 0) {
                bandera = false;
                cajaGabinete.requestFocus();
                JOptionPane.showMessageDialog(jpContainer, "Ingrese un número de gabinete correcto", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {

            bandera = false;
            JOptionPane.showMessageDialog(jpContainer, "Ingrese un número de gabinete correcto", "Error", JOptionPane.ERROR_MESSAGE);
            return bandera;
        }

        int existenciasConsumible = Integer.parseInt(cajaExistencias.getText());
        try {
            if (existenciasConsumible < 0) {
                bandera = false;
                cajaExistencias.requestFocus();
                JOptionPane.showMessageDialog(jpContainer, "Ingrese un número de existencias correcto", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            bandera = false;
            JOptionPane.showMessageDialog(jpContainer, "Ingrese un número de gabinete correcto", "Error", JOptionPane.ERROR_MESSAGE);
            return bandera;
        }

        return bandera;
    }

    private void borrarDatos() {
        cajaReferencia.setText("");
        cajaObservaciones.setText("");
        cajaGabinete.setText("");
        cajaNombre.setText("");
        cajaMarca.setText("");
        chkDisponible.setSelected(false);
        cajaExistencias.setText("");
        lblSubirFoto.setIcon(null);
        lblSubirFoto.setText("Adjuntar imagen");

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jpLateral = new javax.swing.JPanel();
        jpLogo = new javax.swing.JPanel();
        lblLogo = new javax.swing.JLabel();
        jpHome = new javax.swing.JPanel();
        lblHome = new javax.swing.JLabel();
        jpContainer = new javax.swing.JPanel();
        lblTitulo = new javax.swing.JLabel();
        jpGabinete = new javax.swing.JPanel();
        lblGabinete = new javax.swing.JLabel();
        cajaGabinete = new javax.swing.JTextField();
        jpMarca = new javax.swing.JPanel();
        lblMarca = new javax.swing.JLabel();
        cajaMarca = new javax.swing.JTextField();
        jpObservaciones = new javax.swing.JPanel();
        lblObservaciones = new javax.swing.JLabel();
        cajaObservaciones = new javax.swing.JTextField();
        jpNombre = new javax.swing.JPanel();
        lblNombre = new javax.swing.JLabel();
        cajaNombre = new javax.swing.JTextField();
        btnAnadir = new javax.swing.JButton();
        lblDatos = new javax.swing.JLabel();
        jpReferencia = new javax.swing.JPanel();
        lblReferencia = new javax.swing.JLabel();
        cajaReferencia = new javax.swing.JTextField();
        chkDisponible = new javax.swing.JCheckBox();
        jpFoto = new javax.swing.JPanel();
        lblFoto = new javax.swing.JLabel();
        lblSubirFoto = new javax.swing.JLabel();
        jpRegresar = new javax.swing.JPanel();
        lblRegresar = new javax.swing.JLabel();
        jpExistencias = new javax.swing.JPanel();
        lblExistencias = new javax.swing.JLabel();
        cajaExistencias = new javax.swing.JTextField();
        jpImagenLat = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);

        jpLateral.setBackground(new java.awt.Color(26, 49, 76));
        jpLateral.setLayout(null);

        jpLogo.setBackground(new java.awt.Color(26, 49, 76));

        lblLogo.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);

        javax.swing.GroupLayout jpLogoLayout = new javax.swing.GroupLayout(jpLogo);
        jpLogo.setLayout(jpLogoLayout);
        jpLogoLayout.setHorizontalGroup(
            jpLogoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpLogoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jpLogoLayout.setVerticalGroup(
            jpLogoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpLogoLayout.createSequentialGroup()
                .addComponent(lblLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jpLateral.add(jpLogo);
        jpLogo.setBounds(-3, 0, 110, 80);

        jpHome.setBackground(new java.awt.Color(255, 255, 255));
        jpHome.setLayout(null);

        lblHome.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblHome.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblHomeMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblHomeMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblHomeMouseExited(evt);
            }
        });
        jpHome.add(lblHome);
        lblHome.setBounds(0, 0, 90, 80);

        jpLateral.add(jpHome);
        jpHome.setBounds(10, 160, 90, 80);

        getContentPane().add(jpLateral);
        jpLateral.setBounds(0, 0, 110, 690);

        jpContainer.setBackground(new java.awt.Color(232, 236, 239));
        jpContainer.setLayout(null);

        lblTitulo.setFont(new java.awt.Font("Montserrat", 1, 60)); // NOI18N
        lblTitulo.setText("Añadir Nuevo Consumible");
        jpContainer.add(lblTitulo);
        lblTitulo.setBounds(20, 30, 830, 75);

        jpGabinete.setBackground(new java.awt.Color(255, 255, 255));

        lblGabinete.setFont(new java.awt.Font("Montserrat SemiBold", 0, 14)); // NOI18N
        lblGabinete.setForeground(new java.awt.Color(98, 106, 109));
        lblGabinete.setText("No. Gabinete:");

        cajaGabinete.setFont(new java.awt.Font("Montserrat SemiBold", 1, 24)); // NOI18N
        cajaGabinete.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        cajaGabinete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cajaGabineteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpGabineteLayout = new javax.swing.GroupLayout(jpGabinete);
        jpGabinete.setLayout(jpGabineteLayout);
        jpGabineteLayout.setHorizontalGroup(
            jpGabineteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpGabineteLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jpGabineteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpGabineteLayout.createSequentialGroup()
                        .addComponent(lblGabinete)
                        .addGap(0, 135, Short.MAX_VALUE))
                    .addComponent(cajaGabinete))
                .addContainerGap())
        );
        jpGabineteLayout.setVerticalGroup(
            jpGabineteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpGabineteLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblGabinete)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cajaGabinete, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jpContainer.add(jpGabinete);
        jpGabinete.setBounds(10, 420, 250, 70);

        jpMarca.setBackground(new java.awt.Color(255, 255, 255));

        lblMarca.setFont(new java.awt.Font("Montserrat SemiBold", 0, 14)); // NOI18N
        lblMarca.setForeground(new java.awt.Color(98, 106, 109));
        lblMarca.setText("Marca:");

        cajaMarca.setFont(new java.awt.Font("Montserrat SemiBold", 1, 24)); // NOI18N
        cajaMarca.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        cajaMarca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cajaMarcaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpMarcaLayout = new javax.swing.GroupLayout(jpMarca);
        jpMarca.setLayout(jpMarcaLayout);
        jpMarcaLayout.setHorizontalGroup(
            jpMarcaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpMarcaLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jpMarcaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpMarcaLayout.createSequentialGroup()
                        .addComponent(lblMarca)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(cajaMarca, javax.swing.GroupLayout.DEFAULT_SIZE, 232, Short.MAX_VALUE))
                .addContainerGap())
        );
        jpMarcaLayout.setVerticalGroup(
            jpMarcaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpMarcaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblMarca)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cajaMarca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jpContainer.add(jpMarca);
        jpMarca.setBounds(10, 240, 250, 70);

        jpObservaciones.setBackground(new java.awt.Color(255, 255, 255));

        lblObservaciones.setFont(new java.awt.Font("Montserrat SemiBold", 0, 14)); // NOI18N
        lblObservaciones.setForeground(new java.awt.Color(98, 106, 109));
        lblObservaciones.setText("Observaciones:");

        cajaObservaciones.setFont(new java.awt.Font("Montserrat SemiBold", 1, 24)); // NOI18N
        cajaObservaciones.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        cajaObservaciones.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cajaObservacionesActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpObservacionesLayout = new javax.swing.GroupLayout(jpObservaciones);
        jpObservaciones.setLayout(jpObservacionesLayout);
        jpObservacionesLayout.setHorizontalGroup(
            jpObservacionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpObservacionesLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jpObservacionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpObservacionesLayout.createSequentialGroup()
                        .addComponent(lblObservaciones)
                        .addGap(0, 452, Short.MAX_VALUE))
                    .addComponent(cajaObservaciones))
                .addContainerGap())
        );
        jpObservacionesLayout.setVerticalGroup(
            jpObservacionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpObservacionesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblObservaciones)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cajaObservaciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jpContainer.add(jpObservaciones);
        jpObservaciones.setBounds(10, 500, 580, 70);

        jpNombre.setBackground(new java.awt.Color(255, 255, 255));

        lblNombre.setFont(new java.awt.Font("Montserrat SemiBold", 0, 14)); // NOI18N
        lblNombre.setForeground(new java.awt.Color(98, 106, 109));
        lblNombre.setText("Nombre:");

        cajaNombre.setFont(new java.awt.Font("Montserrat SemiBold", 1, 24)); // NOI18N
        cajaNombre.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        cajaNombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cajaNombreActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpNombreLayout = new javax.swing.GroupLayout(jpNombre);
        jpNombre.setLayout(jpNombreLayout);
        jpNombreLayout.setHorizontalGroup(
            jpNombreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpNombreLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jpNombreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpNombreLayout.createSequentialGroup()
                        .addComponent(lblNombre)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(cajaNombre, javax.swing.GroupLayout.DEFAULT_SIZE, 232, Short.MAX_VALUE))
                .addContainerGap())
        );
        jpNombreLayout.setVerticalGroup(
            jpNombreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpNombreLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblNombre)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cajaNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jpContainer.add(jpNombre);
        jpNombre.setBounds(10, 150, 250, 70);

        btnAnadir.setBackground(new java.awt.Color(37, 206, 203));
        btnAnadir.setFont(new java.awt.Font("Montserrat", 1, 18)); // NOI18N
        btnAnadir.setForeground(new java.awt.Color(255, 255, 255));
        btnAnadir.setText("Añadir Consumible");
        btnAnadir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnadirActionPerformed(evt);
            }
        });
        jpContainer.add(btnAnadir);
        btnAnadir.setBounds(230, 620, 230, 30);

        lblDatos.setFont(new java.awt.Font("Montserrat", 1, 18)); // NOI18N
        lblDatos.setText("Datos Generales:");
        jpContainer.add(lblDatos);
        lblDatos.setBounds(30, 110, 170, 23);

        jpReferencia.setBackground(new java.awt.Color(255, 255, 255));

        lblReferencia.setFont(new java.awt.Font("Montserrat SemiBold", 0, 14)); // NOI18N
        lblReferencia.setForeground(new java.awt.Color(98, 106, 109));
        lblReferencia.setText("Referencia:");

        cajaReferencia.setFont(new java.awt.Font("Montserrat SemiBold", 1, 24)); // NOI18N
        cajaReferencia.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        cajaReferencia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cajaReferenciaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpReferenciaLayout = new javax.swing.GroupLayout(jpReferencia);
        jpReferencia.setLayout(jpReferenciaLayout);
        jpReferenciaLayout.setHorizontalGroup(
            jpReferenciaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpReferenciaLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jpReferenciaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpReferenciaLayout.createSequentialGroup()
                        .addComponent(lblReferencia)
                        .addGap(0, 151, Short.MAX_VALUE))
                    .addComponent(cajaReferencia))
                .addContainerGap())
        );
        jpReferenciaLayout.setVerticalGroup(
            jpReferenciaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpReferenciaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblReferencia)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cajaReferencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(8, Short.MAX_VALUE))
        );

        jpContainer.add(jpReferencia);
        jpReferencia.setBounds(10, 330, 250, 70);

        chkDisponible.setFont(new java.awt.Font("Montserrat", 1, 18)); // NOI18N
        chkDisponible.setText("Disponible");
        chkDisponible.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkDisponibleActionPerformed(evt);
            }
        });
        jpContainer.add(chkDisponible);
        chkDisponible.setBounds(540, 180, 130, 27);

        jpFoto.setBackground(new java.awt.Color(255, 255, 255));
        jpFoto.setLayout(null);

        lblFoto.setFont(new java.awt.Font("Montserrat SemiBold", 0, 14)); // NOI18N
        lblFoto.setForeground(new java.awt.Color(98, 106, 109));
        lblFoto.setText("Foto:");
        jpFoto.add(lblFoto);
        lblFoto.setBounds(12, 6, 40, 18);

        lblSubirFoto.setFont(new java.awt.Font("Montserrat", 2, 18)); // NOI18N
        lblSubirFoto.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSubirFoto.setText("Adjuntar Imagen");
        lblSubirFoto.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(37, 206, 203), 2, true));
        lblSubirFoto.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblSubirFoto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblSubirFotoMouseClicked(evt);
            }
        });
        jpFoto.add(lblSubirFoto);
        lblSubirFoto.setBounds(12, 30, 290, 190);

        jpContainer.add(jpFoto);
        jpFoto.setBounds(280, 250, 310, 230);

        jpRegresar.setBackground(new java.awt.Color(255, 255, 255));
        jpRegresar.setLayout(null);

        lblRegresar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblRegresar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblRegresarMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblRegresarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblRegresarMouseExited(evt);
            }
        });
        jpRegresar.add(lblRegresar);
        lblRegresar.setBounds(0, 0, 60, 60);

        jpContainer.add(jpRegresar);
        jpRegresar.setBounds(840, 30, 60, 60);

        jpExistencias.setBackground(new java.awt.Color(255, 255, 255));

        lblExistencias.setFont(new java.awt.Font("Montserrat SemiBold", 0, 14)); // NOI18N
        lblExistencias.setForeground(new java.awt.Color(98, 106, 109));
        lblExistencias.setText("Existencias:");

        cajaExistencias.setFont(new java.awt.Font("Montserrat SemiBold", 1, 24)); // NOI18N
        cajaExistencias.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        cajaExistencias.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cajaExistenciasActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpExistenciasLayout = new javax.swing.GroupLayout(jpExistencias);
        jpExistencias.setLayout(jpExistenciasLayout);
        jpExistenciasLayout.setHorizontalGroup(
            jpExistenciasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpExistenciasLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jpExistenciasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpExistenciasLayout.createSequentialGroup()
                        .addComponent(lblExistencias)
                        .addGap(0, 148, Short.MAX_VALUE))
                    .addComponent(cajaExistencias))
                .addContainerGap())
        );
        jpExistenciasLayout.setVerticalGroup(
            jpExistenciasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpExistenciasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblExistencias)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cajaExistencias, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jpContainer.add(jpExistencias);
        jpExistencias.setBounds(280, 150, 250, 70);

        jpImagenLat.setBackground(new java.awt.Color(26, 49, 76));

        javax.swing.GroupLayout jpImagenLatLayout = new javax.swing.GroupLayout(jpImagenLat);
        jpImagenLat.setLayout(jpImagenLatLayout);
        jpImagenLatLayout.setHorizontalGroup(
            jpImagenLatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 230, Short.MAX_VALUE)
        );
        jpImagenLatLayout.setVerticalGroup(
            jpImagenLatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 580, Short.MAX_VALUE)
        );

        jpContainer.add(jpImagenLat);
        jpImagenLat.setBounds(690, 100, 230, 580);

        getContentPane().add(jpContainer);
        jpContainer.setBounds(110, 0, 930, 680);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cajaNombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cajaNombreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cajaNombreActionPerformed

    private void cajaMarcaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cajaMarcaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cajaMarcaActionPerformed

    private void cajaGabineteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cajaGabineteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cajaGabineteActionPerformed

    private void cajaObservacionesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cajaObservacionesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cajaObservacionesActionPerformed

    private void lblHomeMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblHomeMouseEntered
        jpHome.setBackground(new Color(26, 49, 76));
        lblHome.setBackground(new Color(26, 49, 76));
    }//GEN-LAST:event_lblHomeMouseEntered

    private void lblHomeMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblHomeMouseExited
        jpHome.setBackground(Color.WHITE);
        lblHome.setBackground(Color.WHITE);
    }//GEN-LAST:event_lblHomeMouseExited

    private void cajaReferenciaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cajaReferenciaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cajaReferenciaActionPerformed

    private void lblRegresarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblRegresarMouseEntered
        jpRegresar.setBackground(new Color(26, 49, 76));
        jpRegresar.setBackground(new Color(26, 49, 76));
    }//GEN-LAST:event_lblRegresarMouseEntered

    private void lblRegresarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblRegresarMouseExited
        jpRegresar.setBackground(Color.WHITE);
        jpRegresar.setBackground(Color.WHITE);
    }//GEN-LAST:event_lblRegresarMouseExited

    private void chkDisponibleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkDisponibleActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chkDisponibleActionPerformed

    private void cajaExistenciasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cajaExistenciasActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cajaExistenciasActionPerformed

    private void btnAnadirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnadirActionPerformed
        if (estaTodoBien()) {
            InputStream fotoInputStream = null;
            try {
                String nombreConsumible, marcaConsumible, referenciaConsumible, observacionesConsumible;
                int numGabineteConsumible, existenciasConsumible;
                boolean disponibilidadConsumible;

                referenciaConsumible = cajaReferencia.getText().toLowerCase();
                observacionesConsumible = cajaObservaciones.getText().toLowerCase();
                nombreConsumible = cajaNombre.getText().toLowerCase();
                marcaConsumible = cajaMarca.getText().toLowerCase();

                numGabineteConsumible = Integer.parseInt(cajaGabinete.getText());
                existenciasConsumible = Integer.parseInt(cajaExistencias.getText());
                disponibilidadConsumible = chkDisponible.isSelected();

                Consumible objConsumible = new Consumible();

                objConsumible.setReferenciaConsumible(referenciaConsumible);
                objConsumible.setObservacionesConsumible(observacionesConsumible);
                objConsumible.setNumGabineteConsumible(numGabineteConsumible);
                objConsumible.setNombreConsumible(nombreConsumible);
                objConsumible.setMarcaConsumible(marcaConsumible);
                objConsumible.setDisponibilidadConsumible(disponibilidadConsumible);
                objConsumible.setExistenciasConsumible(existenciasConsumible);

                // Establecer la foto como InputStream
                fotoInputStream = new FileInputStream(new File(rutaAplicacion));
                objConsumible.setFotoConsumible(fotoInputStream);

                DaoConsumible miDao = new DaoConsumible();
                if (miDao.registrar(objConsumible)) {
                    JOptionPane.showMessageDialog(jpContainer, "¡¡Registro exitoso!!", "Información", JOptionPane.INFORMATION_MESSAGE);

                    borrarDatos();
                } else {
                    JOptionPane.showMessageDialog(jpContainer, "Registro fallido", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(FrmConsumibleRegistrar.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    fotoInputStream.close();
                } catch (IOException ex) {
                    Logger.getLogger(FrmConsumibleRegistrar.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }//GEN-LAST:event_btnAnadirActionPerformed

    private void lblSubirFotoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblSubirFotoMouseClicked
        JFileChooser seleccionar = new JFileChooser();
        seleccionar.setCurrentDirectory(new File(System.getProperty("user.home"))); // Directorio inicial
        seleccionar.setFileFilter(new FileNameExtensionFilter("Imagenes (*.jpg, *.png)", "jpg", "png"));

        int resultado = seleccionar.showOpenDialog(null);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            rutaAplicacion = seleccionar.getSelectedFile().getAbsolutePath();
            System.out.println("Archivo seleccionado: " + rutaAplicacion);

            // Mostrar la imagen en el JLabel
            ImageIcon imagen = new ImageIcon(rutaAplicacion);
            lblSubirFoto.setText("");
            lblSubirFoto.setIcon(new ImageIcon(imagen.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH)));
        }

    }//GEN-LAST:event_lblSubirFotoMouseClicked

    private void lblRegresarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblRegresarMouseClicked
        FrmConsumiblesAdmin consumiblesAdmin = new FrmConsumiblesAdmin();
        consumiblesAdmin.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_lblRegresarMouseClicked

    private void lblHomeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblHomeMouseClicked
        FrmPrincipal principal = new FrmPrincipal();
        principal.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_lblHomeMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {

            javax.swing.UIManager.setLookAndFeel(new FlatLightLaf());

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrmConsumibleRegistrar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FrmConsumibleRegistrar().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAnadir;
    private javax.swing.JTextField cajaExistencias;
    private javax.swing.JTextField cajaGabinete;
    private javax.swing.JTextField cajaMarca;
    private javax.swing.JTextField cajaNombre;
    private javax.swing.JTextField cajaObservaciones;
    private javax.swing.JTextField cajaReferencia;
    private javax.swing.JCheckBox chkDisponible;
    private javax.swing.JPanel jpContainer;
    private javax.swing.JPanel jpExistencias;
    private javax.swing.JPanel jpFoto;
    private javax.swing.JPanel jpGabinete;
    private javax.swing.JPanel jpHome;
    private javax.swing.JPanel jpImagenLat;
    private javax.swing.JPanel jpLateral;
    private javax.swing.JPanel jpLogo;
    private javax.swing.JPanel jpMarca;
    private javax.swing.JPanel jpNombre;
    private javax.swing.JPanel jpObservaciones;
    private javax.swing.JPanel jpReferencia;
    private javax.swing.JPanel jpRegresar;
    private javax.swing.JLabel lblDatos;
    private javax.swing.JLabel lblExistencias;
    private javax.swing.JLabel lblFoto;
    private javax.swing.JLabel lblGabinete;
    private javax.swing.JLabel lblHome;
    private javax.swing.JLabel lblLogo;
    private javax.swing.JLabel lblMarca;
    private javax.swing.JLabel lblNombre;
    private javax.swing.JLabel lblObservaciones;
    private javax.swing.JLabel lblReferencia;
    private javax.swing.JLabel lblRegresar;
    private javax.swing.JLabel lblSubirFoto;
    private javax.swing.JLabel lblTitulo;
    // End of variables declaration//GEN-END:variables
}
