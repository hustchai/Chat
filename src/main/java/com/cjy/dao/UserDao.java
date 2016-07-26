package com.cjy.dao;

import com.cjy.PO.RegisterUser;
import com.cjy.domain.User;

import java.util.List;

/**
 * Created by jychai on 16/7/5.
 */
public interface UserDao {

    public User getUser(Integer id);

    public int insert(User user);

    List<User> getUserByUsername(String username);

    User selectUserByUsernameAndPassword(RegisterUser user);
}
