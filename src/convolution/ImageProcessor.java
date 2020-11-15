package convolution;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.OptionalInt;



public class ImageProcessor {
	public static BufferedImage createFromPixels(int[][] pixels) {
		
		BufferedImage bufferedImage = new BufferedImage(pixels.length, pixels[0].length, BufferedImage.TYPE_INT_RGB);

		// Set each pixel of the BufferedImage to the color from the Color[][].
		for (int x = 0; x < pixels.length; x++) {
		    for (int y = 0; y < pixels[x].length; y++) {	
		    	Color c = new Color(pixels[x][y], pixels[x][y], pixels[x][y]);
	    		bufferedImage.setRGB(x, y, c.getRGB());
		    }
		}
		return bufferedImage;
	}

	public static int[][] convoluteGrayScalePixels(int[][] grayPixels, int[][] kernel){
		int[][] convolution = new int[grayPixels.length-kernel.length+1][grayPixels[0].length-kernel[0].length+1];
		for(int i = 0; i < convolution.length; i++){
			for (int j = 0; j < convolution[0].length; j++){
				convolution[i][j] = singlePixelConvolution(grayPixels, i, j, kernel);
			}
		}
		return convolution;
	}

	public static int singlePixelConvolution(int [][] pixels, int x, int y, int [][] kernel){
		int output = 0;
		for(int i=0;i< kernel.length;++i){
			for(int j=0;j < kernel[0].length;++j){
				output = output + (pixels[x+i][y+j] * kernel[i][j]);
			}
		}
		return output;
	}

	public static int[][] sobelConvolution(int [][] pixels){
		int [][] sobelX = {{-1, 0, 1},{-2, 0, 2},{-1, 0, 1}};
		int [][] sobelY = {{-1, -2, -1},{0, 0, 0},{1, 2, 1}};
		int [][] convolutedX = ImageProcessor.convoluteGrayScalePixels(pixels, sobelX);
		int [][] convolutedY = ImageProcessor.convoluteGrayScalePixels(pixels, sobelY);

		int [][] sobeled = new int[convolutedX.length][convolutedX[0].length];
		for (int x = 0; x < sobeled.length; x++) {
			for (int y = 0; y < sobeled[x].length; y++) {
				sobeled[x][y] = (int) Math.round(Math.sqrt(Math.pow(convolutedX[x][y], 2) + Math.pow(convolutedY[x][y], 2)));
			}
		}
		sobeled = ImageProcessor.normalizeConvolutedGrayPixels(sobeled);

		return sobeled;
	}

	public static Color[][] convertToPixels(BufferedImage img) {
		int width = img.getWidth(null);
		int height = img.getHeight(null);
		Color[][] pixel = new Color[width][height];
		for(int i = 0; i < width; i++) {
			for(int j = 0; j < height; j++) {
				pixel[i][j] = new Color(img.getRGB(i, j));
			}
		}
		return pixel;
	}

	public static BufferedImage createFromPixels(Color[][] pixels, int bufferedImageType) {
		BufferedImage bufferedImage = new BufferedImage(pixels.length, pixels[0].length, bufferedImageType);
		for (int x = 0; x < pixels.length; x++) {
			for (int y = 0; y < pixels[x].length; y++) {
				if(pixels[x][y] != null) {
					bufferedImage.setRGB(x, y, pixels[x][y].getRGB());
				}
			}
		}
		return bufferedImage;
	}

	public static int[][] convertPixelsToGrayScale(Color[][] pixels) {
		int[][] grayPixels = new int[pixels.length][pixels[0].length];
		for (int x = 0; x < pixels.length; x++) {
			for (int y = 0; y < pixels[x].length; y++) {
				if(pixels[x][y] != null) {
					Color p = pixels[x][y];
					grayPixels[x][y] =  (int) Math.floor((0.299 * p.getRed() + 0.587 * p.getGreen() + 0.114 * p.getBlue()));
				}
			}
		}
		return grayPixels;
	}

	public static int[][] normalizeConvolutedGrayPixels(int [][] pixels){
		OptionalInt max = Arrays.stream(pixels).flatMapToInt(Arrays::stream).max();
		OptionalInt min = Arrays.stream(pixels).flatMapToInt(Arrays::stream).min();

		int[][] normalized = new int[pixels.length][pixels[0].length];
		for (int x = 0; x < pixels.length; x++) {
			for (int y = 0; y < pixels[x].length; y++) {
				normalized[x][y] = normalizeInt(pixels[x][y], min.getAsInt(), max.getAsInt(), 0, 255);
			}
		}
		return normalized;
	}

	public static int normalizeInt(int value, int minOld, int maxOld, int minNew, int maxNew){
		return (int) Math.round(( ((float)maxNew - minNew) / (maxOld - minOld) ) * (value - maxOld) + maxNew);
	}

	public static void pixelsToFile(Color[][] pixels, String path, boolean alpha) throws IOException {
		File out = new File(path);
		if (out.exists()) {
			out.delete();
		}
		out.createNewFile();

		for(int x = 0; x < pixels.length; x++) {
			StringBuilder toBeWritten = new StringBuilder();
			for(int j = 0; j < pixels[0].length; j++)
			{
				Color c = pixels[x][j];
				String colors;
				if (alpha){
					colors = "[" + c.getRed() + "," + c.getGreen() + "," + c.getBlue() + "," + c.getAlpha() + "]";
				}
				else{
					colors = "[" + c.getRed() + "," + c.getGreen() + "," + c.getBlue() + "]";
				}
				toBeWritten.append(colors);

				if(j < pixels.length - 1)
					toBeWritten.append("\t\t");
			}
			toBeWritten.append("\n");
			Files.write(Paths.get(path), toBeWritten.toString().getBytes(), StandardOpenOption.APPEND);
		}
	}

	public static void pixelsToFile(int[][] pixels, String path) throws IOException {
		File out = new File(path);
		if (out.exists()) {
			out.delete();
		}
		out.createNewFile();

		for(int x = 0; x < pixels.length; x++) {
			StringBuilder toBeWritten = new StringBuilder();
			for(int y = 0; y < pixels[0].length; y++)
			{
				toBeWritten.append(String.format("%1d", pixels[x][y]));
				if(y < pixels.length - 1)
					toBeWritten.append(",");
			}
			toBeWritten.append("\n");
			Files.write(Paths.get(path), toBeWritten.toString().getBytes(), StandardOpenOption.APPEND);
		}
	}
}

