package dao;

import designpatterns.creational.builder.entities.bank.BankCard;
import designpatterns.creational.builder.entities.bank.BankClient;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
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
        BankCard bankCard = query.getSingleResult();
        entityManager.getTransaction().commit();
        return bankCard;
    }
}
