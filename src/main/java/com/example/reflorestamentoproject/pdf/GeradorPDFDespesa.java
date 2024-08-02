package com.example.reflorestamentoproject.pdf;

import com.example.reflorestamentoproject.padrao.Despesa;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.text.NumberFormat;
import java.util.Locale;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GeradorPDFDespesa {

    public void generateReport(List<Despesa> selectedDespesas, String imovelNome, String codigoCC, String tipoDespesa) {
        try {
            // Ordenar as despesas por data
            Collections.sort(selectedDespesas, Comparator.comparing(Despesa::getData));

            Document document = new Document();
            File file = new File(System.getProperty("user.home") + "/Downloads/ordem_pagamento_" + imovelNome + ".pdf");
            PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();

            // Adicionando imagem da logo centralizada
            Image logo = Image.getInstance(getClass().getResource("/images/logorioverde.png"));
            logo.setAlignment(Element.ALIGN_CENTER);
            logo.scaleToFit(200, 200);
            document.add(logo);

            // Adicionando título e cabeçalho centralizados dentro de um quadrado
            PdfPTable mainTable = new PdfPTable(1);
            mainTable.setWidthPercentage(100);
            mainTable.setSpacingBefore(20f);
            mainTable.setSpacingAfter(20f);

            PdfPCell cell = new PdfPCell();
            cell.setBorder(Rectangle.BOX);

            // Cabeçalho "Ordem de Pagamento" e número da despesa
            PdfPTable headerTable = new PdfPTable(2);
            headerTable.setWidthPercentage(100);
            PdfPCell headerCell = new PdfPCell(new Phrase("Ordem de Pagamento", new Font(Font.FontFamily.HELVETICA, 24, Font.BOLD)));
            headerCell.setBorder(Rectangle.NO_BORDER);
            headerCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            headerTable.addCell(headerCell);

            PdfPCell despesaCell = new PdfPCell(new Phrase("Despesa: " + codigoCC, new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.RED)));
            despesaCell.setBorder(Rectangle.NO_BORDER);
            despesaCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            headerTable.addCell(despesaCell);

            cell.addElement(headerTable);

            // Data de geração
            PdfPTable pedidoDataTable = new PdfPTable(1);
            pedidoDataTable.setWidthPercentage(100);

            // Obter a data atual
            String dataAtual = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

            PdfPCell dataCell = new PdfPCell(new Phrase("Data: " + dataAtual, new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL)));
            dataCell.setBorder(Rectangle.BOX);
            dataCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            pedidoDataTable.addCell(dataCell);

            cell.addElement(pedidoDataTable);

            // Atividade realizada
            PdfPTable atividadeTable = new PdfPTable(1);
            atividadeTable.setWidthPercentage(100);
            PdfPCell atividadeCell = new PdfPCell(new Phrase("Imóvel:" + imovelNome, new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL)));
            atividadeCell.setBorder(Rectangle.BOX);
            atividadeCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            atividadeTable.addCell(atividadeCell);

            cell.addElement(atividadeTable);

            // Adicionando a tabela de despesas
            PdfPTable despesasTable = new PdfPTable(5);
            despesasTable.setWidthPercentage(100);
            despesasTable.setSpacingBefore(20f);
            despesasTable.setSpacingAfter(20f);

            addHeaderCell(despesasTable, "Tipo de Despesa");
            addHeaderCell(despesasTable, "Quantidade");
            addHeaderCell(despesasTable, "Valor Unitário");
            addHeaderCell(despesasTable, "Total");
            addHeaderCell(despesasTable, "Validade");

            NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

            double total = 0;
            for (Despesa despesa : selectedDespesas) {
                addCell(despesasTable, despesa.getTipoDeDespesa());
                addCell(despesasTable, String.valueOf(despesa.getQuantidade()));
                addCell(despesasTable, nf.format(despesa.getValorUnitario()));
                addCell(despesasTable, nf.format(despesa.getValorTotal()));
                addCell(despesasTable, String.valueOf(despesa.getValidade()));
                total += despesa.getValorTotal();
            }

            addTotalCell(despesasTable, "Total", total);

            cell.addElement(despesasTable);

            mainTable.addCell(cell);
            document.add(mainTable);

            document.close();

            Desktop.getDesktop().open(file);

        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
    }

    private void addHeaderCell(PdfPTable table, String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text, new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
    }

    private void addCell(PdfPTable table, String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text, new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
    }

    private void addTotalCell(PdfPTable table, String text, double total) {
        NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        PdfPCell totalCell = new PdfPCell(new Phrase(text, new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        totalCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        totalCell.setColspan(4);
        table.addCell(totalCell);

        PdfPCell valueCell = new PdfPCell(new Phrase(nf.format(total), new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL)));
        valueCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(valueCell);
    }
}
