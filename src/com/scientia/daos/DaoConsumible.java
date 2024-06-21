package com.scientia.daos;

import com.scientia.configuracion.MiConexion;
import com.scientia.entidades.Consumible;
import com.scientia.interfaces.Funcionalidad;
import java.io.IOException;

import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author sisa
 */
public class DaoConsumible extends MiConexion implements Funcionalidad<Consumible> {

    @Override
    public Boolean registrar(Consumible elObjeto) {

        try {
            miCadenaSQL = "INSERT INTO consumibles (referencia_consumible, observaciones_consumible,"
                    + " num_gabinete_consumible, nombre_consumible, marca_consumible, disponibilidad_consumible,"
                    + " existencias_consumible, foto_consumible)"
                    + " VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            miConsulta = miObjConexion.prepareStatement(miCadenaSQL);
            miConsulta.setString(1, elObjeto.getReferenciaConsumible());
            miConsulta.setString(2, elObjeto.getObservacionesConsumible());
            miConsulta.setInt(3, elObjeto.getNumGabineteConsumible());
            miConsulta.setString(4, elObjeto.getNombreConsumible());
            miConsulta.setString(5, elObjeto.getMarcaConsumible());
            miConsulta.setBoolean(6, elObjeto.getDisponibilidadConsumible());
            miConsulta.setInt(7, elObjeto.getExistenciasConsumible());
            miConsulta.setBlob(8, elObjeto.getFotoConsumible());

            miCantidad = miConsulta.executeUpdate();
            return miCantidad > 0;
        } catch (SQLException ex) {
            Logger.getLogger(DaoConsumible.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    @Override
    public List<Consumible> consultar(String orden) {
        try {
            if (orden.isEmpty()) {
                orden = "id_consumible";
            }

            miCadenaSQL = "SELECT id_consumible, referencia_consumible, observaciones_consumible, num_gabinete_consumible, nombre_consumible, marca_consumible, disponibilidad_consumible, existencias_consumible, foto_consumible FROM consumibles ORDER BY " + orden;

            miConsulta = miObjConexion.prepareStatement(miCadenaSQL);
            misRegistros = miConsulta.executeQuery();

            List<Consumible> arrConsumibles = new ArrayList<>();
            while (misRegistros.next()) {
                int idConsumible = misRegistros.getInt("id_consumible");
                String referenciaConsumible = misRegistros.getString("referencia_consumible");
                String observacionesConsumible = misRegistros.getString("observaciones_consumible");
                int numGabineteConsumible = misRegistros.getInt("num_gabinete_consumible");
                String nombreConsumible = misRegistros.getString("nombre_consumible");
                String marcaConsumible = misRegistros.getString("marca_consumible");
                boolean disponibilidadConsumible = misRegistros.getBoolean("disponibilidad_consumible");
                int existenciasConsumible = misRegistros.getInt("existencias_consumible");
                InputStream fotoConsumible = misRegistros.getBlob("foto_consumible").getBinaryStream();

                Consumible consumible = new Consumible(idConsumible, referenciaConsumible, observacionesConsumible, numGabineteConsumible, nombreConsumible, marcaConsumible, disponibilidadConsumible, existenciasConsumible, fotoConsumible);
                arrConsumibles.add(consumible);
            }

            miObjConexion.close();
            return arrConsumibles;
        } catch (SQLException ex) {
            Logger.getLogger(DaoConsumible.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public Consumible buscar(Integer llavePrimaria) {

        try {
            miCadenaSQL = "SELECT * FROM consumibles WHERE id_consumible = ?";
            miConsulta = miObjConexion.prepareStatement(miCadenaSQL);
            miConsulta.setInt(1, llavePrimaria);
            misRegistros = miConsulta.executeQuery();

            Consumible objEncontrado = null;
            if (misRegistros.next()) {
                int idConsumible = misRegistros.getInt("id_consumible");
                String referencia = misRegistros.getString("referencia_consumible");
                String observaciones = misRegistros.getString("observaciones_consumible");
                int numGabinete = misRegistros.getInt("num_gabinete_consumible");
                String nombre = misRegistros.getString("nombre_consumible");
                String marca = misRegistros.getString("marca_consumible");
                boolean disponibilidad = misRegistros.getBoolean("disponibilidad_consumible");
                int existencias = misRegistros.getInt("existencias_consumible");
                InputStream foto = misRegistros.getBlob("foto_consumible").getBinaryStream();

                objEncontrado = new Consumible(idConsumible, referencia, observaciones, numGabinete,
                        nombre, marca, disponibilidad, existencias, foto);

            }

            miObjConexion.close();
            return objEncontrado;
        } catch (SQLException ex) {
            Logger.getLogger(DaoEquipo.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public Boolean eliminar(Integer llavePrimaria) {

        try {
            miCadenaSQL = "DELETE FROM consumibles WHERE id_consumible=?";
            miConsulta = miObjConexion.prepareStatement(miCadenaSQL);
            miConsulta.setInt(1, llavePrimaria);
            miCantidad = miConsulta.executeUpdate();
            return miCantidad > 0;
        } catch (SQLException ex) {
            Logger.getLogger(DaoEquipo.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    @Override
    public Boolean actualizar(Consumible elObjeto) {

        try {
            miCadenaSQL = "UPDATE consumibles SET referencia_consumible = ?, "
                    + "observaciones_consumible = ?, num_gabinete_consumible = ?, "
                    + "nombre_consumible = ?, marca_consumible = ?, "
                    + "disponibilidad_consumible = ?, existencias_consumible = ?, "
                    + "foto_consumible = ? "
                    + "WHERE id_consumible = ?";
            miConsulta = miObjConexion.prepareStatement(miCadenaSQL);

            miConsulta.setString(1, elObjeto.getReferenciaConsumible());
            miConsulta.setString(2, elObjeto.getObservacionesConsumible());
            miConsulta.setInt(3, elObjeto.getNumGabineteConsumible());
            miConsulta.setString(4, elObjeto.getNombreConsumible());
            miConsulta.setString(5, elObjeto.getMarcaConsumible());
            miConsulta.setBoolean(6, elObjeto.getDisponibilidadConsumible());
            miConsulta.setInt(7, elObjeto.getExistenciasConsumible());
            miConsulta.setBlob(8, elObjeto.getFotoConsumible());
            miConsulta.setInt(9, elObjeto.getIdConsumible());

            miCantidad = miConsulta.executeUpdate();
            return miCantidad > 0;
        } catch (SQLException ex) {
            Logger.getLogger(DaoEquipo.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    @Override
    public List<Consumible> buscarDato(String dato, String campo) {
        List<Consumible> consumiblesEncontrados = new ArrayList<>();

        try {
            String consultaSQL = "SELECT * FROM consumibles WHERE " + campo + " = ?";
            miConsulta = miObjConexion.prepareStatement(consultaSQL);
            miConsulta.setString(1, dato);

            misRegistros = miConsulta.executeQuery();

            while (misRegistros.next()) {
                int idConsumible = misRegistros.getInt("id_consumible");
                String referenciaConsumible = misRegistros.getString("referencia_consumible");
                String observacionesConsumible = misRegistros.getString("observaciones_consumible");
                int numGabineteConsumible = misRegistros.getInt("num_gabinete_consumible");
                String nombreConsumible = misRegistros.getString("nombre_consumible");
                String marcaConsumible = misRegistros.getString("marca_consumible");
                boolean disponibilidadConsumible = misRegistros.getBoolean("disponibilidad_consumible");
                int existenciasConsumible = misRegistros.getInt("existencias_consumible");
                InputStream fotoConsumible = misRegistros.getBlob("foto_consumible").getBinaryStream();

                System.out.println("xxxxxxxx" + idConsumible);

                Consumible consumible = new Consumible(idConsumible, referenciaConsumible, observacionesConsumible, numGabineteConsumible, nombreConsumible, marcaConsumible, disponibilidadConsumible, existenciasConsumible, fotoConsumible);
                consumiblesEncontrados.add(consumible);
            }

        } catch (SQLException ex) {
            Logger.getLogger(DaoConsumible.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (misRegistros != null) {
                    misRegistros.close();
                }
                if (miConsulta != null) {
                    miConsulta.close();
                }
                if (miObjConexion != null) {
                    miObjConexion.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(DaoConsumible.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return consumiblesEncontrados;
    }

    public Integer buscarId(String dato) {
        int idConsumible = 0;
        try {
            System.out.println("DATOO" + dato);
            miCadenaSQL = "SELECT id_consumible FROM consumibles WHERE nombre_consumible = ?";
            System.out.println("cadena dao:" + miCadenaSQL);

            miConsulta = miObjConexion.prepareStatement(miCadenaSQL);
            miConsulta.setString(1, dato);
            misRegistros = miConsulta.executeQuery();

            while (misRegistros.next()) {

                idConsumible = misRegistros.getInt("id_consumible");
                System.out.println("xxxxxxxxDATO:::" + idConsumible);

            }

            miObjConexion.close();
            return idConsumible;

        } catch (SQLException ex) {
            Logger.getLogger(DaoEquipo.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public String loVoyAGrabarEnUnArchivoEsPeroYa(DefaultTableModel miModeloLista, String rutaAplicacion, String nombreArchivoSalida) {
        try {
            int i, j, numFilas, numColumnas;
            String cadenaTexto, archiSalida;

            numFilas = miModeloLista.getRowCount();
            numColumnas = miModeloLista.getColumnCount() - 1; // Excluir la última columna
            archiSalida = rutaAplicacion + "\\" + nombreArchivoSalida;
            ArchivoPlanoNIO miArchivo = new ArchivoPlanoNIO(archiSalida);
            miArchivo.resetear();

            for (i = 0; i < numFilas; i++) {
                StringBuilder filaTexto = new StringBuilder();
                for (j = 0; j < numColumnas; j++) {
                    Object valor = miModeloLista.getValueAt(i, j);
                    cadenaTexto = (valor != null) ? valor.toString() : "";
                    filaTexto.append(cadenaTexto);
                    if (j < numColumnas - 1) {
                        filaTexto.append(";"); // Usa tabulador para separar las columnas, puedes cambiarlo si prefieres otro separador
                    }
                }
                miArchivo.agregarDato(filaTexto.toString());
            }

            return archiSalida;
        } catch (IOException ex) {
            Logger.getLogger(DaoElemento.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
