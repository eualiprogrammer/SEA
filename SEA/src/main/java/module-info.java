module com.example.sea {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;

    requires kernel;
    requires io;
    requires layout;
    requires org.slf4j;

    // Permissões de Acesso
    opens com.example.sea.model to javafx.base;
    opens com.example.sea.gui to javafx.fxml;
    opens com.example.sea to javafx.fxml;

    exports com.example.sea;
}
