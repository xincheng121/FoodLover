package com.lion.foodlover.dao;

import com.lion.foodlover.entity.Order;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class OrderDao {
    private final SessionFactory sessionFactory;

    @Autowired
    public OrderDao(SessionFactory sessionFactory) throws Exception {
        this.sessionFactory = sessionFactory;
    }

    public Order newOrder(Order order) {
        Session session = null;
        try{
            session = sessionFactory.openSession();
            session.beginTransaction();
            session.save(order);
            session.getTransaction().commit();
            return order;
        } catch (Exception ex){
            ex.printStackTrace();
            if(session != null) session.getTransaction().rollback();
        } finally {
            if (session != null){
                session.close();
            }
        }
        return null;
    }
}
