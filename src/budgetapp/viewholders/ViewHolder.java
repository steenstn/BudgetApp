package budgetapp.viewholders;

import android.view.View;
import android.widget.TextView;
import budgetapp.main.R;

public abstract class ViewHolder
    implements IViewHolder {

    private TextView leftTextView;
    private TextView centerTextView;
    private TextView rightTextView;

    protected TextView getLeftTextView() {
        return leftTextView;
    }

    protected TextView getCenterTextView() {
        return centerTextView;
    }

    protected TextView getRightTextView() {
        return rightTextView;
    }

    protected void setLeftTextView(TextView textView) {
        this.leftTextView = textView;
    }

    protected void setCenterTextView(TextView textView) {
        this.centerTextView = textView;
    }

    protected void setRightTextView(TextView textView) {
        this.rightTextView = textView;
    }

    @Override
    public void setUpConvertView(View convertView) {
        setLeftTextView((TextView) convertView.findViewById(R.id.listLeftTextView));
        setCenterTextView((TextView) convertView.findViewById(R.id.listCenterTextView));
        setRightTextView((TextView) convertView.findViewById(R.id.listRightTextView));

    }

}
