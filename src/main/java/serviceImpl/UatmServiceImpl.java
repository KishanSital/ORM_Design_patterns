package serviceImpl;

import dao.BankDAO;
import dao.UatmDAO;
import designpatterns.behavioral.strategy.ClearTransactionLog;
import designpatterns.behavioral.strategy.UatmContext;
import designpatterns.creational.builder.entities.bank.BankCard;
import designpatterns.creational.builder.entities.uatm.Transaction;
import designpatterns.creational.factory.JPAConfiguration;
import services.UatmService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class UatmServiceImpl implements UatmService {

    private final Map<String, JPAConfiguration> jpaConfigurationsMap;
    private final UatmDAO uatmDAO;
    //usage of factory pattern
    private UatmContext uatmContext;

    //TODO: work with uatmcontext, UatmServiceImpl kan wegvallen en al deze zaken worden
    // dan geplaatst in de gespecificeerde implementatie voor de uatmOperation die in de
    // uatmcontext injected wordt en dit wordt dan gewoon gedaan in de uatmview
    public UatmServiceImpl(Map<String, JPAConfiguration> jpaConfigurationsMap,
                           UatmDAO uatmDAO) {
        this.jpaConfigurationsMap = jpaConfigurationsMap;
        this.uatmDAO = uatmDAO;
    }

    public Map<String, JPAConfiguration> getJpaConfigurationsMap() {
        return this.jpaConfigurationsMap;
    }

    @Override
    public List<Transaction> getAllTransactions() {
        return UatmSessionServiceImpl.user.getTransactions();
    }

    @Override
    public void clearTransactionLog() {

    }

    @Override
    public BigDecimal getAccountBalance(Long accountNumber) {
        return null;
    }

    @Override
    public BankCard getBankCard() {
        return null;
    }

    @Override
    public void displayAllAccountBalance() {

    }

    @Override
    public void withDrawMoney(BigDecimal amountToWithdraw) {

    }

    @Override
    public void transferMoney(BigDecimal amountToTransfer) {

    }

    @Override
    public void logout() {

    }

    @Override
    public BankCard getBankCardByBankAndCardNumberAndBankPin(String bank,
                                                             Long cardNumber,
                                                             Long bankPin) {
        BankDAO bankDAO = new BankDAO(jpaConfigurationsMap.get(bank).getEntityManager());
        return bankDAO.findBankCardByCardNumberAndBankPin(cardNumber,bankPin);
    }
}
