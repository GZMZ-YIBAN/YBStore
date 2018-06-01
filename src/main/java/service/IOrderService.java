package service;

import entity.YibanProductOrderEntity;

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
 * @date 20:58 2018/4/25
 * @modified By EchoLZY
 */
public interface IOrderService {
    /**
     * 创建订单
     * @param id  商品id
     * @param tradeId 订单ID
     * @param userId 用户id
     * @param userName 用户名
     * @param sex 用户性别
     * @param phone 用户电话
     * @param access_token 用户的授权信息
     */
    public void saveOrder(Integer id, String tradeId,String userId,String userName,String sex,String phone,String access_token);


    /**
     * 通过订单号查找订单
     * @param tradeId 订单号
     * @return 订单
     */
    public YibanProductOrderEntity findByTradeId(String tradeId);

}
