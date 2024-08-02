package com.example.reflorestamentoproject.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class GeradorPDFImovel {
    private static final Map<String, String> columnNamesMap = new HashMap<>();

    static {
        columnNamesMap.put("descricao", "Descrição");
        columnNamesMap.put("area_imovel", "Área do Imóvel");
        columnNamesMap.put("area_plantio", "Área de Plantio");
        columnNamesMap.put("especie", "Espécie");
        columnNamesMap.put("origem", "Origem");
        columnNamesMap.put("num_arvores_plantadas", "Número de Árvores Plantadas");
        columnNamesMap.put("num_arvores_cortadas", "Número de Árvores Cortadas");
        columnNamesMap.put("arvores_remanescentes", "Número de Árvores Remanescentes");
        columnNamesMap.put("matricula", "Matrícula");
        columnNamesMap.put("data_plantio", "Data de Plantio");
        columnNamesMap.put("data_contrato", "Data do Contrato");
        columnNamesMap.put("vencimento_contrato", "Vencimento do Contrato");
        columnNamesMap.put("numero_ccir", "Número do CCIR");
        columnNamesMap.put("numero_itr", "Número do ITR");
        columnNamesMap.put("proprietario", "Proprietário");
        columnNamesMap.put("arrendatario", "Arrendatário");
        columnNamesMap.put("municipio", "Município");
        columnNamesMap.put("localidade", "Localidade");
        columnNamesMap.put("altura_desrama", "Altura de Desrama");
        columnNamesMap.put("classe", "Classe");
        columnNamesMap.put("arvores_cortadas", "Árvores Cortadas");
        columnNamesMap.put("num_arvores_por_hectare", "Número de árvores por hectare");
    }

    public static void generatePDFReport(int imovelId, Connection connection, Window window) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Salvar Relatório");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        File file = fileChooser.showSaveDialog(window);

        if (file != null) {
            try {
                // Consultando os detalhes do imóvel
                String query = "SELECT descricao, area_imovel, area_plantio, especie, origem, num_arvores_plantadas, " +
                        "arvores_remanescentes, arvores_cortadas, num_arvores_por_hectare, matricula, data_plantio, " +
                        "numero_ccir, numero_itr, proprietario, municipio, localidade, classe, " +
                        "ia.data_contrato, ia.vencimento_contrato, ia.arrendatario " + // Adicionando campos de imoveis_arrendados
                        "FROM imoveis i " +
                        "LEFT JOIN imoveis_arrendados ia ON i.id = ia.imovel_id " + // Junta opcional com imoveis_arrendados
                        "WHERE i.id = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, imovelId);
                ResultSet resultSet = statement.executeQuery();

                // Criando o documento PDF
                Document document = new Document();
                PdfWriter.getInstance(document, new FileOutputStream(file));
                document.open();

                // Adicionando o título ao documento PDF
                Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
                Paragraph title = new Paragraph("Relatório do Imóvel", titleFont);
                title.setAlignment(Element.ALIGN_CENTER);
                document.add(title);
                document.add(new Paragraph(" ")); // Espaço em branco

                // Adicionando os detalhes do imóvel ao documento PDF
                if (resultSet.next()) {
                    PdfPTable table = new PdfPTable(2);
                    table.setWidthPercentage(80);  // Ajustando a largura da tabela
                    table.setWidths(new int[]{1, 2});

                    Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
                    Font valueFont = FontFactory.getFont(FontFactory.HELVETICA, 12);

                    for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                        String propertyName = resultSet.getMetaData().getColumnName(i);
                        String propertyValue = resultSet.getString(i);

                        if (propertyValue == null || propertyValue.isEmpty()) {
                            propertyValue = "N/A";
                        }

                        // Formatação de valores
                        if (propertyValue.matches("\\d{4}-\\d{2}-\\d{2}")) {
                            try {
                                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(propertyValue);
                                propertyValue = new SimpleDateFormat("dd/MM/yyyy").format(date);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        } else if (propertyValue.matches("\\d+(\\.\\d{1,2})?")) {
                            try {
                                Number number = NumberFormat.getInstance(Locale.US).parse(propertyValue);
                                propertyValue = NumberFormat.getNumberInstance(new Locale("pt", "BR")).format(number);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        } else if (!propertyValue.isEmpty()) {
                            propertyValue = propertyValue.substring(0, 1).toUpperCase() + propertyValue.substring(1).toLowerCase();
                        }

                        String columnName = columnNamesMap.getOrDefault(propertyName, propertyName);

                        PdfPCell cell1 = new PdfPCell(new Phrase(columnName, headerFont));
                        cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
                        cell1.setBorder(Rectangle.NO_BORDER);
                        table.addCell(cell1);

                        PdfPCell cell2 = new PdfPCell(new Phrase(propertyValue, valueFont));
                        cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
                        cell2.setBorder(Rectangle.NO_BORDER);
                        table.addCell(cell2);
                    }

                    document.add(table);
                }

                document.close();

                // Abrindo o arquivo PDF gerado
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(file);
                }

            } catch (SQLException | DocumentException | FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
