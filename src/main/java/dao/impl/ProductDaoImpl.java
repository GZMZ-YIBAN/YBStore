package dao.impl;

import dao.Base.impl.BaseDaoImpl;
import dao.IProductDao;
import entity.YibanProductEntity;
import entity.YibanProductGetEntity;
import org.hibernate.Session;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;

import javax.persistence.criteria.CriteriaQuery;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author EchoLZY
 * @date 13:32 2018/4/14
 * @description
 * @modified By EchoLZY
 */
public class ProductDaoImpl extends BaseDaoImpl<YibanProductEntity> implements IProductDao {


    public ProductDaoImpl() {
        super(YibanProductEntity.class);
    }
}
