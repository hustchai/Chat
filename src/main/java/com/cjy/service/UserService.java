package com.cjy.service;

import com.cjy.PO.RegisterUser;
import com.cjy.dao.UserDao;
import com.cjy.domain.User;
import com.cjy.util.SessionFactoryUtil;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;

/**
 * Created by jychai on 16/7/7.
 */
public class UserService {

    private SqlSessionFactory sqlSessionFactory = SessionFactoryUtil.getSession();

    public User getUser(Integer id) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            UserDao userDao = session.getMapper(UserDao.class);
            return userDao.getUser(id);
        } finally {
            session.close();
        }
    }

    public int insert(User user) {
        SqlSession session = sqlSessionFactory.openSession();
        try{
            UserDao userDao = session.getMapper(UserDao.class);
            int result = userDao.insert(user);
            if(result == 1) {
                session.commit();
                return result;
            }
            return  0;
        } finally {
            session.close();
        }
    }

    public List<User> getUserByUsername(String username) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            UserDao userDao = session.getMapper(UserDao.class);
            List<User> users = userDao.getUserByUsername(username);
            return users;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return null;
    }

    public User selectUserByUsernameAndPassword(RegisterUser user) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            UserDao userDao = session.getMapper(UserDao.class);
            return userDao.selectUserByUsernameAndPassword(user);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return null;
    }

}
