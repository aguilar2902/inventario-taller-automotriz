package com.taller.service;

import com.taller.dao.impl.ProductoDAOImpl;
import com.taller.dao.interfaces.IProductoDAO;
import com.taller.model.Producto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Optional;

public class ProductoService {
    private static final Logger logger = LoggerFactory.getLogger(ProductoService.class);
    private static ProductoService instance;
    private final IProductoDAO productoDAO;

    private ProductoService() {
        this.productoDAO = new ProductoDAOImpl();
    }

    public static ProductoService getInstance() {
        if (instance == null) {
            synchronized (ProductoService.class) {
                if (instance == null) {
                    instance = new ProductoService();
                }
            }
        }
        return instance;
    }
    public ArrayList<Producto> obtenerTodos() {
        return productoDAO.BuscarTodosLosProductos();
    }

    public Optional<Producto> obtenerPorId(Integer id) {
        return productoDAO.buscarPorId(id);
    }

    public Optional<Producto> obtenerPorCodigo(String codigo) {
        return productoDAO.buscarPorCodigo(codigo);
    }

    public ArrayList<Producto> buscarPorNombre(String nombre) {
        return productoDAO.buscarPorNombre(nombre);
    }

    public ArrayList<Producto> obtenerBajoStock() {
        return productoDAO.buscarBajoStock();
    }
    public boolean existeCodigo(String codigo) {
        return productoDAO.existeElCodigo(codigo);
    }

    public boolean guardar(Producto producto) {
        // Validaciones
        if (producto.getCodigo() == null || producto.getCodigo().trim().isEmpty()) {
            logger.warn("Intento de guardar producto sin código");
            return false;
        }

        if (producto.getNombre() == null || producto.getNombre().trim().isEmpty()) {
            logger.warn("Intento de guardar producto sin nombre");
            return false;
        }

        // Verificar código duplicado
        if (productoDAO.existeElCodigo(producto.getCodigo())) {
            logger.warn("El código ya existe: " + producto.getCodigo());
            return false;
        }

        return productoDAO.guardarProducto(producto);
    }

    public boolean actualizar(Producto producto) {
        if (producto.getId() == null) {
            logger.warn("Intento de actualizar producto sin ID");
            return false;
        }

        // Verificar que existe
        Optional<Producto> existente = productoDAO.buscarPorId(producto.getId());
        if (existente.isEmpty()) {
            logger.warn("Producto no encontrado para actualizar: " + producto.getId());
            return false;
        }

        // Verificar código duplicado (excepto el mismo producto)
        Optional<Producto> porCodigo = productoDAO.buscarPorCodigo(producto.getCodigo());
        if (porCodigo.isPresent() && !porCodigo.get().getId().equals(producto.getId())) {
            logger.warn("El código ya existe en otro producto: " + producto.getCodigo());
            return false;
        }

        return productoDAO.modificarProducto(producto);
    }

    public boolean eliminar(Integer id) {
        return productoDAO.eliminarProducto(id);
    }

    public int contarTotal() {
        return productoDAO.contarTodosLosProductos();
    }

    public int contarBajoStock() {
        return productoDAO.contarBajoStock();
    }

    public boolean validarStock(Producto producto) {
        return producto.getStockActual() >= 0 && producto.getStockMinimo() >= 0;
    }
}
