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
		int[] info = makeEncryptedImage(msg);
		System.out.println(getMsgFromImage(info[0], info[1]));

		// Get it back out
		//String output = getMsgFromImage(msgLen1);
		//System.out.println(output);
	}


	// Incorporates the encrypted message into a user selected image
	public static int[] makeEncryptedImage(String msg) {
		int msgLength = 0;
		int[] ret = new int[2];
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
			
			int upperbound = START_BYTE + (numBytes / 2);
			int lowerbound = START_BYTE + 1;

			int start = (int)(Math.random() * ((upperbound - lowerbound) + 1) + lowerbound);
			int end = numBytes - start - 1;
 
 			int bytesLeft = msgLength;
 			int msgStart = 0;
 			int imageNum = 1;

 			while (bytesLeft > 0)
 			{
				//Change the byte in the image to a message byte
				for (int i = start; i < end && (msgStart < msgLength); i++) 
				{
					imageInByte[i] = (byte) (msgBytes[msgStart]);
					msgStart++;
				}

				// convert byte array back to BufferedImage
				InputStream in = new ByteArrayInputStream(imageInByte);
				if (in == null){
					System.out.println("FAILED");
				}
				BufferedImage bImageFromConvert = ImageIO.read(in);
				String filename = "Stegan" + imageNum + ".bmp";

				ImageIO.write(bImageFromConvert, "bmp", new File(filename));

				// BytesLeft is the number of bytes that still need to be written
				bytesLeft -= numBytes - start; 
				imageNum++;

				// reset byte array of this image
				baos.flush();
				imageInByte = baos.toByteArray();
				baos.close();
 			}	

			ret[0] = start;
			ret[1] = msgLength;
			System.out.println("Encryption done");			

		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		return ret;
	}

	// Given a messages length and a user selected image returns the embedded
	// encrypted message
	public static String getMsgFromImage(int start, int msgLength){
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
			int charsRead = 0;
			int i = start;
			while(charsRead < msgLength && i < imageInByte.length) {
				encrypted[charsRead] = imageInByte[i];
				charsRead++;
				i++;
			}

		//	for (int i = start; i < msgLength && i < imageInByte.length; i++){
		//		System.out.println("")
		//		encrypted[i] = imageInByte[i];
		//	}


		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		return new String(encrypted);
	}

	
}
