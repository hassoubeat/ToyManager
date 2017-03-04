/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hassoubeat.toymanager.service.dao;

import com.hassoubeat.toymanager.service.entity.Account;
import com.hassoubeat.toymanager.service.entity.DiffSyncEvent;
import com.hassoubeat.toymanager.service.entity.DiffSyncEvent_;
import com.hassoubeat.toymanager.service.entity.Event;
import com.hassoubeat.toymanager.service.entity.Event_;
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
public class DiffSyncEventFacade extends AbstractFacade<DiffSyncEvent> {

    @PersistenceContext(unitName = "com.hassoubeat_ToyManager_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DiffSyncEventFacade() {
        super(DiffSyncEvent.class);
    }
    
    /**
     * 引数で受け取ったEventIdの持つカラムの件数を取得する
     * @param eventId 検索したメソッドEventId(Entity)
     * @return 合致したカラムの件数
     */
    public int countByEventId(Event eventId) {
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = builder.createQuery();
        Root<DiffSyncEvent> root = cq.from(DiffSyncEvent.class);
        cq.select(builder.count(root));
        
        // WHERE条件の型と検索条件名(Entityの要素名)を指定する
        Predicate isEqual = builder.equal(root.get(DiffSyncEvent_.eventId), eventId);
        cq.where(isEqual);
        
        Query query = getEntityManager().createQuery(cq);        
        
        return ((Long) query.getSingleResult()).intValue();
    }
    
    /**
     * 引数で受け取ったEventIdのDiffSyncEventを全件取得するメソッド
     * @param eventId 検索したいEventId(Entity)
     * @return イベントIDに紐づくEventの一覧
     */
    public List<DiffSyncEvent> findByEventId(Event eventId) {
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = builder.createQuery();
        Root<DiffSyncEvent> root = cq.from(DiffSyncEvent.class);
        cq.select(root);
        
        // WHERE条件の型と検索条件名(Entityの要素名)を指定する
        Predicate isEqual = builder.equal(root.get(DiffSyncEvent_.eventId), eventId);
        cq.where(isEqual);
        
        return getEntityManager().createQuery(cq).getResultList();
    }
    
}
