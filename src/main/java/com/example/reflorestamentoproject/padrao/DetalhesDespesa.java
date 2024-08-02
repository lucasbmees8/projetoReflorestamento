package com.example.reflorestamentoproject.padrao;

import java.text.DecimalFormat;
import java.util.Locale;

public class DetalhesDespesa {
    private String produto;
    private int quantidade;
    private String unidade;
    private double valorUnitario;
    private double valorTotal;
    private double percentual;

    public DetalhesDespesa(String produto, int quantidade, String unidade, double valorUnitario, double valorTotal) {
        this.produto = produto;
        this.quantidade = quantidade;
        this.unidade = unidade;
        this.valorUnitario = valorUnitario;
        this.valorTotal = valorTotal;
    }

    public String getProduto() {
        // Capitaliza a primeira letra do produto
        return produto.substring(0, 1).toUpperCase(Locale.ROOT) + produto.substring(1);
    }

    public int getQuantidade() {
        return quantidade;
    }

    public String getUnidade() {
        // Retorna a unidade em maiúsculo
        return unidade.toUpperCase();
    }

    public double getValorUnitario() {
        return valorUnitario;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public double getPercentual() {
        return percentual;
    }

    public String getValorUnitarioFormatado() {
        DecimalFormat df = new DecimalFormat("#,##0.00");
        return "R$ " + df.format(valorUnitario);
    }

    public String getValorTotalFormatado() {
        DecimalFormat df = new DecimalFormat("#,##0.00");
        return "R$ " + df.format(valorTotal);
    }

    public String getPercentualFormatado() {
        DecimalFormat df = new DecimalFormat("#0.00");
        return df.format(percentual) + "%";
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public void setValorUnitario(double valorUnitario) {
        this.valorUnitario = valorUnitario;
    }

    public void setPercentual(double percentual) {
        this.percentual = percentual;
    }
}