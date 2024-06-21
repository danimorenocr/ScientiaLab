package com.scientia.forms;

import com.formdev.flatlaf.FlatLightLaf;
import com.scientia.daos.DaoElemento;
import com.scientia.daos.DaoEquipo;
import com.scientia.entidades.Equipo;
import com.scientia.utils.ImageScale;
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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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
public class FrmEquiposAdmin extends javax.swing.JFrame {

    private Integer codEquipo = null;

    Integer seleccionarBuscar = 0;

    String nomEliminar = "/com/scientia/icons/borrar.png";
    String rutaIconEliminar = this.getClass().getResource(nomEliminar).getPath();
    ImageIcon borrarIcono = new ImageIcon(rutaIconEliminar);

    String nomEditar = "/com/scientia/icons/editar.png";
    String rutaIconEditar = this.getClass().getResource(nomEditar).getPath();
    ImageIcon editarIcono = new ImageIcon(rutaIconEditar);

    private String titulos[] = {"ID", "Cod", "Nombre", "Modelo", "Serie", "Mantenimiento", "Req Mante", "Estado", "Marca", "Disponibilidad", "Existencia", "Editar", "Eliminar"};

    private DefaultTableModel miModelitoTabla = new DefaultTableModel(titulos, 0) {

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            if (columnIndex == 11 || columnIndex == 12) {
                return ImageIcon.class;
            }
            return Object.class;
        }
    };

    public FrmEquiposAdmin() {
        initComponents();
        tabEquipos.setModel(miModelitoTabla);
        cargarEquipos("");

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
                if (!e.getSource().equals(tabEquipos)) {
                    tabEquipos.clearSelection();
                }
            }
        });

        tabEquipos.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                tabEquipos.requestFocus();
            }
        });

        String nomLogo = "..\\ScientiaLab\\src\\com\\scientia\\icons\\logo.png";
        System.out.println(nomLogo);
        ImageScale.setImageToLabel(lblLogo, nomLogo);

    }

    private void cargarEquipos(String ordencito) {
        List<Equipo> arrEquipos;

        DaoEquipo miDao = new DaoEquipo();
        miModelitoTabla.setNumRows(0);

        arrEquipos = miDao.consultar(ordencito);
        if (arrEquipos != null) {
            arrEquipos.forEach((equipo) -> {
                Object filita[] = new Object[13];

                filita[0] = equipo.getId_equipo();
                filita[1] = equipo.getCod_inventario_equipo();
                filita[2] = equipo.getNombre_equipo();
                filita[3] = equipo.getModelo_equipo();
                filita[4] = equipo.getSerie_equipo();
                filita[5] = equipo.getMantenimiento_equipo();
                filita[6] = equipo.getReq_mante_equipo();
                filita[7] = equipo.getEstado_equipo();
                filita[8] = equipo.getMarca_equipo();

                if (equipo.getDisponibilidad_equipo()) {
                    filita[9] = "Si";
                } else {
                    filita[9] = "No";

                }

                filita[10] = equipo.getExistencias_equipo();
                filita[11] = editarIcono;
                filita[12] = borrarIcono;

                miModelitoTabla.addRow(filita);
            });

            tabEquipos.getColumnModel().getColumn(0).setPreferredWidth(50);
            tabEquipos.getColumnModel().getColumn(1).setPreferredWidth(50);
            tabEquipos.getColumnModel().getColumn(2).setPreferredWidth(50);

//            
        } else {
            System.out.println("La lista de equipos está vacía.");
        }
    }

    private String campoBuscar(int select) {
        // SE ELIGE EN QUE COLUMNA SE QUIERE APLICAR LA BUSQUEDA DE DATO
        // CAMBIAR UNICAMENTE LOS VALORES DEL CAMPO Y AÑADIR MAS CASE
        // SI HAY MAS CAMPOS POR LOS QUE BUSCAR
        // SETEEN LOS CAMPOS DE MANERA MANUAL EN EL MODELO DEL CMB EN VISUALIZAR

        System.out.println("indice: " + select);
        String campo = "";
        switch (select) {
            case 0 -> {
                campo = "nombre_equipo";
            }
            case 1 -> {
                campo = "cod_inventario_equipo";
            }
            case 2 -> {
                campo = "nombre_equipo";
            }
            case 3 -> {
                campo = "modelo_equipo";
            }
            case 4 -> {
                campo = "serie_equipo";
            }
            case 5 -> {
                campo = "mantenimiento_equipo";
            }
            case 6 -> {
                campo = "req_mante_equipo";
            }
            case 7 -> {
                campo = "estado_equipo";
            }
            case 8 -> {
                campo = "marca_equipo";
            }
            case 9 -> {
                campo = "disponibilidad_equipo";
            }
            case 10 -> {
                campo = "existencias_equipo";
            }
            default ->
                throw new AssertionError();
        }
        return campo;
    }

    private void buscarDato(String dato, String campo) {
        //NO CAMBIAR LOS PARAMETROS DE ENTRADA
        List<Equipo> arrayLab;
        DaoEquipo dao = new DaoEquipo();

        miModelitoTabla.setNumRows(0);

        arrayLab = dao.buscarDato(dato, campo);
        if (arrayLab != null) {
            arrayLab.forEach((equipo) -> {
                Object filita[] = new Object[13];

                filita[0] = equipo.getId_equipo();
                filita[1] = equipo.getCod_inventario_equipo();
                filita[2] = equipo.getNombre_equipo();
                filita[3] = equipo.getModelo_equipo();
                filita[4] = equipo.getSerie_equipo();
                filita[5] = equipo.getMantenimiento_equipo();
                filita[6] = equipo.getReq_mante_equipo();
                filita[7] = equipo.getEstado_equipo();
                filita[8] = equipo.getMarca_equipo();
                filita[9] = equipo.getDisponibilidad_equipo();
                filita[10] = equipo.getExistencias_equipo();
                filita[11] = editarIcono;
                filita[12] = borrarIcono;

                miModelitoTabla.addRow(filita);
            });
        } else {
            System.out.println("No se encontraron resultados para " + campo + " con el valor " + dato);
        }
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

        cmbSort.setBounds(widthContainer - 850, 50, (int) (widthContainer * 0.18), (int) (heightContainer * 0.045));
        jpSearch.setBounds(widthContainer - 575, 50, (int) (widthContainer * 0.18), (int) (heightContainer * 0.045));

        btnAnadir.setBounds(widthContainer - 300, 50, (int) (width * 0.15), (int) (height * 0.052));

        btnDescargar.setBounds(1100, 700, (int) (width * 0.15), (int) (height * 0.052));
        jScrollPane1.setBounds(20, 120, (int) (width * 0.86), (int) (height * 0.67));

        // Revalidar y repintar el contenedor para aplicar los cambios
        this.revalidate();
        this.repaint();
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
            Logger.getLogger(FrmPrincipal.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        //DISEÑO DE LA TABLA 
        JTableHeader header = tabEquipos.getTableHeader();
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
        jScrollPane1 = new javax.swing.JScrollPane();
        tabEquipos = new javax.swing.JTable();
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
        jpSearch.setBounds(670, 30, 239, 35);

        cmbSort.setFont(new java.awt.Font("Montserrat", 1, 18)); // NOI18N
        cmbSort.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Ordenar/Buscar por", "ID", "Codigo", "modelo", "Serie", "Mantenimiento", "ReqMante", "Estado", "Nombre", "Marca", "Disponibilidad", "Existencia" }));
        cmbSort.setBorder(null);
        cmbSort.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSortActionPerformed(evt);
            }
        });
        jpContainer.add(cmbSort);
        cmbSort.setBounds(460, 30, 206, 35);

        lblTitulo.setFont(new java.awt.Font("Montserrat", 1, 60)); // NOI18N
        lblTitulo.setText("Equipos");
        jpContainer.add(lblTitulo);
        lblTitulo.setBounds(10, 30, 420, 75);

        tabEquipos.setFont(new java.awt.Font("Montserrat SemiBold", 0, 14)); // NOI18N
        tabEquipos.setModel(new javax.swing.table.DefaultTableModel(
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
        tabEquipos.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        tabEquipos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabEquiposMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tabEquipos);

        jpContainer.add(jScrollPane1);
        jScrollPane1.setBounds(10, 130, 901, 460);

        btnAnadir.setBackground(new java.awt.Color(37, 206, 203));
        btnAnadir.setFont(new java.awt.Font("Montserrat", 1, 18)); // NOI18N
        btnAnadir.setForeground(new java.awt.Color(255, 255, 255));
        btnAnadir.setText("Añadir Equipo");
        btnAnadir.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAnadir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnadirActionPerformed(evt);
            }
        });
        jpContainer.add(btnAnadir);
        btnAnadir.setBounds(710, 90, 180, 30);

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
        btnDescargar.setBounds(700, 610, 210, 30);

        getContentPane().add(jpContainer);
        jpContainer.setBounds(110, 0, 930, 680);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnDescargarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDescargarActionPerformed
        String rutaAplicacion = System.getProperty("user.dir");
        String nombreArchivoSalida = "Elementos.ramon";

        DaoEquipo miDao = new DaoEquipo();

        String ruta = miDao.loVoyAGrabarEnUnArchivoEsPeroYa(miModelitoTabla, rutaAplicacion, nombreArchivoSalida);

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

    private void btnAnadirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnadirActionPerformed
        FrmEquiposRegistrar frameRegistrar = new FrmEquiposRegistrar();
        frameRegistrar.setVisible(true);
        this.setVisible(false);

    }//GEN-LAST:event_btnAnadirActionPerformed

    private void tabEquiposMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabEquiposMouseClicked
        int columnaEliminar = tabEquipos.getSelectedColumn();
        int columnaActualizar = tabEquipos.getSelectedColumn();

        if (columnaEliminar == 12) {
            int filitaSeleccionada = tabEquipos.getSelectedRow();

            String codigoTexto = miModelitoTabla.getValueAt(filitaSeleccionada, 0).toString(); // fila y columna
            codEquipo = Integer.valueOf(codigoTexto);

            DaoEquipo miDao = new DaoEquipo();
            Equipo objEquipo = miDao.buscar(codEquipo);

            if (objEquipo == null) {
                JOptionPane.showMessageDialog(this, "No se encontró el Equipo", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                if (siElimino(codEquipo)) {
                    DaoEquipo miDaoElim = new DaoEquipo();
                    if (miDaoElim.eliminar(codEquipo)) {
                        cargarEquipos("");
                        JOptionPane.showMessageDialog(this, "Registro eliminado", "Información", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, "No se eliminó el consumible", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }

        if (columnaActualizar == 11) {
            int filaSeleccionada = tabEquipos.getSelectedRow();

            String codigoTexto = miModelitoTabla.getValueAt(filaSeleccionada, 0).toString();
            codEquipo = Integer.valueOf(codigoTexto);

            DaoEquipo miDao = new DaoEquipo();
            Equipo objEquipo = miDao.buscar(codEquipo);

            FrmEquiposEditar ventanaFlotante = new FrmEquiposEditar(null, true, objEquipo);
            ventanaFlotante.setVisible(true);

            ventanaFlotante.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    cargarEquipos("");
                }
            });
        }

    }//GEN-LAST:event_tabEquiposMouseClicked

    private void cmbSortActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSortActionPerformed
        // Selecciona el parámetro y lo ordena según este

        Integer seleccionarOrden = cmbSort.getSelectedIndex();
        switch (seleccionarOrden) {

            case 1 -> {
                cargarEquipos("id_equipo");
            }
            case 2 -> {
                cargarEquipos("cod_inventario_equipo");
            }
            case 3 -> {
                cargarEquipos("modelo_equipo");
            }
            case 4 -> {
                cargarEquipos("serie_equipo");
            }
            case 5 -> {
                cargarEquipos("mantenimiento_equipo");
            }
            case 6 -> {
                cargarEquipos("req_mante_equipo");
            }
            case 7 -> {
                cargarEquipos("estado_equipo");
            }
            case 8 -> {
                cargarEquipos("nombre_equipo");
            }
            case 9 -> {
                cargarEquipos("marca_equipo");
            }
            case 10 -> {
                cargarEquipos("disponibilidad_equipo");
            }
            case 11 -> {
                cargarEquipos("existencias_equipo");
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
            java.util.logging.Logger.getLogger(FrmEquiposAdmin.class
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
                new FrmEquiposAdmin().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAnadir;
    private javax.swing.JButton btnDescargar;
    private javax.swing.JTextField cajaBuscar;
    private javax.swing.JComboBox<String> cmbSort;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel jpContainer;
    private javax.swing.JPanel jpHome;
    private javax.swing.JPanel jpLateral;
    private javax.swing.JPanel jpLogo;
    private javax.swing.JPanel jpSearch;
    private javax.swing.JLabel lblHome;
    private javax.swing.JLabel lblImgSearch;
    private javax.swing.JLabel lblLogo;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JTable tabEquipos;
    // End of variables declaration//GEN-END:variables
}
