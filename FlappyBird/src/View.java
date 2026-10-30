import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.ArrayList;

/**
 Class View bertugas menampilkan (menggambar) game:
 background, burung, pipa, label skor, dan teks Game Over / instruksi mulai.
 Class ini mengambil data dari Logic, dan tidak mengatur aturan permainan.
 */
public class View extends JPanel {
    // Ukuran panel permainan
    int widht = 360;
    int height = 640;

    private Logic logic;          // Referensi ke logika game untuk membaca data
    private JLabel scoreLabel;    // Label untuk menampilkan skor
    private Image backgroundImage; // Variabel untuk menyimpan gambar background

    // Konstruktor: menyiapkan panel, background, input keyboard, dan label skor
    public View(Logic logic){
        this.logic = logic;
        // Ukuran panel yang digunakan frame.pack()
        setPreferredSize(new Dimension(widht, height));

        // Memuat gambar backgroundbaru.png dari folder assets
        backgroundImage = new ImageIcon(getClass().getResource("assets/background.png")).getImage();

        // Layout null agar posisi komponen (label skor) dapat diatur manual
        setLayout(null);
        // Panel harus bisa menerima fokus agar dapat membaca tombol keyboard
        setFocusable(true);
        // Mendaftarkan Logic sebagai pendengar input keyboard
        addKeyListener(logic);

        // Membuat JLabel untuk menampilkan skor dengan background, teks putih, dan border hitam
        scoreLabel = new JLabel("0", SwingConstants.CENTER);
        scoreLabel.setBounds(20, 20, 80, 45);
        scoreLabel.setForeground(Color.white); // Teks putih
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 28));

        scoreLabel.setOpaque(true); // Agar background terlihat
        scoreLabel.setBackground(new Color(0, 0, 0, 120)); // Background hitam transparan

        // Memberikan border warna hitam
        Border blackBorder = BorderFactory.createLineBorder(Color.black, 2);
        scoreLabel.setBorder(blackBorder);

        add(scoreLabel);
    }

    // Dipanggil Swing setiap kali panel perlu digambar (termasuk saat repaint())
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    // Menggambar seluruh elemen game
    public void draw(Graphics g){
        // Menggambar backgroundbaru.png menutupi seluruh ukuran layar (360x640)
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, widht, height, null);
        }

        // Memperbarui teks skor (dibagi 2 karena tiap pasang pipa menambah 2 poin)
        scoreLabel.setText(String.valueOf(logic.getScore() / 2));

        // Menggambar burung
        Player player = logic.getPlayer();
        if (player != null){
            g.drawImage(player.getImage(), player.getPosX(), player.getPosY(), player.getWidth(), player.getHeight(), null);
        }

        // Menggambar semua pipa yang sedang aktif
        ArrayList<Pipe> pipes = logic.getPipes();
        if (pipes != null){
            for (int i = 0; i < pipes.size(); i++){
                Pipe pipe = pipes.get(i);
                g.drawImage(pipe.getImage(), pipe.getPosX(), pipe.getPosY(), pipe.getWidth(), pipe.getHeight(), null);
            }
        }

        // Tampilan saat Game Over (Background redup + Tulisan Merah)
        if (logic.isGameOver()) {
            // Efek background redup (lapisan hitam transparan menutupi layar)
            g.setColor(new Color(0, 0, 0, 150));
            g.fillRect(0, 0, widht, height);

            // Tulisan GAME OVER warna Merah
            g.setColor(Color.red);
            g.setFont(new Font("Arial", Font.BOLD, 35));
            g.drawString("GAME OVER", 75, 260);

            g.setColor(Color.white);
            g.setFont(new Font("Arial", Font.BOLD, 16));
            g.drawString("Tekan 'R' untuk Restart", 95, 310);
        }
        // Tampilan saat menunggu pemain menekan spasi setelah restart atau di awal
        else if (logic.isWaitingToStart()) {
            g.setColor(Color.black);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("Tekan SPASI untuk Mulai", 70, 280);
        }
    }
}