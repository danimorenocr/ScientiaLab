package com.scientia.entidades;

public class Equipo {

    private int id_equipo;
    private String cod_inventario_equipo;
    private String marca_equipo;
    private String modelo_equipo;
    private String serie_equipo;
    private String mantenimiento_equipo;
    private String req_mante_equipo;
    private String estado_equipo;
    private String nombre_equipo;
    private Boolean disponibilidad_equipo;
    private int existencias_equipo;

    public Equipo() {
    }

    public Equipo(int id_equipo, String cod_inventario_equipo, String modelo_equipo, String serie_equipo, String mantenimiento_equipo, String req_mante_equipo, String estado_equipo, String nombre_equipo, String marca_equipo, Boolean disponibilidad_equipo, int existencias_equipo) {
        this.id_equipo = id_equipo;
        this.cod_inventario_equipo = cod_inventario_equipo;
        this.modelo_equipo = modelo_equipo;
        this.serie_equipo = serie_equipo;
        this.mantenimiento_equipo = mantenimiento_equipo;
        this.req_mante_equipo = req_mante_equipo;
        this.estado_equipo = estado_equipo;
        this.nombre_equipo = nombre_equipo;
        this.marca_equipo = marca_equipo;
        this.disponibilidad_equipo = disponibilidad_equipo;
        this.existencias_equipo = existencias_equipo;
    }

    public int getId_equipo() {
        return id_equipo;
    }

    public void setId_equipo(int id_equipo) {
        this.id_equipo = id_equipo;
    }

    public String getCod_inventario_equipo() {
        return cod_inventario_equipo;
    }

    public void setCod_inventario_equipo(String cod_inventario_equipo) {
        this.cod_inventario_equipo = cod_inventario_equipo;
    }

    public String getModelo_equipo() {
        return modelo_equipo;
    }

    public void setModelo_equipo(String modelo_equipo) {
        this.modelo_equipo = modelo_equipo;
    }

    public String getSerie_equipo() {
        return serie_equipo;
    }

    public void setSerie_equipo(String serie_equipo) {
        this.serie_equipo = serie_equipo;
    }

    public String getMantenimiento_equipo() {
        return mantenimiento_equipo;
    }

    public void setMantenimiento_equipo(String mantenimiento_equipo) {
        this.mantenimiento_equipo = mantenimiento_equipo;
    }

    public String getReq_mante_equipo() {
        return req_mante_equipo;
    }

    public void setReq_mante_equipo(String req_mante_equipo) {
        this.req_mante_equipo = req_mante_equipo;
    }

    public String getEstado_equipo() {
        return estado_equipo;
    }

    public void setEstado_equipo(String estado_equipo) {
        this.estado_equipo = estado_equipo;
    }

    public String getNombre_equipo() {
        return nombre_equipo;
    }

    public void setNombre_equipo(String nombre_equipo) {
        this.nombre_equipo = nombre_equipo;
    }

    public String getMarca_equipo() {
        return marca_equipo;
    }

    public void setMarca_equipo(String marca_equipo) {
        this.marca_equipo = marca_equipo;
    }

    public Boolean getDisponibilidad_equipo() {
        return disponibilidad_equipo;
    }

    public void setDisponibilidad_equipo(Boolean disponibilidad_equipo) {
        this.disponibilidad_equipo = disponibilidad_equipo;
    }

    public int getExistencias_equipo() {
        return existencias_equipo;
    }

    public void setExistencias_equipo(int existencias_equipo) {
        this.existencias_equipo = existencias_equipo;
    }

    @Override
    public String toString() {
        return "Equipo{" + "id_equipo=" + id_equipo + ", cod_inventario_equipo=" + cod_inventario_equipo + ", modelo_equipo=" + modelo_equipo + ", serie_equipo=" + serie_equipo + ", mantenimiento_equipo=" + mantenimiento_equipo + ", req_mante_equipo=" + req_mante_equipo + ", estado_equipo=" + estado_equipo + ", nombre_equipo=" + nombre_equipo + ", marca_equipo=" + marca_equipo + ", disponibilidad_equipo=" + disponibilidad_equipo + ", existencias_equipo=" + existencias_equipo + '}';
    }

}
