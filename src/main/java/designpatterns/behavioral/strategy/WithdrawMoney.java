package designpatterns.behavioral.strategy;

import dao.BankDAO;
import dao.UatmDAO;

public class WithdrawMoney implements UatmOperation {

    private BankDAO bankDAO;
    private UatmDAO uatmDAO;

    public WithdrawMoney(BankDAO bankDAO, UatmDAO uatmDAO) {
        this.bankDAO = bankDAO;
        this.uatmDAO = uatmDAO;
    }

    @Override
    public void executeOperation() {

    }
}
