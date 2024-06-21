package com.scientia.entidades;

import java.io.InputStream;
import java.util.Date;
import java.util.List;

public class Elemento extends ItemInventario {
    
    private List<String> codInventarioElemento;
    private InputStream fotoElemento;
    private Boolean manualElemento;
    private String actaElemento;
    private Double costoElemento;
    private Date fechaCompraElemento;
    private Integer cantAccesorios;

    public Elemento(List<String> codInventarioElemento, InputStream fotoElemento, Boolean manualElemento, String actaElemento, Double costoElemento, Date fechaCompraElemento, Integer cantAccesorios, Integer id, String nombre, String marca, Boolean disponibilidad, Integer existencias) {
        super(id, nombre, marca, disponibilidad, existencias);
        this.codInventarioElemento = codInventarioElemento;
        this.fotoElemento = fotoElemento;
        this.manualElemento = manualElemento;
        this.actaElemento = actaElemento;
        this.costoElemento = costoElemento;
        this.fechaCompraElemento = fechaCompraElemento;
        this.cantAccesorios = cantAccesorios;
    }

    public List<String> getCodInventarioElemento() {
        return codInventarioElemento;
    }

    public void setCodInventarioElemento(List<String> codInventarioElemento) {
        this.codInventarioElemento = codInventarioElemento;
    }

    public InputStream getFotoElemento() {
        return fotoElemento;
    }

    public void setFotoElemento(InputStream fotoElemento) {
        this.fotoElemento = fotoElemento;
    }

    public Boolean getManualElemento() {
        return manualElemento;
    }

    public void setManualElemento(Boolean manualElemento) {
        this.manualElemento = manualElemento;
    }

    public String getActaElemento() {
        return actaElemento;
    }

    public void setActaElemento(String actaElemento) {
        this.actaElemento = actaElemento;
    }

    public Double getCostoElemento() {
        return costoElemento;
    }

    public void setCostoElemento(Double costoElemento) {
        this.costoElemento = costoElemento;
    }

    public Date getFechaCompraElemento() {
        return fechaCompraElemento;
    }

    public void setFechaCompraElemento(Date fechaCompraElemento) {
        this.fechaCompraElemento = fechaCompraElemento;
    }

    public Integer getCantAccesorios() {
        return cantAccesorios;
    }

    public void setCantAccesorios(Integer cantAccesorios) {
        this.cantAccesorios = cantAccesorios;
    }

    @Override
    public String toString() {
        return "Elemento{" + "Id: " + super.getId() + ", Nombre: " + super.getNombre() + ", codInventarioElemento=" + codInventarioElemento + ", fotoElemento=" + fotoElemento + ", manualElemento=" + manualElemento + ", actaElemento=" + actaElemento + ", costoElemento=" + costoElemento + ", fechaCompraElemento=" + fechaCompraElemento + ", cantAccesorios=" + cantAccesorios + '}';
    }
}
