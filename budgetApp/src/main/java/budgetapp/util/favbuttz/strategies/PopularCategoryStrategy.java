package budgetapp.util.favbuttz.strategies;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import budgetapp.util.entries.CategoryEntry;

public class PopularCategoryStrategy implements IFavButtStrategy {

    private final int categoriesToSkip;
    private List<CategoryEntry> categories;
    public PopularCategoryStrategy(List<CategoryEntry> categories) {
        this(categories, 0);
    }
    public PopularCategoryStrategy(List<CategoryEntry> categories, int categoriesToSkip) {
        this.categories = new ArrayList<CategoryEntry>();
        for(CategoryEntry c : categories) {
            this.categories.add(c);
        }
        this.categoriesToSkip = Math.max(0, categoriesToSkip);
    }

    @Override
    public String getCategory() {
        removeCategoriesWithPositiveValues(categories);
        Collections.sort(categories, new NumEntriesComparator());

        int resultingIndex = Math.min(categoriesToSkip, categories.size() - 1);

        if(categories.size() > 0) {
            return categories.get(resultingIndex).getCategory();
        }
        return "";
    }

    @Override
    public String getDescription() {
        return "Gives back the most popular category";
    }

    private void removeCategoriesWithPositiveValues(List<CategoryEntry> categories) {
        for (int i = 0; i < categories.size(); i++) {
            if (categories.get(i).getValue().get() >= 0) {
                categories.remove(i);
                i--;
            }
        }
    }

    private class NumEntriesComparator implements Comparator<CategoryEntry> {
        @Override
        public int compare(CategoryEntry a, CategoryEntry b) {
            return b.getNum() - a.getNum();
        }
    }
}
