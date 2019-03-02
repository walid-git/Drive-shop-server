package GUI;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public abstract class ImgUtils {

    public static Image getImageFromByteArray(byte img[])  {
        ByteArrayInputStream bis = new ByteArrayInputStream(img);
        BufferedImage bImage = null;
        try {
            bImage = ImageIO.read(bis);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return SwingFXUtils.toFXImage(bImage, null);

    }

}
