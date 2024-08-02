package com.example.reflorestamentoproject.padrao;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class Despesa {
    private final int id;
    private final int imovelId; // Novo campo para o ID do imóvel
    private final LocalDate data;
    private final String descricao;
    private final String numeroNotaFiscal;
    private final String fornecedor;
    private final String produto;
    private final String unidade;
    private int quantidade; // Agora modificável
    private double valorUnitario; // Agora modificável
    private double valorTotal;
    private String tipoDeDespesa;
    private final Date validade;
    private BooleanProperty selected;

    public Despesa(int id, int imovelId, LocalDate data, String descricao, String numeroNotaFiscal, String fornecedor, String produto, String unidade, int quantidade, double valorUnitario, double valorTotal, String tipoDeDespesa, Date validade) {
        this.id = id;
        this.imovelId = imovelId;
        this.data = data;
        this.descricao = descricao;
        this.numeroNotaFiscal = numeroNotaFiscal;
        this.fornecedor = fornecedor;
        this.produto = produto;
        this.unidade = unidade;
        this.quantidade = quantidade;
        this.valorUnitario = valorUnitario;
        this.valorTotal = valorTotal;
        this.tipoDeDespesa = tipoDeDespesa;
        this.validade = validade;
        this.selected = new SimpleBooleanProperty(false);
    }

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public String getData() {
        return dateFormatter.format(data);
    }

    public String getDescricao() {
        return descricao;
    }

    public String getNumeroNotaFiscal() {
        return numeroNotaFiscal;
    }

    public String getTipoDeDespesa() {
        return tipoDeDespesa;
    }

    public void setTipoDeDespesa(String tipoDeDespesa) {
        this.tipoDeDespesa = tipoDeDespesa;
    }

    public String getFornecedor() {
        return fornecedor;
    }

    public String getProduto() {
        return produto;
    }

    public String getUnidade() {
        return unidade;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public int getId() {
        return id;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
        recalculateValorTotal();
    }

    public double getValorUnitario() {
        NumberFormat nf = DecimalFormat.getCurrencyInstance(new Locale("pt", "BR"));
        return valorUnitario;
    }

    public void setValorUnitario(double valorUnitario) {
        this.valorUnitario = valorUnitario;
        recalculateValorTotal();
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public boolean isSelected() {
        return selected.get();
    }

    public void setSelected(boolean selected) {
        this.selected.set(selected);
    }

    public BooleanProperty selectedProperty() {
        return selected;
    }

    public int getImovelId() {
        return imovelId;
    }

    public void recalculateValorTotal() {
        this.valorTotal = this.quantidade * this.valorUnitario;
    }

    public Date getValidade() {
        return validade;
    }
}
