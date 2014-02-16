package budgetapp.util.commands;

import budgetapp.util.Installment;
import budgetapp.util.database.BudgetDataSource;

public class PayOffInstallmentCommand extends Command {

	Installment installment;
	String dateToEdit;
	public PayOffInstallmentCommand(BudgetDataSource datasource, Installment installment, String dateToEdit) {
		this.datasource = datasource;
		this.installment = installment;
		this.dateToEdit = dateToEdit;
	}
	@Override
	public void execute() {
		datasource.payOffInstallment(installment, dateToEdit);
	}

	@Override
	public void unexecute() {
		// TODO Auto-generated method stub
		
	}

}
