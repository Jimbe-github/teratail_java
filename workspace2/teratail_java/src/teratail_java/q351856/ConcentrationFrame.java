package teratail_java.q351856;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class ConcentrationFrame extends JFrame {

    public static void main(String[] args) throws IOException {
        new ConcentrationFrame().setVisible(true);
    }

    private CardsImage cardsImage;

    public ConcentrationFrame() throws IOException {
        super("神経衰弱");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(10, 10, 1550, 950);

        cardsImage = new CardsImage("card52.png");
    }

    public void paint(Graphics g) {
        super.paint(g);
        for(int i=0; i<13*4+3; i++) {
            int x = (i%13) * (cardsImage.getCardWidth()+2)  + 20;
            int y = (i/13) * (cardsImage.getCardHeight()+2) + 46;
            cardsImage.drawCard(g, i, x, y, this);
        }
    }

    private static class CardsImage {
        private Image img;
        private double height, width;

        public CardsImage(String filename) throws IOException {
            img = ImageIO.read(new File(filename)); //画像のロード
            width = img.getWidth(null) / 13.0;
            height = img.getHeight(null) / 5.0;
        }

        public void drawCard(Graphics g, int n, int dx, int dy, ImageObserver observer) {
            if(img == null) throw new IllegalStateException("image nothing.");
            if(n >= 13*4+3) throw new IllegalArgumentException("Number Error");

            int sx = (int)((n % 13) * width);
            int sy = (int)((n / 13) * height);
            g.drawImage(img, dx, dy, (int)(dx+width), (int)(dy+height), sx, sy, (int)(sx+width), (int)(sy+height), observer);
        }
        public int getCardWidth() { return (int)width; }
        public int getCardHeight() { return (int)height; }
    }
}