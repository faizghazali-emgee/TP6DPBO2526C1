// Import untuk memutar suara (.wav)
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
// Import Swing (Timer, ImageIcon)
import javax.swing.*;
import java.awt.*;
// Import untuk event timer dan keyboard
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
// Import untuk membaca file suara dari resource
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Class Logic berisi seluruh logika permainan:
 * pergerakan burung (gravitasi & lompat), pembuatan dan pergerakan pipa,
 * deteksi tabrakan, skor, tingkat kesulitan, suara, dan restart.
 * ActionListener dipakai untuk game loop (Timer),
 * KeyListener dipakai untuk membaca input keyboard.
 */
public class Logic implements ActionListener, KeyListener {
    // Ukuran area permainan
    int frameWidth = 360;
    int frameHeight = 640;

    // Posisi awal dan ukuran burung (player)
    int playerStartposX = frameWidth/8;
    int playerStartposY = frameHeight/2;
    int playerWidth = 34;
    int playerHeight = 24;

    // Posisi awal dan ukuran pipa (muncul dari sisi kanan layar)
    int pipeStartPosX = frameWidth;
    int pipeStartPosY = 0;
    int pipeWidth = 64;
    int pipeHeight = 512;

    // gameLoop: pembaruan game ~60 kali per detik
    Timer gameLoop;
    // pipesCooldown: jeda waktu antar kemunculan pasangan pipa
    Timer pipesCooldown;
    // Percepatan gravitasi yang menambah kecepatan jatuh tiap frame
    int gravity = 1;

    // Kecepatan pipa bergerak ke kiri (negatif = ke kiri)
    int pipeVelocityX = -2;

    // Referensi ke tampilan untuk memanggil repaint()
    View view;
    Image birdImage;
    Player player;

    // Gambar pipa bawah dan pipa atas
    Image lowerPipeImage;
    Image upperPipeImage;
    // Daftar semua pipa yang sedang aktif di layar
    ArrayList<Pipe> pipes;

    // Status permainan
    boolean gameOver = false;
    boolean isWaitingToStart = true; // true saat awal buka game atau setelah restart, menunggu SPASI
    int score = 0;        // Tiap pipa dihitung 1, sehingga satu pasang = 2
    int currentLevel = 1; // Level kesulitan (1 - 3)

    // Konstruktor: memuat gambar, membuat objek, dan menjalankan timer
    public Logic(){
        // Memuat gambar burung dari folder assets lalu membuat objek Player
        birdImage = new ImageIcon(getClass().getResource("assets/bird.png")).getImage();
        player = new Player(playerStartposX, playerStartposY, playerWidth, playerHeight, birdImage);

        // Memuat gambar pipa dan menyiapkan daftar pipa
        lowerPipeImage = new ImageIcon(getClass().getResource("assets/lowerPipe.png")).getImage();
        upperPipeImage = new ImageIcon(getClass().getResource("assets/upperPipe.png")).getImage();
        pipes = new ArrayList<Pipe>();

        // Timer pembuat pipa: setiap 2000 ms (2 detik) pada awal permainan
        pipesCooldown = new Timer(2000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent){
                // Pipa hanya dibuat jika game belum berakhir dan sudah dimulai
                if (!gameOver && !isWaitingToStart) {
                    placePipes();
                }
            }
        });
        pipesCooldown.start();

        // Game loop: 1000/60 ms per frame (sekitar 60 FPS), memanggil actionPerformed
        gameLoop = new Timer(1000/60, this);
        gameLoop.start();
    }

    // Menyimpan referensi tampilan
    public void setView(View view){
        this.view = view;
    }

    // Getter untuk dipakai oleh View saat menggambar
    public Player getPlayer(){
        return player;
    }

    public ArrayList<Pipe> getPipes(){return pipes;}

    public boolean isGameOver() {
        return gameOver;
    }

    public boolean isWaitingToStart() {
        return isWaitingToStart;
    }

    public int getScore() {
        return score;
    }

    // Fungsi untuk memutar Sound Effect (.wav)
    public void playSound(String soundFileName) {
        try {
            // Mengambil file suara dari folder assets
            InputStream audioSrc = getClass().getResourceAsStream("assets/" + soundFileName);
            // Jika file tidak ditemukan, tampilkan pesan dan hentikan fungsi
            if (audioSrc == null) {
                System.out.println("File suara tidak ditemukan: assets/" + soundFileName);
                return;
            }
            // Dibungkus buffer agar format audio dapat dibaca dengan benar
            InputStream bufferedIn = new BufferedInputStream(audioSrc);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedIn);

            // Membuka dan memutar suara
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        } catch (Exception e) {
            // Jika terjadi kesalahan saat memutar suara
            System.out.println("Gagal memutar suara: " + soundFileName);
            e.printStackTrace();
        }
    }

    // Membuat sepasang pipa (atas dan bawah) dengan celah di antaranya
    public void placePipes(){
        // Posisi Y pipa atas diacak (nilai negatif sehingga sebagian pipa di luar layar)
        int randomPosY = (int) (pipeStartPosY - (pipeHeight / 4) - Math.random() * (pipeHeight / 2));

        // Ukuran celah antar pipa semakin kecil seiring naiknya level
        int openingSpace;
        if (currentLevel == 1) {
            openingSpace = frameHeight / 3;
        } else if (currentLevel == 2) {
            openingSpace = frameHeight / 3 - 20;
        } else {
            openingSpace = frameHeight / 4;
        }

        // Pipa atas
        Pipe upperPipe = new Pipe(pipeStartPosX, randomPosY, pipeWidth, pipeHeight, upperPipeImage);
        pipes.add(upperPipe);

        // Pipa bawah: diletakkan di bawah pipa atas ditambah ukuran celah
        Pipe lowerPipe = new Pipe(pipeStartPosX, randomPosY + openingSpace + pipeHeight, pipeWidth, pipeHeight, lowerPipeImage);
        pipes.add(lowerPipe);
    }

    // Menaikkan level kesulitan berdasarkan skor
    public void updateDifficulty() {
        // Skor ditampilkan = score / 2 karena tiap pasang pipa menambah 2 poin
        int actualScore = score / 2;

        if (actualScore >= 40 && currentLevel < 3) {
            // Level 3: pipa muncul lebih cepat (tiap 1,3 detik)
            currentLevel = 3;
            pipesCooldown.setDelay(1300);
        } else if (actualScore >= 20 && actualScore < 40 && currentLevel < 2) {
            // Level 2: pipa muncul tiap 1,5 detik
            currentLevel = 2;
            pipesCooldown.setDelay(1500);
        }
    }

    // Memperbarui posisi semua objek pada setiap frame
    public void move(){
        // Tidak ada pergerakan jika game over atau sedang menunggu mulai
        if (gameOver || isWaitingToStart) {
            return;
        }

        // Gravitasi menambah kecepatan vertikal, lalu posisi burung diperbarui
        player.setVelocityY(player.getVelocityY() + gravity);
        player.setPosY(player.getPosY() + player.getVelocityY());

        // Batas bawah JFrame (Game Over)
        if (player.getPosY() >= frameHeight - player.getHeight()) {
            player.setPosY(frameHeight - player.getHeight());
            // Suara kematian hanya dimainkan sekali
            if (!gameOver) {
                playSound("sfx_die.wav");
            }
            gameOver = true;
        }

        // Batas atas
        // Burung tidak bisa keluar dari layar bagian atas
        if (player.getPosY() < 0) {
            player.setPosY(0);
            player.setVelocityY(0);
        }

        // Menggerakkan semua pipa ke kiri dan memeriksa skor serta tabrakan
        for (int i = 0; i < pipes.size(); i++){
            Pipe pipe = pipes.get(i);
            pipe.setPosX(pipe.getPosX() + pipeVelocityX);

            // Jika pipa sudah dilewati burung (sisi kanan pipa di kiri burung), tambah skor
            if (!pipe.isPassed() && pipe.getPosX() + pipe.getWidth() < player.getPosX()) {
                pipe.setPassed(true);
                score += 1;
                playSound("sfx_point.wav");
                updateDifficulty();
            }

            // Jika burung menabrak pipa, permainan berakhir
            if (collision(player, pipe)) {
                if (!gameOver) {
                    playSound("sfx_die.wav");
                }
                gameOver = true;
            }
        }
    }

    // Deteksi tabrakan antara dua persegi panjang (burung dan pipa)
    public boolean collision(Player a, Pipe b) {
        return a.getPosX() < b.getPosX() + b.getWidth() &&   // sisi kiri burung sebelum sisi kanan pipa
                a.getPosX() + a.getWidth() > b.getPosX() &&   // sisi kanan burung melewati sisi kiri pipa
                a.getPosY() < b.getPosY() + b.getHeight() &&  // sisi atas burung di atas sisi bawah pipa
                a.getPosY() + a.getHeight() > b.getPosY();    // sisi bawah burung di bawah sisi atas pipa
    }

    // Mengembalikan game ke kondisi awal setelah game over
    public void restartGame() {
        player.setPosY(playerStartposY);
        player.setVelocityY(0);
        pipes.clear();
        score = 0;
        currentLevel = 1;
        pipesCooldown.setDelay(2000);
        gameOver = false;
        // Menunggu pemain menekan SPASI sebelum permainan berjalan lagi
        isWaitingToStart = true;
    }

    // Dipanggil oleh gameLoop pada setiap frame
    @Override
    public void actionPerformed(ActionEvent e){
        // Perbarui posisi objek
        move();
        // Gambar ulang tampilan jika sudah terhubung
        if (view != null){
            view.repaint();
        }
    }

    // keyTyped tidak digunakan
    @Override
    public void keyTyped(KeyEvent e){}
    // Menangani tombol yang ditekan
    public void keyPressed(KeyEvent e){
        if (e.getKeyCode() == KeyEvent.VK_SPACE){
            if (isWaitingToStart) {
                // SPASI pertama (baik saat awal buka game maupun setelah restart): mulai permainan dan burung melompat
                isWaitingToStart = false;
                player.setVelocityY(-10);
                playSound("sfx_wing.wav");
            } else if (!gameOver) {
                // SPASI saat bermain: burung melompat (kecepatan negatif = ke atas)
                player.setVelocityY(-10);
                playSound("sfx_wing.wav");
            }
        } else if (e.getKeyChar() == 'r' || e.getKeyChar() == 'R') {
            // Tombol R hanya berfungsi saat game over
            if (gameOver) {
                restartGame();
            }
        }
    }
    // keyReleased tidak digunakan
    public void keyReleased(KeyEvent e){}
}