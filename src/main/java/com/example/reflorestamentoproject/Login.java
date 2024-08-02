package com.example.reflorestamentoproject;

import com.example.reflorestamentoproject.bd.DatabaseConnector;
import com.example.reflorestamentoproject.pages.LoginController;
import javafx.application.Application;
import javafx.stage.Stage;

import java.sql.Connection; // Importação necessária
import java.sql.SQLException;

public class Login extends Application {

    @Override
    public void start(Stage primaryStage) {

        // Carregar a fonte Roboto-Medium
        primaryStage.setMaximized(true); // Definindo o Stage como tela cheia
        DatabaseConnector databaseConnector = new DatabaseConnector();
        try {
            Connection connection = databaseConnector.connectToDatabase();
            LoginController loginController = new LoginController(connection);
            loginController.show(primaryStage);
        } catch (SQLException e) {
            e.printStackTrace();
            // Lidar com o erro de conexão aqui
        }
    }
    public static void main(String[] args) {
        launch(args);
    }
}
