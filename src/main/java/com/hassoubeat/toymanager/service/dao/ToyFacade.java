/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hassoubeat.toymanager.service.dao;

import com.hassoubeat.toymanager.service.entity.Toy;
import com.hassoubeat.toymanager.service.entity.Toy_;
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
public class ToyFacade extends AbstractFacade<Toy> {

    @PersistenceContext(unitName = "com.hassoubeat_ToyManager_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ToyFacade() {
        super(Toy.class);
    }
    
    /**
     * 引数で受け取ったロットナンバーとパスワードを持つカラムが何件存在するかを取得するメソッド
     * @param rotNumber
     * @param password
     * @return 合致件数
     */
    public int countByRotNumberAndPassword(int rotNumber, String password) {
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = builder.createQuery();
        Root<Toy> root = cq.from(Toy.class);
        cq.select(builder.count(root));
        
        Predicate rotNumEquals = builder.equal(root.get(Toy_.rotNum), rotNumber);
        Predicate passwordEquals = builder.equal(root.get(Toy_.tyingPassword), password);
        cq.where(rotNumEquals);
        cq.where(passwordEquals);
        
        Query query = getEntityManager().createQuery(cq);
        
        return ((Long) query.getSingleResult()).intValue();
    }
    
    /**
     * 引数で受け取ったロットナンバーで検索するメソッド
     * @param rotNumber
     * @return toy 合致したToy情報
     */
    public Toy findByRotNumber(int rotNumber) {
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = builder.createQuery();
        Root<Toy> root = cq.from(Toy.class);
        cq.select(root);
        
        Predicate userIdEquals = builder.equal(root.get(Toy_.rotNum), rotNumber);
        cq.where(userIdEquals);
        
        Query query = getEntityManager().createQuery(cq);
        query.setFirstResult(0);
        query.setMaxResults(0);
        
        
        return (Toy) query.getSingleResult();
    }
    
    
    
}
