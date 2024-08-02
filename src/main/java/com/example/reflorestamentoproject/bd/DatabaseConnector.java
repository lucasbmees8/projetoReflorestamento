package com.example.reflorestamentoproject.bd;

import java.sql.*;

public class DatabaseConnector {

    private static final String URL = "jdbc:mysql://localhost:3306/rioverde";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "PUC@1234";
    private Connection connection;

    public Connection connectToDatabase() throws SQLException {
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            createTables();
        } catch (SQLException e) {
            throw new SQLException("Failed to connect to the database.", e);
        }
        return connection;
    }

    private void createTables() throws SQLException {
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement userStatement = conn.prepareStatement(
                     "CREATE TABLE IF NOT EXISTS users (id INT AUTO_INCREMENT PRIMARY KEY, username VARCHAR(255) NOT NULL UNIQUE, password VARCHAR(255) NOT NULL)"
             );
             PreparedStatement imoveisStatement = conn.prepareStatement(
                     "CREATE TABLE IF NOT EXISTS imoveis (" +
                             "id INT AUTO_INCREMENT PRIMARY KEY, " +
                             "descricao VARCHAR(255) NOT NULL, " +
                             "area_imovel DOUBLE NOT NULL, " +
                             "area_plantio DOUBLE NOT NULL, " +
                             "especie VARCHAR(255) NOT NULL, " +
                             "origem VARCHAR(255) NOT NULL, " +
                             "num_arvores_plantadas INT NOT NULL, " +
                             "data_plantio DATE NOT NULL, " +
                             "numero_ccir VARCHAR(255) NOT NULL, " +
                             "numero_itr VARCHAR(255) NOT NULL, " +
                             "proprietario VARCHAR(255) NOT NULL, " +
                             "matricula VARCHAR(255) NOT NULL, " +
                             "municipio VARCHAR(255) NOT NULL, " +
                             "localidade VARCHAR(255) NOT NULL, " +
                             "classe ENUM('proprio', 'arrendado') NOT NULL, " +
                             "arvores_remanescentes INT NOT NULL DEFAULT 0, " +
                             "arvores_cortadas INT NOT NULL DEFAULT 0, " +
                             "altura_desrama DOUBLE NOT NULL DEFAULT 0, " +
                             "num_arvores_por_hectare INT NOT NULL DEFAULT 0," +
                             "codigo_cc VARCHAR(255) NOT NULL" + // Adicionando o campo "codigo_cc"
                             ")"
             );
             PreparedStatement imoveisArrendadosDetalhesStatement = conn.prepareStatement(
                     "CREATE TABLE IF NOT EXISTS imoveis_arrendados (" +
                             "id INT AUTO_INCREMENT PRIMARY KEY, " +
                             "imovel_id INT, " +
                             "data_contrato DATE NOT NULL, " +
                             "vencimento_contrato DATE NOT NULL, " +
                             "arrendatario VARCHAR(255) NOT NULL, " +
                             "FOREIGN KEY (imovel_id) REFERENCES imoveis(id) ON DELETE CASCADE" +
                             ")"
             );
             PreparedStatement imagensStatement = conn.prepareStatement(
                     "CREATE TABLE IF NOT EXISTS imagens_imovel (id INT AUTO_INCREMENT PRIMARY KEY, imovel_id INT, imagem LONGBLOB, FOREIGN KEY (imovel_id) REFERENCES imoveis(id) ON DELETE CASCADE)"
             );
             PreparedStatement desbasteStatement = conn.prepareStatement(
                     "CREATE TABLE IF NOT EXISTS desbaste (" +
                             "id INT AUTO_INCREMENT PRIMARY KEY, " +
                             "imovel_id INT, " +
                             "numero INT NOT NULL, " +
                             "data DATE NOT NULL, " +
                             "arvores_cortadas INT NOT NULL, " +
                             "lenha DOUBLE NOT NULL, " +
                             "toretes DOUBLE NOT NULL, " +
                             "toras_20a25 DOUBLE NOT NULL, " +
                             "toras_25a33 DOUBLE NOT NULL, " +
                             "toras_acima_33 DOUBLE NOT NULL, " +
                             "total_lenha DOUBLE NOT NULL, " +
                             "total_toretes DOUBLE NOT NULL, " +
                             "total_toras_20a25 DOUBLE NOT NULL, " +
                             "total_toras_25a33 DOUBLE NOT NULL, " +
                             "total_toras_acima_33 DOUBLE NOT NULL, " +
                             "total DOUBLE NOT NULL, " +
                             "preco_lenha DOUBLE NOT NULL, " +
                             "preco_toretes DOUBLE NOT NULL, " +
                             "preco_toras_20a25 DOUBLE NOT NULL, " +
                             "preco_toras_25a33 DOUBLE NOT NULL, " +
                             "preco_toras_acima_33 DOUBLE NOT NULL, " +
                             "previsao BOOLEAN NOT NULL DEFAULT FALSE," +
                             "FOREIGN KEY (imovel_id) REFERENCES imoveis(id) ON DELETE CASCADE)"
             );

             PreparedStatement previsaoDesbasteStatement = conn.prepareStatement(
                     "CREATE TABLE IF NOT EXISTS previsao_desbaste (" +
                             "id INT AUTO_INCREMENT PRIMARY KEY, " +
                             "desbaste_id INT NOT NULL, " +
                             "data_prevista DATE NOT NULL, " +
                             "status VARCHAR(255) NOT NULL," +
                             "FOREIGN KEY (desbaste_id) REFERENCES desbaste(id) ON DELETE CASCADE)"
             );

             PreparedStatement despesasStatement = conn.prepareStatement(
                     "CREATE TABLE IF NOT EXISTS despesas (" +
                             "id INT AUTO_INCREMENT PRIMARY KEY, " +
                             "imovel_id INT, " +
                             "codigo_cc VARCHAR(255), " +  // Adicionado novo campo
                             "data DATE NOT NULL, " +
                             "descricao VARCHAR(255) NOT NULL, " +
                             "numero_nota_fiscal VARCHAR(255) NOT NULL, " +
                             "fornecedor VARCHAR(255), " +
                             "produto VARCHAR(255), " +
                             "unidade VARCHAR(255), " +
                             "quantidade INT, " +
                             "valor_unitario DOUBLE, " +
                             "total DOUBLE, " +
                             "tipo_de_despesa VARCHAR(255), " +
                             "validade DATE NOT NULL, " +
                             "FOREIGN KEY (imovel_id) REFERENCES imoveis(id) ON DELETE CASCADE, " +
                             "FOREIGN KEY (codigo_cc) REFERENCES imoveis(codigo_cc) ON DELETE CASCADE)"  // Adicionada chave estrangeira
             );
             PreparedStatement kmlStatement = conn.prepareStatement(
                     "CREATE TABLE IF NOT EXISTS kml_imovel (id INT AUTO_INCREMENT PRIMARY KEY, imovel_id INT, kml_path VARCHAR(255) NOT NULL, FOREIGN KEY (imovel_id) REFERENCES imoveis(id) ON DELETE CASCADE)"
             );
             PreparedStatement inventarioStatement = conn.prepareStatement(
                     "CREATE TABLE IF NOT EXISTS inventario (id INT AUTO_INCREMENT PRIMARY KEY, imovel_id INT, cap DOUBLE NOT NULL, altura DOUBLE NOT NULL, dap DOUBLE NOT NULL, FOREIGN KEY (imovel_id) REFERENCES imoveis(id) ON DELETE CASCADE)"
             );
             PreparedStatement notasStatement = conn.prepareStatement(
                     "CREATE TABLE IF NOT EXISTS notas (" +
                             "id INT AUTO_INCREMENT PRIMARY KEY, " +
                             "titulo VARCHAR(255) NOT NULL, " +
                             "descricao TEXT, " +
                             "imovel_id INT NOT NULL, " +
                             "FOREIGN KEY (imovel_id) REFERENCES imoveis(id) ON DELETE CASCADE)"
             );
             PreparedStatement desramaStatement = conn.prepareStatement(
                     "CREATE TABLE IF NOT EXISTS desrama (id INT AUTO_INCREMENT PRIMARY KEY, imovel_id INT, numero INT NOT NULL, data_prevista DATE NOT NULL, data_realizacao DATE, altura DOUBLE, status VARCHAR(255) NOT NULL, FOREIGN KEY (imovel_id) REFERENCES imoveis(id) ON DELETE CASCADE)"
             )) {
            userStatement.executeUpdate();
            imoveisStatement.executeUpdate();
            imagensStatement.executeUpdate();
            desbasteStatement.executeUpdate();
            previsaoDesbasteStatement.executeUpdate();
            despesasStatement.executeUpdate();
            kmlStatement.executeUpdate();
            inventarioStatement.executeUpdate();
            desramaStatement.executeUpdate();
            imoveisArrendadosDetalhesStatement.executeUpdate();
            notasStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Failed to create tables.", e);
        }
    }

    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
