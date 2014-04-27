package budgetapp.util;

import java.util.ArrayList;
import java.util.List;

import budgetapp.util.entries.BudgetEntry;

public class Event {
	
	private String name;
	private long id;
	private String startDate;
	private String endDate;
	private String comment;
	private int flags;
	private List<BudgetEntry> entries;
	
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
