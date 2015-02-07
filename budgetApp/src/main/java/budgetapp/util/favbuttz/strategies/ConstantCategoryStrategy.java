package budgetapp.util.favbuttz.strategies;

public class ConstantCategoryStrategy implements IFavButtStrategy {

    private final String category;

    public ConstantCategoryStrategy(String category) {
        this.category = category;
    }
    @Override
    public String getCategory() {
        return category;
    }
}
