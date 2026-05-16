package controller;
import javafx.scene.control.Button;
import util.imageUtil;
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
    @FXML private Label porsiLabel;

    @FXML private Button btnFavorit;
    private Resep resepAktif;
    private boolean isFavorit = false;

    public void setResepData(Resep resep){
        this.resepAktif = resep;
        judulResep.setText(resep.getJudul());
        waktuLabel.setText("⏱ " + resep.getEstimasiWaktu() + "m");
        pedasLabel.setText("🌶 Level " + resep.getTingkatKepedasan());
        kategoriLabel.setText(resep.getJenisMakanan().toUpperCase());
        langkahResep.setText(resep.getLangkahPembuatan());
        porsiLabel.setText("🍽 " + resep.getPorsiSajian() + " Porsi");

        if (util.sessionManager.isLogin()) {
            int idUser = util.sessionManager.getUser().getId();
            isFavorit = new database.userDB().cekFavorit(idUser, resep.getIdResep());
            renderTombolFavorit(); // Ubah tampilan tombol sesuai status
        }

        try {
            Image img = imageUtil.getImage(resep.getFoto());

            if (img != null) {
                fotoResepDetail.setImage(img);
                placeholderIcon.setVisible(false);
            } else {
                fotoResepDetail.setImage(null);
                placeholderIcon.setVisible(true);
            }

        } catch (Exception e) {
            System.out.println("Gambar tidak ditemukan");
            fotoResepDetail.setImage(null);
            placeholderIcon.setVisible(true);
        }        loadBahan(resep.getIdResep());
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

    private void renderTombolFavorit() {
        if (isFavorit) {
            btnFavorit.setText("♥ Hapus Favorit");
            // Warna merah kalau sudah favorit
            btnFavorit.setStyle("-fx-background-color: #E74C3C; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 6; -fx-padding: 12; -fx-cursor: hand;");
        } else {
            btnFavorit.setText("♡ Simpan Favorit");
            // Warna cokelat kalau belum
            btnFavorit.setStyle("-fx-background-color: #A65021; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 6; -fx-padding: 12; -fx-cursor: hand;");
        }
    }

    @FXML
    public void handleToggleFavorit() {
        if (!util.sessionManager.isLogin()) {
            System.out.println("Harus login dulu!");
            return;
        }

        int idUser = util.sessionManager.getUser().getId();
        database.userDB db = new database.userDB();

        if (isFavorit) {
            // Kalau sudah favorit, maka hapus
            db.hapusFavorit(idUser, resepAktif.getIdResep());
        } else {
            // Kalau belum, maka tambah
            db.tambahKeFavorit(idUser, resepAktif.getIdResep());
        }

        // Balikkan status (Toggle) lalu render ulang tombolnya
        isFavorit = !isFavorit;
        renderTombolFavorit();
    }
}
