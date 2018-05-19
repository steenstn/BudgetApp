package budgetapp.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import budgetapp.fragments.AddCategoryDialogFragment;
import budgetapp.fragments.RemoveCategoryDialogFragment;

import budgetapp.main.R;
import budgetapp.models.BudgetModel;

public class CategoriesActivity
    extends Activity {

    private ListView categoryList;
    private ArrayAdapter<String> adapter;
    private BudgetModel model;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        model = new BudgetModel(this);

        updateList();
        setUpListeners();
    }

    public void updateList() {
        categoryList = (ListView) findViewById(R.id.categoryListView);
        List<String> categories = (model.getCategoryNames());

        adapter = new ArrayAdapter<String>(this, R.layout.listitem_draggable, android.R.id.text1, categories);

        categoryList.setAdapter(adapter);
    }

    public boolean addCategory(String category) {
        return model.addCategory(category);
    }

    public boolean removeCategory(String category) {
        return model.removeCategory(category);
    }

    private void setUpListeners() {
        categoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {

                Object item = categoryList.getItemAtPosition(position);
                String theCategory = item.toString();
                Bundle bundle = new Bundle();
                bundle.putString("chosenCategory", theCategory);
                DialogFragment newFragment = new RemoveCategoryDialogFragment();
                newFragment.setArguments(bundle);
                //newFragment.show(CategoriesActivity.this.getFragmentManager(), "remove_category");

            }
        });

        Button addCategory = (Button) findViewById(R.id.addCategoryBtn);

        addCategory.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                DialogFragment newFragment = new AddCategoryDialogFragment();
           //     newFragment.show(CategoriesActivity.this.getSupportFragmentManager(), "add_category");
            }

        });



    }
}
