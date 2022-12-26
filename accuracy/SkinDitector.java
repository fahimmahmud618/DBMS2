package ClassWork;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Arrays;

public class SkinDitector
{
    int[][][] fullImageColorRGB = new int [256][256][256];
    int[][][] maskImageSkinColorRGB = new int [256][256][256];

    String[] ImageNameList(String folderName)
    {
        File f1 = new File(folderName);
        String lista[]=f1.list();
        Arrays.sort(lista);
        return lista;
    }
    void readAllImage() throws IOException {
        String fullImageArray[] = ImageNameList("ibtd");
        String masklImageArray[] = ImageNameList("Mask");

        for(int i=0;i<554;i++)
        {
            String fullImageName = "ibtd/"+fullImageArray[i];
            String maskImageName = "Mask/"+masklImageArray[i];

            File fullImage = new File(fullImageName);
            File maskImage = new File(maskImageName);

            BufferedImage b_fullImage = ImageIO.read(fullImage);
            BufferedImage b_maskImage = ImageIO.read(maskImage);

            for(int y=0;y<b_fullImage.getHeight()&&y<b_maskImage.getHeight();y++)
            {
                for(int x=0;x<b_fullImage.getWidth()&&x<b_maskImage.getWidth();x++)
                {
                    int fullImagePixel = b_fullImage.getRGB(x,y);
                    int maskImagePixel = b_maskImage.getRGB(x,y);

                    Color fullImageColor = new Color(fullImagePixel,true);
                    Color maskImageColor = new Color(maskImagePixel,true);

                    int nonMaskRed = fullImageColor.getRed();
                    int nonMaskGreen = fullImageColor.getGreen();
                    int nonMaskBlue = fullImageColor.getBlue();

                    int maskRed = maskImageColor.getRed();
                    int maskGreen = maskImageColor.getGreen();
                    int maskBlue = maskImageColor.getBlue();

                    if(maskRed>225 && maskGreen>225 && maskBlue>225)
                        maskImageSkinColorRGB[nonMaskRed][nonMaskGreen][nonMaskBlue]++;
                    else
                        fullImageColorRGB[nonMaskRed][nonMaskGreen][nonMaskBlue]++;

                }
            }
            System.out.println(i);

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
        System.out.println("Sum is "+sum);
        return  sum;
    }

    void calculateProbability() throws IOException
    {
        int skinSum = sumOfArray(fullImageColorRGB);
        int noSkinSum = sumOfArray(maskImageSkinColorRGB);

        double temp=0;
        FileOutputStream outputStream = new FileOutputStream("done.txt");
        DataOutputStream dataOutputStr = new DataOutputStream(outputStream);

        Double[][][] skinProbability = new Double[256][256][256];
        Double[][][] nonSkinProbability = new Double[256][256][256];
        int a=0,b=0;

        for(int x=0;x<256;x++)
        {
            for(int y=0;y<256;y++)
            {
                for(int z=0;z<256;z++)
                {
                    skinProbability[x][y][z] = (double) fullImageColorRGB[x][y][z]/skinSum;
                    nonSkinProbability[x][y][z] = (double) maskImageSkinColorRGB[x][y][z]/noSkinSum;

                    if(nonSkinProbability[x][y][z]==0.0) {
                        temp = 0;
                        a++;
                    }
                    else {
                        temp = skinProbability[x][y][z] / nonSkinProbability[x][y][z];
                        b++;
                    }
                    System.out.println("t->"+temp);
                    dataOutputStr.writeDouble(temp);
                }
            }

        }
        System.out.println("skin "+a+" non: "+b);        dataOutputStr.flush();
    }
}
