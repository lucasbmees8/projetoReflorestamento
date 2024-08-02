package com.example.reflorestamentoproject.pages;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.sql.Connection;

public class MainPage {

    public void show(Stage primaryStage, LoginController loginController, Connection connection) {
        primaryStage.setTitle("Main Page");
        primaryStage.setMaximized(true); // Definindo o Stage como tela cheia



        // Carregando a imagem
        Image image = new Image(getClass().getResourceAsStream("/images/logorioverde.png"));


        // Criando o ImageView para exibir a imagem
        ImageView imageView = new ImageView(image);
        // Definindo o tamanho máximo da imagem
        double maxWidth = 800; // Largura máxima desejada
        double maxHeight = 600; // Altura máxima desejada

    // Verificando as dimensões originais da imagem
        double originalWidth = image.getWidth();
        double originalHeight = image.getHeight();

    // Calculando as proporções para redimensionar a imagem mantendo a proporção original
        double scaleFactor = Math.min(maxWidth / originalWidth, maxHeight / originalHeight);

    // Redimensionando a imagem
        imageView.setFitWidth(originalWidth * scaleFactor);
        imageView.setFitHeight(originalHeight * scaleFactor);

        // Criando os botões
        Button registerImovelButton = new Button("Cadastrar Imóveis");
        Button showImovelsButton = new Button("Imóveis");
        Button despesasGerais = new Button("Despesas Gerais");

        // Definindo a paleta de cores
        String backgroundColor = "#294B29";
        String textColor = "#DBE7C9";
        String buttonColor = "#50623A";
        String borderColor = "#789461";



        // Estilizando os botões
        String buttonStyle = "-fx-background-color: " + buttonColor + "; -fx-text-fill: " + textColor + "; -fx-font-size: 16; -fx-font-family: 'Roboto Medium'; -fx-border-color: " + borderColor + "; -fx-background-radius: 15; -fx-border-radius: 15;";
        registerImovelButton.setStyle(buttonStyle);
        showImovelsButton.setStyle(buttonStyle);
        despesasGerais.setStyle(buttonStyle);

        // Adicionando ações aos botões
        registerImovelButton.setOnAction(event -> loginController.redirectToImovelRegistration(primaryStage, connection));
        showImovelsButton.setOnAction(event -> loginController.redirectToShowImoveis(primaryStage, connection));
        despesasGerais.setOnAction(event -> loginController.redirectToDespesasGerais(primaryStage, connection));

        // Criando o layout HBox para os botões
        HBox buttonsBox = new HBox(20); // Definindo o espaçamento horizontal entre os botões
        buttonsBox.setAlignment(Pos.CENTER); // Centralizando os botões na cena
        buttonsBox.getChildren().addAll(registerImovelButton, showImovelsButton, despesasGerais);

        // Criando o layout VBox para organizar os elementos verticalmente
        VBox vbox = new VBox(20); // Definindo o espaçamento vertical entre os elementos
        vbox.setBackground(new Background(new BackgroundFill(Color.web(backgroundColor), CornerRadii.EMPTY, Insets.EMPTY)));
        vbox.setAlignment(Pos.CENTER); // Centralizando o VBox na cena
        vbox.setPadding(new Insets(20)); // Ajuste do espaço em torno dos elementos

        // Adicionando a imagem e o HBox com os botões ao VBox
        vbox.getChildren().addAll(imageView, buttonsBox);

        // Criando a cena com o layout VBox
        Scene scene = new Scene(vbox);

        // Definindo a cena no palco e exibindo-o
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen(); // Centralizando a janela principal ao meio da tela
        primaryStage.show();
    }
}
