package com.taller.service;

import com.taller.dao.impl.CategoriaDAOImpl;
import com.taller.dao.interfaces.ICategoriaDAO;
import com.taller.model.Categoria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Optional;

public class CategoriaService {
    private static final Logger logger = LoggerFactory.getLogger(CategoriaService.class);
    private static CategoriaService instance;
    private final ICategoriaDAO categoriaDAO;

    private CategoriaService(){
        this.categoriaDAO = new CategoriaDAOImpl();
    }

    public static CategoriaService getInstance() {
        if (instance == null) {
            synchronized (CategoriaService.class) {
                if (instance == null) {
                    instance = new CategoriaService();
                }
            }
        }
        return instance;
    }

    public ArrayList<Categoria> obtenerTodas() {
        return categoriaDAO.buscarTodosCategoria();
    }

    public Optional<Categoria> obtenerPorId(Integer id) {
        return categoriaDAO.buscarPorId(id);
    }

    public boolean guardar(Categoria categoria) {
        if (categoria.getNombre() == null || categoria.getNombre().trim().isEmpty()) {
            logger.warn("Intento de guardar categoría sin nombre");
            return false;
        }

        if (categoriaDAO.existeCategoria(categoria.getNombre())) {
            logger.warn("La categoría ya existe: " + categoria.getNombre());
            return false;
        }

        return categoriaDAO.guardarCategoria(categoria);
    }

    public boolean actualizar(Categoria categoria) {
        if (categoria.getId() == null) {
            logger.warn("Intento de actualizar categoría sin ID");
            return false;
        }

        return categoriaDAO.modificarCategoria(categoria);
    }

    public boolean eliminar(Integer id) {
        return categoriaDAO.eliminarCategoria(id);
    }
}
