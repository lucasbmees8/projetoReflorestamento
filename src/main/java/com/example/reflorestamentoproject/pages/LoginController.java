package com.example.reflorestamentoproject.pages;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController {
    private TextField usernameField;
    private PasswordField passwordField;
    private Connection connection;

    public LoginController(Connection connection) {
        this.connection = connection;
    }


    public void show(Stage primaryStage) {
        primaryStage.setTitle("Login");
        Font.loadFont(getClass().getResourceAsStream("/fonts/Roboto-Medium.ttf"), 16);

        String backgroundColor = "#294B29";
        String textColor = "#DBE7C9";
        String buttonColor = "#50623A";
        String borderColor = "#789461";
        String buttonStyle = "-fx-background-color: " + buttonColor + "; -fx-text-fill: " + textColor + "; -fx-font-size: 16; -fx-font-family: 'Roboto Medium'; -fx-border-color: " + borderColor + "; -fx-background-radius: 10; -fx-border-radius: 10;";


        // Criação dos componentes da tela de login
        Label usernameLabel = new Label("Usuário:");
        usernameLabel.setTextFill(Color.WHITE);
        usernameLabel.setFont(Font.font("Arial", 24));
        usernameField = new TextField();
        usernameField.setStyle("-fx-background-color:" + textColor + "; -fx-text-fill:" + textColor + "-fx-font-size: 18px;");
        Label passwordLabel = new Label("Senha:");
        passwordLabel.setTextFill(Color.WHITE);
        passwordLabel.setFont(Font.font("Arial", 24));
        passwordField = new PasswordField();
        passwordField.setStyle("-fx-background-color:" + textColor + "; -fx-text-fill:" + textColor + "-fx-font-size: 18px;");
        Button loginButton = new Button("Logar");
        loginButton.setStyle(buttonStyle);


        // Layout
        GridPane grid = new GridPane();
        grid.setStyle("-fx-background-color:" + backgroundColor);
        grid.setAlignment(Pos.CENTER); // Centraliza os elementos na cena
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(usernameLabel, 0, 0);
        grid.add(usernameField, 1, 0);
        grid.add(passwordLabel, 0, 1);
        grid.add(passwordField, 1, 1);
        grid.add(loginButton, 0, 2);

        // Eventos dos botões
        loginButton.setOnAction(event -> {
            if (authenticateUser(usernameField.getText(), passwordField.getText(), primaryStage)) {
                // Se autenticado, abre a página principal
                openMainPage(primaryStage, connection);
            } else {
                // Se não autenticado, mostra uma mensagem de erro
                showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid username or password.", primaryStage);
            }
        });

        Scene scene = new Scene(grid);
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    // Método para autenticar o usuário
    private boolean authenticateUser(String username, String password, Stage primaryStage) {
        if (connection == null) {
            showAlert(Alert.AlertType.ERROR, "Database Connection Error", "Failed to connect to the database.", primaryStage);
            return false;
        }

        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?");
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return true;
            } else {
                showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid username or password.", primaryStage);
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Login Failed", "An error occurred while authenticating user.", primaryStage);
            return false;
        }
    }

    // Método para cadastrar um novo usuário
    private void registerNewUser(String username, String password, Stage primaryStage) {
        if (connection == null) {
            showAlert(Alert.AlertType.ERROR, "Database Connection Error", "Failed to connect to the database.", primaryStage);
            return;
        }

        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO users (username, password) VALUES (?, ?)");
            statement.setString(1, username);
            statement.setString(2, password);
            statement.executeUpdate();
            showAlert(Alert.AlertType.INFORMATION, "Registration", "New user registered successfully!", primaryStage);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Registration Failed", "Failed to register new user.", primaryStage);
        }
    }

    // Método para abrir a página principal
    private void openMainPage(Stage primaryStage, Connection connection) {
        MainPage mainPage = new MainPage();
        primaryStage.close(); // Fecha a tela de login
        primaryStage.setMaximized(true); // Define a janela como tela cheia
        mainPage.show(primaryStage, this, connection); // Passa o primaryStage e o LoginController para a MainPage
    }


    // Método para exibir um alerta
    private void showAlert(Alert.AlertType type, String title, String message, Stage primaryStage) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Métodos de redirecionamento para outras páginas
    public void redirectToImovelRegistration(Stage primaryStage, Connection connection) {
        primaryStage.setMaximized(true); // Define a janela como maximizada
        ImovelRegistrationPage imovelRegistrationPage = new ImovelRegistrationPage(this);
        imovelRegistrationPage.show(primaryStage, connection);
    }

    public void redirectToShowImoveis(Stage primaryStage, Connection connection) {
        primaryStage.setMaximized(true); // Define a janela como maximizada
        ShowImoveisPage showImoveisPage = new ShowImoveisPage(this);
        showImoveisPage.show(primaryStage, connection);
    }

    public void redirectToMainPage(Stage primaryStage, Connection connection) {
        MainPage mainPage = new MainPage();
        primaryStage.close(); // Fecha a página atual
        primaryStage.setMaximized(true); // Define a janela como tela cheia
        mainPage.show(primaryStage, this, connection); // Mostra a página principal
    }

    public void redirectToDespesasGerais(Stage primaryStage, Connection connection) {
        DespesasGerais despesasGerais = new DespesasGerais();
        primaryStage.close(); // Fecha a página atual
        primaryStage.setMaximized(true); // Define a janela como tela cheia
        despesasGerais.show(primaryStage, this, connection); // Mostra a página principal
    }
}
