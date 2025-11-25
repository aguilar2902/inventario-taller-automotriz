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


}
