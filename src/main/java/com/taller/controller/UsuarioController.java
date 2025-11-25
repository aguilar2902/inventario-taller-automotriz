package com.taller.controller;

import com.taller.model.Usuario;
import com.taller.service.UsuarioService;
import com.taller.view.UsuariosView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Optional;

public class UsuarioController {
    private static final Logger logger = LoggerFactory.getLogger(UsuarioController.class);
    private final UsuariosView view;
    private final UsuarioService usuarioService;

    public UsuarioController(UsuariosView view) {
        this.view = view;
        this.usuarioService = UsuarioService.getInstance();
    }

    public void cargarUsuarios() {
        logger.info("Cargando todos los usuarios");
        try {
            ArrayList<Usuario> usuarios = usuarioService.obtenerTodos();
            view.actualizarTabla(usuarios);
            actualizarEstadisticas();
            logger.info("Se cargaron " + usuarios.size() + " usuarios");
        } catch (Exception e) {
            logger.error("Error al cargar usuarios", e);
            view.actualizarTabla(new ArrayList<>());
        }
    }

    public Usuario obtenerUsuarioPorId(Integer id) {
        logger.info("Obteniendo usuario con ID: " + id);
        try {
            Optional<Usuario> usuario = usuarioService.obtenerPorId(id);
            if (usuario.isPresent()) {
                return usuario.get();
            } else {
                logger.warn("Usuario no encontrado con ID: " + id);
                return null;
            }
        } catch (Exception e) {
            logger.error("Error al obtener usuario", e);
            return null;
        }
    }

    public boolean guardarUsuario(Usuario usuario) {
        logger.info("Intentando guardar usuario: " + usuario.getUsuario());
        try {
            boolean resultado = usuarioService.guardar(usuario);
            if (resultado) {
                logger.info("Usuario guardado exitosamente: " + usuario.getUsuario());
            } else {
                logger.warn("No se pudo guardar el usuario: " + usuario.getUsuario());
            }
            return resultado;
        } catch (Exception e) {
            logger.error("Error al guardar usuario", e);
            return false;
        }
    }

    public boolean actualizarUsuario(Usuario usuario) {
        logger.info("Intentando actualizar usuario ID: " + usuario.getId());
        try {
            boolean resultado = usuarioService.actualizar(usuario);
            if (resultado) {
                logger.info("Usuario actualizado exitosamente ID: " + usuario.getId());
            } else {
                logger.warn("No se pudo actualizar el usuario ID: " + usuario.getId());
            }
            return resultado;
        } catch (Exception e) {
            logger.error("Error al actualizar usuario", e);
            return false;
        }
    }

    public boolean cambiarContrasena(Integer id, String nuevaContrasena) {
        logger.info("Intentando cambiar contraseña para usuario ID: " + id);
        try {
            boolean resultado = usuarioService.cambiarContrasena(id, nuevaContrasena);
            if (resultado) {
                logger.info("Contraseña cambiada exitosamente para usuario ID: " + id);
            } else {
                logger.warn("No se pudo cambiar la contraseña para usuario ID: " + id);
            }
            return resultado;
        } catch (Exception e) {
            logger.error("Error al cambiar contraseña", e);
            return false;
        }
    }

    public boolean resetearContrasena(Integer id) {
        logger.info("Intentando resetear contraseña para usuario ID: " + id);
        try {
            boolean resultado = usuarioService.resetearContrasena(id);
            if (resultado) {
                logger.info("Contraseña reseteada exitosamente para usuario ID: " + id);
            } else {
                logger.warn("No se pudo resetear la contraseña para usuario ID: " + id);
            }
            return resultado;
        } catch (Exception e) {
            logger.error("Error al resetear contraseña", e);
            return false;
        }
    }

    public boolean eliminarUsuario(Integer id) {
        logger.info("Intentando eliminar usuario ID: " + id);
        try {
            boolean resultado = usuarioService.eliminar(id);
            if (resultado) {
                logger.info("Usuario eliminado exitosamente ID: " + id);
            } else {
                logger.warn("No se pudo eliminar el usuario ID: " + id);
            }
            return resultado;
        } catch (Exception e) {
            logger.error("Error al eliminar usuario", e);
            return false;
        }
    }

    private void actualizarEstadisticas() {
        try {
            int total = usuarioService.contarTotal();
            view.actualizarEstadisticas(total);
        } catch (Exception e) {
            logger.error("Error al actualizar estadísticas", e);
            view.actualizarEstadisticas(0);
        }
    }

    public boolean validarUsuarioUnico(String usuario) {
        return !usuarioService.existeUsuario(usuario);
    }
}