package com.taller.service;

import com.taller.dao.impl.UsuarioDAOImpl;
import com.taller.dao.interfaces.IUsuarioDAO;
import com.taller.model.Usuario;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class AuthService {
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
    private static AuthService instance;
    private final IUsuarioDAO usuarioDAO;
    private Usuario usuarioActual;

    private AuthService() {
        this.usuarioDAO = new UsuarioDAOImpl();
    }

    public static AuthService getInstance() {
        if (instance == null) {
            synchronized (AuthService.class) {
                if (instance == null) {
                    instance = new AuthService();
                }
            }
        }
        return instance;
    }

    public boolean login(String p_usuario, String p_pass) {
        try {
            Optional<Usuario> usuarioOpt = usuarioDAO.buscarPorUsuario(p_usuario);

            if (usuarioOpt.isPresent()) {
                Usuario usuario = usuarioOpt.get();

                // Verificar si tiene contraseña configurada
                if (usuario.getContrasena() == null || usuario.getContrasena().isEmpty()) {
                    logger.warn("Usuario sin contraseña configurada: " + p_usuario);
                    return false;
                }

                // Verificar contraseña
                if (BCrypt.checkpw(p_pass, usuario.getContrasena())) {
                    this.usuarioActual = usuario;
                    logger.info("Login exitoso para usuario: " + p_usuario);
                    return true;
                } else {
                    logger.warn("Contraseña incorrecta para usuario: " + p_usuario);
                }
            } else {
                logger.warn("Usuario no encontrado: " + p_usuario);
            }

        } catch (Exception e) {
            logger.error("Error durante el login", e);
        }

        return false;
    }

    /**
     * Login para primer acceso (sin contraseña)
     * Solo valida que el usuario exista y no tenga contraseña
     */
    public boolean loginPrimerAcceso(String p_usuario) {
        try {
            Optional<Usuario> usuarioOpt = usuarioDAO.buscarPorUsuario(p_usuario);

            if (usuarioOpt.isPresent()) {
                Usuario usuario = usuarioOpt.get();

                if (usuario.getRol() == Usuario.Rol.ADMIN) {
                    logger.warn("ADMIN intentó primer login sin contraseña: " + p_usuario);
                    return false;
                }
                // Verificar que NO tenga contraseña o que requiera cambio
                if (usuario.getContrasena() == null || usuario.getContrasena().isEmpty() || usuario.getRequiereCambioPassword()) {
                    this.usuarioActual = usuario;
                    logger.info("Primer login exitoso para usuario: " + p_usuario);
                    return true;
                } else {
                    logger.warn("Usuario ya tiene contraseña configurada: " + p_usuario);
                }
            } else {
                logger.warn("Usuario no encontrado: " + p_usuario);
            }

        } catch (Exception e) {
            logger.error("Error durante el primer login", e);
        }

        return false;
    }

    /**
     * Establece la contraseña por primera vez
     */
    public boolean establecerContrasenaPrimerLogin(String nuevaContrasena) {
        if (usuarioActual == null) {
            logger.error("No hay usuario autenticado para cambiar la contraseña");
            return false;
        }

        String hash = BCrypt.hashpw(nuevaContrasena, BCrypt.gensalt());

        boolean actualizado = usuarioDAO.actualizarPassword(usuarioActual.getId(), hash);

        if (actualizado) {
            logger.info("Contraseña establecida para usuario: " + usuarioActual.getUsuario());
            logger.info("Contraseña: " + nuevaContrasena);

            usuarioActual = usuarioDAO.buscarPorId(usuarioActual.getId()).orElse(null);
        }

        return actualizado;
    }
    public void logout() {
        if (usuarioActual != null) {
            logger.info("Logout de usuario: " + usuarioActual.getUsuario());
            this.usuarioActual = null;
        }
    }

    public Usuario getUsuarioActual() {
        return usuarioActual;
    }

    public boolean isAuthenticated() {
        return usuarioActual != null;
    }

    public boolean isAdmin() {
        return isAuthenticated() && usuarioActual.getRol().equals(Usuario.Rol.ADMIN);
    }

    public boolean requiereCambioPassword() {
        return isAuthenticated() && usuarioActual.getRequiereCambioPassword();
    }

    public boolean actualizarPassword(int id, String nuevaPasswordPlano) {
        // encriptar
        String hash = BCrypt.hashpw(nuevaPasswordPlano, BCrypt.gensalt());

        return usuarioDAO.actualizarPassword(id, hash);
    }

}
