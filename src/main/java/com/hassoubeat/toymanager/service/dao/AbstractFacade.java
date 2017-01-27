/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hassoubeat.toymanager.service.dao;

import com.hassoubeat.toymanager.util.ParamConst;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author hassoubeat
 */
public abstract class AbstractFacade<T>{

    private Class<T> entityClass;

    public AbstractFacade(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    protected abstract EntityManager getEntityManager();

    public T create(T entity) {
        getEntityManager().persist(entity);
        
        //強制的にINSERTする
        this.flush(); 
        this.clear();
        
        return entity;
    }

    public T edit(T entity) {
        getEntityManager().merge(entity);
        
        //強制的にUPDATEする
        this.flush();
        this.clear();
        
        return entity;
    }

    public T remove(T entity) {
        getEntityManager().remove(getEntityManager().merge(entity));
        
        //強制的にDELETEする
        this.flush();
        this.clear();
        
        return entity;
    }

    public T find(Object id) {
        T entity = getEntityManager().find(entityClass, id);
        
        // 既に永続性コンテキスト(キャッシュ)にエンティティが存在した場合、キャッシュをDBから取得した値で更新する (更新後に再度ページを確認しようとすると、前のキャッシュを読んでしまうため)
        if (this.isEntityCached(entity)) {
            this.refresh(entity);
        }
        return entity;
    }

    public List<T> findAll() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        return getEntityManager().createQuery(cq).getResultList();
    }

    public List<T> findRange(int[] range) {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        q.setMaxResults(range[1] - range[0] + 1);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        javax.persistence.criteria.Root<T> rt = cq.from(entityClass);
        cq.select(getEntityManager().getCriteriaBuilder().count(rt));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }
    
    /**
     * 引数で受け取った順番で登録日(CreateDate)でソートし、全件取得するメソッド
     * @param order  "ASC","DESC"を持ち、ソートしたい順を指定する
     * @return 登録日でソートされたEntityのリスト
     */
    public List<T> findAllByOrderByCreateDate(String order) {
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = builder.createQuery();
        Root<T> root = cq.from(entityClass);
        cq.select(root);
        
        switch (order) {
        // 昇順指定
            case "ASC":
                cq.orderBy(builder.asc(root.get(ParamConst.WHERE_CREATE_DATE)));
                break;
        // 降順指定
            case "DESC":
                cq.orderBy(builder.desc(root.get(ParamConst.WHERE_CREATE_DATE)));
                break;
        // TODO どちらでもない時はExceptionをスローする
            default:
                break;
        }
        
        return getEntityManager().createQuery(cq).getResultList();
    }
    
    /**
     * 引数で受け取った順番で更新日(EditDate)でソートし、全件取得するメソッド
     * @param order  "ASC","DESC"を持ち、ソートしたい順を指定する
     * @return 更新日でソートされたEntityのリスト
     */
    public List<T> findAllByOrderByEditDate(String order) {
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = builder.createQuery();
        Root<T> root = cq.from(entityClass);
        cq.select(root);
        
        switch (order) {
        // 昇順指定
            case "ASC":
                cq.orderBy(builder.asc(root.get(ParamConst.WHERE_EDIT_DATE)));
                break;
        // 降順指定
            case "DESC":
                cq.orderBy(builder.desc(root.get(ParamConst.WHERE_EDIT_DATE)));
                break;
        // TODO どちらでもない時はExceptionをスローする
            default:
                break;
        }
        
        return getEntityManager().createQuery(cq).getResultList();
    }
    
    /**
     * 引数で受け取った順番で登録日(CreateDate)でソートし、指定した範囲のエンティティを取得するメソッド
     * @param range  取得したい範囲を指定する
     * @param order "ASC","DESC"を持ち、ソートしたい順を指定する
     * @return 登録日でソートされたEntityのリスト
     */
    public List<T> findRangeOrderByCreateDate(int[] range, String order) {
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery cq = builder.createQuery();
        Root<T> root = cq.from(entityClass);
        cq.select(root);
        
        switch (order) {
        // 昇順指定
            case "ASC":
                cq.orderBy(builder.asc(root.get(ParamConst.WHERE_CREATE_DATE)));
                break;
        // 降順指定
            case "DESC":
                cq.orderBy(builder.desc(root.get(ParamConst.WHERE_CREATE_DATE)));
                break;
        // TODO どちらでもない時はExceptionをスローする
            default:
                break;
        }
        
        Query q = getEntityManager().createQuery(cq);
        q.setMaxResults(range[1] - range[0] + 1);
        q.setFirstResult(range[0]);
        
        return q.getResultList();
    }
    
    /**
     * 引数で受け取った順番で更新日(EditDate)でソートし、指定した範囲のエンティティを取得するメソッド
     * @param range  取得したい範囲を指定する
     * @param order "ASC","DESC"を持ち、ソートしたい順を指定する
     * @return 登録日でソートされたEntityのリスト
     */
    public List<T> findRangeOrderByEditDate(int[] range, String order) {
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery cq = builder.createQuery();
        Root<T> root = cq.from(entityClass);
        cq.select(root);
        
        switch (order) {
        // 昇順指定
            case "ASC":
                cq.orderBy(builder.asc(root.get(ParamConst.WHERE_EDIT_DATE)));
                break;
        // 降順指定
            case "DESC":
                cq.orderBy(builder.desc(root.get(ParamConst.WHERE_EDIT_DATE)));
                break;
        // TODO どちらでもない時はExceptionをスローする
            default:
                break;
        }
        
        Query q = getEntityManager().createQuery(cq);
        q.setMaxResults(range[1] - range[0] + 1);
        q.setFirstResult(range[0]);
        
        return q.getResultList();
    }
    
    // 現在の永続的コンテキスト内の全データを即時DBに反映する
    public void flush() {
        getEntityManager().flush();
    }
    
    // 現在の永続的コンテキスト内の全データを永続的コンテキストから分離する
    public void clear() {
        getEntityManager().clear();
    }
    
    // 引数で渡したエンティティが永続的コンテキスト内(キャッシュ)として存在するかをチェックする
    public boolean isEntityCached(T entity) {
        return getEntityManager().contains(entity);
    }
    
    // 引数で渡したエンティティを永続的コンテキスト内(キャッシュ)から分離する
    public void detach(T entity) {
        getEntityManager().detach(entity);
    }
    
    // 引数で渡した永続的コンテキスト内(キャッシュ)のエンティティをデータベース内に存在する値で更新する
    public void refresh(T entity) {
        getEntityManager().refresh(entity);
    }
    
}
