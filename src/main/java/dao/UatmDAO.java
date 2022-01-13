package dao;

import com.github.KishanSital.authenticator.serviceImpl.UserSessionServiceImpl;
import designpatterns.creational.builder.entities.uatm.Transaction;
import designpatterns.creational.builder.entities.uatm.User;
import serviceImpl.UatmSessionServiceImpl;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

public class UatmDAO {
    private EntityManager entityManager;

    public UatmDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public User findUserByUsername(String username) {
        entityManager.getTransaction().begin();
        String jpql = "select u from User u where u.username = :username";
        TypedQuery<User> query = entityManager.createQuery(jpql, User.class);
        query.setParameter("username", username);
        User user = query.getSingleResult();
        entityManager.getTransaction().commit();
        return user;
    }

    public Transaction insertNewTransaction(Transaction transaction) {
        entityManager.getTransaction().begin();
        entityManager.merge(transaction);
        entityManager.getTransaction().commit();
        return transaction;
    }

    public List<Transaction> findAllTransactionsByUserId() {
        entityManager.getTransaction().begin();
        String jpql = "select c from Transaction c where c.user.id = :userId";
        TypedQuery<Transaction> query = entityManager.createQuery(jpql, Transaction.class);
        query.setParameter("userId", UatmSessionServiceImpl.user.getId());
        List<Transaction> transactions = query.getResultList();
        entityManager.getTransaction().commit();
        return transactions;
    }

    public int deleteAllTransactionsByUserId() {
        entityManager.getTransaction().begin();
        Query query = entityManager.createQuery("delete from Transaction c where c.user.id = :userId");
        query.setParameter("userId", UatmSessionServiceImpl.user.getId());
        int rowsDeleted = query.executeUpdate();
        entityManager.getTransaction().commit();
        return rowsDeleted;
    }
}
