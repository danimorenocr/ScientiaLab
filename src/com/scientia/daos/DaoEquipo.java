package com.scientia.daos;

import com.scientia.configuracion.MiConexion;
import com.scientia.entidades.Equipo;
import com.scientia.interfaces.Funcionalidad;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

public class DaoEquipo extends MiConexion implements Funcionalidad<Equipo> {

    @Override
    public Boolean registrar(Equipo elObjeto) {
        try {
            miCadenaSQL = "INSERT INTO equipos (cod_inventario_equipo, modelo_equipo, serie_equipo, "
                    + "mantenimiento_equipo, req_mante_equipo, estado_equipo, nombre_equipo, "
                    + "marca_equipo, disponibilidad_equipo, existencias_equipo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            miConsulta = miObjConexion.prepareStatement(miCadenaSQL);

            miConsulta.setString(1, elObjeto.getCod_inventario_equipo());
            miConsulta.setString(2, elObjeto.getModelo_equipo());
            miConsulta.setString(3, elObjeto.getSerie_equipo());
            miConsulta.setString(4, elObjeto.getMantenimiento_equipo());
            miConsulta.setString(5, elObjeto.getReq_mante_equipo());
            miConsulta.setString(6, elObjeto.getEstado_equipo());
            miConsulta.setString(7, elObjeto.getNombre_equipo());
            miConsulta.setString(8, elObjeto.getMarca_equipo());
            miConsulta.setBoolean(9, elObjeto.getDisponibilidad_equipo());
            miConsulta.setInt(10, elObjeto.getExistencias_equipo());

            miCantidad = miConsulta.executeUpdate();
            miObjConexion.close();
            return miCantidad > 0;
        } catch (SQLException ex) {
            Logger.getLogger(DaoEquipo.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    @Override
    public List<Equipo> consultar(String orden) {
        if (orden.isEmpty()) {
            orden = "id_equipo";
        }
        try {
            miCadenaSQL = "SELECT * FROM equipos ORDER BY " + orden;
            miConsulta = miObjConexion.prepareStatement(miCadenaSQL);
            misRegistros = miConsulta.executeQuery();

            List<Equipo> arrEquipos = new ArrayList<>();

            while (misRegistros.next()) {
                int idEquipo = misRegistros.getInt("id_equipo");
                String codInventario = misRegistros.getString("cod_inventario_equipo");
                String modelo = misRegistros.getString("modelo_equipo");
                String serie = misRegistros.getString("serie_equipo");
                String mantenimiento = misRegistros.getString("mantenimiento_equipo");
                String reqMantenimiento = misRegistros.getString("req_mante_equipo");
                String estado = misRegistros.getString("estado_equipo");
                String nombre = misRegistros.getString("nombre_equipo");
                String marca = misRegistros.getString("marca_equipo");
                boolean disponibilidad = misRegistros.getBoolean("disponibilidad_equipo");
                int existencias = misRegistros.getInt("existencias_equipo");

                Equipo objEquipo = new Equipo(idEquipo, codInventario, modelo, serie, mantenimiento, reqMantenimiento, estado, nombre, marca, disponibilidad, existencias);
                arrEquipos.add(objEquipo);

            }
            miObjConexion.close();
            return arrEquipos;
        } catch (SQLException ex) {
            Logger.getLogger(DaoEquipo.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public Equipo buscar(Integer llavePrimaria) {
        try {
            miCadenaSQL = "SELECT * FROM equipos WHERE id_equipo = ?";
            miConsulta = miObjConexion.prepareStatement(miCadenaSQL);
            miConsulta.setInt(1, llavePrimaria);
            misRegistros = miConsulta.executeQuery();

            Equipo objEncontrado = null;

            if (misRegistros.next()) {
                int idequipo = misRegistros.getInt("id_equipo");
                String codInventario = misRegistros.getString("cod_inventario_equipo");
                String modelo = misRegistros.getString("modelo_equipo");
                String serie = misRegistros.getString("serie_equipo");
                String mantenimiento = misRegistros.getString("mantenimiento_equipo");
                String reqMante = misRegistros.getString("req_mante_equipo");
                String estado = misRegistros.getString("estado_equipo");
                String nombre = misRegistros.getString("nombre_equipo");
                String marca = misRegistros.getString("marca_equipo");
                Boolean disponibilidad = misRegistros.getBoolean("disponibilidad_equipo");
                int existenciaEquipo = misRegistros.getInt("existencias_equipo");

                objEncontrado = new Equipo(idequipo, codInventario, modelo, serie, mantenimiento, reqMante, estado, nombre, marca, disponibilidad, existenciaEquipo);
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
            miCadenaSQL = "DELETE FROM equipos WHERE id_equipo=?";

            miConsulta = miObjConexion.prepareStatement(miCadenaSQL);

            miConsulta.setInt(1, llavePrimaria);
            miCantidad = miConsulta.executeUpdate();

            miObjConexion.close();
            return miCantidad > 0;
        } catch (SQLException ex) {
            Logger.getLogger(DaoEquipo.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    @Override
    public Boolean actualizar(Equipo elObjeto) {
        try {
            miCadenaSQL = "UPDATE equipos SET cod_inventario_equipo = ?, "
                    + "modelo_equipo = ?, serie_equipo = ?, mantenimiento_equipo = ?, "
                    + "req_mante_equipo = ?, estado_equipo = ?, nombre_equipo = ?, marca_equipo = ?, "
                    + "disponibilidad_equipo = ?, existencias_equipo = ? WHERE id_equipo = ?";

            miConsulta = miObjConexion.prepareStatement(miCadenaSQL);

            miConsulta.setString(1, elObjeto.getCod_inventario_equipo());
            miConsulta.setString(2, elObjeto.getModelo_equipo());
            miConsulta.setString(3, elObjeto.getSerie_equipo());
            miConsulta.setString(4, elObjeto.getMantenimiento_equipo());
            miConsulta.setString(5, elObjeto.getReq_mante_equipo());
            miConsulta.setString(6, elObjeto.getEstado_equipo());
            miConsulta.setString(7, elObjeto.getNombre_equipo());
            miConsulta.setString(8, elObjeto.getMarca_equipo());
            miConsulta.setBoolean(9, elObjeto.getDisponibilidad_equipo());
            miConsulta.setInt(10, elObjeto.getExistencias_equipo());
            miConsulta.setInt(11, elObjeto.getId_equipo());

            miCantidad = miConsulta.executeUpdate();

            miObjConexion.close();
            return miCantidad > 0;
        } catch (SQLException ex) {
            Logger.getLogger(DaoEquipo.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    @Override
    public List<Equipo> buscarDato(String dato, String campo) {
        try {
            miCadenaSQL = "SELECT * FROM equipos WHERE " + campo + " = ?";
            System.out.println("cadena dao:" + miCadenaSQL);

            miConsulta = miObjConexion.prepareStatement(miCadenaSQL);
            miConsulta.setString(1, "%" + dato + "%"); // Agregar comodines para la búsqueda con LIKE
            misRegistros = miConsulta.executeQuery();

            List<Equipo> arrEquipo = new ArrayList<>();

            while (misRegistros.next()) {

                int idequipo = misRegistros.getInt("id_equipo");
                String codInventario = misRegistros.getString("cod_inventario_equipo");
                String nombre = misRegistros.getString("nombre_equipo");
                String modelo = misRegistros.getString("modelo_equipo");
                String serie = misRegistros.getString("serie_equipo");
                String mantenimiento = misRegistros.getString("mantenimiento_equipo");
                String reqMante = misRegistros.getString("req_mante_equipo");
                String estado = misRegistros.getString("estado_equipo");
                String marca = misRegistros.getString("marca_equipo");
                Boolean disponibilidad = misRegistros.getBoolean("disponibilidad_equipo");
                int existenciaEquipo = misRegistros.getInt("existencias_equipo");
                System.out.println("xxxxxxxx" + idequipo);

                Equipo objEquipo = new Equipo(idequipo, codInventario, nombre, modelo, serie, mantenimiento, reqMante, estado, marca, disponibilidad, existenciaEquipo);

                arrEquipo.add(objEquipo);
            }

            miObjConexion.close();
            return arrEquipo;

        } catch (SQLException ex) {
            Logger.getLogger(DaoEquipo.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public Integer buscarId(String dato) {
        int idequipo = 0;
        try {
            System.out.println("DATOO"+dato);
            miCadenaSQL = "SELECT id_equipo FROM equipos WHERE nombre_equipo = ?";
            System.out.println("cadena dao:" + miCadenaSQL);

            miConsulta = miObjConexion.prepareStatement(miCadenaSQL);
            miConsulta.setString(1, dato); // Agregar comodines para la búsqueda con LIKE
            misRegistros = miConsulta.executeQuery();

            while (misRegistros.next()) {

                idequipo = misRegistros.getInt("id_equipo");
                System.out.println("xxxxxxxxDATO:::" + idequipo);

            }

            miObjConexion.close();
            return idequipo;

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
