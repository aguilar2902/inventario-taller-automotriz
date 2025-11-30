package com.taller.dao.interfaces;

import com.taller.model.Usuario;

import java.util.ArrayList;
import java.util.Optional;

public interface IUsuarioDAO {
    Optional<Usuario> buscarPorUsuario(String p_usuario);
    Optional<Usuario> buscarPorId(Integer p_id);
    ArrayList<Usuario> buscarTodosLosUsuarios();
    boolean guardarUsuario(Usuario p_usuario);
    boolean modificarUsuario(Usuario p_usuario);
    boolean eliminarUsuario(Integer p_id);
    boolean existeUsuario(String p_usuario);
    boolean actualizarPassword(int id, String nuevaPasswordHash);
}
