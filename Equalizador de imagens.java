import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Programa {

    public static final String PATH = "EQUALIZADOR";

    int[] calcHist(BufferedImage img){
        int[] hist = new int[256];
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                Color color = new Color(img.getRGB(x, y));
                int red = color.getRed();
                hist[red] += 1;
            }
        }
        return hist;
    }


    public int[] calcHistAcumulado(int[] hist) {
        int[] acumulado = new int[256];
        acumulado[0] = hist[0];
        for(int i=1;i < hist.length;i++) {

            acumulado[i] = hist[i] + acumulado[i-1];
        }
        return acumulado;
    }

    private int menorValor(int[] hist) {
        for(int i=0; i <hist.length; i++) {
            if(hist[i] != 0){
                return hist[i];
            }
        }
        return 0;
    }

    private int[] calculaMapadeCores(int[] hist, int pixels) {
        int[] mapaDeCores = new int[256];
        int[] acumulado = calcHistAcumulado(hist);
        float menor = menorValor(hist);
        for(int i=0; i < hist.length; i++) {
            mapaDeCores[i] = Math.round(((acumulado[i] - menor) / (pixels - menor)) * 255);
        }
        return mapaDeCores;
    }

    public BufferedImage equalizacao(BufferedImage img) {
        int[] hist = calcHist(img);
        int[] mapaDeCores = calculaMapadeCores(hist, img.getWidth() * img.getHeight());
        BufferedImage out = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                Color color = new Color(img.getRGB(x, y));
                int tom = color.getRed();
                int newTom = mapaDeCores[tom];
                Color newColor = new Color(newTom, newTom, newTom);
                out.setRGB(x, y, newColor.getRGB());
            }
        }
      
        return out;
    }

    void run() throws IOException {
        BufferedImage img = ImageIO.read(new File(PATH, "Foto.jpg"));
        BufferedImage newImg = equalizacao(img);
        ImageIO.write(newImg, "png", new File(PATH, "Foto.png"));
    }

    public static void main(String[] args) throws IOException {
        new Programa().run();
    }

}