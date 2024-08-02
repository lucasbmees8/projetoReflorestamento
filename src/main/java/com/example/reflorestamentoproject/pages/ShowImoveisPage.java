package com.example.reflorestamentoproject.pages;

import com.example.reflorestamentoproject.padrao.Imovel;
import com.example.reflorestamentoproject.pdf.GeradorPDFImoveis;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShowImoveisPage {

    public ShowImoveisPage() {
        // Construtor vazio
    }

    private LoginController loginController;
    private GeradorPDFImoveis pdf;

    public ShowImoveisPage(LoginController loginController) {
        this.loginController = loginController;
        this.pdf = new GeradorPDFImoveis();
    }

    public void show(Stage primaryStage, Connection connection) {
        primaryStage.setTitle("Show Imoveis");
        primaryStage.setWidth(1920);
        primaryStage.setHeight(1080);

        // Componentes
        TableView<Imovel> tableView = new TableView<>();


        // Adicione outras colunas de acordo com os campos do imóvel
        TableColumn<Imovel, String> descricaoColumn = new TableColumn<>("Descrição");
        descricaoColumn.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        descricaoColumn.setMinWidth(400);

        descricaoColumn.setCellFactory(column -> new TableCell<Imovel, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    // Transforme a descrição para capitalizar a primeira letra de cada palavra
                    setText(capitalize(item));
                }
            }

            // Método auxiliar para capitalizar a primeira letra de cada palavra
            private String capitalize(String text) {
                String[] words = text.split("\\s+");
                StringBuilder capitalized = new StringBuilder();
                for (String word : words) {
                    if (word.length() > 0) {
                        capitalized.append(Character.toUpperCase(word.charAt(0)))
                                .append(word.substring(1).toLowerCase())
                                .append(" ");
                    }
                }
                return capitalized.toString().trim();
            }
        });

        // Coluna para o botão de exclusão
        TableColumn<Imovel, Void> deleteColumn = new TableColumn<>("Excluir");
        deleteColumn.setCellFactory(param -> new TableCell<Imovel, Void>() {
            private final Button deleteButton = new Button("Excluir");

            {
                deleteButton.setOnAction(event -> {
                    Imovel imovel = getTableView().getItems().get(getIndex());
                    deleteImovel(imovel, connection, tableView);
                    tableView.getItems().remove(imovel);
                });
                // Define o estilo do botão
                deleteButton.setStyle("-fx-background-color: #FF0000; -fx-text-fill: white; -fx-font-size: 14;");
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                }
            }
        });

        tableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // Verifica se foi um clique duplo
                Imovel selectedImovel = tableView.getSelectionModel().getSelectedItem();
                if (selectedImovel != null) {
                    showImovelDetails(primaryStage, selectedImovel, connection);
                }
            }
        });

        TextField searchField = new TextField(); // Campo de pesquisa
        searchField.setPromptText("Pesquisar por nome de imóvel");

        // Adicione o estilo personalizado para a barra de pesquisa
        searchField.setStyle("-fx-background-color: white; -fx-text-fill: black; -fx-prompt-text-fill: gray; -fx-font-size: 14; -fx-background-radius: 15; -fx-border-radius: 15; -fx-padding: 8px; -fx-border-color: #d3d3d3; -fx-border-width: 1px;");

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterImoveis(newValue, tableView, connection);
        });

        Button backButton = new Button("Voltar");
        backButton.setOnAction(event -> loginController.redirectToMainPage(primaryStage, connection));

        // Botões de filtragem
        Button filterArrendadoButton = new Button("Arrendado");
        filterArrendadoButton.setOnAction(event -> filterByStatus("Arrendado", tableView, connection));

        Button filterPropriosButton = new Button("Próprio");
        filterPropriosButton.setOnAction(event -> filterByStatus("Próprio", tableView, connection));

        // Botão "Todos"
        Button filterTodosButton = new Button("Todos");
        filterTodosButton.setOnAction(event -> filterAllImoveis(tableView, connection));

        // Botão Gerar Relatório
        Button generateReportButton = new Button("Gerar Relatório");
        generateReportButton.setOnAction(event -> openReportPopup(primaryStage, connection));

        // Defina o esquema de cores para o modo escuro
        String backgroundColor = "#294B29";
        String textColor = "#DBE7C9";
        String buttonColor = "#50623A";
        String borderColor = "#789461";
        String buttonStyle = "-fx-background-color: " + buttonColor + "; -fx-text-fill: " + textColor + "; -fx-font-size: 16; -fx-font-family: 'Roboto Medium'; -fx-border-color: " + borderColor + "; -fx-background-radius: 15; -fx-border-radius: 15;";

        // Layout
        tableView.getColumns().addAll(descricaoColumn, deleteColumn);
        VBox vbox = new VBox(searchField, tableView, backButton, generateReportButton); // Adicionando o campo de pesquisa e o botão Gerar Relatório
        vbox.setStyle("-fx-background-color:" + backgroundColor);
        HBox filterBox = new HBox(filterArrendadoButton, filterPropriosButton, filterTodosButton);
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(filterBox);
        borderPane.setCenter(vbox);
        Scene scene = new Scene(borderPane);

        tableView.setStyle("-fx-background-color: " + backgroundColor + "; -fx-control-inner-background: " + backgroundColor + "; -fx-text-fill: " + textColor + "; -fx-font-family: 'Roboto Medium'");
        descricaoColumn.setStyle("-fx-text-fill: white; -fx-alignment: CENTER;");
        deleteColumn.setStyle("-fx-text-fill: white; -fx-alignment: CENTER;");
        backButton.setStyle(buttonStyle);
        filterBox.setStyle("-fx-padding: 10px; -fx-spacing: 10px;-fx-background-color: " + backgroundColor);
        filterArrendadoButton.setStyle(buttonStyle);
        filterPropriosButton.setStyle(buttonStyle);
        generateReportButton.setStyle(buttonStyle);
        filterTodosButton.setStyle(buttonStyle);

        primaryStage.setScene(scene);
        primaryStage.show();

        // Carregue os dados da tabela
        loadImoveis(tableView, connection);
    }

    private void loadImoveis(TableView<Imovel> tableView, Connection connection) {
        ObservableList<Imovel> imovelList = FXCollections.observableArrayList();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM imoveis");
            while (resultSet.next()) {
                // Obtenha os valores dos campos do imóvel do ResultSet
                int id = resultSet.getInt("id");
                String descricao = resultSet.getString("descricao");

                // Crie um objeto Imovel com os valores obtidos
                Imovel imovel = new Imovel();
                imovel.setId(id);
                imovel.setDescricao(descricao);

                // Adicione o Imovel à lista
                imovelList.add(imovel);
            }
            tableView.setItems(imovelList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteImovel(Imovel imovel, Connection connection, TableView<Imovel> tableView) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmação de Exclusão");
        alert.setHeaderText("Você tem certeza que deseja excluir este imóvel?");
        alert.setContentText("Essa ação não pode ser desfeita.");

        // Personalize os botões do diálogo de confirmação
        ButtonType buttonTypeSim = new ButtonType("Sim");
        ButtonType buttonTypeNao = new ButtonType("Não");

        // Adicione os botões personalizados ao diálogo
        alert.getButtonTypes().setAll(buttonTypeSim, buttonTypeNao);

        // Mostra o diálogo e espera pela resposta do usuário
        alert.showAndWait().ifPresent(buttonType -> {
            if (buttonType == buttonTypeSim) {
                // Se o usuário clicou em "Sim", exclua o imóvel
                String query = "DELETE FROM imoveis WHERE id = ?";
                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    statement.setInt(1, imovel.getId());
                    statement.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            // Recarrega os dados da tabela após a exclusão ou cancelamento
            loadImoveis(tableView, connection);
        });
    }

    private void showImovelDetails(Stage primaryStage, Imovel imovel, Connection connection) {
        primaryStage.setTitle("Detalhes do Imóvel");
        ImovelDetailsPage detailsPage = new ImovelDetailsPage(imovel.getId(), connection, primaryStage, loginController);

        // Obtém a cena atual
        Scene currentScene = primaryStage.getScene();

        // Define a cena da página de detalhes do imóvel
        Scene detailsScene = detailsPage.getScene();

        // Define a cena da janela principal como a cena de detalhes
        primaryStage.setScene(detailsScene);

        // Define um botão de voltar na página de detalhes do imóvel
        Button backButton = new Button("Voltar");
        backButton.setOnAction(event -> {
            // Define a cena da janela principal de volta para a cena original
            primaryStage.setScene(currentScene);
        });

        // Adicione o botão de voltar à cena de detalhes do imóvel
        VBox vbox = (VBox) detailsScene.getRoot();
        vbox.getChildren().add(backButton);

        primaryStage.show();
    }

    private void filterImoveis(String searchTerm, TableView<Imovel> tableView, Connection connection) {
        ObservableList<Imovel> filteredList = FXCollections.observableArrayList();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM imoveis WHERE descricao LIKE ?");
            statement.setString(1, "%" + searchTerm + "%");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String descricao = resultSet.getString("descricao");
                Imovel imovel = new Imovel();
                imovel.setId(id);
                imovel.setDescricao(descricao);
                filteredList.add(imovel);
            }
            tableView.setItems(filteredList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para filtrar todos os imóveis
    private void filterAllImoveis(TableView<Imovel> tableView, Connection connection) {
        loadImoveis(tableView, connection);
    }

    private void filterByStatus(String status, TableView<Imovel> tableView, Connection connection) {
        ObservableList<Imovel> filteredList = FXCollections.observableArrayList();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM imoveis WHERE classe = ?");
            statement.setString(1, status);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String descricao = resultSet.getString("descricao");
                Imovel imovel = new Imovel();
                imovel.setId(id);
                imovel.setDescricao(descricao);
                filteredList.add(imovel);
            }
            tableView.setItems(filteredList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void openReportPopup(Stage primaryStage, Connection connection) {
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Gerar Relatório");

        // Mapeamento dos nomes amigáveis para os nomes das colunas
        Map<String, String> columnMap = new HashMap<>();
        columnMap.put("Descrição", "descricao");
        columnMap.put("Área do Imóvel", "area_imovel");
        columnMap.put("Área de Plantio", "area_plantio");
        columnMap.put("Espécie", "especie");
        columnMap.put("Origem", "origem");
        columnMap.put("Número de Árvores Plantadas", "num_arvores_plantadas");
        columnMap.put("Número de Árvores Cortadas", "arvores_cortadas");
        columnMap.put("Número de Árvores Remanescentes", "arvores_remanescentes");
        columnMap.put("Matrícula", "matricula");
        columnMap.put("Município", "municipio");
        columnMap.put("Localidade", "localidade");
        columnMap.put("Altura de Desrama", "altura_desrama");
        columnMap.put("Data do Contrato", "data_contrato");
        columnMap.put("Vencimento do Contrato", "vencimento_contrato");
        columnMap.put("Número do CCIR", "numero_ccir");
        columnMap.put("Número do ITR", "numero_itr");
        columnMap.put("Proprietário", "proprietario");
        columnMap.put("Arrendatário", "arrendatario");
        columnMap.put("Classe", "classe");
        columnMap.put("Código Centro de Custo", "codigo_cc");

        // Checklist de informações com nomes amigáveis
        List<CheckBox> checkBoxes = new ArrayList<>();
        for (String friendlyName : columnMap.keySet()) {
            checkBoxes.add(new CheckBox(friendlyName));
        }

        // Layout dos checkboxes
        VBox commonVBox = new VBox(10);
        commonVBox.getChildren().addAll(checkBoxes);

        // Botões para selecionar o tipo de relatório
        ToggleGroup reportTypeGroup = new ToggleGroup();
        RadioButton todosButton = new RadioButton("Todos");
        todosButton.setToggleGroup(reportTypeGroup);
        todosButton.setSelected(true); // Default
        RadioButton arrendadoButton = new RadioButton("Arrendado");
        arrendadoButton.setToggleGroup(reportTypeGroup);
        RadioButton proprioButton = new RadioButton("Próprio");
        proprioButton.setToggleGroup(reportTypeGroup);

        // Layout para os botões de seleção
        HBox buttonsHBox = new HBox(10, todosButton, arrendadoButton, proprioButton);
        buttonsHBox.setAlignment(Pos.CENTER);

        // Botão para gerar o relatório
        Button generateReportButton = new Button("Gerar Relatório");
        generateReportButton.setAlignment(Pos.CENTER);

        // Layout principal
        VBox mainVBox = new VBox(20, buttonsHBox, commonVBox, generateReportButton);
        mainVBox.setPadding(new Insets(20));
        mainVBox.setAlignment(Pos.CENTER);
        mainVBox.setStyle("-fx-background-color: #294B29;");

        // Defina o esquema de cores para o modo escuro
        String textColor = "#DBE7C9";
        String buttonColor = "#50623A";
        String borderColor = "#789461";
        String buttonStyle = "-fx-background-color: " + buttonColor + "; -fx-text-fill: " + textColor + "; -fx-font-size: 16; -fx-font-family: 'Roboto Medium'; -fx-border-color: " + borderColor + "; -fx-background-radius: 15; -fx-border-radius: 15;";
        String checkBoxStyle = "-fx-text-fill: white;";

        todosButton.setStyle(buttonStyle);
        arrendadoButton.setStyle(buttonStyle);
        proprioButton.setStyle(buttonStyle);
        generateReportButton.setStyle(buttonStyle);

        for (Node node : commonVBox.getChildren()) {
            if (node instanceof CheckBox) {
                node.setStyle(checkBoxStyle);
            }
        }

        Scene scene = new Scene(mainVBox, 400, 600);
        scene.getStylesheets().add("https://fonts.googleapis.com/css?family=Roboto:400,500,700&display=swap");
        popupStage.setScene(scene);
        popupStage.show();

        // Adicionar funcionalidade para desabilitar campos arrendados quando necessário
        reportTypeGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == proprioButton) {
                disableArrendadoFields(true, commonVBox);
            } else {
                disableArrendadoFields(false, commonVBox);
            }
        });

        generateReportButton.setOnAction(event -> {
            String selectedReportType = ((RadioButton) reportTypeGroup.getSelectedToggle()).getText();

            pdf.generateReport(connection, commonVBox, columnMap, selectedReportType);
            popupStage.close();
        });
    }




    private void disableArrendadoFields(boolean disable, VBox commonVBox) {
        for (Node node : commonVBox.getChildren()) {
            if (node instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) node;
                if (checkBox.getText().equals("Data do Contrato") || checkBox.getText().equals("Vencimento do Contrato") || checkBox.getText().equals("Proprietário") || checkBox.getText().equals("Arrendatário")) {
                    checkBox.setDisable(disable);
                } else {
                    checkBox.setDisable(false);
                }
            }
        }
    }
}

