package dao.impl;

import dao.Base.impl.BaseDaoImpl;
import dao.IOrderDao;
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
 * @date 20:23 2018/4/25
 * @modified By EchoLZY
 */
public class OrderDaoImpl extends BaseDaoImpl<YibanProductOrderEntity> implements IOrderDao {

    public OrderDaoImpl() {
        super(YibanProductOrderEntity.class);
    }
}
