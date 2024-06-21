package com.scientia.entidades;

public class Grupo {

    private int idGrupo;
    private GrupoLaboratorio idGrupoLaboratorio;

    public Grupo() {
    }

    public Grupo(Integer idGrupo, GrupoLaboratorio idGrupoLaboratorio) {
        this.idGrupo = idGrupo;
        this.idGrupoLaboratorio = idGrupoLaboratorio;
    }

    public Integer getIdGrupo() {
        return idGrupo;
    }

    public void setIdGrupo(Integer idGrupo) {
        this.idGrupo = idGrupo;
    }

    public GrupoLaboratorio getIdGrupoLaboratorio() {
        return idGrupoLaboratorio;
    }

    public void setIdGrupoLaboratorio(GrupoLaboratorio idGrupoLaboratorio) {
        this.idGrupoLaboratorio = idGrupoLaboratorio;
    }

    @Override
    public String toString() {
        return "Grupo{" + "idGrupo=" + idGrupo + ", idGrupoLaboratorio=" + idGrupoLaboratorio + '}';
    }

}
