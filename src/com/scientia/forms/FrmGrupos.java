package com.scientia.forms;

import com.formdev.flatlaf.FlatLightLaf;
import com.scientia.daos.DaoEstudiante;
import com.scientia.entidades.GrupoLaboratorio;
import com.scientia.daos.DaoGrupo;
import com.scientia.daos.DaoGrupoLaboratorio;
import com.scientia.entidades.Estudiante;
import com.scientia.entidades.Grupo;
import com.scientia.utils.ImageScale;
import com.scientia.utils.PlaceHolder;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import static java.awt.Frame.MAXIMIZED_BOTH;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import static javax.swing.SwingConstants.CENTER;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class FrmGrupos extends javax.swing.JFrame {

    private Integer codLab = null;
    private Integer codGrupo = null;

    private final Map<Integer, Integer> losCodigosLab = new HashMap<>();

    private Map<Integer, Integer> codigosLaboratorios = new HashMap<>();
    private DefaultComboBoxModel modeloCombo = new DefaultComboBoxModel();
    private Integer indiceLab;

    Integer indiceBuscar = 0;
    Integer idGrupo, columnaSeleccionada;

    String nomAgreg = "/com/scientia/icons/agregar.png";
    String rutaConAgregar = this.getClass().getResource(nomAgreg).getPath();
    ImageIcon agregarIcon = new ImageIcon(rutaConAgregar);

    String nomEliminar = "/com/scientia/icons/borrar.png";
    String rutaIconEliminar = this.getClass().getResource(nomEliminar).getPath();
    ImageIcon borrarIcono = new ImageIcon(rutaIconEliminar);

    String nomEditar = "/com/scientia/icons/editar.png";
    String rutaIconEditar = this.getClass().getResource(nomEditar).getPath();
    ImageIcon editarIcono = new ImageIcon(rutaIconEditar);

    private String titulosTe[] = {"ID", "Nombre", "Apellido", "+"};
    private DefaultTableModel modeloTablaEstudiante = new DefaultTableModel(titulosTe, 0) {

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            if (columnIndex == 3) {
                return ImageIcon.class;
            }
            if (columnIndex == 4) {
                return ImageIcon.class;
            }
            return Object.class;
        }
    };

    private String titulosTeA[] = {"ID", "Nombre", "Apellido", "-"};
    private DefaultTableModel modelotablaEstuAgregado = new DefaultTableModel(titulosTeA, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            if (columnIndex == 3) {
                return ImageIcon.class;
            }
            return Object.class;
        }
    };

    private String titulosTEL[] = {"Grupo", "Laboratorio", "Editar", "Eliminar"};
    private DefaultTableModel modeloTablaGrupos = new DefaultTableModel(titulosTEL, 0) {

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            if (columnIndex == 2 || columnIndex == 3) {
                return ImageIcon.class;
            }

            return Object.class;
        }
    };

    public FrmGrupos() {
        initComponents();

        cmbLaboratorio.setModel(modeloCombo);
        tablaEstudiante.setModel(modeloTablaEstudiante);
        tablaEstudianteAgregado.setModel(modelotablaEstuAgregado);
        tabGrupo.setModel(modeloTablaGrupos);
        cargarEstudiante("");
        cargarGrupos("");
        cargarLaboratorios();

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
                if (!e.getSource().equals(tablaEstudiante)) {
                    tabGrupo.clearSelection();
                    limpiarCajas();
                    cargarEstudiante("");
                    modelotablaEstuAgregado.setRowCount(0);
                    editarDesactivado();
                    cargarLaboratorios();
                }
            }
        });

        tablaEstudiante.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                tablaEstudiante.requestFocus();
            }
        });

        String nomLogo = "..\\ScientiaLab\\src\\com\\scientia\\icons\\logo.png";
        System.out.println(nomLogo);
        ImageScale.setImageToLabel(lblLogo, nomLogo);

    }

    private Boolean estaTodoBien() {
        Integer seleccionado;
        Boolean bandera = true;

        seleccionado = cmbLaboratorio.getSelectedIndex();
        if (seleccionado == 0) {
            cmbLaboratorio.getSelectedIndex();
            bandera = false;
            JOptionPane.showMessageDialog(jpContainer,
                    "Seleccione un laboratorio",
                    "ADVERTENCIA",
                    JOptionPane.WARNING_MESSAGE);
        }

        DefaultTableModel modelTablaEstudianteSeleccionado = (DefaultTableModel) tablaEstudianteAgregado.getModel();
        if (modelTablaEstudianteSeleccionado.getRowCount() == 0) {
            bandera = false;
            JOptionPane.showMessageDialog(jpContainer,
                    "Agregue estudiantes",
                    "ADVERTENCIA",
                    JOptionPane.WARNING_MESSAGE);
        }

        return bandera;
    }

    private void editarActivado() {
        btnAnadir.setEnabled(false);
    }

    private void editarDesactivado() {
        btnAnadir.setEnabled(true);
    }

    private void limpiarCajas() {
        cmbLaboratorio.setSelectedIndex(0);
        modelotablaEstuAgregado.setRowCount(0);
    }

    private String campoBuscar(int select) {

        String campo = "";

        switch (select) {
            case 1 ->
                campo = "g.id_grupo";
            case 2 ->
                campo = "gl.id_grupo_laboratorio";
            case 3 ->
                campo = "gl.nombre_laboratorio";

        }

        return campo;
    }

    private void buscarDato(String dato, String campo) {
        List<Grupo> arregloGrupos;
        DaoGrupo dao = new DaoGrupo();

        modeloTablaGrupos.setNumRows(0);

        arregloGrupos = dao.buscarDato(dato, campo);

        arregloGrupos.forEach((grupo) -> {
            Object fila[] = new Object[4];

            fila[0] = grupo.getIdGrupo();
            fila[1] = grupo.getIdGrupoLaboratorio().getNombreLaboratorio();
            fila[2] = borrarIcono;
            fila[3] = editarIcono;

            modeloTablaGrupos.addRow(fila);

        });
    }

    private void cargarGrupos(String ordencito) {
        List<Grupo> arrGrup;
        DaoGrupo miDaoGrupo = new DaoGrupo();
        modeloTablaGrupos.setNumRows(0);

        arrGrup = miDaoGrupo.consultar(ordencito);
        arrGrup.forEach((grupo) -> {
            Object fila[] = new Object[4];

            fila[0] = grupo.getIdGrupo();
            fila[1] = grupo.getIdGrupoLaboratorio().getNombreLaboratorio();
            fila[2] = editarIcono;
            fila[3] = borrarIcono;

            modeloTablaGrupos.addRow(fila);
        });
//
        tabGrupo.getColumnModel().getColumn(0).setPreferredWidth(50);
        tabGrupo.getColumnModel().getColumn(1).setPreferredWidth(100);

    }

    private void cargarLaboratorios() {
        modeloCombo.removeAllElements();
        List<GrupoLaboratorio> arrGrupoLab;
        Integer indice = 0;
        DaoGrupoLaboratorio miDao = new DaoGrupoLaboratorio();
        arrGrupoLab = miDao.consultar("");

        codigosLaboratorios.put(0, 0);

        modeloCombo.addElement("Seleccione el Laboratorio");

        for (GrupoLaboratorio miGrupito : arrGrupoLab) {
            indice++;
            codigosLaboratorios.put(indice, miGrupito.getIdGrupoLaboratorio());
            modeloCombo.addElement(miGrupito.getNombreLaboratorio()); //OJO
        }
    }

    private void cargarEstudiante(String ordencito) {
        List<Estudiante> arrEstudiante;
        DaoEstudiante miDaoEstudiante = new DaoEstudiante();

        modeloTablaEstudiante.setNumRows(0);

        arrEstudiante = miDaoEstudiante.consultar(ordencito);

        arrEstudiante.forEach((Estudiante) -> {
            Object filita[] = new Object[4];

            filita[0] = Estudiante.getIdEstudiante();
            filita[1] = Estudiante.getNomEstudiante();
            filita[2] = Estudiante.getApellidoEstudiante();
            filita[3] = agregarIcon;

            modeloTablaEstudiante.addRow(filita);
        });

    }

    private void cargarEstudianteEditar(Integer idGrupo) {
        //CARGA LOS ESTUDIANTES EN LA TABLA SEGUN EL GRUPO
        modelotablaEstuAgregado.setRowCount(0);
        List<Estudiante> arrEstudiantes;
        DaoEstudiante miDaoEstudiante = new DaoEstudiante();

        modeloTablaEstudiante.setNumRows(0);

        //BUSCA A LOS ESTUDIANTES SEGUN EL GRUPO
        arrEstudiantes = miDaoEstudiante.consultarNombresPorGrupo(idGrupo);

        arrEstudiantes.forEach((estudiante) -> {
            Object filita[] = new Object[4];

            filita[0] = estudiante.getIdEstudiante();
            filita[1] = estudiante.getNomEstudiante();
            filita[2] = estudiante.getApellidoEstudiante();
            filita[3] = borrarIcono;

            modelotablaEstuAgregado.addRow(filita);
        });
    }

    private void buscarEstudiante(String dato, String campo) {
        //NO CAMBIAR LOS PARÁMETROS DE ENTRADA
        List<Estudiante> estudiantesEncontrados;
        DaoEstudiante daoEstudiante = new DaoEstudiante();

        modeloTablaEstudiante.setNumRows(0);

        estudiantesEncontrados = daoEstudiante.buscarDato(dato, campo);
        estudiantesEncontrados.forEach((estudiante) -> {
            Object fila[] = new Object[4];

            fila[0] = estudiante.getIdEstudiante();
            fila[1] = estudiante.getNomEstudiante();
            fila[2] = estudiante.getApellidoEstudiante();
            fila[3] = agregarIcon;

            modeloTablaEstudiante.addRow(fila);
        });
    }

    private Integer buscarEstudianteRegistrar(String dato, String campo) {
        List<Estudiante> arrayEstudiante;
        DaoEstudiante dao = new DaoEstudiante();

        arrayEstudiante = dao.buscarDato(dato, campo);
        for (Estudiante estudiante : arrayEstudiante) {
            // Si el nombre del estudiante coincide con el dato buscado
            if (estudiante.getNomEstudiante().equals(dato)) {
                // Devolvemos el código del estudiante

                System.out.println("nom: " + estudiante.getNomEstudiante());
                System.out.println("codEs: " + estudiante.getIdEstudiante());
                return estudiante.getIdEstudiante();
            }
        }

        return null;
    }

    private void registrarEstudiante() {

        DefaultTableModel modelTablaEstudianteSeleccionado = (DefaultTableModel) tablaEstudianteAgregado.getModel();

        DaoGrupo dao = new DaoGrupo();
        codGrupo = dao.buscarUltimoIdGrupo();

        for (int i = 0; i < modelTablaEstudianteSeleccionado.getRowCount(); i++) {
            String nombre = (String) modelTablaEstudianteSeleccionado.getValueAt(i, 1);
            Integer codEstudiante = buscarEstudianteRegistrar(nombre, "nom_estudiante");

            DaoEstudiante daoEstudiante = new DaoEstudiante();
            Estudiante objEstudiante = new Estudiante();
            objEstudiante.setIdGrupo(codGrupo);
            objEstudiante.setIdEstudiante(codEstudiante);

            daoEstudiante.actualizarGrupo(objEstudiante);

        }
    }

    private void eliminarEstudiante() {
        DefaultTableModel modelTablaEstudianteSeleccionado = (DefaultTableModel) tablaEstudianteAgregado.getModel();

        for (int i = 0; i < modelTablaEstudianteSeleccionado.getRowCount(); i++) {
            String nombre = (String) modelTablaEstudianteSeleccionado.getValueAt(i, 1);
            Integer codEstudiante = buscarEstudianteRegistrar(nombre, "nom_estudiante");

            if (codEstudiante != null) {
                DaoEstudiante daoEstudiante = new DaoEstudiante();
                Estudiante objEstudiante = new Estudiante();
                //SE ESTABLECE EL ID GRUPO COMO VACIO
                objEstudiante.setIdGrupo(null);
                objEstudiante.setIdEstudiante(codEstudiante);

                daoEstudiante.actualizarGrupo(objEstudiante);
            }
        }

        modelotablaEstuAgregado.setRowCount(0);
    }

    // Método para registrar estudiantes
    private void registrarEstudiantesEditar(List<Integer> ids, int codGrupo) {
        DaoEstudiante daoEstudiante = new DaoEstudiante();
        for (Integer id : ids) {
            Estudiante objEstudiante = new Estudiante();
            objEstudiante.setIdGrupo(codGrupo);
            objEstudiante.setIdEstudiante(id);
            daoEstudiante.actualizarGrupo(objEstudiante);
        }
    }

    // Método para eliminar estudiantes
    private void eliminarEstudiantesEditar(List<Integer> ids) {
        DaoEstudiante daoEstudiante = new DaoEstudiante();
        for (Integer id : ids) {
            Estudiante objEstudiante = new Estudiante();
            objEstudiante.setIdGrupo(null); // SE ESTABLECE EL ID GRUPO COMO VACIO
            objEstudiante.setIdEstudiante(id);
            daoEstudiante.actualizarGrupo(objEstudiante);
        }
    }

    private Boolean siElimino(Integer codigoEliminar) {
        int opcion;
        Boolean bandera = false;
        String textoBotones[] = {"Aceptar", "Cancelar"};

        DaoGrupo miDao = new DaoGrupo();
        Grupo objGrupo = miDao.buscar(codigoEliminar);

        opcion = JOptionPane.showOptionDialog(this,
                "¿Desea eliminar el grupo " + objGrupo.getIdGrupo() + "?", "Aviso",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE, null,
                textoBotones, textoBotones[1]);

        if (opcion == JOptionPane.YES_OPTION) {
            bandera = true;

        }
        return bandera;
    }

    private void agregarEstudiante() {
        int filaSeleccionada = tablaEstudiante.getSelectedRow();

        if (filaSeleccionada == -1) {
            return;
        }

        DefaultTableModel modelTablaEstudiante = (DefaultTableModel) tablaEstudiante.getModel();
        DefaultTableModel modelTablaEstudianteSeleccionado = (DefaultTableModel) tablaEstudianteAgregado.getModel();

        boolean exists = false;

        // Verificar si el estudiante ya está en la tabla de estudiantes agregados
        for (int i = 0; i < modelTablaEstudianteSeleccionado.getRowCount(); i++) {
            if (modelTablaEstudianteSeleccionado.getValueAt(i, 0).equals(modelTablaEstudiante.getValueAt(filaSeleccionada, 0))) {
                exists = true;
                JOptionPane.showMessageDialog(jpContainer, "El estudiante ya ha sido agregado", "Información", JOptionPane.INFORMATION_MESSAGE);
                break;
            }
        }

        if (!exists) {
            Object[] datosFila = new Object[modelTablaEstudiante.getColumnCount()];

            for (int i = 0; i < datosFila.length; i++) {
                datosFila[i] = modelTablaEstudiante.getValueAt(filaSeleccionada, i);
            }

            // Añadir el ícono en la última columna
            datosFila[datosFila.length - 1] = borrarIcono;

            // Añadir la fila a la tabla de estudiantes agregados
            modelTablaEstudianteSeleccionado.addRow(datosFila);
            // Remover la fila de la tabla original
            modelTablaEstudiante.removeRow(filaSeleccionada);
        }
    }

    private void removerEstudiante() {
        int filaSeleccionada = tablaEstudianteAgregado.getSelectedRow();

        if (filaSeleccionada == -1) {
            return;
        }

        DefaultTableModel modelTablaEstudiante = (DefaultTableModel) tablaEstudiante.getModel();
        DefaultTableModel modelTablaEstudianteSeleccionado = (DefaultTableModel) tablaEstudianteAgregado.getModel();

        boolean exists = false;

        // Verificar si el estudiante ya está en la tabla original
        for (int i = 0; i < modelTablaEstudiante.getRowCount(); i++) {
            if (modelTablaEstudiante.getValueAt(i, 0).equals(modelTablaEstudianteSeleccionado.getValueAt(filaSeleccionada, 0))) {
                exists = true;
                break;
            }
        }

        if (exists) {
            // Si el estudiante está en la tabla original, remover la fila de la segunda tabla
            modelTablaEstudianteSeleccionado.removeRow(filaSeleccionada);
        } else {
            // Si el estudiante no está en la tabla original, devolverlo a la primera tabla
            Object[] datosFila = new Object[modelTablaEstudianteSeleccionado.getColumnCount()];

            for (int i = 0; i < datosFila.length; i++) {
                datosFila[i] = modelTablaEstudianteSeleccionado.getValueAt(filaSeleccionada, i);
            }

            datosFila[datosFila.length - 1] = agregarIcon; // Añadir el ícono en la última columna

            modelTablaEstudiante.addRow(datosFila);
            modelTablaEstudianteSeleccionado.removeRow(filaSeleccionada);
        }
    }

    private void cargarLabBase() {
        modeloCombo.removeAllElements();
        List<GrupoLaboratorio> arrLab;
        int indice = 1;

        int filaSeleccionada = tabGrupo.getSelectedRow();
        String nomLab = modeloTablaGrupos.getValueAt(filaSeleccionada, 1).toString();

        DaoGrupoLaboratorio miDao = new DaoGrupoLaboratorio();
        arrLab = miDao.consultar("");
        codigosLaboratorios.put(0, 0);
        modeloCombo.addElement("Seleccione el laboratorio");

        for (GrupoLaboratorio miLab : arrLab) {
            losCodigosLab.put(indice, miLab.getIdGrupoLaboratorio());
            modeloCombo.addElement(miLab.getNombreLaboratorio());

            if (Objects.equals(nomLab, miLab.getNombreLaboratorio())) {
                indiceLab = indice;
                System.out.println("indice: " + indice);
                System.out.println("indiceLab: " + indiceLab);
                System.out.println("cat: " + miLab.getNombreLaboratorio());
            }
            indice++;

        }
        cmbLaboratorio.setSelectedIndex(indiceLab);
    }

    //DISEÑO 
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

        jpBuscar.setBounds(widthContainer - 325, 50, (int) (widthContainer * 0.18), (int) (heightContainer * 0.045));
        jpBuscarEstudiantes.setBounds(155, 150, (int) (widthContainer * 0.18), (int) (heightContainer * 0.045));
        cmbSort.setBounds(widthContainer - 600, 50, (int) (widthContainer * 0.18), (int) (heightContainer * 0.045));

        jpLaboratorio.setBounds(890, 180, (int) (width * 0.25), (int) (height * 0.09));
        jsEstudiantes.setBounds(20, 200, (int) (width * 0.25), (int) (height * 0.29));
        jsEstudianteAgregado.setBounds(450, 200, (int) (width * 0.25), (int) (height * 0.29));

        btnAnadir.setBounds(950, 300, (int) (width * 0.1), (int) (height * 0.052));

        jSeparator1.setBounds(35, 470, (int) (width * 0.6), (int) (height * 0.052));
        btnDescargar.setBounds(1000, 450, (int) (width * 0.15), (int) (height * 0.052));
        jScrollPane1.setBounds(20, 510, (int) (width * 0.86), (int) (height * 0.38));

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

        PlaceHolder buscar = new PlaceHolder("Buscar...", cajaBuscar);
        PlaceHolder estudiante = new PlaceHolder("Buscar Estudiante...", cajaBuscarEstudiantes);

        //DISEÑO DE LA TABLA 
        JTableHeader headerGrupo = tabGrupo.getTableHeader();
        headerGrupo.setDefaultRenderer(new CustomHeaderRenderer());

        JTableHeader headerEstu = tablaEstudiante.getTableHeader();
        headerEstu.setDefaultRenderer(new CustomHeaderRenderer());

        JTableHeader headerEstudAgre = tablaEstudianteAgregado.getTableHeader();
        headerEstudAgre.setDefaultRenderer(new CustomHeaderRenderer());
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

    public static List<Integer> obtenerIDTablaEstudiante(JTable tabla) {
        List<Integer> primeraColumna = new ArrayList<>();
        for (int i = 0; i < tabla.getRowCount(); i++) {
            String valor = tabla.getValueAt(i, 0).toString();
            primeraColumna.add(Integer.valueOf(valor));
        }

        return primeraColumna;
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
        jpBuscar = new javax.swing.JPanel();
        cajaBuscar = new javax.swing.JTextField();
        lblImgSearch = new javax.swing.JLabel();
        cmbSort = new javax.swing.JComboBox<>();
        lblTitulo = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabGrupo = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jpLaboratorio = new javax.swing.JPanel();
        lblLaboratorio = new javax.swing.JLabel();
        cmbLaboratorio = new javax.swing.JComboBox<>();
        btnAnadir = new javax.swing.JButton();
        btnDescargar = new javax.swing.JButton();
        jsEstudiantes = new javax.swing.JScrollPane();
        tablaEstudiante = new javax.swing.JTable();
        jsEstudianteAgregado = new javax.swing.JScrollPane();
        tablaEstudianteAgregado = new javax.swing.JTable();
        jpBuscarEstudiantes = new javax.swing.JPanel();
        cajaBuscarEstudiantes = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);

        jpLateral.setBackground(new java.awt.Color(26, 49, 76));
        jpLateral.setLayout(null);

        jpLogo.setBackground(new java.awt.Color(26, 49, 76));

        lblLogo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

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
        jpHome.setBounds(10, 170, 90, 60);

        getContentPane().add(jpLateral);
        jpLateral.setBounds(0, 0, 110, 690);

        jpContainer.setBackground(new java.awt.Color(232, 236, 239));
        jpContainer.setLayout(null);

        jpBuscar.setBackground(new java.awt.Color(255, 255, 255));

        cajaBuscar.setFont(new java.awt.Font("Montserrat", 1, 18)); // NOI18N
        cajaBuscar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        cajaBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cajaBuscarActionPerformed(evt);
            }
        });
        cajaBuscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cajaBuscarKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                cajaBuscarKeyReleased(evt);
            }
        });

        lblImgSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                lblImgSearchKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jpBuscarLayout = new javax.swing.GroupLayout(jpBuscar);
        jpBuscar.setLayout(jpBuscarLayout);
        jpBuscarLayout.setHorizontalGroup(
            jpBuscarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpBuscarLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblImgSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cajaBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jpBuscarLayout.setVerticalGroup(
            jpBuscarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpBuscarLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpBuscarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cajaBuscar)
                    .addComponent(lblImgSearch, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jpContainer.add(jpBuscar);
        jpBuscar.setBounds(615, 34, 239, 37);

        cmbSort.setFont(new java.awt.Font("Montserrat", 1, 18)); // NOI18N
        cmbSort.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Ordenar...", "Id grupo", "Id laboratorio", "Nombre laboratorio", "Nombre estudiante" }));
        cmbSort.setBorder(null);
        cmbSort.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSortActionPerformed(evt);
            }
        });
        jpContainer.add(cmbSort);
        cmbSort.setBounds(403, 34, 206, 35);

        lblTitulo.setFont(new java.awt.Font("Montserrat", 1, 60)); // NOI18N
        lblTitulo.setText("Grupos");
        jpContainer.add(lblTitulo);
        lblTitulo.setBounds(10, 30, 390, 75);

        jSeparator1.setForeground(new java.awt.Color(37, 206, 203));
        jpContainer.add(jSeparator1);
        jSeparator1.setBounds(10, 430, 500, 10);

        tabGrupo.setFont(new java.awt.Font("Montserrat SemiBold", 0, 18)); // NOI18N
        tabGrupo.setModel(new javax.swing.table.DefaultTableModel(
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
        tabGrupo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        tabGrupo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabGrupoMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tabGrupo);

        jpContainer.add(jScrollPane1);
        jScrollPane1.setBounds(10, 460, 901, 205);

        jLabel2.setFont(new java.awt.Font("Montserrat", 1, 18)); // NOI18N
        jLabel2.setText("Datos Generales:");
        jpContainer.add(jLabel2);
        jLabel2.setBounds(30, 110, 170, 23);

        jpLaboratorio.setBackground(new java.awt.Color(255, 255, 255));

        lblLaboratorio.setFont(new java.awt.Font("Montserrat SemiBold", 0, 14)); // NOI18N
        lblLaboratorio.setForeground(new java.awt.Color(98, 106, 109));
        lblLaboratorio.setText("Laboratorio:");

        cmbLaboratorio.setFont(new java.awt.Font("Montserrat", 1, 18)); // NOI18N
        cmbLaboratorio.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccionar laboratorio" }));
        cmbLaboratorio.setBorder(null);
        cmbLaboratorio.setName(""); // NOI18N
        cmbLaboratorio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbLaboratorioActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpLaboratorioLayout = new javax.swing.GroupLayout(jpLaboratorio);
        jpLaboratorio.setLayout(jpLaboratorioLayout);
        jpLaboratorioLayout.setHorizontalGroup(
            jpLaboratorioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpLaboratorioLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(lblLaboratorio)
                .addContainerGap(152, Short.MAX_VALUE))
            .addGroup(jpLaboratorioLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cmbLaboratorio, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jpLaboratorioLayout.setVerticalGroup(
            jpLaboratorioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpLaboratorioLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblLaboratorio)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbLaboratorio, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE)
                .addContainerGap())
        );

        jpContainer.add(jpLaboratorio);
        jpLaboratorio.setBounds(660, 150, 250, 70);

        btnAnadir.setBackground(new java.awt.Color(37, 206, 203));
        btnAnadir.setFont(new java.awt.Font("Montserrat", 1, 18)); // NOI18N
        btnAnadir.setForeground(new java.awt.Color(255, 255, 255));
        btnAnadir.setText("Añadir");
        btnAnadir.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAnadir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnadirActionPerformed(evt);
            }
        });
        jpContainer.add(btnAnadir);
        btnAnadir.setBounds(680, 250, 100, 30);

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
        btnDescargar.setBounds(690, 420, 210, 30);

        tablaEstudiante.setFont(new java.awt.Font("Montserrat SemiBold", 0, 14)); // NOI18N
        tablaEstudiante.setModel(new javax.swing.table.DefaultTableModel(
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
        tablaEstudiante.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        tablaEstudiante.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaEstudianteMouseClicked(evt);
            }
        });
        jsEstudiantes.setViewportView(tablaEstudiante);

        jpContainer.add(jsEstudiantes);
        jsEstudiantes.setBounds(10, 200, 290, 210);

        tablaEstudianteAgregado.setFont(new java.awt.Font("Montserrat SemiBold", 0, 14)); // NOI18N
        tablaEstudianteAgregado.setModel(new javax.swing.table.DefaultTableModel(
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
        tablaEstudianteAgregado.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        tablaEstudianteAgregado.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaEstudianteAgregadoMouseClicked(evt);
            }
        });
        jsEstudianteAgregado.setViewportView(tablaEstudianteAgregado);

        jpContainer.add(jsEstudianteAgregado);
        jsEstudianteAgregado.setBounds(320, 200, 300, 210);

        jpBuscarEstudiantes.setBackground(new java.awt.Color(255, 255, 255));

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

        javax.swing.GroupLayout jpBuscarEstudiantesLayout = new javax.swing.GroupLayout(jpBuscarEstudiantes);
        jpBuscarEstudiantes.setLayout(jpBuscarEstudiantesLayout);
        jpBuscarEstudiantesLayout.setHorizontalGroup(
            jpBuscarEstudiantesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpBuscarEstudiantesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cajaBuscarEstudiantes, javax.swing.GroupLayout.DEFAULT_SIZE, 188, Short.MAX_VALUE)
                .addContainerGap())
        );
        jpBuscarEstudiantesLayout.setVerticalGroup(
            jpBuscarEstudiantesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpBuscarEstudiantesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cajaBuscarEstudiantes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jpContainer.add(jpBuscarEstudiantes);
        jpBuscarEstudiantes.setBounds(189, 150, 200, 35);

        getContentPane().add(jpContainer);
        jpContainer.setBounds(110, 0, 930, 680);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnDescargarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDescargarActionPerformed
        String rutaAplicacion = System.getProperty("user.dir");
        String nombreArchivoSalida = "Elementos.ramon";

        DaoGrupo miDao = new DaoGrupo();

        String ruta = miDao.loVoyAGrabarEnUnArchivoEsPeroYa(modeloTablaGrupos, rutaAplicacion, nombreArchivoSalida);

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

    private void cmbSortActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSortActionPerformed
//        Integer indiceSort;
//
//        indiceSort = cmbSort.getSelectedIndex();
//
//        switch (indiceSort) {
//            case 1 ->
//                cargarGrupo("g.id_grupo");
//            case 2 ->
//                cargarGrupo("gl.id_grupo_laboratorio");
//            case 3 ->
//                cargarGrupo("gl.nombre_laboratorio");
//        }
    }//GEN-LAST:event_cmbSortActionPerformed

    private void tabGrupoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabGrupoMouseClicked
        columnaSeleccionada = tabGrupo.getSelectedColumn();

        //OBTIENE EL COD DEL GRUPO SELECCIONADO
        int filaSeleccionada = tabGrupo.getSelectedRow();
        String codTexto = modeloTablaGrupos.getValueAt(filaSeleccionada, 0).toString();
        codGrupo = Integer.valueOf(codTexto);

        editarActivado();
        if (columnaSeleccionada != 2) {
            //CARGA A LOS ESTUDIANTES SEGUN EL GRUPO SELECCIONADO
            cargarEstudianteEditar(codGrupo);
            cargarEstudiante("");
            cargarLabBase();
        }

        //GUARDA LOS ID DE CADA ESTUDIANTE QUE ESTE ASOCIADO AL GRUPO
        DaoEstudiante daoEstudiante = new DaoEstudiante();
        List<Integer> listaDeIdsAntes = new ArrayList<>(daoEstudiante.consultarIdsPorGrupo(codGrupo));
        System.out.println("antes: " + listaDeIdsAntes);

        // EDITAR
        if (columnaSeleccionada == 2) {
            filaSeleccionada = tabGrupo.getSelectedRow();

            String codigoTexto = modeloTablaGrupos.getValueAt(filaSeleccionada, 0).toString();
            idGrupo = Integer.valueOf(codigoTexto);

            DaoGrupo miDao = new DaoGrupo();
            Grupo objGrupo = miDao.buscar(idGrupo);

            List<Integer> listaDeIdsDespues = new ArrayList<>(obtenerIDTablaEstudiante(tablaEstudianteAgregado));
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

            // Imprimir resultados
            System.out.println("IDs eliminados: " + listaEliminados);
            System.out.println("IDs añadidos: " + listaAñadidos);

            // Imprimir resultados
            System.out.println("IDs eliminados: " + eliminados);
            System.out.println("IDs añadidos: " + añadidos);
            System.out.println("IDs que permanecen iguales: " + iguales);

            if (modelotablaEstuAgregado.getRowCount() == 0) {
                JOptionPane.showMessageDialog(jpContainer, "Agregue estudiantes o elimine el grupo",
                        "Información", JOptionPane.INFORMATION_MESSAGE);
                cargarEstudianteEditar(idGrupo);
                cargarEstudiante("");
            } else {

                // Procesar los IDs eliminados y añadidos
                eliminarEstudiantesEditar(listaEliminados);
                registrarEstudiantesEditar(listaAñadidos, idGrupo);

                JOptionPane.showMessageDialog(jpContainer, "Actualización exitosa",
                        "Información", JOptionPane.INFORMATION_MESSAGE);
            }

        }

        if (columnaSeleccionada == 3) {
            filaSeleccionada = tabGrupo.getSelectedRow();

            String codigoTexto = modeloTablaGrupos.getValueAt(filaSeleccionada, 0).toString();
            idGrupo = Integer.valueOf(codigoTexto);

            DaoGrupo miDao = new DaoGrupo();
            Grupo objGrupo = miDao.buscar(idGrupo);

            if (objGrupo == null) {
                JOptionPane.showMessageDialog(jpContainer, "No se encontró el grupo",
                        "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                if (siElimino(idGrupo)) {
                    eliminarEstudiante();
                    DaoGrupo miDaoElim = new DaoGrupo();
                    if (miDaoElim.eliminar(idGrupo)) {
                        cargarGrupos("");
                        limpiarCajas();
                        JOptionPane.showMessageDialog(jpContainer, "Eliminación exitosa",
                                "Información", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(jpContainer, "No se pudo eliminar",
                                "Advertencia", JOptionPane.WARNING_MESSAGE);
                    }
                }
            }
        }


    }//GEN-LAST:event_tabGrupoMouseClicked

    private void lblImgSearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_lblImgSearchKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_lblImgSearchKeyReleased

    private void cajaBuscarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cajaBuscarKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_cajaBuscarKeyPressed

    private void cajaBuscarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cajaBuscarKeyReleased
        indiceBuscar = cmbSort.getSelectedIndex();
        String campoSelecccionado = campoBuscar(indiceBuscar);
        buscarDato("%" + cajaBuscar.getText().toLowerCase() + "%", campoSelecccionado);
    }//GEN-LAST:event_cajaBuscarKeyReleased

    private void btnAnadirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnadirActionPerformed
        if (estaTodoBien()) {
            Integer indiceSeleccionado, codSeleccionado;

            indiceSeleccionado = cmbLaboratorio.getSelectedIndex();
            codSeleccionado = codigosLaboratorios.get(indiceSeleccionado);

            GrupoLaboratorio objLaboratorio = new GrupoLaboratorio(codSeleccionado, "", "");

            Grupo objGrupo = new Grupo();
            objGrupo.setIdGrupoLaboratorio(objLaboratorio);

            DaoGrupo miDao = new DaoGrupo();
            if (miDao.registrar(objGrupo)) {
                JOptionPane.showMessageDialog(jpContainer, "¡ Registro exitoso !", "INFORMACION", JOptionPane.INFORMATION_MESSAGE);
                registrarEstudiante();
                cargarGrupos("");
                limpiarCajas();
            } else {
                JOptionPane.showMessageDialog(jpContainer, "Error en registro", "ERROR", JOptionPane.ERROR_MESSAGE);
            }

        }
    }//GEN-LAST:event_btnAnadirActionPerformed

    private void cmbLaboratorioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbLaboratorioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbLaboratorioActionPerformed

    private void cajaBuscarEstudiantesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cajaBuscarEstudiantesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cajaBuscarEstudiantesActionPerformed

    private void cajaBuscarEstudiantesKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cajaBuscarEstudiantesKeyReleased
        buscarEstudiante("%" + cajaBuscarEstudiantes.getText().toLowerCase() + "%", "nom_estudiante");
    }//GEN-LAST:event_cajaBuscarEstudiantesKeyReleased

    private void tablaEstudianteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaEstudianteMouseClicked
        columnaSeleccionada = tablaEstudiante.getSelectedColumn();
        if (columnaSeleccionada == 3) {
            agregarEstudiante();
        }
    }//GEN-LAST:event_tablaEstudianteMouseClicked

    private void tablaEstudianteAgregadoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaEstudianteAgregadoMouseClicked
        columnaSeleccionada = tablaEstudianteAgregado.getSelectedColumn();
        if (columnaSeleccionada == 3) {
            removerEstudiante();
        }
    }//GEN-LAST:event_tablaEstudianteAgregadoMouseClicked

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
            java.util.logging.Logger.getLogger(FrmGrupos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            FrmGrupos dialog = new FrmGrupos();
            dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent e) {
                    System.exit(0);
                }
            });
            dialog.setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAnadir;
    private javax.swing.JButton btnDescargar;
    private javax.swing.JTextField cajaBuscar;
    private javax.swing.JTextField cajaBuscarEstudiantes;
    private javax.swing.JComboBox<String> cmbLaboratorio;
    private javax.swing.JComboBox<String> cmbSort;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JPanel jpBuscar;
    private javax.swing.JPanel jpBuscarEstudiantes;
    private javax.swing.JPanel jpContainer;
    private javax.swing.JPanel jpHome;
    private javax.swing.JPanel jpLaboratorio;
    private javax.swing.JPanel jpLateral;
    private javax.swing.JPanel jpLogo;
    private javax.swing.JScrollPane jsEstudianteAgregado;
    private javax.swing.JScrollPane jsEstudiantes;
    private javax.swing.JLabel lblHome;
    private javax.swing.JLabel lblImgSearch;
    private javax.swing.JLabel lblLaboratorio;
    private javax.swing.JLabel lblLogo;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JTable tabGrupo;
    private javax.swing.JTable tablaEstudiante;
    private javax.swing.JTable tablaEstudianteAgregado;
    // End of variables declaration//GEN-END:variables
}
