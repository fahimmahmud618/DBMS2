import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        SkinDitector skinDitector = new SkinDitector();
        skinDitector.readAllImage();
        skinDitector.calculateProbability();
    }
}
