import java.awt.*;

/**
 * Class Player merepresentasikan burung yang dikendalikan pemain.
 * Menyimpan posisi, ukuran, gambar, dan kecepatan vertikal burung.
 */
public class Player {
    private int posX;      // Posisi horizontal burung
    private int posY;      // Posisi vertikal burung
    private int width;     // Lebar burung
    private int height;    // Tinggi burung
    private Image image;   // Gambar burung
    private int velocityY = -0; // Kecepatan vertikal (negatif = naik, positif = turun)

    // Konstruktor: mengisi posisi, ukuran, dan gambar burung
    public Player(int posX,int posY, int width, int height, Image image){
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
        this.image = image;

        // Kecepatan awal 0 (burung diam)
        this.velocityY = -0;
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
    // Getter dan Setter untuk velocityY (dipakai untuk gravitasi dan lompatan)
    public int getVelocityY() {
        return velocityY;
    }

    public void setVelocityY(int velocityY) {
        this.velocityY = velocityY;
    }
}