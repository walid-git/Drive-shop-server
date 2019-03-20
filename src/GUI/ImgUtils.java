package GUI;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
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

    public static byte[] imgToBytes(String file) {
        File imgFile = new File(file);
        try {
            BufferedImage img = ImageIO.read(imgFile);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(img, "png", outputStream);
            return outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
