package com.scientia.daos;

import com.scientia.configuracion.MiConexion;
import com.scientia.entidades.Accesorio;
import com.scientia.entidades.Elemento;
import com.scientia.entidades.Estudiante;
import com.scientia.interfaces.Funcionalidad;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author POWER
 */
public class DaoAccesorio extends MiConexion implements Funcionalidad<Accesorio> {

    @Override
    public Boolean registrar(Accesorio elObjeto) {
        try {
            miCadenaSQL = "INSERT INTO accesorios (cod_inventario_accesorio, nombre_accesorio, referencia_accesorio, cantidad_accesorio) "
                    + "VALUES (?, ?, ?, ?)";

            miConsulta = miObjConexion.prepareStatement(miCadenaSQL);

            // Manejo de List<String>
            List<String> codigosElemento = elObjeto.getCodInventarioAccesorio();
            String codigosElementoString = String.join(";", codigosElemento);

            miConsulta.setString(1, codigosElementoString);
            // ***  

            miConsulta.setString(2, elObjeto.getNombreAccesorio());
            miConsulta.setString(3, elObjeto.getReferenciaAccesorio());
            miConsulta.setInt(4, elObjeto.getCantidadAccesorio());

            miCantidad = miConsulta.executeUpdate();
            miObjConexion.close();
            return miCantidad > 0;

        } catch (SQLException ex) {
            Logger.getLogger(DaoAccesorio.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    @Override
    public List<Accesorio> consultar(String orden) {
        if (orden.isEmpty()) {
            orden = "id_accesorio";
        }

        try {
            miCadenaSQL = "SELECT a.id_accesorio, a.cod_inventario_accesorio, a.nombre_accesorio, a.referencia_accesorio, a.cantidad_accesorio, "
                    + "e.id_elemento, e.nombre_elemento "
                    + "FROM accesorios a "
                    + "LEFT JOIN elementos e ON a.id_elemento = e.id_elemento "
                    + "ORDER BY " + orden;

            miConsulta = miObjConexion.prepareStatement(miCadenaSQL);
            misRegistros = miConsulta.executeQuery();

            List<Accesorio> arrAccesorio = new ArrayList<>();

            while (misRegistros.next()) {

                int idAcce = misRegistros.getInt(1);
                String codInvAcce = misRegistros.getString(2);
                String nomAcce = misRegistros.getString(3);
                String referenciaAcce = misRegistros.getString(4);
                int cantAcce = misRegistros.getInt(5);

                int idElem = misRegistros.getInt(6);
                String nomElemen = misRegistros.getString(7);

                List<String> listCodInv = Arrays.asList(codInvAcce.split(";"));

                Elemento objElem = new Elemento(null, null, null, null, null, null, null, idElem, nomElemen, null, null, null);

                Accesorio objAcc = new Accesorio(idAcce, listCodInv, nomAcce, referenciaAcce, cantAcce, objElem);

                arrAccesorio.add(objAcc);
            }
            miObjConexion.close();
            return arrAccesorio;
        } catch (SQLException ex) {
            Logger.getLogger(DaoAccesorio.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public Accesorio buscar(Integer llavePrimaria) {
        try {
            miCadenaSQL = "SELECT a.id_accesorio, a.cod_inventario_accesorio, a.nombre_accesorio, a.referencia_accesorio, a.cantidad_accesorio, "
                    + "e.id_elemento, e.nombre_elemento "
                    + "FROM accesorios a "
                    + "LEFT JOIN elementos e ON a.id_elemento = e.id_elemento "
                    + "WHERE a.id_accesorio = ?";

            miConsulta = miObjConexion.prepareStatement(miCadenaSQL);
            miConsulta.setInt(1, llavePrimaria);
            misRegistros = miConsulta.executeQuery();

            Accesorio objEncontrado = null;

            if (misRegistros.next()) {

                int idAcc = misRegistros.getInt(1);
                String codInvAcce = misRegistros.getString(2);
                String nomAcc = misRegistros.getString(3);
                String refAcc = misRegistros.getString(4);
                int cantAcc = misRegistros.getInt(5);

                int idElem = misRegistros.getInt(6);
                String nomElem = misRegistros.getString(7);

                List<String> listCodInv = Arrays.asList(codInvAcce.split(";"));

                Elemento objElem = new Elemento(null, null, null, null, null, null, null, idElem, nomElem, null, null, null);
                objEncontrado = new Accesorio(idAcc, listCodInv, nomAcc, refAcc, cantAcc, objElem);

            }

            miObjConexion.close();
            return objEncontrado;

        } catch (SQLException ex) {
            Logger.getLogger(DaoAccesorio.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public Boolean eliminar(Integer llavePrimaria) {

        try {
            miCadenaSQL = "DELETE FROM accesorios WHERE id_accesorio = ?";

            miConsulta = miObjConexion.prepareStatement(miCadenaSQL);
            miConsulta.setInt(1, llavePrimaria);

            miCantidad = miConsulta.executeUpdate();

            miObjConexion.close();
            return miCantidad > 0;
        } catch (SQLException ex) {
            Logger.getLogger(DaoAccesorio.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

    }

    @Override
    public Boolean actualizar(Accesorio elObjeto) {
        try {
            miCadenaSQL = "UPDATE accesorios a "
                    + "LEFT JOIN elementos e ON a.id_elemento = e.id_elemento "
                    + "SET a.cod_inventario_accesorio = ?, a.nombre_accesorio = ?, "
                    + "a.referencia_accesorio = ?, a.cantidad_accesorio = ?, "
                    + "a.id_elemento = ? "
                    + "WHERE id_accesorio = ?";

            miConsulta = miObjConexion.prepareStatement(miCadenaSQL);

            // Manejo de List<String>
            List<String> codigosElemento = elObjeto.getCodInventarioAccesorio();
            String codigosElementoString = String.join(";", codigosElemento);

            miConsulta.setString(1, codigosElementoString);
            // ***  

            miConsulta.setString(2, elObjeto.getNombreAccesorio());
            miConsulta.setString(3, elObjeto.getReferenciaAccesorio());
            miConsulta.setInt(4, elObjeto.getCantidadAccesorio());
            miConsulta.setInt(5, elObjeto.getElemento().getId());

            miConsulta.setInt(6, elObjeto.getIdAccesorio());

            System.out.println("CadenaSQL: " + miConsulta);
            miCantidad = miConsulta.executeUpdate();

            miObjConexion.close();
            return miCantidad > 0;
        } catch (SQLException ex) {
            Logger.getLogger(DaoAccesorio.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean actualizarAccesorio(Accesorio accesorio) {
        try {
            miCadenaSQL = "UPDATE accesorios SET id_elemento = ? WHERE id_accesorio = ?";
            miConsulta = miObjConexion.prepareStatement(miCadenaSQL);
            miConsulta.setObject(1, accesorio.getElemento().getId(), java.sql.Types.INTEGER);
            miConsulta.setInt(2, accesorio.getIdAccesorio());

            miCantidad = miConsulta.executeUpdate();

            miObjConexion.close();
            return miCantidad > 0;

        } catch (SQLException e) {
            Logger.getLogger(DaoAccesorio.class.getName()).log(Level.SEVERE, null, e);
            return false;
        }
    }

    @Override
    public List<Accesorio> buscarDato(String dato, String campo) {
        try {

            miCadenaSQL = "SELECT a.id_accesorio, a.cod_inventario_accesorio, a.nombre_accesorio, a.referencia_accesorio, a.cantidad_accesorio, "
                    + "e.id_elemento, e.nombre_elemento "
                    + "FROM accesorios a "
                    + "LEFT JOIN elementos e ON a.id_elemento = e.id_elemento "
                    + "WHERE " + campo + " LIKE ?";

            System.out.println("cadena dao:" + miCadenaSQL);

            miConsulta = miObjConexion.prepareStatement(miCadenaSQL);
            miConsulta.setString(1, dato);
            misRegistros = miConsulta.executeQuery();

            List<Accesorio> arrAccesorios = new ArrayList<>();

            while (misRegistros.next()) {

                int idAcc = misRegistros.getInt(1);
                String codInvAcce = misRegistros.getString(2);
                String nomAcc = misRegistros.getString(3);
                String refAcc = misRegistros.getString(4);
                int cantAcc = misRegistros.getInt(5);

                int idElem = misRegistros.getInt(6);
                String nomElem = misRegistros.getString(7);

                List<String> listCodInv = Arrays.asList(codInvAcce.split(";"));

                Elemento objElem = new Elemento(null, null, null, null, null, null, null, idElem, nomElem, null, null, null);

                Accesorio objAccesorio = new Accesorio(idAcc, listCodInv, nomAcc, refAcc, cantAcc, objElem);
                arrAccesorios.add(objAccesorio);
            }

            miObjConexion.close();
            return arrAccesorios;

        } catch (SQLException ex) {

            Logger.getLogger(DaoElemento.class.getName()).log(Level.SEVERE, null, ex);
            return null;

        }
    }

    public List<Accesorio> consultarNombresPorElem(Integer codElemento) {
        List<Accesorio> accesoriosEncontrados = new ArrayList<>();

        try {
            miCadenaSQL = "SELECT id_accesorio, nombre_accesorio FROM accesorios WHERE id_elemento = ?";
            miConsulta = miObjConexion.prepareStatement(miCadenaSQL);
            miConsulta.setInt(1, codElemento);

            misRegistros = miConsulta.executeQuery();

            while (misRegistros.next()) {
                int idAccesorio = misRegistros.getInt("id_accesorio");
                String nombreAccesorio = misRegistros.getString("nombre_accesorio");

                Elemento elemento = new Elemento(null, null, false, "", null, null, 0, codElemento, "", "", false, 0);
                Accesorio accesorio = new Accesorio(idAccesorio, null, nombreAccesorio, "", 0, elemento);
                accesoriosEncontrados.add(accesorio);
            }

        } catch (SQLException ex) {
            Logger.getLogger(DaoEstudiante.class.getName()).log(Level.SEVERE, null, ex);
        }

        return accesoriosEncontrados;
    }

    public List<Integer> consultarIdsPorElem(Integer idElemento) {
        List<Integer> accesoriosIds = new ArrayList<>();

        try {
            miCadenaSQL = "SELECT id_accesorio FROM accesorios WHERE id_elemento = ?";
            miConsulta = miObjConexion.prepareStatement(miCadenaSQL);
            miConsulta.setInt(1, idElemento);

            misRegistros = miConsulta.executeQuery();

            while (misRegistros.next()) {
                int idAccesorio = misRegistros.getInt("id_accesorio");
                accesoriosIds.add(idAccesorio);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DaoEstudiante.class.getName()).log(Level.SEVERE, null, ex);
        }

        return accesoriosIds;
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
