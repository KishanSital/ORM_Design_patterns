package dao;

import designpatterns.creational.builder.entities.bank.BankAccount;
import designpatterns.creational.builder.entities.bank.BankCard;
import designpatterns.creational.builder.entities.bank.BankClient;
import serviceImpl.CardSessionServiceImpl;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class BankDAO {
    private EntityManager entityManager;

    public BankDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    public List<BankClient> retrieveClientList() {
        entityManager.getTransaction().begin();

        String jpql = "select c from BankClient c";
        TypedQuery<BankClient> query = entityManager.createQuery(jpql, BankClient.class);
        List<BankClient> bankClientList = query.getResultList();
        entityManager.getTransaction().commit();
        return bankClientList;
    }

    public BankCard findBankCardByCardNumberAndBankPin(Long cardNumber, Long bankPin) {
        entityManager.getTransaction().begin();
        String jpql = "select c from BankCard c where c.cardNumber = :cardNumber and c.bankPin = :bankPin";
        TypedQuery<BankCard> query = entityManager.createQuery(jpql, BankCard.class);
        query.setParameter("cardNumber", cardNumber);
        query.setParameter("bankPin", bankPin);
        List<BankCard> resultList = new ArrayList<>(query.getResultList());
        entityManager.getTransaction().commit();
        return resultList.stream().findFirst().orElse(null);
    }

    public int updateAccountBalance(Long accountNumber,
                                    BigDecimal newBalance) {
        entityManager.getTransaction().begin();
        Query query = entityManager.createQuery("UPDATE BankAccount c SET c.bankBalance = :newBalance where c.accountNumber = :accountNumber");
        query.setParameter("newBalance", newBalance);
        query.setParameter("accountNumber", accountNumber);
        int updatedRecords = query.executeUpdate();
        entityManager.getTransaction().commit();
        return updatedRecords;
    }

    public BankAccount findBankAccountByAccountNumber(Long accountNumber) {
        entityManager.getTransaction().begin();
        String jpql = "select c from BankAccount c where c.accountNumber = :accountNumber";
        TypedQuery<BankAccount> query = entityManager.createQuery(jpql, BankAccount.class);
        query.setParameter("accountNumber", accountNumber);
        BankAccount bankAccount = query.getSingleResult();
        entityManager.getTransaction().commit();
        return bankAccount;

    }

    public List<BankAccount> findBankAccountsByCardNumber() {
        entityManager.getTransaction().begin();
        String jpql = "select c from BankCard c where c.cardNumber = :cardNumber";
        TypedQuery<BankCard> query = entityManager.createQuery(jpql, BankCard.class);
        query.setParameter("cardNumber", CardSessionServiceImpl.bankCard.getCardNumber());
        List<BankCard> resultList = new ArrayList<>(query.getResultList());
        entityManager.getTransaction().commit();
        return resultList.get(0).getBankAccounts();
    }
}
