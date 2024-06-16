package pawel.cookier.ignaczak.economypack.utility;

import java.util.Random;

public class RandomUtility implements IRandomUtility{
    private final Random random;

    public RandomUtility(Random random) {
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
