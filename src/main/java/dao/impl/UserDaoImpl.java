package dao.impl;

import dao.Base.impl.BaseDaoImpl;
import dao.IUserDao;
import entity.YibanProductGetEntity;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;

import javax.persistence.criteria.CriteriaQuery;
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
 * @date 18:29 2018/4/15
 * @modified By EchoLZY
 */
public class UserDaoImpl extends BaseDaoImpl<YibanProductGetEntity> implements IUserDao {
    public UserDaoImpl() {
        super(YibanProductGetEntity.class);
    }
}
