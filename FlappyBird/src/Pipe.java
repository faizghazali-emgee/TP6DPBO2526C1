import java.awt.*;

/**
 * Class Pipe merepresentasikan satu pipa (atas atau bawah) dalam game.
 * Menyimpan posisi, ukuran, gambar, kecepatan, dan status apakah
 * pipa sudah dilewati burung (untuk perhitungan skor).
 */
public class Pipe {
    private int posX;       // Posisi horizontal pipa
    private int posY;       // Posisi vertikal pipa
    private int width;      // Lebar pipa
    private int height;     // Tinggi pipa
    private Image image;    // Gambar pipa
    private int velocityX;  // Kecepatan horizontal (tidak dipakai Logic, pergerakan diatur di Logic)
    boolean passed;         // true jika pipa sudah dilewati burung

    // Konstruktor: mengisi posisi, ukuran, dan gambar pipa
    public Pipe(int posX,int posY, int width, int height, Image image){
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
        this.image = image;

        // Nilai awal: belum bergerak dan belum dilewati
        this.velocityX = 0;
        this.passed = false;
    }
    // Getter dan Setter untuk posX
    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    // Getter dan Setter untuk posY
    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    // Getter dan Setter untuk width
    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    // Getter dan Setter untuk height
    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    // Getter dan Setter untuk image
    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    // Getter dan Setter untuk velocityX
    public int getVelocityX() {
        return velocityX;
    }

    public void setVelocityX(int velocityX) {
        this.velocityX = velocityX;
    }

    // Getter dan Setter untuk passed
    public boolean isPassed() {
        return passed;
    }

    public void setPassed(boolean passed) {
        this.passed = passed;
    }
}