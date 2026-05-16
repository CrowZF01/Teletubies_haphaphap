package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.Resep;
import util.imageUtil;

public class itemResepController {
    // 1. Tambahkan porsiLabel di sini
    @FXML private Label judulLabel, kategoriLabel, bahanLabel, waktuLabel, porsiLabel;
    @FXML private ImageView fotoResep;

    public void setData(Resep resep) {
        judulLabel.setText(resep.getJudul());

        String kategori = resep.getJenisMakanan();
        kategoriLabel.setText(kategori != null ? kategori.toUpperCase() : "UMUM");

        bahanLabel.setText(resep.getBahan() != null ? resep.getBahan() : "");
        waktuLabel.setText("⏱ " + resep.getEstimasiWaktu() + "m");

        // 2. Set teks porsi secara dinamis!
        porsiLabel.setText("🍽 " + resep.getPorsiSajian() + " Porsi");

        Image img = imageUtil.getImage(resep.getFoto());

        if (img != null) {
            fotoResep.setImage(img);
        } else {
            fotoResep.setImage(null);
        }
    }
}