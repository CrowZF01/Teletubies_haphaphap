package controller;

import database.resepDB;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import model.Resep;
import model.Bahan;
import java.util.List;

public class detailController {

    @FXML
    private Label bahanResep;

    @FXML
    private ImageView fotoResepDetail;

    @FXML
    private Label judulResep;

    @FXML
    private Label kategoriLabel;

    @FXML
    private Label langkahResep;

    @FXML
    private Label pedasLabel;

    @FXML
    private Label placeholderIcon;

    @FXML
    private Label waktuLabel;

    public void setResepData(Resep resep){
        judulResep.setText(resep.getJudul());
        waktuLabel.setText("⏱ " + resep.getEstimasiWaktu() + "m");
        pedasLabel.setText("🌶 Level " + resep.getTingkatKepedasan());
        kategoriLabel.setText(resep.getJenisMakanan().toUpperCase());
        langkahResep.setText(resep.getLangkahPembuatan());

        try {
            if (resep.getFoto() != null && !resep.getFoto().isEmpty()){
                String imgPath = "/images/" + resep.getFoto();
                Image img = new Image(getClass().getResourceAsStream(imgPath));
                fotoResepDetail.setImage(img);
                placeholderIcon.setVisible(false);
            }
        } catch (Exception e){
            System.out.println("Gambar tidak ditemukan");
            placeholderIcon.setVisible(true);
        }
        loadBahan(resep.getIdResep());
    }

    private void loadBahan(int idResep) {
        resepDB db = new resepDB();
        List<Bahan> listBahan = db.getBahanByResep(idResep);
        if (listBahan.isEmpty()){
            bahanResep.setText("Daftar bahan tidak tersedia");
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (Bahan bahan : listBahan){
            sb.append("- ").append(bahan.getNamaBahan()).append("\n");
        }
        bahanResep.setText(sb.toString());
    }

    @FXML
    private void handleKembali() {
        try {
            Stage stage = (Stage) judulResep.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/felix_71241153/app/haphaphap/home.fxml"));
            Parent root = loader.load();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
