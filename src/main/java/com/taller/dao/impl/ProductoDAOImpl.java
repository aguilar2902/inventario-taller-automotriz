package com.taller.dao.impl;

import com.taller.config.DatabaseManager;
import com.taller.dao.interfaces.ICategoriaDAO;
import com.taller.dao.interfaces.IProductoDAO;
import com.taller.model.Categoria;
import com.taller.model.Producto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

public class ProductoDAOImpl implements IProductoDAO {
    private static final Logger logger = LoggerFactory.getLogger(ProductoDAOImpl.class);
    private final DatabaseManager dbManager;

    public ProductoDAOImpl() {
        this.dbManager = DatabaseManager.getInstance();
    }

    @Override
    public Optional<Producto> buscarPorId(Integer p_id) {
        String sql = "SELECT * FROM productos WHERE id = ? AND activo = 1";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, p_id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapaProductos(rs));
            }

        } catch (SQLException e) {
            logger.error("Error al buscar producto por ID: " + p_id, e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Producto> buscarPorCodigo(String p_codigo) {
        String sql = "SELECT * FROM productos WHERE codigo = ? AND activo = 1";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, p_codigo);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapaProductos(rs));
            }

        } catch (SQLException e) {
            logger.error("Error al buscar producto por código: " + p_codigo, e);
        }
        return Optional.empty();
    }

    @Override
    public ArrayList<Producto> BuscarTodosLosProductos() {
        ArrayList<Producto> productos = new ArrayList<Producto>();
        String sql = """
            SELECT p.*, c.id as categoria_id, c.nombre as categoria_nombre, c.activo as categoria_activo
            FROM productos p 
            LEFT JOIN categorias c ON p.categoria_id = c.id 
            WHERE p.activo = 1 
            ORDER BY p.nombre
        """;

        try (Connection conn = dbManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                productos.add(mapaProductos(rs));
            }

        } catch (SQLException e) {
            logger.error("Error al listar productos", e);
        }

        return productos;
}

    @Override
    public ArrayList<Producto> buscarPorNombre(String p_nombre) {
        ArrayList<Producto> productos = new ArrayList<Producto>();
        String sql = "SELECT * FROM productos WHERE nombre LIKE ? AND activo = 1 ORDER BY nombre";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + p_nombre + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                productos.add(mapaProductos(rs));
            }

        } catch (SQLException e) {
            logger.error("Error al buscar productos por nombre: " + p_nombre, e);
        }

        return productos;
    }

    @Override
    public ArrayList<Producto> buscarPorCategoria(Integer p_categoriaId) {
        ArrayList<Producto> productos = new ArrayList<Producto>();
        String sql = "SELECT * FROM productos WHERE categoria_id = ? AND activo = 1 ORDER BY nombre";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)){

            stmt.setInt(1, p_categoriaId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                productos.add(mapaProductos(rs));
            }

        } catch (SQLException e) {
            logger.error("Error al buscar productos por categoría: " + p_categoriaId, e);
        }

        return productos;
    }

    @Override
    public ArrayList<Producto> buscarBajoStock() {
        ArrayList<Producto> productos = new ArrayList<Producto>();
        String sql = "SELECT * FROM productos WHERE stock_actual <= stock_minimo AND activo = 1 ORDER BY stock_actual";

        try (Connection conn = dbManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                productos.add(mapaProductos(rs));
            }

        } catch (SQLException e) {
            logger.error("Error al buscar productos bajo stock", e);
        }
        return productos;
    }

    @Override
    public boolean guardarProducto(Producto p_producto) {
        String sql = "INSERT INTO productos (codigo, nombre, descripcion, categoria_id, stock_actual, " +
                "stock_minimo, precio_unitario, activo) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, p_producto.getCodigo());
            stmt.setString(2, p_producto.getNombre());
            stmt.setString(3, p_producto.getDescripcion());

            // Manejar categoría
            if (p_producto.getCategoria() != null && p_producto.getCategoria().getId() != null) {
                stmt.setInt(4, p_producto.getCategoria().getId());
            } else {
                stmt.setNull(4, Types.INTEGER);
            }

            stmt.setInt(5, p_producto.getStockActual());
            stmt.setInt(6, p_producto.getStockMinimo());
            stmt.setBigDecimal(7, p_producto.getPrecioUnitario());
            stmt.setBoolean(8, p_producto.getActivo());


            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                try (Statement idStmt = conn.createStatement();
                     ResultSet rs = idStmt.executeQuery("SELECT last_insert_rowid()")) {
                    if (rs.next()) {
                        logger.info("Producto creado: " + p_producto.getCodigo() + " - " + p_producto.getNombre());
                    }
                }
                //return this.buscarPorCodigo(p_producto.getCodigo()).isPresent();
                return true;
            }

        } catch (SQLException e) {
            logger.error("Error al guardar producto: " + p_producto.getCodigo(), e);
        }
        return false;
    }

    @Override
    public boolean modificarProducto(Producto p_producto) {
        String sql = "UPDATE productos SET codigo = ?, nombre = ?, descripcion = ?, categoria_id = ?, " +
                "stock_actual = ?, stock_minimo = ?, precio_unitario = ?, activo = ? WHERE id = ?";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, p_producto.getCodigo());
            stmt.setString(2, p_producto.getNombre());
            stmt.setString(3, p_producto.getDescripcion());
            // Manejar categoría
            if (p_producto.getCategoria() != null && p_producto.getCategoria().getId() != null) {
                stmt.setInt(4, p_producto.getCategoria().getId());
            } else {
                stmt.setNull(4, Types.INTEGER);
            }
            stmt.setInt(5, p_producto.getStockActual());
            stmt.setInt(6, p_producto.getStockMinimo());
            stmt.setBigDecimal(7, p_producto.getPrecioUnitario());
            stmt.setBoolean(8, p_producto.getActivo());
            stmt.setInt(9, p_producto.getId());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                logger.info("Producto actualizado: " + p_producto.getCodigo());
                return true;
            }

        } catch (SQLException e) {
            logger.error("Error al actualizar producto ID: " + p_producto.getId(), e);
        }
        return false;
    }

    @Override
    public boolean eliminarProducto(Integer p_id) {
        // Borrado lógico
        String sql = "UPDATE productos SET activo = 0 WHERE id = ?";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, p_id);
            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                logger.info("Producto desactivado ID: " + p_id);
                return true;
            }

        } catch (SQLException e) {
            logger.error("Error al desactivar producto ID: " + p_id, e);
        }
        return false;
    }

    @Override
    public boolean existeElCodigo(String p_codigo) {
        String sql = "SELECT COUNT(*) FROM productos WHERE codigo = ?;";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            stmt.setString(1, p_codigo);


            if (rs.next()) {
                int count = rs.getInt(1);
                logger.debug("Productos con codigo '" + p_codigo + "': " + count);
                return count > 0;
            }

        } catch (SQLException e) {
            logger.error("Error al verificar existencia de código: " + p_codigo, e);
        }
        return false;
    }

    @Override
    public int contarTodosLosProductos() {
        String sql = "SELECT COUNT(*) FROM productos WHERE activo = 1";

        try (Connection conn = dbManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            logger.error("Error al contar productos", e);
        }
        return 0;
    }

    @Override
    public int contarBajoStock() {
        String sql = "SELECT COUNT(*) FROM productos WHERE stock_actual <= stock_minimo AND activo = 1";

        try (Connection conn = dbManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            logger.error("Error al contar productos bajo stock", e);
        }
        return 0;
    }

    private Producto mapaProductos(ResultSet rs) throws SQLException{
        Categoria cat = null;
        Integer catId = rs.getInt("categoria_id");
        if(!rs.wasNull() && catId > 0){
            // Obtener datos de categoría (si haces JOIN)
            try {
                String categoriaNombre = rs.getString("categoria_nombre");
                boolean categoriaActivo = rs.getBoolean("categoria_activo");
                cat = new Categoria(catId, categoriaNombre, categoriaActivo);
            } catch (SQLException e) {
                // Si no hay JOIN, solo crear con ID
                // Luego el servicio puede cargar los datos completos si es necesario
                //ICategoriaDAO categoriaDAO = new CategoriaDAOImpl();
                //cat = categoriaDAO.buscarPorId(catId).orElse(null);
                cat = new Categoria(catId, null, true);
            }
        }


        Integer id = rs.getInt("id");
        String codigo = rs.getString("codigo");
        String nombre = rs.getString("nombre");
        String descripcion = rs.getString("descripcion");
        int stockActual = rs.getInt("stock_actual");
        int stockMinimo = rs.getInt("stock_minimo");
        BigDecimal precioUnit = rs.getBigDecimal("precio_unitario");
        boolean activo = rs.getBoolean("activo");
        Timestamp timestamp = rs.getTimestamp("fecha_creacion");
        LocalDateTime fechaCreacion = timestamp != null ? timestamp.toLocalDateTime() : LocalDateTime.now();

        return new Producto(
                rs.getInt("id"),
                rs.getString("codigo"),
                rs.getString("nombre"),
                rs.getString("descripcion"),
                rs.getInt("stock_actual"),
                rs.getInt("stock_minimo"),
                rs.getBigDecimal("precio_unitario"),
                rs.getBoolean("activo"),
                fechaCreacion,
                cat
        );
    }
}
