package ClassWork;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.Set;
import java.util.HashSet;

public class TeClass {

    Set<Integer> testImageNumber = new HashSet<>();
    Set<Integer> trainImageNumber = new HashSet<>();

    double accuracy;

    public double repeat10Times() throws IOException {
        Random rand = new Random();

        while (testImageNumber.size() < 50) {
            int randomNumber = rand.nextInt(534) + 1;
            testImageNumber.add(randomNumber);
        }

        for(int i=1;i<555;i++)
        {
            if(!testImageNumber.contains(i))
                trainImageNumber.add(i);
        }
        for (int num : testImageNumber) {
            System.out.println(num);
        }

        ImageProcess imageProcess = new ImageProcess();
        imageProcess.readImage(trainImageNumber);
        imageProcess.calculateProbability();

        Testing testing = new Testing();
        testing.loadData();
        accuracy = accuracy + testing.calculateAccuracy(testImageNumber);

        return accuracy;
    }



    public static void main(String[] args) throws IOException {
        TeClass teClass = new TeClass();
        double ifficiency =0;
        for(int i=0;i<10;i++)
        {
            ifficiency = ifficiency + teClass.repeat10Times();
        }

        System.out.println("FI=inal Accuracy: "+ifficiency/10);
    }
}