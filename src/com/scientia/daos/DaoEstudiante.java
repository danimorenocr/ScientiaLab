package com.scientia.daos;

import com.scientia.configuracion.MiConexion;
import com.scientia.entidades.Estudiante;
import com.scientia.interfaces.Funcionalidad;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

public class DaoEstudiante extends MiConexion implements Funcionalidad<Estudiante> {

    @Override
    public Boolean registrar(Estudiante elObjeto) {
        try {

            miCadenaSQL = "INSERT INTO estudiantes "
                    + "(codU_estudiante, nom_estudiante, apellido_estudiante, correo_estudiante, id_grupo) "
                    + "VALUES(?,?,?,?,?)";

            // Preparar la consulta con la nueva cadena SQL
            miConsulta = miObjConexion.prepareStatement(miCadenaSQL, Statement.RETURN_GENERATED_KEYS);
            miConsulta.setString(1, elObjeto.getCodUEstudiante());
            miConsulta.setString(2, elObjeto.getNomEstudiante());
            miConsulta.setString(3, elObjeto.getApellidoEstudiante());
            miConsulta.setString(4, elObjeto.getCorreoEstudiante());

            if (elObjeto.getIdGrupo() != null) {
                miConsulta.setInt(5, elObjeto.getIdGrupo());
            } else {
                miConsulta.setNull(5, java.sql.Types.INTEGER);
            }

            miCantidad = miConsulta.executeUpdate();

            miObjConexion.close();

            return miCantidad > 0;

        } catch (SQLException ex) {

            Logger.getLogger(DaoEstudiante.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

    }

    @Override
    public List<Estudiante> consultar(String orden) {
        try {

            if (orden.isEmpty()) {
                orden = "id_estudiante";
            }

            miCadenaSQL = "SELECT id_estudiante, codU_estudiante, nom_estudiante, apellido_estudiante, correo_estudiante, id_grupo "
                    + "FROM estudiantes ORDER BY " + orden;

            miConsulta = miObjConexion.prepareStatement(miCadenaSQL);
            misRegistros = miConsulta.executeQuery();

            List<Estudiante> arrEstudiantes = new ArrayList<>();
            while (misRegistros.next()) {
                int idEstudiante = misRegistros.getInt("id_estudiante");
                String codUEstudiante = misRegistros.getString("codU_estudiante");
                String nomEstudiante = misRegistros.getString("nom_estudiante");
                String apellidoEstudiante = misRegistros.getString("apellido_estudiante");
                String correoEstudiante = misRegistros.getString("correo_estudiante");
                int idGrupo = misRegistros.getInt("id_grupo");

                Estudiante estudiante = new Estudiante(idEstudiante, codUEstudiante, nomEstudiante, apellidoEstudiante, correoEstudiante, idGrupo);
                arrEstudiantes.add(estudiante);
            }

            miObjConexion.close();

            return arrEstudiantes;
        } catch (SQLException ex) {

            Logger.getLogger(DaoEstudiante.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }

    @Override
    public Estudiante buscar(Integer llavePrimaria) {
        try {

            miCadenaSQL = "SELECT * FROM estudiantes WHERE id_estudiante = ?";

            miConsulta = miObjConexion.prepareStatement(miCadenaSQL);
            miConsulta.setInt(1, llavePrimaria);

            misRegistros = miConsulta.executeQuery();

            Estudiante estudianteEncontrado = null;

            if (misRegistros.next()) {
                int idEstudiante = misRegistros.getInt("id_estudiante");
                String codUEstudiante = misRegistros.getString("codU_estudiante");
                String nomEstudiante = misRegistros.getString("nom_estudiante");
                String apellidoEstudiante = misRegistros.getString("apellido_estudiante");
                String correoEstudiante = misRegistros.getString("correo_estudiante");
                int idGrupo = misRegistros.getInt("id_grupo");

                estudianteEncontrado = new Estudiante(idEstudiante, codUEstudiante, nomEstudiante, apellidoEstudiante, correoEstudiante, idGrupo);
            }

            miObjConexion.close();
            return estudianteEncontrado;

        } catch (SQLException ex) {

            Logger.getLogger(DaoEstudiante.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }

    @Override
    public Boolean eliminar(Integer llavePrimaria) {
        try {
            miCadenaSQL = "DELETE FROM estudiantes WHERE id_estudiante=?";

            miConsulta = miObjConexion.prepareStatement(miCadenaSQL);

            miConsulta.setInt(1, llavePrimaria);
            miCantidad = miConsulta.executeUpdate();

            miObjConexion.close();
            return miCantidad > 0;
        } catch (SQLException ex) {
            Logger.getLogger(DaoEstudiante.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

    }

    @Override
    public Boolean actualizar(Estudiante elObjeto) {
        try {

            miCadenaSQL = "UPDATE estudiantes SET codU_estudiante = ?, "
                    + "nom_estudiante = ?, apellido_estudiante = ?, correo_estudiante = ?, id_grupo = ? WHERE id_estudiante = ?";

            miConsulta = miObjConexion.prepareStatement(miCadenaSQL);

            miConsulta.setString(1, elObjeto.getCodUEstudiante());
            miConsulta.setString(2, elObjeto.getNomEstudiante());
            miConsulta.setString(3, elObjeto.getApellidoEstudiante());
            miConsulta.setString(4, elObjeto.getCorreoEstudiante());

            if (elObjeto.getIdGrupo() == null) {
                miConsulta.setNull(5, java.sql.Types.INTEGER);
            } else {
                miConsulta.setInt(5, elObjeto.getIdGrupo());
            }

            miConsulta.setInt(6, elObjeto.getIdEstudiante());

            // Ejecutar la consulta
            miCantidad = miConsulta.executeUpdate();

            // Cerrar la conexión
            miObjConexion.close();
            return miCantidad > 0;
        } catch (SQLException ex) {
            // Manejar la excepción y registrar el error
            Logger.getLogger(DaoEstudiante.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

    }

    public boolean actualizarGrupo(Estudiante estudiante) {
        try {
            miCadenaSQL = "UPDATE estudiantes SET id_grupo = ? WHERE id_estudiante = ?";
            miConsulta = miObjConexion.prepareStatement(miCadenaSQL);
            miConsulta.setObject(1, estudiante.getIdGrupo(), java.sql.Types.INTEGER);
            miConsulta.setInt(2, estudiante.getIdEstudiante());

            miCantidad = miConsulta.executeUpdate();

            return miCantidad > 0;
        } catch (SQLException e) {
            Logger.getLogger(DaoEstudiante.class.getName()).log(Level.SEVERE, null, e);
            return false;
        }
    }

    @Override
    public List<Estudiante> buscarDato(String dato, String campo) {
        List<Estudiante> estudiantesEncontrados = new ArrayList<>();

        try {

            String consultaSQL = "SELECT * FROM estudiantes WHERE " + campo + " LIKE ?";
            miConsulta = miObjConexion.prepareStatement(consultaSQL);
            miConsulta.setString(1, dato);

            misRegistros = miConsulta.executeQuery();

            while (misRegistros.next()) {
                int idEstudiante = misRegistros.getInt("id_estudiante");
                String codUEstudiante = misRegistros.getString("codU_estudiante");
                String nomEstudiante = misRegistros.getString("nom_estudiante");
                String apellidoEstudiante = misRegistros.getString("apellido_estudiante");
                String correoEstudiante = misRegistros.getString("correo_estudiante");
                Integer idGrupo = (misRegistros.getObject("id_grupo") != null) ? misRegistros.getInt("id_grupo") : null;

                Estudiante estudiante = new Estudiante(idEstudiante, codUEstudiante, nomEstudiante, apellidoEstudiante, correoEstudiante, idGrupo);
                estudiantesEncontrados.add(estudiante);
            }

        } catch (SQLException ex) {
            Logger.getLogger(DaoEstudiante.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                // Cierra los recursos utilizados
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
                Logger.getLogger(DaoEstudiante.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return estudiantesEncontrados;
    }

    public List<Estudiante> consultarNombresPorGrupo(Integer codGrupo) {
        List<Estudiante> estudiantesEncontrados = new ArrayList<>();

        try {
            miCadenaSQL = "SELECT * FROM estudiantes WHERE id_grupo = ?";
            miConsulta = miObjConexion.prepareStatement(miCadenaSQL);
            miConsulta.setInt(1, codGrupo);

            misRegistros = miConsulta.executeQuery();

            while (misRegistros.next()) {
                int idEstudiante = misRegistros.getInt("id_estudiante");
                String codUEstudiante = misRegistros.getString("codU_estudiante");
                String nomEstudiante = misRegistros.getString("nom_estudiante");
                String apellidoEstudiante = misRegistros.getString("apellido_estudiante");
                String correoEstudiante = misRegistros.getString("correo_estudiante");
                int idGrupo = misRegistros.getInt("id_grupo");

                Estudiante estudiante = new Estudiante(idEstudiante, codUEstudiante, nomEstudiante, apellidoEstudiante, correoEstudiante, idGrupo);
                estudiantesEncontrados.add(estudiante);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DaoEstudiante.class.getName()).log(Level.SEVERE, null, ex);
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
                Logger.getLogger(DaoEstudiante.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return estudiantesEncontrados;
    }

    public List<Integer> consultarIdsPorGrupo(Integer codGrupo) {
        List<Integer> estudiantesIds = new ArrayList<>();

        try {
            miCadenaSQL = "SELECT id_estudiante FROM estudiantes WHERE id_grupo = ?";
            miConsulta = miObjConexion.prepareStatement(miCadenaSQL);
            miConsulta.setInt(1, codGrupo);
            misRegistros = miConsulta.executeQuery();

            while (misRegistros.next()) {
                int idEstudiante = misRegistros.getInt("id_estudiante");
                estudiantesIds.add(idEstudiante);
            }

        } catch (SQLException ex) {
            Logger.getLogger(DaoEstudiante.class.getName()).log(Level.SEVERE, null, ex);
        }

        return estudiantesIds;
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
