package budgetapp.activities;

import java.util.List;

import budgetapp.fragments.AddCategoryDialogFragment;
import budgetapp.fragments.RemoveCategoryDialogFragment;
import budgetapp.main.R;
import budgetapp.models.BudgetModel;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class CategoriesActivity extends FragmentActivity {
	
	ListView categoryList;
	BudgetModel model;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_categories);
		
		model = new BudgetModel(this);
		
		updateList();
		setUpListeners();
		
	}
	
	public void updateList()
	{
		categoryList = (ListView)findViewById(R.id.categoryListView);
		List<String> categories = (model.getCategoryNames());
        String temp[] = new String[categories.size()];
        for(int i=0;i<categories.size();i++)
        {
        	temp[i] = categories.get(i);
        }
       
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
        		  android.R.layout.simple_list_item_1, android.R.id.text1, temp);
        
		categoryList.setAdapter(adapter);
	}
	
	public boolean addCategory(String category)
	{
		return model.addCategory(category);
	}
	
	public boolean removeCategory(String category)
	{
		return model.removeCategory(category);
	}
	
	private void setUpListeners()
	{
		categoryList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
		{
			@Override
			public boolean onItemLongClick(AdapterView<?> adapter, View view, int position, long arg) {
				
					DialogFragment newFragment = new RemoveCategoryDialogFragment();
	            	newFragment.show(CategoriesActivity.this.getSupportFragmentManager(), "remove_category");
			   
				
				return true;
		   }
		});
		
		Button addCategory = (Button)findViewById(R.id.addCategoryBtn);
		
		addCategory.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				DialogFragment newFragment = new AddCategoryDialogFragment();
            	newFragment.show(CategoriesActivity.this.getSupportFragmentManager(), "add_category");
		   
			}
			
		});
		
	}
}
