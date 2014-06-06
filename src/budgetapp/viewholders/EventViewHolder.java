package budgetapp.viewholders;

import android.graphics.Color;
import android.widget.TextView;
import budgetapp.util.Event;

public class EventViewHolder
    extends ViewHolder {

    private Event event;
    private TextView leftTextView;
    private TextView centerTextView;
    private TextView rightTextView;

    public EventViewHolder(Event event) {
        this.event = event;
    }

    public EventViewHolder(EventViewHolder viewHolder) {
        this.event = viewHolder.getEvent();
    }

    @Override
    public void populateViews() {
        leftTextView = (TextView) getFirstView();
        centerTextView = (TextView) getSecondView();
        rightTextView = (TextView) getThirdView();
        leftTextView.setText(event.getName() + " - " + event.getTotalCost().makePositive());
        if (event.isActive()) {
            leftTextView.setShadowLayer(10, 0, 0, Color.rgb(135, 240, 255));
            leftTextView.setTextColor(Color.WHITE);
        } else {
            leftTextView.setTextColor(Color.LTGRAY);
            leftTextView.setShadowLayer(0, 0, 0, Color.rgb(135, 240, 255));
        }
        //String startDate = event.getStartDate().substring(0, 10);
        //String endDate = event.getEndDate().substring(0, 10);
        //String resultingDateString = startDate.equalsIgnoreCase(endDate) ? startDate : startDate + " - " + endDate;

        centerTextView.setText("Entries: " + event.getEntries().size()/* + "\n" + resultingDateString*/);
        rightTextView.setText(event.getComment());

    }

    @Override
    public void recycle(IViewHolder tempEntry) {
        setEvent(((EventViewHolder) tempEntry).getEvent());
    }

    @Override
    public IViewHolder copy() {
        return new EventViewHolder(this);
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

}
