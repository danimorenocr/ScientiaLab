package com.scientia.entidades;

public class PrestamoItem {

    private Integer idPrestamoItem;
    private Prestamo codPrestamo;
    private Elemento codElemento;
    private Equipo codEquipo;
    private Consumible codConsumible;

    // Constructor
    public PrestamoItem() {
    }

    public PrestamoItem(Integer idPrestamoItem, Prestamo codPrestamo, Elemento codElemento, Equipo codEquipo, Consumible codConsumible) {
        this.idPrestamoItem = idPrestamoItem;
        this.codPrestamo = codPrestamo;
        this.codElemento = codElemento;
        this.codEquipo = codEquipo;
        this.codConsumible = codConsumible;
    }

   

    public Integer getIdPrestamoItem() {
        return idPrestamoItem;
    }

    public void setIdPrestamoItem(Integer idPrestamoItem) {
        this.idPrestamoItem = idPrestamoItem;
    }

    public Prestamo getCodPrestamo() {
        return codPrestamo;
    }

    public void setCodPrestamo(Prestamo codPrestamo) {
        this.codPrestamo = codPrestamo;
    }

    public Elemento getCodElemento() {
        return codElemento;
    }

    public void setCodElemento(Elemento codElemento) {
        this.codElemento = codElemento;
    }

    public Equipo getCodEquipo() {
        return codEquipo;
    }

    public void setCodEquipo(Equipo codEquipo) {
        this.codEquipo = codEquipo;
    }

    public Consumible getCodConsumible() {
        return codConsumible;
    }

    public void setCodConsumible(Consumible codConsumible) {
        this.codConsumible = codConsumible;
    }

    

    

}
