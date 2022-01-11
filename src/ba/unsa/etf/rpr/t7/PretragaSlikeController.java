package ba.unsa.etf.rpr.t7;

import at.mukprojects.giphy4j.Giphy;
import at.mukprojects.giphy4j.entity.search.SearchFeed;
import at.mukprojects.giphy4j.exception.GiphyException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;
import org.json.JSONObject;
import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class PretragaSlikeController {
    public TilePane tilePane;
    public ScrollPane scrollPane;
    public TextField fldPretraga;
    private String slikaUrl = null;
    private KorisnikController korisnikController;

    public void searchAction(ActionEvent actionEvent) {
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

    public void setKorisnikController(KorisnikController korisnikController) {
        this.korisnikController = korisnikController;
    }
}
