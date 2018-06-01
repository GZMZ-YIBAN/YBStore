package service.impl;

import dao.IOrderDao;
import dao.IProductDao;
import dao.impl.OrderDaoImpl;
import dao.impl.ProductDaoImpl;
import entity.YibanProductEntity;
import entity.YibanProductOrderEntity;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import service.IOrderService;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
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
 * @date 20:59 2018/4/25
 * @modified By EchoLZY
 */
public class OrderServiceImpl implements IOrderService {
    private IProductDao productDao;
    private IOrderDao orderDao;
    @Override
    public void saveOrder(Integer id, String tradeId, String userId, String userName, String sex,String phone,String access_token) {
        YibanProductEntity yibanProductEntity = productDao.findById(id);
        Integer productId = yibanProductEntity.getId();
        YibanProductOrderEntity yibanProductOrderEntity = new YibanProductOrderEntity(
                productId.toString(),tradeId,userId,userName,sex,phone,access_token,"0",
                Timestamp.valueOf(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
        );
        orderDao.save(yibanProductOrderEntity);
    }

    @Override
    public YibanProductOrderEntity findByTradeId(String tradeId) {
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(YibanProductOrderEntity.class);
        detachedCriteria.add(Restrictions.like("tradeId",tradeId));
        return orderDao.findByDeta(detachedCriteria).get(0);
    }

    public void setOrderDao(OrderDaoImpl orderDao) {
        this.orderDao = orderDao;
    }

    public void setProductDao(IProductDao productDao) {
        this.productDao = productDao;
    }
}
