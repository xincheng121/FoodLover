package com.lion.foodlover.dao;

import com.lion.foodlover.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.lion.foodlover.entity.Authorities;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@Repository
public class UserDao {
    private final SessionFactory sessionFactory;

    @Autowired
    public UserDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void signUp(User user) throws Exception {
        Authorities authorities = new Authorities();
        authorities.setUsername(user.getUsername());
        authorities.setAuthorities("ROLE_USER");
        Session session = null;

        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            session.save(authorities);
            session.save(user);
            session.getTransaction().commit();

        } catch (Exception exception) {
            exception.printStackTrace();
            if (session != null) {
                session.getTransaction().rollback();
            }
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public User getUserByUsername(String username) {
        try (Session session = sessionFactory.openSession()) {
            User user = session.get(User.class, username);
            if ( user != null) {
                return user;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public User getUserByEmail(String email) {
        User user = null;
        Session session = null;
        try {
            session = sessionFactory.openSession();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<User> criteriaQuery = builder.createQuery(User.class);
            Root<User> userRoot = criteriaQuery.from(User.class);
            Predicate predicate = builder.and(builder.equal(userRoot.get("email"), email));
            criteriaQuery.select(userRoot).where(predicate);
            TypedQuery<User> query = session.createQuery(criteriaQuery);
            user = query.getSingleResult();
        } catch (Exception exception) {
            exception.printStackTrace();
            if (session != null) {
                session.getTransaction().rollback();
            }
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return user;
    }
}
