package com.scientia.forms;

import com.formdev.flatlaf.FlatLightLaf;
import com.scientia.daos.DaoEquipo;
import com.scientia.entidades.Equipo;
import com.scientia.utils.ImageScale;
import com.scientia.utils.PlaceHolder;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 *
 * @author danim
 */
public class FrmEquiposRegistrar extends javax.swing.JFrame {

    public FrmEquiposRegistrar() {
        initComponents();
        borrarDatos();
        //máximizar pantalla
        this.setExtendedState(MAXIMIZED_BOTH);
        jpNombre.setBounds(0, 0, 50, 5);
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
        jpModelo.setBounds(15, 350, (int) (width * 0.23), (int) (height * 0.09));
        jpSerie.setBounds(15, 450, (int) (width * 0.23), (int) (height * 0.09));
        jpInventario.setBounds(15, 550, (int) (width * 0.23), (int) (height * 0.09));

        jpMantenimiento.setBounds(400, 250, (int) (width * 0.25), (int) (height * 0.09));
        jpReq.setBounds(400, 350, (int) (width * 0.25), (int) (height * 0.09));
        jpEstado.setBounds(400, 450, (int) (width * 0.25), (int) (height * 0.09));

        btnAnadir.setBounds(470, 565, (int) (width * 0.15), (int) (height * 0.052));
        jpImagenLat.setBounds(width - 600, 0, (int) (width * 0.7), height);

        // Revalidar y repintar el contenedor para aplicar los cambios
        this.revalidate();
        this.repaint();
    }

    private void borrarDatos() {
        cajaInventario.setText("");
        cajaModelo.setText("");
        cajaSerie.setText("");
        cmbMantenimiento.setSelectedIndex(0);
        cmbReq.setSelectedIndex(0);
        cmbEstado.setSelectedIndex(0);
        cajaNombre.setText("");
        cajaMarca.setText("");
        chkDisponible.setSelected(false);
        cajaExistencias.setText("");
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
        PlaceHolder modelo = new PlaceHolder("ME-123", cajaModelo);
        PlaceHolder existencias = new PlaceHolder("0", cajaExistencias);
        PlaceHolder serie = new PlaceHolder("0", cajaSerie);
    }

    private Boolean estaTodoBien() {
        String codInventario;
        String modelo;
        String serie;
        String mantenimiento;
        String reqMante;
        String estado;
        String nombre;
        String marca;
        Boolean disponibilidad;
        Integer existencias;
        Boolean bandera = true;

        codInventario = cajaInventario.getText();
        if (codInventario.equals("")) {
            cajaInventario.requestFocus();
            bandera = false;
            JOptionPane.showMessageDialog(jpContainer, "Digite el código de inventario del equipo", "ADVERTENCIA", JOptionPane.WARNING_MESSAGE);
        }
        modelo = cajaModelo.getText();
        if (modelo.equals("")) {
            cajaModelo.requestFocus();
            bandera = false;
            JOptionPane.showMessageDialog(jpContainer, "Digite el modelo del equipo", "ADVERTENCIA", JOptionPane.WARNING_MESSAGE);
        }

        serie = cajaSerie.getText();
        if (serie.equals("")) {
            cajaSerie.requestFocus();
            bandera = false;
            JOptionPane.showMessageDialog(jpContainer, "Digite la serie del equipo", "ADVERTENCIA", JOptionPane.WARNING_MESSAGE);
        }

        mantenimiento = (String) cmbMantenimiento.getSelectedItem();
        if (mantenimiento == null || mantenimiento.equals("")) {
            cmbMantenimiento.requestFocus();
            bandera = false;
            JOptionPane.showMessageDialog(jpContainer, "Seleccione el mantenimiento del equipo", "ADVERTENCIA", JOptionPane.WARNING_MESSAGE);
        }

        reqMante = (String) cmbReq.getSelectedItem();
        if (reqMante == null || reqMante.equals("")) {
            cmbReq.requestFocus();
            bandera = false;
            JOptionPane.showMessageDialog(jpContainer, "Seleccione el mantenimiento del equipo", "ADVERTENCIA", JOptionPane.WARNING_MESSAGE);
        }

        estado = (String) cmbEstado.getSelectedItem();
        if (estado == null || estado.equals("")) {
            cmbEstado.requestFocus();
            bandera = false;
            JOptionPane.showMessageDialog(jpContainer, "Seleccione el mantenimiento del equipo", "ADVERTENCIA", JOptionPane.WARNING_MESSAGE);
        }

        nombre = cajaNombre.getText();
        if (nombre.equals("")) {
            cajaNombre.requestFocus();
            bandera = false;
            JOptionPane.showMessageDialog(jpContainer, "Digite el nombre del equipo", "ADVERTENCIA", JOptionPane.WARNING_MESSAGE);
        }

        marca = cajaMarca.getText();
        if (marca.equals("")) {
            cajaMarca.requestFocus();
            bandera = false;
            JOptionPane.showMessageDialog(jpContainer, "Digite la marca del equipo", "ADVERTENCIA", JOptionPane.WARNING_MESSAGE);
        }

        disponibilidad = chkDisponible.isSelected();

        try {
            existencias = Integer.valueOf(cajaExistencias.getText());
            if (existencias < 0) {
                cajaExistencias.requestFocus();
                bandera = false;
                JOptionPane.showMessageDialog(jpContainer, "Digite una cantidad válida de existencias", "ADVERTENCIA", JOptionPane.WARNING_MESSAGE);
            }
        } catch (HeadlessException | NumberFormatException e) {
            bandera = false;
            JOptionPane.showMessageDialog(jpContainer, "Escribe el número de existencias", "ADVERTENCIA", JOptionPane.WARNING_MESSAGE);
        }
        return bandera;
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
        jpSerie = new javax.swing.JPanel();
        lblSerie = new javax.swing.JLabel();
        cajaSerie = new javax.swing.JTextField();
        jpMarca = new javax.swing.JPanel();
        lblMarca = new javax.swing.JLabel();
        cajaMarca = new javax.swing.JTextField();
        jpNombre = new javax.swing.JPanel();
        lblNombre = new javax.swing.JLabel();
        cajaNombre = new javax.swing.JTextField();
        btnAnadir = new javax.swing.JButton();
        lblDatos = new javax.swing.JLabel();
        jpModelo = new javax.swing.JPanel();
        lblModelo = new javax.swing.JLabel();
        cajaModelo = new javax.swing.JTextField();
        chkDisponible = new javax.swing.JCheckBox();
        jpRegresar = new javax.swing.JPanel();
        lblRegresar = new javax.swing.JLabel();
        jpExistencias = new javax.swing.JPanel();
        lblExistencias = new javax.swing.JLabel();
        cajaExistencias = new javax.swing.JTextField();
        jpImagenLat = new javax.swing.JPanel();
        jpMantenimiento = new javax.swing.JPanel();
        lblMantenimiento = new javax.swing.JLabel();
        cmbMantenimiento = new javax.swing.JComboBox<>();
        jpReq = new javax.swing.JPanel();
        lblReq = new javax.swing.JLabel();
        cmbReq = new javax.swing.JComboBox<>();
        jpEstado = new javax.swing.JPanel();
        lblEstado = new javax.swing.JLabel();
        cmbEstado = new javax.swing.JComboBox<>();
        jpInventario = new javax.swing.JPanel();
        lblInventario = new javax.swing.JLabel();
        cajaInventario = new javax.swing.JTextField();

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
        lblTitulo.setText("Añadir Nuevo Equipo");
        jpContainer.add(lblTitulo);
        lblTitulo.setBounds(20, 20, 830, 75);

        jpSerie.setBackground(new java.awt.Color(255, 255, 255));

        lblSerie.setFont(new java.awt.Font("Montserrat SemiBold", 0, 14)); // NOI18N
        lblSerie.setForeground(new java.awt.Color(98, 106, 109));
        lblSerie.setText("Serie:");

        cajaSerie.setFont(new java.awt.Font("Montserrat SemiBold", 1, 24)); // NOI18N
        cajaSerie.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        cajaSerie.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cajaSerieActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpSerieLayout = new javax.swing.GroupLayout(jpSerie);
        jpSerie.setLayout(jpSerieLayout);
        jpSerieLayout.setHorizontalGroup(
            jpSerieLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpSerieLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jpSerieLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpSerieLayout.createSequentialGroup()
                        .addComponent(lblSerie)
                        .addGap(0, 193, Short.MAX_VALUE))
                    .addComponent(cajaSerie))
                .addContainerGap())
        );
        jpSerieLayout.setVerticalGroup(
            jpSerieLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpSerieLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblSerie)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cajaSerie, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jpContainer.add(jpSerie);
        jpSerie.setBounds(10, 420, 250, 70);

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
        btnAnadir.setText("Añadir Equipo");
        btnAnadir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnadirActionPerformed(evt);
            }
        });
        jpContainer.add(btnAnadir);
        btnAnadir.setBounds(480, 580, 190, 30);

        lblDatos.setFont(new java.awt.Font("Montserrat", 1, 18)); // NOI18N
        lblDatos.setText("Datos Generales:");
        jpContainer.add(lblDatos);
        lblDatos.setBounds(20, 110, 170, 23);

        jpModelo.setBackground(new java.awt.Color(255, 255, 255));

        lblModelo.setFont(new java.awt.Font("Montserrat SemiBold", 0, 14)); // NOI18N
        lblModelo.setForeground(new java.awt.Color(98, 106, 109));
        lblModelo.setText("Modelo:");

        cajaModelo.setFont(new java.awt.Font("Montserrat SemiBold", 1, 24)); // NOI18N
        cajaModelo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        cajaModelo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cajaModeloActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpModeloLayout = new javax.swing.GroupLayout(jpModelo);
        jpModelo.setLayout(jpModeloLayout);
        jpModeloLayout.setHorizontalGroup(
            jpModeloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpModeloLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jpModeloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpModeloLayout.createSequentialGroup()
                        .addComponent(lblModelo)
                        .addGap(0, 175, Short.MAX_VALUE))
                    .addComponent(cajaModelo))
                .addContainerGap())
        );
        jpModeloLayout.setVerticalGroup(
            jpModeloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpModeloLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblModelo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cajaModelo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(8, Short.MAX_VALUE))
        );

        jpContainer.add(jpModelo);
        jpModelo.setBounds(10, 330, 250, 70);

        chkDisponible.setFont(new java.awt.Font("Montserrat", 1, 18)); // NOI18N
        chkDisponible.setText("Disponible");
        chkDisponible.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkDisponibleActionPerformed(evt);
            }
        });
        jpContainer.add(chkDisponible);
        chkDisponible.setBounds(540, 180, 130, 27);

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

        jpMantenimiento.setBackground(new java.awt.Color(255, 255, 255));

        lblMantenimiento.setFont(new java.awt.Font("Montserrat SemiBold", 0, 14)); // NOI18N
        lblMantenimiento.setForeground(new java.awt.Color(98, 106, 109));
        lblMantenimiento.setText("Mantenimiento:");

        cmbMantenimiento.setFont(new java.awt.Font("Montserrat", 1, 18)); // NOI18N
        cmbMantenimiento.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Elije una opción...", "C", "P", "CL" }));
        cmbMantenimiento.setBorder(null);
        cmbMantenimiento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbMantenimientoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpMantenimientoLayout = new javax.swing.GroupLayout(jpMantenimiento);
        jpMantenimiento.setLayout(jpMantenimientoLayout);
        jpMantenimientoLayout.setHorizontalGroup(
            jpMantenimientoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpMantenimientoLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(lblMantenimiento)
                .addContainerGap(264, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpMantenimientoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cmbMantenimiento, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jpMantenimientoLayout.setVerticalGroup(
            jpMantenimientoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpMantenimientoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblMantenimiento)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbMantenimiento, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE)
                .addContainerGap())
        );

        jpContainer.add(jpMantenimiento);
        jpMantenimiento.setBounds(280, 240, 390, 70);

        jpReq.setBackground(new java.awt.Color(255, 255, 255));

        lblReq.setFont(new java.awt.Font("Montserrat SemiBold", 0, 14)); // NOI18N
        lblReq.setForeground(new java.awt.Color(98, 106, 109));
        lblReq.setText("Req. de Mantenimiento:");

        cmbReq.setFont(new java.awt.Font("Montserrat", 1, 18)); // NOI18N
        cmbReq.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Elije una opción...", "Semestral", "Bianual", "Anual" }));
        cmbReq.setBorder(null);
        cmbReq.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbReqActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpReqLayout = new javax.swing.GroupLayout(jpReq);
        jpReq.setLayout(jpReqLayout);
        jpReqLayout.setHorizontalGroup(
            jpReqLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpReqLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(lblReq)
                .addContainerGap(205, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpReqLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cmbReq, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jpReqLayout.setVerticalGroup(
            jpReqLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpReqLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblReq)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbReq, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE)
                .addContainerGap())
        );

        jpContainer.add(jpReq);
        jpReq.setBounds(280, 330, 390, 70);

        jpEstado.setBackground(new java.awt.Color(255, 255, 255));

        lblEstado.setFont(new java.awt.Font("Montserrat SemiBold", 0, 14)); // NOI18N
        lblEstado.setForeground(new java.awt.Color(98, 106, 109));
        lblEstado.setText("Estado de uso:");

        cmbEstado.setFont(new java.awt.Font("Montserrat", 1, 18)); // NOI18N
        cmbEstado.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Elije una opción...", "A", "I", "DB" }));
        cmbEstado.setBorder(null);

        javax.swing.GroupLayout jpEstadoLayout = new javax.swing.GroupLayout(jpEstado);
        jpEstado.setLayout(jpEstadoLayout);
        jpEstadoLayout.setHorizontalGroup(
            jpEstadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpEstadoLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(lblEstado)
                .addContainerGap(273, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpEstadoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cmbEstado, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jpEstadoLayout.setVerticalGroup(
            jpEstadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpEstadoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblEstado)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbEstado, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE)
                .addContainerGap())
        );

        jpContainer.add(jpEstado);
        jpEstado.setBounds(280, 420, 390, 70);

        jpInventario.setBackground(new java.awt.Color(255, 255, 255));

        lblInventario.setFont(new java.awt.Font("Montserrat SemiBold", 0, 14)); // NOI18N
        lblInventario.setForeground(new java.awt.Color(98, 106, 109));
        lblInventario.setText("Cod Inventario:");

        cajaInventario.setFont(new java.awt.Font("Montserrat SemiBold", 1, 24)); // NOI18N
        cajaInventario.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        cajaInventario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cajaInventarioActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpInventarioLayout = new javax.swing.GroupLayout(jpInventario);
        jpInventario.setLayout(jpInventarioLayout);
        jpInventarioLayout.setHorizontalGroup(
            jpInventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpInventarioLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jpInventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpInventarioLayout.createSequentialGroup()
                        .addComponent(lblInventario)
                        .addGap(0, 136, Short.MAX_VALUE))
                    .addComponent(cajaInventario))
                .addContainerGap())
        );
        jpInventarioLayout.setVerticalGroup(
            jpInventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpInventarioLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblInventario)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cajaInventario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jpContainer.add(jpInventario);
        jpInventario.setBounds(20, 540, 250, 70);

        getContentPane().add(jpContainer);
        jpContainer.setBounds(110, 10, 930, 680);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cajaNombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cajaNombreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cajaNombreActionPerformed

    private void cajaMarcaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cajaMarcaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cajaMarcaActionPerformed

    private void cajaSerieActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cajaSerieActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cajaSerieActionPerformed

    private void lblHomeMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblHomeMouseEntered
        jpHome.setBackground(new Color(26, 49, 76));
        lblHome.setBackground(new Color(26, 49, 76));
    }//GEN-LAST:event_lblHomeMouseEntered

    private void lblHomeMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblHomeMouseExited
        jpHome.setBackground(Color.WHITE);
        lblHome.setBackground(Color.WHITE);
    }//GEN-LAST:event_lblHomeMouseExited

    private void cajaModeloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cajaModeloActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cajaModeloActionPerformed

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

            String codInventario, modelo, serie, mantenimiento, reqMante, estado, nombre, marca;
            Boolean disponibilidad;
            Integer existencias;

            codInventario = cajaInventario.getText();
            modelo = cajaModelo.getText();
            serie = cajaSerie.getText();
            mantenimiento = (String) cmbMantenimiento.getSelectedItem();
            reqMante = (String) cmbReq.getSelectedItem();
            estado = (String) cmbEstado.getSelectedItem();
            nombre = cajaNombre.getText();
            marca = cajaMarca.getText();
            disponibilidad = chkDisponible.isSelected();
            existencias = Integer.valueOf(cajaExistencias.getText());

            Equipo objEquipo = new Equipo();

            objEquipo.setCod_inventario_equipo(codInventario);
            objEquipo.setModelo_equipo(modelo);
            objEquipo.setSerie_equipo(serie);
            objEquipo.setMantenimiento_equipo(mantenimiento);
            objEquipo.setReq_mante_equipo(reqMante);
            objEquipo.setEstado_equipo(estado);
            objEquipo.setNombre_equipo(nombre);
            objEquipo.setMarca_equipo(marca);
            objEquipo.setDisponibilidad_equipo(disponibilidad);
            objEquipo.setExistencias_equipo(existencias);

            DaoEquipo miDao = new DaoEquipo();

            if (miDao.registrar(objEquipo)) {
                JOptionPane.showMessageDialog(jpContainer, "Se registro", "INFORMACION", JOptionPane.INFORMATION_MESSAGE);
                borrarDatos();
            } else {
                JOptionPane.showMessageDialog(jpContainer, "Error al registrar", "ERROR", JOptionPane.ERROR_MESSAGE);
            }

        }
    }//GEN-LAST:event_btnAnadirActionPerformed

    private void cajaInventarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cajaInventarioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cajaInventarioActionPerformed

    private void cmbReqActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbReqActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbReqActionPerformed

    private void cmbMantenimientoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbMantenimientoActionPerformed

    }//GEN-LAST:event_cmbMantenimientoActionPerformed

    private void lblHomeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblHomeMouseClicked
        FrmPrincipal principal = new FrmPrincipal();
        principal.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_lblHomeMouseClicked

    private void lblRegresarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblRegresarMouseClicked
        FrmEquiposAdmin equiposAdmin = new FrmEquiposAdmin();
        equiposAdmin.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_lblRegresarMouseClicked

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
            java.util.logging.Logger.getLogger(FrmEquiposRegistrar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
                new FrmEquiposRegistrar().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAnadir;
    private javax.swing.JTextField cajaExistencias;
    private javax.swing.JTextField cajaInventario;
    private javax.swing.JTextField cajaMarca;
    private javax.swing.JTextField cajaModelo;
    private javax.swing.JTextField cajaNombre;
    private javax.swing.JTextField cajaSerie;
    private javax.swing.JCheckBox chkDisponible;
    private javax.swing.JComboBox<String> cmbEstado;
    private javax.swing.JComboBox<String> cmbMantenimiento;
    private javax.swing.JComboBox<String> cmbReq;
    private javax.swing.JPanel jpContainer;
    private javax.swing.JPanel jpEstado;
    private javax.swing.JPanel jpExistencias;
    private javax.swing.JPanel jpHome;
    private javax.swing.JPanel jpImagenLat;
    private javax.swing.JPanel jpInventario;
    private javax.swing.JPanel jpLateral;
    private javax.swing.JPanel jpLogo;
    private javax.swing.JPanel jpMantenimiento;
    private javax.swing.JPanel jpMarca;
    private javax.swing.JPanel jpModelo;
    private javax.swing.JPanel jpNombre;
    private javax.swing.JPanel jpRegresar;
    private javax.swing.JPanel jpReq;
    private javax.swing.JPanel jpSerie;
    private javax.swing.JLabel lblDatos;
    private javax.swing.JLabel lblEstado;
    private javax.swing.JLabel lblExistencias;
    private javax.swing.JLabel lblHome;
    private javax.swing.JLabel lblInventario;
    private javax.swing.JLabel lblLogo;
    private javax.swing.JLabel lblMantenimiento;
    private javax.swing.JLabel lblMarca;
    private javax.swing.JLabel lblModelo;
    private javax.swing.JLabel lblNombre;
    private javax.swing.JLabel lblRegresar;
    private javax.swing.JLabel lblReq;
    private javax.swing.JLabel lblSerie;
    private javax.swing.JLabel lblTitulo;
    // End of variables declaration//GEN-END:variables
}
