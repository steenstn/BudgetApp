package tests.unit;

import static budgetapp.util.FlagHandler.isFlagSet;
import static budgetapp.util.FlagHandler.setFlag;
import static budgetapp.util.FlagHandler.unsetFlag;
import android.test.AndroidTestCase;

public class FlagHandlerTest
    extends AndroidTestCase {

    public void testSetFlag() {
        int flags = 1;
        int flagToSet = 2;
        flags = setFlag(flags, flagToSet);
        flags = setFlag(flags, flagToSet);
        assertEquals(flagToSet + 1, flags);
    }

    public void testUnsetFlag() {
        int flags = 3;
        int flagToUnset = 1;
        flags = unsetFlag(flags, flagToUnset);
        flags = unsetFlag(flags, flagToUnset);
        assertEquals(true, isFlagSet(flags, 2));
    }

    public void testActiveFlag() {
        int flags = 4;
        assertEquals(true, isFlagSet(flags, 4));
    }

    public void testActiveFlags() {
        int flags = 6;
        assertEquals(true, isFlagSet(flags, 2));
        assertEquals(true, isFlagSet(flags, 4));

    }

}
