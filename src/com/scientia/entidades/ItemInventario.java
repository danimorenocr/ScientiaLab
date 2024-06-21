
package com.scientia.entidades;

public abstract class ItemInventario {
    private int id;
    private String nombre;
    private String marca;
    private Boolean disponibilidad;
    private Integer existencias;

    public ItemInventario(int id, String nombre, String marca, Boolean disponibilidad, Integer existencias) {
        this.id = id;
        this.nombre = nombre;
        this.marca = marca;
        this.disponibilidad = disponibilidad;
        this.existencias = existencias;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public Boolean getDisponibilidad() {
        return disponibilidad;
    }

    public void setDisponibilidad(Boolean disponibilidad) {
        this.disponibilidad = disponibilidad;
    }

    public Integer getExistencias() {
        return existencias;
    }

    public void setExistencias(Integer existencias) {
        this.existencias = existencias;
    }
    
    
}
