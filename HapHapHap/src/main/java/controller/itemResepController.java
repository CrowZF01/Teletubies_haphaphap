package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.Resep;
import util.imageUtil;

public class itemResepController {
    @FXML private Label judulLabel, kategoriLabel, bahanLabel, waktuLabel;
    @FXML private ImageView fotoResep;

    public void setData(Resep resep) {
        judulLabel.setText(resep.getJudul());

        String kategori = resep.getJenisMakanan();
        kategoriLabel.setText(kategori != null ? kategori.toUpperCase() : "UMUM");

        bahanLabel.setText(resep.getBahan() != null ? resep.getBahan() : "");
        waktuLabel.setText("⏱ " + resep.getEstimasiWaktu() + "m");

        Image img = imageUtil.getImage(resep.getFoto());

        if (img != null) {
            fotoResep.setImage(img);
        } else {
            fotoResep.setImage(null);
        }
    }
}