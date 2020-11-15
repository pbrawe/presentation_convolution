package test;

import convolution.ImageProcessor;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Presentation {

    @Test
    void testConvolution() throws IOException {
        File dir = new File((System.getProperty("user.dir") + "/ress"));
        File[] images = dir.listFiles((file, name) -> name.endsWith(".jpg") || name.endsWith(".png"));
        for (File image : images){
            Color[][] pixels = ImageProcessor.convertToPixels(ImageIO.read(image));
            int[][] grayPixels = ImageProcessor.convertPixelsToGrayScale(pixels);
            int[][] sobeled = ImageProcessor.sobelConvolution(grayPixels);
            BufferedImage newImg = ImageProcessor.createFromPixels(sobeled);
            File outputFile = new File(image.getParentFile().getAbsolutePath() + "/convoluted_grayScaled" + image.getName() + ".jpg");
            ImageIO.write(newImg, "jpg", outputFile);
        }
    }
}
