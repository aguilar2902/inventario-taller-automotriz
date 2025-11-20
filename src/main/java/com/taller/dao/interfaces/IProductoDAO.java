package com.taller.dao.interfaces;

import com.taller.model.Producto;

import java.util.ArrayList;
import java.util.Optional;

public interface IProductoDAO {
    Optional<Producto> buscarPorId(Integer p_id);
    Optional<Producto> buscarPorCodigo(String p_codigo);
    ArrayList<Producto> BuscarTodosLosProductos();
    ArrayList<Producto> buscarPorNombre(String p_nombre);
    ArrayList<Producto> buscarPorCategoria(Integer p_categoriaId);
    ArrayList<Producto> buscarBajoStock();
    boolean guardarProducto(Producto p_producto);
    boolean modificarProducto(Producto p_producto);
    boolean eliminarProducto(Integer p_id);
    boolean existeElCodigo(String p_codigo);
    int contarTodosLosProductos();
    int contarBajoStock();
}
