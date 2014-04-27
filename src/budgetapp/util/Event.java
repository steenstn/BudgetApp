package budgetapp.util;

import java.util.ArrayList;
import java.util.List;

import budgetapp.util.entries.BudgetEntry;
import budgetapp.util.money.Money;
import budgetapp.util.money.MoneyFactory;

public class Event {
	
	public static final int EVENT_ACTIVE = 1;
	private String name;
	private long id;
	private String startDate;
	private String endDate;
	private String comment;
	private int flags;
	private List<BudgetEntry> entries;
	
	public Event() {
		name = "";
		id = -1;
		startDate = endDate = "";
		flags = 0;
		entries = new ArrayList<BudgetEntry>();
	}
	public Event(long id, String name, String startDate, String endDate, String comment, int flags) {
		this.id = id;
		this.name = name;
		this.startDate = startDate;
		this.endDate = endDate;
		this.comment = comment;
		this.flags = flags;
		entries = new ArrayList<BudgetEntry>();
	}
	
	public Event(long id, String name, String startDate, String endDate, String comment, int flags, List<BudgetEntry> entries) {
		this.id = id;
		this.name = name;
		this.startDate = startDate;
		this.endDate = endDate;
		this.comment = comment;
		this.flags = flags;
		this.entries = entries;
	}

	public Money getTotalCost() {
		Money sum = MoneyFactory.createMoney();
		for(BudgetEntry entry : entries) {
			sum = sum.add(entry.getValue());
		}
		return sum;
	}
	
	public boolean isActive() {
		return (flags & EVENT_ACTIVE) == EVENT_ACTIVE;
	}
	
	public void setActive(boolean value) {
		if(value) {
			flags = flags | EVENT_ACTIVE;
		}
		else {
			flags = flags & (flags ^ EVENT_ACTIVE);
		}
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public int getFlags() {
		return flags;
	}

	public void setFlags(int flags) {
		this.flags = flags;
	}

	public List<BudgetEntry> getEntries() {
		return entries;
	}

	public void setEntries(List<BudgetEntry> entries) {
		this.entries = entries;
	}
	
	
}
