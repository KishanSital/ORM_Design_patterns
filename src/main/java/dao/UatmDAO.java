package dao;

import designpatterns.creational.builder.entities.uatm.User;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

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
}
