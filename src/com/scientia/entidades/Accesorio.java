
package com.scientia.entidades;

import java.util.List;

public class Accesorio {
    
    private Integer idAccesorio;
    private List<String> codInventarioAccesorio;
    private String nombreAccesorio;
    private String referenciaAccesorio;
    private Integer cantidadAccesorio;
    private Elemento elemento;

    public Accesorio() {
    }
       
    public Accesorio(Integer idAccesorio, List<String> codInventarioAccesorio, String nombreAccesorio, String referenciaAccesorio, Integer cantidadAccesorio, Elemento elemento) {
        this.idAccesorio = idAccesorio;
        this.codInventarioAccesorio = codInventarioAccesorio;
        this.nombreAccesorio = nombreAccesorio;
        this.referenciaAccesorio = referenciaAccesorio;
        this.cantidadAccesorio = cantidadAccesorio;
        this.elemento = elemento;
    }

    public Integer getIdAccesorio() {
        return idAccesorio;
    }

    public void setIdAccesorio(Integer idAccesorio) {
        this.idAccesorio = idAccesorio;
    }

    public List<String> getCodInventarioAccesorio() {
        return codInventarioAccesorio;
    }

    public void setCodInventarioAccesorio(List<String> codInventarioAccesorio) {
        this.codInventarioAccesorio = codInventarioAccesorio;
    }

    public String getNombreAccesorio() {
        return nombreAccesorio;
    }

    public void setNombreAccesorio(String nombreAccesorio) {
        this.nombreAccesorio = nombreAccesorio;
    }

    public String getReferenciaAccesorio() {
        return referenciaAccesorio;
    }

    public void setReferenciaAccesorio(String referenciaAccesorio) {
        this.referenciaAccesorio = referenciaAccesorio;
    }

    public Integer getCantidadAccesorio() {
        return cantidadAccesorio;
    }

    public void setCantidadAccesorio(Integer cantidadAccesorio) {
        this.cantidadAccesorio = cantidadAccesorio;
    }

    public Elemento getElemento() {
        return elemento;
    }

    public void setElemento(Elemento elemento) {
        this.elemento = elemento;
    }

    @Override
    public String toString() {
        return "Accesorio{" + "idAccesorio=" + idAccesorio + ", codInventarioAccesorio=" + codInventarioAccesorio + ", nombreAccesorio=" + nombreAccesorio + ", referenciaAccesorio=" + referenciaAccesorio + ", cantidadAccesorio=" + cantidadAccesorio + ", elemento=" + elemento + '}';
    }
    
    
    
}
