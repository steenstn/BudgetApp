package budgetapp.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import budgetapp.activities.BankActivity;
import budgetapp.main.R;
import budgetapp.util.entries.BudgetEntry;

public class ChooseCategoryBankTransactionFragment extends DialogFragment {

    private ListView categoryList;
    private String[] categories;
    private BudgetEntry entry;
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        categories = getArguments().getStringArray("categories");
        entry = getArguments().getParcelable("entry");
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_choose_category, null);

        builder.setView(view);
        categoryList = (ListView) view.findViewById(R.id.dialog_choose_category_listview);

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id) {
                ChooseCategoryBankTransactionFragment.this.getDialog().cancel();
            }
        });
        updateCategories();
        setUpListeners();
        return builder.create();
    }

    private void updateCategories() {
        String temp[] = new String[categories.length+1];
        for(int i=0;i<categories.length;i++)
        {
            temp[i] = categories[i];
        }
        temp[categories.length] = "Other...";


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, temp);

        categoryList.setAdapter(adapter);
    }

    private void setUpListeners() {
        categoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String category = (String)categoryList.getItemAtPosition(position);
                entry.setCategory(category);
                ((BankActivity)getActivity()).addTransaction(entry);
                ChooseCategoryBankTransactionFragment.this.getDialog().cancel();
            }
        });
    }
}
