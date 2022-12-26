package ClassWork;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Set;

public class Testing {

    int tp=0,fp=0,tn=0,fn=0;
    double accuracy;
    double[][][] resultValue;
    int b=0,w=0;
    static final double T = 0.35;

    public String imageSerialFillWithZero(int serial, String extention)
    {
        int cnt=0;
        String output;
        output = "0";
        while(serial!=0)
        {
            serial=serial/10;
            cnt++;
        }
        cnt=4-cnt-1;

        for(int i=0;i<cnt;i++)
            output=output+"0";

        String s = "/home/sakib/Downloads/ibtd/"+output + Integer.toString(serial)+extention;
        return s;

    }

    public void loadData() throws IOException
    {
        FileInputStream inputStream = null;
        resultValue = new double[256][256][256];

        inputStream = new FileInputStream("done.txt");
        DataInputStream dataInputStr = new DataInputStream(inputStream);

        for(int x=0; x<=255; x++)
        {
            for(int y=0; y<=255; y++)
            {
                for(int z=0; z<=255;z++)
                {
                    resultValue[x][y][z] = dataInputStr.readDouble();
                }
            }
        }
    }


    public double calculateAccuracy(Set<Integer> paths) throws IOException {

        for(int imageSerial : paths)
        {
            String temp;
            temp = "/home/sakib/Downloads/ibtd/Mask";
            temp = temp+imageSerialFillWithZero(imageSerial,".bmp");

            File imageFile = new File(imageSerialFillWithZero(imageSerial,".jpg"));
            BufferedImage img = ImageIO.read(imageFile);

            File imageFile2 = new File(temp);
            BufferedImage img2 = ImageIO.read(imageFile2);

            int width = img.getWidth();
            int height = img.getHeight();

            BufferedImage newImage = new BufferedImage(width, height, img.getType());
            int pixel,pixel2;

            for (int i = 0; i < width; i++)
            {
                for (int j = 0; j < height; j++)
                {
                    pixel = img.getRGB(i, j);
                    pixel2 = img2.getRGB(i, j);

                    Color color = new Color(pixel, true);
                    int red = color.getRed();
                    int green = color.getGreen();
                    int blue = color.getBlue();

                    Color color2 = new Color(pixel2, true);
                    int red2 = color2.getRed();
                    int green2 = color2.getGreen();
                    int blue2 = color2.getBlue();

                    double rValue = resultValue[red][green][blue];

                    if(rValue < T)
                    {
                        if (red2>225 && green2>225 && blue2>225)
                        {
                            tp++;
                        }
                        else
                            tn++;
                    }
                    else
                    {
                        if (red2>225 && green2>225 && blue2>225)
                        {
                            fn++;
                        }
                        else
                            fp++;
                    }

                }
            }

        }

        accuracy = (double) (tp+fp)/ (double) (tn+fn);
        return accuracy;
    }

//    public static void main(String[] args) throws IOException {
//        Testing testing = new Testing();
//        testing.loadData();
//        testing.maskImageCreate("0000.jpg");
//
//        //System.out.println("Total black "+testing.b+"  white: "+testing.w);
//    }
}
