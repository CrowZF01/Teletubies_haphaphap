package controller;

import database.resepDB;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import util.sessionManager;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class addResepController {

    @FXML private TextField inputJudul;
    @FXML private ComboBox<String> comboKategori;
    @FXML private TextField inputWaktu;
    @FXML private VBox bahanContainer;
    @FXML private VBox langkahContainer;
    @FXML private TextField inputPorsi;
    @FXML private Button btnLv0, btnLv1, btnLv2, btnLv3;

    // Variabel untuk Foto
    @FXML private ImageView previewFoto;
    @FXML private VBox placeholderFoto;
    private File fotoTerpilih;

    private int tingkatKepedasan = 0;
    private int stepCounter = 1;

    @FXML
    public void initialize() {
        comboKategori.setItems(FXCollections.observableArrayList("Makanan", "Dessert", "Minuman"));
        bahanContainer.getChildren().clear();
        langkahContainer.getChildren().clear();
        tambahBarisBahan();
        tambahBarisLangkah();
    }

    // ================= UPLOAD FOTO (GAYA SIBARISTA) =================
    @FXML
    public void handleUploadFoto(javafx.scene.input.MouseEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Pilih Foto Resep");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            fotoTerpilih = selectedFile;
            // Tampilkan ke layar
            Image image = new Image(selectedFile.toURI().toString());
            previewFoto.setImage(image);
            placeholderFoto.setVisible(false); // Sembunyikan tulisan "Pilih Foto"
        }
    }

    // ================= DYNAMIC UI FIELDS =================
    @FXML
    public void tambahBarisBahan() {
        HBox row = new HBox(12);
        row.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        Label dot = new Label("⠿");
        dot.setStyle("-fx-text-fill: #BBBBBB; -fx-font-size: 16px;");

        TextField input = new TextField();
        input.setPromptText("Contoh: 1 Siung Bawang");
        input.setStyle("-fx-background-color: #F5F5F5; -fx-padding: 10 12; -fx-background-radius: 6; -fx-border-width: 0;");
        HBox.setHgrow(input, Priority.ALWAYS);

        Label hapus = new Label("✕");
        hapus.setStyle("-fx-cursor: hand; -fx-font-size: 14px; -fx-text-fill: #888888;");
        hapus.setOnMouseClicked(e -> bahanContainer.getChildren().remove(row));

        row.getChildren().addAll(dot, input, hapus);
        bahanContainer.getChildren().add(row);
    }

    @FXML
    public void tambahBarisLangkah() {
        HBox row = new HBox(15);
        row.setAlignment(javafx.geometry.Pos.TOP_LEFT);

        Label nomor = new Label(String.valueOf(stepCounter++));
        nomor.setStyle("-fx-font-size: 40px; -fx-font-weight: bold; -fx-text-fill: #F0E5DE; -fx-translate-y: -10;");

        TextArea input = new TextArea();
        input.setPrefRowCount(2);
        input.setWrapText(true);
        input.setPromptText("Jelaskan langkahnya...");
        input.setStyle("-fx-control-inner-background: #F5F5F5; -fx-background-color: #F5F5F5; -fx-background-radius: 6; -fx-border-width: 0;");
        HBox.setHgrow(input, Priority.ALWAYS);

        row.getChildren().addAll(nomor, input);
        langkahContainer.getChildren().add(row);
    }

    // ================= KEPEDASAN TOGGLE =================
    @FXML public void setPedas0() { updateKepedasan(0, btnLv0); }
    @FXML public void setPedas1() { updateKepedasan(1, btnLv1); }
    @FXML public void setPedas2() { updateKepedasan(2, btnLv2); }
    @FXML public void setPedas3() { updateKepedasan(3, btnLv3); }

    private void updateKepedasan(int level, Button btnAktif) {
        this.tingkatKepedasan = level;
        String pasif = "-fx-background-color: #F5F5F5; -fx-background-radius: 20; -fx-text-fill: #555555; -fx-padding: 6 15; -fx-cursor: hand;";
        String aktif = "-fx-background-color: #FBE2D1; -fx-background-radius: 20; -fx-text-fill: #A65021; -fx-font-weight: bold; -fx-padding: 6 15; -fx-cursor: hand;";

        btnLv0.setStyle(pasif); btnLv1.setStyle(pasif); btnLv2.setStyle(pasif); btnLv3.setStyle(pasif);
        btnAktif.setStyle(aktif);
    }

    // ================= SIMPAN DATA =================
    @FXML
    public void handleSimpan(javafx.event.ActionEvent event) {
        String judul = inputJudul.getText();
        String kategori = comboKategori.getValue();
        int waktu = inputWaktu.getText().isEmpty() ? 0 : Integer.parseInt(inputWaktu.getText());
        int porsi = inputPorsi.getText().isEmpty() ? 0 : Integer.parseInt(inputPorsi.getText());

        if(sessionManager.getUser() == null) {
            System.out.println("Anda belum login!");
            return;
        }
        int idUser = sessionManager.getUser().getId();

        List<String> listBahan = new ArrayList<>();
        for (Node node : bahanContainer.getChildren()) {
            HBox row = (HBox) node;
            TextField tf = (TextField) row.getChildren().get(1);
            if (!tf.getText().trim().isEmpty()) listBahan.add(tf.getText().trim());
        }

        StringBuilder langkahGabungan = new StringBuilder();
        int step = 1;
        for (Node node : langkahContainer.getChildren()) {
            HBox row = (HBox) node;
            TextArea ta = (TextArea) row.getChildren().get(1);
            if (!ta.getText().trim().isEmpty()) {
                langkahGabungan.append(step).append(". ").append(ta.getText().trim()).append("\n\n");
                step++;
            }
        }

        int idKategori = 2; // Makanan
        if ("Dessert".equals(kategori)) idKategori = 4;
        else if ("Minuman".equals(kategori)) idKategori = 5;

        // ----------- PROSES KOPI FOTO (SIBARISTA STYLE) -----------
        String namaFileFoto = null;
        if (fotoTerpilih != null) {
            try {
                // Bikin nama unik biar gak nabrak
                namaFileFoto = System.currentTimeMillis() + "_" + fotoTerpilih.getName().replaceAll("\\s+", "_");

                // 1. Simpan ke Target (Biar langsung muncul)
                File destTarget = new File("HapHapHap/target/classes/images/" + namaFileFoto);
                destTarget.getParentFile().mkdirs();
                Files.copy(fotoTerpilih.toPath(), destTarget.toPath(), StandardCopyOption.REPLACE_EXISTING);

                // 2. Simpan ke Src (Biar permanen di project)
                File destSrc = new File("HapHapHap/src/main/resources/images/" + namaFileFoto);
                destSrc.getParentFile().mkdirs();
                Files.copy(fotoTerpilih.toPath(), destSrc.toPath(), StandardCopyOption.REPLACE_EXISTING);

            } catch (Exception e) {
                System.out.println("❌ Gagal mengopi gambar!");
                e.printStackTrace();
                namaFileFoto = null;
            }
        }
        // ---------------------------------------------------------

        resepDB db = new resepDB();
        // Perhatikan di sini kita mengirim 'namaFileFoto'
        boolean sukses = db.tambahResepLengkap(idUser, judul, idKategori, tingkatKepedasan, waktu, porsi, langkahGabungan.toString(), listBahan, namaFileFoto);

        if (sukses) {
            System.out.println("✅ Resep berhasil disimpan!");
            kembaliKeHome(event);
        } else {
            System.out.println("❌ Gagal menyimpan resep!");
        }
    }

    // ================= NAVIGASI =================
    @FXML
    public void handleKembali(javafx.event.Event event) {
        kembaliKeHome(event);
    }

    private void kembaliKeHome(javafx.event.Event event) {
        try {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/felix_71241153/app/haphaphap/home.fxml"));
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}