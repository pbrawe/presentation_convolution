package test;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.junit.jupiter.api.Test;

import convolution.ImageProcessor;

public class ImageProcessorTest {

	
	@Test
	void testConvertToPixelsAndBackToNewFile() throws IOException {
		String path = System.getProperty("user.dir") + "/ress/quadrat.png";
		String logpath = System.getProperty("user.dir") + "/ress/out.txt";

		Color[][] img = ImageProcessor.convertToPixels(ImageIO.read(new File(path)));
		BufferedImage newImg = ImageProcessor.createFromPixels(img, BufferedImage.TYPE_INT_RGB);
		File outputfile = new File(System.getProperty("user.dir") + "/ress/new.jpg");
		ImageIO.write(newImg, "jpg", outputfile);
		ImageProcessor.pixelsToFile(img, logpath, false);
	}
	
	@Test
	void testConvertToGrayScale() throws IOException {
		String path = System.getProperty("user.dir") + "/ress/original.jpg";

		Color[][] pixels = ImageProcessor.convertToPixels(ImageIO.read(new File(path)));
		int[][] grayPixels = ImageProcessor.convertPixelsToGrayScale(pixels);

		BufferedImage newImg = ImageProcessor.createFromPixels(grayPixels);
		File outputfile = new File(System.getProperty("user.dir") + "/ress/new_grayScale.jpg");
		ImageIO.write(newImg, "jpg", outputfile);
	}

	@Test
	void testConvolution() throws IOException {
		String path = System.getProperty("user.dir") + "/ress/urlaub.jpg";

		Color[][] pixels = ImageProcessor.convertToPixels(ImageIO.read(new File(path)));
		int[][] grayPixels = ImageProcessor.convertPixelsToGrayScale(pixels);
		int[][] sobeled = ImageProcessor.sobelConvolution(grayPixels);
		BufferedImage newImg = ImageProcessor.createFromPixels(sobeled);
		File outputfile = new File(System.getProperty("user.dir") + "/ress/urlaub_convoluted_grayScale.jpg");
		ImageIO.write(newImg, "jpg", outputfile);
	}

	@Test
	void PrintProjectDir() {
		System.out.println(System.getProperty("user.dir"));
	}

	@Test void createRandomRgbValuesAndPrintToFile() throws IOException {
		String logpath = System.getProperty("user.dir") + "/ress/random.txt";

		BufferedImage newImg = new BufferedImage(3, 3, BufferedImage.TYPE_INT_RGB);
		for(int x = 0; x < newImg.getWidth(); x++){
			for(int y = 0; y < newImg.getHeight(); y++){
				int r = (int) (Math.random() * 255 +1);
				int g = (int) (Math.random() * 255 +1);
				int b = (int) (Math.random() * 255 +1);
				newImg.setRGB(x, y, new Color(r, g, b).getRGB());
			}
		}

		ImageProcessor.pixelsToFile(ImageProcessor.convertToPixels(newImg), logpath, false);
	}

	@Test void createRandomValuesAndPrintToFile() throws IOException {
		String logpath = System.getProperty("user.dir") + "/ress/random.txt";

		int newImg[][] = new int[4][4];
		for(int x = 0; x < newImg.length; x++){
			for(int y = 0; y < newImg[0].length; y++){
				newImg[x][y] = (int) (Math.random() * 9 +1);
			}
		}

		ImageProcessor.pixelsToFile(newImg, logpath);
	}
}
