package dao.Base.impl;

import dao.Base.IBaseDao;

import entity.YibanProductEntity;
import entity.YibanProductGetEntity;
import entity.YibanProductOrderEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;
import utils.DoUtil;

import javax.persistence.criteria.CriteriaQuery;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
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
 * @date 19:49 2018/4/25
 * @modified By EchoLZY
 */
public class BaseDaoImpl<T> extends HibernateDaoSupport implements IBaseDao<T> {
    private static Logger logger = LogManager.getLogger(BaseDaoImpl.class.getName());
    private Class<T> entityClass;

    public BaseDaoImpl(Class<T> entityClass){
        logger.info("获取到泛型");
        this.entityClass = entityClass;
    }

    public BaseDaoImpl() {
    }

    @Override
    public void save(T entity) {
        logger.info("存放数据" + entityClass.getSimpleName());
        this.getHibernateTemplate().save(entity);
    }

    @Override
    public void delete(T entity) {
        logger.info("删除数据" + entityClass.getSimpleName());
        this.getHibernateTemplate().delete(entity);
    }

    @Override
    public void update(T entity) {
        logger.info("更新数据" + entityClass.getSimpleName());
        this.getHibernateTemplate().update(entity);
    }

    @Override
    public void saveOrUpdate(T entity) {
        logger.info("保存或更新数据" + entityClass.getSimpleName());
        this.getHibernateTemplate().saveOrUpdate(entity);
    }

    @Override
    public T findById(Serializable id) {
        logger.info("根据id查询" + id + entityClass.getSimpleName());
        return this.getHibernateTemplate().get(entityClass, id);
    }

    @Override
    public List<T> findAll() {
        logger.info("查询所有" + entityClass.getSimpleName());
        List<T> list = null;
        try {
            Session session = getSessionFactory().openSession();
            CriteriaQuery<T> criteriaQuery = session.getCriteriaBuilder().createQuery(entityClass);
            criteriaQuery.from(entityClass);
            list = session.createQuery(criteriaQuery).list();
            session.close();
        } catch (Exception e){
            logger.error("findAll Exception" + e.getMessage());
            logger.fatal("异常详细信息：" + DoUtil.getExceptionInfo(e));
        }
        return list;
    }

    @Override
    public List<T> findByDeta(DetachedCriteria detachedCriteria) {
        logger.info("离线查询");
        List<T> list = null;
        try (
            Session session = getHibernateTemplate().getSessionFactory().openSession()) {
            list = detachedCriteria.getExecutableCriteria(session).list();
            session.close();
        } catch (Exception e) {
            logger.error("DetachedCriteria Exception" + e.getMessage());
            logger.fatal("异常详细信息：" + DoUtil.getExceptionInfo(e));
        }
        return list;
    }

    public void setMySessionFactory(SessionFactory sessionFactory){
        super.setSessionFactory(sessionFactory);
    }
}
