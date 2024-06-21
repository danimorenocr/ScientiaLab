package com.scientia.entidades;

public class GrupoLaboratorio {

    private Integer idGrupoLaboratorio;
    private String nombreLaboratorio;
    private String descLaboratorio;

    public GrupoLaboratorio() {
    }

    public GrupoLaboratorio(Integer idGrupoLaboratorio, String nombreLaboratorio, String descLaboratorio) {
        this.idGrupoLaboratorio = idGrupoLaboratorio;
        this.nombreLaboratorio = nombreLaboratorio;
        this.descLaboratorio = descLaboratorio;
    }
    

    public Integer getIdGrupoLaboratorio() {
        return idGrupoLaboratorio;
    }

    public void setIdGrupoLaboratorio(Integer idGrupoLaboratorio) {
        this.idGrupoLaboratorio = idGrupoLaboratorio;
    }

    public String getNombreLaboratorio() {
        return nombreLaboratorio;
    }

    public void setNombreLaboratorio(String nombreLaboratorio) {
        this.nombreLaboratorio = nombreLaboratorio;
    }

    public String getDescLaboratorio() {
        return descLaboratorio;
    }

    public void setDescLaboratorio(String descLaboratorio) {
        this.descLaboratorio = descLaboratorio;
    }

    @Override
    public String toString() {
        return "GrupoLaboratorio{" + "idGrupoLaboratorio=" + idGrupoLaboratorio + ", nombreLaboratorio=" + nombreLaboratorio + ", descLaboratorio=" + descLaboratorio + '}';
    }
    
    

}
