package budgetapp.util;

public class FlagHandler {

    public static int setFlag(int currentFlags, int flagToSet) {
        return currentFlags | flagToSet;
    }

    public static int unsetFlag(int currentFlags, int flagToUnset) {
        return currentFlags & (currentFlags ^ flagToUnset);
    }

    public static boolean isFlagSet(int currentFlags, int flagToCheck) {
        return (currentFlags & flagToCheck) == flagToCheck;
    }

}
