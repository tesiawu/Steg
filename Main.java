/*
 * Name1: Hannah Anees
 * EID1: ha9656
 * email1: hannahanees@gmail.com
 *
 * Name2: Sunaina Krishnamoorthy
 * EID2: sk42352
 * email2: sunaina@utexas.edu 
 *
 * Name3: Tesia Wu
 * EID3: tjw2492
 * email3: tesiawu@utexas.edu
 *
 * Name4: Robby Stigler
 * EID4: rns777
 * email4: robbystig@gmail.com
 *
 */
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/*
 * This is the main runner class for our unique encryption algorithm. 
 *  
 * It has the encrypt and decrypt methods, and helper methods to integrate
 * the data into pictures
 */

public class Main {

	private static final int START_BYTE = 200;
	
	// Main Runner method
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter a message you want to hide in the image: ");
		String msg = sc.nextLine();
		// Hide it
		int msgLen1 = makeEncryptedImage(msg);




		// Get it back out
		String output = getMsgFromImage(msgLen1);
		System.out.println(output);
	}


	// Incorporates the encrypted message into a user selected image
	public static int makeEncryptedImage(String msg) {
		int msgLength = 0;
		try {
			// Get the users chosen picture 
			JFileChooser fc = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter(
    			"JPG & GIF Images", "jpg", "gif");
			fc.setFileFilter(filter);
			fc.setDialogTitle("Select an image to use for encryption!");
			int bs = fc.showOpenDialog(null);

			File img = fc.getSelectedFile();
			byte[] imageInByte;
			
			BufferedImage originalImage = ImageIO.read(img);

			// convert BufferedImage to byte array
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(originalImage, "bmp", baos);
			baos.flush();
			imageInByte = baos.toByteArray();
			baos.close();
			byte[] msgBytes = msg.getBytes();
			msgLength = msgBytes.length;





			// What we started on
	
			int numBytes = imageInByte.length - START_BYTE;
			System.out.println("num bytes in a picture: " + numBytes);
			// Apparently the terminal truncates the characters if you do too much
			System.out.println("num bytes in the mesg: " + msgLength);
			BufferedImage[] images = new BufferedImage[(int) Math.ceil(1.0 * msgLength / numBytes)];

			for (int i = 0; i < images.length; i++){
				try {
					images[i] = ImageIO.read(img);
				} catch (IOException e){
					System.out.println("Couldn't read.");
					return -1;
				}
			}
			System.out.println(images.length);

			for(int i = 0; i < images.length; i++){
				
			}








			//Change the byte in the image to a message byte
			for (int i = 0; i < msgLength; i++) {
				imageInByte[i + START_BYTE] = (byte) (msgBytes[i]); 
			}

			System.out.println();
			// convert byte array back to BufferedImage
			InputStream in = new ByteArrayInputStream(imageInByte);
			if (in == null){
				System.out.println("FAILED");
			}
			BufferedImage bImageFromConvert = ImageIO.read(in);
			ImageIO.write(bImageFromConvert, "bmp", new File("Stegan.bmp"));

		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		return msgLength;
	}

	// Given a messages length and a user selected image returns the embedded
	// encrypted message
	public static String getMsgFromImage(int msgLength){
		byte[] encrypted = null;
		try {
			// Get user selected file
			JFileChooser fc = new JFileChooser();
			int bs = fc.showOpenDialog(null);
			File img = fc.getSelectedFile();
			byte[] imageInByte;
			
			//Turn picture into BufferedImage object
			BufferedImage originalImage = ImageIO.read(img);

			// convert BufferedImage to byte array
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(originalImage, "bmp", baos);
			baos.flush();
			imageInByte = baos.toByteArray();
			baos.close();
			encrypted = new byte[msgLength];
			//Read the specified bytes from the image
			for (int i = 0; i < msgLength; i++){
				encrypted[i] = imageInByte[i + START_BYTE];
			}


		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		return new String(encrypted);
	}

	
}