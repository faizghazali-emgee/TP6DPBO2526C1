// Mengimpor komponen GUI Swing (JFrame, JPanel, JButton, JLabel, dll.)
import javax.swing.*;
// Mengimpor class AWT untuk warna, font, dan layout
import java.awt.*;
// Mengimpor class untuk menangani event klik tombol
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Class App adalah titik masuk (entry point) program.
 * Class ini membuat jendela utama, menampilkan Main Menu,
 * dan memulai permainan ketika tombol "Mulai Permainan" ditekan.
 */
public class App {
    public static void main(String[] args){
        // Membuat jendela utama dengan judul "FlappyBird"
        JFrame frame = new JFrame("FlappyBird");
        // Program berhenti sepenuhnya saat jendela ditutup
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Ukuran jendela 360 x 640 piksel
        frame.setSize(360,640);
        // Menempatkan jendela di tengah layar
        frame.setLocationRelativeTo(null);
        // Ukuran jendela tidak boleh diubah oleh pemain
        frame.setResizable(false);

        // Membuat Main Menu
        JPanel mainMenu = new JPanel();
        // Layout null = posisi komponen diatur manual dengan setBounds
        mainMenu.setLayout(null);
        // Warna latar menu: cyan (biru muda)
        mainMenu.setBackground(Color.cyan);

        // Label judul di tengah menu (rata tengah horizontal)
        JLabel titleLabel = new JLabel("Flappy Bird", SwingConstants.CENTER);
        // setBounds(x, y, lebar, tinggi)
        titleLabel.setBounds(30, 150, 300, 50);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        mainMenu.add(titleLabel);

        // Tombol Mulai Permainan (Warna Oranye)
        JButton startButton = new JButton("Mulai Permainan");
        startButton.setBounds(80, 260, 200, 40);
        startButton.setBackground(new Color(255, 140, 0)); // Warna Oranye
        startButton.setForeground(Color.white);             // Warna teks tombol
        startButton.setFont(new Font("Arial", Font.BOLD, 14));
        // Menghilangkan garis fokus di sekitar teks tombol
        startButton.setFocusPainted(false);
        mainMenu.add(startButton);

        // Tombol Keluar (Warna Oranye)
        JButton exitButton = new JButton("Keluar");
        exitButton.setBounds(80, 320, 200, 40);
        exitButton.setBackground(new Color(255, 140, 0)); // Warna Oranye
        exitButton.setForeground(Color.white);
        exitButton.setFont(new Font("Arial", Font.BOLD, 14));
        exitButton.setFocusPainted(false);
        mainMenu.add(exitButton);

        // Menambahkan panel menu ke jendela lalu menampilkannya
        frame.add(mainMenu);
        frame.setVisible(true);

        // Aksi tombol Mulai
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Menghapus tampilan menu dari jendela
                frame.remove(mainMenu);

                // Membuat objek logika game (fisika, pipa, skor, suara)
                Logic logika = new Logic();
                // Membuat tampilan game dan menghubungkannya dengan logika
                View tampilan = new View(logika);
                // Logika perlu referensi ke tampilan agar bisa memanggil repaint()
                logika.setView(tampilan);

                // Memasang tampilan game ke jendela
                frame.add(tampilan);
                // Menyesuaikan ukuran jendela dengan ukuran preferensi panel
                frame.pack();
                // Meminta fokus keyboard agar tombol SPASI/R terbaca
                tampilan.requestFocus();
                // Menyegarkan tata letak dan menggambar ulang jendela
                frame.revalidate();
                frame.repaint();
            }
        });

        // Aksi tombol Keluar
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Menutup program
                System.exit(0);
            }
        });
    }
}