package com.scientia.configuracion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MiConexion {

    private String url;
    private String clave;
    private String usuario;
    private String driver;

    protected String miCadenaSQL;
    protected ResultSet misRegistros;
    protected Connection miObjConexion;
    protected Integer miCantidad;
    protected PreparedStatement miConsulta;

    public MiConexion() {
        usuario = "user_poo";
        clave = "123456";
        driver = "com.mysql.cj.jdbc.Driver";
        url = "jdbc:mysql://localhost:3306/labfisica";
        conectarse();
    }

    private void conectarse() {
        try {
            Class.forName(driver);
            miObjConexion = DriverManager.getConnection(url, usuario, clave);
            System.out.println("Conectado a MySQL!!!");
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(MiConexion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Connection obtenerConexion() {
        if (miObjConexion == null) {
            conectarse();
        }
        return miObjConexion;
    }

    public void cerrarConexion() {
        if (miObjConexion != null) {
            try {
                miObjConexion.close();
            } catch (SQLException ex) {
                Logger.getLogger(MiConexion.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
