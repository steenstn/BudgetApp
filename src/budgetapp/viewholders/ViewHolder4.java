package budgetapp.viewholders;

import android.view.View;
import budgetapp.main.R;

/**
 * Abstract class for ViewHolders
 * 
 * @author Steen
 * 
 */
public abstract class ViewHolder4
    implements IViewHolder {

    private View firstView;
    private View secondView;
    private View thirdView;
    private View fourthView;

    protected View getFirstView() {
        return firstView;
    }

    protected View getSecondView() {
        return secondView;
    }

    protected View getThirdView() {
        return thirdView;
    }

    protected View getFourthView() {
        return fourthView;

    }

    protected void setFirstView(View view) {
        this.firstView = view;
    }

    protected void setSecondView(View view) {
        this.secondView = view;
    }

    protected void setThirdView(View view) {
        this.thirdView = view;
    }

    protected void setFourthView(View view) {
        this.fourthView = view;
    }

    @Override
    public void setUpConvertView(View convertView) {
        setFirstView(convertView.findViewById(R.id.listFirstView));
        setSecondView(convertView.findViewById(R.id.listSecondView));
        setThirdView(convertView.findViewById(R.id.listThirdView));
        //setFourthView(convertView.findViewById(R.id.listFourthView));
    }

}
