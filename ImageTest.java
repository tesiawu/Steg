import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;

public class ImageTest {

	public static void makeEncryptedImage(String msg) {

		try {
			JFileChooser fc = new JFileChooser();
			int bs = fc.showOpenDialog(null);
			//System.out.println(fc.getName(fc.getSelectedFile()));
			File img = fc.getSelectedFile();
			byte[] imageInByte;
			
			BufferedImage originalImage = ImageIO.read(img);

			// convert BufferedImage to byte array
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(originalImage, "jpg", baos);
			baos.flush();
			imageInByte = baos.toByteArray();
			baos.close();

			// convert byte array back to BufferedImage
			InputStream in = new ByteArrayInputStream(imageInByte);
			BufferedImage bImageFromConvert = ImageIO.read(in);

			ImageIO.write(bImageFromConvert, "jpg", new File("Encrypted.jpg"));

		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
}