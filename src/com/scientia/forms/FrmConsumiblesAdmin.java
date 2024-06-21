package com.scientia.forms;

import com.formdev.flatlaf.FlatLightLaf;
import com.scientia.daos.DaoConsumible;
import com.scientia.entidades.Consumible;
import com.scientia.utils.ImageScale;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import static javax.swing.SwingConstants.CENTER;
import javax.swing.table.JTableHeader;
import java.awt.Image;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.swing.ImageIcon;

/**
 *
 * @author danim
 */
public class FrmConsumiblesAdmin extends javax.swing.JFrame {

    Integer seleccionarBuscar = 0, codConsumible = null;

    String nomEliminar = "/com/scientia/icons/borrar.png";
    String rutaIconEliminar = this.getClass().getResource(nomEliminar).getPath();
    ImageIcon borrarIcono = new ImageIcon(rutaIconEliminar);

    String nomEditar = "/com/scientia/icons/editar.png";
    String rutaIconEditar = this.getClass().getResource(nomEditar).getPath();
    ImageIcon editarIcono = new ImageIcon(rutaIconEditar);

    private String titulos[] = {"ID", "Nombre", "Marca", "Num Gabinete", "Referencia", "Observaciones", "Disponibilidad", "Existencias", "Foto", "Editar", "Eliminar"};
    private DefaultTableModel miModelitoTabla = new DefaultTableModel(titulos, 0) {

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            if (columnIndex == 8 || columnIndex == 9 || columnIndex == 10) {
                return ImageIcon.class;
            }
            return Object.class;
        }

    };

    public FrmConsumiblesAdmin() {
        initComponents();
        tabConsumibles.setModel(miModelitoTabla);
        cargarConsumibles("");

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

        jpSearch.setBounds(widthContainer - 625, 50, (int) (widthContainer * 0.18), (int) (heightContainer * 0.045));
        cmbSort.setBounds(widthContainer - 900, 50, (int) (widthContainer * 0.18), (int) (heightContainer * 0.045));

        btnAnadir.setBounds(widthContainer - 320, 50, (int) (width * 0.15), (int) (height * 0.052));

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
        JTableHeader header = tabConsumibles.getTableHeader();
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

    private void cargarConsumibles(String ordencito) {
        List<Consumible> arrConsumibles;

        DaoConsumible miDao = new DaoConsumible();
        miModelitoTabla.setNumRows(0);

        arrConsumibles = miDao.consultar(ordencito);
        if (arrConsumibles != null) {
            arrConsumibles.forEach((var consumible) -> {
                Object fila[] = new Object[11];

                fila[0] = consumible.getIdConsumible();
                fila[1] = consumible.getNombreConsumible();
                fila[2] = consumible.getMarcaConsumible();
                fila[3] = consumible.getNumGabineteConsumible();
                fila[4] = consumible.getReferenciaConsumible();
                fila[5] = consumible.getObservacionesConsumible();

                if (consumible.getDisponibilidadConsumible()) {
                    fila[6] = "Si";
                } else {
                    fila[6] = "No";

                }

                fila[7] = consumible.getExistenciasConsumible();

                // Convertir InputStream a ImageIcon
                try {
                    InputStream is = consumible.getFotoConsumible();
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
                    fila[8] = fotoIcon;
                } catch (IOException e) {
                    fila[8] = null;
                    e.printStackTrace();
                }

                fila[9] = editarIcono;
                fila[10] = borrarIcono;

                miModelitoTabla.addRow(fila);
            });

            tabConsumibles.getColumnModel().getColumn(9).setPreferredWidth(50);
            tabConsumibles.getColumnModel().getColumn(10).setPreferredWidth(50);

            // Ajustar la altura de las filas
            tabConsumibles.setRowHeight(75);
        } else {
            System.out.println("La lista de consumibles está vacía.");
        }
    }

    private boolean siElimino(Integer codigo) {
        int opcion;
        boolean bandera = false;
        String textoBotones[] = {"Aceptar", "Cancelar"};
        DaoConsumible dao = new DaoConsumible();

        Consumible objConsumible = dao.buscar(codigo);

        opcion = JOptionPane.showOptionDialog(
                null,
                "¿Está seguro de eliminar el consumible " + objConsumible.getNombreConsumible() + "?",
                "Aviso",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                textoBotones,
                textoBotones[1]
        );

        if (opcion == JOptionPane.YES_OPTION) {
            bandera = true;
        }

        return bandera;
    }

    private String campoBuscarConsumible(int indice) {
        switch (indice) {
            case 0:
                return "nombre_consumible";
            case 1:
                return "id_consumible";
            case 2:
                return "nombre_consumible";
            case 3:
                return "marca_consumible";
            case 4:
                return "num_gabinete_consumible";
            case 5:
                return "referencia_consumible";
            case 6:
                return "observaciones_consumible";
            case 7:
                return "disponibilidad_consumible";
            case 8:
                return "existencias_consumible";
            default:
                throw new IllegalArgumentException("Índice de búsqueda inválido: " + indice);
        }
    }

    private void buscarConsumible(String dato, String campo) {
        //NO CAMBIAR LOS PARÁMETROS DE ENTRADA
        List<Consumible> consumiblesEncontrados;
        DaoConsumible daoConsumible = new DaoConsumible();

        miModelitoTabla.setNumRows(0);

        consumiblesEncontrados = daoConsumible.buscarDato(dato, campo);
        consumiblesEncontrados.forEach((consumible) -> {
            Object fila[] = new Object[11];

            fila[0] = consumible.getIdConsumible();
            fila[1] = consumible.getNombreConsumible();
            fila[2] = consumible.getMarcaConsumible();
            fila[3] = consumible.getNumGabineteConsumible();
            fila[4] = consumible.getReferenciaConsumible();
            fila[5] = consumible.getObservacionesConsumible();
            fila[6] = consumible.getDisponibilidadConsumible();
            fila[7] = consumible.getExistenciasConsumible();

            // Convertir InputStream a ImageIcon
            try {
                InputStream is = consumible.getFotoConsumible();
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
                fila[8] = fotoIcon;
            } catch (IOException e) {
                fila[8] = null;
            }

            fila[9] = editarIcono;
            fila[10] = borrarIcono;

            miModelitoTabla.addRow(fila);
        });
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
        tabConsumibles = new javax.swing.JTable();
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
        cajaBuscar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cajaBuscarMouseClicked(evt);
            }
        });
        cajaBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cajaBuscarActionPerformed(evt);
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
        cmbSort.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ordenar por:", "ID", "Nombre", "Marca", "Num Gabinete", "Referencia ", "Observaciones", "Disponibilidad", "Existencias" }));
        cmbSort.setBorder(null);
        cmbSort.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSortActionPerformed(evt);
            }
        });
        jpContainer.add(cmbSort);
        cmbSort.setBounds(460, 30, 206, 35);

        lblTitulo.setFont(new java.awt.Font("Montserrat", 1, 60)); // NOI18N
        lblTitulo.setText("Consumibles");
        jpContainer.add(lblTitulo);
        lblTitulo.setBounds(10, 30, 420, 75);

        tabConsumibles.setFont(new java.awt.Font("Montserrat SemiBold", 0, 16)); // NOI18N
        tabConsumibles.setModel(new javax.swing.table.DefaultTableModel(
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
        tabConsumibles.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        tabConsumibles.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabConsumiblesMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tabConsumibles);

        jpContainer.add(jScrollPane1);
        jScrollPane1.setBounds(10, 130, 901, 460);

        btnAnadir.setBackground(new java.awt.Color(37, 206, 203));
        btnAnadir.setFont(new java.awt.Font("Montserrat", 1, 18)); // NOI18N
        btnAnadir.setForeground(new java.awt.Color(255, 255, 255));
        btnAnadir.setText("Añadir Consumibles");
        btnAnadir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnadirActionPerformed(evt);
            }
        });
        jpContainer.add(btnAnadir);
        btnAnadir.setBounds(640, 90, 250, 30);

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

        DaoConsumible miDao = new DaoConsumible();

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

        FrmConsumibleRegistrar frameEditar = new FrmConsumibleRegistrar();
        frameEditar.setVisible(true);
        this.setVisible(false);

    }//GEN-LAST:event_btnAnadirActionPerformed

    private void tabConsumiblesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabConsumiblesMouseClicked
        int columnaEliminar = tabConsumibles.getSelectedColumn();
        int columnaActualizar = tabConsumibles.getSelectedColumn();

        if (columnaEliminar == 10) {
            int filitaSeleccionada = tabConsumibles.getSelectedRow();

            String codigoTexto = miModelitoTabla.getValueAt(filitaSeleccionada, 0).toString(); // fila y columna
            codConsumible = Integer.valueOf(codigoTexto);

            DaoConsumible miDao = new DaoConsumible();
            Consumible objConsumible = miDao.buscar(codConsumible);

            if (objConsumible == null) {
                JOptionPane.showMessageDialog(this, "No se encontró el consumible", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                if (siElimino(codConsumible)) {
                    DaoConsumible miDaoElim = new DaoConsumible();
                    if (miDaoElim.eliminar(codConsumible)) {
                        cargarConsumibles("");
                        JOptionPane.showMessageDialog(this, "Registro eliminado", "Información", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, "No se eliminó el consumible", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }

        if (columnaActualizar == 9) {
            int filaSeleccionada = tabConsumibles.getSelectedRow();

            String codigoTexto = miModelitoTabla.getValueAt(filaSeleccionada, 0).toString();
            codConsumible = Integer.valueOf(codigoTexto);

            DaoConsumible miDao = new DaoConsumible();
            Consumible objConsumible = miDao.buscar(codConsumible);

            FrmConsumibleEditar ventanaFlotante = new FrmConsumibleEditar(objConsumible);
            ventanaFlotante.setVisible(true);

            ventanaFlotante.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    cargarConsumibles("");
                }
            });
        }

    }//GEN-LAST:event_tabConsumiblesMouseClicked

    private void cmbSortActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSortActionPerformed

        String seleccionarOrden = (String) cmbSort.getSelectedItem();
        System.out.println("Valor seleccionado: " + seleccionarOrden);  // Para depuración

        switch (seleccionarOrden) {
            case "Nombre" ->
                cargarConsumibles("nombre_consumible");
            case "ID" ->
                cargarConsumibles("id_consumible");

            case "Marca" ->
                cargarConsumibles("marca_consumible");
            case "Num Gabinete" ->
                cargarConsumibles("num_gabinete_consumible");
            case "Referencia" ->
                cargarConsumibles("referencia_consumible");
            case "Observaciones" ->
                cargarConsumibles("observaciones_consumible");
            case "Disponibilidad" ->
                cargarConsumibles("disponibilidad_consumible");
            case "Existencias" ->
                cargarConsumibles("existencias_consumible");
            default -> {
                System.err.println("Error: valor no esperado: " + seleccionarOrden);
                throw new AssertionError("Valor no esperado: " + seleccionarOrden);
            }
        }


    }//GEN-LAST:event_cmbSortActionPerformed

    private void cajaBuscarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cajaBuscarMouseClicked

        seleccionarBuscar = cmbSort.getSelectedIndex();

        String campoSelect = campoBuscarConsumible(seleccionarBuscar);
        System.out.println("campo: " + campoSelect);

        buscarConsumible("%" + cajaBuscar.getText().toLowerCase() + "%", campoSelect);

    }//GEN-LAST:event_cajaBuscarMouseClicked

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
            java.util.logging.Logger.getLogger(FrmConsumiblesAdmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FrmConsumiblesAdmin().setVisible(true);
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
    private javax.swing.JTable tabConsumibles;
    // End of variables declaration//GEN-END:variables
}
