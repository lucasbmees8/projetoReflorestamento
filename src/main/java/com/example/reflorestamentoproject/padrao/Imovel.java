package com.example.reflorestamentoproject.padrao;

import java.time.LocalDate;

public class Imovel {
    private int id;
    private String descricao;
    private double areaImovel;
    private double areaPlantio;
    private String especie;
    private String origem;
    private int numArvoresPlantadas;
    private LocalDate dataPlantio;
    private String numeroCCIR;
    private String numeroITR;
    private String proprietario;
    private String matricula;
    private String municipio;
    private String localidade;
    private String classe;
    private double alturaDesrama; // Para imóveis próprios
    private LocalDate dataContrato; // Para imóveis arrendados
    private LocalDate vencimentoContrato; // Para imóveis arrendados
    private String arrendatario; // Para imóveis arrendados

    // Construtores
    public Imovel() {}

    // Construtor para imóveis próprios
    public Imovel(String descricao, double areaImovel, double areaPlantio, String especie, String origem, int numArvoresPlantadas,
                  LocalDate dataPlantio, String numeroCCIR, String numeroITR, String proprietario, String matricula,
                  String municipio, String localidade, double alturaDesrama) {
        this(descricao, areaImovel, areaPlantio, especie, origem, numArvoresPlantadas, dataPlantio, numeroCCIR, numeroITR,
                proprietario, matricula, municipio, localidade, "proprio");
        this.alturaDesrama = alturaDesrama;
    }

    // Construtor para imóveis arrendados
    public Imovel(String descricao, double areaImovel, double areaPlantio, String especie, String origem, int numArvoresPlantadas,
                  LocalDate dataPlantio, String numeroCCIR, String numeroITR, String proprietario, String matricula,
                  String municipio, String localidade, LocalDate dataContrato, LocalDate vencimentoContrato, String arrendatario) {
        this(descricao, areaImovel, areaPlantio, especie, origem, numArvoresPlantadas, dataPlantio, numeroCCIR, numeroITR,
                proprietario, matricula, municipio, localidade, "arrendado");
        this.dataContrato = dataContrato;
        this.vencimentoContrato = vencimentoContrato;
        this.arrendatario = arrendatario;
    }

    // Construtor base para ambos os tipos
    private Imovel(String descricao, double areaImovel, double areaPlantio, String especie, String origem, int numArvoresPlantadas,
                   LocalDate dataPlantio, String numeroCCIR, String numeroITR, String proprietario, String matricula,
                   String municipio, String localidade, String classe) {
        this.descricao = descricao;
        this.areaImovel = areaImovel;
        this.areaPlantio = areaPlantio;
        this.especie = especie;
        this.origem = origem;
        this.numArvoresPlantadas = numArvoresPlantadas;
        this.dataPlantio = dataPlantio;
        this.numeroCCIR = numeroCCIR;
        this.numeroITR = numeroITR;
        this.proprietario = proprietario;
        this.matricula = matricula;
        this.municipio = municipio;
        this.localidade = localidade;
        this.classe = classe;
    }

    // Getters e Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getAreaImovel() {
        return areaImovel;
    }

    public void setAreaImovel(double areaImovel) {
        this.areaImovel = areaImovel;
    }

    public double getAreaPlantio() {
        return areaPlantio;
    }

    public void setAreaPlantio(double areaPlantio) {
        this.areaPlantio = areaPlantio;
    }

    public String getEspecie() {
        return especie;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    public String getOrigem() {
        return origem;
    }

    public void setOrigem(String origem) {
        this.origem = origem;
    }

    public int getNumArvoresPlantadas() {
        return numArvoresPlantadas;
    }

    public void setNumArvoresPlantadas(int numArvoresPlantadas) {
        this.numArvoresPlantadas = numArvoresPlantadas;
    }

    public LocalDate getDataPlantio() {
        return dataPlantio;
    }

    public void setDataPlantio(LocalDate dataPlantio) {
        this.dataPlantio = dataPlantio;
    }

    public String getNumeroCCIR() {
        return numeroCCIR;
    }

    public void setNumeroCCIR(String numeroCCIR) {
        this.numeroCCIR = numeroCCIR;
    }

    public String getProprietario() {
        return proprietario;
    }

    public void setProprietario(String proprietario) {
        this.proprietario = proprietario;
    }

    public String getNumeroITR() {
        return numeroITR;
    }

    public void setNumeroITR(String numeroITR) {
        this.numeroITR = numeroITR;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getLocalidade() {
        return localidade;
    }

    public void setLocalidade(String localidade) {
        this.localidade = localidade;
    }

    public String getClasse() {
        return classe;
    }

    public void setClasse(String classe) {
        this.classe = classe;
    }

    public double getAlturaDesrama() {
        return alturaDesrama;
    }

    public void setAlturaDesrama(double alturaDesrama) {
        this.alturaDesrama = alturaDesrama;
    }

    public LocalDate getDataContrato() {
        return dataContrato;
    }

    public void setDataContrato(LocalDate dataContrato) {
        this.dataContrato = dataContrato;
    }

    public LocalDate getVencimentoContrato() {
        return vencimentoContrato;
    }

    public void setVencimentoContrato(LocalDate vencimentoContrato) {
        this.vencimentoContrato = vencimentoContrato;
    }

    public String getArrendatario() {
        return arrendatario;
    }

    public void setArrendatario(String arrendatario) {
        this.arrendatario = arrendatario;
    }
    // ... (inclua todos os getters e setters para os atributos da classe)

    // Método para calcular o número de árvores por hectare
    public double getNumArvoresPorHectare() {
        return areaPlantio > 0 ? numArvoresPlantadas / areaPlantio : 0;
    }
}
