package budgetapp.viewholders;

import android.graphics.Color;
import android.widget.CheckBox;
import android.widget.TextView;
import budgetapp.util.Installment;

public class InstallmentViewHolder
    extends ViewHolder {

    private Installment entry;
    private String title;
    private TextView leftTextView;
    private TextView centerTextView;
    private TextView rightTextView;
    private CheckBox installmentActiveCheckBox;

    public InstallmentViewHolder(Installment entry) {
        this.entry = entry;
        this.title = "";
    }

    public InstallmentViewHolder(InstallmentViewHolder viewHolder) {
        this.entry = viewHolder.getEntry();
        this.title = viewHolder.getTitle();
    }

    public Installment getEntry() {
        return entry;
    }

    public String getTitle() {
        return title;
    }

    public void setEntry(Installment entry) {
        this.entry = entry;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public CheckBox getCheckBox() {
        return installmentActiveCheckBox;
    }

    @Override
    public void populateViews() {
        this.leftTextView = (TextView) getFirstView();
        this.centerTextView = (TextView) getSecondView();
        this.rightTextView = (TextView) getThirdView();
        leftTextView.setText(getEntry().getCategory());
        centerTextView.setText("" + getEntry().getRemainingValue().makePositive() + "/"
                + getEntry().getTotalValue().makePositive());

        if (!entry.isPaused()) {
            leftTextView.setShadowLayer(10, 0, 0, Color.rgb(135, 240, 255));
        } else {
            leftTextView.setShadowLayer(0, 0, 0, Color.rgb(135, 240, 255));
        }

        double daysLeft = (getEntry().getRemainingValue().divide(getEntry().getDailyPayment())).get();
        int numDaysLeft = (int) Math.ceil(daysLeft);
        rightTextView.setText("" + numDaysLeft);
    }

    @Override
    public void recycle(IViewHolder tempEntry) {
        setEntry(((InstallmentViewHolder) tempEntry).getEntry());
        setTitle(((InstallmentViewHolder) tempEntry).getTitle());
    }

    @Override
    public IViewHolder copy() {
        return new InstallmentViewHolder(this);
    }
}
