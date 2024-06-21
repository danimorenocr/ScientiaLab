package com.scientia.daos;

import com.scientia.configuracion.MiConexion;
import com.scientia.entidades.Elemento;
import com.scientia.interfaces.Funcionalidad;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

public class DaoElemento extends MiConexion implements Funcionalidad<Elemento> {

    @Override
    public Boolean registrar(Elemento elObjeto) {
        try {
            miCadenaSQL = "INSERT INTO elementos (nombre_elemento, marca_elemento, disponibilidad_elemento, existencias_elemento, cod_inventario_elemento, foto_elemento, manual_elemento, acta_elemento, costo_elemento, fecha_compra_elemento) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            miConsulta = miObjConexion.prepareStatement(miCadenaSQL);

            miConsulta.setString(1, elObjeto.getNombre());
            miConsulta.setString(2, elObjeto.getMarca());
            miConsulta.setBoolean(3, elObjeto.getDisponibilidad());
            miConsulta.setInt(4, elObjeto.getExistencias());

            // Manejo de List<String>
            List<String> codigosElemento = elObjeto.getCodInventarioElemento();
            String codigosElementoString = String.join(";", codigosElemento);

            miConsulta.setString(5, codigosElementoString);
            // ***  

            miConsulta.setBlob(6, elObjeto.getFotoElemento());
            miConsulta.setBoolean(7, elObjeto.getManualElemento());
            miConsulta.setString(8, elObjeto.getActaElemento());
            miConsulta.setDouble(9, elObjeto.getCostoElemento());

            // Fecha
            long miliSegundos = elObjeto.getFechaCompraElemento().getTime();
            Date fechaLista = new Date(miliSegundos);
            miConsulta.setDate(10, fechaLista);
            // ***

            miCantidad = miConsulta.executeUpdate();
            miObjConexion.close();
            return miCantidad > 0;

        } catch (SQLException ex) {
            Logger.getLogger(DaoElemento.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    @Override
    public List<Elemento> consultar(String orden) {
        if (orden.isEmpty()) {
            orden = "e.id_elemento";
        }
        try {
            miCadenaSQL = "SELECT e.id_elemento, e.nombre_elemento, e.marca_elemento, e.disponibilidad_elemento, e.existencias_elemento, "
                    + "e.cod_inventario_elemento, e.foto_elemento, e.manual_elemento, e.acta_elemento, e.costo_elemento, e.fecha_compra_elemento, "
                    + "(SELECT COUNT(a.id_accesorio) FROM accesorios a WHERE a.id_elemento = e.id_elemento) AS cantAccesorios "
                    + "FROM elementos e "
                    + "ORDER BY " + orden;

            miConsulta = miObjConexion.prepareStatement(miCadenaSQL);
            misRegistros = miConsulta.executeQuery();

            List<Elemento> arrElemento = new ArrayList<>();

            System.out.println("Cadena SQL: " + miCadenaSQL);

            System.out.println("MIS REGISTROS" + misRegistros);

            while (misRegistros.next()) {

                System.out.println("Se hizo el while");

                int idElem = misRegistros.getInt(1);
                String nomEleme = misRegistros.getString(2);
                String marcaElem = misRegistros.getString(3);
                boolean disponibilidadElem = misRegistros.getBoolean(4);
                int existenciaElem = misRegistros.getInt(5);
                String codInventarioElem = misRegistros.getString(6);
                InputStream fotoElem = misRegistros.getBlob(7).getBinaryStream();
                boolean manualElem = misRegistros.getBoolean(8);
                String actaElem = misRegistros.getString(9);
                Double costoElem = misRegistros.getDouble(10);
                Date fechaElem = misRegistros.getDate(11);
                int cantAccesorios = misRegistros.getInt(12);

                List<String> listaCodigos = Arrays.asList(codInventarioElem.split(";"));

                Elemento objElemento = new Elemento(listaCodigos, fotoElem, manualElem, actaElem, costoElem, fechaElem, cantAccesorios, idElem, nomEleme, marcaElem, disponibilidadElem, existenciaElem);

                arrElemento.add(objElemento);
            }

            miObjConexion.close();
            return arrElemento;

        } catch (SQLException ex) {
            Logger.getLogger(DaoElemento.class.getName()).log(Level.SEVERE, null, ex);
            return null;

        }
    }

    @Override
    public Elemento buscar(Integer llavePrimaria) {
        try {
            miCadenaSQL = "SELECT e.id_elemento, e.nombre_elemento, e.marca_elemento, e.disponibilidad_elemento, e.existencias_elemento, "
                    + "e.cod_inventario_elemento, e.foto_elemento, e.manual_elemento, e.acta_elemento, e.costo_elemento, e.fecha_compra_elemento "
                    + "FROM elementos e "
                    + "WHERE e.id_elemento = ?";

            miConsulta = miObjConexion.prepareStatement(miCadenaSQL);
            miConsulta.setInt(1, llavePrimaria);
            misRegistros = miConsulta.executeQuery();

            Elemento objEncontrado = null;

            if (misRegistros.next()) {
                int idElem = misRegistros.getInt(1);
                String nomEleme = misRegistros.getString(2);
                String marcaElem = misRegistros.getString(3);
                boolean disponibilidadElem = misRegistros.getBoolean(4);
                int existenciaElem = misRegistros.getInt(5);
                String codInventarioElem = misRegistros.getString(6);
                InputStream fotoElem = misRegistros.getBlob(7).getBinaryStream();
                boolean manualElem = misRegistros.getBoolean(8);
                String actaElem = misRegistros.getString(9);
                Double costoElem = misRegistros.getDouble(10);
                Date fechaElem = misRegistros.getDate(11);

                List<String> listaCodigos = Arrays.asList(codInventarioElem.split(";"));

                objEncontrado = new Elemento(listaCodigos, fotoElem, manualElem, actaElem, costoElem, fechaElem, 0, idElem, nomEleme, marcaElem, disponibilidadElem, existenciaElem);

            }

            miObjConexion.close();
            return objEncontrado;

        } catch (SQLException ex) {
            Logger.getLogger(DaoElemento.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public Boolean eliminar(Integer llavePrimaria) {
        try {
            miCadenaSQL = "DELETE FROM elementos WHERE id_elemento = ?";

            miConsulta = miObjConexion.prepareStatement(miCadenaSQL);
            miConsulta.setInt(1, llavePrimaria);

            miCantidad = miConsulta.executeUpdate();

            miObjConexion.close();
            return miCantidad > 0;
        } catch (SQLException ex) {
            Logger.getLogger(DaoElemento.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    @Override
    public Boolean actualizar(Elemento elObjeto) {
        try {
            miCadenaSQL = "UPDATE elementos e SET "
                    + "e.nombre_elemento = ?, e.marca_elemento = ?, e.disponibilidad_elemento = ?, e.existencias_elemento = ?, "
                    + "e.cod_inventario_elemento = ?, e.foto_elemento = ?, e.manual_elemento = ?, e.acta_elemento = ?, e.costo_elemento = ?, e.fecha_compra_elemento = ? "
                    + "WHERE id_elemento = ?";

            miConsulta = miObjConexion.prepareStatement(miCadenaSQL);

            miConsulta.setString(1, elObjeto.getNombre());
            miConsulta.setString(2, elObjeto.getMarca());
            miConsulta.setBoolean(3, elObjeto.getDisponibilidad());
            miConsulta.setInt(4, elObjeto.getExistencias());

            // Manejo de List<String>
            List<String> codigosElemento = elObjeto.getCodInventarioElemento();
            String codigosElementoString = String.join(";", codigosElemento);

            miConsulta.setString(5, codigosElementoString);
            // ***  

            miConsulta.setBlob(6, elObjeto.getFotoElemento());
            miConsulta.setBoolean(7, elObjeto.getManualElemento());
            miConsulta.setString(8, elObjeto.getActaElemento());
            miConsulta.setDouble(9, elObjeto.getCostoElemento());

            // Fecha
            long miliSegundos = elObjeto.getFechaCompraElemento().getTime();
            Date fechaLista = new Date(miliSegundos);
            miConsulta.setDate(10, fechaLista);
            // ***

            miConsulta.setInt(11, elObjeto.getId());

            miCantidad = miConsulta.executeUpdate();

            miObjConexion.close();
            return miCantidad > 0;

        } catch (SQLException ex) {
            Logger.getLogger(DaoElemento.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    @Override
    public List<Elemento> buscarDato(String dato, String campo) {

        try {
            miCadenaSQL = "SELECT e.id_elemento, e.nombre_elemento, e.marca_elemento, e.disponibilidad_elemento, e.existencias_elemento, "
                    + "e.cod_inventario_elemento, e.foto_elemento, e.manual_elemento, e.acta_elemento, e.costo_elemento, e.fecha_compra_elemento, "
                    + "(SELECT COUNT(a.id_accesorio) FROM accesorios a WHERE a.id_elemento = e.id_elemento) AS cantAccesorios "
                    + "FROM elementos e "
                    + "WHERE " + campo + " = ? ";

            miConsulta = miObjConexion.prepareStatement(miCadenaSQL);
            miConsulta.setString(1, dato);

            misRegistros = miConsulta.executeQuery();

            List<Elemento> arrElementos = new ArrayList<>();

            while (misRegistros.next()) {

                int idElem = misRegistros.getInt(1);
                String nomEleme = misRegistros.getString(2);
                String marcaElem = misRegistros.getString(3);
                boolean disponibilidadElem = misRegistros.getBoolean(4);
                int existenciaElem = misRegistros.getInt(5);
                String codInventarioElem = misRegistros.getString(6);
                InputStream fotoElem = misRegistros.getBlob(7).getBinaryStream();
                boolean manualElem = misRegistros.getBoolean(8);
                String actaElem = misRegistros.getString(9);
                Double costoElem = misRegistros.getDouble(10);
                Date fechaElem = misRegistros.getDate(11);
                int cantAccesorios = misRegistros.getInt(12);

                System.out.println("xxxxxxxx" + idElem);

                List<String> listaCodigos = Arrays.asList(codInventarioElem.split(";"));

                Elemento objElemento = new Elemento(listaCodigos, fotoElem, manualElem, actaElem, costoElem, fechaElem, cantAccesorios, idElem, nomEleme, marcaElem, disponibilidadElem, existenciaElem);
                arrElementos.add(objElemento);
            }

            miObjConexion.close();
            return arrElementos;

        } catch (SQLException ex) {
            Logger.getLogger(DaoElemento.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public Integer buscarId(String dato) {
        int idElemento = 0;
        try {
            System.out.println("DATOO" + dato);
            miCadenaSQL = "SELECT id_elemento FROM elementos WHERE nombre_elemento = ?";
            System.out.println("cadena dao:" + miCadenaSQL);

            miConsulta = miObjConexion.prepareStatement(miCadenaSQL);
            miConsulta.setString(1, dato);
            misRegistros = miConsulta.executeQuery();

            while (misRegistros.next()) {

                idElemento = misRegistros.getInt("id_elemento");
                System.out.println("xxxxxxxxDATO:::" + idElemento);

            }

            miObjConexion.close();
            return idElemento;

        } catch (SQLException ex) {
            Logger.getLogger(DaoEquipo.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public Integer buscarUltimoIdElem() {
        try {
            miCadenaSQL = "SELECT id_elemento FROM elementos ORDER BY id_elemento DESC LIMIT 1";

            miConsulta = miObjConexion.prepareStatement(miCadenaSQL);

            misRegistros = miConsulta.executeQuery();
            Integer ultimoIdGrupo = null;

            if (misRegistros.next()) {
                ultimoIdGrupo = misRegistros.getInt(1);
            }

            miObjConexion.close();
            return ultimoIdGrupo;

        } catch (SQLException ex) {
            Logger.getLogger(DaoGrupo.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public String loVoyAGrabarEnUnArchivoEsPeroYa(DefaultTableModel miModeloLista, String rutaAplicacion, String nombreArchivoSalida) {
        try {
            int i, j, numFilas, numColumnas;
            String cadenaTexto, archiSalida;

            numFilas = miModeloLista.getRowCount();
            numColumnas = miModeloLista.getColumnCount() - 2; // Excluir las últimas dos columnas
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
