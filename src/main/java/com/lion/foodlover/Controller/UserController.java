package com.lion.foodlover.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lion.foodlover.entity.Order;
import com.lion.foodlover.entity.Post;
import com.lion.foodlover.entity.User;
import com.lion.foodlover.service.*;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Key;
import java.util.HashMap;
import java.util.Map;

@Controller
public class UserController {
    private final UserService userService;
    private final TokenService tokenService;
    private final PostService postService;
    private final OrderService orderService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public UserController(UserService userService,
                          TokenService tokenService,
                          PostService postService,
                          OrderService orderService) {
        this.userService = userService;
        this.tokenService = tokenService;
        this.postService = postService;
        this.orderService = orderService;
    }

    @RequestMapping(value = "/signin", method = RequestMethod.POST, produces="application/json")
//    @ResponseBody
    public void signIn(@RequestBody User user, HttpServletResponse response) throws IOException {
        try {
            User userInDB = userService.getUser(user.getUsername());
            if (user.getPassword().equals(userInDB.getPassword())) {
                response.setStatus(HttpStatus.CREATED.value());
                response.setStatus(266);
                String token = tokenService.getJWTToken(user.getUsername());
                Map<String, Object> data = new HashMap<>();
                data.put("token", token);
                response.getOutputStream().println(objectMapper.writeValueAsString(data));
            } else {
                // wrong password
                response.setStatus(HttpStatus.CONFLICT.value());
                Map<String, Object> data = new HashMap<>();
                data.put("message", "Wrong Password");
                response.getOutputStream().println(objectMapper.writeValueAsString(data));
            }
        } catch (Exception exception) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            Map<String, Object> data = new HashMap<>();
            data.put("message", exception.getMessage());
            response.getOutputStream().println(objectMapper.writeValueAsString(data));
        }
    }

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    public void signUp(@RequestBody User user, HttpServletResponse response) throws IOException {
        try {
            userService.signUp(user);
        } catch (Exception exception) {
            response.setStatus(HttpStatus.CONFLICT.value());
            Map<String, Object> data = new HashMap<>();
            data.put("message", exception.getMessage());
            response.getOutputStream().println(objectMapper.writeValueAsString(data));
        }
    }

    @RequestMapping(value = {"/user/{username}"}, method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public User searchUserByUsername(@PathVariable("username") String username, @RequestHeader("Authorization") String token,
                                     HttpServletResponse response){
        if (!tokenService.verify(token)) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return null;
        }
        if (tokenService.getUsernameFromToken(token).equals(username)){ //get all user info if token matches current user
            return userService.getUser(username);
        } else { // get part user info if it's a visitor
            return userService.getPartUserInfo(username);
        }
    }

    @RequestMapping(value = {"/newOrder/{postID}"}, method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    @ResponseBody
    public Order placeOrder(@PathVariable("postID") int postID, @RequestHeader("Authorization") String token,
                            HttpServletResponse response){
        if (!tokenService.verify(token)) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return null;
        }
        String username = tokenService.getUsernameFromToken(token);
        User user = userService.getUser(username);
        Post post = postService.getPost(postID);
        return orderService.newOrder(user, post);
    }
}