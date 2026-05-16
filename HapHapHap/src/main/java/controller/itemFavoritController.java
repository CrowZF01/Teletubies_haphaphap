package controller;

import database.userDB;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Resep;
import util.imageUtil;
import util.sessionManager;

public class itemFavoritController {
    @FXML private ImageView fotoResep;
    @FXML private Label kategoriLabel, judulLabel, waktuLabel, porsiLabel, pedasLabel, deskripsiLabel;

    private Resep resepAktif;
    private favoritController parentController; // Untuk merefresh halaman utama saat dihapus

    public void setData(Resep resep, favoritController parentController) {
        this.resepAktif = resep;
        this.parentController = parentController;

        judulLabel.setText(resep.getJudul());
        kategoriLabel.setText(resep.getJenisMakanan() != null ? resep.getJenisMakanan().toUpperCase() : "UMUM");
        waktuLabel.setText("⏱ " + resep.getEstimasiWaktu() + " Menit");
        porsiLabel.setText("🍽 " + resep.getPorsiSajian() + " Porsi");
        pedasLabel.setText("🌶 Level " + resep.getTingkatKepedasan());

        // Ambil cuplikan langkah untuk deskripsi
        String deskripsi = resep.getLangkahPembuatan();
        if (deskripsi != null && deskripsi.length() > 100) {
            deskripsi = deskripsi.substring(0, 100) + "...";
        }
        deskripsiLabel.setText(deskripsi);

        fotoResep.setImage(imageUtil.getImage(resep.getFoto()));
    }

    @FXML
    public void handleLihatResep(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/felix_71241153/app/haphaphap/detail.fxml"));
            Parent root = loader.load();
            detailController controller = loader.getController();
            controller.setResepData(resepAktif);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleHapusFavorit() {
        if (sessionManager.isLogin()) {
            userDB db = new userDB();
            boolean sukses = db.hapusFavorit(sessionManager.getUser().getId(), resepAktif.getIdResep());
            if (sukses && parentController != null) {
                // Refresh daftar favorit di layar
                parentController.loadDataFavorit();
            }
        }
    }
}