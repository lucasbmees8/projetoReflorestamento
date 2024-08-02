package com.example.reflorestamentoproject.pages;

import com.itextpdf.text.Font;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

public class RelatorioPage extends Stage {
    private final int imovelId;
    private final Connection connection;

    public RelatorioPage(int imovelId, Connection connection) {
        this.imovelId = imovelId;
        this.connection = connection;
        setTitle("Gerar Relatório");

        // Definindo o estilo
        String backgroundColor = "#294B29";
        String textColor = "#DBE7C9";
        String borderColor = "#789461";
        String buttonColor = "#50623A";
        String fieldStyle = "-fx-font-family: 'Roboto'; -fx-font-size: 14px; -fx-pref-width: 200px; -fx-background-color: #DBE7C9; -fx-text-fill: #294B29; -fx-border-color: " + borderColor + "; -fx-background-radius: 10; -fx-border-radius: 10;";
        String buttonStyle = "-fx-background-color: " + buttonColor + "; -fx-text-fill: " + textColor + "; -fx-font-size: 16; -fx-font-family: 'Roboto Medium'; -fx-border-color: " + borderColor + "; -fx-background-radius: 15; -fx-border-radius: 15;";

        // Campos de Data
        DatePicker startDatePicker = new DatePicker();
        startDatePicker.setPromptText("Data Inicial");
        startDatePicker.setStyle(fieldStyle);

        DatePicker endDatePicker = new DatePicker();
        endDatePicker.setPromptText("Data Final");
        endDatePicker.setStyle(fieldStyle);

        // Botão Gerar Relatório
        Button gerarButton = new Button("Gerar");
        gerarButton.setStyle(buttonStyle);
        gerarButton.setOnAction(event -> {
            LocalDate startDate = startDatePicker.getValue();
            LocalDate endDate = endDatePicker.getValue();
            if (startDate != null && endDate != null) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Salvar Relatório");
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
                File file = fileChooser.showSaveDialog(this);
                if (file != null) {
                    gerarRelatorio(startDate, endDate, file);
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Aviso");
                alert.setHeaderText("Datas inválidas");
                alert.setContentText("Por favor, selecione uma data inicial e uma data final.");
                alert.showAndWait();
            }
        });

        // Layout
        VBox layout = new VBox(10, startDatePicker, endDatePicker, gerarButton);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: " + backgroundColor);

        // Cena
        Scene scene = new Scene(layout, 400, 200);
        setScene(scene);
    }

    private void gerarRelatorio(LocalDate startDate, LocalDate endDate, File file) {
        String sql = "SELECT data, fornecedor, quantidade, valor_unitario FROM despesas WHERE imovel_id = ? AND data BETWEEN ? AND ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, imovelId);
            stmt.setDate(2, java.sql.Date.valueOf(startDate));
            stmt.setDate(3, java.sql.Date.valueOf(endDate));
            ResultSet rs = stmt.executeQuery();

            Document document = new Document(PageSize.A4.rotate()); // Orientação horizontal
            PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.BLACK);
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.BLACK);
            Font bodyFont = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.BLACK);

            Paragraph title = new Paragraph("Relatório de Despesas", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            Paragraph dateRange = new Paragraph("Despesas Geradas de " + startDate.format(dateFormatter) + " a " + endDate.format(dateFormatter), bodyFont);
            dateRange.setAlignment(Element.ALIGN_CENTER);
            document.add(dateRange);

            document.add(new Paragraph(" ")); // Espaçamento

            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);
            table.setWidths(new float[]{2f, 3f, 3f, 2f, 2f});

            // Cabeçalhos
            String[] headers = {"Data", "Empresa", "Documento", "Fornecedor", "Valor Total"};
            for (String header : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(header, headerFont));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                table.addCell(cell);
            }

            // Dados
            int totalDespesas = 0;
            double valorTotalGeral = 0.0;
            while (rs.next()) {
                totalDespesas++;
                LocalDate data = rs.getDate("data").toLocalDate();
                String fornecedor = rs.getString("fornecedor");
                int quantidade = rs.getInt("quantidade");
                double valorUnitario = rs.getDouble("valor_unitario");
                double valorTotal = quantidade * valorUnitario;

                valorTotalGeral += valorTotal;

                table.addCell(new PdfPCell(new Phrase(data.format(dateFormatter), bodyFont)));
                table.addCell(new PdfPCell(new Phrase("Empresa Fictícia", bodyFont)));
                table.addCell(new PdfPCell(new Phrase("Documento Fictício", bodyFont)));
                table.addCell(new PdfPCell(new Phrase(fornecedor, bodyFont)));
                table.addCell(new PdfPCell(new Phrase(String.format("R$ %.2f", valorTotal), bodyFont)));
            }

            document.add(table);

            // Informações adicionais
            document.add(new Paragraph(" "));
            Paragraph totalDespesasParagraph = new Paragraph("Total de Despesas: " + totalDespesas, bodyFont);
            totalDespesasParagraph.setAlignment(Element.ALIGN_RIGHT);
            document.add(totalDespesasParagraph);

            Paragraph valorTotalParagraph = new Paragraph("Valor Total: R$ " + String.format("%.2f", valorTotalGeral), bodyFont);
            valorTotalParagraph.setAlignment(Element.ALIGN_RIGHT);
            document.add(valorTotalParagraph);

            Paragraph footer = new Paragraph("Emitido por: Sistema de Gerenciamento de Despesas - Usuário: SeuNome", bodyFont);
            footer.setAlignment(Element.ALIGN_RIGHT);
            document.add(footer);

            document.close();

            // Abrindo o arquivo PDF gerado
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(file);
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Relatório Gerado");
            alert.setHeaderText("Relatório gerado com sucesso");
            alert.setContentText("O relatório foi salvo como '" + file.getName() + "'.");
            alert.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Erro ao gerar relatório");
            alert.setContentText("Ocorreu um erro ao gerar o relatório. Tente novamente.");
            alert.showAndWait();
        }
    }
}
