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
        tilePane.getChildren().clear();
        new Thread(() -> {
            try {
                Giphy giphy = new Giphy("228D8OGOH1PFC685HuBJWnmYartPPvsa");
                SearchFeed feed = giphy.search(fldPretraga.getText(), 25, 0);
                for (int i = 0; i < 25; i++) {
                    ImageView imageView = new ImageView("/img/loading.gif");
                    imageView.setFitHeight(128);
                    imageView.setFitWidth(128);
                    Button button = new Button();
                    button.setGraphic(imageView);
                    Platform.runLater(() -> {
                        tilePane.getChildren().add(button);
                        scrollPane.setContent(tilePane);
                    });
                    String url = feed.getDataList().get(i).getImages().getOriginalStill().getUrl();
                    imageView.setImage(new Image("https://i" + url.substring(14, url.indexOf("?"))));
                    button.setOnMouseClicked(me -> {
                        slikaUrl = imageView.getImage().getUrl();
                    });
                }
                /*URL url = new URL("https://api.giphy.com/v1/gifs/search?api_key=228D8OGOH1PFC685HuBJWnmYartPPvsa&q=" + fldPretraga.getText() + "&limit=25");
                BufferedReader ulaz = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8));
                String json = "", line = null;
                while ((line = ulaz.readLine()) != null)
                    json = json + line;
                JSONObject jsonObject = new JSONObject(json);
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                for(int i = 0; i < jsonArray.length(); i++) {
                    JSONObject gif = jsonArray.getJSONObject(i); //25
                    ImageView imageView = new ImageView("/img/loading.gif");
                    imageView.setFitHeight(128);
                    imageView.setFitWidth(128);
                    Button button = new Button();
                    button.setGraphic(imageView);
                    Platform.runLater(() -> {
                        tilePane.getChildren().add(button);
                        scrollPane.setContent(tilePane);
                    });
                    JSONObject image = new JSONObject(gif.getJSONObject("images").get("original_still").toString());
                    imageView.setImage(new Image("https://i" + image.get("url").toString().substring(14, image.get("url").toString().indexOf("?"))));
                    button.setOnMouseClicked(me -> {
                        slikaUrl = imageView.getImage().getUrl();
                    });
                }*/
            } catch (GiphyException e) {
                e.printStackTrace();
            } /*catch(IOException e) {
                e.printStackTrace();
            }*/
        }).start();
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
