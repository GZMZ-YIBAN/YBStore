package service.impl;

import com.alibaba.fastjson.JSONObject;
import dao.IOrderDao;
import dao.IProductDao;
import dao.IUserDao;
import entity.YibanProductEntity;
import entity.YibanProductGetEntity;
import entity.YibanProductOrderEntity;
import service.IProductService;
import utils.redis.RedisUtil;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author EchoLZY
 * @date 14:19 2018/4/14
 * @description
 * 用于处理服务的实现类
 * @modified By EchoLZY
 * @version 2.0
 */
public class ProductServiceImpl implements IProductService {
    private IProductDao productDao;
    private IOrderDao orderDao;
    private IUserDao userDao;
    private RedisUtil redisUtil;
    @Override
    public List<YibanProductEntity> findAll() {
        List<YibanProductEntity> list = productDao.findAll();
        redisUtil.set("YBStore_product", JSONObject.toJSON(list).toString());
        return list;
    }

    @Override
    public YibanProductEntity findById(Integer id) {
        return productDao.findById(id);
    }

    @Override
    public void savePsy(YibanProductOrderEntity yibanProductOrderEntity) {
        YibanProductEntity yibanProductEntity = productDao.findById(Integer.parseInt(yibanProductOrderEntity.getProductId()));
        yibanProductEntity.setNumber(yibanProductEntity.getNumber() - 1);
        productDao.update(yibanProductEntity);
        YibanProductGetEntity yibanProductGetEntity = new YibanProductGetEntity(
                yibanProductOrderEntity.getUserName(),yibanProductOrderEntity.getUserId(),
                yibanProductOrderEntity.getSex(),yibanProductOrderEntity.getPhone(),
                yibanProductEntity.getPrice(),1,yibanProductEntity.getPhoto(),0,
                yibanProductEntity.getName(),Timestamp.valueOf(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
        );
        userDao.save(yibanProductGetEntity);
        yibanProductOrderEntity.setEnable("1");
        orderDao.update(yibanProductOrderEntity);
    }

    public void setProductDao(IProductDao productDao) {
        this.productDao = productDao;
    }

    public void setRedisUtil(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    public void setOrderDao(IOrderDao orderDao) {
        this.orderDao = orderDao;
    }

    public void setUserDao(IUserDao userDao) {
        this.userDao = userDao;
    }
}
