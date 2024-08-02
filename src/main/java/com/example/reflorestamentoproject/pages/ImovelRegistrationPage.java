package com.example.reflorestamentoproject.pages;

import com.example.reflorestamentoproject.pages.LoginController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.sql.*;

public class ImovelRegistrationPage {
    private final LoginController loginController;

    // Botões de opção
    private final Button arrendadoOption;
    private final Button proprioOption;
    private final Button submitButton;

    // Campos de texto
    private final TextField descricaoField = createTextField("Descrição");
    private final TextField areaImovelField = createTextField("Área do Imóvel");
    private final TextField areaPlantioField = createTextField("Área de Plantio");
    private final TextField especieField = createTextField("Espécie");
    private final TextField origemField = createTextField("Origem");
    private final TextField numArvoresField = createTextField("Número de Árvores Plantadas");
    private final DatePicker dataPlantioPicker = createDateField("Data de Plantio");
    private final TextField numeroCCIRField = createTextField("Número CCIR");
    private final TextField numeroITRField = createTextField("Número ITR");
    private final TextField proprietarioField = createTextField("Proprietário");
    private final TextField matriculaField = createTextField("Número da Matrícula");
    private final TextField municipioField = createTextField("Município");
    private final TextField localidadeField = createTextField("Localidade");
    private final TextField codigoCcField = createTextField("Código CC");

    // Campos específicos para arrendado
    private final DatePicker dataContratoPicker = createDateField("Data do Contrato");
    private final DatePicker vencimentoContratoPicker = createDateField("Vencimento do Contrato");
    private final TextField arrendatarioField = createTextField("Arrendatário");

    // Caixa de formulários
    private final VBox formBox = new VBox(10);

    // Estilo
    private static final String backgroundColor = "#294B29";
    private static final String textColor = "#DBE7C9";
    private static final String buttonColor = "#50623A";
    private static final String borderColor = "#789461";
    private static final String buttonStyle = "-fx-background-color: " + buttonColor + "; -fx-text-fill: " + textColor + "; -fx-font-size: 16; -fx-font-family: 'Roboto Medium'; -fx-border-color: " + borderColor + "; -fx-background-radius: 10; -fx-border-radius: 10;";
    private static final String fieldStyle = "-fx-font-family: 'Roboto'; -fx-font-size: 14px; -fx-width: 200px; -fx-background-color: #FFF; -fx-text-fill: #294B29; -fx-border-color: " + borderColor + "; -fx-background-radius: 10; -fx-border-radius: 10;";
    private static final String errorFieldStyle = "-fx-background-color: #FFCDD2; -fx-text-fill: #D32F2F; -fx-border-color: #D32F2F;";

    private boolean isArrendado = false;

    public ImovelRegistrationPage(LoginController loginController) {
        this.loginController = loginController;
        this.arrendadoOption = createButton("Arrendado");
        this.proprioOption = createButton("Próprio");
        this.submitButton = createButton("Registrar");
        this.submitButton.setVisible(false);
    }

    public void show(Stage primaryStage, Connection connection) {
        primaryStage.setTitle("Cadastro de Imóvel");
        primaryStage.setFullScreen(true);

        formBox.setPadding(new Insets(20));
        formBox.setAlignment(Pos.CENTER);
        formBox.setStyle("-fx-background-color: " + backgroundColor + ";");

        HBox optionsBox = new HBox(20, arrendadoOption, proprioOption);
        optionsBox.setAlignment(Pos.CENTER);
        optionsBox.setStyle("-fx-background-color: " + backgroundColor + "; -fx-text-fill: " + textColor + ";");

        Button backButton = createButton("Voltar");
        backButton.setOnAction(event -> loginController.redirectToMainPage(primaryStage, connection));

        HBox backButtonBox = new HBox(backButton);
        backButtonBox.setAlignment(Pos.TOP_LEFT);
        backButtonBox.setPadding(new Insets(10));

        VBox mainBox = new VBox(20, backButtonBox, optionsBox, formBox, submitButton);
        mainBox.setPadding(new Insets(40));
        mainBox.setAlignment(Pos.CENTER);
        mainBox.setStyle("-fx-background-color: " + backgroundColor + ";");

        ScrollPane scrollPane = new ScrollPane(mainBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: " + backgroundColor + ";");

        Scene scene = new Scene(scrollPane, 1700, 800);
        scene.getStylesheets().add("https://fonts.googleapis.com/css?family=Roboto:400,500,700&display=swap");

        primaryStage.setScene(scene);
        primaryStage.show();

        arrendadoOption.setOnAction(event -> showForm(true));
        proprioOption.setOnAction(event -> showForm(false));
        submitButton.setOnAction(event -> {
            if (validateFields()) {
                if (isArrendado) {
                    registerArrendado(connection);
                } else {
                    registerProprio(connection);
                }
            }
        });

        showForm(false); // Iniciar mostrando o formulário de Próprio
    }

    private void showForm(boolean isArrendado) {
        this.isArrendado = isArrendado;
        formBox.getChildren().clear();
        formBox.getChildren().addAll(
                descricaoField, areaImovelField, areaPlantioField, especieField, origemField,
                numArvoresField, dataPlantioPicker, numeroCCIRField, numeroITRField, proprietarioField,
                matriculaField, municipioField, localidadeField, codigoCcField
        );
        if (isArrendado) {
            formBox.getChildren().addAll(dataContratoPicker, vencimentoContratoPicker, arrendatarioField);
        }
        submitButton.setVisible(true);
    }

    private boolean validateFields() {
        boolean isValid = validateTextField(descricaoField) && validateTextField(areaImovelField) &&
                validateTextField(areaPlantioField) && validateTextField(especieField) &&
                validateTextField(origemField) && validateTextField(numArvoresField) &&
                validateDatePicker(dataPlantioPicker) && validateTextField(numeroCCIRField) &&
                validateTextField(numeroITRField) && validateTextField(proprietarioField) &&
                validateTextField(matriculaField) && validateTextField(municipioField) &&
                validateTextField(localidadeField) &&  validateTextField(codigoCcField);

        if (isArrendado) {
            isValid &= validateDatePicker(dataContratoPicker) && validateDatePicker(vencimentoContratoPicker) &&
                    validateTextField(arrendatarioField);
        }
        return isValid;
    }

    private boolean validateTextField(TextField field) {
        if (field.getText().isEmpty()) {
            field.setStyle(fieldStyle + errorFieldStyle);
            return false;
        } else {
            field.setStyle(fieldStyle);
            return true;
        }
    }

    private boolean validateDatePicker(DatePicker datePicker) {
        if (datePicker.getValue() == null) {
            datePicker.setStyle(fieldStyle + errorFieldStyle);
            return false;
        } else {
            datePicker.setStyle(fieldStyle);
            return true;
        }
    }
    private int calcularArvoresRemanescentes() {
        return Integer.parseInt(numArvoresField.getText());
    }

    private int calcularNumArvoresPorHectare() {
        int arvoresRemanescentes = calcularArvoresRemanescentes();
        return arvoresRemanescentes; // Supondo que seja remanescentes por arvores plantadas, que seria 1 neste caso.
    }


    private void registerProprio(Connection connection) {
        String insertQuery = "INSERT INTO imoveis (descricao, area_imovel, area_plantio, especie, origem, num_arvores_plantadas, data_plantio, numero_ccir, numero_itr, proprietario, matricula, municipio, localidade, classe, arvores_remanescentes, arvores_cortadas, altura_desrama, num_arvores_por_hectare, codigo_cc) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 'proprio', ?, 0, 0, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setString(1, descricaoField.getText());
            preparedStatement.setString(2, areaImovelField.getText());
            preparedStatement.setString(3, areaPlantioField.getText());
            preparedStatement.setString(4, especieField.getText());
            preparedStatement.setString(5, origemField.getText());
            preparedStatement.setString(6, numArvoresField.getText());
            preparedStatement.setDate(7, Date.valueOf(dataPlantioPicker.getValue()));
            preparedStatement.setString(8, numeroCCIRField.getText());
            preparedStatement.setString(9, numeroITRField.getText());
            preparedStatement.setString(10, proprietarioField.getText());
            preparedStatement.setString(11, matriculaField.getText());
            preparedStatement.setString(12, municipioField.getText());
            preparedStatement.setString(13, localidadeField.getText());
            preparedStatement.setInt(14, calcularArvoresRemanescentes());
            preparedStatement.setInt(15, calcularNumArvoresPorHectare());
            preparedStatement.setString(16, codigoCcField.getText());

            preparedStatement.executeUpdate();
            showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Imóvel próprio registrado com sucesso!");
            clearFields(); // Limpar campos após sucesso

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erro", "Falha ao registrar o imóvel próprio: " + e.getMessage());
        }
    }

    private void registerArrendado(Connection connection) {
        String insertImovelQuery = "INSERT INTO imoveis (descricao, area_imovel, area_plantio, especie, origem, num_arvores_plantadas, data_plantio, numero_ccir, numero_itr, proprietario, matricula, municipio, localidade, classe, arvores_remanescentes, arvores_cortadas, altura_desrama, num_arvores_por_hectare, codigo_cc) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 'arrendado', ?, 0, 0, ?, ?)";
        String insertArrendadoQuery = "INSERT INTO imoveis_arrendados (imovel_id, data_contrato, vencimento_contrato, arrendatario) VALUES (?, ?, ?, ?)";

        try (PreparedStatement imovelStatement = connection.prepareStatement(insertImovelQuery, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement arrendadoStatement = connection.prepareStatement(insertArrendadoQuery)) {
            // Inserir dados na tabela imoveis
            imovelStatement.setString(1, descricaoField.getText());
            imovelStatement.setString(2, areaImovelField.getText());
            imovelStatement.setString(3, areaPlantioField.getText());
            imovelStatement.setString(4, especieField.getText());
            imovelStatement.setString(5, origemField.getText());
            imovelStatement.setString(6, numArvoresField.getText());
            imovelStatement.setDate(7, Date.valueOf(dataPlantioPicker.getValue()));
            imovelStatement.setString(8, numeroCCIRField.getText());
            imovelStatement.setString(9, numeroITRField.getText());
            imovelStatement.setString(10, proprietarioField.getText());
            imovelStatement.setString(11, matriculaField.getText());
            imovelStatement.setString(12, municipioField.getText());
            imovelStatement.setString(13, localidadeField.getText());
            imovelStatement.setInt(14, calcularArvoresRemanescentes());
            imovelStatement.setInt(15, calcularNumArvoresPorHectare());
            imovelStatement.setString(16, codigoCcField.getText());

            imovelStatement.executeUpdate();

            // Obter o ID do imóvel recém-inserido
            ResultSet generatedKeys = imovelStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int imovelId = generatedKeys.getInt(1);

                // Inserir dados na tabela imoveis_arrendados
                arrendadoStatement.setInt(1, imovelId);
                arrendadoStatement.setDate(2, Date.valueOf(dataContratoPicker.getValue()));
                arrendadoStatement.setDate(3, Date.valueOf(vencimentoContratoPicker.getValue()));
                arrendadoStatement.setString(4, arrendatarioField.getText());

                arrendadoStatement.executeUpdate();

                showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Imóvel arrendado registrado com sucesso!");
                clearFields(); // Limpar campos após sucesso
            } else {
                throw new SQLException("Falha ao obter o ID do imóvel.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erro", "Falha ao registrar o imóvel arrendado: " + e.getMessage());
            clearFields(); // Limpar campos após sucesso
        }
    }



    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void clearFields() {
        descricaoField.clear();
        areaImovelField.clear();
        areaPlantioField.clear();
        especieField.clear();
        origemField.clear();
        numArvoresField.clear();
        dataPlantioPicker.setValue(null);
        numeroCCIRField.clear();
        numeroITRField.clear();
        proprietarioField.clear();
        matriculaField.clear();
        municipioField.clear();
        localidadeField.clear();
        codigoCcField.clear();

        if (isArrendado) {
            dataContratoPicker.setValue(null);
            vencimentoContratoPicker.setValue(null);
            arrendatarioField.clear();
        }
    }
    private void styleTextField(TextField textField) {
        textField.setMaxWidth(200);
        textField.setStyle(fieldStyle);
    }

    private void styleDataField(DatePicker datePicker) {
        datePicker.setStyle(fieldStyle);
    }

    private TextField createTextField(String promptText) {
        TextField textField = new TextField();
        textField.setPromptText(promptText);
        textField.setMaxWidth(200);
        textField.setStyle(fieldStyle);
        return textField;
    }

    private DatePicker createDateField(String promptText) {
        DatePicker datePicker = new DatePicker();
        datePicker.setPromptText(promptText);
        datePicker.setStyle(fieldStyle);
        return datePicker;
    }

    private Button createButton(String text) {
        Button button = new Button(text);
        button.setStyle(buttonStyle);
        return button;
    }
}
