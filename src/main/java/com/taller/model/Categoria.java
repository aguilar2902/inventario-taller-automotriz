package com.taller.model;

public class Categoria {
    private Integer id;
    private String nombre;
    private boolean activo;

    public Categoria(Integer p_id, String p_nombre, boolean p_activo){
        this.setId(p_id);
        this.setNombre(p_nombre);
        this.setActivo(p_activo);
    }
    public Categoria(String p_nombre, boolean p_activo){
        this.setNombre(p_nombre);
        this.setActivo(p_activo);
    }

    private  void setId(Integer p_id){
        this.id = p_id;
    }
    private void setNombre(String p_nombre){
        this.nombre = p_nombre;
    }
    private void setActivo(boolean p_activo){
        this.activo = p_activo;
    }

    public Integer getId(){
        return this.id;
    }

    public String getNombre(){
        return this.nombre;
    }

    public  boolean getActivo(){
        return this.activo;
    }

    @Override
    public String toString() {
        return this.getNombre(); // Para mostrar en ComboBox
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Categoria categoria = (Categoria) o;
        return this.getId() != null && this.getId().equals(categoria.id);
    }

    @Override
    public int hashCode() {
        return this.getId() != null ? this.getId().hashCode() : 0;
    }
}
