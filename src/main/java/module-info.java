module com.example.reflorestamentoproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires itextpdf;
    requires java.desktop;
    requires mysql.connector.java;
    requires layout;
    requires kernel;


    opens com.example.reflorestamentoproject to javafx.fxml;
    exports com.example.reflorestamentoproject;
    exports com.example.reflorestamentoproject.padrao;
    opens com.example.reflorestamentoproject.padrao to javafx.fxml;
    exports com.example.reflorestamentoproject.bd;
    opens com.example.reflorestamentoproject.bd to javafx.fxml;
    exports com.example.reflorestamentoproject.pages;
    opens com.example.reflorestamentoproject.pages to javafx.fxml;
}