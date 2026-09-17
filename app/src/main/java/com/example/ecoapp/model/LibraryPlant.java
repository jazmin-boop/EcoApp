package com.example.ecoapp.model;

public class LibraryPlant {
    private String nombre;
    private String especie;
    private String categoria;
    private String descripcion;
    private String origen;
    private String riegoInfo;
    private String tag1, tag2, tag3;
    private int imageResId;
    private String imageUrl;

    public LibraryPlant(String nombre, String especie, String categoria, String descripcion, String origen, String riegoInfo, String tag1, String tag2, String tag3, int imageResId, String imageUrl) {
        this.nombre = nombre;
        this.especie = especie;
        this.categoria = categoria;
        this.descripcion = descripcion;
        this.origen = origen;
        this.riegoInfo = riegoInfo;
        this.tag1 = tag1;
        this.tag2 = tag2;
        this.tag3 = tag3;
        this.imageResId = imageResId;
        this.imageUrl = imageUrl;
    }

    public String getNombre() { return nombre; }
    public String getEspecie() { return especie; }
    public String getCategoria() { return categoria; }
    public String getDescripcion() { return descripcion; }
    public String getOrigen() { return origen; }
    public String getRiegoInfo() { return riegoInfo; }
    public String getTag1() { return tag1; }
    public String getTag2() { return tag2; }
    public String getTag3() { return tag3; }
    public int getImageResId() { return imageResId; }
    public String getImageUrl() { return imageUrl; }
}
