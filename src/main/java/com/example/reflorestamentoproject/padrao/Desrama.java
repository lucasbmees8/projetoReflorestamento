package com.example.reflorestamentoproject.padrao;

import javafx.beans.property.*;

import java.util.Date;

public class Desrama {
        private final SimpleIntegerProperty id;
        private final SimpleIntegerProperty imovelId;
        private final SimpleIntegerProperty numero;
        private final SimpleObjectProperty<Date> dataPrevista;
        private final SimpleObjectProperty<Date> dataRealizacao;
        private final SimpleDoubleProperty altura;
        private final SimpleStringProperty status;

        public Desrama(int id, int imovelId, int numero, Date dataPrevista, Date dataRealizacao, Double altura, String status) {
            this.id = new SimpleIntegerProperty(id);
            this.imovelId = new SimpleIntegerProperty(imovelId);
            this.numero = new SimpleIntegerProperty(numero);
            this.dataPrevista = new SimpleObjectProperty<>(dataPrevista);
            this.dataRealizacao = new SimpleObjectProperty<>(dataRealizacao);
            this.altura = new SimpleDoubleProperty(altura);
            this.status = new SimpleStringProperty(status);
        }

        public int getId() {
            return id.get();
        }

        public void setId(int id) {
            this.id.set(id);
        }

        public IntegerProperty idProperty() {
            return id;
        }

        public int getImovelId() {
            return imovelId.get();
        }

        public void setImovelId(int imovelId) {
            this.imovelId.set(imovelId);
        }

        public IntegerProperty imovelIdProperty() {
            return imovelId;
        }

        public int getNumero() {
            return numero.get();
        }

        public void setNumero(int numero) {
            this.numero.set(numero);
        }

        public IntegerProperty numeroProperty() {
            return numero;
        }

        public Date getDataPrevista() {
            return dataPrevista.get();
        }

        public void setDataPrevista(Date dataPrevista) {
            this.dataPrevista.set(dataPrevista);
        }

        public ObjectProperty<Date> dataPrevistaProperty() {
            return dataPrevista;
        }

        public Date getDataRealizacao() {
            return dataRealizacao.get();
        }

        public void setDataRealizacao(Date dataRealizacao) {
            this.dataRealizacao.set(dataRealizacao);
        }

        public ObjectProperty<Date> dataRealizacaoProperty() {
            return dataRealizacao;
        }

        public Double getAltura() {
            return altura.get();
        }

        public void setAltura(Double altura) {
            this.altura.set(altura);
        }

        public DoubleProperty alturaProperty() {
            return altura;
        }

        public String getStatus() {
            return status.get();
        }

        public void setStatus(String status) {
            this.status.set(status);
        }

        public StringProperty statusProperty() {
            return status;
        }
    }
