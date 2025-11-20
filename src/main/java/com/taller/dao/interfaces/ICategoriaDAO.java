package com.taller.dao.interfaces;

import com.taller.model.Categoria;

import java.util.ArrayList;
import java.util.Optional;

public interface ICategoriaDAO {
    Optional<Categoria> buscarPorId(Integer p_id);
    ArrayList<Categoria> buscarTodosCategoria();
    boolean guardarCategoria(Categoria p_categoria);
    boolean modificarCategoria(Categoria p_categoria);
    boolean eliminarCategoria(Integer p_id);
    boolean existeCategoria(String p_nombre);
}
