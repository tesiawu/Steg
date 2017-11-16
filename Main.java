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

public class Main 
{
	private static final int START_BYTE = 200;
	
	// Main Runner method
	public static void main(String[] args) 
	{
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter a message you want to hide in the image: ");
		String msg = sc.nextLine();

		int[] info = makeEncryptedImage(msg);
		
		System.out.println("Here is the key: " + info[0] + info[1]);

		System.out.println("Would you like to decrypt a message? Enter Y or N:");
		String answer = sc.nextLine();

		if(answer.equals("Y") || answer.equals("y")) {
			System.out.println("Enter the key for this image:");
			String key = sc.nextLine();
			if(key.equals("" + info[0] + info[1])) {
				// Decrypt logic here

				System.out.println(getMsgFromImage(info[0], info[1]));
			} else {
				System.out.println("Wrong key. Good bye.");
			}
		} else {
			System.out.println("Ok. Good bye.");
		}
	}


	// Incorporates the encrypted message into a user selected image
	public static int[] makeEncryptedImage(String msg) 
	{
		int msgLength = 0;
		int[] ret = new int[2];
		try 
		{
			// Get the users chosen picture 
			JFileChooser fc = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter(
    			"JPG & GIF Images", "jpg", "gif", "png", "bmp", "jpeg");
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
	
			int numBytes = imageInByte.length - START_BYTE;
			if(numBytes <= 0)
			{
				System.out.println("Image too small");
				return null;
			}
			
			int upperbound = START_BYTE + (numBytes / 2);
			int lowerbound = START_BYTE + 1;

			int start = (int)(Math.random() * ((upperbound - lowerbound) + 1) + lowerbound);
			int end = numBytes;
 
 			int bytesLeft = msgLength;
 			int msgStart = 0;
 			int imageNum = 1;

			for (int i = start; i < end && (msgStart < msgLength); i++) 
			{
				imageInByte[i] = msgBytes[msgStart];

				msgStart++;
				bytesLeft--;
			}

			// convert byte array back to BufferedImage
			InputStream in = new ByteArrayInputStream(imageInByte);
			if (in == null){
				System.out.println("FAILED");
			}
			BufferedImage bImageFromConvert = ImageIO.read(in);
			ImageIO.write(bImageFromConvert, "bmp", new File("Stegan.bmp"));


			// reset byte array of this image
			baos = new ByteArrayOutputStream();
			ImageIO.write(originalImage, "bmp", baos);

			baos.flush();
			imageInByte = baos.toByteArray();
			baos.close();
 			

			ret[0] = start;
			ret[1] = msgLength;
			System.out.println("Encryption done");			

		} 

		catch (IOException e) 
		{
			System.out.println(e.getMessage());
		}
		return ret;
	}

	// Given a messages length and a user selected image returns the embedded
	// encrypted message
	public static String getMsgFromImage(int start, int msgLength)
	{
		byte[] encrypted = null;
		try 
		{
			encrypted = new byte[msgLength];
			int charsRead = 0;

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
			//Read the specified bytes from the image
			
			int index = start;
			while(charsRead < msgLength && index < imageInByte.length)
			{
				encrypted[charsRead] = imageInByte[index];
				if (index == start + 2) 
					System.out.println("byte: " + imageInByte[index]);
				charsRead++;
				index++;
			}
			
		} 

		catch (IOException e) 
		{
			System.out.println(e.getMessage());
		}
		return new String(encrypted);
	}
}
