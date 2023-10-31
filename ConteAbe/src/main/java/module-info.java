module com.conteabe.conteabe {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;

    requires org.kordamp.bootstrapfx.core;

    opens com.conteabe.conteabe to javafx.fxml;
    exports com.conteabe.conteabe;
}