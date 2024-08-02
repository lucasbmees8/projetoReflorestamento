package com.example.reflorestamentoproject.pages;

import com.example.reflorestamentoproject.padrao.Despesa;
import com.example.reflorestamentoproject.pdf.GeradorPDFDespesa;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;
import java.sql.*;
import java.text.*;
import java.time.LocalDate;
import java.util.*;
import java.util.Date;
import java.util.List;

public class DespesaPage extends Stage {

    private final int imovelId;
    private final Connection connection;
    private Stage previousStage;
    private LoginController loginController;
    private DatePicker dataPicker;
    private TextField descricaoField;
    private TextField numeroNotaFiscalField;
    private TextField fornecedorField;
    private TextField produtoField;
    private TextField unidadeField;
    private TextField quantidadeField;
    private TextField valorUnitarioField;
    private ComboBox<String> tipoDespesaComboBox;
    private DatePicker validadePicker;

    public DespesaPage(int imovelId, Connection connection, Stage previousStage, LoginController loginController) {
        this.imovelId = imovelId;
        this.connection = connection;
        this.previousStage = previousStage;
        this.loginController = loginController;
        setTitle("Cadastro de Despesa");

        // Definindo o esquema de cores e estilos
        String backgroundColor = "#294B29";
        String textColor = "#DBE7C9";
        String buttonColor = "#50623A";
        String borderColor = "#789461";
        String buttonStyle = "-fx-background-color: " + buttonColor + "; -fx-text-fill: " + textColor + "; -fx-font-size: 16; -fx-font-family: 'Roboto Medium'; -fx-border-color: " + borderColor + ";";

        // Estilo para os campos
        String fieldStyle = "-fx-font-family: 'Roboto'; " +
                "-fx-font-size: 14px; " +
                "-fx-pref-width: 200px; " +
                "-fx-background-color: #FFF; " +
                "-fx-text-fill: #294B29; " +
                "-fx-border-color: " + borderColor + "; " +
                "-fx-background-radius: 10; " +
                "-fx-border-radius: 10;";

        TableView<Despesa> tableView = new TableView<>();
        tableView.setStyle("-fx-background-color: " + backgroundColor + "; -fx-control-inner-background: " + backgroundColor + "; -fx-text-fill: " + textColor + "; -fx-font-family: 'Roboto Medium'");

        addColumns(tableView);

        Button cadastrarButton = createButton("Cadastrar", event -> handleCadastro(tableView));
        Button relatorioButton = createButton("Gerar Ordem", event -> generateReport(tableView, imovelId));
        Button selectAllButton = createButton("Selecionar Todos", event -> selectAll(tableView));
        Button gerarRelatorioButton = createButton("Gerar Relatório", event -> openRelatorioPage());
        Button visualizarDetalhesButton = createButton("Visualizar Detalhes", event -> {
            // Chame o método para abrir a nova página
            openDetalhesDespesasPage();
        });


        validadePicker = new DatePicker();
        validadePicker.setPromptText("Validade");
        validadePicker.setStyle(fieldStyle);
        dataPicker = new DatePicker();
        dataPicker.setPromptText("Data");
        dataPicker.setStyle(fieldStyle);

        VBox formBox = createFormBox();

        tipoDespesaComboBox = new ComboBox<>(FXCollections.observableArrayList(
                "Compra de florestas",
                "Compra de mudas",
                "Compra de sementes",
                "Compra de pesagens",
                "Serviço de abertura e manutenção de estradas",
                "Serviços de corte",
                "Serviços de plantio",
                "Serviços de coroamento",
                "Serviços de roçada",
                "Serviços de desrama",
                "Serviço de preparo de solo",
                "Aplicação de adubo",
                "Aplicação de herbicidas",
                "Aplicação de inseticidas",
                "Serviço de georreferenciamento"
        ));

        tipoDespesaComboBox.setPromptText("Tipo de Despesa");
        tipoDespesaComboBox.setStyle(fieldStyle);

        // Adicionando o ComboBox ao formulário
        formBox.getChildren().add(tipoDespesaComboBox);

        HBox dataBox = new HBox(); // Criando um HBox para o DatePicker
        HBox dataBox2 = new HBox(); // Criando um HBox para o DatePicker
        dataBox2.getChildren().add(validadePicker);
        dataBox.getChildren().add(dataPicker); // Adicionando o DatePicker ao HBox
        formBox.getChildren().add(dataBox); // Adicionando o HBox ao formBox
        formBox.getChildren().add(dataBox2);

        HBox buttonsBox = createButtonsBox(cadastrarButton, relatorioButton, selectAllButton, visualizarDetalhesButton, gerarRelatorioButton);

        BorderPane borderPane = createBorderPane(tableView, formBox, buttonsBox);

        updateTableView(tableView);

        Scene scene = new Scene(borderPane, 1000, 600);
        scene.setFill(Color.BLACK);

        setScene(scene);
    }

    private void openDetalhesDespesasPage() {
        // Crie uma instância da classe para visualizar detalhes de despesas
        VisualizarDetalhesDespesasPage visualizarDetalhesDespesasPage = new VisualizarDetalhesDespesasPage(imovelId, connection, this, loginController);

        // Mostre a nova página
        visualizarDetalhesDespesasPage.show();
    }

    private void openRelatorioPage() {
        RelatorioPage relatorioPage = new RelatorioPage(imovelId, connection);
        relatorioPage.show();
    }

    private void addColumns(TableView<Despesa> tableView) {
        String backgroundColor = "#294B29";
        String textColor = "#DBE7C9";
        String buttonColor = "#50623A";
        String borderColor = "#789461";
        String buttonStyle = "-fx-background-color: " + buttonColor + "; -fx-text-fill: " + textColor + "; -fx-font-size: 16; -fx-font-family: 'Roboto Medium'; -fx-border-color: " + borderColor + ";";

        TableColumn<Despesa, CheckBox> selectColumn = new TableColumn<>("Selecionar");
        selectColumn.setCellValueFactory(cellData -> {
            Despesa despesa = cellData.getValue();
            CheckBox checkBox = new CheckBox();
            checkBox.selectedProperty().bindBidirectional(despesa.selectedProperty());
            return new SimpleObjectProperty<>(checkBox);
        });
        selectColumn.setSortable(false);
        selectColumn.setPrefWidth(100);

        TableColumn<Despesa, LocalDate> dataColumn = new TableColumn<>("Data");
        dataColumn.setCellValueFactory(new PropertyValueFactory<>("data"));

        TableColumn<Despesa, String> descricaoColumn = new TableColumn<>("Descrição");
        descricaoColumn.setCellValueFactory(new PropertyValueFactory<>("descricao"));

        TableColumn<Despesa, String> notaFiscalColumn = new TableColumn<>("Número Nota Fiscal");
        notaFiscalColumn.setCellValueFactory(new PropertyValueFactory<>("numeroNotaFiscal"));

        TableColumn<Despesa, String> fornecedorColumn = new TableColumn<>("Fornecedor");
        fornecedorColumn.setCellValueFactory(new PropertyValueFactory<>("fornecedor"));

        TableColumn<Despesa, String> produtoColumn = new TableColumn<>("Produto");
        produtoColumn.setCellValueFactory(new PropertyValueFactory<>("produto"));

        TableColumn<Despesa, String> unidadeColumn = new TableColumn<>("Unidade");
        unidadeColumn.setCellValueFactory(new PropertyValueFactory<>("unidade"));

        // Coluna Quantidade Editável
        TableColumn<Despesa, Integer> quantidadeColumn = new TableColumn<>("Quantidade");
        quantidadeColumn.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        quantidadeColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        quantidadeColumn.setOnEditCommit(event -> {
            Despesa despesa = event.getRowValue();
            despesa.setQuantidade(event.getNewValue());
            updateDespesaInDatabase(despesa);
            despesa.recalculateValorTotal();
        });

        // Coluna Valor Unitário Editável
        TableColumn<Despesa, Double> valorUnitarioColumn = new TableColumn<>("Valor Unitário");
        valorUnitarioColumn.setCellValueFactory(new PropertyValueFactory<>("valorUnitario"));
        valorUnitarioColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        valorUnitarioColumn.setOnEditCommit(event -> {
            Despesa despesa = event.getRowValue();
            despesa.setValorUnitario(event.getNewValue());
            updateDespesaInDatabase(despesa);
            despesa.recalculateValorTotal();
        });

        TableColumn<Despesa, Double> valorTotalColumn = new TableColumn<>("Valor Total");
        valorTotalColumn.setCellValueFactory(new PropertyValueFactory<>("valorTotal"));

        tableView.getColumns().addAll(selectColumn, dataColumn, descricaoColumn, notaFiscalColumn, fornecedorColumn,
                produtoColumn, unidadeColumn, quantidadeColumn, valorUnitarioColumn, valorTotalColumn);
        tableView.setStyle("-fx-background-color: " + backgroundColor + "; -fx-control-inner-background: " + backgroundColor + "; -fx-text-fill: " + textColor + "; -fx-font-family: 'Roboto Medium'");

        // Permitir edição de células
        tableView.setEditable(true);
    }


    private Button createButton(String text, javafx.event.EventHandler<javafx.event.ActionEvent> eventHandler) {
        String backgroundColor = "#294B29";
        String textColor = "#DBE7C9";
        String buttonColor = "#50623A";
        String borderColor = "#789461";
        String buttonStyle = "-fx-background-color: " + buttonColor + "; -fx-text-fill: " + textColor + "; -fx-font-size: 16; -fx-font-family: 'Roboto Medium'; -fx-border-color: " + borderColor + "; -fx-background-radius: 10; -fx-border-radius: 10;";
        Button button = new Button(text);
        button.setStyle(buttonStyle);
        button.setOnAction(eventHandler);
        return button;
    }

    // Defina o estilo dos campos
    private final String borderColor = "#789461";
    private final String fieldStyle = "-fx-font-family: 'Roboto'; " +
            "-fx-font-size: 14px; " +
            "-fx-pref-width: 200px; " +
            "-fx-background-color: #FFF; " +
            "-fx-text-fill: #294B29; " +
            "-fx-border-color: " + borderColor + "; " +
            "-fx-background-radius: 10; " +
            "-fx-border-radius: 10;";

    private VBox createFormBox() {
        VBox formBox = new VBox();
        formBox.setSpacing(10);
        formBox.setPadding(new Insets(10));

        Map<String, String> fieldLabels = new HashMap<>();
        fieldLabels.put("descricao", "Descrição");
        fieldLabels.put("numeroNotaFiscal", "Número Nota Fiscal");
        fieldLabels.put("fornecedor", "Fornecedor");
        fieldLabels.put("produto", "Produto");
        fieldLabels.put("unidade", "Unidade");
        fieldLabels.put("quantidade", "Quantidade");
        fieldLabels.put("valorUnitario", "Valor Unitário");

        for (Map.Entry<String, String> entry : fieldLabels.entrySet()) {
            TextField textField = new TextField();
            textField.setPromptText(entry.getValue());
            textField.setMaxWidth(200); // Esta linha pode ser removida porque o estilo -fx-pref-width já define a largura

            // Aplicando o estilo ao campo de texto
            textField.setStyle(fieldStyle);

            switch (entry.getKey()) {
                case "descricao":
                    descricaoField = textField;
                    break;
                case "numeroNotaFiscal":
                    numeroNotaFiscalField = textField;
                    break;
                case "fornecedor":
                    fornecedorField = textField;
                    break;
                case "produto":
                    produtoField = textField;
                    break;
                case "unidade":
                    unidadeField = textField;
                    break;
                case "quantidade":
                    quantidadeField = textField;
                    break;
                case "valorUnitario":
                    valorUnitarioField = textField;
                    break;
            }

            formBox.getChildren().add(textField);
        }
        return formBox;
    }

    private HBox createButtonsBox(Button cadastrarButton, Button relatorioButton, Button selectAllButton, Button visulizarDetalhes, Button gerarRelatorio) {
        String backgroundColor = "#294B29";
        HBox buttonsBox = new HBox(cadastrarButton, relatorioButton, selectAllButton, visulizarDetalhes, gerarRelatorio);
        buttonsBox.setStyle("-fx-background-color:" + backgroundColor);
        buttonsBox.setPadding(new Insets(10));
        buttonsBox.setSpacing(10);
        return buttonsBox;
    }

    private BorderPane createBorderPane(TableView<Despesa> tableView, VBox formBox, HBox buttonsBox) {
        BorderPane borderPane = new BorderPane();
        String borderColor = "#294B29";
        borderPane.setCenter(tableView);
        borderPane.setBottom(new VBox(formBox, buttonsBox));
        borderPane.setStyle("-fx-background-color:" + borderColor);
        return borderPane;
    }

    private void updateTableView(TableView<Despesa> tableView) {
        tableView.getItems().clear();
        try {
            String query = "SELECT * FROM despesas WHERE imovel_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, imovelId);
            ResultSet resultSet = statement.executeQuery();

            int rowCount = 0;
            while (resultSet.next() && rowCount < 8) {
                int id = resultSet.getInt("id");
                LocalDate data = LocalDate.parse(resultSet.getString("data"));
                String descricao = resultSet.getString("descricao");
                String numeroNotaFiscal = resultSet.getString("numero_nota_fiscal");
                String fornecedor = resultSet.getString("fornecedor");
                String produto = resultSet.getString("produto");
                String unidade = resultSet.getString("unidade");
                int quantidade = resultSet.getInt("quantidade");
                double valorUnitario = resultSet.getDouble("valor_unitario");
                double valorTotal = quantidade * valorUnitario;
                String tipoDeDespesa = resultSet.getString("tipo_de_despesa");
                Date validade = resultSet.getDate("validade");

                Despesa despesa = new Despesa(id, imovelId, data, descricao, numeroNotaFiscal, fornecedor, produto, unidade, quantidade, valorUnitario, valorTotal, tipoDeDespesa, validade);
                tableView.getItems().add(despesa);
                rowCount++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateDespesaInDatabase(Despesa despesa) {
        try {
            String updateQuery = "UPDATE despesas SET quantidade = ?, valor_unitario = ? WHERE id = ?";
            PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
            updateStatement.setInt(1, despesa.getQuantidade());
            updateStatement.setDouble(2, despesa.getValorUnitario());
            updateStatement.setInt(3, despesa.getId());
            updateStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao atualizar despesa: " + e.getMessage());
        }
    }
    private void handleCadastro(TableView<Despesa> tableView) {
        if (areFieldsFilled()) {
            LocalDate data = dataPicker.getValue();
            String descricao = descricaoField.getText();
            String numeroNotaFiscal = numeroNotaFiscalField.getText();
            String fornecedor = fornecedorField.getText();
            String produto = produtoField.getText();
            String unidade = unidadeField.getText();
            String quantidadeText = quantidadeField.getText();
            String valorUnitarioText = valorUnitarioField.getText();
            String tipoDeDespesa = tipoDespesaComboBox.getValue();
            LocalDate validade = validadePicker.getValue();

            try {
                NumberFormat numberFormat = DecimalFormat.getInstance(new Locale("pt", "BR"));
                int quantidade = numberFormat.parse(quantidadeText).intValue();
                double valorUnitario = numberFormat.parse(valorUnitarioText).doubleValue();

                // Recuperar o codigo_cc do imóvel
                String codigoCC = getCodigoCC(imovelId);

                if (codigoCC == null) {
                    showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao recuperar o código CC do imóvel.");
                    return;
                }

                String insertQuery = "INSERT INTO despesas (imovel_id, data, descricao, numero_nota_fiscal, fornecedor, produto, unidade, quantidade, valor_unitario, tipo_de_despesa, validade,  codigo_cc) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
                insertStatement.setInt(1, imovelId);
                insertStatement.setObject(2, data);
                insertStatement.setString(3, descricao);
                insertStatement.setString(4, numeroNotaFiscal);
                insertStatement.setString(5, fornecedor);
                insertStatement.setString(6, produto);
                insertStatement.setString(7, unidade);
                insertStatement.setInt(8, quantidade);
                insertStatement.setDouble(9, valorUnitario);
                insertStatement.setString(10, tipoDeDespesa);
                insertStatement.setObject(11, validade);
                insertStatement.setString(12, codigoCC);

                insertStatement.executeUpdate();

                showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Despesa cadastrada com sucesso.");

                clearFields();
                updateTableView(tableView);
            } catch (SQLException | ParseException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao inserir despesa: " + e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Por favor, preencha todos os campos.");
        }
    }

    private String getCodigoCC(int imovelId) {
        String codigoCC = null;
        try {
            String query = "SELECT codigo_cc FROM imoveis WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, imovelId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                codigoCC = resultSet.getString("codigo_cc");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return codigoCC;
    }

    private String getTipoDespesa(int imovelId) {
        String tipoDespesa = null;
        try {
            String query = "SELECT tipo_de_despesa FROM despesas WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, imovelId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                tipoDespesa = resultSet.getString("tipo_de_despesa");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tipoDespesa;
    }


    private boolean areFieldsFilled() {
        return dataPicker.getValue() != null &&
                !descricaoField.getText().isEmpty() &&
                !numeroNotaFiscalField.getText().isEmpty() &&
                !fornecedorField.getText().isEmpty() &&
                !produtoField.getText().isEmpty() &&
                !unidadeField.getText().isEmpty() &&
                !quantidadeField.getText().isEmpty() &&
                !valorUnitarioField.getText().isEmpty() &&
                tipoDespesaComboBox.getValue() != null &&
                validadePicker.getValue() != null;
    }

    private void clearFields() {
        dataPicker.setValue(null);
        descricaoField.clear();
        numeroNotaFiscalField.clear();
        fornecedorField.clear();
        produtoField.clear();
        unidadeField.clear();
        quantidadeField.clear();
        valorUnitarioField.clear();
        tipoDespesaComboBox.setValue(null);
        validadePicker.setValue(null);
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private String getNomeImovel(int imovelId) {
        String nomeImovel = "";
        try {
            String query = "SELECT descricao FROM imoveis WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, imovelId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                nomeImovel = resultSet.getString("descricao");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nomeImovel;
    }

    private void generateReport(TableView<Despesa> tableView, int imovelId) {
        String imovelNome = getNomeImovel(imovelId);
        String codigoCC = getCodigoCC(imovelId);
        String tipoDespesa = getTipoDespesa(imovelId);

        List<Despesa> selectedDespesas = new ArrayList<>();
        for (Despesa despesa : tableView.getItems()) {
            if (despesa.isSelected()) {
                selectedDespesas.add(despesa);
            }
        }

        if (selectedDespesas.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Nenhuma despesa selecionada.");
            return;
        }

        GeradorPDFDespesa pdfGenerator = new GeradorPDFDespesa();
        pdfGenerator.generateReport(selectedDespesas, imovelNome, codigoCC, tipoDespesa);
    }

    private void selectAll(TableView<Despesa> tableView) {
        for (Despesa despesa : tableView.getItems()) {
            despesa.setSelected(true);
        }
    }

}