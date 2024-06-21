package com.scientia.daos;

import com.scientia.configuracion.MiConexion;
import com.scientia.entidades.Consumible;
import com.scientia.entidades.Elemento;
import com.scientia.entidades.Equipo;
import com.scientia.entidades.Prestamo;
import com.scientia.entidades.PrestamoItem;
import com.scientia.interfaces.Funcionalidad;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DaoPrestamoItem extends MiConexion implements Funcionalidad<PrestamoItem> {

    @Override
    public Boolean registrar(PrestamoItem elObjeto) {
        try {
            miObjConexion = obtenerConexion(); // Asegúrate de que este método obtiene una conexión válida
            miCadenaSQL = "INSERT INTO prestamos_items (id_prestamo, id_elemento, id_equipo, id_consumible) VALUES (?, ?, ?, ?)";
            miConsulta = miObjConexion.prepareStatement(miCadenaSQL);

            miConsulta.setInt(1, elObjeto.getCodPrestamo().getId_prestamo());
            miConsulta.setInt(2, elObjeto.getCodElemento().getId());
            miConsulta.setInt(3, elObjeto.getCodEquipo().getId_equipo());
            miConsulta.setInt(4, elObjeto.getCodConsumible().getIdConsumible());

            miCantidad = miConsulta.executeUpdate();
//            miObjConexion.close();
            return miCantidad > 0;
        } catch (SQLException ex) {
            Logger.getLogger(DaoConsumible.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    @Override
    public List<PrestamoItem> consultar(String orden) {
        try {
            miCadenaSQL = "SELECT * FROM prestamos_items";
            miConsulta = miObjConexion.prepareStatement(miCadenaSQL);
            misRegistros = miConsulta.executeQuery();

            List<PrestamoItem> arrPrestamoItems = new ArrayList<>();
            while (misRegistros.next()) {
                int idPrestamoItem = misRegistros.getInt("id_prestamo_item");
                Integer idPrestamo = misRegistros.getInt("id_prestamo");
                Integer idElemento = misRegistros.getInt("id_elemento");
                Integer idEquipo = misRegistros.getInt("id_equipo");
                Integer idConsumible = misRegistros.getInt("id_consumible");

                Elemento objElem = new Elemento(null, null, false, "", 0.0, null, 0, idElemento, "", "", false, idEquipo);
                Equipo objEqui = new Equipo(idEquipo, "", "", "", "", "", "", "", "", false, 0);
                Consumible objCon = new Consumible(idConsumible, "", "", 0, "", "", false, 0, null);
                Prestamo objPre = new Prestamo(idPrestamo, null, null, null);

                PrestamoItem prestamoItem = new PrestamoItem(idPrestamoItem, objPre, objElem, objEqui, objCon);
                arrPrestamoItems.add(prestamoItem);
            }

            miObjConexion.close();
            return arrPrestamoItems;
        } catch (SQLException ex) {
            Logger.getLogger(DaoPrestamoItem.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public PrestamoItem buscar(Integer llavePrimaria) {
        try {
            miCadenaSQL = "SELECT * FROM prestamos_items WHERE id_prestamo_item = ?";
            miConsulta = miObjConexion.prepareStatement(miCadenaSQL);
            miConsulta.setInt(1, llavePrimaria);
            misRegistros = miConsulta.executeQuery();

            PrestamoItem objEncontrado = null;

            if (misRegistros.next()) {
                int idPrestamoItem = misRegistros.getInt("id_prestamo_item");
                Integer idPrestamo = misRegistros.getInt("id_prestamo");
                Integer idElemento = misRegistros.getInt("id_elemento");
                Integer idEquipo = misRegistros.getInt("id_equipo");
                Integer idConsumible = misRegistros.getInt("id_consumible");

                Elemento objElem = new Elemento(null, null, false, "", 0.0, null, 0, idElemento, "", "", false, idEquipo);
                Equipo objEqui = new Equipo(idEquipo, "", "", "", "", "", "", "", "", false, 0);
                Consumible objCon = new Consumible(idConsumible, "", "", 0, "", "", false, 0, null);
                Prestamo objPre = new Prestamo(idPrestamo, null, null, null);

                objEncontrado = new PrestamoItem(idPrestamoItem, objPre, objElem, objEqui, objCon);
            }

            miObjConexion.close();
            return objEncontrado;
        } catch (SQLException ex) {
            Logger.getLogger(DaoPrestamoItem.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public Boolean eliminar(Integer llavePrimaria) {
        try {
            miCadenaSQL = "DELETE FROM prestamos_items WHERE id_prestamo_item = ?";
            miConsulta = miObjConexion.prepareStatement(miCadenaSQL);
            miConsulta.setInt(1, llavePrimaria);
            miCantidad = miConsulta.executeUpdate();
            miObjConexion.close();
            return miCantidad > 0;
        } catch (SQLException ex) {
            Logger.getLogger(DaoPrestamoItem.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public Boolean eliminar(Integer llavePrimaria, String tipoId) {
        try {
            miCadenaSQL = "DELETE FROM prestamos_items WHERE " + tipoId + " = ?";
            miConsulta = miObjConexion.prepareStatement(miCadenaSQL);
            miConsulta.setInt(1, llavePrimaria);
            miCantidad = miConsulta.executeUpdate();
            miObjConexion.close();
            return miCantidad > 0;
        } catch (SQLException ex) {
            Logger.getLogger(DaoPrestamoItem.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    @Override
    public Boolean actualizar(PrestamoItem elObjeto) {
        try {
            miCadenaSQL = "UPDATE prestamos_items SET id_prestamo = ?, id_elemento = ?, id_equipo = ?, id_consumible = ? WHERE id_prestamo_item = ?";
            miConsulta = miObjConexion.prepareStatement(miCadenaSQL);

            miConsulta.setInt(1, elObjeto.getCodPrestamo().getId_prestamo());
            miConsulta.setInt(2, elObjeto.getCodElemento().getId());
            miConsulta.setInt(3, elObjeto.getCodEquipo().getId_equipo());
            miConsulta.setInt(4, elObjeto.getCodConsumible().getIdConsumible());

            miConsulta.setInt(5, elObjeto.getIdPrestamoItem());

            miCantidad = miConsulta.executeUpdate();
            miObjConexion.close();
            return miCantidad > 0;
        } catch (SQLException ex) {
            Logger.getLogger(DaoPrestamoItem.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public Boolean actualizarItem(PrestamoItem elObjeto, String campo) {
        try {
            miCadenaSQL = "UPDATE prestamos_items SET id_prestamo = ?, id_elemento = ?, id_equipo = ?, id_consumible = ? WHERE " + campo + " = ?";
            miConsulta = miObjConexion.prepareStatement(miCadenaSQL);

            miConsulta.setInt(1, elObjeto.getCodPrestamo().getId_prestamo());
            miConsulta.setInt(2, elObjeto.getCodElemento().getId());
            miConsulta.setInt(3, elObjeto.getCodEquipo().getId_equipo());
            miConsulta.setInt(4, elObjeto.getCodConsumible().getIdConsumible());

            miConsulta.setInt(5, elObjeto.getIdPrestamoItem());

            miCantidad = miConsulta.executeUpdate();
            miObjConexion.close();
            return miCantidad > 0;
        } catch (SQLException ex) {
            Logger.getLogger(DaoPrestamoItem.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    @Override
    public List<PrestamoItem> buscarDato(String dato, String campo) {
        List<PrestamoItem> prestamosItemsEncontrados = new ArrayList<>();
        try {
            String consultaSQL = "SELECT * FROM prestamos_items WHERE " + campo + " LIKE ?";
            miConsulta = miObjConexion.prepareStatement(consultaSQL);
            miConsulta.setString(1, "%" + dato + "%");
            misRegistros = miConsulta.executeQuery();

            while (misRegistros.next()) {
                int idPrestamoItem = misRegistros.getInt("id_prestamo_item");
                Integer idPrestamo = misRegistros.getInt("id_prestamo");
                Integer idElemento = misRegistros.getInt("id_elemento");
                Integer idEquipo = misRegistros.getInt("id_equipo");
                Integer idConsumible = misRegistros.getInt("id_consumible");

                Elemento objElem = new Elemento(null, null, false, "", 0.0, null, 0, idElemento, "", "", false, idEquipo);
                Equipo objEqui = new Equipo(idEquipo, "", "", "", "", "", "", "", "", false, 0);
                Consumible objCon = new Consumible(idConsumible, "", "", 0, "", "", false, 0, null);
                Prestamo objPre = new Prestamo(idPrestamo, null, null, null);

                PrestamoItem prestamoItem = new PrestamoItem(idPrestamoItem, objPre, objElem, objEqui, objCon);
                prestamosItemsEncontrados.add(prestamoItem);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DaoPrestamoItem.class.getName()).log(Level.SEVERE, null, ex);

        }
        return prestamosItemsEncontrados;
    }

    public List<PrestamoItem> consultarPorPrestamo(Integer cod) {
        try {
            miCadenaSQL = "SELECT p.id_elemento, el.nombre_elemento, el.disponibilidad_elemento, "
                    + "p.id_consumible, c.nombre_consumible, c.disponibilidad_consumible, "
                    + "p.id_equipo, e.nombre_equipo, e.disponibilidad_equipo "
                    + "FROM prestamos_items p "
                    + "LEFT JOIN elementos el ON p.id_elemento = el.id_elemento "
                    + "LEFT JOIN equipos e ON p.id_equipo = e.id_equipo "
                    + "LEFT JOIN consumibles c ON p.id_consumible = c.id_consumible "
                    + "WHERE p.id_prestamo = ?";
            miConsulta = miObjConexion.prepareStatement(miCadenaSQL);
            miConsulta.setInt(1, cod);
            misRegistros = miConsulta.executeQuery();

            List<PrestamoItem> arrPrestamoItems = new ArrayList<>();
            while (misRegistros.next()) {
                Integer idElemento = misRegistros.getInt("id_elemento");
                Integer idEquipo = misRegistros.getInt("id_equipo");
                Integer idConsumible = misRegistros.getInt("id_consumible");

                String nomElemento = misRegistros.getString("nombre_elemento");
                String nomEquipo = misRegistros.getString("nombre_equipo");
                String nomConsumible = misRegistros.getString("nombre_consumible");

                Boolean disElemento = misRegistros.getBoolean("disponibilidad_elemento");
                Boolean disEquipo = misRegistros.getBoolean("disponibilidad_equipo");
                Boolean disConsumible = misRegistros.getBoolean("disponibilidad_consumible");

                Elemento objElem = new Elemento(null, null, false, "", 0.0, null, 0, idElemento, nomElemento, "", disElemento, null);

                Equipo objEqui = new Equipo(idEquipo, "", "", "", "", "", "", nomEquipo, "", disEquipo, 0);
                Consumible objCon = new Consumible(idConsumible, "", "", 0, nomConsumible, "", disConsumible, 0, null);

                PrestamoItem prestamoItem = new PrestamoItem(null, null, objElem, objEqui, objCon);
                arrPrestamoItems.add(prestamoItem);
            }

//            miObjConexion.close();
            return arrPrestamoItems;
        } catch (SQLException ex) {
            Logger.getLogger(DaoPrestamoItem.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

}
