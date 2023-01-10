import java.util.*;

public class Test {
    public static void main(String[] args) {

        Set<String> set1 = Set.of("i1","i2","i5");
        Set<String> set2 = Set.of("i2","i4");
        Set<String> set3 = Set.of("i2","i3");
        Set<String> set4 = Set.of("i1","i2","i4");
        Set<String> set5 = Set.of("i1","i3");
        Set<String> set6 = Set.of("i2","i3");
        Set<String> set7 = Set.of("i1","i3");
        Set<String> set8 = Set.of("i1","i2","i3","i5");
        Set<String> set9 = Set.of("i1","i2","i3");

        List<Set<String>> data2 = Arrays.asList(set1, set2, set3, set4,set5,set6,set7,set8,set9);

        Apriori ap = new Apriori();
        ap.apriori(data2,2);
        //System.out.println(ap.apriori(data2,2));


    }
}
