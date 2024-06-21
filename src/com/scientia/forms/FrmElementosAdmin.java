package com.scientia.forms;

import com.formdev.flatlaf.FlatLightLaf;
import com.scientia.daos.DaoElemento;
import com.scientia.entidades.Elemento;
import com.scientia.utils.ImageScale;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
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

public class FrmElementosAdmin extends javax.swing.JFrame {

    Integer indiceBuscar = 0;
    Integer idElemento = 0;

    private Integer codigoElementos = null;
    private String titulos[] = {"Id", "Nombre", "Marca", "Disponibilidad", "Existencias", "Código de Inventario", "Foto", "Manual", "Acta", "Costo", "Fecha de compra", "Cantidad Accesorios", "Eliminar", "Editar"};
    private DefaultTableModel miModeloTabla = new DefaultTableModel(titulos, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            if (columnIndex == 6 || columnIndex == 12 || columnIndex == 13) {
                return ImageIcon.class;
            } else {
                return Object.class;
            }
        }

    };

    public FrmElementosAdmin() {
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

        tabElementos.setModel(miModeloTabla);
        cargarElementos("");

        String nomLogo = "..\\ScientiaLab\\src\\com\\scientia\\icons\\logo.png";
        System.out.println(nomLogo);
        ImageScale.setImageToLabel(lblLogo, nomLogo);

    }

    private void cargarElementos(String orden) {
        List<Elemento> arrElementos;
        DaoElemento miDao = new DaoElemento();

        String nomEliminar = "/com/scientia/icons/borrar.png";
        String rutaIconEliminar = this.getClass().getResource(nomEliminar).getPath();
        ImageIcon borrarIcono = new ImageIcon(rutaIconEliminar);

        String nomEditar = "/com/scientia/icons/editar.png";
        String rutaIconEditar = this.getClass().getResource(nomEditar).getPath();
        ImageIcon editarIcono = new ImageIcon(rutaIconEditar);

        miModeloTabla.setNumRows(0);
        arrElementos = miDao.consultar(orden);

        System.out.println(arrElementos.toString());

        arrElementos.forEach((elemento) -> {
            Object fila[] = new Object[14];

            fila[0] = elemento.getId();
            fila[1] = elemento.getNombre();
            fila[2] = elemento.getMarca();

            fila[4] = elemento.getExistencias();
            fila[5] = elemento.getCodInventarioElemento().toString();

            fila[8] = elemento.getActaElemento();
            fila[9] = "$ " + elemento.getCostoElemento();
            fila[10] = elemento.getFechaCompraElemento();
            fila[11] = elemento.getCantAccesorios();
            fila[12] = borrarIcono;
            fila[13] = editarIcono;

            if (elemento.getDisponibilidad()) {
                fila[3] = "Si";
            } else {
                fila[3] = "No";

            }
            if (elemento.getManualElemento()) {
                fila[7] = "Si";
            } else {
                fila[7] = "No";

            }

            // Convertir InputStream a ImageIcon
            try {
                InputStream is = elemento.getFotoElemento();
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                int nRead;
                byte[] data = new byte[16384];
                while ((nRead = is.read(data, 0, data.length)) != -1) {
                    buffer.write(data, 0, nRead);
                }
                buffer.flush();
                byte[] imageBytes = buffer.toByteArray();
                ImageIcon fotoIcon = new ImageIcon(imageBytes);
                Image img = fotoIcon.getImage();
                Image newImg = img.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                fotoIcon = new ImageIcon(newImg);
                fila[6] = fotoIcon;
            } catch (IOException e) {
                fila[6] = null;
                e.printStackTrace();
            }

            miModeloTabla.addRow(fila);

        });
        tabElementos.getColumnModel().getColumn(12).setPreferredWidth(50);
        tabElementos.getColumnModel().getColumn(13).setPreferredWidth(50);

        // Ajustar la altura de las filas
        tabElementos.setRowHeight(75);

    }

    private String campoBuscar(int select) {

        String campo = "";

        switch (select) {
            case 1 ->
                campo = "e.id_elemento";
            case 2 ->
                campo = "e.nombre_elemento";
            case 3 ->
                campo = "e.marca_elemento";
            case 4 ->
                campo = "e.disponibilidad_elemento";
            case 5 ->
                campo = "e.existencias_elemento";
            case 6 ->
                campo = "e.cod_inventario_elemento";
            case 7 ->
                campo = "e.manual_elemento";
            case 8 ->
                campo = "e.acta_elemento";
            case 9 ->
                campo = "e.costo_elemento";
            case 10 ->
                campo = "e.fecha_compra_elemento";
        }

        return campo;
    }

    private void buscarDato(String dato, String campo) {
        List<Elemento> arregloElementos;
        DaoElemento dao = new DaoElemento();

        miModeloTabla.setNumRows(0);

        arregloElementos = dao.buscarDato(dato, campo);

        String nomEliminar = "/com/scientia/icons/borrar.png";
        String rutaIconEliminar = this.getClass().getResource(nomEliminar).getPath();
        ImageIcon borrarIcono = new ImageIcon(rutaIconEliminar);

        String nomEditar = "/com/scientia/icons/editar.png";
        String rutaIconEditar = this.getClass().getResource(nomEditar).getPath();
        ImageIcon editarIcono = new ImageIcon(rutaIconEditar);

        arregloElementos.forEach((elemento) -> {
            Object fila[] = new Object[14];

            fila[0] = elemento.getId();
            fila[1] = elemento.getNombre();
            fila[2] = elemento.getMarca();
            fila[3] = elemento.getDisponibilidad();
            fila[4] = elemento.getExistencias();
            fila[5] = elemento.getCodInventarioElemento().toString();

            // Convertir InputStream a ImageIcon
            try {
                InputStream is = elemento.getFotoElemento();
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                int nRead;
                byte[] data = new byte[16384];
                while ((nRead = is.read(data, 0, data.length)) != -1) {
                    buffer.write(data, 0, nRead);
                }
                buffer.flush();
                byte[] imageBytes = buffer.toByteArray();
                ImageIcon fotoIcon = new ImageIcon(imageBytes);
                Image img = fotoIcon.getImage();
                Image newImg = img.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                fotoIcon = new ImageIcon(newImg);
                fila[6] = fotoIcon;
            } catch (IOException e) {
                fila[6] = null;
                e.printStackTrace();
            }

            fila[7] = elemento.getManualElemento();
            fila[8] = elemento.getActaElemento();
            fila[9] = "$ " + elemento.getCostoElemento();
            fila[10] = elemento.getFechaCompraElemento();
            fila[11] = elemento.getCantAccesorios();
            fila[12] = borrarIcono;
            fila[13] = editarIcono;

            miModeloTabla.addRow(fila);

        });

        tabElementos.getColumnModel().getColumn(12).setPreferredWidth(50);
        tabElementos.getColumnModel().getColumn(13).setPreferredWidth(50);

        // Ajustar la altura de las filas
        tabElementos.setRowHeight(75);
    }

    private Boolean eliminar(Integer codigoEliminar) {
        int opcion;
        Boolean bandera = false;
        String textoBotones[] = {"Aceptar", "Cancelar"};

        DaoElemento miDao = new DaoElemento();
        Elemento objElemento = miDao.buscar(codigoEliminar);

        opcion = JOptionPane.showOptionDialog(this,
                "¿Desea eliminar el elemento " + objElemento.getNombre() + "?", "Aviso",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE, null,
                textoBotones, textoBotones[1]);

        if (opcion == JOptionPane.YES_OPTION) {
            if (objElemento.getId() >= 0) {
                bandera = true;
            }
        }
        return bandera;
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

        cmbSort.setBounds(widthContainer - 970, 55, (int) (widthContainer * 0.18), (int) (heightContainer * 0.045));
        jpSearch.setBounds(widthContainer - 700, 55, (int) (widthContainer * 0.18), (int) (heightContainer * 0.045));

        btnAnadir.setBounds(widthContainer - 430, 50, (int) (width * 0.13), (int) (height * 0.052));
        btnAccesorios.setBounds(widthContainer - 200, 50, (int) (width * 0.09), (int) (height * 0.052));

        btnDescargar.setBounds(1100, 720, (int) (width * 0.15), (int) (height * 0.052));
        jScrollPane1.setBounds(20, 150, (int) (width * 0.86), (int) (height * 0.63));

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

        //DISEÑO DE LA TABLA 
        JTableHeader header = tabElementos.getTableHeader();
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
        tabElementos = new javax.swing.JTable();
        btnAccesorios = new javax.swing.JButton();
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
        jpSearch.setBounds(615, 34, 239, 35);

        cmbSort.setFont(new java.awt.Font("Montserrat", 1, 18)); // NOI18N
        cmbSort.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Ordenar/Buscar por", "Id", "Nombre", "Marca", "Disponibilidad", "Existencias", "Código de Inventario", "Manual", "Acta", "Costo", "Fecha de compra" }));
        cmbSort.setBorder(null);
        cmbSort.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSortActionPerformed(evt);
            }
        });
        jpContainer.add(cmbSort);
        cmbSort.setBounds(403, 34, 206, 35);

        lblTitulo.setFont(new java.awt.Font("Montserrat", 1, 60)); // NOI18N
        lblTitulo.setText("Elementos");
        jpContainer.add(lblTitulo);
        lblTitulo.setBounds(10, 30, 390, 75);

        tabElementos.setFont(new java.awt.Font("Montserrat SemiBold", 0, 14)); // NOI18N
        tabElementos.setModel(new javax.swing.table.DefaultTableModel(
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
        tabElementos.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        tabElementos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabElementosMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tabElementos);

        jpContainer.add(jScrollPane1);
        jScrollPane1.setBounds(10, 130, 901, 460);

        btnAccesorios.setBackground(new java.awt.Color(37, 206, 203));
        btnAccesorios.setFont(new java.awt.Font("Montserrat", 1, 18)); // NOI18N
        btnAccesorios.setForeground(new java.awt.Color(255, 255, 255));
        btnAccesorios.setText("Accesorios");
        btnAccesorios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAccesoriosActionPerformed(evt);
            }
        });
        jpContainer.add(btnAccesorios);
        btnAccesorios.setBounds(750, 90, 150, 30);

        btnAnadir.setBackground(new java.awt.Color(37, 206, 203));
        btnAnadir.setFont(new java.awt.Font("Montserrat", 1, 18)); // NOI18N
        btnAnadir.setForeground(new java.awt.Color(255, 255, 255));
        btnAnadir.setText("Añadir Elemento");
        btnAnadir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnadirActionPerformed(evt);
            }
        });
        jpContainer.add(btnAnadir);
        btnAnadir.setBounds(510, 90, 210, 30);

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

        DaoElemento miDao = new DaoElemento();

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

    private void btnAnadirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnadirActionPerformed
        FrmElementoRegistrar elementosRegistrar = new FrmElementoRegistrar();
        elementosRegistrar.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_btnAnadirActionPerformed

    private void cmbSortActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSortActionPerformed
        Integer indiceSort;

        indiceSort = cmbSort.getSelectedIndex();

        switch (indiceSort) {
            case 1 ->
                cargarElementos("e.id_elemento");
            case 2 ->
                cargarElementos("e.nombre_elemento");
            case 3 ->
                cargarElementos("e.marca_elemento");
            case 4 ->
                cargarElementos("e.disponibilidad_elemento");
            case 5 ->
                cargarElementos("e.existencias_elemento");
            case 6 ->
                cargarElementos("e.cod_inventario_elemento");
            case 7 ->
                cargarElementos("e.manual_elemento");
            case 8 ->
                cargarElementos("e.acta_elemento");
            case 9 ->
                cargarElementos("e.costo_elemento");
            case 10 ->
                cargarElementos("e.fecha_compra_elemento");

        }

    }//GEN-LAST:event_cmbSortActionPerformed

    private void cajaBuscarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cajaBuscarKeyReleased
        indiceBuscar = cmbSort.getSelectedIndex();

        String campoSelecccionado = campoBuscar(indiceBuscar);

        buscarDato("%" + cajaBuscar.getText().toLowerCase() + "%", campoSelecccionado);
    }//GEN-LAST:event_cajaBuscarKeyReleased

    private void tabElementosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabElementosMouseClicked
        int columnaSeleccionada = tabElementos.getSelectedColumn();

        if (columnaSeleccionada == 12) {
            int filaSeleccionada = tabElementos.getSelectedRow();

            String codigoTexto = miModeloTabla.getValueAt(filaSeleccionada, 0).toString();
            idElemento = Integer.valueOf(codigoTexto);

            DaoElemento miDao = new DaoElemento();
            Elemento objElemento = miDao.buscar(idElemento);

            if (objElemento == null) {
                JOptionPane.showMessageDialog(jpContainer, "No se encontró el elemento",
                        "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                if (eliminar(idElemento)) {
                    DaoElemento miDaoElim = new DaoElemento();
                    if (miDaoElim.eliminar(idElemento)) {
                        cargarElementos("");
                        JOptionPane.showMessageDialog(jpContainer, "Eliminación exitosa",
                                "Información", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(jpContainer, "No se pudo eliminar",
                                "Advertencia", JOptionPane.WARNING_MESSAGE);
                    }
                }
            }
        }

        if (columnaSeleccionada == 13) {
            int filaSeleccionada = tabElementos.getSelectedRow();

            String codigoTexto = miModeloTabla.getValueAt(filaSeleccionada, 0).toString();
            idElemento = Integer.valueOf(codigoTexto);

            DaoElemento miDao = new DaoElemento();
            Elemento objElemento = miDao.buscar(idElemento);

            FrmElementoEditar ventanaFlotante = new FrmElementoEditar(null, true, objElemento);
            ventanaFlotante.setVisible(true);
            this.setVisible(false);

            ventanaFlotante.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    cargarElementos("");
                }
            });
        }
    }//GEN-LAST:event_tabElementosMouseClicked

    private void btnAccesoriosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAccesoriosActionPerformed
        FrmAccesorios accesorios = new FrmAccesorios();
        accesorios.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_btnAccesoriosActionPerformed

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
            java.util.logging.Logger.getLogger(FrmElementosAdmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
                new FrmElementosAdmin().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAccesorios;
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
    private javax.swing.JTable tabElementos;
    // End of variables declaration//GEN-END:variables
}
