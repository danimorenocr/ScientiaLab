package com.scientia.entidades;


public class Estudiante {
    private Integer idEstudiante;
    private String codUEstudiante;
    private String nomEstudiante;
    private String apellidoEstudiante;
    private String correoEstudiante;
    private Integer idGrupo; // Cambiado a Integer para permitir valores nulos

    // Constructores
    public Estudiante() {
    }

    public Estudiante(int idEstudiante, String codUEstudiante, String nomEstudiante, String apellidoEstudiante, String correoEstudiante, Integer idGrupo) {
        this.idEstudiante = idEstudiante;
        this.codUEstudiante = codUEstudiante;
        this.nomEstudiante = nomEstudiante;
        this.apellidoEstudiante = apellidoEstudiante;
        this.correoEstudiante = correoEstudiante;
        this.idGrupo = idGrupo;
    }

    // Getters y Setters
    public int getIdEstudiante() {
        return idEstudiante;
    }

    public void setIdEstudiante(int idEstudiante) {
        this.idEstudiante = idEstudiante;
    }

    public String getCodUEstudiante() {
        return codUEstudiante;
    }

    public void setCodUEstudiante(String codUEstudiante) {
        this.codUEstudiante = codUEstudiante;
    }

    public String getNomEstudiante() {
        return nomEstudiante;
    }

    public void setNomEstudiante(String nomEstudiante) {
        this.nomEstudiante = nomEstudiante;
    }

    public String getApellidoEstudiante() {
        return apellidoEstudiante;
    }

    public void setApellidoEstudiante(String apellidoEstudiante) {
        this.apellidoEstudiante = apellidoEstudiante;
    }

    public String getCorreoEstudiante() {
        return correoEstudiante;
    }

    public void setCorreoEstudiante(String correoEstudiante) {
        this.correoEstudiante = correoEstudiante;
    }

    public Integer getIdGrupo() { // Cambiado a Integer
        return idGrupo;
    }

    public void setIdGrupo(Integer idGrupo) { // Cambiado a Integer
        this.idGrupo = idGrupo;
    }

    // Método toString
    @Override
    public String toString() {
        return "Estudiante{" +
                "idEstudiante=" + idEstudiante +
                ", codUEstudiante='" + codUEstudiante + '\'' +
                ", nomEstudiante='" + nomEstudiante + '\'' +
                ", apellidoEstudiante='" + apellidoEstudiante + '\'' +
                ", correoEstudiante='" + correoEstudiante + '\'' +
                ", idGrupo=" + idGrupo +
                '}';
    }
}
