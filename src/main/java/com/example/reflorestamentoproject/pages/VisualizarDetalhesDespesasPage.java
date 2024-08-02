package com.example.reflorestamentoproject.pages;

import com.example.reflorestamentoproject.padrao.DetalhesDespesa;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class VisualizarDetalhesDespesasPage extends Stage {

    private final int imovelId;
    private final Connection connection;
    private Stage previousStage;
    private LoginController loginController;

    public VisualizarDetalhesDespesasPage(int imovelId, Connection connection, Stage previousStage, LoginController loginController) {
        this.imovelId = imovelId;
        this.connection = connection;
        this.previousStage = previousStage;
        this.loginController = loginController;

        // Carrega a fonte Roboto
        Font.loadFont(getClass().getResourceAsStream("/fonts/Roboto-Medium.ttf"), 16);
        String backgroundColor = "#294B29";
        String textColor = "#DBE7C9";
        String buttonColor = "#50623A";
        String borderColor = "#789461";
        String buttonStyle = "-fx-background-color: " + buttonColor + "; -fx-text-fill: " + textColor + "; -fx-font-size: 16; -fx-font-family: 'Roboto Medium'; -fx-border-color: " + borderColor + ";";


        setTitle("Detalhes das Despesas");

        // Criando a tabela e suas colunas
        TableView<DetalhesDespesa> tableView = new TableView<>();
        TableColumn<DetalhesDespesa, String> produtoColumn = new TableColumn<>("Produto");
        produtoColumn.setCellValueFactory(new PropertyValueFactory<>("produto"));
        TableColumn<DetalhesDespesa, Integer> quantidadeColumn = new TableColumn<>("Quantidade");
        quantidadeColumn.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        TableColumn<DetalhesDespesa, String> unidadeColumn = new TableColumn<>("Unidade");
        unidadeColumn.setCellValueFactory(new PropertyValueFactory<>("unidade"));
        TableColumn<DetalhesDespesa, String> valorUnitarioColumn = new TableColumn<>("Valor Unitário (R$)");
        valorUnitarioColumn.setCellValueFactory(new PropertyValueFactory<>("valorUnitarioFormatado"));
        TableColumn<DetalhesDespesa, String> valorTotalColumn = new TableColumn<>("Valor Total (R$)");
        valorTotalColumn.setCellValueFactory(new PropertyValueFactory<>("valorTotalFormatado"));
        TableColumn<DetalhesDespesa, String> percentualColumn = new TableColumn<>("Percentual (%)");
        percentualColumn.setCellValueFactory(new PropertyValueFactory<>("percentualFormatado"));

        // Adicionando as colunas à tabela
        tableView.getColumns().addAll(produtoColumn, quantidadeColumn, unidadeColumn, valorUnitarioColumn, valorTotalColumn, percentualColumn);
        tableView.setStyle("-fx-background-color: " + backgroundColor + "; -fx-control-inner-background: " + backgroundColor + "; -fx-text-fill: " + textColor + "; -fx-font-family: 'Roboto Medium'");

        // Preenchendo a tabela com os dados do banco de dados
        ObservableList<DetalhesDespesa> despesas = FXCollections.observableArrayList();
        double totalGeral = 0;

        // Usando um HashMap para rastrear os totais para cada produto
        Map<String, DetalhesDespesa> mapProdutos = new HashMap<>();
        try {
            String query = "SELECT produto, quantidade, unidade, valor_unitario FROM despesas WHERE imovel_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, imovelId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String produto = resultSet.getString("produto").split(" ")[0].toLowerCase();
                int quantidade = resultSet.getInt("quantidade");
                String unidade = resultSet.getString("unidade");
                double valorUnitario = resultSet.getDouble("valor_unitario");
                double valorTotal = quantidade * valorUnitario;

                totalGeral += valorTotal;

                // Verificando se o produto já está no mapa
                if (mapProdutos.containsKey(produto)) {
                    // Se estiver, atualiza a quantidade e o valor total
                    DetalhesDespesa existingDespesa = mapProdutos.get(produto);
                    existingDespesa.setQuantidade(existingDespesa.getQuantidade() + quantidade);
                    existingDespesa.setValorTotal(existingDespesa.getValorTotal() + valorTotal);
                    existingDespesa.setValorUnitario(existingDespesa.getValorUnitario() + valorUnitario);
                } else {
                    // Se não estiver, adiciona um novo registro ao mapa
                    DetalhesDespesa newDespesa = new DetalhesDespesa(produto, quantidade, unidade, valorUnitario, valorTotal);
                    mapProdutos.put(produto, newDespesa);
                }
            }

            // Calculando o percentual para cada registro
            for (DetalhesDespesa despesa : mapProdutos.values()) {
                double percentual = (despesa.getValorTotal() / totalGeral) * 100;
                despesa.setPercentual(percentual);
            }

            // Adicionando os valores do mapa à lista de despesas
            despesas.addAll(mapProdutos.values());

        } catch (SQLException e) {
            e.printStackTrace();
        }

        tableView.setItems(despesas);

        // Criando um rótulo para exibir o total geral formatado
        NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        Label totalGeralLabel = new Label("Total Geral: " + nf.format(totalGeral));
        totalGeralLabel.setStyle("-fx-font-family: 'Roboto'; -fx-font-size: 20px; -fx-text-fill: white;");

        VBox root = new VBox(tableView, totalGeralLabel);
        root.setStyle("-fx-background-color:" + borderColor);
        Scene scene = new Scene(root, 800, 600);
        setScene(scene);
    }


}
