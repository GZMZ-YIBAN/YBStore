package service.impl;

import com.alibaba.fastjson.JSONObject;
import dao.IUserDao;
import entity.YibanProductGetEntity;
import org.hibernate.criterion.DetachedCriteria;
import service.IUserService;
import utils.redis.RedisUtil;

import java.util.List;

/**
 * -----------------------------
 * Copyright © 2017, EchoCow
 * All rights reserved.
 *
 * @author EchoLZY
 * @version 2.0
 * <p>
 * -----------------------------
 * @description
 * @date 18:47 2018/4/15
 * @modified By EchoLZY
 */
public class UserServiceImpl implements IUserService {
    private IUserDao userDao;
    private RedisUtil redisUtil;

    @Override
    public String findByUser(String ybUserid,DetachedCriteria detachedCriteria) {
        List<YibanProductGetEntity> byUser = userDao.findByDeta(detachedCriteria);
        String productUserList = JSONObject.toJSON(byUser).toString();
        redisUtil.set(ybUserid,productUserList);
        return productUserList;
    }

    @Override
    public YibanProductGetEntity findById(Integer id) {
        return userDao.findById(id);
    }

    @Override
    public void updateProduct(Integer id) {
        YibanProductGetEntity y = userDao.findById(id);
        y.setEnable(1);
        userDao.update(y);
    }

    public void setUserDao(IUserDao userDao) {
        this.userDao = userDao;
    }

    public void setRedisUtil(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }
}
