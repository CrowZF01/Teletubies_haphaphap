package util;

import javafx.scene.image.Image;
import java.net.URL;

public class imageUtil {

    // Method dinamis untuk mengambil gambar dari nama file
    public static Image getImage(String namaFile) {
        URL imageUrl = null;

        try {
            // 1. Cek apakah nama file dari DB tidak kosong
            if (namaFile != null && !namaFile.trim().isEmpty()) {

                // KUNCI UTAMA: Gabungkan "/images/" dengan nama file dari DB.
                // Ditambah sedikit proteksi: jika user tidak sengaja mengetik garis miring di awal,
                // kita biarkan saja agar tidak jadi double slash ("//images/").
                String pathFoto = namaFile.startsWith("/") ? namaFile : "/images/" + namaFile;

                imageUrl = imageUtil.class.getResource(pathFoto);
            }

            // 2. Jika gambar asli tidak ketemu (salah ketik di DB / file belum masuk folder),
            // maka otomatis arahkan ke gambar default
            if (imageUrl == null) {
                System.out.println("⚠️ Info: Gambar [" + namaFile + "] tidak ketemu.");
            }

            // 3. Ubah URL menjadi objek Image JavaFX
            if (imageUrl != null) {
                return new Image(imageUrl.toExternalForm());
            } else {
                System.out.println("❌ ERROR FATAL: File tidak ada di folder resources/images!");
                return null;
            }

        } catch (Exception e) {
            System.out.println("❌ Terjadi kesalahan saat memuat gambar: " + e.getMessage());
            return null;
        }
    }
}