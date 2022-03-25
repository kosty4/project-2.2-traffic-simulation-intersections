module com.trafficsimulation {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires java.xml;
    requires java.desktop;

    opens com.simulation to javafx.fxml;
    exports com.simulation;
    exports com.backend;
    opens com.backend to javafx.fxml;
}