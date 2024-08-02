package com.example.reflorestamentoproject.pages;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class EditarPage extends Stage {

    private final int imovelId;
    private final Connection connection;
    private final ImovelDetailsPage previousPage;
    private final LoginController loginController;

    public EditarPage(int imovelId, Connection connection, ImovelDetailsPage previousPage, LoginController loginController) {
        this.imovelId = imovelId;
        this.connection = connection;
        this.previousPage = previousPage;
        this.loginController = loginController;
        String backgroundColor = "#294B29";
        String textColor = "#DBE7C9";
        String buttonColor = "#50623A";
        String borderColor = "#789461";
        String buttonStyle = "-fx-background-color: " + buttonColor + "; -fx-text-fill: " + textColor + "; -fx-font-size: 16; -fx-font-family: 'Roboto Medium'; -fx-border-color: " + borderColor + ";";


        setTitle("Editar Imóvel");

        // Configurando a fonte Roboto para todos os elementos da página
        Font.loadFont(getClass().getResourceAsStream("/fonts/Roboto-Medium.ttf"), 14);

        // Criando os campos de entrada para as informações do imóvel
        TextField descricaoField = createStyledTextField();
        TextField areaImovelField = createStyledTextField();
        TextField areaPlantioField = createStyledTextField();
        TextField especieField = createStyledTextField();
        TextField origemField = createStyledTextField();
        TextField numArvoresPlantadasField = createStyledTextField();
        TextField numArvoresCortadasField = createStyledTextField();
        TextField numArvoresRemanescentesField = createStyledTextField();
        TextField matriculaField = createStyledTextField();
        DatePicker dataPlantioPicker = new DatePicker();
        dataPlantioPicker.setPrefWidth(200); // Definindo a largura do DatePicker
        DatePicker dataContratoPicker = new DatePicker();
        dataContratoPicker.setPrefWidth(200); // Definindo a largura do DatePicker
        DatePicker vencimentoContratoPicker = new DatePicker();
        vencimentoContratoPicker.setPrefWidth(200); // Definindo a largura do DatePicker
        TextField numeroCCIRField = createStyledTextField();
        TextField numeroITRField = createStyledTextField();
        TextField proprietarioField = createStyledTextField();
        TextField arrendatarioField = createStyledTextField();
        TextField municipioField = createStyledTextField();
        TextField localidadeField = createStyledTextField();
        TextField alturaDesramaField = createStyledTextField();
        ComboBox<String> classeComboBox = new ComboBox<>();

        classeComboBox.getItems().addAll("Próprio", "Arrendado");
        classeComboBox.setStyle("-fx-text-fill:" + borderColor);
        classeComboBox.setPrefWidth(200); // Definindo a largura do ComboBox

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.addRow(0, new Label("Descrição:"), descricaoField);
        gridPane.addRow(1, new Label("Área do Imóvel:"), areaImovelField);
        gridPane.addRow(2, new Label("Área de Plantio:"), areaPlantioField);
        gridPane.addRow(3, new Label("Espécie:"), especieField);
        gridPane.addRow(4, new Label("Origem:"), origemField);
        gridPane.addRow(5, new Label("Número de Árvores Plantadas:"), numArvoresPlantadasField);
        gridPane.addRow(6, new Label("Número de Árvores Cortadas:"), numArvoresCortadasField);
        gridPane.addRow(7, new Label("Número de Árvores Remanescentes:"), numArvoresRemanescentesField);
        gridPane.addRow(9, new Label("Matrícula:"), matriculaField);
        gridPane.addRow(10, new Label("Data de Plantio:"), dataPlantioPicker);
        gridPane.addRow(11, new Label("Data do Contrato:"), dataContratoPicker);
        gridPane.addRow(12, new Label("Vencimento do Contrato:"), vencimentoContratoPicker);
        gridPane.addRow(13, new Label("Número do CCIR:"), numeroCCIRField);
        gridPane.addRow(14, new Label("Número do IT:"), numeroITRField);
        gridPane.addRow(15, new Label("Proprietário:"), proprietarioField);
        gridPane.addRow(16, new Label("Arrendatário:"), arrendatarioField);
        gridPane.addRow(17, new Label("Município:"), municipioField);
        gridPane.addRow(18, new Label("Localidade:"), localidadeField);
        gridPane.addRow(19, new Label("Altura de Desrama:"), alturaDesramaField);
        gridPane.addRow(20, new Label("Classe:"), classeComboBox);

        // Configurando rótulos (labels) para branco e fonte Roboto
        for (Node node : gridPane.getChildren()) {
            if (node instanceof Label) {
                ((Label) node).setTextFill(Color.WHITE);
                ((Label) node).setFont(Font.font("Roboto", 14));
            }
        }

        // Carregar os dados do imóvel para edição
        try {
            String query = "SELECT * FROM imoveis WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, imovelId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                descricaoField.setText(resultSet.getString("descricao"));
                areaImovelField.setText(resultSet.getString("area_imovel"));
                areaPlantioField.setText(resultSet.getString("area_plantio"));
                especieField.setText(resultSet.getString("especie"));
                origemField.setText(resultSet.getString("origem"));
                numArvoresPlantadasField.setText(resultSet.getString("num_arvores_plantadas"));
                numArvoresCortadasField.setText(resultSet.getString("num_arvores_cortadas"));
                numArvoresRemanescentesField.setText(resultSet.getString("num_arvores_remanescentes"));
                matriculaField.setText(resultSet.getString("matricula"));
                // Configurar valores para os DatePickers
                dataPlantioPicker.setValue(LocalDate.parse(resultSet.getString("data_plantio")));
                dataContratoPicker.setValue(LocalDate.parse(resultSet.getString("data_contrato")));
                vencimentoContratoPicker.setValue(LocalDate.parse(resultSet.getString("vencimento_contrato")));
                numeroCCIRField.setText(resultSet.getString("numero_ccir"));
                numeroITRField.setText(resultSet.getString("numero_itr"));
                proprietarioField.setText(resultSet.getString("proprietario"));
                arrendatarioField.setText(resultSet.getString("arrendatario"));
                municipioField.setText(resultSet.getString("municipio"));
                localidadeField.setText(resultSet.getString("localidade"));
                alturaDesramaField.setText(resultSet.getString("altura_desrama"));
                classeComboBox.setValue(resultSet.getString("classe"));
                // Adicionar configurações para os campos restantes, se houver
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Button salvarButton = new Button("Salvar");
        salvarButton.setStyle(buttonStyle);
        salvarButton.setOnAction(event -> {
            // Obter os valores dos campos de entrada
            String descricao = descricaoField.getText();
            double areaImovel = Double.parseDouble(areaImovelField.getText());
            double areaPlantio = Double.parseDouble(areaPlantioField.getText());
            String especie = especieField.getText();
            String origem = origemField.getText();
            int numArvoresPlantadas = Integer.parseInt(numArvoresPlantadasField.getText());
            int numArvoresCortadas = Integer.parseInt(numArvoresCortadasField.getText());
            int numArvoresRemanescentes = Integer.parseInt(numArvoresRemanescentesField.getText());
            String matricula = matriculaField.getText();
            String dataPlantio = dataPlantioPicker.getValue().toString();
            String dataContrato = dataContratoPicker.getValue().toString();
            String vencimentoContrato = vencimentoContratoPicker.getValue().toString();
            String numeroCCIR = numeroCCIRField.getText();
            String numeroITR = numeroITRField.getText();
            String proprietario = proprietarioField.getText();
            String arrendatario = arrendatarioField.getText();
            String municipio = municipioField.getText();
            String localidade = localidadeField.getText();
            double alturaDesrama = Double.parseDouble(alturaDesramaField.getText());
            String classe = classeComboBox.getValue();

            // Atualizar as informações do imóvel no banco de dados
            try {
                String query = "UPDATE imoveis SET descricao = ?, area_imovel = ?, area_plantio = ?, especie = ?, origem = ?, num_arvores_plantadas = ?, num_arvores_cortadas = ?, num_arvores_remanescentes = ?, matricula = ?, data_plantio = ?, data_contrato = ?, vencimento_contrato = ?, numero_ccir = ?, numero_itr = ?, proprietario = ?, arrendatario = ?, municipio = ?, localidade = ?, altura_desrama = ?, classe = ? WHERE id = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, descricao);
                statement.setDouble(2, areaImovel);
                statement.setDouble(3, areaPlantio);
                statement.setString(4, especie);
                statement.setString(5, origem);
                statement.setInt(6, numArvoresPlantadas);
                statement.setInt(7, numArvoresCortadas);
                statement.setInt(8, numArvoresRemanescentes);
                statement.setString(10, matricula);
                statement.setString(11, dataPlantio);
                statement.setString(12, dataContrato);
                statement.setString(13, vencimentoContrato);
                statement.setString(14, numeroCCIR);
                statement.setString(15, numeroITR);
                statement.setString(16, proprietario);
                statement.setString(17, arrendatario);
                statement.setString(18, municipio);
                statement.setString(19, localidade);
                statement.setDouble(20, alturaDesrama);
                statement.setString(21, classe);
                statement.setInt(22, imovelId);
                statement.executeUpdate();
                close(); // Fechar a janela de edição
                previousPage.atualizarDetalhes(imovelId, connection); // Atualizar os detalhes na página anterior
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        VBox vbox = new VBox(10, gridPane, salvarButton);
        vbox.setStyle("-fx-background-color:" + backgroundColor);
        vbox.setPadding(new Insets(10));
        Scene scene = new Scene(vbox);
        scene.setFill(Color.rgb(29, 48, 29));
        setScene(scene);
    }

    private TextField createStyledTextField() {
        TextField textField = new TextField();
        textField.setFont(Font.font("Roboto", 14));
        return textField;
    }
}
