package com.example.reflorestamentoproject.padrao;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class Desbaste {
    private int id;
    private int numero;
    private LocalDate data;
    private int arvoresCortadas;
    private double lenha;
    private double toretes;
    private double toras20a25;
    private double toras25a33;
    private double torasAcima33;
    private double totalLenha;
    private double totalToretes;
    private double totalToras20a25;
    private double totalToras25a33;
    private double totalTorasAcima33;
    private double total;
    private double precoLenha;
    private double precoToretes;
    private double precoToras20a25;
    private double precoToras25a33;
    private double precoTorasAcima33;
    private boolean previsao;
    private BooleanProperty selecionado = new SimpleBooleanProperty(false);
    private String status;

    public Desbaste(int id, int imovelId, int numero, LocalDate data, int arvoresCortadas, double lenha, double toretes, double toras20a25, double toras25a33, double torasAcima33, double totalLenha, double totalToretes, double totalToras20a25, double totalToras25a33, double totalTorasAcima33, double total, double precoLenha, double precoToretes, double precoToras20a25, double precoToras25a33, double precoTorasAcima33, String status) {
        this.id = id;
        this.numero = this.numero;
        this.data = data;
        this.arvoresCortadas = arvoresCortadas;
        this.lenha = lenha;
        this.toretes = toretes;
        this.toras20a25 = toras20a25;
        this.toras25a33 = toras25a33;
        this.torasAcima33 = torasAcima33;
        this.totalLenha = totalLenha;
        this.totalToretes = totalToretes;
        this.totalToras20a25 = totalToras20a25;
        this.totalToras25a33 = totalToras25a33;
        this.totalTorasAcima33 = totalTorasAcima33;
        this.total = total;
        this.precoLenha = precoLenha;
        this.precoToretes = precoToretes;
        this.precoToras20a25 = precoToras20a25;
        this.precoToras25a33 = precoToras25a33;
        this.precoTorasAcima33 = precoTorasAcima33;
        this.status = status;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getData() {
        return getDataFormatada(data);
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public int getArvoresCortadas() {
        return arvoresCortadas;
    }

    public boolean isPrevisao() {
        return previsao;
    }

    public void setPrevisao(boolean previsao) {
        this.previsao = previsao;
    }

    public void setArvoresCortadas(int arvoresCortadas) {
        this.arvoresCortadas = arvoresCortadas;
    }

    public double getLenha() {
        return lenha;
    }

    public void setLenha(double lenha) {
        this.lenha = lenha;
    }

    public double getToretes() {
        return toretes;
    }

    public void setToretes(double toretes) {
        this.toretes = toretes;
    }

    public double getToras20a25() {
        return toras20a25;
    }

    public void setToras20a25(double toras20a25) {
        this.toras20a25 = toras20a25;
    }

    public double getToras25a33() {
        return toras25a33;
    }

    public void setToras25a33(double toras25a33) {
        this.toras25a33 = toras25a33;
    }

    public double getTorasAcima33() {
        return torasAcima33;
    }

    public void setTorasAcima33(double torasAcima33) {
        this.torasAcima33 = torasAcima33;
    }

    public double getTotalLenha() {
        return totalLenha;
    }

    public void setTotalLenha(double totalLenha) {
        this.totalLenha = totalLenha;
    }

    public double getTotalToretes() {
        return totalToretes;
    }

    public void setTotalToretes(double totalToretes) {
        this.totalToretes = totalToretes;
    }

    public double getTotalToras20a25() {
        return totalToras20a25;
    }

    public void setTotalToras20a25(double totalToras20a25) {
        this.totalToras20a25 = totalToras20a25;
    }

    public double getTotalToras25a33() {
        return totalToras25a33;
    }

    public void setTotalToras25a33(double totalToras25a33) {
        this.totalToras25a33 = totalToras25a33;
    }

    public double getTotalTorasAcima33() {
        return totalTorasAcima33;
    }

    public void setTotalTorasAcima33(double totalTorasAcima33) {
        this.totalTorasAcima33 = totalTorasAcima33;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getPrecoLenha() {
        return precoLenha;
    }

    public void setPrecoLenha(double precoLenha) {
        this.precoLenha = precoLenha;
    }

    public double getPrecoToretes() {
        return precoToretes;
    }

    public void setPrecoToretes(double precoToretes) {
        this.precoToretes = precoToretes;
    }

    public double getPrecoToras20a25() {
        return precoToras20a25;
    }

    public void setPrecoToras20a25(double precoToras20a25) {
        this.precoToras20a25 = precoToras20a25;
    }

    public double getPrecoToras25a33() {
        return precoToras25a33;
    }

    public void setPrecoToras25a33(double precoToras25a33) {
        this.precoToras25a33 = precoToras25a33;
    }

    public double getPrecoTorasAcima33() {
        return precoTorasAcima33;
    }

    public void setPrecoTorasAcima33(double precoTorasAcima33) {
        this.precoTorasAcima33 = precoTorasAcima33;
    }

    // Métodos para cálculos de totais
    public double calcularTotalLenha() {
        return this.lenha * this.precoLenha;
    }

    public double calcularTotalToretes() {
        return this.toretes * this.precoToretes;
    }

    public double calcularTotalToras20a25() {
        return this.toras20a25 * this.precoToras20a25;
    }

    public double calcularTotalToras25a33() {
        return this.toras25a33 * this.precoToras25a33;
    }

    public double calcularTotalTorasAcima33() {
        return this.torasAcima33 * this.precoTorasAcima33;
    }

    public double calcularTotalGeral() {
        return calcularTotalLenha() + calcularTotalToretes() + calcularTotalToras20a25() + calcularTotalToras25a33() + calcularTotalTorasAcima33();
    }

    // Método para retornar a data formatada no padrão brasileiro
    public String getDataFormatada(LocalDate data) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return data.format(formatter);
    }

    public void setFieldValue(String fieldName, Object value) {
        switch (fieldName) {
            case "data":
                this.setData((LocalDate) value);
                break;
            case "arvoresCortadas":
                this.setArvoresCortadas((int) value);
                break;
            case "lenha":
                this.setLenha((double) value);
                break;
            case "toretes":
                this.setToretes((double) value);
                break;
            case "toras20a25":
                this.setToras20a25((double) value);
                break;
            case "toras25a33":
                this.setToras25a33((double) value);
                break;
            case "torasAcima33":
                this.setTorasAcima33((double) value);
                break;
            case "totalLenha":
                this.setTotalLenha((double) value);
                break;
            case "totalToretes":
                this.setTotalToretes((double) value);
                break;
            case "totalToras20a25":
                this.setTotalToras20a25((double) value);
                break;
            case "totalToras25a33":
                this.setTotalToras25a33((double) value);
                break;
            case "totalTorasAcima33":
                this.setTotalTorasAcima33((double) value);
                break;
            case "total":
                this.setTotal((double) value);
                break;
            case "precoLenha":
                this.setPrecoLenha((double) value);
                break;
            case "precoToretes":
                this.setPrecoToretes((double) value);
                break;
            case "precoToras20a25":
                this.setPrecoToras20a25((double) value);
                break;
            case "precoToras25a33":
                this.setPrecoToras25a33((double) value);
                break;
            case "precoTorasAcima33":
                this.setPrecoTorasAcima33((double) value);
                break;
            default:
                throw new IllegalArgumentException("Campo não reconhecido: " + fieldName);
        }
    }
    // Getter e Setter para o BooleanProperty
    public BooleanProperty selecionadoProperty() {
        return selecionado;
    }

    public boolean isSelecionado() {
        return selecionado.get();
    }

    public void setSelecionado(boolean selecionado) {
        this.selecionado.set(selecionado);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
