package service;

import entity.YibanProductEntity;
import entity.YibanProductOrderEntity;
import org.hibernate.criterion.DetachedCriteria;

import java.util.List;

/**
 * @author EchoLZY
 * @date 14:19 2018/4/14
 * @description
 * 商品服务的接口
 * @modified By EchoLZY
 */
public interface IProductService {
    /**
     * 查询所有商品同时存入或更新缓存
     * @return  所有产品
     */
    public List<YibanProductEntity> findAll();

    /**
     * 由id查询单个商品
     * @param id id
     * @return 商品
     */
    public YibanProductEntity findById(Integer id);



//    public void savePay(YibanProductEntity yibanProductEntity, String userId, String userName, String sex,String phone);
    /**
     * 支付成功后将商品信息存入用户购买库中，并更新订单状态
     * @param yibanProductOrderEntity 订单信息
     */
    public void savePsy(YibanProductOrderEntity yibanProductOrderEntity);

}
