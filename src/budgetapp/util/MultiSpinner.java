package budgetapp.util;

/*
 * Original class from Destil at Stack Overflow
 * http://stackoverflow.com/questions/5015686/android-spinner-with-multiple-choice
 */
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class MultiSpinner
    extends Spinner
    implements OnMultiChoiceClickListener {

    private List<String> items;
    private boolean[] selected;
    private boolean[] selectedOnStart;
    private String defaultText;

    public MultiSpinner(Context context) {
        super(context);
    }

    public MultiSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MultiSpinner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public List<String> getSelectedItems() {
        List<String> selectedItems = new ArrayList<String>();
        for (int i = 0; i < selected.length; i++) {
            if (selected[i]) {
                System.out.println(i + " true adding " + items.get(i));
                selectedItems.add(items.get(i));
            }
        }
        return selectedItems;
    }

    public void setChecked(String itemName, boolean checked) {
        int itemIndex = items.indexOf(itemName);
        if (itemIndex != -1) {
            selected[itemIndex] = checked;
            selectedOnStart[itemIndex] = checked;
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
        System.out.println("onClick");
        if (isChecked)
            selected[which] = true;
        else
            selected[which] = false;
    }

    private String createSpinnerText() {
        System.out.println("createSpinnerText");
        StringBuffer spinnerBuffer = new StringBuffer();
        boolean someUnselected = false;
        for (int i = 0; i < items.size(); i++) {
            if (selected[i] == true) {
                spinnerBuffer.append(items.get(i));
                spinnerBuffer.append(", ");
            } else {
                someUnselected = true;
            }
        }
        String spinnerText;
        if (someUnselected) {
            spinnerText = spinnerBuffer.toString();
            if (spinnerText.length() > 2)
                spinnerText = spinnerText.substring(0, spinnerText.length() - 2);
        } else {
            spinnerText = defaultText;
        }

        return spinnerText;
    }

    @Override
    public boolean performClick() {
        System.out.println("performClick");
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMultiChoiceItems(items.toArray(new CharSequence[items.size()]), selected, this);

        builder.setOnCancelListener(new OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                System.out.println("GOT CANCELLED YO");
                for (int i = 0; i < selected.length; i++) {
                    selected[i] = selectedOnStart[i];
                }

            }
        });
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                for (int i = 0; i < selected.length; i++) {
                    selectedOnStart[i] = selected[i];
                }
                String spinnerText = createSpinnerText();
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                    android.R.layout.simple_spinner_item, new String[] { spinnerText });
                setAdapter(adapter);
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.out.println("GOT CANCELCLICEKD YO");
                for (int i = 0; i < selected.length; i++) {
                    selected[i] = selectedOnStart[i];
                }
            }
        });

        builder.show();
        return true;
    }

    public void setItems(List<String> items, String allText) {
        System.out.println("setItems");
        this.items = items;
        this.defaultText = allText;

        selected = new boolean[items.size()];
        selectedOnStart = new boolean[items.size()];

        for (int i = 0; i < selected.length; i++) {
            selected[i] = false;
            selectedOnStart[i] = false;
        }

        // all text on the spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item,
            new String[] { createSpinnerText() });
        setAdapter(adapter);
    }

}
