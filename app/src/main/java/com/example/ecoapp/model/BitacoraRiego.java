package com.example.ecoapp.model;

public class BitacoraRiego {
    private int idRiego;
    private int plantaId;
    private String fechaHora;
    private double temperaturaMomento;
    private int llovio;
    private String estadoTierra;

    public BitacoraRiego(int idRiego, int plantaId, String fechaHora, double temperaturaMomento, int llovio, String estadoTierra) {
        this.idRiego = idRiego;
        this.plantaId = plantaId;
        this.fechaHora = fechaHora;
        this.temperaturaMomento = temperaturaMomento;
        this.llovio = llovio;
        this.estadoTierra = estadoTierra;
    }

    public int getIdRiego() { return idRiego; }
    public int getPlantaId() { return plantaId; }
    public String getFechaHora() { return fechaHora; }
    public double getTemperaturaMomento() { return temperaturaMomento; }
    public int getLlovio() { return llovio; }
    public String getEstadoTierra() { return estadoTierra; }
}
