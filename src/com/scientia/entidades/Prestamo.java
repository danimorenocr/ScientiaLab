
package com.scientia.entidades;

import java.sql.Time;
import java.util.Date;


public class Prestamo {
    
    private int id_prestamo;
    private Date fecha_prestamo;
    private Time hora_prestamo;
    private Grupo id_grupo;

    public Prestamo() {
    }

    public Prestamo(int id_prestamo, Date fecha_prestamo, Time hora_prestamo, Grupo id_grupo) {
        this.id_prestamo = id_prestamo;
        this.fecha_prestamo = fecha_prestamo;
        this.hora_prestamo = hora_prestamo;
        this.id_grupo = id_grupo;
    }

    public int getId_prestamo() {
        return id_prestamo;
    }

    public void setId_prestamo(int id_prestamo) {
        this.id_prestamo = id_prestamo;
    }

    public Date getFecha_prestamo() {
        return fecha_prestamo;
    }

    public void setFecha_prestamo(Date fecha_prestamo) {
        this.fecha_prestamo = fecha_prestamo;
    }

    public Time getHora_prestamo() {
        return hora_prestamo;
    }

    public void setHora_prestamo(Time hora_prestamo) {
        this.hora_prestamo = hora_prestamo;
    }

    public Grupo getId_grupo() {
        return id_grupo;
    }

    public void setId_grupo(Grupo id_grupo) {
        this.id_grupo = id_grupo;
    }

    
    @Override
    public String toString() {
        return "Prestamo{" + "id_prestamo=" + id_prestamo + ", fecha_prestamo=" + fecha_prestamo + ", hora_prestamo=" + hora_prestamo + ", id_grupo=" + id_grupo + '}';
    }
    
    
    
}
