package com.taller.controller;

import com.taller.model.Producto;
import com.taller.service.ProductoService;
import com.taller.view.ProductosView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Optional;

public class ProductoController {
    private static final Logger logger = LoggerFactory.getLogger(ProductoController.class);
    private final ProductosView view;
    private final ProductoService productoService;

    public ProductoController(ProductosView view) {
        this.view = view;
        this.productoService = ProductoService.getInstance();
    }

    public void cargarProductos() {
        logger.info("Cargando todos los productos");
        try {
            ArrayList<Producto> productos = productoService.obtenerTodos();
            view.actualizarTabla(productos);
            actualizarEstadisticas();
            logger.info("Se cargaron " + productos.size() + " productos");
        } catch (Exception e) {
            logger.error("Error al cargar productos", e);
            view.actualizarTabla(new ArrayList<>());
        }
    }

    public void buscarProductos(String textoBusqueda) {
        logger.info("Buscando productos con: " + textoBusqueda);
        try {
            ArrayList<Producto> productos = productoService.buscarPorNombre(textoBusqueda);
            view.actualizarTabla(productos);
            logger.info("Se encontraron " + productos.size() + " productos");
        } catch (Exception e) {
            logger.error("Error al buscar productos", e);
            view.actualizarTabla(new ArrayList<>());
        }
    }

    public Producto obtenerProductoPorId(Integer id) {
        logger.info("Obteniendo producto con ID: " + id);
        try {
            Optional<Producto> producto = productoService.obtenerPorId(id);
            if (producto.isPresent()) {
                return producto.get();
            } else {
                logger.warn("Producto no encontrado con ID: " + id);
                return null;
            }
        } catch (Exception e) {
            logger.error("Error al obtener producto", e);
            return null;
        }
    }

    public boolean guardarProducto(Producto producto) {
        logger.info("Intentando guardar producto: " + producto.getCodigo());
        try {
            boolean resultado = productoService.guardar(producto);
            if (resultado) {
                logger.info("Producto guardado exitosamente: " + producto.getCodigo());
            } else {
                logger.warn("No se pudo guardar el producto: " + producto.getCodigo());
            }
            return resultado;
        } catch (Exception e) {
            logger.error("Error al guardar producto", e);
            return false;
        }
    }

    public boolean actualizarProducto(Producto producto) {
        logger.info("Intentando actualizar producto ID: " + producto.getId());
        try {
            boolean resultado = productoService.actualizar(producto);
            if (resultado) {
                logger.info("Producto actualizado exitosamente ID: " + producto.getId());
            } else {
                logger.warn("No se pudo actualizar el producto ID: " + producto.getId());
            }
            return resultado;
        } catch (Exception e) {
            logger.error("Error al actualizar producto", e);
            return false;
        }
    }

    public boolean eliminarProducto(Integer id) {
        logger.info("Intentando eliminar producto ID: " + id);
        try {
            boolean resultado = productoService.eliminar(id);
            if (resultado) {
                logger.info("Producto eliminado exitosamente ID: " + id);
            } else {
                logger.warn("No se pudo eliminar el producto ID: " + id);
            }
            return resultado;
        } catch (Exception e) {
            logger.error("Error al eliminar producto", e);
            return false;
        }
    }

    private void actualizarEstadisticas() {
        try {
            int total = productoService.contarTotal();
            int bajoStock = productoService.contarBajoStock();
            view.actualizarEstadisticas(total, bajoStock);
        } catch (Exception e) {
            logger.error("Error al actualizar estadísticas", e);
            view.actualizarEstadisticas(0, 0);
        }
    }
}