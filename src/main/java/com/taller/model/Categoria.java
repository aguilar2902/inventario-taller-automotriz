package com.taller.model;

public class Categoria {
    private Integer id;
    private String nombre;

    public Categoria(String p_nombre){
        this.setNombre(p_nombre);
    }

    private void setNombre(String p_nombre){
        this.nombre = p_nombre;
    }

    public Integer getId(){
        return this.id;
    }

    public String getNombre(){
        return this.nombre;
    }
}
