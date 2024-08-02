package com.example.reflorestamentoproject.pages;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class GalleryPage extends Stage {

    private final int imovelId;
    private final Connection connection;
    private FlowPane galleryPane;
    private final ImovelDetailsPage previousPage;
    private List<Image> images;
    private int currentIndex;

    public GalleryPage(int imovelId, Connection connection, ImovelDetailsPage previousPage) {
        this.imovelId = imovelId;
        this.connection = connection;
        this.previousPage = previousPage;


        // Defina o esquema de cores para o modo escuro
        String backgroundColor = "#294B29";
        String textColor = "#DBE7C9";
        String buttonColor = "#50623A";
        String borderColor = "#789461";
        String buttonStyle = "-fx-background-color: " + buttonColor + "; -fx-text-fill: " + textColor + "; -fx-font-size: 16; -fx-font-family: 'Roboto Medium'; -fx-border-color: " + borderColor + "; -fx-background-radius: 10; -fx-border-radius: 10;";

        setTitle("Galeria de Imagens");

        // Inicializar lista de imagens
        images = new ArrayList<>();
        currentIndex = 0;
        // Criar layout para a galeria
        VBox root = new VBox();
        root.setStyle("-fx-background:" + backgroundColor);

        galleryPane = new FlowPane();
        galleryPane.setHgap(10); // Espaçamento horizontal entre as imagens
        galleryPane.setVgap(10); // Espaçamento vertical entre as imagens

        // Adicionando o FlowPane dentro de um ScrollPane
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(galleryPane);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setStyle("-fx-background:" + backgroundColor);

        Button addButton = new Button("Adicionar Imagem");
        addButton.setStyle(buttonStyle);
        addButton.setOnAction(event -> addImage());

        root.getChildren().addAll(scrollPane, addButton);

        // Configurar cena e exibir
        Scene scene = new Scene(root, 800, 600);
        scene.setFill(Color.BLACK);
        setScene(scene);

        // Carregar imagens do banco de dados
        loadImages();
    }

    private void loadImages() {
        try {
            // Limpar o galleryPane antes de carregar as novas imagens
            galleryPane.getChildren().clear();
            images.clear();

            String query = "SELECT imagem FROM imagens_imovel WHERE imovel_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, imovelId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                byte[] imageData = resultSet.getBytes("imagem");
                Image image = new Image(new ByteArrayInputStream(imageData));
                images.add(image);
                addImageView(image);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addImageView(Image image) {
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(200);
        imageView.setFitHeight(200);
        imageView.setOnMouseClicked(event -> showFullScreen(image));
        galleryPane.getChildren().add(imageView);
    }

    private void addImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Selecionar Imagem");
        File selectedFile = fileChooser.showOpenDialog(this);

        if (selectedFile != null) {
            try {
                String query = "INSERT INTO imagens_imovel (imovel_id, imagem) VALUES (?, ?)";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, imovelId);
                FileInputStream fis = new FileInputStream(selectedFile);
                statement.setBinaryStream(2, fis, selectedFile.length());

                // Utilizar uma thread para executar a operação de inserção
                new Thread(() -> {
                    try {
                        int rowsAffected = statement.executeUpdate();
                        fis.close();

                        Platform.runLater(() -> {
                            if (rowsAffected > 0) {
                                // Inserção bem-sucedida, então recarrega as imagens
                                loadImages();
                                showAlert(Alert.AlertType.INFORMATION, "Imagem adicionada com sucesso.");
                            } else {
                                // Inserção falhou, exibe uma mensagem de erro
                                showAlert(Alert.AlertType.ERROR, "Erro ao adicionar imagem.");
                            }
                        });
                    } catch (SQLException | IOException e) {
                        e.printStackTrace();
                    }
                }).start();

            } catch (SQLException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void showFullScreen(Image image) {
        ImageView fullScreenImageView = new ImageView(image);
        fullScreenImageView.setPreserveRatio(true);
        fullScreenImageView.setFitWidth(getWidth());
        fullScreenImageView.setFitHeight(getHeight());

        StackPane fullScreenLayout = new StackPane(fullScreenImageView);
        Scene fullScreenScene = new Scene(fullScreenLayout);
        Stage fullScreenStage = new Stage();
        fullScreenStage.setScene(fullScreenScene);
        fullScreenStage.setTitle("Visualização em Tela Cheia");
        fullScreenStage.show();
    }

    // Método para exibir uma caixa de diálogo com mensagem
    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
