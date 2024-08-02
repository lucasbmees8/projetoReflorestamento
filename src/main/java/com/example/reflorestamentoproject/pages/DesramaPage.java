package com.example.reflorestamentoproject.pages;

import com.example.reflorestamentoproject.padrao.Desrama;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import javafx.util.converter.DateStringConverter;
import javafx.util.converter.DoubleStringConverter;

import java.sql.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class DesramaPage extends Stage {

    private final Stage previousStage;
    private final LoginController loginController;
    private final ImovelDetailsPage imovelPage; // Referência à página de detalhes do imóvel
    private TableView<Desrama> tableView;
    private final ObservableList<Desrama> desramaList = FXCollections.observableArrayList();

    public DesramaPage(int imovelId, Connection connection, Stage previousStage, LoginController loginController, ImovelDetailsPage imovelPage) {
        this.previousStage = previousStage;
        this.loginController = loginController;
        this.imovelPage = imovelPage; // Inicializa a referência
        setTitle("Desrama");

        // Definindo a paleta de cores
        String backgroundColor = "#294B29";
        String textColor = "#DBE7C9";
        String buttonColor = "#50623A";
        String borderColor = "#789461";

        tableView = new TableView<>();
        tableView.setEditable(true); // Torna a tabela editável
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.setStyle("-fx-background-color: " + backgroundColor + "; -fx-control-inner-background: " + backgroundColor + "; -fx-text-fill: " + textColor + "; -fx-font-family: 'Roboto Medium'");

        TableColumn<Desrama, Integer> numeroColumn = new TableColumn<>("Número");
        numeroColumn.setCellValueFactory(cellData -> cellData.getValue().numeroProperty().asObject());

        TableColumn<Desrama, Date> dataPrevistaColumn = new TableColumn<>("Data Prevista");
        dataPrevistaColumn.setCellValueFactory(cellData -> cellData.getValue().dataPrevistaProperty());

        TableColumn<Desrama, Date> dataRealizacaoColumn = new TableColumn<>("Data Realização");
        dataRealizacaoColumn.setCellValueFactory(cellData -> cellData.getValue().dataRealizacaoProperty());

        TableColumn<Desrama, Double> alturaColumn = new TableColumn<>("Altura");
        alturaColumn.setCellValueFactory(cellData -> cellData.getValue().alturaProperty().asObject());

        TableColumn<Desrama, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(cellData -> cellData.getValue().statusProperty());

        // Adicionando a lógica de cores para a coluna de status
        statusColumn.setCellFactory(new Callback<TableColumn<Desrama, String>, TableCell<Desrama, String>>() {
            @Override
            public TableCell<Desrama, String> call(TableColumn<Desrama, String> column) {
                return new TableCell<Desrama, String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setText(null);
                            setStyle("");
                        } else {
                            setText(item);
                            if ("Realizada".equals(item)) {
                                setStyle("-fx-background-color: green; -fx-text-fill: white;");
                            } else {
                                setStyle("-fx-background-color: red; -fx-text-fill: white;");
                            }
                        }
                    }
                };
            }
        });

        // Torna as colunas editáveis
        dataPrevistaColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DateStringConverter()));
        dataRealizacaoColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DateStringConverter()));
        alturaColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        statusColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        dataPrevistaColumn.setOnEditCommit(event -> updateDesramaField(event.getRowValue(), "data_prevista", event.getNewValue(), connection));
        dataRealizacaoColumn.setOnEditCommit(event -> updateDesramaField(event.getRowValue(), "data_realizacao", event.getNewValue(), connection));
        alturaColumn.setOnEditCommit(event -> updateDesramaField(event.getRowValue(), "altura", event.getNewValue(), connection));
        statusColumn.setOnEditCommit(event -> updateDesramaField(event.getRowValue(), "status", event.getNewValue(), connection));

        tableView.getColumns().addAll(numeroColumn, dataPrevistaColumn, dataRealizacaoColumn, alturaColumn, statusColumn);

        loadDesramaData(imovelId, connection);

        String buttonStyle = "-fx-background-color: " + buttonColor + "; -fx-text-fill: " + textColor + "; -fx-font-size: 16; -fx-font-family: 'Roboto Medium'; -fx-border-color: " + borderColor + "; -fx-background-radius: 15; -fx-border-radius: 15;";

        Button addDesramaButton = new Button("Adicionar Previsão de Desrama");
        addDesramaButton.setStyle(buttonStyle);
        addDesramaButton.setOnAction(event -> {
            showAddDesramaDialog(imovelId, connection);
        });

        Button updateDesramaButton = new Button("Realizar Cadastro de Desrama");
        updateDesramaButton.setStyle(buttonStyle);
        updateDesramaButton.setOnAction(event -> {
            Desrama selectedDesrama = tableView.getSelectionModel().getSelectedItem();
            if (selectedDesrama != null) {
                showUpdateDesramaDialog(selectedDesrama, connection);
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Nenhuma desrama selecionada");
                alert.setHeaderText(null);
                alert.setContentText("Por favor, selecione uma desrama para realizar o cadastro.");
                alert.showAndWait();
            }
        });

        Button deleteDesramaButton = new Button("Excluir Desrama");
        deleteDesramaButton.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-font-size: 16; -fx-font-family: 'Roboto Medium'; -fx-border-color: " + borderColor + "; -fx-background-radius: 15; -fx-border-radius: 15;");
        deleteDesramaButton.setOnAction(event -> {
            Desrama selectedDesrama = tableView.getSelectionModel().getSelectedItem();
            if (selectedDesrama != null) {
                deleteDesrama(selectedDesrama, connection);
                // Recarregar os dados da tabela após a exclusão
                loadDesramaData(imovelId, connection);
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Nenhuma desrama selecionada");
                alert.setHeaderText(null);
                alert.setContentText("Por favor, selecione uma desrama para excluir.");
                alert.showAndWait();
            }
        });

        Button backButton = new Button("Voltar");
        backButton.setStyle(buttonStyle);
        backButton.setOnAction(event -> {
            close();
            previousStage.show();
        });

        VBox vbox = new VBox();
        vbox.getChildren().addAll(tableView);
        vbox.setStyle("-fx-background-color: " + backgroundColor + ";");

        // Remoção do botão "Voltar"
        HBox bottomBox = new HBox(10, addDesramaButton, updateDesramaButton, deleteDesramaButton);
        bottomBox.setStyle("-fx-background-color: " + backgroundColor + ";");
        bottomBox.setPadding(new Insets(10));

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(vbox);
        borderPane.setBottom(bottomBox);
        borderPane.setStyle("-fx-background-color: " + backgroundColor + ";");

        // Criando a cena (Scene)
        Scene scene = new Scene(borderPane, 800, 600);
        scene.setFill(Color.web(backgroundColor));

        // Definindo a cena (Scene) no palco (Stage)
        setScene(scene);

        // Adicionando um ouvinte ao evento de exibição do palco para verificar a última semana
        setOnShown(event -> {
            checkLastWeek();
        });
    }

    private void loadDesramaData(int imovelId, Connection connection) {
        desramaList.clear();
        String query = "SELECT * FROM desrama WHERE imovel_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, imovelId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int numero = resultSet.getInt("numero");
                Date dataPrevista = resultSet.getDate("data_prevista");
                Date dataRealizacao = resultSet.getDate("data_realizacao");
                Double altura = resultSet.getDouble("altura");
                String status = resultSet.getString("status");
                desramaList.add(new Desrama(id, imovelId, numero, dataPrevista, dataRealizacao, altura, status));
            }
            tableView.setItems(desramaList);
            atualizarAlturaImovel(imovelId, connection); // Atualiza a altura do imóvel com a soma das alturas realizadas
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showAddDesramaDialog(int imovelId, Connection connection) {
        Stage dialog = new Stage();
        dialog.setTitle("Adicionar Previsão de Desrama");

        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10));
        vbox.setSpacing(10);

        DatePicker dataPrevistaPicker = new DatePicker();
        Label dataPrevistaLabel = new Label("Data Prevista:");
        dataPrevistaLabel.setLabelFor(dataPrevistaPicker);

        Button addButton = new Button("Adicionar");
        addButton.setOnAction(event -> {
            LocalDate dataPrevista = dataPrevistaPicker.getValue();
            if (dataPrevista != null) {
                addDesrama(imovelId, Date.from(dataPrevista.atStartOfDay(ZoneId.systemDefault()).toInstant()), connection);
                dialog.close();
            }
        });

        vbox.getChildren().addAll(dataPrevistaLabel, dataPrevistaPicker, addButton);

        Scene scene = new Scene(vbox);
        dialog.setScene(scene);
        dialog.show();
    }

    private void addDesrama(int imovelId, Date dataPrevista, Connection connection) {
        String query = "INSERT INTO desrama (imovel_id, numero, data_prevista, status) VALUES (?, ?, ?, 'Pendente')";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, imovelId);
            statement.setInt(2, getNextDesramaNumero(imovelId, connection));
            statement.setDate(3, new java.sql.Date(dataPrevista.getTime()));
            statement.executeUpdate();
            loadDesramaData(imovelId, connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int getNextDesramaNumero(int imovelId, Connection connection) {
        String query = "SELECT MAX(numero) AS max_numero FROM desrama WHERE imovel_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, imovelId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("max_numero") + 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 1;
    }

    private void showUpdateDesramaDialog(Desrama desrama, Connection connection) {
        Stage dialog = new Stage();
        dialog.setTitle("Realizar Cadastro de Desrama");

        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10));
        vbox.setSpacing(10);

        DatePicker dataRealizacaoPicker = new DatePicker();
        Label dataRealizacaoLabel = new Label("Data de Realização:");
        dataRealizacaoLabel.setLabelFor(dataRealizacaoPicker);

        TextField alturaField = new TextField();
        Label alturaLabel = new Label("Altura:");
        alturaLabel.setLabelFor(alturaField);

        Button updateButton = new Button("Atualizar");
        updateButton.setOnAction(event -> {
            LocalDate dataRealizacao = dataRealizacaoPicker.getValue();
            String alturaText = alturaField.getText();
            if (dataRealizacao != null && alturaText != null && !alturaText.isEmpty()) {
                try {
                    double altura = Double.parseDouble(alturaText);
                    updateDesrama(desrama.getId(), Date.from(dataRealizacao.atStartOfDay(ZoneId.systemDefault()).toInstant()), altura, connection, desrama);
                    dialog.close();
                } catch (NumberFormatException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erro de Formato");
                    alert.setHeaderText(null);
                    alert.setContentText("Por favor, insira um valor válido para a altura.");
                    alert.showAndWait();
                }
            }
        });

        vbox.getChildren().addAll(dataRealizacaoLabel, dataRealizacaoPicker, alturaLabel, alturaField, updateButton);

        Scene scene = new Scene(vbox);
        dialog.setScene(scene);
        dialog.show();
    }

    private void updateDesrama(int id, Date dataRealizacao, double altura, Connection connection, Desrama desrama) {
        String query = "UPDATE desrama SET data_realizacao = ?, altura = ?, status = 'Realizada' WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setDate(1, new java.sql.Date(dataRealizacao.getTime()));
            statement.setDouble(2, altura);
            statement.setInt(3, id);
            statement.executeUpdate();
            loadDesramaData(desrama.getImovelId(), connection); // Atualiza os dados de desrama após a exclusão
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateDesramaField(Desrama desrama, String field, Object newValue, Connection connection) {
        String query = "UPDATE desrama SET " + field + " = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            if (newValue instanceof Date) {
                statement.setDate(1, new java.sql.Date(((Date) newValue).getTime()));
            } else if (newValue instanceof Double) {
                statement.setDouble(1, (Double) newValue);
            } else if (newValue instanceof String) {
                statement.setString(1, (String) newValue);
            }
            statement.setInt(2, desrama.getId());
            statement.executeUpdate();
            loadDesramaData(desrama.getImovelId(), connection); // Atualiza os dados de desrama após a alteração
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteDesrama(Desrama desrama, Connection connection) {
        String query = "DELETE FROM desrama WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, desrama.getId());
            statement.executeUpdate();
            loadDesramaData(desrama.getImovelId(), connection); // Atualiza os dados de desrama após a exclusão
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void atualizarAlturaImovel(int imovelId, Connection connection) {
        String query = "SELECT SUM(altura) AS total_altura FROM desrama WHERE imovel_id = ? AND status = 'Realizada'";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, imovelId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                double totalAltura = resultSet.getDouble("total_altura");
                String updateQuery = "UPDATE imoveis SET altura_desrama = ? WHERE id = ?";
                try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                    updateStatement.setDouble(1, totalAltura);
                    updateStatement.setInt(2, imovelId);
                    updateStatement.executeUpdate();
                    // Atualizar a exibição na página de detalhes do imóvel
                    imovelPage.atualizarDetalhes(imovelId, connection);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void checkLastWeek() {
        LocalDate today = LocalDate.now();
        LocalDate oneWeekAhead = today.plusWeeks(1);
        LocalDate lastWeekStart = oneWeekAhead.minusWeeks(1);
        LocalDate lastWeekEnd = oneWeekAhead.minusDays(1);

        // Verifica se hoje está dentro da última semana
        if (today.isEqual(lastWeekStart) || (today.isAfter(lastWeekStart) && today.isBefore(lastWeekEnd))) {
            // Verifica se existe alguma desrama pendente
            boolean desramasPendentes = desramaList.stream().anyMatch(desrama -> "Pendente".equals(desrama.getStatus()));
            if (desramasPendentes) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Última Semana");
                alert.setHeaderText(null);
                alert.setContentText("Falta pouco para o cadastro da desrama!");
                alert.showAndWait();
            }
        }
    }
}
