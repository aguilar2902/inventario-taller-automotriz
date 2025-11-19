package com.taller.dao.impl;


import com.taller.config.DatabaseManager;
import com.taller.dao.interfaces.IUsuarioDAO;
import com.taller.model.Usuario;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UsuarioDAOImpl implements IUsuarioDAO{
    private static final Logger logger = LoggerFactory.getLogger(UsuarioDAOImpl.class);
    private final DatabaseManager dbManager;

    public UsuarioDAOImpl(){
        this.dbManager = DatabaseManager.getInstance();
    }

    @Override
    public Optional<Usuario> buscarPorUsuario(String p_usuario){
        String sql = "SELECT * FROM usuarios WHERE username = ? AND activo = 1";

        try(Connection con = dbManager.getConnection();
            PreparedStatement stmt = con.prepareStatement(sql)){
            stmt.setString(1, p_usuario);
            ResultSet rs = stmt.executeQuery();

            if(rs.next()){
                return Optional.of(mapaUsuarios(rs));
            }
        }catch (SQLException e){
            logger.error("Error al listar usuarios", e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Usuario> buscarPorId(Integer p_id) {
        String sql = "SELECT * FROM usuarios WHERE id = ?";

        try(Connection con = dbManager.getConnection();
            PreparedStatement stmt = con.prepareStatement(sql)){

            stmt.setInt(1, p_id);
            ResultSet rs = stmt.executeQuery();

            if(rs.next()){
                return Optional.of(mapaUsuarios(rs));
            }
        }catch (SQLException e){
            logger.error("Error al buscar usuario por ID: " + p_id, e);
        }

        return Optional.empty();
    }

    @Override
    public ArrayList<Usuario> buscarTodosLosUsuarios() {
        ArrayList<Usuario> usuarios = new ArrayList<Usuario>();
        String sql = "SELECT * FROM usuarios WHERE activo = 1 ORDER BY username";

        try (Connection conn = dbManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                usuarios.add(mapaUsuarios(rs));
            }

        } catch (SQLException e) {
            logger.error("Error al listar usuarios", e);
        }

        return usuarios;
    }

    @Override
    public boolean guardarUsuario(Usuario p_usuario) {
        String sql = "INSERT INTO usuarios (username, password, nombre_completo, rol) VALUES (?, ?, ?, ?)";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, p_usuario.getUsuario());
            stmt.setString(2, p_usuario.getContrasena());
            stmt.setString(3, p_usuario.getNombreCompleto());
            stmt.setString(4, p_usuario.getRol().name());
            stmt.setBoolean(5, p_usuario.getActivo());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        //revisar luego esta linea i es necesario
                        //p_usuario.setId(generatedKeys.getInt(1));
                    }
                }
                logger.info("Usuario creado: " + p_usuario.getUsuario());
                return true;
            }

        } catch (SQLException e) {
            logger.error("Error al guardar usuario: " + p_usuario.getUsuario(), e);
        }

        return false;
    }

    @Override
    public boolean modificarUsuario(Usuario p_usuario) {
        String sql = "UPDATE usuarios SET username = ?, password = ?, nombre_completo = ?, rol = ?, activo = ? WHERE id = ?";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, p_usuario.getUsuario());
            stmt.setString(2, p_usuario.getContrasena());
            stmt.setString(3, p_usuario.getNombreCompleto());
            stmt.setString(4, p_usuario.getRol().name());
            stmt.setBoolean(5, p_usuario.getActivo());
            stmt.setInt(6, p_usuario.getId());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                logger.info("Usuario actualizado: " + p_usuario.getUsuario());
                return true;
            }

        } catch (SQLException e) {
            logger.error("Error al actualizar usuario ID: " + p_usuario.getId(), e);
        }
        return false;
    }

    @Override
    public boolean eliminarUsuario(Integer p_id) {
        // Borrado lógico
        String sql = "UPDATE usuarios SET activo = 0 WHERE id = ?";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, p_id);
            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                logger.info("Usuario desactivado ID: " + p_id);
                return true;
            }

        } catch (SQLException e) {
            logger.error("Error al desactivar usuario ID: " + p_id, e);
        }
        return false;
    }

    @Override
    public boolean existeUsuario(String p_usuario) {
        String sql = "SELECT COUNT(*) FROM usuarios WHERE username = ?";

        try (Connection conn = dbManager.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, p_usuario);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            logger.error("Error al verificar existencia de username: " + p_usuario, e);
        }
        return false;
    }


    private Usuario mapaUsuarios(ResultSet rs) throws SQLException{
        Integer id = rs.getInt("id");
        String usuario = rs.getString("username");
        String contrasena = rs.getString("password");
        String nombreCompleto = rs.getString("nombre_completo");
        Usuario.Rol rol = Usuario.Rol.valueOf(rs.getString("rol"));
        boolean activo = rs.getBoolean("activo");
        Timestamp timestamp = rs.getTimestamp("fecha_creacion");
        LocalDateTime fecha = timestamp != null ? timestamp.toLocalDateTime() : null;

        Usuario user = new Usuario(id, usuario, contrasena, nombreCompleto,rol, activo, fecha);
        return user;
    }
}
