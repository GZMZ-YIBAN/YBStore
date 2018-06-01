package service;

import entity.YibanProductGetEntity;
import org.hibernate.criterion.DetachedCriteria;

import java.util.List;

/**
 * -----------------------------
 * Copyright © 2017, EchoCow
 * All rights reserved.
 *
 * @author EchoLZY
 * @version 2.0
 * <p>对用户
 * -----------------------------
 * @description
 * @date 18:46 2018/4/15
 * @modified By EchoLZY
 */
public interface IUserService {
    /**
     * 通过用户id查询商品，同时存入或更新缓存
     * @param ybUserid 用户id
     * @param detachedCriteria 离线查询对象
     * @return 列表的json
     */
    public String findByUser(String ybUserid,DetachedCriteria detachedCriteria);

    /**
     * 通过id查询用户要兑换的商品
     * @param id 商品id
     * @return 商品
     */
    public YibanProductGetEntity findById(Integer id);

    /**
     * 兑换后，更新产品状态
     * @param id 产品id
     */
    public void updateProduct(Integer id);
}
