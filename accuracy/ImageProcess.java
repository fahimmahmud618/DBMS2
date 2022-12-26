package ClassWork;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Arrays;
import java.util.Set;

public class ImageProcess {

        int[][][] screenColor,nonScreenColor;
        double[][][] screenResult, nonScreenResult;

    public String imageSerialFillWithZero(int serial, String extention)
    {
        String x =  Integer.toString(serial);
        int cnt=0;
        String output;
        output = "0";
        while(serial!=0)
        {
            serial=serial/10;
            cnt++;
        }
        cnt=4-cnt-1;

        for(int i=0;i<cnt;i++) {
            output = output + "0";
        }

        String s = output + x +extention;
        System.out.println("From function "+s);
        return s;

    }

        public void readImage(Set<Integer> path) throws IOException {
            String maskFolderName = "/home/sakib/Downloads/ibtd/Mask";
            String nonMaskFolderName = "/home/sakib/Downloads/ibtd";

            screenColor = new int[256][256][256];
            nonScreenColor = new int[256][256][256];

           for(int imageSerial : path) {

               System.out.println("serail: "+imageSerial+" name: "+imageSerialFillWithZero(imageSerial,"aaaa"));
               String maskPathName = maskFolderName + "/" + imageSerialFillWithZero(imageSerial,".bmp");
               String nonMaskPathName = nonMaskFolderName + "/" + imageSerialFillWithZero(imageSerial,".jpg");

               System.out.println(maskPathName);
               File maskImageFile = new File(maskPathName);
               File nonMaskImageFile = new File(nonMaskPathName);
               BufferedImage maskImage = ImageIO.read(maskImageFile);
               BufferedImage nonMaskImage = ImageIO.read(nonMaskImageFile);
               System.out.println(imageSerial);


               for (int y = 0; y < maskImage.getHeight() && y< nonMaskImage.getHeight(); y++) {
                   for (int x = 0; x < maskImage.getHeight() && x< nonMaskImage.getWidth(); x++) {
                       int maskPixel = maskImage.getRGB(x, y);
                       int nonMaskPixel = nonMaskImage.getRGB(x, y);

                       Color maskColor = new Color(maskPixel, true);
                       Color nonMaskColor = new Color(nonMaskPixel, true);

                       storeInArray(maskColor, nonMaskColor);
                   }
               }
           }

        }


        public void storeInArray(Color maskColor, Color nonMaskColor)
        {
            int maskRed = maskColor.getRed();
            int maskGreen = maskColor.getGreen();
            int maskBlue = maskColor.getBlue();

            int nonMaskRed = nonMaskColor.getRed();
            int nonMaskGreen = nonMaskColor.getGreen();
            int nonMaskBlue = nonMaskColor.getBlue();

            if(maskRed>225 && maskGreen>225 && maskBlue>225){
                nonScreenColor[nonMaskRed][nonMaskGreen][nonMaskBlue]++;
            }
            else{
                screenColor[nonMaskRed][nonMaskGreen][nonMaskBlue]++;
                //System.out.println(nonMaskRed+" "+nonMaskGreen+" "+nonMaskBlue);
            }

            //screenColor[0][0][0] = 5;

        }

        public void calculateProbability() throws IOException {
            int screenSum = getSum(screenColor);
            int nonScreenSum = getSum(nonScreenColor);
            double tempResult;

             screenResult = new double[256][256][256];
             nonScreenResult  = new double[256][256][256];


            divide(screenSum,screenResult,screenColor);
            divide(nonScreenSum,nonScreenResult,nonScreenColor);

            FileOutputStream outputStream = new FileOutputStream("done.txt");
            DataOutputStream dataOutputStr = new DataOutputStream(outputStream);

            for(int x=0; x<=255; x++){
                for(int y=0; y<=255; y++){
                    for(int z=0; z<=255;z++){
                        if(nonScreenResult[x][y][z] != 0.0)
                            tempResult = screenResult[x][y][z] / nonScreenResult[x][y][z];
                        else
                            tempResult =0.0;

                        dataOutputStr.writeDouble(tempResult);
                        //System.out.print(tempResult);
                    }
                }
            }

            dataOutputStr.flush();

        }

        public void divide(int total, double resultArray[][][], int counter[][][])
        {
            for(int x=0; x<=255; x++){
                for(int y=0; y<=255; y++){
                    for(int z=0; z<=255;z++){
                        resultArray[x][y][z] =(double) counter[x][y][z]/ total;
                    }
                }
            }
        }

        public int getSum(int colorArray[][][]){
            int total=0;
            for(int x=0; x<=255; x++){
                for(int y=0; y<=255; y++){
                    total += Arrays.stream(colorArray[x][y]).sum();
                }
            }
            return total;
        }


        private String[] readImageFromFolder(String pathName)
        {
            File directoryPath = new File(pathName);
            String imageNames[] = directoryPath.list();
            Arrays.sort(imageNames);

            return imageNames;
        }

//    public static void main(String[] args) throws IOException {
//        ImageProcess imageProcess = new ImageProcess();
//        imageProcess.readImage();
//        imageProcess.calculateProbability();
//    }
}
