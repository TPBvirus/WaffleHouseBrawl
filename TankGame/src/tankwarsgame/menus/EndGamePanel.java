package tankwarsgame.menus;

import tankwarsgame.Launcher;
import tankwarsgame.game.ResourceManager;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;

public class EndGamePanel extends JPanel {

    private BufferedImage winnnerImage;
    private BufferedImage backgroundImage;
    private JLabel winner;
    private final Launcher lf;

    public EndGamePanel(Launcher lf) {
        this.lf = lf;

        try {
            System.out.println(lf.getPlayerWinner());
            backgroundImage = ImageIO.read(this.getClass().getClassLoader().getResource("title.png"));

        } catch (IOException e) {
            System.out.println("Error cant read menu background");
            e.printStackTrace();
            System.exit(-3);
        }
        this.setBackground(Color.BLACK);
        this.setLayout(null);


        JButton start = new JButton("Restart Game");
        start.setFont(new Font("Courier New", Font.BOLD, 24));
        start.setBounds(150, 400, 250, 50);
        start.addActionListener((actionEvent -> this.lf.setFrame("game")));


        JButton exit = new JButton("Exit");
        exit.setFont(new Font("Courier New", Font.BOLD, 24));
        exit.setBounds(150, 450, 250, 50);
        exit.addActionListener((actionEvent -> this.lf.closeGame()));

        this.add(start);
        this.add(exit);
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        //g2.drawImage(this.winnnerImage, 0, 0, null);
        g2.drawImage(this.backgroundImage, 0, 0, null);
    }

    public void updateWinner(int winner){
        this.winner = new JLabel("Player %d wins!".formatted(lf.getPlayerWinner()));
        this.winner.setFont(new Font("Courier New", Font.BOLD, 20));
        this.winner.setForeground(Color.WHITE);
        this.winner.setBounds(150, 300, 200, 50);
        this.add(this.winner);
    }
}
