package serviceImpl;

import dao.BankDAO;
import dao.UatmDAO;
import designpatterns.behavioral.strategy.UatmContext;
import designpatterns.creational.builder.entities.bank.BankAccount;
import designpatterns.creational.builder.entities.bank.BankCard;
import designpatterns.creational.builder.entities.uatm.Transaction;
import designpatterns.creational.factory.JPAConfiguration;
import designpatterns.creational.factory.JPAConfigurationFactory;
import services.UatmService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

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
        return uatmDAO.findAllTransactionsByUserId();
    }

    @Override
    public int clearTransactionLog() {
        return uatmDAO.deleteAllTransactionsByUserId();
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
    public List<BankAccount> getAllAccountByCardNumber() {
        BankDAO bankDAO = new BankDAO(jpaConfigurationsMap.get(CardSessionServiceImpl.selectedBank).getEntityManager());
        return bankDAO.findBankAccountsByCardNumber();
    }

    @Override
    public BankAccount withDrawMoney(Long accountNumber,
                                     BigDecimal newBalance) {
        BankDAO bankDAO = new BankDAO(jpaConfigurationsMap.get(CardSessionServiceImpl.selectedBank).getEntityManager());
        bankDAO.updateAccountBalance(accountNumber, newBalance);
        return bankDAO.findBankAccountByAccountNumber(accountNumber);

    }

    @Override
    public void transferMoney(BigDecimal amountToTransfer) {
        BankDAO bankDAO = new BankDAO(jpaConfigurationsMap.get(CardSessionServiceImpl.selectedBank).getEntityManager());

    }

    @Override
    public void logout() {

    }

    @Override
    public BankCard getBankCardByBankAndCardNumberAndBankPin(Long cardNumber,
                                                             Long bankPin) {
        BankDAO bankDAO = new BankDAO(jpaConfigurationsMap.get(CardSessionServiceImpl.selectedBank).getEntityManager());
        return bankDAO.findBankCardByCardNumberAndBankPin(cardNumber, bankPin);
    }

    @Override
    public Transaction createTransationLog(Long transactionAccountNumber,
                                           BigDecimal transactionAmount,
                                           String transactionDescription) {
        return uatmDAO.insertNewTransaction(new Transaction
                .TransactionBuilder()
                .transactionAccountNumber(transactionAccountNumber)
                .transactionAmount(transactionAmount)
                .transactionCardNumber(CardSessionServiceImpl.bankCard.getCardNumber())
                .transactionDate(LocalDate.now())
                .transactionDescription(transactionDescription)
                .user(UatmSessionServiceImpl.user)
                .transactionSource(CardSessionServiceImpl.selectedBank)
                .build());

    }
}
