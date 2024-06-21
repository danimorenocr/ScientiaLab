package com.scientia.daos;

import com.scientia.configuracion.MiConexion;
import com.scientia.entidades.GrupoLaboratorio;
import com.scientia.interfaces.Funcionalidad;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

public class DaoGrupoLaboratorio extends MiConexion implements Funcionalidad<GrupoLaboratorio> {

    @Override
    public Boolean registrar(GrupoLaboratorio elObjeto) {
        try {
            miCadenaSQL = "INSERT INTO grupos_laboratorios (nombre_laboratorio, desc_laboratorio) VALUES (?, ?)";
            miConsulta = miObjConexion.prepareStatement(miCadenaSQL);

            miConsulta.setString(1, elObjeto.getNombreLaboratorio());
            miConsulta.setString(2, elObjeto.getDescLaboratorio());

            miCantidad = miConsulta.executeUpdate();
            miObjConexion.close();
            return miCantidad > 0;
        } catch (SQLException ex) {
            Logger.getLogger(DaoGrupoLaboratorio.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    @Override
    public List<GrupoLaboratorio> consultar(String orden) {
        if (orden.isEmpty()) {
            orden = "id_grupo_laboratorio";
        }
        try {
            miCadenaSQL = "SELECT * FROM grupos_laboratorios ORDER BY " + orden;
            miConsulta = miObjConexion.prepareStatement(miCadenaSQL);
            misRegistros = miConsulta.executeQuery();

            List<GrupoLaboratorio> arrGrupoLaboratorio = new ArrayList<>();

            while (misRegistros.next()) {
                int idGrupoLaboratorio = misRegistros.getInt(1);
                String nombre = misRegistros.getString(2);
                String descripcion = misRegistros.getString(3);

                GrupoLaboratorio objGrupoLab = new GrupoLaboratorio(idGrupoLaboratorio, nombre, descripcion);
                arrGrupoLaboratorio.add(objGrupoLab);

            }
            miObjConexion.close();
            return arrGrupoLaboratorio;
        } catch (SQLException ex) {
            Logger.getLogger(DaoGrupoLaboratorio.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }

    @Override
    public GrupoLaboratorio buscar(Integer llavePrimaria) {

        try {
            miCadenaSQL = "SELECT * FROM grupos_laboratorios WHERE id_grupo_laboratorio = ?";

            miConsulta = miObjConexion.prepareStatement(miCadenaSQL);
            miConsulta.setInt(1, llavePrimaria);
            misRegistros = miConsulta.executeQuery();

            GrupoLaboratorio objEncontrado = null;

            if (misRegistros.next()) {

                int idGrupoLaboratorio = misRegistros.getInt(1);
                String nombre = misRegistros.getString(2);
                String descripcion = misRegistros.getString(3);

                objEncontrado = new GrupoLaboratorio(idGrupoLaboratorio, nombre, descripcion);

            }
            miObjConexion.close();
            return objEncontrado;

        } catch (SQLException ex) {
            Logger.getLogger(DaoGrupoLaboratorio.class.getName()).log(Level.SEVERE, null, ex);

            return null;
        }

    }

    @Override
    public Boolean eliminar(Integer llavePrimaria) {
        try {
            miCadenaSQL = "DELETE FROM grupos_laboratorios WHERE id_grupo_laboratorio=?";

            miConsulta = miObjConexion.prepareStatement(miCadenaSQL);

            miConsulta.setInt(1, llavePrimaria);
            miCantidad = miConsulta.executeUpdate();

            miObjConexion.close();
            return miCantidad > 0;
        } catch (SQLException ex) {
            Logger.getLogger(DaoGrupoLaboratorio.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    @Override
    public Boolean actualizar(GrupoLaboratorio elObjeto) {
        try {
            miCadenaSQL = "UPDATE grupos_laboratorios SET nombre_laboratorio = ?, "
                    + "desc_laboratorio = ? WHERE id_grupo_laboratorio = ?";

            miConsulta = miObjConexion.prepareStatement(miCadenaSQL);

            miConsulta.setString(1, elObjeto.getNombreLaboratorio());
            miConsulta.setString(2, elObjeto.getDescLaboratorio());
            miConsulta.setInt(3, elObjeto.getIdGrupoLaboratorio());

            miCantidad = miConsulta.executeUpdate();

            miObjConexion.close();
            return miCantidad > 0;
        } catch (SQLException ex) {
            Logger.getLogger(DaoGrupoLaboratorio.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

    }

    @Override
    public List<GrupoLaboratorio> buscarDato(String dato, String campo) {
        //DEJEN LA CONSULTA DESPUES DEL WHERE EXACTAMENTE IGUAL
        try {
            miCadenaSQL = "SELECT id_grupo_laboratorio, nombre_laboratorio, "
                    + "desc_laboratorio "
                    + "FROM grupos_laboratorios "
                    + "WHERE " + campo + " LIKE ? ";
            System.out.println("cadena dao:" + miCadenaSQL);

            miConsulta = miObjConexion.prepareStatement(miCadenaSQL);
            miConsulta.setString(1, dato);
            misRegistros = miConsulta.executeQuery();

            List<GrupoLaboratorio> arrGrupoLab = new ArrayList<>();

            while (misRegistros.next()) {

                int idGrupoLab = misRegistros.getInt(1);
                String nombre = misRegistros.getString(2);
                String descripcion = misRegistros.getString(3);

                //LLAMEN A LA ENTIDAD
                GrupoLaboratorio objGrupoLaboratorio = new GrupoLaboratorio(idGrupoLab, nombre, descripcion);
                arrGrupoLab.add(objGrupoLaboratorio);
            }

            miObjConexion.close();
            return arrGrupoLab;

        } catch (SQLException ex) {
            Logger.getLogger(DaoGrupoLaboratorio.class.getName()).log(Level.SEVERE, null, ex);
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
