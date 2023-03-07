package com.lion.foodlover.service;

import com.lion.foodlover.dao.OrderDao;
import com.lion.foodlover.dao.PostDao;
import com.lion.foodlover.dao.TagDao;
import com.lion.foodlover.entity.Order;
import com.lion.foodlover.entity.Post;
import com.lion.foodlover.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;

@Service
public class OrderService {
    private final PostDao postDao;
    private final com.lion.foodlover.dao.UserDao userDao;
    private final TagDao tagDao;
    private final OrderDao orderDao;

    @Autowired
    public OrderService(PostDao postDao, com.lion.foodlover.dao.UserDao userDao, TagDao tagDao, OrderDao orderDao) {
        this.userDao = userDao;
        this.postDao = postDao;
        this.tagDao = tagDao;
        this.orderDao = orderDao;
    }

    public Order newOrder(User buyer, Post post) {
        User seller = post.getOwner();
        Order order = new Order();
        order.setBuyer(buyer);
        order.setSeller(seller);
        order.setPost(post);
        order.setCreatedTime(Timestamp.from(Instant.now()));
        return orderDao.newOrder(order);
    }
}