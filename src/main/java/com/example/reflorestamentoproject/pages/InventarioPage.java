package com.example.reflorestamentoproject.pages;

import com.example.reflorestamentoproject.padrao.Registro;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class InventarioPage {
    private final int imovelId;
    private final Connection connection;
    private final Stage previousStage;
    private final LoginController loginController;
    private final List<Registro> registros = new ArrayList<>();
    private final DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");

    public InventarioPage(int imovelId, Connection connection, Stage previousStage, LoginController loginController) {
        this.imovelId = imovelId;
        this.connection = connection;
        this.previousStage = previousStage;
        this.loginController = loginController;
    }

    public void show() {
        Stage stage = new Stage();
        stage.setTitle("Inventário");
        Font.loadFont(getClass().getResourceAsStream("/fonts/Roboto-Medium.ttf"), 16);
        String backgroundColor = "#294B29";
        String textColor = "#DBE7C9";
        String buttonColor = "#50623A";
        String borderColor = "#789461";
        String buttonStyle = "-fx-background-color: " + buttonColor + "; -fx-text-fill: " + textColor + "; -fx-font-size: 16; -fx-font-family: 'Roboto Medium'; -fx-border-color: " + borderColor + "; -fx-background-radius: 15; -fx-border-radius: 15;";


        TableView<Registro> table = new TableView<>();
        table.setStyle("-fx-background-color: #333333; -fx-text-fill: white;");

        // Criar as colunas da tabela
        TableColumn<Registro, Integer> imovelIdColumn = new TableColumn<>("Imóvel ID");
        imovelIdColumn.setCellValueFactory(new PropertyValueFactory<>("imovelId"));

        TableColumn<Registro, Double> capMedioColumn = new TableColumn<>("Cap Médio");
        capMedioColumn.setCellValueFactory(new PropertyValueFactory<>("capMedio"));

        TableColumn<Registro, Double> dapMedioColumn = new TableColumn<>("DAP Médio");
        dapMedioColumn.setCellValueFactory(new PropertyValueFactory<>("dapMedio"));

        TableColumn<Registro, Double> alturaMediaColumn = new TableColumn<>("Altura Média");
        alturaMediaColumn.setCellValueFactory(new PropertyValueFactory<>("alturaMedia"));

        table.getColumns().addAll(imovelIdColumn, capMedioColumn, dapMedioColumn, alturaMediaColumn);
        table.setStyle("-fx-background-color: " + backgroundColor + "; -fx-control-inner-background: " + backgroundColor + "; -fx-text-fill: " + textColor + "; -fx-font-family: 'Roboto Medium'");

        // Defina o estilo dos campos
        String fieldStyle = "-fx-font-family: 'Roboto'; " +
                "-fx-font-size: 14px; " +
                "-fx-pref-width: 200px; " +
                "-fx-background-color: #FFF; " +
                "-fx-text-fill: #294B29; " +
                "-fx-border-color: " + borderColor + "; " +
                "-fx-background-radius: 10; " +
                "-fx-border-radius: 10;";

        TextField capTextField = new TextField();
        capTextField.setPromptText("Cap");
        capTextField.setStyle(fieldStyle);

        TextField alturaTextField = new TextField();
        alturaTextField.setPromptText("Altura");
        alturaTextField.setStyle(fieldStyle);

        // Botão para cadastrar
        Button cadastrarButton = new Button("Cadastrar");
        cadastrarButton.setStyle(buttonStyle);
        cadastrarButton.setOnAction(e -> {
            // Obter os valores dos campos de texto
            double capacidade = parseTextField(capTextField);
            double altura = parseTextField(alturaTextField);
            double dap = capacidade / 3.14;

            // Criar um novo registro com os valores inseridos e adicioná-lo à lista
            Registro registro = new Registro(imovelId, capacidade, altura, dap);
            registros.add(registro);

            // Adicionar o registro à tabela
            table.getItems().add(registro);

            // Limpar os campos de texto após cadastrar
            capTextField.clear();
            alturaTextField.clear();
        });

        // Botão para calcular média e inserir no banco de dados
        Button calcularMediaButton = new Button("Calcular Média e Inserir");
        calcularMediaButton.setStyle(buttonStyle);
        calcularMediaButton.setOnAction(e -> {
            if (!registros.isEmpty()) {
                // Calcular média dos valores de capacidade e altura
                double capMedia = registros.stream().mapToDouble(Registro::getCapMedio).average().orElse(0);
                double alturaMedia = registros.stream().mapToDouble(Registro::getAlturaMedia).average().orElse(0);

                // Calcular DAP médio
                double dapMedia = capMedia / 3.14;

                // Criar um novo registro com as médias calculadas
                Registro registroMedia = new Registro(imovelId, capMedia, alturaMedia, dapMedia);

                // Inserir os dados na tabela do banco de dados
                inserirDadosTabela(registroMedia);

                // Limpar a lista de registros
                registros.clear();

                // Limpar a tabela
                table.getItems().clear();

                // Exibir uma mensagem de sucesso
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Média calculada e inserida");
                alert.setHeaderText(null);
                alert.setContentText("Médias calculadas e inseridas com sucesso!");
                alert.showAndWait();

                // Atualizar a tabela com os dados do banco de dados
                updateTableFromDatabase(table);
            } else {
                // Exibir uma mensagem de erro se a lista de registros estiver vazia
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erro");
                alert.setHeaderText(null);
                alert.setContentText("Nenhum registro para calcular média e inserir!");
                alert.showAndWait();
            }
        });

        Button visualizarRegistros = new Button("Visualizar Registros");
        visualizarRegistros.setStyle(buttonStyle);
        visualizarRegistros.setOnAction(e -> {
            VisualizarRegistrosPage visualizarRegistrosPage = new VisualizarRegistrosPage(imovelId, connection);
            visualizarRegistrosPage.show();
        });


        // Layout da página
        VBox vbox = new VBox();
        vbox.setSpacing(10);
        vbox.setPadding(new Insets(10));
        vbox.getChildren().addAll(table, capTextField, alturaTextField, cadastrarButton, calcularMediaButton, visualizarRegistros);
        vbox.setStyle("-fx-background-color:" + backgroundColor);
        Scene scene = new Scene(vbox, 400, 400);
        stage.setScene(scene);
        stage.show();

        // Atualizar a tabela com os dados do banco de dados
        updateTableFromDatabase(table);
    }

    private double parseTextField(TextField textField) {
        String text = textField.getText().replace(",", ".");
        try {
            return decimalFormat.parse(text).doubleValue();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0.0;
        }
    }

    private void inserirDadosTabela(Registro registro) {
        try {
            String query = "INSERT INTO inventario (imovel_id, cap, altura, dap) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, registro.getImovelId());
            statement.setDouble(2, registro.getCapMedio());
            statement.setDouble(3, registro.getAlturaMedia());
            statement.setDouble(4, registro.getDapMedio());

            // Imprimir a query e os valores
            System.out.println("Query: " + query);
            System.out.println("Valores: imovel_id=" + registro.getImovelId() + ", cap=" + registro.getCapMedio() + ", altura=" + registro.getAlturaMedia() + ", dap=" + registro.getDapMedio());

            statement.executeUpdate();

            // Fechar o PreparedStatement
            statement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void updateTableFromDatabase(TableView<Registro> table) {
        try {
            String query = "SELECT * FROM inventario WHERE imovel_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, imovelId);
            ResultSet resultSet = statement.executeQuery();

            ObservableList<Registro> data = FXCollections.observableArrayList();
            while (resultSet.next()) {
                int imovelId = resultSet.getInt("imovel_id");
                double capMedio = resultSet.getDouble("cap_medio");
                double dapMedio = resultSet.getDouble("dap_medio");
                double alturaMedia = resultSet.getDouble("altura_media");

                data.add(new Registro(imovelId, capMedio, alturaMedia, dapMedio));
            }

            table.setItems(data);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
