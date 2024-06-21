package com.scientia.forms;

import com.formdev.flatlaf.FlatLightLaf;
import com.scientia.utils.ImageScale;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

/**
 *
 * @author danim
 */
public class FrmPrincipal extends javax.swing.JFrame {

    public FrmPrincipal() {
        initComponents();

        //máximizar pantalla
        this.setExtendedState(MAXIMIZED_BOTH);
        loadDesign();

        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                resizeComponents();
            }
        });
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

        lblTitle.setBounds(0, 30, (int) (width - anchoLateral), (int) (height * 0.1));

        btnPrestamos.setBounds(70, 120, (int) (width * 0.15), (int) (height * 0.052));

        //ESTUDIANTES
        jpEstudiantes.setBounds(70, 180, (int) (width * 0.23), (int) (height * 0.3));
        lblImgEstud.setBounds(10, 10, (int) (width * 0.215), (int) (height * 0.15));
        lblConsultaEl.setBounds(10, 140, (int) (width * 0.215), (int) (height * 0.03));
        lblConsultaEl1.setBounds(10, 160, (int) (width * 0.215), (int) (height * 0.03));
        btnEstudiantes.setBounds(95, 190, (int) (width * 0.1), (int) (height * 0.05));

        //GRUPOS
        jpGrupos.setBounds(500, 180, (int) (width * 0.23), (int) (height * 0.3));
        lblImgGrup.setBounds(10, 10, (int) (width * 0.215), (int) (height * 0.15));
        lblConsultaEl6.setBounds(10, 140, (int) (width * 0.215), (int) (height * 0.03));
        lblConsultaEl7.setBounds(10, 160, (int) (width * 0.215), (int) (height * 0.03));
        btnGrupos.setBounds(95, 190, (int) (width * 0.1), (int) (height * 0.05));

        //LABORATORIOS
        jpLaboratorios.setBounds(950, 180, (int) (width * 0.23), (int) (height * 0.3));
        lblImgLab.setBounds(10, 10, (int) (width * 0.215), (int) (height * 0.15));
        lblConsultaEl4.setBounds(10, 140, (int) (width * 0.215), (int) (height * 0.03));
        lblConsultaEl5.setBounds(10, 160, (int) (width * 0.215), (int) (height * 0.03));
        btnLaboratorios.setBounds(88, 190, (int) (width * 0.13), (int) (height * 0.05));

        lblInventario.setBounds(70, 450, (int) (width * 0.15), (int) (height * 0.052));

        //EQUIPOS
        jpEquipos.setBounds(70, 500, (int) (width * 0.23), (int) (height * 0.3));
        lblImgEqui.setBounds(10, 10, (int) (width * 0.215), (int) (height * 0.15));
        lblConsultaEl2.setBounds(10, 140, (int) (width * 0.215), (int) (height * 0.03));
        lblConsultaEl3.setBounds(10, 160, (int) (width * 0.215), (int) (height * 0.03));
        btnEquipos.setBounds(95, 190, (int) (width * 0.1), (int) (height * 0.05));

        //ELEMENTOS
        jpEle.setBounds(500, 500, (int) (width * 0.23), (int) (height * 0.3));
        lblImgEle.setBounds(10, 10, (int) (width * 0.215), (int) (height * 0.15));
        lblConsultaEl8.setBounds(10, 140, (int) (width * 0.215), (int) (height * 0.03));
        lblConsultaEl9.setBounds(10, 160, (int) (width * 0.215), (int) (height * 0.03));
        btnElementos.setBounds(95, 190, (int) (width * 0.1), (int) (height * 0.05));

        //CONSUMIBLES
        jpConsumible.setBounds(950, 500, (int) (width * 0.23), (int) (height * 0.3));
        lblImgConsumible.setBounds(10, 10, (int) (width * 0.215), (int) (height * 0.15));
        lblConsultaEl10.setBounds(10, 140, (int) (width * 0.215), (int) (height * 0.03));
        lblConsultaEl11.setBounds(10, 160, (int) (width * 0.215), (int) (height * 0.03));
        btnConsumible.setBounds(88, 190, (int) (width * 0.13), (int) (height * 0.05));

        // Si el panel tiene un tamaño inicial, asegúrate de que no sea más pequeño que eso
        // Revalidar y repintar el contenedor para aplicar los cambios
        this.revalidate();
        this.repaint();
    }

    private void loadDesign() {

        try {
            //IMAGEN PARA LA BARRA DE TAREAS
            String rutaIcon = "/com/scientia/icons/scientiaLogo.png";
            setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource(rutaIcon)));

            //LOGO
            String nomLogo = "..\\ScientiaLab\\src\\com\\scientia\\icons\\logo.png";
            System.out.println(nomLogo);
            ImageScale.setImageToLabel(lblLogo, nomLogo);

            //ESTUDIANTES
            URL imgEstud = new URL("https://cdn-icons-png.flaticon.com/512/3135/3135810.png");
            Image estuImg = new ImageIcon(imgEstud).getImage().getScaledInstance(100, 100, 0);
            ImageIcon iconoEstu = new ImageIcon(estuImg);
            lblImgEstud.setIcon(iconoEstu);

            //GRUPOS
            URL imgGrup = new URL("https://cdn-icons-png.flaticon.com/128/4170/4170623.png");
            Image grupImg = new ImageIcon(imgGrup).getImage().getScaledInstance(100, 100, 0);
            ImageIcon iconoGrup = new ImageIcon(grupImg);
            lblImgGrup.setIcon(iconoGrup);

            //LABORATORIOS
            URL imgLab = new URL("https://cdn-icons-png.flaticon.com/512/4902/4902981.png");
            Image labImg = new ImageIcon(imgLab).getImage().getScaledInstance(100, 100, 0);
            ImageIcon iconoLab = new ImageIcon(labImg);
            lblImgLab.setIcon(iconoLab);

            //EQUIPOS
            URL imgEqui = new URL("https://cdn-icons-png.flaticon.com/512/4631/4631814.png");
            Image equiImg = new ImageIcon(imgEqui).getImage().getScaledInstance(100, 100, 0);
            ImageIcon iconoEqui = new ImageIcon(equiImg);
            lblImgEqui.setIcon(iconoEqui);

            //ELEMENTOS
            URL imgEle = new URL("https://cdn-icons-png.flaticon.com/512/1753/1753487.png");
            Image EleImg = new ImageIcon(imgEle).getImage().getScaledInstance(100, 100, 0);
            ImageIcon iconoEle = new ImageIcon(EleImg);
            lblImgEle.setIcon(iconoEle);

            //CONSUMIBLES
            URL imgConsum = new URL("https://cdn-icons-png.flaticon.com/512/1687/1687513.png");
            Image ConsumImg = new ImageIcon(imgConsum).getImage().getScaledInstance(100, 100, 0);
            ImageIcon iconoConsum = new ImageIcon(ConsumImg);
            lblImgConsumible.setIcon(iconoConsum);

        } catch (MalformedURLException ex) {
            Logger.getLogger(FrmPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void mostrarVentana(JFrame ventanaNueva) {
        ventanaNueva.setVisible(true);
        this.setVisible(false);
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
        jpContainer = new javax.swing.JPanel();
        lblInventario = new javax.swing.JLabel();
        lblTitle = new javax.swing.JLabel();
        jpGrupos = new javax.swing.JPanel();
        lblConsultaEl6 = new javax.swing.JLabel();
        lblConsultaEl7 = new javax.swing.JLabel();
        lblImgGrup = new javax.swing.JLabel();
        btnGrupos = new javax.swing.JButton();
        jpLaboratorios = new javax.swing.JPanel();
        lblConsultaEl4 = new javax.swing.JLabel();
        lblConsultaEl5 = new javax.swing.JLabel();
        lblImgLab = new javax.swing.JLabel();
        btnLaboratorios = new javax.swing.JButton();
        jpEstudiantes = new javax.swing.JPanel();
        lblConsultaEl = new javax.swing.JLabel();
        lblConsultaEl1 = new javax.swing.JLabel();
        lblImgEstud = new javax.swing.JLabel();
        btnEstudiantes = new javax.swing.JButton();
        jpConsumible = new javax.swing.JPanel();
        lblConsultaEl10 = new javax.swing.JLabel();
        lblConsultaEl11 = new javax.swing.JLabel();
        btnConsumible = new javax.swing.JButton();
        lblImgConsumible = new javax.swing.JLabel();
        jpEle = new javax.swing.JPanel();
        lblConsultaEl8 = new javax.swing.JLabel();
        lblConsultaEl9 = new javax.swing.JLabel();
        btnElementos = new javax.swing.JButton();
        lblImgEle = new javax.swing.JLabel();
        jpEquipos = new javax.swing.JPanel();
        lblConsultaEl2 = new javax.swing.JLabel();
        lblConsultaEl3 = new javax.swing.JLabel();
        btnEquipos = new javax.swing.JButton();
        lblImgEqui = new javax.swing.JLabel();
        btnPrestamos = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);

        jpLateral.setBackground(new java.awt.Color(26, 49, 76));

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
                .addComponent(lblLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jpLateralLayout = new javax.swing.GroupLayout(jpLateral);
        jpLateral.setLayout(jpLateralLayout);
        jpLateralLayout.setHorizontalGroup(
            jpLateralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpLateralLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jpLogo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jpLateralLayout.setVerticalGroup(
            jpLateralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpLateralLayout.createSequentialGroup()
                .addComponent(jpLogo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 579, Short.MAX_VALUE))
        );

        getContentPane().add(jpLateral);
        jpLateral.setBounds(0, 0, 0, 0);

        jpContainer.setBackground(new java.awt.Color(232, 236, 239));
        jpContainer.setLayout(null);

        lblInventario.setFont(new java.awt.Font("Montserrat", 1, 24)); // NOI18N
        lblInventario.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblInventario.setText("Inventario");
        jpContainer.add(lblInventario);
        lblInventario.setBounds(30, 370, 200, 30);

        lblTitle.setFont(new java.awt.Font("Montserrat", 1, 60)); // NOI18N
        lblTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitle.setText("ScientiaLab");
        jpContainer.add(lblTitle);
        lblTitle.setBounds(0, 22, 924, 75);

        jpGrupos.setBackground(new java.awt.Color(255, 255, 255));
        jpGrupos.setLayout(null);

        lblConsultaEl6.setFont(new java.awt.Font("Montserrat", 1, 18)); // NOI18N
        lblConsultaEl6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblConsultaEl6.setText("Consulte el listado completo");
        jpGrupos.add(lblConsultaEl6);
        lblConsultaEl6.setBounds(20, 110, 170, 23);

        lblConsultaEl7.setFont(new java.awt.Font("Montserrat", 1, 18)); // NOI18N
        lblConsultaEl7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblConsultaEl7.setText("de grupos");
        jpGrupos.add(lblConsultaEl7);
        lblConsultaEl7.setBounds(20, 130, 170, 23);

        lblImgGrup.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jpGrupos.add(lblImgGrup);
        lblImgGrup.setBounds(14, 6, 180, 93);

        btnGrupos.setBackground(new java.awt.Color(37, 206, 203));
        btnGrupos.setFont(new java.awt.Font("Montserrat", 1, 18)); // NOI18N
        btnGrupos.setForeground(new java.awt.Color(255, 255, 255));
        btnGrupos.setText("Grupos");
        btnGrupos.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnGrupos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGruposActionPerformed(evt);
            }
        });
        jpGrupos.add(btnGrupos);
        btnGrupos.setBounds(20, 150, 160, 30);

        jpContainer.add(jpGrupos);
        jpGrupos.setBounds(360, 160, 220, 190);

        jpLaboratorios.setBackground(new java.awt.Color(255, 255, 255));
        jpLaboratorios.setLayout(null);

        lblConsultaEl4.setFont(new java.awt.Font("Montserrat", 1, 18)); // NOI18N
        lblConsultaEl4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblConsultaEl4.setText("Consulte el listado completo");
        jpLaboratorios.add(lblConsultaEl4);
        lblConsultaEl4.setBounds(20, 110, 170, 23);

        lblConsultaEl5.setFont(new java.awt.Font("Montserrat", 1, 18)); // NOI18N
        lblConsultaEl5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblConsultaEl5.setText("de laboratorios");
        jpLaboratorios.add(lblConsultaEl5);
        lblConsultaEl5.setBounds(20, 130, 170, 23);

        lblImgLab.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jpLaboratorios.add(lblImgLab);
        lblImgLab.setBounds(14, 6, 180, 93);

        btnLaboratorios.setBackground(new java.awt.Color(37, 206, 203));
        btnLaboratorios.setFont(new java.awt.Font("Montserrat", 1, 18)); // NOI18N
        btnLaboratorios.setForeground(new java.awt.Color(255, 255, 255));
        btnLaboratorios.setText("Laboratorios");
        btnLaboratorios.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnLaboratorios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLaboratoriosActionPerformed(evt);
            }
        });
        jpLaboratorios.add(btnLaboratorios);
        btnLaboratorios.setBounds(20, 150, 160, 30);

        jpContainer.add(jpLaboratorios);
        jpLaboratorios.setBounds(690, 160, 220, 190);

        jpEstudiantes.setBackground(new java.awt.Color(255, 255, 255));
        jpEstudiantes.setLayout(null);

        lblConsultaEl.setFont(new java.awt.Font("Montserrat", 1, 18)); // NOI18N
        lblConsultaEl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblConsultaEl.setText("Consulte el listado completo");
        jpEstudiantes.add(lblConsultaEl);
        lblConsultaEl.setBounds(20, 110, 170, 23);

        lblConsultaEl1.setFont(new java.awt.Font("Montserrat", 1, 18)); // NOI18N
        lblConsultaEl1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblConsultaEl1.setText("de estudiantes");
        jpEstudiantes.add(lblConsultaEl1);
        lblConsultaEl1.setBounds(20, 130, 170, 23);

        lblImgEstud.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jpEstudiantes.add(lblImgEstud);
        lblImgEstud.setBounds(14, 6, 180, 93);

        btnEstudiantes.setBackground(new java.awt.Color(37, 206, 203));
        btnEstudiantes.setFont(new java.awt.Font("Montserrat", 1, 18)); // NOI18N
        btnEstudiantes.setForeground(new java.awt.Color(255, 255, 255));
        btnEstudiantes.setText("Estudiantes");
        btnEstudiantes.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEstudiantes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEstudiantesActionPerformed(evt);
            }
        });
        jpEstudiantes.add(btnEstudiantes);
        btnEstudiantes.setBounds(30, 150, 150, 30);

        jpContainer.add(jpEstudiantes);
        jpEstudiantes.setBounds(28, 162, 220, 190);

        jpConsumible.setBackground(new java.awt.Color(255, 255, 255));
        jpConsumible.setLayout(null);

        lblConsultaEl10.setFont(new java.awt.Font("Montserrat", 1, 18)); // NOI18N
        lblConsultaEl10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblConsultaEl10.setText("Consulte el listado completo");
        jpConsumible.add(lblConsultaEl10);
        lblConsultaEl10.setBounds(6, 84, 200, 23);

        lblConsultaEl11.setFont(new java.awt.Font("Montserrat", 1, 18)); // NOI18N
        lblConsultaEl11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblConsultaEl11.setText("de consumibles");
        jpConsumible.add(lblConsultaEl11);
        lblConsultaEl11.setBounds(6, 113, 200, 23);

        btnConsumible.setBackground(new java.awt.Color(37, 206, 203));
        btnConsumible.setFont(new java.awt.Font("Montserrat", 1, 18)); // NOI18N
        btnConsumible.setForeground(new java.awt.Color(255, 255, 255));
        btnConsumible.setText("Consumibles");
        btnConsumible.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnConsumible.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConsumibleActionPerformed(evt);
            }
        });
        jpConsumible.add(btnConsumible);
        btnConsumible.setBounds(30, 150, 150, 30);

        lblImgConsumible.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jpConsumible.add(lblImgConsumible);
        lblImgConsumible.setBounds(14, 6, 180, 93);

        jpContainer.add(jpConsumible);
        jpConsumible.setBounds(680, 400, 220, 230);

        jpEle.setBackground(new java.awt.Color(255, 255, 255));
        jpEle.setLayout(null);

        lblConsultaEl8.setFont(new java.awt.Font("Montserrat", 1, 18)); // NOI18N
        lblConsultaEl8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblConsultaEl8.setText("Consulte el listado completo");
        jpEle.add(lblConsultaEl8);
        lblConsultaEl8.setBounds(6, 84, 200, 23);

        lblConsultaEl9.setFont(new java.awt.Font("Montserrat", 1, 18)); // NOI18N
        lblConsultaEl9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblConsultaEl9.setText("de elementos");
        jpEle.add(lblConsultaEl9);
        lblConsultaEl9.setBounds(6, 113, 200, 23);

        btnElementos.setBackground(new java.awt.Color(37, 206, 203));
        btnElementos.setFont(new java.awt.Font("Montserrat", 1, 18)); // NOI18N
        btnElementos.setForeground(new java.awt.Color(255, 255, 255));
        btnElementos.setText("Elementos");
        btnElementos.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnElementos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnElementosActionPerformed(evt);
            }
        });
        jpEle.add(btnElementos);
        btnElementos.setBounds(30, 150, 150, 30);

        lblImgEle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jpEle.add(lblImgEle);
        lblImgEle.setBounds(14, 6, 180, 93);

        jpContainer.add(jpEle);
        jpEle.setBounds(360, 400, 220, 230);

        jpEquipos.setBackground(new java.awt.Color(255, 255, 255));
        jpEquipos.setLayout(null);

        lblConsultaEl2.setFont(new java.awt.Font("Montserrat", 1, 18)); // NOI18N
        lblConsultaEl2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblConsultaEl2.setText("Consulte el listado completo");
        jpEquipos.add(lblConsultaEl2);
        lblConsultaEl2.setBounds(6, 84, 200, 23);

        lblConsultaEl3.setFont(new java.awt.Font("Montserrat", 1, 18)); // NOI18N
        lblConsultaEl3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblConsultaEl3.setText("de equipos");
        jpEquipos.add(lblConsultaEl3);
        lblConsultaEl3.setBounds(6, 113, 200, 23);

        btnEquipos.setBackground(new java.awt.Color(37, 206, 203));
        btnEquipos.setFont(new java.awt.Font("Montserrat", 1, 18)); // NOI18N
        btnEquipos.setForeground(new java.awt.Color(255, 255, 255));
        btnEquipos.setText("Equipos");
        btnEquipos.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEquipos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEquiposActionPerformed(evt);
            }
        });
        jpEquipos.add(btnEquipos);
        btnEquipos.setBounds(30, 150, 150, 30);

        lblImgEqui.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jpEquipos.add(lblImgEqui);
        lblImgEqui.setBounds(14, 6, 180, 93);

        jpContainer.add(jpEquipos);
        jpEquipos.setBounds(30, 410, 220, 230);

        btnPrestamos.setBackground(new java.awt.Color(37, 206, 203));
        btnPrestamos.setFont(new java.awt.Font("Montserrat", 1, 24)); // NOI18N
        btnPrestamos.setForeground(new java.awt.Color(255, 255, 255));
        btnPrestamos.setText("Préstamos");
        btnPrestamos.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnPrestamos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrestamosActionPerformed(evt);
            }
        });
        jpContainer.add(btnPrestamos);
        btnPrestamos.setBounds(30, 100, 200, 40);

        getContentPane().add(jpContainer);
        jpContainer.setBounds(110, 0, 930, 680);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnPrestamosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrestamosActionPerformed
        FrmPrestamosAdmin presAdmin = new FrmPrestamosAdmin();
        presAdmin.setVisible(true);
        this.setVisible(false);

    }//GEN-LAST:event_btnPrestamosActionPerformed

    private void btnConsumibleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConsumibleActionPerformed
        FrmConsumiblesAdmin consumiblesAdmin = new FrmConsumiblesAdmin();
        consumiblesAdmin.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_btnConsumibleActionPerformed

    private void btnLaboratoriosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLaboratoriosActionPerformed
        FrmLaboratorios laboratorios = new FrmLaboratorios();
        laboratorios.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_btnLaboratoriosActionPerformed

    private void btnEstudiantesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEstudiantesActionPerformed
        FrmEstudiantes estudiantes = new FrmEstudiantes();
        estudiantes.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_btnEstudiantesActionPerformed

    private void btnGruposActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGruposActionPerformed
        FrmGrupos grupos = new FrmGrupos();
        grupos.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_btnGruposActionPerformed

    private void btnEquiposActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEquiposActionPerformed
        FrmEquiposAdmin equiposAdmin = new FrmEquiposAdmin();
        equiposAdmin.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_btnEquiposActionPerformed

    private void btnElementosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnElementosActionPerformed
        FrmElementosAdmin elementosAdmin = new FrmElementosAdmin();
        elementosAdmin.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_btnElementosActionPerformed

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
            java.util.logging.Logger.getLogger(FrmGrupos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FrmPrincipal().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnConsumible;
    private javax.swing.JButton btnElementos;
    private javax.swing.JButton btnEquipos;
    private javax.swing.JButton btnEstudiantes;
    private javax.swing.JButton btnGrupos;
    private javax.swing.JButton btnLaboratorios;
    private javax.swing.JButton btnPrestamos;
    private javax.swing.JPanel jpConsumible;
    private javax.swing.JPanel jpContainer;
    private javax.swing.JPanel jpEle;
    private javax.swing.JPanel jpEquipos;
    private javax.swing.JPanel jpEstudiantes;
    private javax.swing.JPanel jpGrupos;
    private javax.swing.JPanel jpLaboratorios;
    private javax.swing.JPanel jpLateral;
    private javax.swing.JPanel jpLogo;
    private javax.swing.JLabel lblConsultaEl;
    private javax.swing.JLabel lblConsultaEl1;
    private javax.swing.JLabel lblConsultaEl10;
    private javax.swing.JLabel lblConsultaEl11;
    private javax.swing.JLabel lblConsultaEl2;
    private javax.swing.JLabel lblConsultaEl3;
    private javax.swing.JLabel lblConsultaEl4;
    private javax.swing.JLabel lblConsultaEl5;
    private javax.swing.JLabel lblConsultaEl6;
    private javax.swing.JLabel lblConsultaEl7;
    private javax.swing.JLabel lblConsultaEl8;
    private javax.swing.JLabel lblConsultaEl9;
    private javax.swing.JLabel lblImgConsumible;
    private javax.swing.JLabel lblImgEle;
    private javax.swing.JLabel lblImgEqui;
    private javax.swing.JLabel lblImgEstud;
    private javax.swing.JLabel lblImgGrup;
    private javax.swing.JLabel lblImgLab;
    private javax.swing.JLabel lblInventario;
    private javax.swing.JLabel lblLogo;
    private javax.swing.JLabel lblTitle;
    // End of variables declaration//GEN-END:variables
}
