package com.taller.model;

import java.time.LocalDateTime;

public class Usuario {
    private Integer id;
    private String usuario;
    private String contrasena;
    private String nombreCompleto;
    private Rol rol;
    private boolean activo;
    private LocalDateTime fechaCreacion;

    // Enumeración para roles
    public enum Rol {
        ADMIN, EMPLEADO
    }

    //Constructor vacio
    public Usuario(Integer p_id, String p_usuario, String p_contrasena, String p_nombreCompleto, Rol p_rol, boolean p_activo, LocalDateTime p_fecha){
        this.setId(p_id);
        this.setUsuario(p_usuario);
        this.setContrasena(p_contrasena);
        this.setNombreCompleto(p_nombreCompleto);
        this.setRol(p_rol);
        this.setActivo(p_activo);
        this.setFechaCreacion(p_fecha);
    }
    // Constructor completo
    public Usuario(String p_usuario, String p_contrasena, String p_nombreCompleto, Rol p_rol) {
        this.setUsuario(p_usuario);
        this.setContrasena(p_contrasena);
        this.setNombreCompleto(p_nombreCompleto);
        this.setRol(p_rol);
        this.setActivo(true);
        this.setFechaCreacion(LocalDateTime.now());
    }

    //setters

    private void setId(Integer p_id) {
        this.id = p_id;
    }
    private void setUsuario(String p_usuario) {
        this.usuario = p_usuario;
    }
    private void setContrasena(String p_contrasena) {
        this.contrasena = p_contrasena;
    }
    private void setNombreCompleto(String p_nombreCompleto) {
        this.nombreCompleto = p_nombreCompleto;
    }
    private void setRol(Rol p_rol) {
        this.rol = p_rol;
    }
    private void setActivo(boolean activo) {
        this.activo = activo;
    }
    private void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    // Getters
    public Integer getId() {
        return this.id;
    }

    public String getUsuario() {
        return this.usuario;
    }

    public String getContrasena() {
        return this.contrasena;
    }

    public String getNombreCompleto() {
        return this.nombreCompleto;
    }

    public Rol getRol() {
        return this.rol;
    }

    public boolean getActivo() {
        return this.activo;
    }

    public LocalDateTime getFechaCreacion() {
        return this.fechaCreacion;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + this.getId() +
                ", usuario='" + this.getUsuario() + '\'' +
                ", nombreCompleto='" + this.getNombreCompleto() + '\'' +
                ", rol=" + this.getRol() +
                ", activo=" + this.getActivo() +
                '}';
    }
}
