package pawel.cookier.ignaczak.economypack.utility;

public interface IRandomUtility {
    boolean isTrueBasedOnChance(int trueChance);
    int generateRandomNumber(int min, int max);
}
