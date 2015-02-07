package budgetapp.util.favbuttz;


import budgetapp.util.favbuttz.strategies.IFavButtStrategy;

public class FavButt {

    private final IFavButtStrategy strategy;
    private boolean enabled;

    public FavButt(IFavButtStrategy strategy) {
        this.strategy = strategy;
        enabled = true;
    }

}
