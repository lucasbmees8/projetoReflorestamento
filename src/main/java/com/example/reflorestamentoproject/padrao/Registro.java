package com.example.reflorestamentoproject.padrao;

public class Registro {
    private int imovelId;
    private double capMedio;
    private double alturaMedia;
    private double dapMedio;

    public Registro(int imovelId, double capMedio, double alturaMedia, double dapMedio) {
        this.imovelId = imovelId;
        this.capMedio = capMedio;
        this.alturaMedia = alturaMedia;
        this.dapMedio = dapMedio;
    }

    public int getImovelId() {
        return imovelId;
    }

    public void setImovelId(int imovelId) {
        this.imovelId = imovelId;
    }

    public double getCapMedio() {
        return capMedio;
    }

    public void setCapMedio(double capMedio) {
        this.capMedio = capMedio;
    }

    public double getAlturaMedia() {
        return alturaMedia;
    }

    public void setAlturaMedia(double alturaMedia) {
        this.alturaMedia = alturaMedia;
    }

    public double getDapMedio() {
        return dapMedio;
    }

    public void setDapMedio(double dapMedio) {
        this.dapMedio = dapMedio;
    }
}