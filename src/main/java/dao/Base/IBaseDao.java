package dao.Base;

import org.hibernate.criterion.DetachedCriteria;

import java.io.Serializable;
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
 * @date 19:48 2018/4/25
 * @modified By EchoLZY
 */
public interface IBaseDao<T> {
    /**
     * 保存
     * @param entity 对象
     */
    public void save(T entity);

    /**
     * 删除
     * @param entity 对象
     */
    public void delete(T entity);

    /**
     * 更新
     * @param entity 对象
     */
    public void update(T entity);

    /**
     * 保存或更新
     * @param entity 对象
     */
    public void saveOrUpdate(T entity);

    /**
     * 通过 id 查询
     * @param id id
     * @return 查询到的对象
     */
    public T findById(Serializable id);

    /**
     * 查询所有
     * @return 集合
     */
    public List<T> findAll();


    public List<T> findByDeta(DetachedCriteria detachedCriteria);
}
