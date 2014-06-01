package budgetapp.fragments;

/**
 * Dialog Fragment for adding a new category
 * 
 */
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import budgetapp.activities.MainActivity;
import budgetapp.main.R;

public class OtherCategoryDialogFragment
    extends DialogFragment {

    View view;

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        view = inflater.inflate(R.layout.dialog_other_category, null);

        builder.setView(view);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int id) {
                EditText category = (EditText) view.findViewById(R.id.dialog_other_category_name);

                EditText comment = (EditText) view.findViewById(R.id.dialog_other_category_comment);

                String theCategory = category.getText().toString();
                if (!theCategory.equalsIgnoreCase("")) {
                    SharedPreferences settings = PreferenceManager
                        .getDefaultSharedPreferences((MainActivity) getActivity());
                    boolean autoAdd = settings.getBoolean("autoAddOther", false);

                    if (autoAdd) {
                        addCategory(theCategory);
                    }

                    String enteredValue = ((MainActivity) getActivity()).getEnteredValue();
                    if (!enteredValue.equalsIgnoreCase("")) {
                        ((MainActivity) getActivity()).subtractFromBudget(enteredValue, theCategory, comment
                            .getText()
                            .toString());
                    }
                } else {
                    Toast.makeText(view.getContext(), "Please enter category name", Toast.LENGTH_LONG).show();
                }
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                OtherCategoryDialogFragment.this.getDialog().cancel();
            }
        });

        return builder.create();
    }

    @Override
    public void onResume() {
        super.onResume();
        EditText category = (EditText) view.findViewById(R.id.dialog_other_category_name);
        String chosenCategory = ((MainActivity) getActivity()).getChosenCategory();
        if (chosenCategory.length() != 0) {
            category.setText(chosenCategory);
            ((MainActivity) getActivity()).setChosenCategory("");
            EditText comment = (EditText) view.findViewById(R.id.dialog_other_category_comment);
            comment.requestFocus();
        }

    }

    /**
     * Adds the category to the database if it does not exist
     */
    private void addCategory(String theCategory) {
        List<String> allCategories = ((MainActivity) getActivity()).getCategoryNames();
        boolean categoryExists = false;
        for (int i = 0; i < allCategories.size(); i++) {
            if (allCategories.get(i).equalsIgnoreCase(theCategory)) {
                categoryExists = true;
                break;
            }
        }

        if (!categoryExists) {
            ((MainActivity) getActivity()).addCategory(theCategory);
        }
    }
}
