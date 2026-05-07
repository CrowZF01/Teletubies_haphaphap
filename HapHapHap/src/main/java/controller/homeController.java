package controller;

import database.resepDB;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import model.Resep;
import java.util.List;

public class homeController {

    @FXML
    private FlowPane resepContainer;

    public void initialize() {
        tampilkanResep();
    }

    public void tampilkanResep() {
        resepDB db = new resepDB();
        List<Resep> listResep = db.getAllResep();

        resepContainer.getChildren().clear();

        for (Resep resep : listResep) {
            try {
                // Memuat template FXML untuk tiap item
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/felix_71241153/app/haphaphap/itemResep.fxml"));
                VBox card = loader.load();

                // Mengirim data resep ke controller item tersebut
                itemResepController controller = loader.getController();
                controller.setData(resep);

                // Masukkan ke layar utama
                resepContainer.getChildren().add(card);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}