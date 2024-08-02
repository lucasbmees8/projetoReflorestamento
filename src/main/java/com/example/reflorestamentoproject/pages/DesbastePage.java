package com.example.reflorestamentoproject.pages;

import com.example.reflorestamentoproject.padrao.Desbaste;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DesbastePage extends Stage {
    private final int imovelId;
    private final Connection connection;
    private Stage previousStage;
    private LoginController loginController;
    private TableView<Desbaste> tableView;
    private final ImovelDetailsPage previousPage;
    private final ImovelDetailsPage imovelPage;

    private TextField numeroField;
    private DatePicker dataPicker;
    private TextField arvoresCortadasField;
    private TextField lenhaField;
    private TextField toretesField;
    private TextField toras20a25Field;
    private TextField toras25a33Field;
    private TextField torasAcima33Field;
    private TextField precoLenhaField;
    private TextField precoToretesField;
    private TextField precoToras20a25Field;
    private TextField precoToras25a33Field;
    private TextField precoTorasAcima33Field;

    public DesbastePage(int imovelId, Connection connection, Stage previousStage, LoginController loginController, ImovelDetailsPage previousPage, ImovelDetailsPage imovelPage) {
        this.imovelId = imovelId;
        this.connection = connection;
        this.previousStage = previousStage;
        this.loginController = loginController;
        this.previousPage = previousPage;
        this.imovelPage = imovelPage;
        setTitle("Cadastro de Desbaste");


        // Definindo a paleta de cores
        Font.loadFont(getClass().getResourceAsStream("/fonts/Roboto-Medium.ttf"), 16);
        // Definindo a paleta de cores
        String backgroundColor = "#294B29";
        String textColor = "#DBE7C9";
        String buttonColor = "#50623A";
        String borderColor = "#789461";
        String buttonStyle = "-fx-background-color: " + buttonColor + "; -fx-text-fill: " + textColor + "; -fx-font-size: 16; -fx-font-family: 'Roboto Medium'; -fx-border-color: " + borderColor + "; -fx-background-radius: 15; -fx-border-radius: 15;";
        String buttonColorRemove = "#C82C2C"; // Vermelho
        String buttonStyleRemove = "-fx-background-color: " + buttonColorRemove +
                "; -fx-text-fill: " + textColor +
                "; -fx-font-size: 16; -fx-font-family: 'Roboto Medium'" +
                "; -fx-border-color: " + borderColor +
                "; -fx-background-radius: 15; -fx-border-radius: 15;";


        tableView = new TableView<>();
        tableView.setStyle("-fx-background-color: #333333; -fx-text-fill: white;");
        tableView.setEditable(true);

        TableColumn<Desbaste, Boolean> colunaSelecao = new TableColumn<>("Selecionado");
        colunaSelecao.setCellValueFactory(cellData -> cellData.getValue().selecionadoProperty());
        colunaSelecao.setCellFactory(CheckBoxTableCell.forTableColumn(colunaSelecao));
        tableView.getColumns().add(colunaSelecao);

        TableColumn<Desbaste, LocalDate> dataColumn = new TableColumn<>("Data");
        dataColumn.setCellValueFactory(new PropertyValueFactory<>("data"));

        TableColumn<Desbaste, Integer> arvoresCortadasColumn = new TableColumn<>("Árvores Cortadas");
        arvoresCortadasColumn.setCellValueFactory(new PropertyValueFactory<>("arvoresCortadas"));

        TableColumn<Desbaste, Double> lenhaColumn = new TableColumn<>("Lenha");
        lenhaColumn.setCellValueFactory(new PropertyValueFactory<>("lenha"));

        TableColumn<Desbaste, Double> toretesColumn = new TableColumn<>("Toretes");
        toretesColumn.setCellValueFactory(new PropertyValueFactory<>("toretes"));

        TableColumn<Desbaste, Double> toras20a25Column = new TableColumn<>("Toras 20-25");
        toras20a25Column.setCellValueFactory(new PropertyValueFactory<>("toras20a25"));

        TableColumn<Desbaste, Double> toras25a33Column = new TableColumn<>("Toras 25-33");
        toras25a33Column.setCellValueFactory(new PropertyValueFactory<>("toras25a33"));

        TableColumn<Desbaste, Double> torasAcima33Column = new TableColumn<>("Toras Acima 33");
        torasAcima33Column.setCellValueFactory(new PropertyValueFactory<>("torasAcima33"));

        TableColumn<Desbaste, Double> totalLenhaColumn = new TableColumn<>("Total Lenha");
        totalLenhaColumn.setCellValueFactory(new PropertyValueFactory<>("totalLenha"));

        TableColumn<Desbaste, Double> totalToretesColumn = new TableColumn<>("Total Toretes");
        totalToretesColumn.setCellValueFactory(new PropertyValueFactory<>("totalToretes"));

        TableColumn<Desbaste, Double> totalToras20a25Column = new TableColumn<>("Total Toras 20-25");
        totalToras20a25Column.setCellValueFactory(new PropertyValueFactory<>("totalToras20a25"));

        TableColumn<Desbaste, Double> totalToras25a33Column = new TableColumn<>("Total Toras 25-33");
        totalToras25a33Column.setCellValueFactory(new PropertyValueFactory<>("totalToras25a33"));

        TableColumn<Desbaste, Double> totalTorasAcima33Column = new TableColumn<>("Total Toras Acima 33");
        totalTorasAcima33Column.setCellValueFactory(new PropertyValueFactory<>("totalTorasAcima33"));

        TableColumn<Desbaste, Double> totalColumn = new TableColumn<>("Total Geral");
        totalColumn.setCellValueFactory(new PropertyValueFactory<>("total"));

        TableColumn<Desbaste, Double> precoLenhaColumn = new TableColumn<>("Preço Lenha");
        precoLenhaColumn.setCellValueFactory(new PropertyValueFactory<>("precoLenha"));

        TableColumn<Desbaste, Double> precoToretesColumn = new TableColumn<>("Preço Toretes");
        precoToretesColumn.setCellValueFactory(new PropertyValueFactory<>("precoToretes"));

        TableColumn<Desbaste, Double> precoToras20a25Column = new TableColumn<>("Preço Toras 20-25");
        precoToras20a25Column.setCellValueFactory(new PropertyValueFactory<>("precoToras20a25"));

        TableColumn<Desbaste, Double> precoToras25a33Column = new TableColumn<>("Preço Toras 25-33");
        precoToras25a33Column.setCellValueFactory(new PropertyValueFactory<>("precoToras25a33"));

        TableColumn<Desbaste, Double> precoTorasAcima33Column = new TableColumn<>("Preço Toras Acima 33");
        precoTorasAcima33Column.setCellValueFactory(new PropertyValueFactory<>("precoTorasAcima33"));

        TableColumn<Desbaste, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));


        ScrollPane tableScrollPane = new ScrollPane(tableView);
        tableScrollPane.setFitToWidth(true);
        tableScrollPane.setFitToHeight(true);
        tableScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        tableView.getColumns().addAll(dataColumn, arvoresCortadasColumn, lenhaColumn, toretesColumn, toras20a25Column, toras25a33Column, torasAcima33Column, totalLenhaColumn, totalToretesColumn, totalToras20a25Column, totalToras25a33Column, totalTorasAcima33Column, totalColumn, precoLenhaColumn, precoToretesColumn, precoToras20a25Column, precoToras25a33Column, precoTorasAcima33Column, statusColumn);
        tableView.setStyle("-fx-background-color: " + backgroundColor + "; -fx-control-inner-background: " + backgroundColor + "; -fx-text-fill: " + textColor + "; -fx-font-family: 'Roboto Medium'");

        makeColumnEditable(dataColumn, "data", LocalDate.class);
        makeColumnEditable(arvoresCortadasColumn, "arvores_cortadas", Integer.class);
        makeColumnEditable(lenhaColumn, "lenha", Double.class);
        makeColumnEditable(toretesColumn, "toretes", Double.class);
        makeColumnEditable(toras20a25Column, "toras_20a25", Double.class);
        makeColumnEditable(toras25a33Column, "toras_25a33", Double.class);
        makeColumnEditable(torasAcima33Column, "toras_acima_33", Double.class);
        makeColumnEditable(precoLenhaColumn, "preco_lenha", Double.class);
        makeColumnEditable(precoToretesColumn, "preco_toretes", Double.class);
        makeColumnEditable(precoToras20a25Column, "preco_toras_20a25", Double.class);
        makeColumnEditable(precoToras25a33Column, "preco_toras_25a33", Double.class);
        makeColumnEditable(precoTorasAcima33Column, "preco_toras_acima_33", Double.class);

        Button cadastrarButton = new Button("Cadastrar");
        cadastrarButton.setStyle(buttonStyle);
        cadastrarButton.setOnAction(event -> atualizarDesbaste());

        Button cadastrarPrevisaoButton = new Button("Cadastrar Previsão");
        cadastrarPrevisaoButton.setStyle(buttonStyle);
        cadastrarPrevisaoButton.setOnAction(event -> abrirPopupPrevisao());

        Button excluirButton = new Button("Excluir Selecionados"); // Add the delete button
        excluirButton.setStyle(buttonStyleRemove);
        excluirButton.setOnAction(event -> excluirDesbastesSelecionados());

        GridPane formGrid = new GridPane();
        formGrid.setVgap(10);
        formGrid.setHgap(10);
        formGrid.setPadding(new Insets(10));

        initializeFields();

        formGrid.addRow(0, numeroField);
        formGrid.addRow(1, dataPicker);
        formGrid.addRow(2, arvoresCortadasField);
        formGrid.addRow(3, lenhaField);
        formGrid.addRow(4, toretesField);
        formGrid.addRow(5, toras20a25Field);
        formGrid.addRow(6, toras25a33Field);
        formGrid.addRow(7, torasAcima33Field);
        formGrid.addRow(8, precoLenhaField);
        formGrid.addRow(9, precoToretesField);
        formGrid.addRow(10, precoToras20a25Field);
        formGrid.addRow(11, precoToras25a33Field);
        formGrid.addRow(12, precoTorasAcima33Field);

        VBox mainLayout = new VBox(10, formGrid, cadastrarButton, cadastrarPrevisaoButton, excluirButton, tableScrollPane);
        mainLayout.setPadding(new Insets(10));
        mainLayout.setStyle("-fx-background-color: " + backgroundColor);

        Scene scene = new Scene(mainLayout, 1280, 720);
        setScene(scene);
        carregarDesbastes();
    }

    private void initializeFields() {
        numeroField = new TextField();
        dataPicker = new DatePicker();
        arvoresCortadasField = new TextField();
        lenhaField = new TextField();
        toretesField = new TextField();
        toras20a25Field = new TextField();
        toras25a33Field = new TextField();
        torasAcima33Field = new TextField();
        precoLenhaField = new TextField();
        precoToretesField = new TextField();
        precoToras20a25Field = new TextField();
        precoToras25a33Field = new TextField();
        precoTorasAcima33Field = new TextField();


        // Defina o estilo dos campos
        String borderColor = "#789461";
        String fieldStyle = "-fx-font-family: 'Roboto'; " +
                "-fx-font-size: 14px; " +
                "-fx-pref-width: 200px; " +
                "-fx-background-color: #FFF; " +
                "-fx-text-fill: #294B29; " +
                "-fx-border-color: " + borderColor + "; " +
                "-fx-background-radius: 10; " +
                "-fx-border-radius: 10;";


        numeroField.setStyle(fieldStyle);
        dataPicker.setStyle(fieldStyle);
        arvoresCortadasField.setStyle(fieldStyle);
        lenhaField.setStyle(fieldStyle);
        toretesField.setStyle(fieldStyle);
        toras20a25Field.setStyle(fieldStyle);
        toras25a33Field.setStyle(fieldStyle);
        torasAcima33Field.setStyle(fieldStyle);
        precoLenhaField.setStyle(fieldStyle);
        precoToretesField.setStyle(fieldStyle);
        precoToras20a25Field.setStyle(fieldStyle);
        precoToras25a33Field.setStyle(fieldStyle);
        precoTorasAcima33Field.setStyle(fieldStyle);

        numeroField.setPromptText("Número");
        dataPicker.setPromptText("Data");
        arvoresCortadasField.setPromptText("Árvores Cortadas");
        lenhaField.setPromptText("Lenha");
        toretesField.setPromptText("Toretes");
        toras20a25Field.setPromptText("Toras de 20 a 25");
        toras25a33Field.setPromptText("Toras de 25 a 33");
        torasAcima33Field.setPromptText("Toras acima de 33");
        precoLenhaField.setPromptText("Preço da Lenha");
        precoToretesField.setPromptText("Preço dos Toretes");
        precoToras20a25Field.setPromptText("Preço das Toras de 20 a 25");
        precoToras25a33Field.setPromptText("Preço das Toras de 25 a 33");
        precoTorasAcima33Field.setPromptText("Preço das Toras acima de 33");

    }

    private Desbaste getSelectedDesbaste() {
        for (Desbaste desbaste : tableView.getItems()) {
            if (desbaste.isSelecionado()) {
                return desbaste;
            }
        }
        return null;
    }


    private void excluirDesbastesSelecionados() {
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirmação de Exclusão");
        confirmationAlert.setHeaderText(null);
        confirmationAlert.setContentText("Tem certeza que deseja excluir os registros selecionados?");

        confirmationAlert.showAndWait().ifPresent(buttonType -> {
            if (buttonType == ButtonType.OK) {
                try {
                    // Percorra uma cópia da lista original para evitar problemas de modificação durante a iteração
                    List<Desbaste> desbastesParaExcluir = new ArrayList<>(tableView.getItems());
                    for (Desbaste desbaste : desbastesParaExcluir) {
                        if (desbaste.isSelecionado()) {
                            excluirDesbasteDoBanco(desbaste.getId());
                            tableView.getItems().remove(desbaste); // Remove da tabela após excluir do banco de dados
                        }
                    }
                    imovelPage.atualizarDetalhes(imovelId, connection); // Atualiza os detalhes do imóvel
                } catch (SQLException e) {
                    e.printStackTrace();
                    // Handle the error appropriately (e.g., show an error message)
                }
            }
        });
    }

    private void excluirDesbasteDoBanco(int desbasteId) throws SQLException {
        // Exclui da tabela de previsões
        String deletePrevisaoSql = "DELETE FROM previsao_desbaste WHERE desbaste_id = ?";
        try (PreparedStatement previsaoStatement = connection.prepareStatement(deletePrevisaoSql)) {
            previsaoStatement.setInt(1, desbasteId);
            previsaoStatement.executeUpdate();
        }

        // Exclui da tabela de desbastes
        String deleteDesbasteSql = "DELETE FROM desbaste WHERE id = ?";
        try (PreparedStatement desbasteStatement = connection.prepareStatement(deleteDesbasteSql)) {
            desbasteStatement.setInt(1, desbasteId);
            desbasteStatement.executeUpdate();
        }
    }


    private void abrirPopupPrevisao() {
        Stage popupStage = new Stage();
        popupStage.setTitle("Selecionar Data de Previsão de Desbaste");

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));

        DatePicker previsaoDatePicker = new DatePicker();
        previsaoDatePicker.setPromptText("Selecione a data da previsão");

        Button confirmButton = new Button("Confirmar");
        confirmButton.setOnAction(event -> {
            LocalDate dataPrevisao = previsaoDatePicker.getValue();
            if (dataPrevisao != null) {
                cadastrarDesbastePrevisao(dataPrevisao);
                popupStage.close();
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Data Inválida");
                alert.setHeaderText(null);
                alert.setContentText("Por favor, selecione uma data válida.");
                alert.showAndWait();
            }
        });

        layout.getChildren().addAll(new Label("Selecione a data da previsão:"), previsaoDatePicker, confirmButton);
        Scene popupScene = new Scene(layout);
        popupStage.setScene(popupScene);
        popupStage.showAndWait();
    }

    private void cadastrarDesbastePrevisao(LocalDate dataPrevisao) {
        try {
            // Insere na tabela desbaste com previsao marcada como true
            String sql = "INSERT INTO desbaste (imovel_id, numero, data, arvores_cortadas, lenha, toretes, toras_20a25, toras_25a33, toras_acima_33, total_lenha, total_toretes, total_toras_20a25, total_toras_25a33, total_toras_acima_33, total, preco_lenha, preco_toretes, preco_toras_20a25, preco_toras_25a33, preco_toras_acima_33, previsao) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, true)";
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, imovelId);
            statement.setInt(2, getNewNumero()); // Método para gerar um novo número de desbaste
            statement.setDate(3, Date.valueOf(dataPrevisao));
            // Valores padrões para outros campos
            statement.setInt(4, 0); // Árvore cortadas
            statement.setDouble(5, 0.0); // Lenha
            statement.setDouble(6, 0.0); // Toretes
            statement.setDouble(7, 0.0); // Toras 20-25
            statement.setDouble(8, 0.0); // Toras 25-33
            statement.setDouble(9, 0.0); // Toras acima 33
            statement.setDouble(10, 0.0); // Total lenha
            statement.setDouble(11, 0.0); // Total toretes
            statement.setDouble(12, 0.0); // Total toras 20-25
            statement.setDouble(13, 0.0); // Total toras 25-33
            statement.setDouble(14, 0.0); // Total toras acima 33
            statement.setDouble(15, 0.0); // Total geral
            statement.setDouble(16, 0.0); // Preço lenha
            statement.setDouble(17, 0.0); // Preço toretes
            statement.setDouble(18, 0.0); // Preço toras 20-25
            statement.setDouble(19, 0.0); // Preço toras 25-33
            statement.setDouble(20, 0.0); // Preço toras acima 33

            statement.executeUpdate();

            // Obtendo o ID gerado
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int desbasteId = generatedKeys.getInt(1);

                // Insere na tabela previsao_desbaste
                String previsaoSql = "INSERT INTO previsao_desbaste (desbaste_id, data_prevista, status) VALUES (?, ?, ?)";
                PreparedStatement previsaoStatement = connection.prepareStatement(previsaoSql);
                previsaoStatement.setInt(1, desbasteId);
                previsaoStatement.setDate(2, Date.valueOf(dataPrevisao));
                previsaoStatement.setString(3, "Pendente"); // Status inicial
                previsaoStatement.executeUpdate();
            }

            carregarDesbastes(); // Recarregar a tabela após adicionar a previsão
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int getNewNumero() {
        int newNumero = 1; // Valor inicial
        String sql = "SELECT MAX(numero) FROM desbaste WHERE imovel_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, imovelId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int maxNumero = resultSet.getInt(1);
                if (maxNumero > 0) {
                    newNumero = maxNumero + 1;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return newNumero;
    }

    private <T> void makeColumnEditable(TableColumn<Desbaste, T> column, String fieldName, Class<T> fieldType) {
        column.setCellFactory(col -> new EditingCell<>(fieldType));
        column.setOnEditCommit(event -> {
            Desbaste desbaste = event.getRowValue();
            try {
                T newValue = event.getNewValue();
                executeUpdate(desbaste.getId(), fieldName, newValue);
                desbaste.setFieldValue(fieldName, newValue);
                // Recarregar a página para refletir a atualização
                tableView.refresh();
                reloadPage();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    // Método para recarregar a página
    private void reloadPage() {
        this.close(); // Fecha a janela atual
        DesbastePage newPage = new DesbastePage(imovelId, connection, previousStage, loginController, previousPage, imovelPage);
        newPage.show(); // Abre uma nova instância da página
    }

    // Classe personalizada para células editáveis
    private class EditingCell<T> extends TableCell<Desbaste, T> {
        private final TextField textField = new TextField();

        public EditingCell(Class<T> fieldType) {
            textField.setOnAction(event -> commitEdit(parseValue(textField.getText(), fieldType)));
            textField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
                if (!isNowFocused) {
                    commitEdit(parseValue(textField.getText(), fieldType));
                }
            });
        }

        @Override
        public void startEdit() {
            super.startEdit();
            if (getItem() != null) {
                textField.setText(getItem().toString());
            }
            setText(null);
            setGraphic(textField);
            textField.selectAll();
        }

        @Override
        public void cancelEdit() {
            super.cancelEdit();
            if (getItem() != null) {
                setText(getItem().toString());
            }
            setGraphic(null);
        }

        @Override
        public void updateItem(T item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setText(null);
                setGraphic(null);
            } else {
                if (isEditing()) {
                    if (getItem() != null) {
                        textField.setText(getItem().toString());
                    }
                    setText(null);
                    setGraphic(textField);
                } else {
                    if (getItem() != null) {
                        setText(getItem().toString());
                    }
                    setGraphic(null);
                }
            }
        }

        private T parseValue(String value, Class<T> fieldType) {
            try {
                if (fieldType == Integer.class) {
                    return fieldType.cast(Integer.valueOf(value));
                } else if (fieldType == Double.class) {
                    return fieldType.cast(Double.valueOf(value));
                } else if (fieldType == LocalDate.class) {
                    return fieldType.cast(LocalDate.parse(value));
                } else {
                    return fieldType.cast(value);
                }
            } catch (Exception e) {
                return null; // Tratar exceções conforme necessário
            }
        }
    }

    private void executeUpdate(int id, String fieldName, Object newValue) throws SQLException {
        String sql = "UPDATE desbaste SET " + fieldName + " = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, newValue);
            statement.setInt(2, id);
            statement.executeUpdate();
        }

        // Recalcular os totais
        String selectQuery = "SELECT lenha, toretes, toras_20a25, toras_25a33, toras_acima_33, preco_lenha, preco_toretes, preco_toras_20a25, preco_toras_25a33, preco_toras_acima_33 FROM desbaste WHERE id = ?";
        try (PreparedStatement selectStatement = connection.prepareStatement(selectQuery)) {
            selectStatement.setInt(1, id);
            ResultSet rs = selectStatement.executeQuery();
            if (rs.next()) {
                double lenha = rs.getDouble("lenha");
                double toretes = rs.getDouble("toretes");
                double toras20a25 = rs.getDouble("toras_20a25");
                double toras25a33 = rs.getDouble("toras_25a33");
                double torasAcima33 = rs.getDouble("toras_acima_33");
                double precoLenha = rs.getDouble("preco_lenha");
                double precoToretes = rs.getDouble("preco_toretes");
                double precoToras20a25 = rs.getDouble("preco_toras_20a25");
                double precoToras25a33 = rs.getDouble("preco_toras_25a33");
                double precoTorasAcima33 = rs.getDouble("preco_toras_acima_33");

                double totalLenha = lenha * precoLenha;
                double totalToretes = toretes * precoToretes;
                double totalToras20a25 = toras20a25 * precoToras20a25;
                double totalToras25a33 = toras25a33 * precoToras25a33;
                double totalTorasAcima33 = torasAcima33 * precoTorasAcima33;
                double total = totalLenha + totalToretes + totalToras20a25 + totalToras25a33 + totalTorasAcima33;

                String updateTotalsQuery = "UPDATE desbaste SET total_lenha = ?, total_toretes = ?, total_toras_20a25 = ?, total_toras_25a33 = ?, total_toras_acima_33 = ?, total = ? WHERE id = ?";
                try (PreparedStatement updateTotalsStatement = connection.prepareStatement(updateTotalsQuery)) {
                    updateTotalsStatement.setDouble(1, totalLenha);
                    updateTotalsStatement.setDouble(2, totalToretes);
                    updateTotalsStatement.setDouble(3, totalToras20a25);
                    updateTotalsStatement.setDouble(4, totalToras25a33);
                    updateTotalsStatement.setDouble(5, totalTorasAcima33);
                    updateTotalsStatement.setDouble(6, total);
                    updateTotalsStatement.setInt(7, id);
                    updateTotalsStatement.executeUpdate();
                }
            }
        }

        // Atualizar árvores remanescentes e árvores cortadas na tabela imoveis
        String updateImoveisQuery = "UPDATE imoveis SET arvores_remanescentes = num_arvores_plantadas - (SELECT COALESCE(SUM(arvores_cortadas), 0) FROM desbaste WHERE imovel_id = ?), arvores_cortadas = (SELECT COALESCE(SUM(arvores_cortadas), 0) FROM desbaste WHERE imovel_id = ?) WHERE id = ?";
        try (PreparedStatement updateImoveisStatement = connection.prepareStatement(updateImoveisQuery)) {
            updateImoveisStatement.setInt(1, imovelId);
            updateImoveisStatement.setInt(2, imovelId);
            updateImoveisStatement.setInt(3, imovelId);
            updateImoveisStatement.executeUpdate();
            imovelPage.atualizarDetalhes(imovelId, connection);
        }
    }

    private void atualizarDesbaste() {
        Desbaste desbasteSelecionado = getSelectedDesbaste();
        if (desbasteSelecionado != null) {
            try {
                // Calcular os valores totais
                double lenha = Double.parseDouble(lenhaField.getText());
                double toretes = Double.parseDouble(toretesField.getText());
                double toras20a25 = Double.parseDouble(toras20a25Field.getText());
                double toras25a33 = Double.parseDouble(toras25a33Field.getText());
                double torasAcima33 = Double.parseDouble(torasAcima33Field.getText());
                double precoLenha = Double.parseDouble(precoLenhaField.getText());
                double precoToretes = Double.parseDouble(precoToretesField.getText());
                double precoToras20a25 = Double.parseDouble(precoToras20a25Field.getText());
                double precoToras25a33 = Double.parseDouble(precoToras25a33Field.getText());
                double precoTorasAcima33 = Double.parseDouble(precoTorasAcima33Field.getText());

                double totalLenha = lenha * precoLenha;
                double totalToretes = toretes * precoToretes;
                double totalToras20a25 = toras20a25 * precoToras20a25;
                double totalToras25a33 = toras25a33 * precoToras25a33;
                double totalTorasAcima33 = torasAcima33 * precoTorasAcima33;
                double total = totalLenha + totalToretes + totalToras20a25 + totalToras25a33 + totalTorasAcima33;

                // Atualize os dados do desbaste selecionado
                String sql = "UPDATE desbaste SET numero = ?, data = ?, arvores_cortadas = ?, lenha = ?, toretes = ?, toras_20a25 = ?, toras_25a33 = ?, toras_acima_33 = ?, total_lenha = ?, total_toretes = ?, total_toras_20a25 = ?, total_toras_25a33 = ?, total_toras_acima_33 = ?, total = ?, preco_lenha = ?, preco_toretes = ?, preco_toras_20a25 = ?, preco_toras_25a33 = ?, preco_toras_acima_33 = ? WHERE id = ?";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setInt(1, Integer.parseInt(numeroField.getText()));
                statement.setDate(2, Date.valueOf(dataPicker.getValue()));
                statement.setInt(3, Integer.parseInt(arvoresCortadasField.getText()));
                statement.setDouble(4, lenha);
                statement.setDouble(5, toretes);
                statement.setDouble(6, toras20a25);
                statement.setDouble(7, toras25a33);
                statement.setDouble(8, torasAcima33);
                statement.setDouble(9, totalLenha);
                statement.setDouble(10, totalToretes);
                statement.setDouble(11, totalToras20a25);
                statement.setDouble(12, totalToras25a33);
                statement.setDouble(13, totalTorasAcima33);
                statement.setDouble(14, total);
                statement.setDouble(15, precoLenha);
                statement.setDouble(16, precoToretes);
                statement.setDouble(17, precoToras20a25);
                statement.setDouble(18, precoToras25a33);
                statement.setDouble(19, precoTorasAcima33);
                statement.setInt(20, desbasteSelecionado.getId());
                statement.executeUpdate();

                // Atualizar o status na tabela previsao_desbaste
                String updateStatusSQL = "UPDATE previsao_desbaste SET status = ? WHERE desbaste_id = ?";
                PreparedStatement updateStatusStmt = connection.prepareStatement(updateStatusSQL);
                updateStatusStmt.setString(1, "Realizado");
                updateStatusStmt.setInt(2, desbasteSelecionado.getId());
                updateStatusStmt.executeUpdate();

                // Atualizar árvores remanescentes e árvores cortadas na tabela imoveis
                String updateQuery = "UPDATE imoveis SET arvores_remanescentes = num_arvores_plantadas - (SELECT COALESCE(SUM(arvores_cortadas), 0) FROM desbaste WHERE imovel_id = ?), arvores_cortadas = (SELECT COALESCE(SUM(arvores_cortadas), 0) FROM desbaste WHERE imovel_id = ?) WHERE id = ?";
                try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                    updateStatement.setInt(1, imovelId);
                    updateStatement.setInt(2, imovelId);
                    updateStatement.setInt(3, imovelId);
                    updateStatement.executeUpdate();
                    imovelPage.atualizarDetalhes(imovelId, connection); // Atualizar detalhes do imóvel
                }

                carregarDesbastes(); // Recarrega a tabela após atualizar os dados
                clearFields(); // Limpar campos após atualização
            } catch (SQLException | NumberFormatException e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Nenhum Desbaste Selecionado");
            alert.setHeaderText(null);
            alert.setContentText("Por favor, selecione um desbaste para atualizar.");
            alert.showAndWait();
        }
    }

    private void carregarDesbastes() {
        tableView.getItems().clear();
        try {
            String sql = "SELECT d.*, pd.status " +
                    "FROM desbaste d " +
                    "INNER JOIN previsao_desbaste pd ON d.id = pd.desbaste_id " +
                    "WHERE d.imovel_id = ?";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, imovelId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Desbaste desbaste = new Desbaste(
                        resultSet.getInt("id"),
                        resultSet.getInt("imovel_id"),
                        resultSet.getInt("numero"),
                        resultSet.getDate("data").toLocalDate(),
                        resultSet.getInt("arvores_cortadas"),
                        resultSet.getDouble("lenha"),
                        resultSet.getDouble("toretes"),
                        resultSet.getDouble("toras_20a25"),
                        resultSet.getDouble("toras_25a33"),
                        resultSet.getDouble("toras_acima_33"),
                        resultSet.getDouble("total_lenha"),
                        resultSet.getDouble("total_toretes"),
                        resultSet.getDouble("total_toras_20a25"),
                        resultSet.getDouble("total_toras_25a33"),
                        resultSet.getDouble("total_toras_acima_33"),
                        resultSet.getDouble("total"),
                        resultSet.getDouble("preco_lenha"),
                        resultSet.getDouble("preco_toretes"),
                        resultSet.getDouble("preco_toras_20a25"),
                        resultSet.getDouble("preco_toras_25a33"),
                        resultSet.getDouble("preco_toras_acima_33"),
                        resultSet.getString("status")
                );

                tableView.getItems().add(desbaste);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void clearFields() {
        numeroField.clear();
        dataPicker.setValue(null);
        arvoresCortadasField.clear();
        lenhaField.clear();
        toretesField.clear();
        toras20a25Field.clear();
        toras25a33Field.clear();
        torasAcima33Field.clear();
        precoLenhaField.clear();
        precoToretesField.clear();
        precoToras20a25Field.clear();
        precoToras25a33Field.clear();
        precoTorasAcima33Field.clear();
    }
}
