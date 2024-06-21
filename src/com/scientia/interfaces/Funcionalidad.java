package com.scientia.interfaces;

import java.util.List;

public interface Funcionalidad<T> {

    public Boolean registrar(T elObjeto);

    public List<T> consultar(String orden);

    public T buscar(Integer llavePrimaria);

    public Boolean eliminar(Integer llavePrimaria);

    public Boolean actualizar(T elObjeto);

    public List<T> buscarDato(String dato, String campo);

}
