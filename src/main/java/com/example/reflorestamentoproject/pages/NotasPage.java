package com.example.reflorestamentoproject.pages;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class NotasPage extends Stage {

    private final int imovelId;
    private final Connection connection;
    private ListView<String> notasListView;
    private TextField tituloTextField;
    private TextArea descricaoTextArea;

    public NotasPage(int imovelId, Connection connection) {
        this.imovelId = imovelId;
        this.connection = connection;
        setTitle("Notas");

        // Definindo a paleta de cores
        String backgroundColor = "#294B29";
        String textColor = "#DBE7C9";
        String buttonColor = "#50623A";
        String borderColor = "#789461";

        VBox mainLayout = new VBox();
        mainLayout.setSpacing(10);
        mainLayout.setPadding(new Insets(10));
        mainLayout.setBackground(new Background(new BackgroundFill(Color.web(backgroundColor), CornerRadii.EMPTY, Insets.EMPTY)));
        mainLayout.setStyle("-fx-text-fill: " + textColor + ";");

        notasListView = new ListView<>();
        carregarNotas();

        notasListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // Verifica se foi um clique duplo
                String selectedNote = notasListView.getSelectionModel().getSelectedItem();
                if (selectedNote != null) {
                    String[] parts = selectedNote.split(": ");
                    if (parts.length == 2) {
                        mostrarDetalhesNota(parts[0], parts[1]);
                    }
                }
            }
        });

        Label tituloLabel = new Label("Título:");
        tituloLabel.setStyle("-fx-text-fill: " + textColor + ";");

        tituloTextField = new TextField();

        Label descricaoLabel = new Label("Descrição:");
        descricaoLabel.setStyle("-fx-text-fill: " + textColor + ";");

        descricaoTextArea = new TextArea();
        descricaoTextArea.setWrapText(true);

        Button salvarButton = new Button("Salvar");
        salvarButton.setStyle("-fx-background-color: " + buttonColor + "; -fx-text-fill: " + textColor + ";");
        salvarButton.setOnAction(event -> salvarNota());

        HBox formLayout = new HBox(10, tituloLabel, tituloTextField, descricaoLabel, descricaoTextArea, salvarButton);

        mainLayout.getChildren().addAll(notasListView, formLayout);

        Scene scene = new Scene(mainLayout, 800, 600); // Aumentando o tamanho da janela
        scene.setFill(Color.web(backgroundColor));
        setScene(scene);
        setOnCloseRequest(this::handleCloseRequest);
    }

    private void carregarNotas() {
        notasListView.getItems().clear();
        String query = "SELECT titulo, descricao FROM notas WHERE imovel_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, imovelId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String titulo = resultSet.getString("titulo");
                String descricao = resultSet.getString("descricao");
                notasListView.getItems().add(titulo + ": " + descricao);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void salvarNota() {
        String titulo = tituloTextField.getText();
        String descricao = descricaoTextArea.getText();

        if (titulo.isEmpty() || descricao.isEmpty()) {
            return;
        }

        String query = "INSERT INTO notas (titulo, descricao, imovel_id) VALUES (?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, titulo);
            statement.setString(2, descricao);
            statement.setInt(3, imovelId);
            statement.executeUpdate();
            carregarNotas();
            tituloTextField.clear();
            descricaoTextArea.clear();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void mostrarDetalhesNota(String titulo, String descricao) {
        Stage detalheStage = new Stage();
        detalheStage.setTitle("Detalhes da Nota");

        VBox detalheLayout = new VBox();
        detalheLayout.setSpacing(10);
        detalheLayout.setPadding(new Insets(10));
        detalheLayout.setBackground(new Background(new BackgroundFill(Color.web("#294B29"), CornerRadii.EMPTY, Insets.EMPTY)));

        Label tituloLabel = new Label("Título:");
        tituloLabel.setStyle("-fx-text-fill: #DBE7C9;");
        TextField tituloField = new TextField(titulo);

        Label descricaoLabel = new Label("Descrição:");
        descricaoLabel.setStyle("-fx-text-fill: #DBE7C9;");
        TextArea descricaoArea = new TextArea(descricao);
        descricaoArea.setWrapText(true);

        Button atualizarButton = new Button("Atualizar");
        atualizarButton.setStyle("-fx-background-color: #50623A; -fx-text-fill: #DBE7C9;");
        atualizarButton.setOnAction(event -> {
            String novoTitulo = tituloField.getText();
            String novaDescricao = descricaoArea.getText();
            atualizarNota(titulo, novoTitulo, novaDescricao);
            detalheStage.close();
        });

        Button excluirButton = new Button("Excluir");
        excluirButton.setStyle("-fx-background-color: #FF6F61; -fx-text-fill: #DBE7C9;");
        excluirButton.setOnAction(event -> {
            excluirNota(titulo);
            detalheStage.close();
        });

        HBox buttonLayout = new HBox(10, atualizarButton, excluirButton);

        detalheLayout.getChildren().addAll(tituloLabel, tituloField, descricaoLabel, descricaoArea, buttonLayout);

        Scene detalheScene = new Scene(detalheLayout, 400, 300);
        detalheStage.setScene(detalheScene);
        detalheStage.show();
    }

    private void excluirNota(String titulo) {
        String query = "DELETE FROM notas WHERE titulo = ? AND imovel_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, titulo);
            statement.setInt(2, imovelId);
            statement.executeUpdate();
            carregarNotas();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void atualizarNota(String tituloAntigo, String novoTitulo, String novaDescricao) {
        String query = "UPDATE notas SET titulo = ?, descricao = ? WHERE titulo = ? AND imovel_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, novoTitulo);
            statement.setString(2, novaDescricao);
            statement.setString(3, tituloAntigo);
            statement.setInt(4, imovelId);
            statement.executeUpdate();
            carregarNotas();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void handleCloseRequest(WindowEvent event) {
        // Apenas fecha a janela, sem retornar à página anterior
    }
}
