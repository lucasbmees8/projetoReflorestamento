package com.example.reflorestamentoproject.pages;

import com.example.reflorestamentoproject.padrao.Property;
import com.example.reflorestamentoproject.pdf.GeradorPDFImovel;
import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.converter.DefaultStringConverter;

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

public class ImovelDetailsPage extends Stage {

    private final Stage previousStage;
    private final LoginController loginController;
    private TableView<Property> tableView;
    private static final Map<String, String> columnNamesMap = new HashMap<>();
    private int currentImovelId;
    private Connection currentConnection;

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
        columnNamesMap.put("codigo_cc", "Código Centro de Custo");
    }

    public ImovelDetailsPage(int imovelId, Connection connection, Stage previousStage, LoginController loginController) {
        this.previousStage = previousStage;
        this.loginController = loginController;
        this.currentImovelId = imovelId;
        this.currentConnection = connection;
        setTitle("Detalhes do Imóvel");

        // Definindo a paleta de cores
        String backgroundColor = "#294B29";
        String textColor = "#DBE7C9";
        String buttonColor = "#50623A";
        String borderColor = "#789461";

        tableView = new TableView<>();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.setStyle("-fx-background-color: " + backgroundColor + "; -fx-control-inner-background: " + backgroundColor + "; -fx-text-fill: " + textColor + "; -fx-font-family: 'Roboto Medium'; -fx-font-size: 14px;");
        tableView.setEditable(true); // Tornando a tabela editável

        TableColumn<Property, String> propertyColumn = new TableColumn<>("Propriedade");
        propertyColumn.setCellValueFactory(new PropertyValueFactory<>("property"));
        propertyColumn.setCellFactory(col -> {
            TableCell<Property, String> cell = new TextFieldTableCell<>(new DefaultStringConverter());
            cell.setStyle("-fx-alignment: CENTER-LEFT;"); // Alinhando o texto à esquerda
            return cell;
        });
        propertyColumn.setMinWidth(200); // Ajustando a largura mínima da coluna "Propriedade"
        propertyColumn.setMaxWidth(200);

        TableColumn<Property, String> valueColumn = new TableColumn<>("Valor");
        valueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
        valueColumn.setCellFactory(col -> new TextFieldTableCell<Property, String>(new DefaultStringConverter()) {
            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty && item != null) {
                    // Detectando se é uma data e formatando para o padrão brasileiro
                    if (item.matches("\\d{4}-\\d{2}-\\d{2}")) {
                        try {
                            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(item);
                            item = new SimpleDateFormat("dd/MM/yyyy").format(date);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    // Detectando se é um número e formatando para o padrão brasileiro
                    else if (item.matches("\\d+(\\.\\d{1,2})?")) {
                        try {
                            Number number = NumberFormat.getInstance(Locale.US).parse(item);
                            item = NumberFormat.getNumberInstance(new Locale("pt", "BR")).format(number);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    // Capitalizando a primeira letra de cada texto
                    else if (!item.isEmpty()) {
                        item = item.substring(0, 1).toUpperCase() + item.substring(1).toLowerCase();
                    }
                    setText(item);
                } else {
                    setText(null);
                }
            }
        });
        valueColumn.setOnEditCommit(event -> {
            Property property = event.getRowValue();
            property.setValue(event.getNewValue());
            updateDatabase(property.getProperty(), event.getNewValue(), imovelId, connection);
        });


        tableView.getColumns().addAll(propertyColumn, valueColumn);

        atualizarDetalhes(imovelId, connection);

        String buttonStyle = "-fx-background-color: " + buttonColor + "; -fx-text-fill: " + textColor + "; -fx-font-size: 16; -fx-font-family: 'Roboto Medium'; -fx-border-color: " + borderColor + "; -fx-background-radius: 15; -fx-border-radius: 15;";

        Button generateReportButton = new Button("Gerar Relatório");
        generateReportButton.setStyle(buttonStyle);
        generateReportButton.setOnAction(event -> GeradorPDFImovel.generatePDFReport(imovelId, connection, this));

        Button imagesButton = new Button("Ver Imagens");
        imagesButton.setStyle(buttonStyle);
        imagesButton.setOnAction(event -> {
            GalleryPage galleryPage = new GalleryPage(imovelId, connection, this);
            galleryPage.show();
        });

        Button backButton = new Button("Voltar");
        backButton.setStyle(buttonStyle);
        backButton.setOnAction(event -> {
            close();
            loginController.redirectToShowImoveis(previousStage, connection);
        });

        Button despesaButton = new Button("Despesa");
        despesaButton.setStyle(buttonStyle);
        despesaButton.setOnAction(event -> {
            DespesaPage despesaPage = new DespesaPage(imovelId, connection, this, loginController);
            despesaPage.show();
        });

        Button desbasteButton = new Button("Desbaste");
        desbasteButton.setStyle(buttonStyle);
        desbasteButton.setOnAction(event -> {
            DesbastePage desbastePage = new DesbastePage(imovelId, connection, this, loginController, this, this );
            desbastePage.show();
        });

        Button inventarioButton = new Button("Inventário");
        inventarioButton.setStyle(buttonStyle);
        inventarioButton.setOnAction(event -> {
            InventarioPage inventarioPage = new InventarioPage(imovelId, connection, this, loginController);
            inventarioPage.show();
        });

        Button mapaButton = new Button("Mapa");
        mapaButton.setStyle(buttonStyle);
        mapaButton.setOnAction(event -> {
            MapaPage mapaPage = new MapaPage(imovelId, connection, this, loginController);
            mapaPage.show();
        });

        Button desramaButton = new Button("Desrama");
        desramaButton.setStyle(buttonStyle);
        desramaButton.setOnAction(event -> {
            DesramaPage desramaPage = new DesramaPage(imovelId, connection, this, loginController, this);
            desramaPage.show();
        });

        Button notasButton = new Button("Notas");
        notasButton.setStyle(buttonStyle);
        notasButton.setOnAction(event -> {
            NotasPage notasPage = new NotasPage(imovelId, connection);
            notasPage.show();
        });

        VBox vbox = new VBox();
        vbox.getChildren().addAll(tableView);
        vbox.setStyle("-fx-background-color: " + backgroundColor + ";");

        HBox bottomBox = new HBox(imagesButton, mapaButton, despesaButton, desbasteButton, desramaButton, inventarioButton, generateReportButton, notasButton, backButton);
        bottomBox.setSpacing(5); // Aproximando mais os botões
        bottomBox.setStyle("-fx-background-color: " + backgroundColor + ";");
        bottomBox.setPadding(new Insets(10));

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(vbox);
        borderPane.setBottom(bottomBox);
        borderPane.setStyle("-fx-background-color: " + backgroundColor + ";");

        Scene scene = new Scene(borderPane, 1280, 720);
        scene.setFill(Color.web(backgroundColor));

        setScene(scene);
    }

    public void atualizarDetalhes(int imovelId, Connection connection) {
        tableView.getItems().clear();

        try {
            String query = "SELECT classe FROM imoveis WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, imovelId);
            ResultSet resultSet = statement.executeQuery();

            String queryDetalhes = "";
            if (resultSet.next()) {
                String classe = resultSet.getString("classe");
                if (classe.equals("proprio")) {
                    queryDetalhes = "SELECT i.descricao, i.area_imovel, i.area_plantio, i.especie, i.origem, i.num_arvores_plantadas, " +
                            "i.arvores_remanescentes, i.arvores_cortadas, i.num_arvores_por_hectare, i.matricula, i.data_plantio, " +
                            "i.numero_ccir, i.numero_itr, i.proprietario, i.municipio, i.localidade, i.classe, " +
                            "i.altura_desrama, i.codigo_cc " + // Adicionando o campo "codigo_cc"
                            "FROM imoveis i " +
                            "WHERE i.id = ?";
                } else {
                    queryDetalhes = "SELECT i.descricao, i.area_imovel, i.area_plantio, i.especie, i.origem, i.num_arvores_plantadas, " +
                            "i.arvores_remanescentes, i.arvores_cortadas, i.num_arvores_por_hectare, i.matricula, i.data_plantio, " +
                            "i.numero_ccir, i.numero_itr, i.proprietario, i.municipio, i.localidade, i.classe, "+
                            "ia.data_contrato, ia.vencimento_contrato, ia.arrendatario, " +
                            "i.altura_desrama " + // Adicionando a altura_desrama para imóveis arrendados
                            "FROM imoveis i " +
                            "LEFT JOIN imoveis_arrendados ia ON i.id = ia.imovel_id " +
                            "WHERE i.id = ?";
                }

                PreparedStatement newStatement = connection.prepareStatement(queryDetalhes);
                newStatement.setInt(1, imovelId);
                ResultSet newResultSet = newStatement.executeQuery();

                if (newResultSet.next()) {
                    for (int i = 1; i <= newResultSet.getMetaData().getColumnCount(); i++) {
                        String propertyName = newResultSet.getMetaData().getColumnName(i);
                        String propertyValue = newResultSet.getString(i);

                        if (propertyValue == null || propertyValue.isEmpty()) {
                            propertyValue = "N/A";
                        }

                        String columnName = columnNamesMap.getOrDefault(propertyName, propertyName);
                        Property property = new Property(columnName, propertyValue);
                        tableView.getItems().add(property);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateDatabase(String columnName, String newValue, int imovelId, Connection connection) {
        String dbColumnName = getColumnNameForDatabase(columnName);
        String updateQuery;

        if (dbColumnName.equals("altura_desrama") || dbColumnName.equals("data_contrato") || dbColumnName.equals("vencimento_contrato") || dbColumnName.equals("arrendatario")) {
            // Update specific tables for these columns
            if (dbColumnName.equals("altura_desrama")) {
                updateQuery = "UPDATE imoveis_proprios SET " + dbColumnName + " = ? WHERE imovel_id = ?";
            } else {
                updateQuery = "UPDATE imoveis_arrendados SET " + dbColumnName + " = ? WHERE imovel_id = ?";
            }
        } else {
            updateQuery = "UPDATE imoveis SET " + dbColumnName + " = ? WHERE id = ?";
        }

        try {
            PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
            updateStatement.setString(1, newValue);
            updateStatement.setInt(2, imovelId);
            updateStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String getColumnNameForDatabase(String property) {
        for (Map.Entry<String, String> entry : columnNamesMap.entrySet()) {
            if (entry.getValue().equals(property)) {
                return entry.getKey();
            }
        }
        return property;
    }
}
