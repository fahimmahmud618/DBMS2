import java.io.File;
import java.io.FileWriter;
import java.awt.Color;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
public class GetPxiles {
    public static void main(String args[])throws Exception {
        int[][][] skin = new int[255][255][255];
        int[][][] unskin = new int[255][255][255];
        
        FileWriter writer = new FileWriter("pixel_values.txt");
        //Reading the image
        String str = "/home/sakib/IdeaProjects/First/ibtd/";
        String str2 = "/home/sakib/IdeaProjects/First/Mask/";
        File file= new File(str+"0037.jpg");
        File file2 = new File(str2+"0036.bmp");
        BufferedImage img = ImageIO.read(file);
        BufferedImage img2 = ImageIO.read(file2);
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                //Retrieving contents of a pixel
                int pixel = img.getRGB(x,y);
                int pixel2 = img2.getRGB(x,y);
                //Creating a Color object from pixel value
                Color color = new Color(pixel, true);
                Color color2 = new Color(pixel2, true);
                //Retrieving the R G B values
                int red = color.getRed();
                int green = color.getGreen();
                int blue = color.getBlue();

                int red2 = color2.getRed();
                int green2 = color2.getGreen();
                int blue2 = color2.getBlue();

                if((red2>=220)&&(green2>=220)&&(blue2>=220))
                    unskin[red2][green2][blue2]++;
                else
                    skin[red2][green2][blue2]++;
                
                
                writer.append(red-red2+":");
                writer.append(green-green2+":");
                writer.append(blue-blue2+"");
                writer.append(" ");
                writer.flush();
            }
        }
        writer.close();
        System.out.println("RGB values at each pixel are stored in the specified file");
    }
}

