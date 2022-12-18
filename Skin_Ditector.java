import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class Skin_Ditector {
    int[][][] skinColorRGB = new int [256][256][256];
    int[][][] nonSkinColorRGB = new int [256][256][256];

    String[] ImageNameList(String folderName)
    {
        File f1 = new File(folderName);
        String lista[]=f1.list();
        Arrays.sort(lista);
        return lista;
    }
    void readAllImage() throws IOException {
        String fullImageArray[] = ImageNameList("ibtb");
        String masklImageArray[] = ImageNameList("Mask");

        for(int i=0;i<554;i++)
        {
            String fullImageName = "ibtb/"+fullImageArray[i];
            String maskImageName = "Mask/"+masklImageArray[i];

            File fullImage = new File(fullImageName);
            File maskImage = new File(maskImageName);

            BufferedImage b_fullImage = ImageIO.read(fullImage);
            BufferedImage b_maskImage = ImageIO.read(maskImage);

            for(int y=0;y<b_fullImage.getHeight();y++)
            {
                for(int x=0;x<b_fullImage.getWidth();x++)
                {
                    int fullImagePixel = b_fullImage.getRGB(x,y);
                    int maskImagePixel = b_fullImage.getRGB(x,y);

                    Color fullImageColor = new Color(fullImagePixel,true);
                    Color maskImageColor = new Color(maskImagePixel,true);

                    int nonMaskRed = fullImageColor.getRed();
                    int nonMaskGreen = fullImageColor.getGreen();
                    int nonMaskBlue = fullImageColor.getBlue();

                    int maskRed = maskImageColor.getRed();
                    int maskGreen = maskImageColor.getGreen();
                    int maskBlue = maskImageColor.getBlue();

                    if(maskRed>225 && maskGreen>225 && maskBlue>225)
                        nonSkinColorRGB[nonMaskRed][nonMaskGreen][nonMaskBlue]++;
                    else
                        skinColorRGB[nonMaskRed][nonMaskGreen][nonMaskBlue]++;

                }
            }


        }
    }
    int sumOfArray(int theArray[][][])
    {
        int sum=0;
        for(int x=0;x<256;x++)
        {
            for(int y=0;y<256;y++)
            {
                sum += Arrays.stream(theArray[x][y]).sum();
            }
        }
        return  sum;
    }

    void calculateProbability() throws IOException {
        int skinSum = sumOfArray(skinColorRGB);
        int noSkinSum = sumOfArray(nonSkinColorRGB);

        double temp=0;
        FileWriter writer = new FileWriter("prob_values.tex");

        Double[][][] skinProbability = new Double[256][256][256];
        Double[][][] nonSkinProbability = new Double[256][256][256];

        for(int x=0;x<256;x++)
        {
            for(int y=0;y<256;y++)
            {
                for(int z=0;z<256;z++)
                {
                    skinProbability[x][y][z] = Double.valueOf(skinColorRGB[x][y][z]/skinSum);
                    nonSkinProbability[x][y][z] = Double.valueOf(nonSkinColorRGB[x][y][z]/noSkinSum);

                    if(skinProbability[x][y][z]==0)
                       temp =0;
                    else
                        temp=nonSkinProbability[x][y][z]/skinProbability[x][y][z];

                    writer.append((char)temp);
                    writer.append("\n");
                }
            }

        }
    }
}
