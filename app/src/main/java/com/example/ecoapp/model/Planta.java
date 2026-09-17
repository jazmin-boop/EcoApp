package com.example.ecoapp.model;

public class Planta {
    private int idPlanta;
    private String nombre;
    private String especie;
    private String fechaSiembra;
    private int frecuenciaRiegoDias;
    private String ubicacion;
    private String notas;
    private int racha; // Racha de cumplimiento de tareas

    public Planta(int idPlanta, String nombre, String especie, String fechaSiembra, int frecuenciaRiegoDias, String ubicacion, String notas) {
        this.idPlanta = idPlanta;
        this.nombre = nombre;
        this.especie = especie;
        this.fechaSiembra = fechaSiembra;
        this.frecuenciaRiegoDias = frecuenciaRiegoDias;
        this.ubicacion = ubicacion;
        this.notas = notas;
        this.racha = 0;
    }

    public Planta(int idPlanta, String nombre, String especie, String fechaSiembra, int frecuenciaRiegoDias, String ubicacion, String notas, int racha) {
        this.idPlanta = idPlanta;
        this.nombre = nombre;
        this.especie = especie;
        this.fechaSiembra = fechaSiembra;
        this.frecuenciaRiegoDias = frecuenciaRiegoDias;
        this.ubicacion = ubicacion;
        this.notas = notas;
        this.racha = racha;
    }

    public int getIdPlanta() { return idPlanta; }
    public void setIdPlanta(int idPlanta) { this.idPlanta = idPlanta; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEspecie() { return especie; }
    public void setEspecie(String especie) { this.especie = especie; }

    public String getFechaSiembra() { return fechaSiembra; }

    public int getFrecuenciaRiegoDias() { return frecuenciaRiegoDias; }

    public String getUbicacion() { return ubicacion; }

    public String getNotas() { return notas; }
    
    public int getRacha() { return racha; }
    public void setRacha(int racha) { this.racha = racha; }
}
