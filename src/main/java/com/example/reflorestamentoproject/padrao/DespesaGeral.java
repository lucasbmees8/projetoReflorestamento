package com.example.reflorestamentoproject.padrao;

import javafx.beans.property.*;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DespesaGeral {
    private IntegerProperty id;
    private IntegerProperty imovelId;
    private ObjectProperty<LocalDate> data;
    private StringProperty descricao;
    private StringProperty numeroNotaFiscal;
    private StringProperty fornecedor;
    private StringProperty produto;
    private StringProperty unidade;
    private IntegerProperty quantidade;
    private DoubleProperty valorUnitario;
    private DoubleProperty total;
    private BooleanProperty selecionado;
    private StringProperty descricaoImovel; // Novo atributo para a descrição do imóvel

    public DespesaGeral(int id, int imovelId, LocalDate data, String descricao, String numeroNotaFiscal, String fornecedor, String produto, String unidade, int quantidade, double valorUnitario, double total, String descricaoImovel) {
        this.id = new SimpleIntegerProperty(id);
        this.imovelId = new SimpleIntegerProperty(imovelId);
        this.data = new SimpleObjectProperty<>(data);
        this.descricao = new SimpleStringProperty(descricao);
        this.numeroNotaFiscal = new SimpleStringProperty(numeroNotaFiscal);
        this.fornecedor = new SimpleStringProperty(fornecedor);
        this.produto = new SimpleStringProperty(produto);
        this.unidade = new SimpleStringProperty(unidade);
        this.quantidade = new SimpleIntegerProperty(quantidade);
        this.valorUnitario = new SimpleDoubleProperty(valorUnitario);
        this.total = new SimpleDoubleProperty(total);
        this.selecionado = new SimpleBooleanProperty(false);
        this.descricaoImovel = new SimpleStringProperty(descricaoImovel); // Inicializando a nova propriedade
    }

    // Métodos getter e setter para os atributos
    // Você pode gerar esses métodos automaticamente na sua IDE
    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public int getImovelId() {
        return imovelId.get();
    }

    public IntegerProperty imovelIdProperty() {
        return imovelId;
    }

    public void setImovelId(int imovelId) {
        this.imovelId.set(imovelId);
    }

    public LocalDate getData() {
        return data.get();
    }

    public ObjectProperty<LocalDate> dataProperty() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data.set(data);
    }

    // Método para formatar a data no formato dia/mês/ano
    public String getDataFormatada() {
        return data.get().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    public String getDescricao() {
        return descricao.get();
    }

    public StringProperty descricaoProperty() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao.set(descricao);
    }

    public String getNumeroNotaFiscal() {
        return numeroNotaFiscal.get();
    }

    public StringProperty numeroNotaFiscalProperty() {
        return numeroNotaFiscal;
    }

    public void setNumeroNotaFiscal(String numeroNotaFiscal) {
        this.numeroNotaFiscal.set(numeroNotaFiscal);
    }

    public String getFornecedor() {
        return fornecedor.get();
    }

    public StringProperty fornecedorProperty() {
        return fornecedor;
    }

    public void setFornecedor(String fornecedor) {
        this.fornecedor.set(fornecedor);
    }

    public String getProduto() {
        return produto.get();
    }

    public StringProperty produtoProperty() {
        return produto;
    }

    public void setProduto(String produto) {
        this.produto.set(produto);
    }

    public String getUnidade() {
        return unidade.get();
    }

    public StringProperty unidadeProperty() {
        return unidade;
    }

    public void setUnidade(String unidade) {
        this.unidade.set(unidade);
    }

    public int getQuantidade() {
        return quantidade.get();
    }

    public IntegerProperty quantidadeProperty() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade.set(quantidade);
    }

    public double getValorUnitario() {
        return valorUnitario.get();
    }

    public DoubleProperty valorUnitarioProperty() {
        return valorUnitario;
    }

    public void setValorUnitario(double valorUnitario) {
        this.valorUnitario.set(valorUnitario);
    }

    public double getTotal() {
        return total.get();
    }

    public DoubleProperty totalProperty() {
        return total;
    }

    public void setTotal(double total) {
        this.total.set(total);
    }

    public boolean isSelecionado() {
        return selecionado.get();
    }

    public BooleanProperty selecionadoProperty() {
        return selecionado;
    }

    public void setSelecionado(boolean selecionado) {
        this.selecionado.set(selecionado);
    }

    public String getDescricaoImovel() {
        return descricaoImovel.get();
    }

    public StringProperty descricaoImovelProperty() {
        return descricaoImovel;
    }

    public void setDescricaoImovel(String descricaoImovel) {
        this.descricaoImovel.set(descricaoImovel);
    }

    // Método para formatar o valor unitário com a moeda BRL (Real brasileiro)
    public String getValorUnitarioFormatado() {
        NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        return nf.format(getValorUnitario());
    }

    // Método para calcular o total e formatá-lo com a moeda BRL (Real brasileiro)
    public String getTotalFormatado() {
        double totalCalculado = getQuantidade() * getValorUnitario();
        NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        return nf.format(totalCalculado);
    }
}
