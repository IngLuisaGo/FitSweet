package com.example.fitsweet.models;

public class CarritoItem {
    private int id;
    private int productoId;
    private String nombre;
    private String descripcion;
    private double precio;
    private int cantidad;
    private String imagenUrl;

    public CarritoItem(int id, int productoId, String nombre, String descripcion, double precio, int cantidad, String imagenUrl) {
        this.id = id;
        this.productoId = productoId;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.cantidad = cantidad;
        this.imagenUrl = imagenUrl;
    }

    public int getId() {
        return id;
    }

    public int getProductoId() {
        return productoId;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public double getPrecio() {
        return precio;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getTotal() {
        return precio * cantidad;
    }

    public String getImagenUrl() { return imagenUrl; }
}
