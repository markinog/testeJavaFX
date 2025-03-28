module org.financiai {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires itextpdf;
    requires java.desktop;

    opens org.financiai to javafx.fxml;
    exports org.financiai;
    exports org.financiai.JavaFxController;
    opens org.financiai.JavaFxController to javafx.fxml;
}