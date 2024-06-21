package com.scientia.forms;

import com.formdev.flatlaf.FlatLightLaf;
import com.scientia.daos.DaoGrupoLaboratorio;
import com.scientia.entidades.GrupoLaboratorio;
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
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import static javax.swing.SwingConstants.CENTER;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

/**
 *
 * @author danim
 */
public class FrmLaboratorios extends javax.swing.JFrame {

    private Integer codGrupoLaboratotio = null;

    // Se establece el cmbSort en el id para que realice la busqueda desde
    // ese campo por default
    Integer seleccionarBuscar = 0;

    private String titulos[] = {"ID", "Nombre", "Descripción", "Eliminar"};
    private DefaultTableModel miModelitoTabla = new DefaultTableModel(titulos, 0) {

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            if (columnIndex == 3) {
                return ImageIcon.class;
            } else {
                return Object.class;
            }
        }
    };

    public FrmLaboratorios() {
        initComponents();
        tabLab.setModel(miModelitoTabla);
        cargarLaboratorios("");

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

        // Añadir un MouseListener para deseleccionar filas cuando se hace clic fuera de la tabla
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (!e.getSource().equals(tabLab)) {
                    tabLab.clearSelection();
                    borrarDatos();
                    editarDesactivado();
                }
            }
        });

        tabLab.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                tabLab.requestFocus();
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

        Dimension containerSize = jpContainer.getSize();
        int widthContainer = containerSize.width;
        int heightContainer = containerSize.height;
        int anchoLateral = jpLateral.getWidth();

        // Ajustar tamaños en proporción al tamaño del frame
        jpContainer.setBounds(anchoLateral, 0, (int) (width - anchoLateral), height);
        jpLateral.setBounds(0, 0, (int) (width * 0.1), height);

        jpLogo.setBounds(20, 10, (int) (width * 0.06), (int) (height * 0.09));
        jpHome.setBounds(35, 250, (int) (width * 0.055), (int) (height * 0.09));
        lblHome.setBounds(18, 0, (int) (width * 0.055), (int) (height * 0.09));

        jpSearch.setBounds(widthContainer - 325, 50, (int) (widthContainer * 0.18), (int) (heightContainer * 0.045));
        cmbSort.setBounds(widthContainer - 600, 50, (int) (widthContainer * 0.18), (int) (heightContainer * 0.045));

        jpNombre.setBounds(15, 150, (int) (width * 0.25), (int) (height * 0.09));
        jpDescripcion.setBounds(420, 150, (int) (width * 0.35), (int) (height * 0.09));

        btnAnadir.setBounds(1000, 170, (int) (width * 0.1), (int) (height * 0.052));
        btnEditar.setBounds(1200, 170, (int) (width * 0.1), (int) (height * 0.052));

        jSeparator1.setBounds(35, 270, (int) (width * 0.6), (int) (height * 0.052));
        btnDescargar.setBounds(1050, 250, (int) (width * 0.15), (int) (height * 0.052));
        jScrollPane1.setBounds(20, 310, (int) (width * 0.86), (int) (height * 0.57));

        // Revalidar y repintar el contenedor para aplicar los cambios
        this.revalidate();
        this.repaint();
    }

    private void loadDesign() {

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

        editarDesactivado();
        PlaceHolder buscar = new PlaceHolder("Buscar...", cajaBuscar);
        PlaceHolder nombre = new PlaceHolder("Física Mecánica SIS-A", cajaNombre);
        PlaceHolder descripcion = new PlaceHolder("Profesor encargado del laboratorio...", cajaDescripcion);

        //DISEÑO DE LA TABLA 
        JTableHeader header = tabLab.getTableHeader();
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

    private void cargarLaboratorios(String ordencito) {
        List<GrupoLaboratorio> arrGrupoLaboratorio;
        DaoGrupoLaboratorio miDao = new DaoGrupoLaboratorio();

        String nomEliminar = "/com/scientia/icons/borrar.png";
        String rutaIconEliminar = this.getClass().getResource(nomEliminar).getPath();
        ImageIcon borrarIcono = new ImageIcon(rutaIconEliminar);

        miModelitoTabla.setNumRows(0);

        arrGrupoLaboratorio = miDao.consultar(ordencito);
        if (arrGrupoLaboratorio != null) {
            arrGrupoLaboratorio.forEach((GrupoLaboratorio) -> {
                Object filita[] = new Object[4];

                filita[0] = GrupoLaboratorio.getIdGrupoLaboratorio();
                filita[1] = GrupoLaboratorio.getNombreLaboratorio();
                filita[2] = GrupoLaboratorio.getDescLaboratorio();
                filita[3] = borrarIcono;

                miModelitoTabla.addRow(filita);
            });
            //DEFINE EL TAMAÑO DE CADA COLUMNA
            tabLab.getColumnModel().getColumn(0).setPreferredWidth(50);
            tabLab.getColumnModel().getColumn(1).setPreferredWidth(150);
            tabLab.getColumnModel().getColumn(2).setPreferredWidth(350);

//            // CENTRA LOS TITULOS DE CADA COLUMNA, MENOS LOS ICONOS
            DefaultTableCellRenderer centrado = new DefaultTableCellRenderer();
            centrado.setHorizontalAlignment(JLabel.CENTER);
            for (int i = 0; i < (tabLab.getColumnCount() - 1); i++) {
                tabLab.getColumnModel().getColumn(i).setCellRenderer(centrado);
            }
        } else {
            System.out.println("La lista de grupos de laboratorio está vacía.");
        }
    }

    private String campoBuscar(int select) {
        // SE ELIGE EN QUE COLUMNA SE QUIERE APLICAR LA BUSQUEDA DE DATO
        //CAMBIAR UNICAMENTE LOS VALORES DEL CAMPO Y AÑADIR MAS CASE
        //SI HAY MAS CAMPOS POR LOS QUE BUSCAR
        //SETEEN LOS CAMPOS DE MANERA MANUAL EN EL MODELO DEL CMB EN VISUALIZAR

        System.out.println("indice: " + select);
        String campo = "";
        switch (select) {
            //BUSCA POR EL NOMBRE EN CASO QUE EL CMB ESTE EN ORDENAR
            case 0 -> {
                campo = "nombre_laboratorio";
            }
            case 1 -> {
                //SE ASIGNA EL VALOR DEL CAMPO
                campo = "id_grupo_laboratorio";
            }
            case 2 -> {
                campo = "nombre_laboratorio";
            }
            case 3 -> {
                campo = "desc_laboratorio";
            }
            default ->
                throw new AssertionError();
        }
        return campo;
    }

    private void buscarDato(String dato, String campo) {
        //NO CAMBIAR LOS PARAMETROS DE ENTRADA
        List<GrupoLaboratorio> arrayLab;
        DaoGrupoLaboratorio dao = new DaoGrupoLaboratorio();

        miModelitoTabla.setNumRows(0);

        arrayLab = dao.buscarDato(dato, campo);
        arrayLab.forEach((lab) -> {
            Object filita[] = new Object[3];

            filita[0] = lab.getIdGrupoLaboratorio();
            filita[1] = lab.getNombreLaboratorio();
            filita[2] = lab.getDescLaboratorio();

            miModelitoTabla.addRow(filita);

        });
    }

    private Boolean estaTodoBien() {
        Boolean bandera = true;
        //TENGAN CUIDADO CON LOS MENSAJES DEL JOPTION
        String nombre;
        nombre = cajaNombre.getText();
        if (nombre.equals("")) {
            cajaNombre.requestFocus();
            bandera = false;
            JOptionPane.showMessageDialog(jpContainer, "Ingrese un nombre correcto", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            String descripcion;
            descripcion = cajaDescripcion.getText();
            if (descripcion.equals("")) {
                cajaDescripcion.requestFocus();
                bandera = false;
                JOptionPane.showMessageDialog(jpContainer, "Ingrese la descrpción", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        return bandera;
    }

    private void borrarDatos() {
        cajaNombre.setText("");
        cajaDescripcion.setText("");
        cajaNombre.requestFocus();
    }

    private boolean siElimino(Integer codigo) {
        int opcion;
        Boolean bandera = false;
        String textoBotones[] = {"Aceptar", "Cancelar"};
        DaoGrupoLaboratorio dao = new DaoGrupoLaboratorio();

        GrupoLaboratorio objLab = dao.buscar(codigo);

        opcion = JOptionPane.showOptionDialog(jpContainer, "¿Esta seguro de elimnar el laboratorio " + objLab.getNombreLaboratorio()
                + "?", "Aviso", JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, textoBotones, textoBotones[1]);

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
        tabLab = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jpDescripcion = new javax.swing.JPanel();
        lblDescripcion = new javax.swing.JLabel();
        cajaDescripcion = new javax.swing.JTextField();
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
        jpHome.setBounds(10, 169, 90, 80);

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
        jpSearch.setBounds(670, 30, 239, 35);

        cmbSort.setFont(new java.awt.Font("Montserrat", 1, 18)); // NOI18N
        cmbSort.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Ordenar por:", "Id", "Nombre", "Descripción" }));
        cmbSort.setBorder(null);
        cmbSort.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSortActionPerformed(evt);
            }
        });
        jpContainer.add(cmbSort);
        cmbSort.setBounds(460, 30, 206, 35);

        lblTitulo.setFont(new java.awt.Font("Montserrat", 1, 60)); // NOI18N
        lblTitulo.setText("Laboratorios");
        jpContainer.add(lblTitulo);
        lblTitulo.setBounds(10, 30, 420, 75);

        jSeparator1.setForeground(new java.awt.Color(37, 206, 203));
        jpContainer.add(jSeparator1);
        jSeparator1.setBounds(6, 330, 500, 10);

        tabLab.setFont(new java.awt.Font("Montserrat SemiBold", 0, 14)); // NOI18N
        tabLab.setModel(new javax.swing.table.DefaultTableModel(
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
        tabLab.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        tabLab.setSelectionBackground(new java.awt.Color(37, 206, 203));
        tabLab.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabLabMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tabLab);

        jpContainer.add(jScrollPane1);
        jScrollPane1.setBounds(10, 460, 901, 205);

        jLabel2.setFont(new java.awt.Font("Montserrat", 1, 18)); // NOI18N
        jLabel2.setText("Datos Generales:");
        jpContainer.add(jLabel2);
        jLabel2.setBounds(30, 110, 170, 23);

        jpDescripcion.setBackground(new java.awt.Color(255, 255, 255));

        lblDescripcion.setFont(new java.awt.Font("Montserrat SemiBold", 0, 14)); // NOI18N
        lblDescripcion.setForeground(new java.awt.Color(98, 106, 109));
        lblDescripcion.setText("Descripción:");

        cajaDescripcion.setFont(new java.awt.Font("Montserrat SemiBold", 1, 24)); // NOI18N
        cajaDescripcion.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        cajaDescripcion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cajaDescripcionActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpDescripcionLayout = new javax.swing.GroupLayout(jpDescripcion);
        jpDescripcion.setLayout(jpDescripcionLayout);
        jpDescripcionLayout.setHorizontalGroup(
            jpDescripcionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpDescripcionLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jpDescripcionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpDescripcionLayout.createSequentialGroup()
                        .addComponent(lblDescripcion)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(cajaDescripcion, javax.swing.GroupLayout.DEFAULT_SIZE, 232, Short.MAX_VALUE))
                .addContainerGap())
        );
        jpDescripcionLayout.setVerticalGroup(
            jpDescripcionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpDescripcionLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblDescripcion)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cajaDescripcion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jpContainer.add(jpDescripcion);
        jpDescripcion.setBounds(370, 150, 250, 70);

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
        btnEditar.setBounds(770, 170, 100, 30);

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
        btnAnadir.setBounds(650, 170, 100, 30);

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
        btnDescargar.setBounds(670, 310, 210, 30);

        getContentPane().add(jpContainer);
        jpContainer.setBounds(110, 0, 930, 680);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cajaNombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cajaNombreActionPerformed
        cajaNombre.setText("");
    }//GEN-LAST:event_cajaNombreActionPerformed

    private void cajaDescripcionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cajaDescripcionActionPerformed
        cajaDescripcion.setText("");
    }//GEN-LAST:event_cajaDescripcionActionPerformed

    private void btnDescargarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDescargarActionPerformed
        String rutaAplicacion = System.getProperty("user.dir");
        String nombreArchivoSalida = "Elementos.ramon";

        DaoGrupoLaboratorio miDao = new DaoGrupoLaboratorio();

        String ruta = miDao.loVoyAGrabarEnUnArchivoEsPeroYa(miModelitoTabla, rutaAplicacion, nombreArchivoSalida);

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
            String nombre;
            String descripcion;

            nombre = cajaNombre.getText().toLowerCase();
            descripcion = cajaDescripcion.getText().toLowerCase();

            GrupoLaboratorio objGrupoLaboratorio = new GrupoLaboratorio();

            objGrupoLaboratorio.setNombreLaboratorio(nombre);
            objGrupoLaboratorio.setDescLaboratorio(descripcion);

            DaoGrupoLaboratorio miDao = new DaoGrupoLaboratorio();
            if (miDao.registrar(objGrupoLaboratorio)) {
                JOptionPane.showMessageDialog(jpContainer, "¡Registro exitoso!", "Informacion", JOptionPane.INFORMATION_MESSAGE);
                //CARGAR LA TABLA DESPUES DE QUE EL REGISTRO SEA EXITOSO
                cargarLaboratorios("");
                borrarDatos();
            } else {
                JOptionPane.showMessageDialog(jpContainer, "Registro fallido", "Error", JOptionPane.ERROR_MESSAGE);
            }

        }


    }//GEN-LAST:event_btnAnadirActionPerformed

    private void tabLabMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabLabMouseClicked
        //HABILITA EL BOTON DE EDITAR
        editarActivado();

        //DEPENDIENDO DE LA COLUMNA QUE SE SELECCIONE SE CARGAN LOS DATOS
        //LAS CAJAS
        int filaSeleccionada = tabLab.getSelectedRow();
        String codTexto = miModelitoTabla.getValueAt(filaSeleccionada, 0).toString();
        codGrupoLaboratotio = Integer.valueOf(codTexto);

        DaoGrupoLaboratorio dao = new DaoGrupoLaboratorio();
        GrupoLaboratorio objLab = dao.buscar(codGrupoLaboratotio);

        cajaNombre.setText(objLab.getNombreLaboratorio());
        cajaDescripcion.setText(objLab.getDescLaboratorio());

        //ELIMINAR //
        int columnaSeleccionada = tabLab.getSelectedColumn();

        //SI LE DA EN LA COLUMNA ELIMINAR
        if (columnaSeleccionada == 3) {

            if (objLab == null) {
                JOptionPane.showMessageDialog(jpContainer, "No se encontró el laboratorio", "Advertencia", JOptionPane.WARNING_MESSAGE);
            } else {

                if (siElimino(codGrupoLaboratotio)) {
                    DaoGrupoLaboratorio daoElim = new DaoGrupoLaboratorio();
                    if (daoElim.eliminar(codGrupoLaboratotio)) {
                        cargarLaboratorios("");
                        JOptionPane.showMessageDialog(jpContainer, "El laboratorio ha sido eliminado correctamente", "Información", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(jpContainer, "No se pudo eliminar el producto", "Información", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }

        }


    }//GEN-LAST:event_tabLabMouseClicked

    private void cmbSortActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSortActionPerformed
        // Selecciona el parámetro y lo ordena según este

        Integer seleccionarOrden = cmbSort.getSelectedIndex();
        switch (seleccionarOrden) {

            case 1 -> {
                cargarLaboratorios("id_grupo_laboratorio");
            }
            case 2 -> {
                cargarLaboratorios("nombre_laboratorio");
            }
            case 3 -> {
                cargarLaboratorios("desc_laboratorio");
            }

            default ->
                throw new AssertionError();
        }

    }//GEN-LAST:event_cmbSortActionPerformed

    private void cajaBuscarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cajaBuscarKeyReleased
        //SE SELECCIONA EL CAMPO A BUSCAR SEGUN EL CMB DE ORDEN
        seleccionarBuscar = cmbSort.getSelectedIndex();

        //SE PASA A LA FUNCION DE TRAER EN Q CAMPO SE VA A BUSCAR
        String campoSelect = campoBuscar(seleccionarBuscar);
        System.out.println("campo: " + campoSelect);

        // SE REALIZA LA CONSULTA DE BUSCAR SEGUN LO QUE EL USUARIO DIGITE EN LA CAJA
        buscarDato("%" + cajaBuscar.getText().toLowerCase() + "%", campoSelect);
    }//GEN-LAST:event_cajaBuscarKeyReleased

    private void btnEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarActionPerformed
        if (estaTodoBien()) {
            //TRAE EL ID DE LA FILA SELECCIONADA
            int filaSeleccionada = tabLab.getSelectedRow();
            String codTexto = tabLab.getValueAt(filaSeleccionada, 0).toString();
            codGrupoLaboratotio = Integer.valueOf(codTexto);

            String nombre, descripcion;
            //OBTIENE LOS TEXTOS
            nombre = cajaNombre.getText().toLowerCase();
            descripcion = cajaDescripcion.getText().toLowerCase();

            DaoGrupoLaboratorio miDao = new DaoGrupoLaboratorio();
            GrupoLaboratorio objGrupoLaboratorio = new GrupoLaboratorio();

            //SETEA LA NUEVA INFORMACION
            objGrupoLaboratorio.setNombreLaboratorio(nombre);
            objGrupoLaboratorio.setDescLaboratorio(descripcion);
            objGrupoLaboratorio.setIdGrupoLaboratorio(codGrupoLaboratotio);

            if (miDao.actualizar(objGrupoLaboratorio)) {
                JOptionPane.showMessageDialog(jpContainer, "¡Actualización exitosa!", "Informacion", JOptionPane.INFORMATION_MESSAGE);
                //CARGAR LA TABLA DESPUES DE QUE LA ACTUALIZACION SEA EXITOSA
                borrarDatos();
                cargarLaboratorios("");
            } else {
                JOptionPane.showMessageDialog(jpContainer, "No se pudo actualizar", "Error", JOptionPane.ERROR_MESSAGE);
            }

        }
    }//GEN-LAST:event_btnEditarActionPerformed

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
            java.util.logging.Logger.getLogger(FrmLaboratorios.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
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

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FrmLaboratorios().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAnadir;
    private javax.swing.JButton btnDescargar;
    private javax.swing.JButton btnEditar;
    private javax.swing.JTextField cajaBuscar;
    private javax.swing.JTextField cajaDescripcion;
    private javax.swing.JTextField cajaNombre;
    private javax.swing.JComboBox<String> cmbSort;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JPanel jpContainer;
    private javax.swing.JPanel jpDescripcion;
    private javax.swing.JPanel jpHome;
    private javax.swing.JPanel jpLateral;
    private javax.swing.JPanel jpLogo;
    private javax.swing.JPanel jpNombre;
    private javax.swing.JPanel jpSearch;
    private javax.swing.JLabel lblDescripcion;
    private javax.swing.JLabel lblHome;
    private javax.swing.JLabel lblImgSearch;
    private javax.swing.JLabel lblLogo;
    private javax.swing.JLabel lblNombre;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JTable tabLab;
    // End of variables declaration//GEN-END:variables
}
