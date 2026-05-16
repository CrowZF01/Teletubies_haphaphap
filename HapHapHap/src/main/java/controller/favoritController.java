package controller;

import database.resepDB;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Resep;
import util.sessionManager;

import java.util.List;

public class favoritController {

    @FXML private VBox favoritContainer;

    @FXML
    public void initialize() {
        loadDataFavorit();
    }

    public void loadDataFavorit() {
        favoritContainer.getChildren().clear();

        if (!sessionManager.isLogin()) return;

        resepDB db = new resepDB();
        int idUser = sessionManager.getUser().getId();
        List<Resep> listFavorit = db.getFavoritByUser(idUser);

        if (listFavorit.isEmpty()) {
            Label kosong = new Label("Belum ada resep yang difavoritkan.");
            kosong.setStyle("-fx-text-fill: #888888; -fx-font-size: 15px;");
            favoritContainer.getChildren().add(kosong);
            return;
        }

        for (Resep resep : listFavorit) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/felix_71241153/app/haphaphap/itemFavorit.fxml"));
                HBox card = loader.load();
                itemFavoritController controller = loader.getController();

                // Kirim data resep dan "this" (controller ini) agar bisa refresh saat dihapus
                controller.setData(resep, this);

                favoritContainer.getChildren().add(card);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void pindahHome(javafx.scene.input.MouseEvent event) {
        try {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/felix_71241153/app/haphaphap/home.fxml"));
            Parent root = loader.load();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}