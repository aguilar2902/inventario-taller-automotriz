package com.taller.service;

import com.taller.dao.impl.UsuarioDAOImpl;
import com.taller.dao.interfaces.IUsuarioDAO;
import com.taller.model.Usuario;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Optional;

public class UsuarioService {
    private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class);
    private static UsuarioService instance;
    private final IUsuarioDAO usuarioDAO;

    private UsuarioService() {
        this.usuarioDAO = new UsuarioDAOImpl();
    }

    public static UsuarioService getInstance() {
        if (instance == null) {
            synchronized (UsuarioService.class) {
                if (instance == null) {
                    instance = new UsuarioService();
                }
            }
        }
        return instance;
    }

    public ArrayList<Usuario> obtenerTodos() {
        return usuarioDAO.buscarTodosLosUsuarios();
    }

    public Optional<Usuario> obtenerPorId(Integer id) {
        return usuarioDAO.buscarPorId(id);
    }

    public Optional<Usuario> obtenerPorUsuario(String usuario) {
        return usuarioDAO.buscarPorUsuario(usuario);
    }

    public boolean guardar(Usuario usuario) {
        // Validaciones
        if (usuario.getUsuario() == null || usuario.getUsuario().trim().isEmpty()) {
            logger.warn("Intento de guardar usuario sin nombre de usuario");
            return false;
        }

        if (usuario.getNombreCompleto() == null || usuario.getNombreCompleto().trim().isEmpty()) {
            logger.warn("Intento de guardar usuario sin nombre completo");
            return false;
        }

        // Verificar usuario duplicado
        if (usuarioDAO.existeUsuario(usuario.getUsuario())) {
            logger.warn("El usuario ya existe: " + usuario.getUsuario());
            return false;
        }

        // Si viene con contraseña, encriptarla antes de guardar
        if (usuario.getContrasena() != null && !usuario.getContrasena().isEmpty()) {
            String hashedPassword = BCrypt.hashpw(usuario.getContrasena(), BCrypt.gensalt());
            // Crear nuevo usuario con contraseña encriptada
            Usuario usuarioConHashedPassword = new Usuario(
                    usuario.getUsuario(),
                    hashedPassword,
                    usuario.getNombreCompleto(),
                    usuario.getRol()

            );

            return usuarioDAO.guardarUsuario(usuarioConHashedPassword);
        }

        // Si no tiene contraseña (primer login)
        return usuarioDAO.guardarUsuario(usuario);
    }

    public boolean actualizar(Usuario usuario) {
        if (usuario.getId() == null) {
            logger.warn("Intento de actualizar usuario sin ID");
            return false;
        }

        // Verificar que existe
        Optional<Usuario> existente = usuarioDAO.buscarPorId(usuario.getId());
        if (existente.isEmpty()) {
            logger.warn("Usuario no encontrado para actualizar: " + usuario.getId());
            return false;
        }

        return usuarioDAO.modificarUsuario(usuario);
    }

    public boolean eliminar(Integer id) {
        return usuarioDAO.eliminarUsuario(id);
    }

    public int contarTotal() {
        return usuarioDAO.buscarTodosLosUsuarios().size();
    }

    public boolean validarContrasena(String contrasena, String hash) {
        try {
            return BCrypt.checkpw(contrasena, hash);
        } catch (Exception e) {
            logger.error("Error al validar contraseña", e);
            return false;
        }
    }

    public boolean existeUsuario(String usuario) {
        return usuarioDAO.existeUsuario(usuario);
    }
    /**
     * Cambia la contraseña de un usuario (desde el CRUD de usuarios)
     */
    public boolean cambiarContrasena(Integer id, String nuevaContrasena) {
        logger.info("Intentando cambiar contraseña para usuario ID: " + id);

        try {
            Optional<Usuario> usuarioOpt = usuarioDAO.buscarPorId(id);

            if (usuarioOpt.isEmpty()) {
                logger.warn("Usuario no encontrado para cambiar contraseña: " + id);
                return false;
            }

            Usuario usuario = usuarioOpt.get();

            // Encriptar nueva contraseña
            String hashedPassword = BCrypt.hashpw(nuevaContrasena, BCrypt.gensalt());

            // Crear usuario actualizado
            Usuario usuarioActualizado = new Usuario(
                    usuario.getId(),
                    usuario.getUsuario(),
                    hashedPassword,
                    usuario.getNombreCompleto(),
                    usuario.getRol(),
                    usuario.getActivo(),
                    false,  // Ya no requiere cambio de contraseña
                    usuario.getFechaCreacion()
            );

            boolean resultado = usuarioDAO.modificarUsuario(usuarioActualizado);

            if (resultado) {
                logger.info("Contraseña cambiada exitosamente para usuario ID: " + id);
            }

            return resultado;

        } catch (Exception e) {
            logger.error("Error al cambiar contraseña para usuario ID: " + id, e);
            return false;
        }
    }

    /**
     * BLANQUEA la contraseña del usuario (solo admin puede hacer esto)
     * Establece contraseña en NULL y marca requiere_cambio_password = TRUE
     */
    public boolean blanquearContrasena(Integer id) {
        logger.info("Intentando blanquear contraseña para usuario ID: " + id);

        try {
            Optional<Usuario> usuarioOpt = usuarioDAO.buscarPorId(id);

            if (usuarioOpt.isEmpty()) {
                logger.warn("Usuario no encontrado para blanquear contraseña: " + id);
                return false;
            }

            Usuario usuario = usuarioOpt.get();

            // No permitir blanquear la contraseña del propio admin logueado
            AuthService authService = AuthService.getInstance();
            if (authService.getUsuarioActual() != null &&
                    authService.getUsuarioActual().getId().equals(id)) {
                logger.warn("No se puede blanquear la contraseña del usuario actual");
                return false;
            }

            // Crear usuario con contraseña NULL y requiere cambio
            Usuario usuarioBlanqueado = new Usuario(
                    usuario.getId(),
                    usuario.getUsuario(),
                    null,  // Contraseña NULL
                    usuario.getNombreCompleto(),
                    usuario.getRol(),
                    usuario.getActivo(),
                    true,  // REQUIERE cambio de contraseña
                    usuario.getFechaCreacion()
            );

            boolean resultado = usuarioDAO.modificarUsuario(usuarioBlanqueado);

            if (resultado) {
                logger.info("Contraseña blanqueada exitosamente para usuario ID: " + id);
            }

            return resultado;

        } catch (Exception e) {
            logger.error("Error al blanquear contraseña para usuario ID: " + id, e);
            return false;
        }
    }

    /**
     * RESETEA la contraseña a un valor por defecto (username + "123")
     * Y marca requiere_cambio_password = TRUE
     */
    public boolean resetearContrasena(Integer id) {
        logger.info("Intentando resetear contraseña para usuario ID: " + id);

        try {
            Optional<Usuario> usuarioOpt = usuarioDAO.buscarPorId(id);

            if (usuarioOpt.isEmpty()) {
                logger.warn("Usuario no encontrado para resetear contraseña: " + id);
                return false;
            }

            Usuario usuario = usuarioOpt.get();

            // No permitir resetear la contraseña del propio admin logueado
            AuthService authService = AuthService.getInstance();
            if (authService.getUsuarioActual() != null &&
                    authService.getUsuarioActual().getId().equals(id)) {
                logger.warn("No se puede resetear la contraseña del usuario actual");
                return false;
            }

            // Contraseña temporal: username + "123"
            String contrasenaTemporal = usuario.getUsuario() + "123";
            String hashedPassword = BCrypt.hashpw(contrasenaTemporal, BCrypt.gensalt());

            // Crear usuario con contraseña temporal
            Usuario usuarioReseteado = new Usuario(
                    usuario.getId(),
                    usuario.getUsuario(),
                    hashedPassword,
                    usuario.getNombreCompleto(),
                    usuario.getRol(),
                    usuario.getActivo(),
                    true,  // REQUIERE cambio de contraseña
                    usuario.getFechaCreacion()
            );

            boolean resultado = usuarioDAO.modificarUsuario(usuarioReseteado);

            if (resultado) {
                logger.info("Contraseña reseteada a temporal para usuario ID: " + id);
            }

            return resultado;

        } catch (Exception e) {
            logger.error("Error al resetear contraseña para usuario ID: " + id, e);
            return false;
        }
    }
}
