package designpatterns.behavioral.strategy;

import dao.BankDAO;
import dao.UatmDAO;

public class DisplayAllAccountBalance implements UatmOperation {

    private BankDAO bankDAO;
    private UatmDAO uatmDAO;

    public DisplayAllAccountBalance(BankDAO bankDAO, UatmDAO uatmDAO) {
        this.bankDAO = bankDAO;
        this.uatmDAO = uatmDAO;
    }

    @Override
    public void executeOperation() {

    }
}