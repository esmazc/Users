package ba.unsa.etf.rpr.t7;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import javafx.scene.control.TextArea;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ResourceBundle;

public class AboutController {
    public Button btnCancel;
    public TextArea oAutoru;

    @FXML
    public void initialize() {
        ResourceBundle bundle = ResourceBundle.getBundle("Translation");
        oAutoru.setText(bundle.getString("text"));
    }

    public void cancelAction(ActionEvent actionEvent) {
        ((Stage)btnCancel.getScene().getWindow()).close();
    }

    public void linkAction(ActionEvent actionEvent) throws URISyntaxException, IOException {
        Desktop.getDesktop().browse(new URI("https://github.com/RPR-2019/rpr21-zadaca4-esmazc"));
    }
}
