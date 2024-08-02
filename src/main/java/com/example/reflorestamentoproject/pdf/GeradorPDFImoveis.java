package com.example.reflorestamentoproject.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;

public class GeradorPDFImoveis {
    public void generateReport(Connection connection, VBox commonVBox, Map<String, String> columnMap, String reportType) {
        Document document = new Document(PageSize.A4, 36, 36, 90, 36); // Margens modernas
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialFileName("relatorio_imoveis.pdf");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf"));
        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            try {
                PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
                document.open();

                // Título com estilo moderno
                Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20);
                Paragraph title = new Paragraph("Relatório de Imóveis - " + reportType, titleFont);
                title.setAlignment(Element.ALIGN_CENTER);
                document.add(title);

                // Tabela com estilo moderno
                PdfPTable table = new PdfPTable(getSelectedFieldsCount(commonVBox));
                table.setWidthPercentage(100); // Largura total da página
                table.setSpacingBefore(20); // Espaçamento antes da tabela
                table.setSpacingAfter(20); // Espaçamento depois da tabela

                // Cabeçalhos da tabela
                Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
                for (Node node : commonVBox.getChildren()) {
                    if (node instanceof CheckBox && ((CheckBox) node).isSelected()) {
                        PdfPCell cell = new PdfPCell(new Phrase(((CheckBox) node).getText(), headerFont));
                        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        table.addCell(cell);
                    }
                }

                String query = "";
                if (reportType.equals("Arrendado")) {
                    query = "SELECT i.*, ia.* FROM imoveis i " +
                            "JOIN imoveis_arrendados ia ON i.id = ia.imovel_id " +
                            "WHERE i.classe = 'arrendado'";
                } else if (reportType.equals("Próprio")) {
                    query = "SELECT i.* FROM imoveis i " +
                            "WHERE i.classe = 'proprio'";
                } else {
                    query = "SELECT i.*, ia.*, ip.* FROM imoveis i " +
                            "LEFT JOIN imoveis_arrendados ia ON i.id = ia.imovel_id " +
                            "LEFT JOIN imoveis_proprios ip ON i.id = ip.imovel_id";
                }

                PreparedStatement statement = connection.prepareStatement(query);
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    for (Node node : commonVBox.getChildren()) {
                        if (node instanceof CheckBox && ((CheckBox) node).isSelected()) {
                            String columnName = columnMap.get(((CheckBox) node).getText());
                            table.addCell(resultSet.getString(columnName));
                        }
                    }
                }

                document.add(table);
                document.close();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Relatório Gerado");
                alert.setHeaderText(null);
                alert.setContentText("O relatório foi gerado e salvo com sucesso.");
                alert.showAndWait();
            } catch (Exception e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erro ao Gerar Relatório");
                alert.setHeaderText(null);
                alert.setContentText("Ocorreu um erro ao gerar o relatório.");
                alert.showAndWait();
            }
        }
    }

    private int getSelectedFieldsCount(VBox... vBoxes) {
        int count = 0;
        for (VBox vBox : vBoxes) {
            for (Node node : vBox.getChildren()) {
                if (node instanceof CheckBox && ((CheckBox) node).isSelected()) {
                    count++;
                }
            }
        }
        return count;
    }
}
