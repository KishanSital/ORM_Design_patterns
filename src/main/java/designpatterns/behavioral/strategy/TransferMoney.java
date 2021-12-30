package designpatterns.behavioral.strategy;

import dao.BankDAO;
import dao.UatmDAO;

public class TransferMoney implements UatmOperation {

    private BankDAO bankDAO;
    private UatmDAO uatmDAO;

    public TransferMoney(BankDAO bankDAO, UatmDAO uatmDAO) {
        this.bankDAO = bankDAO;
        this.uatmDAO = uatmDAO;
    }

    @Override
    public void executeOperation() {

    }
}
