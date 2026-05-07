package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import model.Resep;

public class itemResepController {
    @FXML private Label judulLabel, kategoriLabel, bahanLabel, waktuLabel;

    public void setData(Resep resep) {
        judulLabel.setText(resep.getJudul());
        kategoriLabel.setText(resep.getJenisMakanan().toUpperCase());
        bahanLabel.setText(resep.getBahan());
        waktuLabel.setText("⏱ " + resep.getEstimasiWaktu() + "m");
    }
}