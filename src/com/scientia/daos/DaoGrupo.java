package com.scientia.daos;

import com.scientia.configuracion.MiConexion;
import com.scientia.entidades.Grupo;
import com.scientia.entidades.GrupoLaboratorio;
import com.scientia.interfaces.Funcionalidad;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

public class DaoGrupo extends MiConexion implements Funcionalidad<Grupo> {

    @Override
    public Boolean registrar(Grupo elObjeto) {
        try {
            miCadenaSQL = "INSERT INTO grupos(id_grupo_laboratorio) "
                    + " VALUES (?)";

            miConsulta = miObjConexion.prepareStatement(miCadenaSQL);

            miConsulta.setInt(1, elObjeto.getIdGrupoLaboratorio().getIdGrupoLaboratorio());

            miCantidad = miConsulta.executeUpdate();
            miObjConexion.close();
            return miCantidad > 0;

        } catch (SQLException ex) {
            Logger.getLogger(DaoGrupo.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    @Override
    public List<Grupo> consultar(String orden) {
        try {
            if (orden.isEmpty()) {
                orden = "g.id_grupo";
            }
            miCadenaSQL = "SELECT g.id_grupo, gl.id_grupo_laboratorio, gl.nombre_laboratorio "
                    + "FROM grupos g INNER JOIN grupos_laboratorios gl "
                    + "ON g.id_grupo_laboratorio = gl.id_grupo_laboratorio "
                    + "ORDER BY " + orden;

            miConsulta = miObjConexion.prepareStatement(miCadenaSQL);

            misRegistros = miConsulta.executeQuery();
            List<Grupo> arrGrupos = new ArrayList<>();

            while (misRegistros.next()) {
                int idGrupo = misRegistros.getInt(1);
                int idGrupoLab = misRegistros.getInt(2);
                String nomGrupoLab = misRegistros.getString(3);

                GrupoLaboratorio objGrupoLaboratorio = new GrupoLaboratorio(idGrupoLab, nomGrupoLab, "");
                Grupo objGrupo = new Grupo(idGrupo, objGrupoLaboratorio);

                arrGrupos.add(objGrupo);
            }

            miObjConexion.close();
            return arrGrupos;

        } catch (SQLException ex) {
            Logger.getLogger(DaoGrupo.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }

    @Override
    public Grupo buscar(Integer llavePrimaria) {
        try {
            miCadenaSQL = "SELECT g.id_grupo, gl.id_grupo_laboratorio, gl.nombre_laboratorio "
                    + "FROM grupos g INNER JOIN grupos_laboratorios gl "
                    + "ON g.id_grupo_laboratorio = gl.id_grupo_laboratorio "
                    + "WHERE g.id_grupo=?";

            miConsulta = miObjConexion.prepareStatement(miCadenaSQL);
            miConsulta.setInt(1, llavePrimaria);

            misRegistros = miConsulta.executeQuery();
            Grupo objEncontrada = null;

            if (misRegistros.next()) {
                int idGrupo = misRegistros.getInt(1);

                int idGrupoLab = misRegistros.getInt(2);
                String nomGrupoLab = misRegistros.getString(3);

                GrupoLaboratorio objGrupoLaboratorio = new GrupoLaboratorio(idGrupoLab, nomGrupoLab, "");
                objEncontrada = new Grupo(idGrupo, objGrupoLaboratorio);
            }

            miObjConexion.close();
            return objEncontrada;

        } catch (SQLException ex) {
            Logger.getLogger(DaoGrupo.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }

    @Override
    public Boolean eliminar(Integer llavePrimaria) {
        try {
            miCadenaSQL = "DELETE FROM grupos WHERE id_grupo = ? ";

            miConsulta = miObjConexion.prepareStatement(miCadenaSQL);
            miConsulta.setInt(1, llavePrimaria);

            miCantidad = miConsulta.executeUpdate();

            miObjConexion.close();
            return miCantidad > 0;
        } catch (SQLException ex) {
            Logger.getLogger(DaoGrupo.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    @Override
    public Boolean actualizar(Grupo elObjeto) {
        try {
            miCadenaSQL = "UPDATE grupos SET "
                    + "id_grupo_laboratorio = ? "
                    + "WHERE id_grupo = ? ";

            miConsulta = miObjConexion.prepareStatement(miCadenaSQL);
            miConsulta.setInt(1, elObjeto.getIdGrupoLaboratorio().getIdGrupoLaboratorio());
            miConsulta.setInt(2, elObjeto.getIdGrupo());

            miCantidad = miConsulta.executeUpdate();

            miObjConexion.close();
            return miCantidad > 0;
        } catch (SQLException ex) {
            Logger.getLogger(DaoGrupo.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    @Override
    public List<Grupo> buscarDato(String dato, String campo) {
        //DEJEN LA CONSULTA DESPUES DEL WHERE EXACTAMENTE IGUAL
        try {
            miCadenaSQL = "SELECT g.id_grupo, gl.id_grupo_laboratorio, gl.nombre_laboratorio "
                    + "FROM grupos g INNER JOIN grupos_laboratorios gl "
                    + "ON g.id_grupo_laboratorio = gl.id_grupo_laboratorio "
                    + "WHERE " + campo + " LIKE ? ";
            System.out.println("cadena dao:" + miCadenaSQL);

            miConsulta = miObjConexion.prepareStatement(miCadenaSQL);
            miConsulta.setString(1, dato);
            misRegistros = miConsulta.executeQuery();
            List<Grupo> arrGrupos = new ArrayList<>();

            while (misRegistros.next()) {
                int idGrupo = misRegistros.getInt(1);
                int idGrupoLab = misRegistros.getInt(2);
                String nomGrupoLab = misRegistros.getString(3);

                GrupoLaboratorio objGrupoLaboratorio = new GrupoLaboratorio(idGrupoLab, nomGrupoLab, "");
                Grupo objGrupo = new Grupo(idGrupo, objGrupoLaboratorio);

                arrGrupos.add(objGrupo);
            }

            miObjConexion.close();
            return arrGrupos;

        } catch (SQLException ex) {
            Logger.getLogger(DaoGrupo.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public Integer buscarUltimoIdGrupo() {
        try {
            miCadenaSQL = "SELECT id_grupo FROM grupos ORDER BY id_grupo DESC LIMIT 1";

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
