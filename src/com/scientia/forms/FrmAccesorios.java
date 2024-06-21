package com.scientia.forms;

import com.formdev.flatlaf.FlatLightLaf;
import com.scientia.daos.DaoAccesorio;
import com.scientia.entidades.Accesorio;
import com.scientia.utils.ImageScale;
import com.scientia.utils.PlaceHolder;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
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
public class FrmAccesorios extends javax.swing.JFrame {

    Integer idAccesorio = 0;
    Integer indiceBuscar = 0;
    private Integer codigoElementos = null;
    private String titulos[] = {"ID Accesorio", "Código de Inventario", "Nombre", "Referencia", "Existencias", "ID Elemento", "Nombre Elemento", "Eliminar"};
    private DefaultTableModel miModeloTabla = new DefaultTableModel(titulos, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            if (columnIndex == 7) {
                return ImageIcon.class;
            } else {
                return Object.class;
            }
        }

    };

    public FrmAccesorios() {
        initComponents();
        tabAccesorios.setModel(miModeloTabla);
        cargarAccesorios("");

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

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (!e.getSource().equals(tabAccesorios)) {
                    tabAccesorios.clearSelection();
                    editarDesactivado();
                    limpiarDatos();
                }
            }
        });

        tabAccesorios.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                tabAccesorios.requestFocus();
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
        int anchoLateral = 155;

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
        jpReferencia.setBounds(420, 150, (int) (width * 0.23), (int) (height * 0.09));
        jpCantidad.setBounds(790, 150, (int) (width * 0.15), (int) (height * 0.09));
        jpInventario.setBounds(1050, 150, (int) (width * 0.13), (int) (height * 0.09));

        btnAnadir.setBounds(300, 240, (int) (width * 0.07), (int) (height * 0.052));
        btnEditar.setBounds(500, 240, (int) (width * 0.07), (int) (height * 0.052));

        jSeparator1.setBounds(35, 300, (int) (width * 0.6), (int) (height * 0.052));
        btnDescargar.setBounds(1000, 270, (int) (width * 0.15), (int) (height * 0.052));
        jScrollPane1.setBounds(20, 330, (int) (width * 0.86), (int) (height * 0.53));

        // Revalidar y repintar el contenedor para aplicar los cambios
        this.revalidate();
        this.repaint();
    }

    private void cargarAccesorios(String orden) {
        List<Accesorio> arrAccesorios;
        DaoAccesorio miDao = new DaoAccesorio();

        String nomEliminar = "/com/scientia/icons/borrar.png";
        String rutaIconEliminar = this.getClass().getResource(nomEliminar).getPath();
        ImageIcon borrarIcono = new ImageIcon(rutaIconEliminar);

        miModeloTabla.setNumRows(0);
        arrAccesorios = miDao.consultar(orden);

        System.out.println(arrAccesorios.toString());

        arrAccesorios.forEach((accesorio) -> {
            Object fila[] = new Object[8];

            fila[0] = accesorio.getIdAccesorio();
            fila[1] = accesorio.getCodInventarioAccesorio().toString();
            fila[2] = accesorio.getNombreAccesorio();
            fila[3] = accesorio.getReferenciaAccesorio();
            fila[4] = accesorio.getCantidadAccesorio();
            fila[5] = accesorio.getElemento().getId();
            fila[6] = accesorio.getElemento().getNombre();
            fila[7] = borrarIcono;

            miModeloTabla.addRow(fila);

            System.out.println(accesorio.toString());

        });

    }

    private String campoBuscar(int select) {

        String campo = "";
        switch (select) {
            case 0 -> {
                cargarAccesorios("a.nombre_accesorio");
                campo = "nombre_accesorio";
            }
            case 1 -> {
                cargarAccesorios("a.id_accesorio");
                campo = "id_accesorio";
            }
            case 2 -> {
                cargarAccesorios("a.nombre_accesorio");
                campo = "nombre_accesorio";
            }
            case 3 -> {
                cargarAccesorios("a.referencia_accesorio");
                campo = "referencia_accesorio";
            }
            case 4 -> {
                cargarAccesorios("a.cantidad_accesorio");
                campo = "cantidad_accesorio";
            }
            case 5 -> {
                cargarAccesorios("a.cod_inventario_accesorio");
                campo = "cod_inventario_accesorio";
            }
            default -> {
                System.out.println("Opción no válida");
            }
        }

        return campo;
    }

    private void buscarDato(String dato, String campo) {
        List<Accesorio> arrAccesorios;
        DaoAccesorio miDao = new DaoAccesorio();

        miModeloTabla.setNumRows(0);

        arrAccesorios = miDao.buscarDato(dato, campo);

        String nomEliminar = "/com/scientia/icons/borrar.png";
        String rutaIconEliminar = this.getClass().getResource(nomEliminar).getPath();
        ImageIcon borrarIcono = new ImageIcon(rutaIconEliminar);

        arrAccesorios.forEach((accesorio) -> {

            Object fila[] = new Object[8];

            fila[0] = accesorio.getIdAccesorio();
            fila[1] = accesorio.getCodInventarioAccesorio().toString();
            fila[2] = accesorio.getNombreAccesorio();
            fila[3] = accesorio.getReferenciaAccesorio();
            fila[4] = accesorio.getCantidadAccesorio();
            fila[5] = accesorio.getElemento().getId();
            fila[6] = accesorio.getElemento().getNombre();
            fila[7] = borrarIcono;

            miModeloTabla.addRow(fila);

        });
    }

    private Boolean eliminar(Integer codigoEliminar) {
        int opcion;
        Boolean bandera = false;
        String textoBotones[] = {"Aceptar", "Cancelar"};

        DaoAccesorio miDao = new DaoAccesorio();
        Accesorio objAccesorio = miDao.buscar(codigoEliminar);

        opcion = JOptionPane.showOptionDialog(this,
                "¿Desea eliminar el accesorio " + objAccesorio.getNombreAccesorio() + "?", "Aviso",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE, null,
                textoBotones, textoBotones[1]);

        if (opcion == JOptionPane.YES_OPTION) {
            if (objAccesorio.getIdAccesorio() >= 0) {
                bandera = true;
            }
        }
        return bandera;
    }

    private void cargarDatosArriba(String nombre, String referencia, Integer cantidad, String codigosAccesoriosString) {

        cajaNombre.setText(nombre);
        cajaReferencia.setText(referencia);
        cajaCantidad.setText("" + cantidad);
        cajaInventario.setText(codigosAccesoriosString);

    }

    private void limpiarDatos() {
        cajaNombre.setText("");
        cajaReferencia.setText("");
        cajaCantidad.setText("");
        cajaInventario.setText("");
        cajaNombre.requestFocus();
    }

    private Boolean validacionCredenciales() {
        String nombre, referencia, codInv;
        Integer cant;
        Boolean bandera = true;

        nombre = cajaNombre.getText();
        if (nombre.equals("")) {
            cajaNombre.requestFocus();
            bandera = false;
            JOptionPane.showMessageDialog(jpContainer,
                    "Digite el nombre del accesorio",
                    "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
        }

        referencia = cajaReferencia.getText();
        if (referencia.equals("")) {
            cajaReferencia.requestFocus();
            bandera = false;
            JOptionPane.showMessageDialog(jpContainer,
                    "Digite la referencia del accesorio",
                    "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
        }

        try {
            cant = Integer.valueOf(cajaCantidad.getText());
            if (cant < 0) {
                cajaCantidad.requestFocus();
                bandera = false;
                JOptionPane.showMessageDialog(jpContainer,
                        "Digite una cantidad válida",
                        "Advertencia",
                        JOptionPane.WARNING_MESSAGE);
            }
        } catch (HeadlessException | NumberFormatException e) {
            bandera = false;
            JOptionPane.showMessageDialog(jpContainer,
                    "Digite una cantidad válida",
                    "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
        }

        codInv = cajaInventario.getText();
        if (codInv.equals("")) {
            cajaInventario.requestFocus();
            bandera = false;
            JOptionPane.showMessageDialog(jpContainer,
                    "Digite el código de inventario del accesorio",
                    "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
        }
        return bandera;
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

        PlaceHolder buscar = new PlaceHolder("Buscar...", cajaBuscar);
        PlaceHolder nombre = new PlaceHolder("Nombre", cajaNombre);
        PlaceHolder referencia = new PlaceHolder("ME-123", cajaReferencia);
        PlaceHolder cantidad = new PlaceHolder("Cantidad", cajaCantidad);
        PlaceHolder inventario = new PlaceHolder("0", cajaInventario);

        //DISEÑO DE LA TABLA 
        JTableHeader header = tabAccesorios.getTableHeader();
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
        tabAccesorios = new javax.swing.JTable();
        lblDatosGen = new javax.swing.JLabel();
        jpCantidad = new javax.swing.JPanel();
        lblCantidad = new javax.swing.JLabel();
        cajaCantidad = new javax.swing.JTextField();
        jpReferencia = new javax.swing.JPanel();
        lblReferencia = new javax.swing.JLabel();
        cajaReferencia = new javax.swing.JTextField();
        jpNombre = new javax.swing.JPanel();
        lblNombre = new javax.swing.JLabel();
        cajaNombre = new javax.swing.JTextField();
        btnEditar = new javax.swing.JButton();
        btnAnadir = new javax.swing.JButton();
        btnDescargar = new javax.swing.JButton();
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
        jpHome.setBounds(10, 170, 90, 80);

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
        cmbSort.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Ordenar/Buscar por", "ID", "Nombre", "Referencia", "Cantidad", "Código" }));
        cmbSort.setBorder(null);
        cmbSort.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSortActionPerformed(evt);
            }
        });
        jpContainer.add(cmbSort);
        cmbSort.setBounds(403, 34, 206, 35);

        lblTitulo.setFont(new java.awt.Font("Montserrat", 1, 60)); // NOI18N
        lblTitulo.setText("Accesorios");
        jpContainer.add(lblTitulo);
        lblTitulo.setBounds(10, 30, 390, 75);

        jSeparator1.setForeground(new java.awt.Color(37, 206, 203));
        jpContainer.add(jSeparator1);
        jSeparator1.setBounds(6, 330, 500, 10);

        tabAccesorios.setFont(new java.awt.Font("Montserrat SemiBold", 1, 18)); // NOI18N
        tabAccesorios.setModel(new javax.swing.table.DefaultTableModel(
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
        tabAccesorios.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        tabAccesorios.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabAccesoriosMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tabAccesorios);

        jpContainer.add(jScrollPane1);
        jScrollPane1.setBounds(10, 410, 901, 205);

        lblDatosGen.setFont(new java.awt.Font("Montserrat", 1, 18)); // NOI18N
        lblDatosGen.setText("Datos Generales:");
        jpContainer.add(lblDatosGen);
        lblDatosGen.setBounds(30, 110, 170, 23);

        jpCantidad.setBackground(new java.awt.Color(255, 255, 255));

        lblCantidad.setFont(new java.awt.Font("Montserrat SemiBold", 0, 14)); // NOI18N
        lblCantidad.setForeground(new java.awt.Color(98, 106, 109));
        lblCantidad.setText("Cantidad:");

        cajaCantidad.setFont(new java.awt.Font("Montserrat SemiBold", 1, 24)); // NOI18N
        cajaCantidad.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        cajaCantidad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cajaCantidadActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpCantidadLayout = new javax.swing.GroupLayout(jpCantidad);
        jpCantidad.setLayout(jpCantidadLayout);
        jpCantidadLayout.setHorizontalGroup(
            jpCantidadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpCantidadLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jpCantidadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpCantidadLayout.createSequentialGroup()
                        .addComponent(lblCantidad)
                        .addGap(0, 162, Short.MAX_VALUE))
                    .addComponent(cajaCantidad))
                .addContainerGap())
        );
        jpCantidadLayout.setVerticalGroup(
            jpCantidadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpCantidadLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblCantidad)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cajaCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(8, Short.MAX_VALUE))
        );

        jpContainer.add(jpCantidad);
        jpCantidad.setBounds(660, 150, 250, 70);

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
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(cajaReferencia, javax.swing.GroupLayout.DEFAULT_SIZE, 232, Short.MAX_VALUE))
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
        jpReferencia.setBounds(370, 150, 250, 70);

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
                .addContainerGap(8, Short.MAX_VALUE))
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
        btnDescargar.setBounds(670, 310, 210, 30);

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
                        .addGap(0, 0, Short.MAX_VALUE))
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
                .addContainerGap(8, Short.MAX_VALUE))
        );

        jpContainer.add(jpInventario);
        jpInventario.setBounds(660, 230, 130, 70);

        getContentPane().add(jpContainer);
        jpContainer.setBounds(110, 0, 930, 680);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cajaNombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cajaNombreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cajaNombreActionPerformed

    private void cajaReferenciaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cajaReferenciaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cajaReferenciaActionPerformed

    private void cajaCantidadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cajaCantidadActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cajaCantidadActionPerformed

    private void btnDescargarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDescargarActionPerformed
        String rutaAplicacion = System.getProperty("user.dir");
        String nombreArchivoSalida = "Accesorios.ramon";

        DaoAccesorio miDao = new DaoAccesorio();

        String ruta = miDao.loVoyAGrabarEnUnArchivoEsPeroYa(miModeloTabla, rutaAplicacion, nombreArchivoSalida);

        JOptionPane.showMessageDialog(jpContainer, "Se ha descargado la lista en " + ruta,
                "Descarga exitosa", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_btnDescargarActionPerformed

    private void cajaBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cajaBuscarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cajaBuscarActionPerformed

    private void lblHomeMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblHomeMouseEntered
        jpHome.setBackground(new Color(26, 49, 76));
        lblHome.setBackground(new Color(26, 49, 76));
    }//GEN-LAST:event_lblHomeMouseEntered

    private void lblHomeMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblHomeMouseExited
        jpHome.setBackground(Color.WHITE);
        lblHome.setBackground(Color.WHITE);
    }//GEN-LAST:event_lblHomeMouseExited

    private void cajaInventarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cajaInventarioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cajaInventarioActionPerformed

    private void cajaBuscarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cajaBuscarKeyReleased
        indiceBuscar = cmbSort.getSelectedIndex();

        String campoSelecccionado = campoBuscar(indiceBuscar);

        buscarDato("%" + cajaBuscar.getText().toLowerCase() + "%", campoSelecccionado);
    }//GEN-LAST:event_cajaBuscarKeyReleased

    private void cmbSortActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSortActionPerformed
        Integer indiceSort;

        indiceSort = cmbSort.getSelectedIndex();

        switch (indiceSort) {
            case 0 ->
                cargarAccesorios("a.nombre_accesorio");
            case 1 ->
                cargarAccesorios("a.id_accesorio");
            case 2 ->
                cargarAccesorios("a.nombre_accesorio");
            case 3 ->
                cargarAccesorios("a.referencia_accesorio");
            case 4 ->
                cargarAccesorios("a.cantidad_accesorio");
            case 5 ->
                cargarAccesorios("a.cod_inventario_accesorio");

        }
    }//GEN-LAST:event_cmbSortActionPerformed

    private void tabAccesoriosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabAccesoriosMouseClicked
        int columnaSeleccionada = tabAccesorios.getSelectedColumn();
        int filaSeleccionada = tabAccesorios.getSelectedRow();

        if (columnaSeleccionada != 7) {
            String idSeleccionadoString = miModeloTabla.getValueAt(filaSeleccionada, 0).toString();
            Integer idSeleccionado = Integer.valueOf(idSeleccionadoString);

            DaoAccesorio miDao = new DaoAccesorio();
            Accesorio objAccesorio = miDao.buscar(idSeleccionado);

            String nombre, referencia;
            Integer cantidad;

            List<String> codigosAccesorios = objAccesorio.getCodInventarioAccesorio();
            String codigosAccesoriosString = String.join(" ", codigosAccesorios);

            nombre = objAccesorio.getNombreAccesorio();
            referencia = objAccesorio.getReferenciaAccesorio();
            cantidad = objAccesorio.getCantidadAccesorio();

            cargarDatosArriba(nombre, referencia, cantidad, codigosAccesoriosString);
        }

        if (columnaSeleccionada == 7) {

            String codigoTexto = miModeloTabla.getValueAt(filaSeleccionada, 0).toString();
            idAccesorio = Integer.valueOf(codigoTexto);

            DaoAccesorio miDao = new DaoAccesorio();
            Accesorio objAccesorioElim = miDao.buscar(idAccesorio);

            if (objAccesorioElim == null) {
                JOptionPane.showMessageDialog(jpContainer, "No se encontró el accesorio",
                        "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                if (eliminar(idAccesorio)) {
                    DaoAccesorio miDaoElim = new DaoAccesorio();
                    if (miDaoElim.eliminar(idAccesorio)) {
                        cargarAccesorios("");
                        limpiarDatos();
                        JOptionPane.showMessageDialog(jpContainer, "Eliminación exitosa",
                                "Información", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(jpContainer, "No se pudo eliminar",
                                "Advertencia", JOptionPane.WARNING_MESSAGE);
                    }
                }
            }

        }
    }//GEN-LAST:event_tabAccesoriosMouseClicked

    private void btnAnadirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnadirActionPerformed
        if (validacionCredenciales()) {
            String nombre, referencia, codInv;
            Integer cant;

            nombre = cajaNombre.getText();
            referencia = cajaReferencia.getText();
            cant = Integer.valueOf(cajaCantidad.getText());

            codInv = cajaInventario.getText();

            List<String> listaCodigos = Arrays.asList(codInv.split(" "));

            Accesorio objAccesorio = new Accesorio();

            objAccesorio.setCodInventarioAccesorio(listaCodigos);
            objAccesorio.setNombreAccesorio(nombre);
            objAccesorio.setReferenciaAccesorio(referencia);
            objAccesorio.setCantidadAccesorio(cant);

            DaoAccesorio daoAccesorio = new DaoAccesorio();

            if (daoAccesorio.registrar(objAccesorio)) {
                JOptionPane.showMessageDialog(jpContainer,
                        "Se registró correctamente el accesorio",
                        "Información",
                        JOptionPane.INFORMATION_MESSAGE);
                limpiarDatos();
                cargarAccesorios("");
            } else {
                JOptionPane.showMessageDialog(jpContainer,
                        "Error en registro",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }

        }
    }//GEN-LAST:event_btnAnadirActionPerformed

    private void btnEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarActionPerformed
        if (validacionCredenciales()) {

            int filaSeleccionada = tabAccesorios.getSelectedRow();

            String codigoTexto = miModeloTabla.getValueAt(filaSeleccionada, 0).toString();
            Integer idAccesorio = Integer.valueOf(codigoTexto);

            DaoAccesorio miDao = new DaoAccesorio();
            Accesorio objAccesorio = miDao.buscar(idAccesorio);

            String nombre, referencia, codInv;
            Integer cant;

            nombre = cajaNombre.getText();
            referencia = cajaReferencia.getText();
            cant = Integer.valueOf(cajaCantidad.getText());

            codInv = cajaInventario.getText();

            List<String> listaCodigos = Arrays.asList(codInv.split(" "));

            objAccesorio.setCodInventarioAccesorio(listaCodigos);
            objAccesorio.setNombreAccesorio(nombre);
            objAccesorio.setReferenciaAccesorio(referencia);
            objAccesorio.setCantidadAccesorio(cant);

            DaoAccesorio daoAccesorio = new DaoAccesorio();

            if (daoAccesorio.actualizar(objAccesorio)) {
                JOptionPane.showMessageDialog(jpContainer,
                        "Se actualizo correctamente el accesorio",
                        "Información",
                        JOptionPane.INFORMATION_MESSAGE);
                cargarAccesorios("");
            } else {
                JOptionPane.showMessageDialog(jpContainer,
                        "Error en actualización",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }

        }
    }//GEN-LAST:event_btnEditarActionPerformed

    private void lblHomeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblHomeMouseClicked
        FrmElementosAdmin elementosAdmin = new FrmElementosAdmin();
        elementosAdmin.setVisible(true);
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
            java.util.logging.Logger.getLogger(FrmAccesorios.class
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

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FrmAccesorios().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAnadir;
    private javax.swing.JButton btnDescargar;
    private javax.swing.JButton btnEditar;
    private javax.swing.JTextField cajaBuscar;
    private javax.swing.JTextField cajaCantidad;
    private javax.swing.JTextField cajaInventario;
    private javax.swing.JTextField cajaNombre;
    private javax.swing.JTextField cajaReferencia;
    private javax.swing.JComboBox<String> cmbSort;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JPanel jpCantidad;
    private javax.swing.JPanel jpContainer;
    private javax.swing.JPanel jpHome;
    private javax.swing.JPanel jpInventario;
    private javax.swing.JPanel jpLateral;
    private javax.swing.JPanel jpLogo;
    private javax.swing.JPanel jpNombre;
    private javax.swing.JPanel jpReferencia;
    private javax.swing.JPanel jpSearch;
    private javax.swing.JLabel lblCantidad;
    private javax.swing.JLabel lblDatosGen;
    private javax.swing.JLabel lblHome;
    private javax.swing.JLabel lblImgSearch;
    private javax.swing.JLabel lblInventario;
    private javax.swing.JLabel lblLogo;
    private javax.swing.JLabel lblNombre;
    private javax.swing.JLabel lblReferencia;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JTable tabAccesorios;
    // End of variables declaration//GEN-END:variables
}
