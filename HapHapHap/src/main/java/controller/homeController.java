package controller;

import database.resepDB;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
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
            Label label = new Label(resep.getJudul());
            resepContainer.getChildren().add(label);
        }
    }
}