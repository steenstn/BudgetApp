package budgetapp.viewholders;

import android.view.View;
import budgetapp.main.R;

public abstract class ViewHolder
    implements IViewHolder {

    private View firstView;
    private View secondView;
    private View thirdView;

    protected View getFirstView() {
        return firstView;
    }

    protected View getSecondView() {
        return secondView;
    }

    protected View getThirdView() {
        return thirdView;
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

    @Override
    public void setUpConvertView(View convertView) {
        setFirstView(convertView.findViewById(R.id.listFirstView));
        setSecondView(convertView.findViewById(R.id.listSecondView));
        setThirdView(convertView.findViewById(R.id.listThirdView));
    }

}
