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

        if (usuario.getContrasena() == null || usuario.getContrasena().trim().isEmpty()) {
            logger.warn("Intento de guardar usuario sin contraseña");
            return false;
        }

        // Verificar si el usuario ya existe
        if (usuarioDAO.existeUsuario(usuario.getUsuario())) {
            logger.warn("El usuario ya existe: " + usuario.getUsuario());
            return false;
        }

        // Encriptar contraseña antes de guardar
        String hashedPassword = BCrypt.hashpw(usuario.getContrasena(), BCrypt.gensalt());

        Usuario usuarioConHash = new Usuario(
                usuario.getUsuario(),
                hashedPassword,
                usuario.getNombreCompleto(),
                usuario.getRol()
        );

        boolean resultado = usuarioDAO.guardarUsuario(usuarioConHash);

        if (resultado) {
            logger.info("Usuario guardado exitosamente: " + usuario.getUsuario());
        }

        return resultado;
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

        // Verificar username duplicado (excepto el mismo usuario)
        Optional<Usuario> porUsuario = usuarioDAO.buscarPorUsuario(usuario.getUsuario());
        if (porUsuario.isPresent() && !porUsuario.get().getId().equals(usuario.getId())) {
            logger.warn("El nombre de usuario ya existe en otro usuario: " + usuario.getUsuario());
            return false;
        }

        boolean resultado = usuarioDAO.modificarUsuario(usuario);

        if (resultado) {
            logger.info("Usuario actualizado exitosamente: " + usuario.getUsuario());
        }

        return resultado;
    }

    public boolean cambiarContrasena(Integer id, String nuevaContrasena) {
        if (id == null || nuevaContrasena == null || nuevaContrasena.trim().isEmpty()) {
            logger.warn("Parámetros inválidos para cambiar contraseña");
            return false;
        }

        Optional<Usuario> usuarioOpt = usuarioDAO.buscarPorId(id);

        if (usuarioOpt.isEmpty()) {
            logger.warn("Usuario no encontrado para cambio de contraseña: " + id);
            return false;
        }

        Usuario usuario = usuarioOpt.get();
        String hashedPassword = BCrypt.hashpw(nuevaContrasena, BCrypt.gensalt());

        Usuario usuarioActualizado = new Usuario(
                usuario.getId(),
                usuario.getUsuario(),
                hashedPassword,
                usuario.getNombreCompleto(),
                usuario.getRol(),
                usuario.getActivo(),
                usuario.getFechaCreacion()
        );

        boolean resultado = usuarioDAO.modificarUsuario(usuarioActualizado);

        if (resultado) {
            logger.info("Contraseña cambiada exitosamente para usuario ID: " + id);
        }

        return resultado;
    }

    public boolean resetearContrasena(Integer id) {
        if (id == null) {
            logger.warn("ID inválido para resetear contraseña");
            return false;
        }

        Optional<Usuario> usuarioOpt = usuarioDAO.buscarPorId(id);

        if (usuarioOpt.isEmpty()) {
            logger.warn("Usuario no encontrado para resetear contraseña: " + id);
            return false;
        }

        Usuario usuario = usuarioOpt.get();

        // Contraseña temporal: username + "123"
        String contrasenaTemporal = usuario.getUsuario() + "123";
        String hashedPassword = BCrypt.hashpw(contrasenaTemporal, BCrypt.gensalt());

        Usuario usuarioActualizado = new Usuario(
                usuario.getId(),
                usuario.getUsuario(),
                hashedPassword,
                usuario.getNombreCompleto(),
                usuario.getRol(),
                usuario.getActivo(),
                usuario.getFechaCreacion()
        );

        boolean resultado = usuarioDAO.modificarUsuario(usuarioActualizado);

        if (resultado) {
            logger.info("Contraseña reseteada para usuario: " + usuario.getUsuario());
        }

        return resultado;
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
}
