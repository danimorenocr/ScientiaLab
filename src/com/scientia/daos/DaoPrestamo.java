package com.scientia.daos;

import com.scientia.configuracion.MiConexion;
import com.scientia.entidades.Grupo;
import com.scientia.entidades.GrupoLaboratorio;
import com.scientia.entidades.Prestamo;
import com.scientia.interfaces.Funcionalidad;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

public class DaoPrestamo extends MiConexion implements Funcionalidad<Prestamo> {

    @Override
    public Boolean registrar(Prestamo elObjeto) {
        try {
            miCadenaSQL = "INSERT INTO prestamos (fecha_prestamo, hora_prestamo, id_grupo) VALUES (?, ?, ?)";

            miConsulta = miObjConexion.prepareStatement(miCadenaSQL);

            //fecha
            long miliSegundos = elObjeto.getFecha_prestamo().getTime();
            Date fechaLista = new Date(miliSegundos);
            miConsulta.setDate(1, fechaLista);
            //***   
            miConsulta.setTime(2, elObjeto.getHora_prestamo());
            miConsulta.setInt(3, elObjeto.getId_grupo().getIdGrupo());
            miCantidad = miConsulta.executeUpdate();
            miObjConexion.close();
            return miCantidad > 0;

        } catch (SQLException ex) {
            Logger.getLogger(DaoPrestamo.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    @Override
    public List<Prestamo> consultar(String orden) {
        try {
            if (orden.isEmpty()) {
                orden = "id_prestamo";
            }

            miCadenaSQL = "SELECT p.id_prestamo, p.fecha_prestamo, p.hora_prestamo, p.id_grupo, g.id_grupo_laboratorio, gl.nombre_laboratorio, gl.desc_laboratorio "
                    + "FROM prestamos p "
                    + "INNER JOIN grupos g ON p.id_grupo = g.id_grupo "
                    + "INNER JOIN grupos_laboratorios gl ON g.id_grupo_laboratorio = gl.id_grupo_laboratorio "
                    + "ORDER BY " + orden;

            miConsulta = miObjConexion.prepareStatement(miCadenaSQL);
            misRegistros = miConsulta.executeQuery();

            List<Prestamo> arrPrestamos = new ArrayList<>();
            while (misRegistros.next()) {
                //prestamo
                int idPrestamo = misRegistros.getInt("id_prestamo");
                Date fechaPrestamo = misRegistros.getDate("fecha_prestamo");
                Time horaPrestamo = misRegistros.getTime("hora_prestamo");
                int idGrupo = misRegistros.getInt("id_grupo");
                //grupo
                int idGrupoLaboratorio = misRegistros.getInt("id_grupo_laboratorio");
                //grupolab
                String nombreLaboratorio = misRegistros.getString("nombre_laboratorio");
                String descLaboratorio = misRegistros.getString("desc_laboratorio");

                GrupoLaboratorio grupoLaboratorio = new GrupoLaboratorio(idGrupoLaboratorio, nombreLaboratorio, "");

                Grupo grupo = new Grupo(idGrupo, grupoLaboratorio);

                Prestamo prestamo = new Prestamo(idPrestamo, fechaPrestamo, horaPrestamo, grupo);
                arrPrestamos.add(prestamo);
            }

            miObjConexion.close();
            return arrPrestamos;
        } catch (SQLException ex) {
            Logger.getLogger(DaoPrestamo.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public Prestamo buscar(Integer llavePrimaria) {
        try {
            miCadenaSQL = "SELECT p.id_prestamo, p.fecha_prestamo, p.hora_prestamo, p.id_grupo, g.id_grupo_laboratorio, gl.nombre_laboratorio "
                    + "FROM prestamos p "
                    + "INNER JOIN grupos g ON p.id_grupo = g.id_grupo "
                    + "INNER JOIN grupos_laboratorios gl ON g.id_grupo_laboratorio = gl.id_grupo_laboratorio "
                    + "WHERE p.id_prestamo = ?";
            miConsulta = miObjConexion.prepareStatement(miCadenaSQL);
            miConsulta.setInt(1, llavePrimaria);
            misRegistros = miConsulta.executeQuery();

            Prestamo objEncontrado = null;

            if (misRegistros.next()) {
                int idPrestamo = misRegistros.getInt("id_prestamo");
                Date fechaPrestamo = misRegistros.getDate("fecha_prestamo");
                Time horaPrestamo = misRegistros.getTime("hora_prestamo");
                int idGrupo = misRegistros.getInt("id_grupo");
                int idGrupoLaboratorio = misRegistros.getInt("id_grupo_laboratorio");
                String nombreLaboratorio = misRegistros.getString("nombre_laboratorio");

                GrupoLaboratorio grupoLaboratorio = new GrupoLaboratorio(idGrupoLaboratorio, nombreLaboratorio, "");

                Grupo grupo = new Grupo(idGrupo, grupoLaboratorio);

                objEncontrado = new Prestamo(idPrestamo, fechaPrestamo, horaPrestamo, grupo);
            }
            miObjConexion.close();
            return objEncontrado;
        } catch (SQLException ex) {
            Logger.getLogger(DaoPrestamo.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public Boolean eliminar(Integer llavePrimaria) {
        try {
            miCadenaSQL = "DELETE FROM prestamos WHERE id_prestamo=?";

            miConsulta = miObjConexion.prepareStatement(miCadenaSQL);

            miConsulta.setInt(1, llavePrimaria);
            miCantidad = miConsulta.executeUpdate();

            miObjConexion.close();
            return miCantidad > 0;
        } catch (SQLException ex) {
            Logger.getLogger(DaoPrestamo.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    @Override
    public Boolean actualizar(Prestamo elObjeto) {
        try {
            miCadenaSQL = "UPDATE prestamos SET fecha_prestamo = ?, "
                    + "hora_prestamo = ?, id_grupo = ? "
                    + "WHERE id_prestamo = ? ";

            miConsulta = miObjConexion.prepareStatement(miCadenaSQL);

            // fecha
            long miliSegundos = elObjeto.getFecha_prestamo().getTime();
            Date fechaLista = new Date(miliSegundos);
            miConsulta.setDate(1, fechaLista);

            miConsulta.setTime(2, elObjeto.getHora_prestamo());
            miConsulta.setInt(3, elObjeto.getId_grupo().getIdGrupo());
            miConsulta.setInt(4, elObjeto.getId_prestamo());
            miCantidad = miConsulta.executeUpdate();

            miObjConexion.close();

            return miCantidad > 0;
        } catch (SQLException ex) {
            Logger.getLogger(DaoPrestamo.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    @Override
    public List<Prestamo> buscarDato(String dato, String campo) {
        try {
            miCadenaSQL = "SELECT * FROM prestamos WHERE " + campo + " LIKE ?";
            miConsulta = miObjConexion.prepareStatement(miCadenaSQL);

            miConsulta.setString(1, "%" + dato + "%");

            misRegistros = miConsulta.executeQuery();

            List<Prestamo> arrPrestamo = new ArrayList<>();

            while (misRegistros.next()) {
                int idPrestamo = misRegistros.getInt("id_prestamo");
                Date fechaPrestamo = misRegistros.getDate("fecha_prestamo");
                Time horaPrestamo = misRegistros.getTime("hora_prestamo");
                int idGrupo = misRegistros.getInt("id_grupo");

                GrupoLaboratorio grupoLaboratorio = new GrupoLaboratorio(0, "", "");

                Grupo grupo = new Grupo(idGrupo, grupoLaboratorio);

                Prestamo prestamo = new Prestamo(idPrestamo, fechaPrestamo, horaPrestamo, grupo);
                arrPrestamo.add(prestamo);
            }

            // Cierra la conexión
            miObjConexion.close();

            // Retorna la lista de préstamos encontrados
            return arrPrestamo;
        } catch (SQLException ex) {
            // Manejo de errores
            Logger.getLogger(DaoPrestamo.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public Integer buscarUltimoIdPres() {
        try {
            miCadenaSQL = "SELECT id_prestamo FROM prestamos ORDER BY id_prestamo DESC LIMIT 1";

            miConsulta = miObjConexion.prepareStatement(miCadenaSQL);

            misRegistros = miConsulta.executeQuery();
            Integer ultimoId = null;

            if (misRegistros.next()) {
                ultimoId = misRegistros.getInt(1);
            }

            miObjConexion.close();
            return ultimoId;

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
