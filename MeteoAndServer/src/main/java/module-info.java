module org.Server {
    requires javafx.controls;
    requires javafx.fxml;

    opens org.Server to javafx.fxml;
    exports org.Server;
}
