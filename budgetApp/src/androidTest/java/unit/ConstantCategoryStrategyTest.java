package unit;

import android.test.AndroidTestCase;

import budgetapp.util.favbuttz.strategies.ConstantCategoryStrategy;
import budgetapp.util.favbuttz.strategies.IFavButtStrategy;

public class ConstantCategoryStrategyTest extends AndroidTestCase {

    public void testConstantCategoryStrategy() {
        IFavButtStrategy strategy = new ConstantCategoryStrategy("testCategory");
        assertEquals(strategy.getCategory(),("testCategory"));
    }
}
