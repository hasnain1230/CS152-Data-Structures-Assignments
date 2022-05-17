package Assignment5.Friends.src.friends;

import java.util.ArrayList;
import java.util.HashMap;

public class Testing {
    public static void main(String[] args) {
        ArrayList<Integer> testing = new ArrayList<>();

        testing.add(5);
        testing.add(6);
        testing.add(7);
        testing.add(8);

        System.out.println(testing);

        testing.remove(0);

        System.out.println(testing);
        System.out.println(testing.get(0));

    }
}
