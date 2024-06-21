package com.scientia.forms;

import com.formdev.flatlaf.FlatLightLaf;
import com.scientia.daos.DaoEstudiante;
import com.scientia.entidades.Estudiante;
import com.scientia.utils.ImageScale;
import com.scientia.utils.PlaceHolder;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

/**
 *
 * @author danim
 */
public class FrmEstudiantes extends javax.swing.JFrame {

    private Integer codEstudiantes = null;

    Integer seleccionarBuscar = 0;

    private String titulos[] = {"Código", "Nombre", "Apellido", "Correo", "Cod Estudiante", "Eliminar"};
    private DefaultTableModel miModeloTabla = new DefaultTableModel(titulos, 0) {

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            if (columnIndex == 5) {
                return ImageIcon.class;
            }

            return Object.class;

        }

    };

    public FrmEstudiantes() {
        initComponents();

        tabEstudiantes.setModel(miModeloTabla);
        cargarEstudiantes("");

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

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (!e.getSource().equals(tabEstudiantes)) {
                    tabEstudiantes.clearSelection();
                    editarDesactivado();
                    limpiarDatosEstudiante();
                }
            }
        });

        tabEstudiantes.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                tabEstudiantes.requestFocus();
            }
        });

        String nomLogo = "..\\ScientiaLab\\src\\com\\scientia\\icons\\logo.png";
        System.out.println(nomLogo);
        ImageScale.setImageToLabel(lblLogo, nomLogo);

    }

    private void resizeComponents() {
        Dimension frameSize = this.getSize();
        int width = frameSize.width;
        int height = frameSize.height;
        int anchoLateral = jpLateral.getWidth();

        Dimension containerSize = jpContainer.getSize();
        int widthContainer = containerSize.width;
        int heightContainer = containerSize.height;

        // Ajustar tamaños en proporción al tamaño del frame
        jpContainer.setBounds(anchoLateral, 0, (int) (width - anchoLateral), height);
        jpLateral.setBounds(0, 0, (int) (width * 0.1), height);

        jpLogo.setBounds(20, 10, (int) (width * 0.06), (int) (height * 0.09));
        jpHome.setBounds(35, 250, (int) (width * 0.055), (int) (height * 0.09));
        lblHome.setBounds(18, 0, (int) (width * 0.055), (int) (height * 0.09));

        jpSearch.setBounds(widthContainer - 325, 50, (int) (widthContainer * 0.18), (int) (heightContainer * 0.045));
        cmbSort.setBounds(widthContainer - 600, 50, (int) (widthContainer * 0.18), (int) (heightContainer * 0.045));

        jpNombre.setBounds(15, 150, (int) (width * 0.25), (int) (height * 0.09));
        jpApellido.setBounds(450, 150, (int) (width * 0.25), (int) (height * 0.09));
        jpCarnet.setBounds(870, 150, (int) (width * 0.25), (int) (height * 0.09));
        jpCorreo.setBounds(15, 250, (int) (width * 0.25), (int) (height * 0.09));

        btnAnadir.setBounds(480, 268, (int) (width * 0.1), (int) (height * 0.052));
        btnEditar.setBounds(650, 268, (int) (width * 0.1), (int) (height * 0.052));

        jSeparator1.setBounds(35, 370, (int) (width * 0.6), (int) (height * 0.052));
        btnDescargar.setBounds(1000, 350, (int) (width * 0.15), (int) (height * 0.052));
        jScrollPane1.setBounds(20, 410, (int) (width * 0.86), (int) (height * 0.45));

        // Revalidar y repintar el contenedor para aplicar los cambios
        this.revalidate();
        this.repaint();
    }

    private void loadDesign() {

        //TRAER IMAGENES
        try {
            URL imgSearch = new URL("https://cdn-icons-png.flaticon.com/512/2618/2618254.png");
            Image searchImg = new ImageIcon(imgSearch).getImage().getScaledInstance(23, 24, 0);
            ImageIcon iconoSearch = new ImageIcon(searchImg);
            lblImgSearch.setIcon(iconoSearch);

            URL imgHome = new URL("https://cdn-icons-png.flaticon.com/512/1946/1946433.png");
            Image homeImg = new ImageIcon(imgHome).getImage().getScaledInstance(50, 40, 0);
            ImageIcon iconoHome = new ImageIcon(homeImg);
            lblHome.setIcon(iconoHome);

        } catch (MalformedURLException ex) {
            Logger.getLogger(FrmPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException ex) {
            System.err.println("Failed to initialize FlatLaf");
        }

        cmbSort.setBorder(BorderFactory.createEmptyBorder());
        cmbSort.setBackground(Color.WHITE);

        editarDesactivado();

        //ESTABLECER PLACEHOLDERS
        PlaceHolder buscar = new PlaceHolder("Buscar...", cajaBuscar);
        PlaceHolder nombre = new PlaceHolder("Nombre", cajaNombre);
        PlaceHolder apellido = new PlaceHolder("Apellido", cajaApellido);
        PlaceHolder carnet = new PlaceHolder("123456", cajaCarnet);
        PlaceHolder correo = new PlaceHolder("example@email.com", cajaCorreo);

        //DISEÑO DE LA TABLA 
        JTableHeader header = tabEstudiantes.getTableHeader();
        header.setDefaultRenderer(new CustomHeaderRenderer());

    }

    static class CustomHeaderRenderer extends DefaultTableCellRenderer {

        public CustomHeaderRenderer() {
            setHorizontalAlignment(CENTER);
            setFont(new Font("Monserrat", Font.BOLD, 24)); // Cambia el tipo y tamaño de letra
            setForeground(Color.BLACK); // Cambia el color del texto
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            c.setBackground(Color.WHITE); // Cambia el color de fondo del header
            return c;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            // Pintar una línea en la parte inferior del header
            g.setColor(new Color(37, 206, 203)); // Cambia el color de la línea aquí
            g.fillRect(0, getHeight() - 1, getWidth(), 1); // Puedes ajustar el grosor de la línea aquí
        }
    }

    private void editarActivado() {
        btnEditar.setEnabled(true);
        btnAnadir.setEnabled(false);
    }

    private void editarDesactivado() {
        btnEditar.setEnabled(false);
        btnAnadir.setEnabled(true);
    }

    private Boolean estaTodoBien() {
        Boolean bandera = true;

        // Validar nomEstudiante
        String nomEstudiante = cajaNombre.getText();
        if (nomEstudiante.isEmpty()) {
            cajaNombre.requestFocus();
            bandera = false;
            JOptionPane.showMessageDialog(jpContainer, "Ingrese un nombre válido", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            // Validar apellidoEstudiante
            String apellidoEstudiante = cajaApellido.getText();
            if (apellidoEstudiante.isEmpty()) {
                cajaApellido.requestFocus();
                bandera = false;
                JOptionPane.showMessageDialog(jpContainer, "Ingrese un apellido válido", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                // Validar codUEstudiante
                String codUEstudiante = cajaCarnet.getText();
                if (codUEstudiante.isEmpty() || codUEstudiante.length() != 6) {
                    cajaCarnet.requestFocus();
                    bandera = false;
                    JOptionPane.showMessageDialog(jpContainer, "Ingrese un código universitario válido de 6 caracteres", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    // Validar correoEstudiante
                    String correoEstudiante = cajaCorreo.getText();
                    if (correoEstudiante.isEmpty() || !correoEstudiante.contains("@")) {
                        cajaCorreo.requestFocus();
                        bandera = false;
                        JOptionPane.showMessageDialog(jpContainer, "Ingrese un correo electrónico válido", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }

            }
        }

        return bandera;
    }

    private void cargarEstudiantes(String ordencito) {
        List<Estudiante> arrEstu;
        DaoEstudiante miDao = new DaoEstudiante();

        String nomElim = "/com/scientia/icons/borrar.png";
        String rutaConElim = this.getClass().getResource(nomElim).getPath();
        ImageIcon borrarIcon = new ImageIcon(rutaConElim);

        miModeloTabla.setNumRows(0);

        arrEstu = miDao.consultar(ordencito);
        arrEstu.forEach((Estu) -> {
            Object filita[] = new Object[6];
            filita[0] = Estu.getIdEstudiante();
            filita[1] = Estu.getNomEstudiante();
            filita[2] = Estu.getApellidoEstudiante();
            filita[3] = Estu.getCorreoEstudiante();
            filita[4] = Estu.getCodUEstudiante();
            filita[5] = borrarIcon;

            miModeloTabla.addRow(filita);
        });

        tabEstudiantes.getColumnModel().getColumn(0).setPreferredWidth(50);
        tabEstudiantes.getColumnModel().getColumn(1).setPreferredWidth(100);
        tabEstudiantes.getColumnModel().getColumn(2).setPreferredWidth(50);
        tabEstudiantes.getColumnModel().getColumn(3).setPreferredWidth(100);
        tabEstudiantes.getColumnModel().getColumn(4).setPreferredWidth(100);

        DefaultTableCellRenderer centrado = new DefaultTableCellRenderer();
        centrado.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < (tabEstudiantes.getColumnCount() - 1); i++) {
            tabEstudiantes.getColumnModel().getColumn(i).setCellRenderer(centrado);
        }
    }

    private void limpiarDatosEstudiante() {
        cajaCarnet.setText("");
        cajaNombre.setText("");
        cajaApellido.setText("");
        cajaCorreo.setText("");
        cajaNombre.requestFocus();
    }

    private String campoBuscarEstudiante(int indice) {
        switch (indice) {
            case 0 -> {
                // Nombre
                return "nom_estudiante";
            }
            case 1 -> {
                // ID Estudiante
                return "id_estudiante";
            }
            case 2 -> {
                // Nombre
                return "nom_estudiante";
            }
            case 3 -> {
                // Apellido
                return "apellido_estudiante";
            }
            case 4 -> {
                // Correo Electrónico
                return "correo_estudiante";
            }
            case 5 -> {
                // Cod U 
                return "codU_estudiante";
            }

            default ->
                throw new IllegalArgumentException("Índice de búsqueda inválido: " + indice);
        }
    }

    private void buscarEstudiante(String dato, String campo) {
        //NO CAMBIAR LOS PARÁMETROS DE ENTRADA
        List<Estudiante> estudiantesEncontrados;
        DaoEstudiante daoEstudiante = new DaoEstudiante();

        miModeloTabla.setNumRows(0);

        estudiantesEncontrados = daoEstudiante.buscarDato(dato, campo);
        estudiantesEncontrados.forEach((estudiante) -> {
            Object fila[] = new Object[5];

            fila[0] = estudiante.getIdEstudiante();
            fila[1] = estudiante.getNomEstudiante();
            fila[2] = estudiante.getApellidoEstudiante();
            fila[3] = estudiante.getCorreoEstudiante();
            fila[4] = estudiante.getCodUEstudiante();

            miModeloTabla.addRow(fila);
        });
    }

    private boolean siElimino(Integer idEstudiante) {
        int opcion;
        Boolean bandera = false;
        String[] textoBotones = {"Aceptar", "Cancelar"};

        DaoEstudiante daoEstudiante = new DaoEstudiante();
        Estudiante estudiante = daoEstudiante.buscar(idEstudiante);
        opcion = JOptionPane.showOptionDialog(jpContainer, "¿Está seguro de eliminar al estudiante " + estudiante.getNomEstudiante() + "?",
                "Aviso", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, textoBotones, textoBotones[1]);
        if (opcion == JOptionPane.YES_OPTION) {
            bandera = true;
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
        jpSearch = new javax.swing.JPanel();
        cajaBuscar = new javax.swing.JTextField();
        lblImgSearch = new javax.swing.JLabel();
        cmbSort = new javax.swing.JComboBox<>();
        lblTitulo = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabEstudiantes = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jpCarnet = new javax.swing.JPanel();
        lblCarnet = new javax.swing.JLabel();
        cajaCarnet = new javax.swing.JTextField();
        jpApellido = new javax.swing.JPanel();
        lblApellido = new javax.swing.JLabel();
        cajaApellido = new javax.swing.JTextField();
        jpCorreo = new javax.swing.JPanel();
        lblCorreo = new javax.swing.JLabel();
        cajaCorreo = new javax.swing.JTextField();
        jpNombre = new javax.swing.JPanel();
        lblNombre = new javax.swing.JLabel();
        cajaNombre = new javax.swing.JTextField();
        btnEditar = new javax.swing.JButton();
        btnAnadir = new javax.swing.JButton();
        btnDescargar = new javax.swing.JButton();

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
        jpHome.setBounds(10, 167, 90, 90);

        getContentPane().add(jpLateral);
        jpLateral.setBounds(0, 0, 110, 690);

        jpContainer.setBackground(new java.awt.Color(232, 236, 239));
        jpContainer.setLayout(null);

        jpSearch.setBackground(new java.awt.Color(255, 255, 255));

        cajaBuscar.setFont(new java.awt.Font("Montserrat", 1, 18)); // NOI18N
        cajaBuscar.setBorder(null);
        cajaBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cajaBuscarActionPerformed(evt);
            }
        });
        cajaBuscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                cajaBuscarKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jpSearchLayout = new javax.swing.GroupLayout(jpSearch);
        jpSearch.setLayout(jpSearchLayout);
        jpSearchLayout.setHorizontalGroup(
            jpSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpSearchLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblImgSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cajaBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jpSearchLayout.setVerticalGroup(
            jpSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpSearchLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cajaBuscar)
                    .addComponent(lblImgSearch, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jpContainer.add(jpSearch);
        jpSearch.setBounds(615, 34, 239, 35);

        cmbSort.setFont(new java.awt.Font("Montserrat", 1, 18)); // NOI18N
        cmbSort.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Ordenar/Buscar por", "Código", "Nombre", "Apellido", "Correo", "Cod Estudiante" }));
        cmbSort.setBorder(null);
        cmbSort.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSortActionPerformed(evt);
            }
        });
        jpContainer.add(cmbSort);
        cmbSort.setBounds(403, 34, 206, 35);

        lblTitulo.setFont(new java.awt.Font("Montserrat", 1, 60)); // NOI18N
        lblTitulo.setText("Estudiantes");
        jpContainer.add(lblTitulo);
        lblTitulo.setBounds(10, 30, 390, 75);

        jSeparator1.setForeground(new java.awt.Color(37, 206, 203));
        jpContainer.add(jSeparator1);
        jSeparator1.setBounds(6, 330, 500, 10);

        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane1.setBorder(null);

        tabEstudiantes.setFont(new java.awt.Font("Montserrat SemiBold", 1, 14)); // NOI18N
        tabEstudiantes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tabEstudiantes.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        tabEstudiantes.setGridColor(new java.awt.Color(255, 255, 255));
        tabEstudiantes.setSelectionBackground(new java.awt.Color(37, 206, 203));
        tabEstudiantes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabEstudiantesMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                tabEstudiantesMouseEntered(evt);
            }
        });
        jScrollPane1.setViewportView(tabEstudiantes);

        jpContainer.add(jScrollPane1);
        jScrollPane1.setBounds(10, 410, 901, 205);

        jLabel2.setFont(new java.awt.Font("Montserrat", 1, 18)); // NOI18N
        jLabel2.setText("Datos Generales:");
        jpContainer.add(jLabel2);
        jLabel2.setBounds(30, 110, 170, 23);

        jpCarnet.setBackground(new java.awt.Color(255, 255, 255));

        lblCarnet.setFont(new java.awt.Font("Montserrat SemiBold", 0, 14)); // NOI18N
        lblCarnet.setForeground(new java.awt.Color(98, 106, 109));
        lblCarnet.setText("Cod. Estudiante:");

        cajaCarnet.setFont(new java.awt.Font("Montserrat SemiBold", 1, 24)); // NOI18N
        cajaCarnet.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        cajaCarnet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cajaCarnetActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpCarnetLayout = new javax.swing.GroupLayout(jpCarnet);
        jpCarnet.setLayout(jpCarnetLayout);
        jpCarnetLayout.setHorizontalGroup(
            jpCarnetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpCarnetLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jpCarnetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpCarnetLayout.createSequentialGroup()
                        .addComponent(lblCarnet)
                        .addGap(0, 114, Short.MAX_VALUE))
                    .addComponent(cajaCarnet))
                .addContainerGap())
        );
        jpCarnetLayout.setVerticalGroup(
            jpCarnetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpCarnetLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblCarnet)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cajaCarnet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jpContainer.add(jpCarnet);
        jpCarnet.setBounds(660, 150, 250, 70);

        jpApellido.setBackground(new java.awt.Color(255, 255, 255));

        lblApellido.setFont(new java.awt.Font("Montserrat SemiBold", 0, 14)); // NOI18N
        lblApellido.setForeground(new java.awt.Color(98, 106, 109));
        lblApellido.setText("Apellido:");

        cajaApellido.setFont(new java.awt.Font("Montserrat SemiBold", 1, 24)); // NOI18N
        cajaApellido.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        cajaApellido.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cajaApellidoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpApellidoLayout = new javax.swing.GroupLayout(jpApellido);
        jpApellido.setLayout(jpApellidoLayout);
        jpApellidoLayout.setHorizontalGroup(
            jpApellidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpApellidoLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jpApellidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpApellidoLayout.createSequentialGroup()
                        .addComponent(lblApellido)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(cajaApellido, javax.swing.GroupLayout.DEFAULT_SIZE, 232, Short.MAX_VALUE))
                .addContainerGap())
        );
        jpApellidoLayout.setVerticalGroup(
            jpApellidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpApellidoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblApellido)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cajaApellido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jpContainer.add(jpApellido);
        jpApellido.setBounds(370, 150, 250, 70);

        jpCorreo.setBackground(new java.awt.Color(255, 255, 255));

        lblCorreo.setFont(new java.awt.Font("Montserrat SemiBold", 0, 14)); // NOI18N
        lblCorreo.setForeground(new java.awt.Color(98, 106, 109));
        lblCorreo.setText("Correo Electrónico:");

        cajaCorreo.setFont(new java.awt.Font("Montserrat SemiBold", 1, 24)); // NOI18N
        cajaCorreo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        cajaCorreo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cajaCorreoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpCorreoLayout = new javax.swing.GroupLayout(jpCorreo);
        jpCorreo.setLayout(jpCorreoLayout);
        jpCorreoLayout.setHorizontalGroup(
            jpCorreoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpCorreoLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jpCorreoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpCorreoLayout.createSequentialGroup()
                        .addComponent(lblCorreo)
                        .addGap(0, 96, Short.MAX_VALUE))
                    .addComponent(cajaCorreo))
                .addContainerGap())
        );
        jpCorreoLayout.setVerticalGroup(
            jpCorreoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpCorreoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblCorreo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cajaCorreo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jpContainer.add(jpCorreo);
        jpCorreo.setBounds(50, 240, 250, 70);

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
        jpNombre.setBounds(50, 150, 250, 70);

        btnEditar.setBackground(new java.awt.Color(37, 206, 203));
        btnEditar.setFont(new java.awt.Font("Montserrat", 1, 18)); // NOI18N
        btnEditar.setForeground(new java.awt.Color(255, 255, 255));
        btnEditar.setText("Editar");
        btnEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarActionPerformed(evt);
            }
        });
        jpContainer.add(btnEditar);
        btnEditar.setBounds(500, 270, 100, 30);

        btnAnadir.setBackground(new java.awt.Color(37, 206, 203));
        btnAnadir.setFont(new java.awt.Font("Montserrat", 1, 18)); // NOI18N
        btnAnadir.setForeground(new java.awt.Color(255, 255, 255));
        btnAnadir.setText("Añadir");
        btnAnadir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnadirActionPerformed(evt);
            }
        });
        jpContainer.add(btnAnadir);
        btnAnadir.setBounds(380, 270, 100, 30);

        btnDescargar.setBackground(new java.awt.Color(37, 206, 203));
        btnDescargar.setFont(new java.awt.Font("Montserrat", 1, 18)); // NOI18N
        btnDescargar.setForeground(new java.awt.Color(255, 255, 255));
        btnDescargar.setText("Descargar Archivo");
        btnDescargar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDescargarActionPerformed(evt);
            }
        });
        jpContainer.add(btnDescargar);
        btnDescargar.setBounds(670, 340, 210, 30);

        getContentPane().add(jpContainer);
        jpContainer.setBounds(110, 0, 930, 680);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cajaNombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cajaNombreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cajaNombreActionPerformed

    private void cajaApellidoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cajaApellidoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cajaApellidoActionPerformed

    private void cajaCarnetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cajaCarnetActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cajaCarnetActionPerformed

    private void cajaCorreoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cajaCorreoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cajaCorreoActionPerformed

    private void btnDescargarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDescargarActionPerformed
        String rutaAplicacion = System.getProperty("user.dir");
        String nombreArchivoSalida = "Elementos.ramon";

        DaoEstudiante miDao = new DaoEstudiante();

        String ruta = miDao.loVoyAGrabarEnUnArchivoEsPeroYa(miModeloTabla, rutaAplicacion, nombreArchivoSalida);

        JOptionPane.showMessageDialog(jpContainer, "Se ha descargado la lista en " + ruta,
                "Descarga exitosa", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_btnDescargarActionPerformed

    private void cajaBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cajaBuscarActionPerformed


    }//GEN-LAST:event_cajaBuscarActionPerformed

    private void lblHomeMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblHomeMouseEntered
        jpHome.setBackground(new Color(26, 49, 76));
        lblHome.setBackground(new Color(26, 49, 76));
    }//GEN-LAST:event_lblHomeMouseEntered

    private void lblHomeMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblHomeMouseExited
        jpHome.setBackground(Color.WHITE);
        lblHome.setBackground(Color.WHITE);
    }//GEN-LAST:event_lblHomeMouseExited

    private void btnAnadirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnadirActionPerformed

        if (estaTodoBien()) {
            String codUEstudiante = cajaCarnet.getText();
            String nomEstudiante = cajaNombre.getText();
            String apellidoEstudiante = cajaApellido.getText();
            String correoEstudiante = cajaCorreo.getText();

            Estudiante objEstudiante = new Estudiante();
            objEstudiante.setCodUEstudiante(codUEstudiante);
            objEstudiante.setNomEstudiante(nomEstudiante);
            objEstudiante.setApellidoEstudiante(apellidoEstudiante);
            objEstudiante.setCorreoEstudiante(correoEstudiante);
            // El idGrupo se deja como nulo

            DaoEstudiante miDao = new DaoEstudiante();
            if (miDao.registrar(objEstudiante)) {
                JOptionPane.showMessageDialog(jpContainer, "Registro exitoso", "Informacion", JOptionPane.INFORMATION_MESSAGE);
                //CARGAR LA TABLA EN EL MOMENTO QUE EL REGISTRO SEA EXITOSO
                cargarEstudiantes("");
                limpiarDatosEstudiante();
            } else {
                JOptionPane.showMessageDialog(jpContainer, "Registro fallido", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }


    }//GEN-LAST:event_btnAnadirActionPerformed

    private void btnEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarActionPerformed
        if (estaTodoBien()) {
            // Obtiene el ID del estudiante seleccionado
            int filaSeleccionada = tabEstudiantes.getSelectedRow();
            String idTexto = tabEstudiantes.getValueAt(filaSeleccionada, 0).toString();
            codEstudiantes = Integer.valueOf(idTexto);

            String codUEstudiante, nomEstudiante, apellidoEstudiante, correoEstudiante;
            // Obtiene los valores de los campos de texto
            codUEstudiante = cajaCarnet.getText();
            nomEstudiante = cajaNombre.getText();
            apellidoEstudiante = cajaApellido.getText();
            correoEstudiante = cajaCorreo.getText();

            DaoEstudiante miDao = new DaoEstudiante();
            Estudiante objEstudiante = new Estudiante();

            // Establece la nueva información del estudiante
            objEstudiante.setCodUEstudiante(codUEstudiante);
            objEstudiante.setNomEstudiante(nomEstudiante);
            objEstudiante.setApellidoEstudiante(apellidoEstudiante);
            objEstudiante.setCorreoEstudiante(correoEstudiante);
            objEstudiante.setIdEstudiante(codEstudiantes);

            if (miDao.actualizar(objEstudiante)) {
                JOptionPane.showMessageDialog(jpContainer, "¡Actualización exitosa!", "Información", JOptionPane.INFORMATION_MESSAGE);
                // Recarga la tabla después de una actualización exitosa
                limpiarDatosEstudiante();
                cargarEstudiantes("");
            } else {
                JOptionPane.showMessageDialog(jpContainer, "No se pudo actualizar", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_btnEditarActionPerformed

    private void tabEstudiantesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabEstudiantesMouseClicked
        // HABILITA EL BOTÓN DE EDITAR
        editarActivado();

        int filaSeleccionada = tabEstudiantes.getSelectedRow();
        String codTexto = miModeloTabla.getValueAt(filaSeleccionada, 0).toString();
        codEstudiantes = Integer.valueOf(codTexto);

        DaoEstudiante daoEstudiante = new DaoEstudiante();
        Estudiante estudianteSeleccionado = daoEstudiante.buscar(codEstudiantes);

        cajaCarnet.setText(estudianteSeleccionado.getCodUEstudiante());
        cajaNombre.setText(estudianteSeleccionado.getNomEstudiante());
        cajaApellido.setText(estudianteSeleccionado.getApellidoEstudiante());
        cajaCorreo.setText(estudianteSeleccionado.getCorreoEstudiante());

        int columnaSeleccionada = tabEstudiantes.getSelectedColumn();

        if (columnaSeleccionada == 5) {

            if (estudianteSeleccionado == null) {
                JOptionPane.showMessageDialog(jpContainer, "No se encontró el estudiante", "Advertencia", JOptionPane.WARNING_MESSAGE);
            } else {

                if (siElimino(codEstudiantes)) {
                    DaoEstudiante daoElim = new DaoEstudiante();
                    if (daoElim.eliminar(codEstudiantes)) {
                        cargarEstudiantes("");
                        JOptionPane.showMessageDialog(jpContainer, "Estudiante ha sido eliminado correctamente", "Información", JOptionPane.INFORMATION_MESSAGE);
                        limpiarDatosEstudiante();
                    } else {
                        JOptionPane.showMessageDialog(jpContainer, "No se pudo eliminar el estudiante", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }

    }//GEN-LAST:event_tabEstudiantesMouseClicked

    private void cmbSortActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSortActionPerformed

        String seleccionarOrden = (String) cmbSort.getSelectedItem();
        System.out.println("Valor seleccionado: " + seleccionarOrden);  // Para depuración

        switch (seleccionarOrden) {
            case "Nombre" ->
                cargarEstudiantes("nom_estudiante");
            case "Apellido" ->
                cargarEstudiantes("apellido_estudiante");
            case "Código" ->
                cargarEstudiantes("id_estudiante");
            case "Correo" ->
                cargarEstudiantes("correo_estudiante");
            case "Cod Estudiante" ->
                cargarEstudiantes("codU_estudiante");

            default -> {
                System.err.println("Error: valor no esperado: " + seleccionarOrden);
                throw new AssertionError("Valor no esperado: " + seleccionarOrden);
            }
        }


    }//GEN-LAST:event_cmbSortActionPerformed

    private void cajaBuscarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cajaBuscarKeyReleased

        seleccionarBuscar = cmbSort.getSelectedIndex();

        String campoSelect = campoBuscarEstudiante(seleccionarBuscar);

        buscarEstudiante("%" + cajaBuscar.getText().toLowerCase() + "%", campoSelect);


    }//GEN-LAST:event_cajaBuscarKeyReleased

    private void lblHomeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblHomeMouseClicked
        FrmPrincipal principal = new FrmPrincipal();
        principal.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_lblHomeMouseClicked

    private void tabEstudiantesMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabEstudiantesMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_tabEstudiantesMouseEntered

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */

        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FrmEstudiantes().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAnadir;
    private javax.swing.JButton btnDescargar;
    private javax.swing.JButton btnEditar;
    private javax.swing.JTextField cajaApellido;
    private javax.swing.JTextField cajaBuscar;
    private javax.swing.JTextField cajaCarnet;
    private javax.swing.JTextField cajaCorreo;
    private javax.swing.JTextField cajaNombre;
    private javax.swing.JComboBox<String> cmbSort;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JPanel jpApellido;
    private javax.swing.JPanel jpCarnet;
    private javax.swing.JPanel jpContainer;
    private javax.swing.JPanel jpCorreo;
    private javax.swing.JPanel jpHome;
    private javax.swing.JPanel jpLateral;
    private javax.swing.JPanel jpLogo;
    private javax.swing.JPanel jpNombre;
    private javax.swing.JPanel jpSearch;
    private javax.swing.JLabel lblApellido;
    private javax.swing.JLabel lblCarnet;
    private javax.swing.JLabel lblCorreo;
    private javax.swing.JLabel lblHome;
    private javax.swing.JLabel lblImgSearch;
    private javax.swing.JLabel lblLogo;
    private javax.swing.JLabel lblNombre;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JTable tabEstudiantes;
    // End of variables declaration//GEN-END:variables
}
