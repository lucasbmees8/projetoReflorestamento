package com.example.reflorestamentoproject.pages;

import com.example.reflorestamentoproject.padrao.Registro;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class VisualizarRegistrosPage {
    private final int imovelId;
    private final Connection connection;

    public VisualizarRegistrosPage(int imovelId, Connection connection) {
        this.imovelId = imovelId;
        this.connection = connection;
    }

    public void show() {
        Stage stage = new Stage();
        stage.setTitle("Visualizar Registros");

        Font.loadFont(getClass().getResourceAsStream("/fonts/Roboto-Medium.ttf"), 16);
        String backgroundColor = "#294B29";
        String textColor = "#DBE7C9";
        String buttonColor = "#50623A";
        String borderColor = "#789461";
        String buttonStyle = "-fx-background-color: " + buttonColor + "; -fx-text-fill: " + textColor + "; -fx-font-size: 16; -fx-font-family: 'Roboto Medium'; -fx-border-color: " + borderColor + ";";


        TableView<Registro> table = new TableView<>();
        table.setStyle("-fx-background-color: #333333; -fx-text-fill: white;");

        // Criar as colunas da tabela
        TableColumn<Registro, Integer> imovelIdColumn = new TableColumn<>("Imóvel ID");
        imovelIdColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getImovelId()).asObject());

        TableColumn<Registro, Double> capMedioColumn = new TableColumn<>("Cap Médio");
        capMedioColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getCapMedio()).asObject());

        TableColumn<Registro, Double> dapMedioColumn = new TableColumn<>("DAP Médio");
        dapMedioColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getDapMedio()).asObject());

        TableColumn<Registro, Double> alturaMediaColumn = new TableColumn<>("Altura Média");
        alturaMediaColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getAlturaMedia()).asObject());

        table.getColumns().addAll(imovelIdColumn, capMedioColumn, dapMedioColumn, alturaMediaColumn);
        table.setStyle("-fx-background-color: " + backgroundColor + "; -fx-control-inner-background: " + backgroundColor + "; -fx-text-fill: " + textColor + "; -fx-font-family: 'Roboto Medium'");

        // Preencher a tabela com os registros do banco de dados
        updateTableFromDatabase(table);

        VBox vbox = new VBox(table);
        Scene scene = new Scene(vbox, 600, 400);
        vbox.setStyle("-fx-background-color: #242424;");
        stage.setScene(scene);
        stage.show();
    }

    private void updateTableFromDatabase(TableView<Registro> table) {
        try {
            String query = "SELECT * FROM inventario WHERE imovel_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, imovelId);
            ResultSet resultSet = statement.executeQuery();

            ObservableList<Registro> data = FXCollections.observableArrayList();
            while (resultSet.next()) {
                double capMedio = resultSet.getDouble("cap");
                double dapMedio = resultSet.getDouble("dap");
                double alturaMedia = resultSet.getDouble("altura");

                data.add(new Registro(imovelId, capMedio, alturaMedia, dapMedio));
            }

            table.setItems(data);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}