package com.example.reflorestamentoproject.pages;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MapaPage extends Stage {

    private final int imovelId;
    private final Connection connection;
    private final Stage previousStage;
    private final LoginController loginController;

    private final List<String> kmlPaths = new ArrayList<>();

    public MapaPage(int imovelId, Connection connection, Stage previousStage, LoginController loginController) {
        this.imovelId = imovelId;
        this.connection = connection;
        this.previousStage = previousStage;
        this.loginController = loginController;
        setTitle("Mapa do Imóvel");
        String backgroundColor = "#294B29";
        String textColor = "#DBE7C9";
        String buttonColor = "#50623A";
        String borderColor = "#789461";
        String buttonStyle = "-fx-background-color: " + buttonColor + "; -fx-text-fill: " + textColor + "; -fx-font-size: 16; -fx-font-family: 'Roboto Medium'; -fx-border-color: " + borderColor + "; -fx-background-radius: 10; -fx-border-radius: 10;";


        Button adicionarMapaButton = new Button("Adicionar Mapa");
        adicionarMapaButton.setStyle(buttonStyle);
        adicionarMapaButton.setOnAction(event -> adicionarMapa());

        Button excluirMapaButton = new Button("Excluir Mapa");
        excluirMapaButton.setStyle("-fx-background-color: #FF0000; -fx-text-fill: white; -fx-font-size: 14;");
        excluirMapaButton.setOnAction(event -> excluirMapa());

        VBox kmlFilesBox = new VBox();
        atualizarKMLFiles(kmlFilesBox);

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(adicionarMapaButton);
        borderPane.setCenter(kmlFilesBox);
        borderPane.setBottom(excluirMapaButton);
        borderPane.setStyle("-fx-background-color:" + backgroundColor);

        Scene scene = new Scene(borderPane, 400, 200);
        setScene(scene);
    }

    private void adicionarMapa() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Todos os Arquivos", "*.*")); // Aceita todos os tipos de arquivos
        File selectedFile = fileChooser.showOpenDialog(this);

        if (selectedFile != null) {
            String kmlPath = salvarMapa(selectedFile);
            inserirMapaNoBancoDeDados(kmlPath);
            atualizarKMLFiles((VBox) ((BorderPane) getScene().getRoot()).getCenter());
        }
    }

    private void excluirMapa() {
        if (kmlPaths.isEmpty()) {
            exibirAlertaExclusaoErro();
            return;
        }

        String kmlPath = kmlPaths.get(kmlPaths.size() - 1);

        try {
            String deleteQuery = "DELETE FROM kml_imovel WHERE kml_path = ?";
            PreparedStatement statement = connection.prepareStatement(deleteQuery);
            statement.setString(1, kmlPath);
            statement.executeUpdate();

            File file = new File(kmlPath);
            Files.deleteIfExists(file.toPath());

            exibirAlertaExclusaoSucesso();
            atualizarKMLFiles((VBox) ((BorderPane) getScene().getRoot()).getCenter());
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            exibirAlertaExclusaoErro();
        }
    }

    private String salvarMapa(File selectedFile) {
        String documentsPath = System.getProperty("user.home") + File.separator + "Documents";
        String folderPath = documentsPath + File.separator + "KMLFiles";
        File folder = new File(folderPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        String fileName = selectedFile.getName();
        String destPath = folderPath + File.separator + fileName;

        try {
            Path sourcePath = Paths.get(selectedFile.getAbsolutePath());
            Path destPathObj = Paths.get(destPath);
            Files.copy(sourcePath, destPathObj);
            return destPath;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void inserirMapaNoBancoDeDados(String kmlPath) {
        try {
            String insertQuery = "INSERT INTO kml_imovel (imovel_id, kml_path) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(insertQuery);
            statement.setInt(1, imovelId);
            statement.setString(2, kmlPath);
            statement.executeUpdate();

            exibirAlertaInsercaoSucesso();
        } catch (SQLException e) {
            e.printStackTrace();
            exibirAlertaInsercaoErro();
        }
    }

    private void exibirAlertaInsercaoSucesso() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sucesso");
        alert.setHeaderText(null);
        alert.setContentText("O arquivo KML foi adicionado com sucesso.");
        alert.showAndWait();
    }

    private void exibirAlertaInsercaoErro() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText("Ocorreu um erro ao adicionar o arquivo KML.");
        alert.showAndWait();
    }

    private void exibirAlertaExclusaoSucesso() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sucesso");
        alert.setHeaderText(null);
        alert.setContentText("O arquivo KML foi excluído com sucesso.");
        alert.showAndWait();
    }

    private void exibirAlertaExclusaoErro() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText("Não há arquivos KML para excluir.");
        alert.showAndWait();
    }

    private void atualizarKMLFiles(VBox kmlFilesBox) {
        kmlFilesBox.getChildren().clear();
        kmlPaths.clear();

        try {
            String selectQuery = "SELECT kml_path FROM kml_imovel WHERE imovel_id = ?";
            PreparedStatement statement = connection.prepareStatement(selectQuery);
            statement.setInt(1, imovelId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String backgroundColor = "#294B29";
                String textColor = "#DBE7C9";
                String buttonColor = "#50623A";
                String borderColor = "#789461";
                String buttonStyle = "-fx-background-color: " + buttonColor + "; -fx-text-fill: " + textColor + "; -fx-font-size: 16; -fx-font-family: 'Roboto Medium'; -fx-border-color: " + borderColor + ";";


                String kmlPath = resultSet.getString("kml_path");
                kmlPaths.add(kmlPath);

                Button kmlButton = new Button(kmlPath);
                kmlButton.setStyle(buttonStyle);
                kmlButton.setOnAction(event -> abrirDiretorioDoMapa(kmlPath));
                kmlFilesBox.getChildren().add(kmlButton);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void abrirDiretorioDoMapa(String kmlPath) {
        File file = new File(kmlPath);
        String folderPath = file.getParent();
        try {
            java.awt.Desktop.getDesktop().open(new File(folderPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
