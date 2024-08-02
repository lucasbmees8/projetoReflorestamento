package com.example.reflorestamentoproject.pages;

import com.example.reflorestamentoproject.padrao.DespesaGeral;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DespesasGerais {

    public void show(Stage primaryStage, LoginController loginController, Connection connection) {
        primaryStage.setTitle("Despesas Gerais");

        // Definindo a paleta de cores
        Font.loadFont(getClass().getResourceAsStream("/fonts/Roboto-Medium.ttf"), 16);
        String backgroundColor = "#294B29";
        String textColor = "#DBE7C9";
        String buttonColor = "#50623A";
        String borderColor = "#789461";
        String buttonStyle = "-fx-background-color: " + buttonColor + "; -fx-text-fill: " + textColor + "; -fx-font-size: 16; -fx-font-family: 'Roboto Medium'; -fx-border-color: " + borderColor + "; -fx-background-radius: 10; -fx-border-radius: 10;";

        // Criando a TableView para exibir as despesas
        TableView<DespesaGeral> tableView = new TableView<>();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.setStyle("-fx-background-color: " + backgroundColor + "; -fx-text-fill: " + textColor + ";");

        // Criando as colunas da tabela
        TableColumn<DespesaGeral, String> dataColumn = new TableColumn<>("Data");
        dataColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDataFormatada()));

        TableColumn<DespesaGeral, String> descricaoColumn = new TableColumn<>("Descrição");
        descricaoColumn.setCellValueFactory(data -> data.getValue().descricaoProperty());

        TableColumn<DespesaGeral, String> notaFiscalColumn = new TableColumn<>("Número Nota Fiscal");
        notaFiscalColumn.setCellValueFactory(data -> data.getValue().numeroNotaFiscalProperty());

        TableColumn<DespesaGeral, String> fornecedorColumn = new TableColumn<>("Fornecedor");
        fornecedorColumn.setCellValueFactory(data -> data.getValue().fornecedorProperty());

        TableColumn<DespesaGeral, String> produtoColumn = new TableColumn<>("Produto");
        produtoColumn.setCellValueFactory(data -> data.getValue().produtoProperty());

        TableColumn<DespesaGeral, String> unidadeColumn = new TableColumn<>("Unidade");
        unidadeColumn.setCellValueFactory(data -> data.getValue().unidadeProperty());

        TableColumn<DespesaGeral, Integer> quantidadeColumn = new TableColumn<>("Quantidade");
        quantidadeColumn.setCellValueFactory(data -> data.getValue().quantidadeProperty().asObject());

        TableColumn<DespesaGeral, String> valorUnitarioColumn = new TableColumn<>("Valor Unitário");
        valorUnitarioColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getValorUnitarioFormatado()));

        TableColumn<DespesaGeral, String> totalColumn = new TableColumn<>("Total");
        totalColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTotalFormatado()));

        TableColumn<DespesaGeral, String> imovelDescricaoColumn = new TableColumn<>("Descrição do Imóvel");
        imovelDescricaoColumn.setCellValueFactory(data -> data.getValue().descricaoImovelProperty());

        // Adicionando as colunas à TableView
        tableView.getColumns().addAll(dataColumn, descricaoColumn, notaFiscalColumn, fornecedorColumn, produtoColumn, unidadeColumn, quantidadeColumn, valorUnitarioColumn, totalColumn, imovelDescricaoColumn);
        tableView.setStyle("-fx-background-color: " + backgroundColor + "; -fx-control-inner-background: " + backgroundColor + "; -fx-text-fill: " + textColor + "; -fx-font-family: 'Roboto Medium'");

        // Carregando os dados das despesas
        ObservableList<DespesaGeral> despesas = FXCollections.observableArrayList();
        loadDespesas(connection, despesas);

        // Configurando os dados da TableView
        tableView.setItems(despesas);

        // Criando o botão para gerar o relatório
        Button relatorioButton = new Button("Gerar Relatório");
        relatorioButton.setStyle(buttonStyle);
        relatorioButton.setOnAction(event -> showDateRangePopup(primaryStage, despesas));

        Button backButton = new Button("Voltar");
        backButton.setOnAction(event -> loginController.redirectToMainPage(primaryStage, connection));
        backButton.setStyle(buttonStyle);

        // Criando o layout VBox para organizar a TableView e os botões
        HBox buttonsBox = new HBox(relatorioButton, backButton);
        buttonsBox.setSpacing(10);

        VBox vbox = new VBox(tableView, buttonsBox);
        vbox.setSpacing(10);
        vbox.setPadding(new Insets(20));
        vbox.setBackground(new Background(new BackgroundFill(Color.web(backgroundColor), CornerRadii.EMPTY, Insets.EMPTY)));

        Scene scene = new Scene(vbox);
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    // Método para carregar as despesas do banco de dados
    private void loadDespesas(Connection connection, ObservableList<DespesaGeral> despesas) {
        String query = "SELECT d.*, i.descricao AS descricao_imovel FROM despesas d JOIN imoveis i ON d.imovel_id = i.id";
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int imovelId = resultSet.getInt("imovel_id");
                LocalDate data = resultSet.getDate("data").toLocalDate();
                String descricao = resultSet.getString("descricao");
                String numeroNotaFiscal = resultSet.getString("numero_nota_fiscal");
                String fornecedor = resultSet.getString("fornecedor");
                String produto = resultSet.getString("produto");
                String unidade = resultSet.getString("unidade");
                int quantidade = resultSet.getInt("quantidade");
                double valorUnitario = resultSet.getDouble("valor_unitario");
                double total = resultSet.getDouble("total");
                String descricaoImovel = resultSet.getString("descricao_imovel");
                DespesaGeral despesa = new DespesaGeral(id, imovelId, data, descricao, numeroNotaFiscal, fornecedor, produto, unidade, quantidade, valorUnitario, total, descricaoImovel);
                despesas.add(despesa);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Lidar com exceções adequadamente
        }
    }

    // Método para mostrar o popup de seleção de data
    private void showDateRangePopup(Stage primaryStage, ObservableList<DespesaGeral> despesas) {
        String backgroundColor = "#294B29";
        String textColor = "#DBE7C9";
        String buttonColor = "#50623A";
        String borderColor = "#789461";
        String buttonStyle = "-fx-background-color: " + buttonColor + "; -fx-text-fill: " + textColor + "; -fx-font-size: 16; -fx-font-family: 'Roboto Medium'; -fx-border-color: " + borderColor + "; -fx-background-radius: 10; -fx-border-radius: 10;";
        String labelStyle = "-fx-text-fill: " + textColor + "; -fx-font-family: 'Roboto Medium'; -fx-font-size: 14;";
        String datePickerStyle = "-fx-background-color: " + backgroundColor + "; -fx-text-fill: " + textColor + ";";

        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Selecionar Período");

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(20));
        vbox.setBackground(new Background(new BackgroundFill(Color.web(backgroundColor), CornerRadii.EMPTY, Insets.EMPTY)));

        Label startDateLabel = new Label("Data Inicial:");
        startDateLabel.setStyle(labelStyle);
        DatePicker startDatePicker = new DatePicker();
        startDatePicker.setStyle(datePickerStyle);

        Label endDateLabel = new Label("Data Final:");
        endDateLabel.setStyle(labelStyle);
        DatePicker endDatePicker = new DatePicker();
        endDatePicker.setStyle(datePickerStyle);

        Button generateButton = new Button("Gerar Relatório");
        generateButton.setStyle(buttonStyle);
        generateButton.setOnAction(event -> {
            LocalDate startDate = startDatePicker.getValue();
            LocalDate endDate = endDatePicker.getValue();
            if (startDate != null && endDate != null && !endDate.isBefore(startDate)) {
                ObservableList<DespesaGeral> filteredDespesas = filterDespesasByDate(despesas, startDate, endDate);
                showSaveDialogAndGenerateReport(primaryStage, filteredDespesas);
                popupStage.close();
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Datas Inválidas");
                alert.setHeaderText(null);
                alert.setContentText("Por favor, selecione um período válido.");
                alert.showAndWait();
            }
        });

        vbox.getChildren().addAll(startDateLabel, startDatePicker, endDateLabel, endDatePicker, generateButton);

        Scene scene = new Scene(vbox);
        popupStage.setScene(scene);
        popupStage.showAndWait();
    }

    // Método para filtrar as despesas pelo período selecionado
    private ObservableList<DespesaGeral> filterDespesasByDate(ObservableList<DespesaGeral> despesas, LocalDate startDate, LocalDate endDate) {
        return despesas.stream()
                .filter(despesa -> !despesa.getData().isBefore(startDate) && !despesa.getData().isAfter(endDate))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
    }

    // Método para mostrar o diálogo de salvamento e gerar o relatório
    private void showSaveDialogAndGenerateReport(Stage primaryStage, List<DespesaGeral> despesas) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Salvar Relatório");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        File file = fileChooser.showSaveDialog(primaryStage);
        if (file != null) {
            generateReport(despesas, file);
            openFile(file);
        }
    }

    // Método para gerar o relatório em PDF
    private void generateReport(List<DespesaGeral> despesas, File file) {
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();

            // Adicionando conteúdo ao documento
            document.add(new Paragraph("Relatório de Despesas Gerais"));

            PdfPTable table = new PdfPTable(10);
            Stream.of("Data", "Descrição", "Número Nota Fiscal", "Fornecedor", "Produto", "Unidade", "Quantidade", "Valor Unitário", "Total", "Descrição do Imóvel")
                    .forEach(headerTitle -> {
                        PdfPCell header = new PdfPCell();
                        header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        header.setBorderWidth(2);
                        header.setPhrase(new Phrase(headerTitle));
                        table.addCell(header);
                    });

            for (DespesaGeral despesa : despesas) {
                table.addCell(despesa.getDataFormatada());
                table.addCell(despesa.getDescricao());
                table.addCell(despesa.getNumeroNotaFiscal());
                table.addCell(despesa.getFornecedor());
                table.addCell(despesa.getProduto());
                table.addCell(despesa.getUnidade());
                table.addCell(String.valueOf(despesa.getQuantidade()));
                table.addCell(despesa.getValorUnitarioFormatado());
                table.addCell(despesa.getTotalFormatado());
                table.addCell(despesa.getDescricaoImovel());
            }

            document.add(table);

        } catch (DocumentException | IOException e) {
            e.printStackTrace();
            // Lidar com exceções adequadamente
        } finally {
            document.close();
        }
    }

    // Método para abrir o arquivo PDF gerado
    private void openFile(File file) {
        try {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(file);
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Informação");
                alert.setHeaderText(null);
                alert.setContentText("Relatório salvo em: " + file.getAbsolutePath());
                alert.showAndWait();
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Lidar com exceções adequadamente
        }
    }
}
