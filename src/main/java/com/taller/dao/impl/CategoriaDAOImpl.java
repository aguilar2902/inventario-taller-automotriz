package com.taller.dao.impl;

import com.taller.config.DatabaseManager;
import com.taller.dao.interfaces.ICategoriaDAO;
import com.taller.model.Categoria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

public class CategoriaDAOImpl implements ICategoriaDAO {
    private static final Logger logger = LoggerFactory.getLogger(CategoriaDAOImpl.class);
    private final DatabaseManager dbManager;

    public CategoriaDAOImpl(){
        this.dbManager = DatabaseManager.getInstance();
    }

    @Override
    public Optional<Categoria> buscarPorId(Integer p_id) {
        String sql = "SELECT * FROM categorias WHERE id = ? AND activo = 1";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, p_id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapaCategorias(rs));
            }

        } catch (SQLException e) {
            logger.error("Error al buscar categoría por ID: " + p_id, e);
        }
        return Optional.empty();
    }

    @Override
    public ArrayList<Categoria> buscarTodosCategoria() {
        ArrayList<Categoria> categorias = new ArrayList<Categoria>();
        String sql = "SELECT * FROM categorias WHERE activo = 1 ORDER BY nombre";

        try (Connection conn = dbManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                categorias.add(mapaCategorias(rs));
            }

        } catch (SQLException e) {
            logger.error("Error al listar categorías", e);
        }
        return categorias;
    }

    @Override
    public boolean guardarCategoria(Categoria p_categoria) {
        String sql = "INSERT INTO categorias (nombre, activo) VALUES (?, ?)";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, p_categoria.getNombre());
            stmt.setBoolean(3, p_categoria.getActivo());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        //por ahora no lo utilizo
                        //p_categoria.setId(generatedKeys.getInt(1));
                    }
                }
                logger.info("Categoría creada: " + p_categoria.getNombre());
                return true;
            }

        } catch (SQLException e) {
            logger.error("Error al guardar categoría: " + p_categoria.getNombre(), e);
        }
        return false;
    }

    @Override
    public boolean modificarCategoria(Categoria p_categoria) {
        String sql = "UPDATE categorias SET nombre = ?, activo = ? WHERE id = ?";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, p_categoria.getNombre());
            stmt.setBoolean(2, p_categoria.getActivo());
            stmt.setInt(3, p_categoria.getId());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                logger.info("Categoría actualizada: " + p_categoria.getNombre());
                return true;
            }

        } catch (SQLException e) {
            logger.error("Error al actualizar categoría ID: " + p_categoria.getId(), e);
        }
        return false;
    }

    @Override
    public boolean eliminarCategoria(Integer p_id) {
        String sql = "UPDATE categorias SET activo = 0 WHERE id = ?";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, p_id);
            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                logger.info("Categoría desactivada ID: " + p_id);
                return true;
            }

        } catch (SQLException e) {
            logger.error("Error al desactivar categoría ID: " + p_id, e);
        }
        return false;
    }

    @Override
    public boolean existeCategoria(String p_nombre) {
        String sql = "SELECT COUNT(*) FROM categorias WHERE nombre = ? AND activo = 1";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, p_nombre);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            logger.error("Error al verificar existencia de categoría: " + p_nombre, e);
        }
        return false;
    }

    private Categoria mapaCategorias(ResultSet rs) throws SQLException{
        Integer id = rs.getInt("id");
        String nombre = rs.getString("nombre");
        boolean activo = rs.getBoolean("activo");

        Categoria categoria = new Categoria(id, nombre, activo);
        return categoria;
    }
}
