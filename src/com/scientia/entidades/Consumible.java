package com.scientia.entidades;

import java.io.InputStream;

public class Consumible {

    private int idConsumible;
    private String referenciaConsumible;
    private String observacionesConsumible;
    private int numGabineteConsumible;
    private String nombreConsumible;
    private String marcaConsumible;
    private boolean disponibilidadConsumible;
    private int existenciasConsumible;
    private InputStream fotoConsumible;

    public Consumible() {
    }

    public Consumible(int idConsumible, String referenciaConsumible, String observacionesConsumible, int numGabineteConsumible, String nombreConsumible, String marcaConsumible, boolean disponibilidadConsumible, int existenciasConsumible, InputStream fotoConsumible) {
        this.idConsumible = idConsumible;
        this.referenciaConsumible = referenciaConsumible;
        this.observacionesConsumible = observacionesConsumible;
        this.numGabineteConsumible = numGabineteConsumible;
        this.nombreConsumible = nombreConsumible;
        this.marcaConsumible = marcaConsumible;
        this.disponibilidadConsumible = disponibilidadConsumible;
        this.existenciasConsumible = existenciasConsumible;
        this.fotoConsumible = fotoConsumible;
    }

    // Getters y Setters
    public int getIdConsumible() {
        return idConsumible;
    }

    public void setIdConsumible(int idConsumible) {
        this.idConsumible = idConsumible;
    }

    public String getReferenciaConsumible() {
        return referenciaConsumible;
    }

    public void setReferenciaConsumible(String referenciaConsumible) {
        this.referenciaConsumible = referenciaConsumible;
    }

    public String getObservacionesConsumible() {
        return observacionesConsumible;
    }

    public void setObservacionesConsumible(String observacionesConsumible) {
        this.observacionesConsumible = observacionesConsumible;
    }

    public int getNumGabineteConsumible() {
        return numGabineteConsumible;
    }

    public void setNumGabineteConsumible(int numGabineteConsumible) {
        this.numGabineteConsumible = numGabineteConsumible;
    }

    public String getNombreConsumible() {
        return nombreConsumible;
    }

    public void setNombreConsumible(String nombreConsumible) {
        this.nombreConsumible = nombreConsumible;
    }

    public String getMarcaConsumible() {
        return marcaConsumible;
    }

    public void setMarcaConsumible(String marcaConsumible) {
        this.marcaConsumible = marcaConsumible;
    }

    public boolean getDisponibilidadConsumible() {
        return disponibilidadConsumible;
    }

    public void setDisponibilidadConsumible(boolean disponibilidadConsumible) {
        this.disponibilidadConsumible = disponibilidadConsumible;
    }

    public int getExistenciasConsumible() {
        return existenciasConsumible;
    }

    public void setExistenciasConsumible(int existenciasConsumible) {
        this.existenciasConsumible = existenciasConsumible;
    }

    public InputStream getFotoConsumible() {
        return fotoConsumible;
    }

    public void setFotoConsumible(InputStream fotoConsumible) {
        this.fotoConsumible = fotoConsumible;
    }

}
