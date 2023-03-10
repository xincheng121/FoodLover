package com.lion.foodlover.service;

import com.lion.foodlover.dao.UserDao;
import com.lion.foodlover.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashSet;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    public void signUp(User user) throws Exception {
        User existingUser = getUser(user.getUsername());
        if (existingUser != null) {
            throw new Exception("Username already exists!");
        }

        if (userDao.getUserByEmail(user.getEmail()) != null) { // exist current email
            throw new Exception("Email already exists!");
        }

        user.setEnabled(true);
        user.setPostList(new HashSet<>());
        user.setPurchaseHistory(new HashSet<>());
        user.setSellingHistory(new HashSet<>());
        try {
            userDao.signUp(user);
        } catch (Exception exception) {
            exception.printStackTrace();
            throw exception;
        }
    }

    public User getUser(String username) {
        return userDao.getUserByUsername(username); // get all user info
    }

    public User getPartUserInfo(String username) {
        User user = userDao.getUserByUsername(username); // get all user info
        // remove part information
        user.setPassword(null);
        user.setSellingHistory(null);
        user.setPurchaseHistory(null);
        return user;
    }
}
