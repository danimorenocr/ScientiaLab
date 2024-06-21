package com.scientia.forms;

import com.formdev.flatlaf.FlatLightLaf;
import com.scientia.daos.DaoConsumible;
import com.scientia.daos.DaoElemento;
import com.scientia.daos.DaoEquipo;
import com.scientia.daos.DaoGrupo;
import com.scientia.daos.DaoPrestamo;
import com.scientia.daos.DaoPrestamoItem;
import com.scientia.entidades.Consumible;
import com.scientia.entidades.Elemento;
import com.scientia.entidades.Equipo;
import com.scientia.entidades.Grupo;
import com.scientia.entidades.GrupoLaboratorio;
import com.scientia.entidades.Prestamo;
import com.scientia.entidades.PrestamoItem;
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
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import javax.swing.JFrame;
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
public class FrmPrestamosEditar extends javax.swing.JFrame {

    String nomAgreg = "/com/scientia/icons/agregar.png";
    String rutaConAgregar = this.getClass().getResource(nomAgreg).getPath();
    ImageIcon agregarIcon = new ImageIcon(rutaConAgregar);

    String nomEliminar = "/com/scientia/icons/borrar.png";
    String rutaIconEliminar = this.getClass().getResource(nomEliminar).getPath();
    ImageIcon borrarIcono = new ImageIcon(rutaIconEliminar);

    private Map<Integer, Integer> losCodigosGrupo = new HashMap<>();
    private DefaultComboBoxModel modeloCombo = new DefaultComboBoxModel();

    private Integer indiceGrupo, columnaSeleccionada;
    private final Prestamo objActualizar;

    private List<Integer> listaInicialConsumible;
    private List<Integer> listaInicialEquipo;
    private List<Integer> listaInicialElemento;

    private String titulosItem[] = {"Código", "Nombre", "Disponibilidad", "Eliminar"};
    private DefaultTableModel modeloTablaItem = new DefaultTableModel(titulosItem, 0) {

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

    private String titulosItemAgregado[] = {"Código", "Nombre", "Disponibilidad", "Eliminar"};
    private DefaultTableModel modeloTablaItemAgregado = new DefaultTableModel(titulosItemAgregado, 0) {

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

    public FrmPrestamosEditar(JFrame parent, boolean modal, Prestamo objExterno) {
        initComponents();
        cmbGrupo.setModel(modeloCombo);
        objActualizar = objExterno;

        //AGREGAR MODELOS
        tablaDisponibles.setModel(modeloTablaItem);
        tablaItemsPrestamo.setModel(modeloTablaItemAgregado);

        //CARGAR ITEMS
        cargarItems("");
        cargarItemsSeleccionados(objExterno.getId_prestamo());

        //HORA Y FECHA
        cargarPrestamos();

        //OBTENER LISTA
        listaInicialElemento = obtenerListas("elemento");
        listaInicialConsumible = obtenerListas("consumible");
        listaInicialEquipo = obtenerListas("equipo");

        System.out.println("listaCon: " + listaInicialConsumible);
        System.out.println("listaEl: " + listaInicialElemento);
        System.out.println("listaEq: " + listaInicialEquipo);

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

    }

    @SuppressWarnings("unchecked")

    private void cargarPrestamos() {
        cargarGrupos();
        fecha.setDate(objActualizar.getFecha_prestamo());

        // Formatear la hora
        SimpleDateFormat horaFormat = new SimpleDateFormat("HH:mm:ss"); // Formato de hora
        String horaStr = horaFormat.format(objActualizar.getHora_prestamo());

        // Establecer la hora en el JTextField
        cajaHora.setText(horaStr);
    }

    public List<Integer> obtenerListas(String tipo) {
        List<Integer> codigos = new ArrayList<>();
        DefaultTableModel modelTablaSeleccionado = (DefaultTableModel) tablaItemsPrestamo.getModel();

        for (int i = 0; i < modelTablaSeleccionado.getRowCount(); i++) {
            String nombre = (String) modelTablaSeleccionado.getValueAt(i, 1);

            Integer codigo = null;
            switch (tipo) {
                case "elemento" ->
                    codigo = buscarDatoElemento(nombre);
                case "equipo" ->
                    codigo = buscarDatoEquipo(nombre);
                case "consumible" ->
                    codigo = buscarDatoConsumible(nombre);
                default -> // Manejo de tipo no válido
                    System.out.println("Tipo de elemento no válido");
            }

            if (codigo != 0) {
                codigos.add(codigo);
            }
        }

        return codigos;
    }

    private Integer buscarDatoElemento(String dato) {
        Integer idElemento;
        DaoElemento daoElemento = new DaoElemento();

        // Buscar en elemento
        idElemento = daoElemento.buscarId(dato);

        System.out.println("idElemento:   " + idElemento);

        return idElemento;
    }

    private Integer buscarDatoConsumible(String dato) {
        Integer idConsumible;
        DaoConsumible daoConsumible = new DaoConsumible();

        // Buscar en consuible
        idConsumible = daoConsumible.buscarId(dato);

        System.out.println("idConsumible:   " + idConsumible);

        return idConsumible;
    }

    private Integer buscarDatoEquipo(String dato) {
        Integer idEquipo;
        DaoEquipo daoEquipo = new DaoEquipo();

        // Buscar en equipos
        idEquipo = daoEquipo.buscarId(dato);

        System.out.println("idEquipo:   " + idEquipo);

        // Si no se encontró en equipos, devolver null
        return idEquipo;
    }

    private void cargarGrupos() {
        modeloCombo.removeAllElements();
        List<Grupo> arrGrupo;
        Integer indice = 0;

        DaoGrupo miDao = new DaoGrupo();
        arrGrupo = miDao.consultar("id_grupo");

        for (Grupo miGrupo : arrGrupo) {

            losCodigosGrupo.put(indice, miGrupo.getIdGrupo());
            modeloCombo.addElement(String.valueOf(miGrupo.getIdGrupo()));

            if (Objects.equals(objActualizar.getId_grupo().getIdGrupo(), miGrupo.getIdGrupo())) {
                indiceGrupo = indice;
                System.out.println("indice: " + indice);
                System.out.println("indiceLab: " + indiceGrupo);
                System.out.println("cat: " + miGrupo.getIdGrupo());
            }
            indice++;
        }
        cmbGrupo.setSelectedIndex(indiceGrupo);
    }

    private void cargarItemsSeleccionados(Integer codPrestamo) {
        // CARGA LOS ACCESORIOS EN LA TABLA SEGUN EL GRUPO
        modeloTablaItemAgregado.setRowCount(0);
        DaoPrestamoItem daoPrestamoItem = new DaoPrestamoItem();
        // BUSCA LOS ITEMS SEGUN EL ID DEL PRESTAMO
        System.out.println("cod prestamo: " + codPrestamo);
        List<PrestamoItem> arrItems = daoPrestamoItem.consultarPorPrestamo(codPrestamo);
        System.out.println("arr: " + arrItems.toString());

        arrItems.forEach((item) -> {
            Object filita[] = new Object[4];

            Integer codElemento = item.getCodElemento().getId();
            Integer codEquipo = item.getCodEquipo().getId_equipo();
            Integer codConsumible = item.getCodConsumible().getIdConsumible();

            if (codElemento != 0) {
                filita[0] = codElemento;
                filita[1] = item.getCodElemento().getNombre();
                filita[2] = item.getCodElemento().getDisponibilidad();
            } else if (codEquipo != 0) {
                filita[0] = codEquipo;
                filita[1] = item.getCodEquipo().getNombre_equipo();
                filita[2] = item.getCodEquipo().getDisponibilidad_equipo();
            } else if (codConsumible != 0) {
                filita[0] = codConsumible;
                filita[1] = item.getCodConsumible().getNombreConsumible();
                filita[2] = item.getCodConsumible().getDisponibilidadConsumible();
            }

            filita[3] = borrarIcono; // Asumo que borrarIcono es un objeto de tipo Icon para eliminar

            modeloTablaItemAgregado.addRow(filita);
        });
    }

    private void cargarItems(String orden) {
        modeloTablaItem.setNumRows(0);

        // Cargar equipos
        List<Equipo> arrEquipos;
        DaoEquipo miDaoEquipo = new DaoEquipo();
        arrEquipos = miDaoEquipo.consultar(orden);
        arrEquipos.forEach((equipo) -> {
            Object fila[] = new Object[4];
            fila[0] = equipo.getId_equipo();
            fila[1] = equipo.getNombre_equipo();
            fila[2] = equipo.getDisponibilidad_equipo() ? "Si" : "No";
            fila[3] = agregarIcon;
            modeloTablaItem.addRow(fila);
        });

        // Cargar elementos
        List<Elemento> arrElementos;
        DaoElemento miDaoElemento = new DaoElemento();
        arrElementos = miDaoElemento.consultar(orden);
        arrElementos.forEach((elemento) -> {
            Object fila[] = new Object[4];
            fila[0] = elemento.getId();
            fila[1] = elemento.getNombre();
            fila[2] = elemento.getDisponibilidad() ? "Si" : "No";
            fila[3] = agregarIcon;
            modeloTablaItem.addRow(fila);
        });

        // Cargar consumibles
        List<Consumible> arrConsumibles;
        DaoConsumible miDaoConsumible = new DaoConsumible();
        arrConsumibles = miDaoConsumible.consultar(orden);
        arrConsumibles.forEach((consumible) -> {
            Object fila[] = new Object[4];
            fila[0] = consumible.getIdConsumible();
            fila[1] = consumible.getNombreConsumible();
            fila[2] = consumible.getDisponibilidadConsumible() ? "Si" : "No";
            fila[3] = agregarIcon;
            modeloTablaItem.addRow(fila);
        });
    }

    private void agregarAccesorio() {
        int filaSeleccionada = tablaDisponibles.getSelectedRow();

        if (filaSeleccionada == -1) {
            return;
        }

        DefaultTableModel modelTabla = (DefaultTableModel) tablaDisponibles.getModel();
        DefaultTableModel modelTablaSeleccionado = (DefaultTableModel) tablaItemsPrestamo.getModel();

        boolean exists = false;

        // Verificar si el estudiante ya está en la tabla de estudiantes agregados
        for (int i = 0; i < modelTablaSeleccionado.getRowCount(); i++) {
            if (modelTablaSeleccionado.getValueAt(i, 1).equals(modelTabla.getValueAt(filaSeleccionada, 1))) {
                exists = true;
                JOptionPane.showMessageDialog(jpContainer, "El item ya ha sido agregado", "Información", JOptionPane.INFORMATION_MESSAGE);
                break;
            }
        }

        if (!exists) {
            Object[] datosFila = new Object[modelTabla.getColumnCount()];

            for (int i = 0; i < datosFila.length; i++) {
                datosFila[i] = modelTabla.getValueAt(filaSeleccionada, i);
            }

            // Añadir el ícono en la última columna
            datosFila[datosFila.length - 1] = borrarIcono;

            // Añadir la fila a la tabla de estudiantes agregados
            modelTablaSeleccionado.addRow(datosFila);
            // Remover la fila de la tabla original
            modelTabla.removeRow(filaSeleccionada);
        }
    }

    private void removerAccesorio() {
        int filaSeleccionada = tablaItemsPrestamo.getSelectedRow();

        if (filaSeleccionada == -1) {
            return;
        }

        DefaultTableModel modelTabla = (DefaultTableModel) tablaDisponibles.getModel();
        DefaultTableModel modelTablaSeleccionado = (DefaultTableModel) tablaItemsPrestamo.getModel();

        boolean exists = false;

        // Verificar si el estudiante ya está en la tabla original
        for (int i = 0; i < modelTabla.getRowCount(); i++) {
            if (modelTabla.getValueAt(i, 1).equals(modelTablaSeleccionado.getValueAt(filaSeleccionada, 1))) {
                exists = true;
                break;
            }
        }

        if (exists) {
            // Si el estudiante está en la tabla original, remover la fila de la segunda tabla
            modelTablaSeleccionado.removeRow(filaSeleccionada);
        } else {
            // Si el estudiante no está en la tabla original, devolverlo a la primera tabla
            Object[] datosFila = new Object[modelTablaSeleccionado.getColumnCount()];

            for (int i = 0; i < datosFila.length; i++) {
                datosFila[i] = modelTablaSeleccionado.getValueAt(filaSeleccionada, i);
            }

            datosFila[datosFila.length - 1] = agregarIcon; // Añadir el ícono en la última columna

            modelTabla.addRow(datosFila);
            modelTablaSeleccionado.removeRow(filaSeleccionada);
        }
    }

    private boolean siElimino(Integer codigo) {
        int opcion;
        Boolean bandera = false;
        String textoBotones[] = {"Aceptar", "Cancelar"};
        DaoEquipo dao = new DaoEquipo();

        Equipo objLab = dao.buscar(codigo);

        opcion = JOptionPane.showOptionDialog(jpContainer, "¿Esta seguro de elimnar el Equipo " + objLab.getNombre_equipo()
                + "?", "Aviso", JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, textoBotones, textoBotones[1]);

        if (opcion == JOptionPane.YES_OPTION) {

            bandera = true;

        }

        return bandera;
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

        jpGrupo.setBounds(30, 150, (int) (width * 0.23), (int) (height * 0.09));
        jpHora.setBounds(415, 150, (int) (width * 0.15), (int) (height * 0.09));
        jpFecha.setBounds(700, 150, (int) (width * 0.25), (int) (height * 0.09));

        lblAcc.setBounds(30, 325, (int) (width * 0.2), (int) (height * 0.03));
        jpBuscarPrestamo.setBounds(230, 320, (int) (width * 0.13), (int) (height * 0.043));
        jsDisponibles.setBounds(30, 370, (int) (width * 0.35), (int) (height * 0.33));

        lblAccSel1.setBounds(650, 325, (int) (width * 0.2), (int) (height * 0.033));
        jsItemsPrestamo.setBounds(650, 370, (int) (width * 0.35), (int) (height * 0.33));

        btnAnadir.setBounds(600, 700, (int) (width * 0.15), (int) (height * 0.052));

        // Revalidar y repintar el contenedor para aplicar los cambios
        this.revalidate();
        this.repaint();
    }

    private boolean estaTodoBien() {
        Integer seleccionado;

        Boolean bandera = true;

        if (fecha.getDate() == null) {
            JOptionPane.showMessageDialog(jpContainer, "Seleccione la fecha del préstamo", "ADVERTENCIA", JOptionPane.WARNING_MESSAGE);
            bandera = false;
        }

        String horaIngresada = cajaHora.getText();
        if (horaIngresada.isEmpty()) {
            JOptionPane.showMessageDialog(jpContainer, "Ingrese la hora del préstamo", "ADVERTENCIA", JOptionPane.WARNING_MESSAGE);
            bandera = false;
        } else {
            try {

                Time hora = Time.valueOf(horaIngresada);
            } catch (IllegalArgumentException e) {

                JOptionPane.showMessageDialog(jpContainer, "Ingrese una hora válida en formato HH:MM:SS", "ADVERTENCIA", JOptionPane.WARNING_MESSAGE);
                bandera = false;
            }
        }

        return bandera;
    }

    private void registrarRompe() {
        List<Integer> listaPostConsumible;
        List<Integer> listaPostEquipo;
        List<Integer> listaPostElemento;

        listaPostElemento = obtenerListas("elemento");
        listaPostConsumible = obtenerListas("consumible");
        listaPostEquipo = obtenerListas("equipo");

        System.out.println("lista PostEl: " + listaPostElemento);
        System.out.println("lista PostCons: " + listaPostConsumible);
        System.out.println("lista PostEq: " + listaPostEquipo);

        Set<Integer> setAntiguoElem = new HashSet<>(listaInicialElemento);
        Set<Integer> setNuevoElem = new HashSet<>(listaPostElemento);

        Set<Integer> setAntiguoConsu = new HashSet<>(listaInicialConsumible);
        Set<Integer> setNuevoConsu = new HashSet<>(listaPostConsumible);

        Set<Integer> setAntiguoEqui = new HashSet<>(listaInicialEquipo);
        Set<Integer> setNuevoEqui = new HashSet<>(listaPostEquipo);

        if (setAntiguoElem.equals(setNuevoElem)) {
            System.out.println("No se han realizado cambios en la lista de IDs.");
            return; // Si no hay cambios, salir del método
        }

        // Encontrar IDs que se han eliminado
        Set<Integer> eliminadosElem = new HashSet<>(setAntiguoElem);
        eliminadosElem.removeAll(setNuevoElem);

        // Encontrar IDs que se han añadido
        Set<Integer> añadidosElem = new HashSet<>(setNuevoElem);
        añadidosElem.removeAll(setAntiguoElem);

        // Encontrar IDs quañadidosEleme permanecen iguales
        Set<Integer> igualesElem = new HashSet<>(setAntiguoElem);
        igualesElem.retainAll(setNuevoElem);

        // Convertir los sets a listas
        List<Integer> listaEliminadosElem = new ArrayList<>(eliminadosElem);
        List<Integer> listaAñadidosElem = new ArrayList<>(añadidosElem);

        // Imprimir resultados
        System.out.println("IDs eliminados: " + listaEliminadosElem);
        System.out.println("IDs añadidos: " + listaAñadidosElem);

        // Imprimir resultados
        System.out.println("IDs eliminados: " + eliminadosElem);
        System.out.println("IDs añadidos: " + añadidosElem);
        System.out.println("IDs que permanecen iguales: " + igualesElem);

        if (setAntiguoConsu.equals(setNuevoConsu)) {
            System.out.println("No se han realizado cambios en la lista de IDs.");
            return; // Si no hay cambios, salir del método
        }

        // Encontrar IDs que se han eliminado
        Set<Integer> eliminadosCons = new HashSet<>(setAntiguoConsu);
        eliminadosCons.removeAll(setNuevoConsu);

        // Encontrar IDs que se han añadido
        Set<Integer> añadidosCons = new HashSet<>(setNuevoConsu);
        añadidosCons.removeAll(setAntiguoConsu);

        // Encontrar IDs quañadidosEleme permanecen iguales
        Set<Integer> igualesCons = new HashSet<>(setAntiguoConsu);
        igualesCons.retainAll(setNuevoConsu);

        // Convertir los sets a listas
        List<Integer> listaEliminadosCons = new ArrayList<>(eliminadosCons);
        List<Integer> listaAñadidosCons = new ArrayList<>(añadidosCons);

        // Imprimir resultados
        System.out.println("IDs eliminados: " + listaEliminadosCons);
        System.out.println("IDs añadidos: " + listaAñadidosCons);

        // Imprimir resultados
        System.out.println("IDs eliminados: " + eliminadosCons);
        System.out.println("IDs añadidos: " + añadidosCons);
        System.out.println("IDs que permanecen iguales: " + igualesCons);

        if (setAntiguoEqui.equals(setNuevoEqui)) {
            System.out.println("No se han realizado cambios en la lista de IDs.");
            return; // Si no hay cambios, salir del método
        }

        // Encontrar IDs que se han eliminado
        Set<Integer> eliminadosEqui = new HashSet<>(setAntiguoEqui);
        eliminadosEqui.removeAll(setNuevoEqui);

        // Encontrar IDs que se han añadido
        Set<Integer> añadidosEqui = new HashSet<>(setNuevoEqui);
        añadidosEqui.removeAll(setAntiguoEqui);

        // Encontrar IDs quañadidosEleme permanecen iguales
        Set<Integer> igualesEqui = new HashSet<>(setAntiguoEqui);
        igualesEqui.retainAll(setNuevoEqui);

        // Convertir los sets a listas
        List<Integer> listaEliminadosEqui = new ArrayList<>(eliminadosEqui);
        List<Integer> listaAñadidosEqui = new ArrayList<>(añadidosEqui);

        // Imprimir resultados
        System.out.println("IDs eliminados: " + listaEliminadosEqui);
        System.out.println("IDs añadidos: " + listaAñadidosEqui);

        // Imprimir resultados
        System.out.println("IDs eliminados: " + eliminadosEqui);
        System.out.println("IDs añadidos: " + añadidosEqui);
        System.out.println("IDs que permanecen iguales: " + igualesEqui);

        DaoPrestamo daoPrestamo = new DaoPrestamo();
        Integer codPrestamo = daoPrestamo.buscarUltimoIdPres();

        System.out.println(
                "IDDD" + codPrestamo);

        DaoPrestamoItem daoPrestamoItem = new DaoPrestamoItem();
        DefaultTableModel modelTablaSeleccionado = (DefaultTableModel) tablaItemsPrestamo.getModel();

        if (modelTablaSeleccionado.getRowCount() == 0) {
            JOptionPane.showMessageDialog(jpContainer, "Agregue elementos o elimine el experimento",
                    "Información", JOptionPane.INFORMATION_MESSAGE);
            cargarItemsSeleccionados(objActualizar.getId_prestamo());
            cargarItems("");
        } else {

            if (!listaEliminadosElem.isEmpty()) {
                eliminarItem(listaEliminadosElem, "id_elemento");
            }
            if (!listaEliminadosCons.isEmpty()) {
                eliminarItem(listaEliminadosCons, "id_consumible");
            }
            if (!listaEliminadosEqui.isEmpty()) {
                eliminarItem(listaEliminadosEqui, "id_equipo");
            }

            if (!listaAñadidosCons.isEmpty()) {
                registrarItemConsu(listaAñadidosCons);
            }
            if (!listaAñadidosElem.isEmpty()) {
                registrarItemElem(listaAñadidosElem);
            }
            if (!listaAñadidosEqui.isEmpty()) {
                registrarItemEquipo(listaAñadidosEqui);
            }

            JOptionPane.showMessageDialog(jpContainer, "Actualización exitosa items",
                    "Información", JOptionPane.INFORMATION_MESSAGE);

        }

    }

    private void eliminarItem(List<Integer> ids, String tipoId) {
        DaoPrestamoItem daoPrestamoItem = new DaoPrestamoItem();
        for (Integer id : ids) {
            daoPrestamoItem.eliminar(id, tipoId);
        }

    }

    private void registrarItemElem(List<Integer> ids) {
        DaoPrestamoItem daoPrestamoItem = new DaoPrestamoItem();
        for (Integer id : ids) {

            Elemento objElemento = new Elemento(null, null, false, "", null, null, null, id, "", "", false, null);
            Equipo objEquipo = new Equipo(id, "", "", "", "", "", "", "", "", false, 0);
            Consumible objConsumible = new Consumible(0, "", "", 0, "", "", false, 0, null);

            Prestamo objPrestamo = new Prestamo(objActualizar.getId_prestamo(), null, null, null);
            PrestamoItem objPrestamoItem = new PrestamoItem();

            objPrestamoItem.setCodElemento(objElemento);
            objPrestamoItem.setCodPrestamo(objPrestamo);
            objPrestamoItem.setCodConsumible(objConsumible);
            objPrestamoItem.setCodEquipo(objEquipo);

            daoPrestamoItem.registrar(objPrestamoItem);
        }
    }

    private void registrarItemConsu(List<Integer> ids) {
        DaoPrestamoItem daoPrestamoItem = new DaoPrestamoItem();
        for (Integer id : ids) {

            Consumible objConsumible = new Consumible(id, "", "", 0, "", "", false, 0, null);
            Equipo objEquipo = new Equipo(0, "", "", "", "", "", "", "", "", false, 0);
            Elemento objElemento = new Elemento(null, null, false, "", null, null, null, 0, "", "", false, null);
            Prestamo objPrestamo = new Prestamo(objActualizar.getId_prestamo(), null, null, null);

            PrestamoItem objPrestamoItem = new PrestamoItem();

            objPrestamoItem.setCodElemento(objElemento);
            objPrestamoItem.setCodPrestamo(objPrestamo);
            objPrestamoItem.setCodConsumible(objConsumible);
            objPrestamoItem.setCodEquipo(objEquipo);

            daoPrestamoItem.registrar(objPrestamoItem);
        }
    }

    private void registrarItemEquipo(List<Integer> ids) {
        DaoPrestamoItem daoPrestamoItem = new DaoPrestamoItem();
        for (Integer id : ids) {

            Equipo objEquipo = new Equipo(id, "", "", "", "", "", "", "", "", false, 0);
            Consumible objConsumible = new Consumible(0, "", "", 0, "", "", false, 0, null);
            Elemento objElemento = new Elemento(null, null, false, "", null, null, null, 0, "", "", false, null);

            Prestamo objPrestamo = new Prestamo(objActualizar.getId_prestamo(), null, null, null);
            PrestamoItem objPrestamoItem = new PrestamoItem();

            objPrestamoItem.setCodElemento(objElemento);
            objPrestamoItem.setCodPrestamo(objPrestamo);
            objPrestamoItem.setCodConsumible(objConsumible);
            objPrestamoItem.setCodEquipo(objEquipo);

            daoPrestamoItem.actualizar(objPrestamoItem);
        }
    }

    private void loadDesign() {
        String nomLogo = "..\\ScientiaLab\\src\\com\\scientia\\icons\\logo.png";
        System.out.println(nomLogo);
        ImageScale.setImageToLabel(lblLogo, nomLogo);
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
            Logger.getLogger(FrmPrincipal.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

        PlaceHolder existencias = new PlaceHolder("0:00", cajaHora);

        //DISEÑO DE LA TABLA 
        JTableHeader headerAcc = tablaDisponibles.getTableHeader();
        headerAcc.setDefaultRenderer(new FrmElementoRegistrar.CustomHeaderRenderer());

        JTableHeader headerAgr = tablaItemsPrestamo.getTableHeader();
        headerAgr.setDefaultRenderer(new FrmElementoRegistrar.CustomHeaderRenderer());

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
        lblAcc = new javax.swing.JLabel();
        jpHora = new javax.swing.JPanel();
        lblHora = new javax.swing.JLabel();
        cajaHora = new javax.swing.JTextField();
        btnAnadir = new javax.swing.JButton();
        jsDisponibles = new javax.swing.JScrollPane();
        tablaDisponibles = new javax.swing.JTable();
        jpBuscarPrestamo = new javax.swing.JPanel();
        cajaBuscarPrestamo = new javax.swing.JTextField();
        jsItemsPrestamo = new javax.swing.JScrollPane();
        tablaItemsPrestamo = new javax.swing.JTable();
        lblDatos = new javax.swing.JLabel();
        lblAccSel1 = new javax.swing.JLabel();
        jpFecha = new javax.swing.JPanel();
        lblFecha = new javax.swing.JLabel();
        fecha = new com.toedter.calendar.JDateChooser();
        jpRegresar = new javax.swing.JPanel();
        lblRegresar = new javax.swing.JLabel();
        jpGrupo = new javax.swing.JPanel();
        lblGrupo = new javax.swing.JLabel();
        cmbGrupo = new javax.swing.JComboBox<>();

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
        lblTitulo.setText("Editar Nuevo Préstamo");
        jpContainer.add(lblTitulo);
        lblTitulo.setBounds(30, 30, 770, 75);

        lblAcc.setFont(new java.awt.Font("Montserrat", 1, 18)); // NOI18N
        lblAcc.setText("Items Disponibles:");
        jpContainer.add(lblAcc);
        lblAcc.setBounds(20, 270, 190, 23);

        jpHora.setBackground(new java.awt.Color(255, 255, 255));

        lblHora.setFont(new java.awt.Font("Montserrat SemiBold", 0, 14)); // NOI18N
        lblHora.setForeground(new java.awt.Color(98, 106, 109));
        lblHora.setText("Hora:");

        cajaHora.setFont(new java.awt.Font("Montserrat SemiBold", 1, 24)); // NOI18N
        cajaHora.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        cajaHora.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cajaHoraActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpHoraLayout = new javax.swing.GroupLayout(jpHora);
        jpHora.setLayout(jpHoraLayout);
        jpHoraLayout.setHorizontalGroup(
            jpHoraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpHoraLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jpHoraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpHoraLayout.createSequentialGroup()
                        .addComponent(lblHora)
                        .addGap(0, 144, Short.MAX_VALUE))
                    .addComponent(cajaHora))
                .addContainerGap())
        );
        jpHoraLayout.setVerticalGroup(
            jpHoraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpHoraLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblHora)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cajaHora, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jpContainer.add(jpHora);
        jpHora.setBounds(550, 160, 200, 70);

        btnAnadir.setBackground(new java.awt.Color(37, 206, 203));
        btnAnadir.setFont(new java.awt.Font("Montserrat", 1, 18)); // NOI18N
        btnAnadir.setForeground(new java.awt.Color(255, 255, 255));
        btnAnadir.setText("Editar Préstamo");
        btnAnadir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnadirActionPerformed(evt);
            }
        });
        jpContainer.add(btnAnadir);
        btnAnadir.setBounds(350, 580, 200, 30);

        tablaDisponibles.setFont(new java.awt.Font("Montserrat SemiBold", 0, 14)); // NOI18N
        tablaDisponibles.setModel(new javax.swing.table.DefaultTableModel(
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
        tablaDisponibles.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        tablaDisponibles.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaDisponiblesMouseClicked(evt);
            }
        });
        jsDisponibles.setViewportView(tablaDisponibles);

        jpContainer.add(jsDisponibles);
        jsDisponibles.setBounds(20, 310, 420, 240);

        jpBuscarPrestamo.setBackground(new java.awt.Color(255, 255, 255));

        cajaBuscarPrestamo.setFont(new java.awt.Font("Montserrat", 1, 18)); // NOI18N
        cajaBuscarPrestamo.setBorder(null);
        cajaBuscarPrestamo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cajaBuscarPrestamoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpBuscarPrestamoLayout = new javax.swing.GroupLayout(jpBuscarPrestamo);
        jpBuscarPrestamo.setLayout(jpBuscarPrestamoLayout);
        jpBuscarPrestamoLayout.setHorizontalGroup(
            jpBuscarPrestamoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpBuscarPrestamoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cajaBuscarPrestamo, javax.swing.GroupLayout.DEFAULT_SIZE, 188, Short.MAX_VALUE)
                .addContainerGap())
        );
        jpBuscarPrestamoLayout.setVerticalGroup(
            jpBuscarPrestamoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpBuscarPrestamoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cajaBuscarPrestamo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jpContainer.add(jpBuscarPrestamo);
        jpBuscarPrestamo.setBounds(240, 260, 200, 35);

        tablaItemsPrestamo.setFont(new java.awt.Font("Montserrat SemiBold", 0, 14)); // NOI18N
        tablaItemsPrestamo.setModel(new javax.swing.table.DefaultTableModel(
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
        tablaItemsPrestamo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        tablaItemsPrestamo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaItemsPrestamoMouseClicked(evt);
            }
        });
        jsItemsPrestamo.setViewportView(tablaItemsPrestamo);

        jpContainer.add(jsItemsPrestamo);
        jsItemsPrestamo.setBounds(480, 310, 430, 240);

        lblDatos.setFont(new java.awt.Font("Montserrat", 1, 18)); // NOI18N
        lblDatos.setText("Datos Generales:");
        jpContainer.add(lblDatos);
        lblDatos.setBounds(30, 110, 170, 23);

        lblAccSel1.setFont(new java.awt.Font("Montserrat", 1, 18)); // NOI18N
        lblAccSel1.setText(" Items Préstamo:");
        jpContainer.add(lblAccSel1);
        lblAccSel1.setBounds(480, 280, 260, 23);

        jpFecha.setBackground(new java.awt.Color(255, 255, 255));

        lblFecha.setFont(new java.awt.Font("Montserrat SemiBold", 0, 14)); // NOI18N
        lblFecha.setForeground(new java.awt.Color(98, 106, 109));
        lblFecha.setText("Fecha:");

        fecha.setFont(new java.awt.Font("Montserrat SemiBold", 0, 18)); // NOI18N

        javax.swing.GroupLayout jpFechaLayout = new javax.swing.GroupLayout(jpFecha);
        jpFecha.setLayout(jpFechaLayout);
        jpFechaLayout.setHorizontalGroup(
            jpFechaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpFechaLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jpFechaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpFechaLayout.createSequentialGroup()
                        .addComponent(lblFecha)
                        .addGap(0, 145, Short.MAX_VALUE))
                    .addComponent(fecha, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jpFechaLayout.setVerticalGroup(
            jpFechaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpFechaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblFecha)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(fecha, javax.swing.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE)
                .addContainerGap())
        );

        jpContainer.add(jpFecha);
        jpFecha.setBounds(300, 160, 210, 71);

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

        jpGrupo.setBackground(new java.awt.Color(255, 255, 255));

        lblGrupo.setFont(new java.awt.Font("Montserrat SemiBold", 0, 14)); // NOI18N
        lblGrupo.setForeground(new java.awt.Color(98, 106, 109));
        lblGrupo.setText("Grupo:");

        cmbGrupo.setFont(new java.awt.Font("Montserrat", 1, 18)); // NOI18N
        cmbGrupo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccione la grupo ..." }));
        cmbGrupo.setBorder(null);

        javax.swing.GroupLayout jpGrupoLayout = new javax.swing.GroupLayout(jpGrupo);
        jpGrupo.setLayout(jpGrupoLayout);
        jpGrupoLayout.setHorizontalGroup(
            jpGrupoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpGrupoLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(lblGrupo)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpGrupoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cmbGrupo, 0, 218, Short.MAX_VALUE)
                .addContainerGap())
        );
        jpGrupoLayout.setVerticalGroup(
            jpGrupoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpGrupoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblGrupo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbGrupo, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE)
                .addContainerGap())
        );

        jpContainer.add(jpGrupo);
        jpGrupo.setBounds(30, 160, 230, 70);

        getContentPane().add(jpContainer);
        jpContainer.setBounds(110, 0, 930, 680);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cajaHoraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cajaHoraActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cajaHoraActionPerformed

    private void lblHomeMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblHomeMouseEntered
        jpHome.setBackground(new Color(26, 49, 76));
        lblHome.setBackground(new Color(26, 49, 76));
    }//GEN-LAST:event_lblHomeMouseEntered

    private void lblHomeMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblHomeMouseExited
        jpHome.setBackground(Color.WHITE);
        lblHome.setBackground(Color.WHITE);
    }//GEN-LAST:event_lblHomeMouseExited

    private void cajaBuscarPrestamoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cajaBuscarPrestamoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cajaBuscarPrestamoActionPerformed

    private void lblRegresarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblRegresarMouseEntered
        jpRegresar.setBackground(new Color(26, 49, 76));
        jpRegresar.setBackground(new Color(26, 49, 76));
    }//GEN-LAST:event_lblRegresarMouseEntered

    private void lblRegresarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblRegresarMouseExited
        jpRegresar.setBackground(Color.WHITE);
        jpRegresar.setBackground(Color.WHITE);
    }//GEN-LAST:event_lblRegresarMouseExited

    private void btnAnadirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnadirActionPerformed
        if (estaTodoBien()) {
            Integer indiceSeleccionado;
            Date fechaPrestamo;
            Time hora;

            indiceSeleccionado = cmbGrupo.getSelectedIndex();

            if (indiceSeleccionado != -1) {
                Integer idGrupo = losCodigosGrupo.get(indiceSeleccionado);

                if (idGrupo != null) {
                    fechaPrestamo = fecha.getDate();
                    hora = Time.valueOf(cajaHora.getText());

                    GrupoLaboratorio objGrupoLaboratorio = new GrupoLaboratorio(idGrupo, "", "");
                    Grupo objGrupo = new Grupo(idGrupo, objGrupoLaboratorio);

                    Prestamo objPrestamo = new Prestamo();
                    objPrestamo.setFecha_prestamo(fechaPrestamo);
                    objPrestamo.setHora_prestamo(hora);
                    objPrestamo.setId_grupo(objGrupo);

                    DaoPrestamo miDao = new DaoPrestamo();
                    if (miDao.registrar(objPrestamo)) {
                        registrarRompe();
                        JOptionPane.showMessageDialog(jpContainer, "Se registró el préstamo", "INFORMACIÓN", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(jpContainer, "Error al registrar el préstamo", "ERROR", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(jpContainer, "Error: idGrupo es null", "ERROR", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(jpContainer, "Seleccione un grupo válido", "ADVERTENCIA", JOptionPane.WARNING_MESSAGE);
            }
        }


    }//GEN-LAST:event_btnAnadirActionPerformed

    private void tablaDisponiblesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaDisponiblesMouseClicked
        columnaSeleccionada = tablaDisponibles.getSelectedColumn();
        if (columnaSeleccionada == 3) {
            agregarAccesorio();
        }
    }//GEN-LAST:event_tablaDisponiblesMouseClicked

    private void tablaItemsPrestamoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaItemsPrestamoMouseClicked
        columnaSeleccionada = tablaItemsPrestamo.getSelectedColumn();
        if (columnaSeleccionada == 3) {
            removerAccesorio();
        }
    }//GEN-LAST:event_tablaItemsPrestamoMouseClicked

    private void lblHomeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblHomeMouseClicked
        FrmPrincipal principal = new FrmPrincipal();
        principal.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_lblHomeMouseClicked

    private void lblRegresarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblRegresarMouseClicked
        FrmPrestamosAdmin prestamosAdmin = new FrmPrestamosAdmin();
        prestamosAdmin.setVisible(true);
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
            java.util.logging.Logger.getLogger(FrmPrestamosEditar.class
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
                new FrmPrestamosEditar(null, true, null).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAnadir;
    private javax.swing.JTextField cajaBuscarPrestamo;
    private javax.swing.JTextField cajaHora;
    private javax.swing.JComboBox<String> cmbGrupo;
    private com.toedter.calendar.JDateChooser fecha;
    private javax.swing.JPanel jpBuscarPrestamo;
    private javax.swing.JPanel jpContainer;
    private javax.swing.JPanel jpFecha;
    private javax.swing.JPanel jpGrupo;
    private javax.swing.JPanel jpHome;
    private javax.swing.JPanel jpHora;
    private javax.swing.JPanel jpLateral;
    private javax.swing.JPanel jpLogo;
    private javax.swing.JPanel jpRegresar;
    private javax.swing.JScrollPane jsDisponibles;
    private javax.swing.JScrollPane jsItemsPrestamo;
    private javax.swing.JLabel lblAcc;
    private javax.swing.JLabel lblAccSel1;
    private javax.swing.JLabel lblDatos;
    private javax.swing.JLabel lblFecha;
    private javax.swing.JLabel lblGrupo;
    private javax.swing.JLabel lblHome;
    private javax.swing.JLabel lblHora;
    private javax.swing.JLabel lblLogo;
    private javax.swing.JLabel lblRegresar;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JTable tablaDisponibles;
    private javax.swing.JTable tablaItemsPrestamo;
    // End of variables declaration//GEN-END:variables
}
