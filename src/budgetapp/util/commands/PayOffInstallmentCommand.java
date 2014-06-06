package budgetapp.util.commands;

import budgetapp.util.Installment;
import budgetapp.util.database.BudgetDataSource;

public class PayOffInstallmentCommand
    extends Command {

    private Installment installment;
    private String dateToEdit;

    public PayOffInstallmentCommand(BudgetDataSource datasource, Installment installment, String dateToEdit) {
        this.datasource = datasource;
        this.installment = installment;
        this.dateToEdit = dateToEdit;
    }

    public Installment getInstallment() {
        return installment;
    }

    @Override
    public void execute() {
        datasource.payOffInstallment(installment, dateToEdit);
        executed = true;
    }

    @Override
    public void unexecute() {
    }

}
