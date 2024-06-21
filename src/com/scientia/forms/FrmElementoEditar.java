package com.scientia.forms;

import com.formdev.flatlaf.FlatLightLaf;
import com.scientia.daos.DaoAccesorio;
import com.scientia.daos.DaoElemento;
import com.scientia.daos.DaoEstudiante;
import com.scientia.entidades.Accesorio;
import com.scientia.entidades.Elemento;
import static com.scientia.forms.FrmGrupos.obtenerIDTablaEstudiante;
import com.scientia.utils.ImageScale;
import com.scientia.utils.ImageUtils;
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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import static javax.swing.SwingConstants.CENTER;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class FrmElementoEditar extends javax.swing.JFrame {

    String nomAgreg = "/com/scientia/icons/agregar.png";
    String rutaConAgregar = this.getClass().getResource(nomAgreg).getPath();
    ImageIcon agregarIcon = new ImageIcon(rutaConAgregar);

    String nomEliminar = "/com/scientia/icons/borrar.png";
    String rutaIconEliminar = this.getClass().getResource(nomEliminar).getPath();
    ImageIcon borrarIcono = new ImageIcon(rutaIconEliminar);

    private final Elemento objActualizar;
    private Integer codElem, columnaSeleccionada;
    ;

    private String rutaAplicacion;
    InputStream fotoInputStream;
    ImageIcon foto;

    private Map<Integer, Integer> losCodigosEquipo = new HashMap<>();

    private String titulosAcc[] = {"ID", "Nombre", "Añadir"};
    private String titulosAgreg[] = {"ID", "Nombre", "Eliminar"};

    private DefaultTableModel modeloTablaAcc = new DefaultTableModel(titulosAcc, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            if (columnIndex == 2) {
                return ImageIcon.class;
            } else {
                return Object.class;
            }
        }

    };
    private DefaultTableModel modeloTablaAgr = new DefaultTableModel(titulosAgreg, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            if (columnIndex == 2) {
                return ImageIcon.class;
            } else {
                return Object.class;
            }
        }

    };

    public FrmElementoEditar(JFrame parent, boolean modal, Elemento objExterno) {

        initComponents();

        cargarDatosElemento(objExterno);
        objActualizar = objExterno;

        tablaAccesorios.setModel(modeloTablaAcc);
        tablaAccesorioAgregado.setModel(modeloTablaAgr);

        //CARGA A LOS ACCESORIOS SEGUN EL ELEMENTO SELECCIONADO
        cargarAccesorioEditar(objExterno);
        cargarAccesorios("");

        //CARGAR IMAGEN DE LA BASE
        fotoInputStream = objActualizar.getFotoElemento();
        foto = ImageUtils.convertToImageIcon(fotoInputStream);
        lblSubirFoto.setIcon(foto);

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

    private void cargarAccesorios(String orden) {
        List<Accesorio> arrAccesorios;
        DaoAccesorio miDao = new DaoAccesorio();

        modeloTablaAcc.setNumRows(0);
        arrAccesorios = miDao.consultar(orden);

        System.out.println(arrAccesorios.toString());

        arrAccesorios.forEach((accesorio) -> {
            Object fila[] = new Object[3];

            fila[0] = accesorio.getIdAccesorio();
            fila[1] = accesorio.getNombreAccesorio();
            fila[2] = agregarIcon;

            modeloTablaAcc.addRow(fila);

        });

    }

    private void cargarAccesorioEditar(Elemento elemento) {
        //CARGA LOS ACCESORIOS EN LA TABLA SEGUN EL GRUPO
        modeloTablaAgr.setRowCount(0);
        List<Accesorio> arrAccesorios;
        DaoAccesorio miDaoAccesorio = new DaoAccesorio();

        modeloTablaAgr.setNumRows(0);

        //BUSCA A LOS ESTUDIANTES SEGUN EL GRUPO
        codElem = elemento.getId();
        System.out.println("cod Ele: " + codElem);
        arrAccesorios = miDaoAccesorio.consultarNombresPorElem(codElem);

        arrAccesorios.forEach((accesorio) -> {
            Object filita[] = new Object[3];

            filita[0] = accesorio.getIdAccesorio();
            filita[1] = accesorio.getNombreAccesorio();
            filita[2] = borrarIcono;

            modeloTablaAgr.addRow(filita);
        });
    }

    // Método para registrar estudiantes
    private void registrarAccesorioEditar(List<Integer> ids, int codElem) {
        DaoAccesorio daoAccesorio = new DaoAccesorio();
        for (Integer id : ids) {
            Accesorio objAccesorio = new Accesorio();
            Elemento objElemento = new Elemento(null, null, false, "", null, null, 0, codElem, "", "", false, 0);

            objAccesorio.setElemento(objElemento);
            objAccesorio.setIdAccesorio(id);
            daoAccesorio.actualizarAccesorio(objAccesorio);
        }
    }

    // Método para eliminar estudiantes
    private void eliminarAccesoriosEditar(List<Integer> ids) {
        DaoAccesorio daoAccesorio = new DaoAccesorio();
        for (Integer id : ids) {
            Accesorio objAccesorio = new Accesorio();
            Elemento objElemento = new Elemento(null, null, false, "", null, null, 0, null, "", "", false, 0);

            objAccesorio.setElemento(objElemento);
            objAccesorio.setIdAccesorio(id);
            daoAccesorio.actualizarAccesorio(objAccesorio);
        }
    }

    private void agregarAccesorio() {
        int filaSeleccionada = tablaAccesorios.getSelectedRow();

        if (filaSeleccionada == -1) {
            return;
        }

        DefaultTableModel modelTablaAccesorio = (DefaultTableModel) tablaAccesorios.getModel();
        DefaultTableModel modelTablaAccesorioSeleccionado = (DefaultTableModel) tablaAccesorioAgregado.getModel();

        boolean exists = false;

        // Verificar si el estudiante ya está en la tabla de estudiantes agregados
        for (int i = 0; i < modelTablaAccesorioSeleccionado.getRowCount(); i++) {
            if (modelTablaAccesorioSeleccionado.getValueAt(i, 0).equals(modelTablaAccesorio.getValueAt(filaSeleccionada, 0))) {
                exists = true;
                JOptionPane.showMessageDialog(jpContainer, "El estudiante ya ha sido agregado", "Información", JOptionPane.INFORMATION_MESSAGE);
                break;
            }
        }

        if (!exists) {
            Object[] datosFila = new Object[modelTablaAccesorio.getColumnCount()];

            for (int i = 0; i < datosFila.length; i++) {
                datosFila[i] = modelTablaAccesorio.getValueAt(filaSeleccionada, i);
            }

            datosFila[datosFila.length - 1] = borrarIcono;

            modelTablaAccesorioSeleccionado.addRow(datosFila);
            modelTablaAccesorio.removeRow(filaSeleccionada);
        }
    }

    private void removerAccesorio() {
        int filaSeleccionada = tablaAccesorioAgregado.getSelectedRow();

        if (filaSeleccionada == -1) {
            return;
        }

        DefaultTableModel modelTablaAccesorio = (DefaultTableModel) tablaAccesorios.getModel();
        DefaultTableModel modelTablaAccesorioSeleccionado = (DefaultTableModel) tablaAccesorioAgregado.getModel();

        boolean exists = false;

        for (int i = 0; i < modelTablaAccesorio.getRowCount(); i++) {
            if (modelTablaAccesorio.getValueAt(i, 0).equals(modelTablaAccesorioSeleccionado.getValueAt(filaSeleccionada, 0))) {
                exists = true;
                break;
            }
        }

        if (exists) {
            modelTablaAccesorioSeleccionado.removeRow(filaSeleccionada);
        } else {
            Object[] datosFila = new Object[modelTablaAccesorioSeleccionado.getColumnCount()];

            for (int i = 0; i < datosFila.length; i++) {
                datosFila[i] = modelTablaAccesorioSeleccionado.getValueAt(filaSeleccionada, i);
            }

            datosFila[datosFila.length - 1] = agregarIcon; // Añadir el ícono en la última columna

            modelTablaAccesorio.addRow(datosFila);
            modelTablaAccesorioSeleccionado.removeRow(filaSeleccionada);
        }
    }

    private void cargarDatosElemento(Elemento elemento) {

        cajaNombre.setText(elemento.getNombre());
        cajaMarca.setText(elemento.getMarca());
        cajaExistencias.setText("" + elemento.getExistencias());
        cajaCosto.setText("" + elemento.getCostoElemento());
        cajaActa.setText(elemento.getActaElemento());
        fCompra.setDate(elemento.getFechaCompraElemento());

        List<String> codigosElemento = elemento.getCodInventarioElemento();
        String codigosElementoString = String.join(" ", codigosElemento);
        cajaInventario.setText(codigosElementoString);

        chkDisponible.setSelected(elemento.getDisponibilidad());
        chkManual.setSelected(elemento.getManualElemento());
        cargarAccesorioEditar(elemento);

    }

    private void buscarAccesorio(String dato, String campo) {
        //NO CAMBIAR LOS PARÁMETROS DE ENTRADA
        List<Accesorio> accesoriosEncontrados;
        DaoAccesorio daoAccesorio = new DaoAccesorio();

        modeloTablaAcc.setNumRows(0);

        accesoriosEncontrados = daoAccesorio.buscarDato(dato, campo);
        accesoriosEncontrados.forEach((accesorio) -> {
            Object fila[] = new Object[3];

            fila[0] = accesorio.getIdAccesorio();
            fila[1] = accesorio.getNombreAccesorio();
            fila[2] = agregarIcon;

            modeloTablaAcc.addRow(fila);
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
        jpHome.setBounds(35, 250, (int) (width * 0.055), (int) (height * 0.09));
        lblHome.setBounds(18, 0, (int) (width * 0.055), (int) (height * 0.09));

        jpRegresar.setBounds(1170, 20, (int) (width * 0.05), (int) (height * 0.08));
        lblRegresar.setBounds(13, 0, (int) (width * 0.05), (int) (height * 0.08));

        jpNombre.setBounds(15, 150, (int) (width * 0.23), (int) (height * 0.09));
        jpMarca.setBounds(400, 150, (int) (width * 0.23), (int) (height * 0.09));
        jpExistencias.setBounds(770, 150, (int) (width * 0.23), (int) (height * 0.09));
        jpCosto.setBounds(15, 250, (int) (width * 0.23), (int) (height * 0.09));
        jpActa.setBounds(400, 250, (int) (width * 0.23), (int) (height * 0.09));
        jpFecha.setBounds(770, 250, (int) (width * 0.23), (int) (height * 0.09));

        chkDisponible.setBounds(1150, 130, (int) (width * 0.23), (int) (height * 0.09));
        chkManual.setBounds(1150, 170, (int) (width * 0.23), (int) (height * 0.09));
        jpInventario.setBounds(1150, 250, (int) (width * 0.1), (int) (height * 0.09));

        jSeparator1.setBounds(35, 370, (int) (width * 0.75), (int) (height * 0.06));

        lblAcc.setBounds(20, 425, (int) (width * 0.2), (int) (height * 0.03));
        jpBuscarAccesorios.setBounds(200, 420, (int) (width * 0.13), (int) (height * 0.043));
        jsAccesorios.setBounds(20, 470, (int) (width * 0.25), (int) (height * 0.33));

        lblAccSel1.setBounds(480, 425, (int) (width * 0.2), (int) (height * 0.033));
        jsAccesorioAgregado.setBounds(480, 470, (int) (width * 0.25), (int) (height * 0.33));

        jpFoto.setBounds(920, 420, (int) (width * 0.23), (int) (height * 0.29));
        lblSubirFoto.setBounds(15, 30, (int) (width * 0.213), (int) (height * 0.23));
        btnAnadir.setBounds(1000, 700, (int) (width * 0.15), (int) (height * 0.052));

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
        PlaceHolder existencias = new PlaceHolder("0", cajaExistencias);
        PlaceHolder costo = new PlaceHolder("0", cajaCosto);
        PlaceHolder acta = new PlaceHolder("NR", cajaActa);

        //DISEÑO DE LA TABLA 
        JTableHeader headerAcc = tablaAccesorios.getTableHeader();
        headerAcc.setDefaultRenderer(new FrmElementoRegistrar.CustomHeaderRenderer());

        JTableHeader headerAgr = tablaAccesorioAgregado.getTableHeader();
        headerAgr.setDefaultRenderer(new CustomHeaderRenderer());
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

    private Boolean validarCredenciales() {
        Boolean bandera = true;

        String nombre, marca, acta, codInventario;
        Integer existencias;
        Double costo;

        nombre = cajaNombre.getText();

        if (nombre.equals("")) {
            cajaNombre.requestFocus();
            bandera = false;
            JOptionPane.showMessageDialog(jpContainer,
                    "Digite el nombre del elemento",
                    "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
        }

        marca = cajaMarca.getText();

        if (marca.equals("")) {
            cajaMarca.requestFocus();
            bandera = false;
            JOptionPane.showMessageDialog(jpContainer,
                    "Digite la marca del elemento",
                    "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
        }

        try {
            existencias = Integer.valueOf(cajaExistencias.getText());
            if (existencias < 0) {
                cajaExistencias.requestFocus();
                bandera = false;
                JOptionPane.showMessageDialog(jpContainer,
                        "Digite una cantidad de existencias válidas",
                        "Advertencia",
                        JOptionPane.WARNING_MESSAGE);
            }
        } catch (HeadlessException | NumberFormatException e) {
            bandera = false;
            JOptionPane.showMessageDialog(jpContainer,
                    "Digite una cantidad de existencias válidas",
                    "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
        }

        try {
            costo = Double.valueOf(cajaCosto.getText());
            if (costo < 0) {
                cajaCosto.requestFocus();
                bandera = false;
                JOptionPane.showMessageDialog(jpContainer,
                        "Digite un costo válido",
                        "Advertencia",
                        JOptionPane.WARNING_MESSAGE);
            }
        } catch (HeadlessException | NumberFormatException e) {
            bandera = false;
            JOptionPane.showMessageDialog(jpContainer,
                    "Digite un costo válido",
                    "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
        }

        acta = cajaActa.getText();

        if (acta.equals("")) {
            cajaMarca.requestFocus();
            bandera = false;
            JOptionPane.showMessageDialog(jpContainer,
                    "Digite la acta del elemento",
                    "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
        }

        if (fCompra.getDate() == null) {
            bandera = false;
            JOptionPane.showMessageDialog(jpContainer,
                    "Seleccione la fecha de compra del elemento",
                    "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
        }

        codInventario = cajaInventario.getText();

        if (codInventario.equals("")) {
            cajaMarca.requestFocus();
            bandera = false;
            JOptionPane.showMessageDialog(jpContainer,
                    "Digite el código de inventario del elemento",
                    "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
        }

        return bandera;
    }

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
        jSeparator1 = new javax.swing.JSeparator();
        lblAcc = new javax.swing.JLabel();
        jpExistencias = new javax.swing.JPanel();
        lblExistencias = new javax.swing.JLabel();
        cajaExistencias = new javax.swing.JTextField();
        jpMarca = new javax.swing.JPanel();
        lblMarca = new javax.swing.JLabel();
        cajaMarca = new javax.swing.JTextField();
        jpCosto = new javax.swing.JPanel();
        lblCosto = new javax.swing.JLabel();
        cajaCosto = new javax.swing.JTextField();
        jpNombre = new javax.swing.JPanel();
        lblNombre = new javax.swing.JLabel();
        cajaNombre = new javax.swing.JTextField();
        btnAnadir = new javax.swing.JButton();
        jsAccesorios = new javax.swing.JScrollPane();
        tablaAccesorios = new javax.swing.JTable();
        jpBuscarAccesorios = new javax.swing.JPanel();
        cajaBuscarEstudiantes = new javax.swing.JTextField();
        jsAccesorioAgregado = new javax.swing.JScrollPane();
        tablaAccesorioAgregado = new javax.swing.JTable();
        lblDatos = new javax.swing.JLabel();
        lblAccSel1 = new javax.swing.JLabel();
        jpActa = new javax.swing.JPanel();
        lblActa = new javax.swing.JLabel();
        cajaActa = new javax.swing.JTextField();
        chkManual = new javax.swing.JCheckBox();
        chkDisponible = new javax.swing.JCheckBox();
        jpFecha = new javax.swing.JPanel();
        lblFecha = new javax.swing.JLabel();
        fCompra = new com.toedter.calendar.JDateChooser();
        jpFoto = new javax.swing.JPanel();
        lblFoto = new javax.swing.JLabel();
        lblSubirFoto = new javax.swing.JLabel();
        jpRegresar = new javax.swing.JPanel();
        lblRegresar = new javax.swing.JLabel();
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
        lblTitulo.setText("Editar Elemento");
        jpContainer.add(lblTitulo);
        lblTitulo.setBounds(30, 30, 770, 75);

        jSeparator1.setForeground(new java.awt.Color(37, 206, 203));
        jpContainer.add(jSeparator1);
        jSeparator1.setBounds(230, 320, 500, 10);

        lblAcc.setFont(new java.awt.Font("Montserrat", 1, 18)); // NOI18N
        lblAcc.setText("Accesorios ");
        jpContainer.add(lblAcc);
        lblAcc.setBounds(10, 390, 110, 23);

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
        jpExistencias.setBounds(530, 150, 250, 70);

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
        jpMarca.setBounds(270, 150, 250, 70);

        jpCosto.setBackground(new java.awt.Color(255, 255, 255));

        lblCosto.setFont(new java.awt.Font("Montserrat SemiBold", 0, 14)); // NOI18N
        lblCosto.setForeground(new java.awt.Color(98, 106, 109));
        lblCosto.setText("Costo:");

        cajaCosto.setFont(new java.awt.Font("Montserrat SemiBold", 1, 24)); // NOI18N
        cajaCosto.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        cajaCosto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cajaCostoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpCostoLayout = new javax.swing.GroupLayout(jpCosto);
        jpCosto.setLayout(jpCostoLayout);
        jpCostoLayout.setHorizontalGroup(
            jpCostoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpCostoLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jpCostoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpCostoLayout.createSequentialGroup()
                        .addComponent(lblCosto)
                        .addGap(0, 188, Short.MAX_VALUE))
                    .addComponent(cajaCosto))
                .addContainerGap())
        );
        jpCostoLayout.setVerticalGroup(
            jpCostoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpCostoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblCosto)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cajaCosto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jpContainer.add(jpCosto);
        jpCosto.setBounds(10, 230, 250, 70);

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
        btnAnadir.setText("Editar Elemento");
        btnAnadir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnadirActionPerformed(evt);
            }
        });
        jpContainer.add(btnAnadir);
        btnAnadir.setBounds(700, 580, 200, 30);

        tablaAccesorios.setFont(new java.awt.Font("Montserrat SemiBold", 0, 14)); // NOI18N
        tablaAccesorios.setModel(new javax.swing.table.DefaultTableModel(
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
        tablaAccesorios.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        tablaAccesorios.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaAccesoriosMouseClicked(evt);
            }
        });
        jsAccesorios.setViewportView(tablaAccesorios);

        jpContainer.add(jsAccesorios);
        jsAccesorios.setBounds(50, 420, 290, 210);

        jpBuscarAccesorios.setBackground(new java.awt.Color(255, 255, 255));

        cajaBuscarEstudiantes.setFont(new java.awt.Font("Montserrat", 1, 18)); // NOI18N
        cajaBuscarEstudiantes.setBorder(null);
        cajaBuscarEstudiantes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cajaBuscarEstudiantesActionPerformed(evt);
            }
        });
        cajaBuscarEstudiantes.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                cajaBuscarEstudiantesKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jpBuscarAccesoriosLayout = new javax.swing.GroupLayout(jpBuscarAccesorios);
        jpBuscarAccesorios.setLayout(jpBuscarAccesoriosLayout);
        jpBuscarAccesoriosLayout.setHorizontalGroup(
            jpBuscarAccesoriosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpBuscarAccesoriosLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cajaBuscarEstudiantes, javax.swing.GroupLayout.DEFAULT_SIZE, 188, Short.MAX_VALUE)
                .addContainerGap())
        );
        jpBuscarAccesoriosLayout.setVerticalGroup(
            jpBuscarAccesoriosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpBuscarAccesoriosLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cajaBuscarEstudiantes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jpContainer.add(jpBuscarAccesorios);
        jpBuscarAccesorios.setBounds(140, 380, 200, 35);

        tablaAccesorioAgregado.setFont(new java.awt.Font("Montserrat SemiBold", 0, 14)); // NOI18N
        tablaAccesorioAgregado.setModel(new javax.swing.table.DefaultTableModel(
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
        tablaAccesorioAgregado.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        tablaAccesorioAgregado.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaAccesorioAgregadoMouseClicked(evt);
            }
        });
        jsAccesorioAgregado.setViewportView(tablaAccesorioAgregado);

        jpContainer.add(jsAccesorioAgregado);
        jsAccesorioAgregado.setBounds(360, 420, 300, 210);

        lblDatos.setFont(new java.awt.Font("Montserrat", 1, 18)); // NOI18N
        lblDatos.setText("Datos Generales:");
        jpContainer.add(lblDatos);
        lblDatos.setBounds(30, 110, 170, 23);

        lblAccSel1.setFont(new java.awt.Font("Montserrat", 1, 18)); // NOI18N
        lblAccSel1.setText("Accesorios en elementos");
        jpContainer.add(lblAccSel1);
        lblAccSel1.setBounds(360, 390, 260, 23);

        jpActa.setBackground(new java.awt.Color(255, 255, 255));

        lblActa.setFont(new java.awt.Font("Montserrat SemiBold", 0, 14)); // NOI18N
        lblActa.setForeground(new java.awt.Color(98, 106, 109));
        lblActa.setText("Acta del elemento:");

        cajaActa.setFont(new java.awt.Font("Montserrat SemiBold", 1, 24)); // NOI18N
        cajaActa.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        cajaActa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cajaActaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpActaLayout = new javax.swing.GroupLayout(jpActa);
        jpActa.setLayout(jpActaLayout);
        jpActaLayout.setHorizontalGroup(
            jpActaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpActaLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jpActaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpActaLayout.createSequentialGroup()
                        .addComponent(lblActa)
                        .addGap(0, 97, Short.MAX_VALUE))
                    .addComponent(cajaActa))
                .addContainerGap())
        );
        jpActaLayout.setVerticalGroup(
            jpActaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpActaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblActa)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cajaActa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(8, Short.MAX_VALUE))
        );

        jpContainer.add(jpActa);
        jpActa.setBounds(270, 230, 250, 70);

        chkManual.setFont(new java.awt.Font("Montserrat", 1, 18)); // NOI18N
        chkManual.setText("Manual");
        jpContainer.add(chkManual);
        chkManual.setBounds(800, 190, 85, 27);

        chkDisponible.setFont(new java.awt.Font("Montserrat", 1, 18)); // NOI18N
        chkDisponible.setText("Disponible");
        jpContainer.add(chkDisponible);
        chkDisponible.setBounds(800, 160, 90, 27);

        jpFecha.setBackground(new java.awt.Color(255, 255, 255));

        lblFecha.setFont(new java.awt.Font("Montserrat SemiBold", 0, 14)); // NOI18N
        lblFecha.setForeground(new java.awt.Color(98, 106, 109));
        lblFecha.setText("Fecha de compra:");

        javax.swing.GroupLayout jpFechaLayout = new javax.swing.GroupLayout(jpFecha);
        jpFecha.setLayout(jpFechaLayout);
        jpFechaLayout.setHorizontalGroup(
            jpFechaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpFechaLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jpFechaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpFechaLayout.createSequentialGroup()
                        .addComponent(lblFecha)
                        .addGap(0, 103, Short.MAX_VALUE))
                    .addComponent(fCompra, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jpFechaLayout.setVerticalGroup(
            jpFechaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpFechaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblFecha)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(fCompra, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                .addContainerGap())
        );

        jpContainer.add(jpFecha);
        jpFecha.setBounds(530, 230, 250, 70);

        jpFoto.setBackground(new java.awt.Color(255, 255, 255));
        jpFoto.setLayout(null);

        lblFoto.setFont(new java.awt.Font("Montserrat SemiBold", 0, 14)); // NOI18N
        lblFoto.setForeground(new java.awt.Color(98, 106, 109));
        lblFoto.setText("Foto:");
        jpFoto.add(lblFoto);
        lblFoto.setBounds(12, 6, 35, 18);

        lblSubirFoto.setFont(new java.awt.Font("Montserrat", 2, 18)); // NOI18N
        lblSubirFoto.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSubirFoto.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(37, 206, 203), 2, true));
        lblSubirFoto.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblSubirFoto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblSubirFotoMouseClicked(evt);
            }
        });
        jpFoto.add(lblSubirFoto);
        lblSubirFoto.setBounds(12, 30, 163, 125);

        jpContainer.add(jpFoto);
        jpFoto.setBounds(710, 370, 190, 170);

        jpRegresar.setBackground(new java.awt.Color(255, 255, 255));
        jpRegresar.setLayout(null);

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
        jpInventario.setBounds(790, 230, 130, 70);

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

    private void cajaExistenciasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cajaExistenciasActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cajaExistenciasActionPerformed

    private void cajaCostoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cajaCostoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cajaCostoActionPerformed

    private void lblHomeMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblHomeMouseEntered
        jpHome.setBackground(new Color(26, 49, 76));
        lblHome.setBackground(new Color(26, 49, 76));
    }//GEN-LAST:event_lblHomeMouseEntered

    private void lblHomeMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblHomeMouseExited
        jpHome.setBackground(Color.WHITE);
        lblHome.setBackground(Color.WHITE);
    }//GEN-LAST:event_lblHomeMouseExited

    private void cajaBuscarEstudiantesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cajaBuscarEstudiantesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cajaBuscarEstudiantesActionPerformed

    private void cajaActaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cajaActaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cajaActaActionPerformed

    private void lblRegresarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblRegresarMouseEntered
        jpRegresar.setBackground(new Color(26, 49, 76));
        jpRegresar.setBackground(new Color(26, 49, 76));
    }//GEN-LAST:event_lblRegresarMouseEntered

    private void lblRegresarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblRegresarMouseExited
        jpRegresar.setBackground(Color.WHITE);
        jpRegresar.setBackground(Color.WHITE);
    }//GEN-LAST:event_lblRegresarMouseExited

    private void cajaInventarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cajaInventarioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cajaInventarioActionPerformed

    private void btnAnadirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnadirActionPerformed

        //GUARDA LOS ID DE CADA ESTUDIANTE QUE ESTE ASOCIADO AL GRUPO
        DaoEstudiante daoEstudiante = new DaoEstudiante();
        List<Integer> listaDeIdsAntes = new ArrayList<>(daoEstudiante.consultarIdsPorGrupo(codElem));

        System.out.println("antes: " + listaDeIdsAntes);
        if (validarCredenciales()) {

            List<Integer> listaDeIdsDespues = new ArrayList<>(obtenerIDTablaEstudiante(tablaAccesorioAgregado));
            System.out.println("antes: " + listaDeIdsAntes);
            System.out.println("des: " + listaDeIdsDespues);

            // Convertir las listas a conjuntos para facilitar las operaciones de comparación
            Set<Integer> setAntiguo = new HashSet<>(listaDeIdsAntes);
            Set<Integer> setNuevo = new HashSet<>(listaDeIdsDespues);

            // Encontrar IDs que se han eliminado
            Set<Integer> eliminados = new HashSet<>(setAntiguo);
            eliminados.removeAll(setNuevo);

            // Encontrar IDs que se han añadido
            Set<Integer> añadidos = new HashSet<>(setNuevo);
            añadidos.removeAll(setAntiguo);

            // Encontrar IDs que permanecen iguales
            Set<Integer> iguales = new HashSet<>(setAntiguo);
            iguales.retainAll(setNuevo);

            // Convertir los sets a listas
            List<Integer> listaEliminados = new ArrayList<>(eliminados);
            List<Integer> listaAñadidos = new ArrayList<>(añadidos);

            // Procesar los IDs eliminados y añadidos
            eliminarAccesoriosEditar(listaEliminados);
            registrarAccesorioEditar(listaAñadidos, codElem);

            String nombre, marca, acta, codInv;
            Integer existencias;
            Double costo;
            Boolean disponible = false, manual = false;
            Date fechaCompra;

            nombre = cajaNombre.getText();
            marca = cajaMarca.getText();
            existencias = Integer.valueOf(cajaExistencias.getText());

            costo = Double.valueOf(cajaCosto.getText());
            acta = cajaActa.getText();
            fechaCompra = fCompra.getDate();

            if (chkDisponible.isSelected()) {
                disponible = true;
            }

            if (chkManual.isSelected()) {
                manual = true;
            }

            codInv = cajaInventario.getText();

            List<String> listaCodigos = Arrays.asList(codInv.split(" "));

            objActualizar.setNombre(nombre);
            objActualizar.setMarca(marca);
            objActualizar.setExistencias(existencias);
            objActualizar.setCostoElemento(costo);
            objActualizar.setActaElemento(acta);
            objActualizar.setFechaCompraElemento(fechaCompra);
            objActualizar.setDisponibilidad(disponible);
            objActualizar.setManualElemento(manual);
            objActualizar.setCodInventarioElemento(listaCodigos);

            // Verifica que la ruta de la aplicación no sea null
            if (rutaAplicacion != null) {
                try {
                    File fotoFile = new File(rutaAplicacion);
                    if (fotoFile.exists()) {
                        fotoInputStream = new FileInputStream(fotoFile);
                        objActualizar.setFotoElemento(fotoInputStream);
                    } else {
                        JOptionPane.showMessageDialog(jpContainer, "La ruta de la foto no es válida o el archivo no existe.", "Error", JOptionPane.ERROR_MESSAGE);

                    }
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(FrmConsumibleEditar.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                // Manejo si la ruta de la foto es null
                JOptionPane.showMessageDialog(jpContainer, "La ruta de la foto es null.", "Error", JOptionPane.ERROR_MESSAGE);
            }

            DaoElemento daoElemento = new DaoElemento();

            if (daoElemento.actualizar(objActualizar)) {
                JOptionPane.showMessageDialog(jpContainer,
                        "Se actualizó correctamente",
                        "Información",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(jpContainer,
                        "Error en actualización",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
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
        FrmElementosAdmin elementosAdmin = new FrmElementosAdmin();
        elementosAdmin.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_lblRegresarMouseClicked

    private void lblHomeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblHomeMouseClicked
        FrmPrincipal principal = new FrmPrincipal();
        principal.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_lblHomeMouseClicked

    private void tablaAccesorioAgregadoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaAccesorioAgregadoMouseClicked
        columnaSeleccionada = tablaAccesorioAgregado.getSelectedColumn();
        if (columnaSeleccionada == 2) {
            removerAccesorio();
        }
    }//GEN-LAST:event_tablaAccesorioAgregadoMouseClicked

    private void tablaAccesoriosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaAccesoriosMouseClicked
        columnaSeleccionada = tablaAccesorios.getSelectedColumn();
        if (columnaSeleccionada == 2) {
            agregarAccesorio();
        }
    }//GEN-LAST:event_tablaAccesoriosMouseClicked

    private void cajaBuscarEstudiantesKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cajaBuscarEstudiantesKeyReleased
        buscarAccesorio("%" + cajaBuscarEstudiantes.getText().toLowerCase() + "%", "a.nombre_accesorio");
    }//GEN-LAST:event_cajaBuscarEstudiantesKeyReleased

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {

            javax.swing.UIManager.setLookAndFeel(new FlatLightLaf());

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrmElementoEditar.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                // Supongamos que no tenemos un JFrame padre, así que pasamos null
                // Supongamos que queremos que el diálogo sea modal, así que pasamos true
                // Supongamos que no tenemos un objeto Equipo para editar, así que pasamos null
                new FrmElementoEditar(null, true, null).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAnadir;
    private javax.swing.JTextField cajaActa;
    private javax.swing.JTextField cajaBuscarEstudiantes;
    private javax.swing.JTextField cajaCosto;
    private javax.swing.JTextField cajaExistencias;
    private javax.swing.JTextField cajaInventario;
    private javax.swing.JTextField cajaMarca;
    private javax.swing.JTextField cajaNombre;
    private javax.swing.JCheckBox chkDisponible;
    private javax.swing.JCheckBox chkManual;
    private com.toedter.calendar.JDateChooser fCompra;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JPanel jpActa;
    private javax.swing.JPanel jpBuscarAccesorios;
    private javax.swing.JPanel jpContainer;
    private javax.swing.JPanel jpCosto;
    private javax.swing.JPanel jpExistencias;
    private javax.swing.JPanel jpFecha;
    private javax.swing.JPanel jpFoto;
    private javax.swing.JPanel jpHome;
    private javax.swing.JPanel jpInventario;
    private javax.swing.JPanel jpLateral;
    private javax.swing.JPanel jpLogo;
    private javax.swing.JPanel jpMarca;
    private javax.swing.JPanel jpNombre;
    private javax.swing.JPanel jpRegresar;
    private javax.swing.JScrollPane jsAccesorioAgregado;
    private javax.swing.JScrollPane jsAccesorios;
    private javax.swing.JLabel lblAcc;
    private javax.swing.JLabel lblAccSel1;
    private javax.swing.JLabel lblActa;
    private javax.swing.JLabel lblCosto;
    private javax.swing.JLabel lblDatos;
    private javax.swing.JLabel lblExistencias;
    private javax.swing.JLabel lblFecha;
    private javax.swing.JLabel lblFoto;
    private javax.swing.JLabel lblHome;
    private javax.swing.JLabel lblInventario;
    private javax.swing.JLabel lblLogo;
    private javax.swing.JLabel lblMarca;
    private javax.swing.JLabel lblNombre;
    private javax.swing.JLabel lblRegresar;
    private javax.swing.JLabel lblSubirFoto;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JTable tablaAccesorioAgregado;
    private javax.swing.JTable tablaAccesorios;
    // End of variables declaration//GEN-END:variables
}
