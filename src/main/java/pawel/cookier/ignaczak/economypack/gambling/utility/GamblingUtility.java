package pawel.cookier.ignaczak.economypack.gambling.utility;

import java.util.Random;

public class GamblingUtility implements IGamblingUtility {
    private final Random random;

    public GamblingUtility(Random random) {
        this.random = random;
    }

    public boolean isTrueBasedOnChance(int trueChance){
        int randomNumber = random.nextInt(0,100);
        return trueChance > randomNumber;
    }

    public int generateRandomNumber(int min, int max){
        return random.nextInt(min, max);
    }
}
