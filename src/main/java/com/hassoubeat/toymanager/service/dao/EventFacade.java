/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hassoubeat.toymanager.service.dao;

import com.hassoubeat.toymanager.service.entity.Account;
import com.hassoubeat.toymanager.service.entity.Event;
import com.hassoubeat.toymanager.service.entity.Event_;
import com.hassoubeat.toymanager.service.entity.Toy;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 *
 * @author hassoubeat
 */
@Stateless
public class EventFacade extends AbstractFacade<Event> {

    @PersistenceContext(unitName = "com.hassoubeat_ToyManager_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EventFacade() {
        super(Event.class);
    }
    
    /**
     * 引数で受け取ったAccountIdのEventを全件取得するメソッド
     * @param accountId 検索したいAccoutId(Entity)
     * @return アカウントIDに紐づくEventの一覧
     */
    public List<Event> findByAccountId(Account accountId) {
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = builder.createQuery();
        Root<Event> charaRoot = cq.from(Event.class);
        cq.select(charaRoot);
        
        // WHERE条件の型と検索条件名(Entityの要素名)を指定する
        Predicate isEqual = builder.equal(charaRoot.get(Event_.accountId), accountId);
        cq.where(isEqual);
        
        return getEntityManager().createQuery(cq).getResultList();
    }
    
    /**
     * 引数で受け取ったToyIdのEventを全件取得するメソッド
     * @param toyId 検索したいToyId(Entity)
     * @return ToyIDに紐づくEventの一覧
     */
    public List<Event> findByToyId(Toy toyId) {
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = builder.createQuery();
        Root<Event> charaRoot = cq.from(Event.class);
        cq.select(charaRoot);
        
        // WHERE条件の型と検索条件名(Entityの要素名)を指定する
        Predicate isEqual = builder.equal(charaRoot.get(Event_.toyId), toyId);
        cq.where(isEqual);
        
        return getEntityManager().createQuery(cq).getResultList();
    }
    
    /**
     * 引数で受け取ったIdが存在するかを確認するメソッド
     * @param id
     * @return 合致件数
     */
    public int countById(int id) {
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = builder.createQuery();
        Root<Event> root = cq.from(Event.class);
        cq.select(builder.count(root));
        
        Predicate idEquals = builder.equal(root.get(Event_.id), id);
        cq.where(idEquals);
        
        Query query = getEntityManager().createQuery(cq);
        
        return ((Long) query.getSingleResult()).intValue();
    }
    
}
