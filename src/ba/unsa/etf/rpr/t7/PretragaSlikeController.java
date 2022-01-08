package ba.unsa.etf.rpr.t7;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class PretragaSlikeController {
    public TextField fldPretraga;
    private String slikaUrl = null;

    public void searchAction(ActionEvent actionEvent) {
        /*new Thread(() -> {
            try {
                URL url = new URL(adresa);
                BufferedReader ulaz = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8));
                String json = "", line = null;
                while ((line = ulaz.readLine()) != null)
                    json = json + line;
                if (...) {
                    Platform.runLater(() -> { polje.setStyle("nije-ispravno"); });
                } catch( … )
            }).start();*/
    }

    public void okAction(ActionEvent actionEvent) {
        if(slikaUrl == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Nijedna slika nije izabrana");
            alert.setHeaderText("Niste izabrali sliku koju želite");
            alert.setContentText("Unesite pretragu a zatim izaberite sliku, ili kliknite na Cancel");

            alert.showAndWait();
        }
        else {

            ((Stage)fldPretraga.getScene().getWindow()).close();
        }
    }

    public void cancelAction(ActionEvent actionEvent) {
        ((Stage)fldPretraga.getScene().getWindow()).close();
    }
}
