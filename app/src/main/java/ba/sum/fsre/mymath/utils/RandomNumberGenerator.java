package ba.sum.fsre.mymath.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomNumberGenerator {

    public List<Integer> generateRandomNumbers() {
        List<Integer> randomNumbers = new ArrayList<>();
        Random random = new Random();
        int previousNumber = -1;
        for (int i = 0; i < 10; i++) {
            int nextNumber;
            do {
                nextNumber = random.nextInt(10); // Generates a random number between 0 and 9
            } while (nextNumber == previousNumber);
            randomNumbers.add(nextNumber);
            previousNumber = nextNumber;
        }
        return randomNumbers;
    }
}