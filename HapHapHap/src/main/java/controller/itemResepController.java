package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import model.Resep;
import util.imageUtil; // Panggil util buatanmu

public class itemResepController {
    @FXML private Label judulLabel, kategoriLabel, bahanLabel, waktuLabel;
    @FXML private ImageView fotoResep;

    public void setData(Resep resep) {
        judulLabel.setText(resep.getJudul());
        kategoriLabel.setText(resep.getJenisMakanan() != null ? resep.getJenisMakanan().toUpperCase() : "UMUM");
        bahanLabel.setText(resep.getBahan());
        waktuLabel.setText("⏱ " + resep.getEstimasiWaktu() + "m");

        // WOW! Pemuatan gambar sekarang cuma butuh 1 BARIS!
        // Semua logika rumit sudah diurus oleh imageUtil
        fotoResep.setImage(imageUtil.getImage(resep.getFoto()));
    }
}