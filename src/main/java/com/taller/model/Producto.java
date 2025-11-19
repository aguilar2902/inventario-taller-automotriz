package com.taller.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Producto {
    private Integer id;
    private String codigo;
    private String nombre;
    private String descripcion;
    private int stockActual;
    private int stockMinimo;
    private BigDecimal precioUnitario;
    private boolean activo;
    private LocalDateTime fechaCreacion;
    private Categoria categoria;

    public Producto(String p_codigo, String p_nombre, String p_descripcion, int p_stockActual, int p_stockMinimo, BigDecimal p_precioUnitario, boolean p_activo, Categoria p_categoria) {
        this.setCodigo(p_codigo);
        this.setNombre(p_nombre);
        this.setDescripcion(p_descripcion);
        this.setStockActual(p_stockActual);
        this.setStockMinimo(p_stockMinimo);
        this.setPrecioUnitario(p_precioUnitario);
        this.setActivo(p_activo);
        this.setFechaCreacion(LocalDateTime.now());
        this.setCategoria(p_categoria);
    }

    private void setCodigo(String p_codigo){
        this.codigo = p_codigo;
    }
    private void setNombre(String p_nombre){
        this.nombre = p_nombre;
    }
    private void setDescripcion(String p_descripcion){
        this.descripcion = p_descripcion;
    }
    private void setStockActual(int p_stockActual){
        this.stockActual = p_stockActual;
    }
    private void setStockMinimo(int p_stockMinimo){
        this.stockMinimo = p_stockMinimo;
    }
    private void setPrecioUnitario(BigDecimal p_precioUnit){
        this.precioUnitario = p_precioUnit;
    }
    private void setActivo(boolean p_activo){
        this.activo = p_activo;
    }
    private void setFechaCreacion(LocalDateTime p_fecha){
        this.fechaCreacion = p_fecha;
    }
    private void setCategoria(Categoria p_categoria){
        this.categoria = p_categoria;
    }

    public Integer getId() {
        return this.id;
    }
    public String getCodigo() {
        return this.codigo;
    }
    public String getNombre() {
        return this.nombre;
    }
    public String getDescripcion() {
        return this.descripcion;
    }
    public int getStockActual() {
        return this.stockActual;
    }
    public int getStockMinimo() {
        return this.stockMinimo;
    }
    public BigDecimal getPrecioUnitario() {
        return this.precioUnitario;
    }
    public boolean getActivo() {
        return this.activo;
    }
    public LocalDateTime getFechaCreacion() {
        return this.fechaCreacion;
    }
    public Categoria getCategoria(){
        return this.categoria;
    }

    @Override
    public String toString() {
        return "Producto{" +
                "id=" + this.getId() +
                ", codigo='" + this.getCodigo() + '\'' +
                ", nombre='" + this.getNombre() + '\'' +
                ", stockActual=" + this.getStockActual() +
                ", stockMinimo=" + this.getStockMinimo() +
                ", precioUnitario=" + this.getPrecioUnitario() +
                '}';
    }
}
