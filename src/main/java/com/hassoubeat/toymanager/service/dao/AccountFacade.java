/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hassoubeat.toymanager.service.dao;

import com.hassoubeat.toymanager.service.entity.Account;
import com.hassoubeat.toymanager.service.entity.Account_;
import java.sql.SQLException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
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
public class AccountFacade extends AbstractFacade<Account> {

    @PersistenceContext(unitName = "com.hassoubeat_ToyManager_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AccountFacade() {
        super(Account.class);
    }
    
    /**
     * 引数で受け取ったUserIdが何件存在するかを取得するメソッド
     * @param userId
     * @return 合致件数
     */
    public int countByUserId(String userId) {
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = builder.createQuery();
        Root<Account> root = cq.from(Account.class);
        cq.select(builder.count(root));
        
        Predicate userIdEquals = builder.equal(root.get(Account_.userId), userId);
        cq.where(userIdEquals);
        
        Query query = getEntityManager().createQuery(cq);
        
        return ((Long) query.getSingleResult()).intValue();
    }
    
    /**
     * 引数で受け取ったUserIdで検索するメソッド
     * @param userId
     * @return account 合致したアカウント情報
     * @throws java.sql.SQLException
     */
    public Account findByUserId(String userId) throws SQLException {
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = builder.createQuery();
        Root<Account> root = cq.from(Account.class);
        cq.select(root);
        
        Predicate userIdEquals = builder.equal(root.get(Account_.userId), userId);
        cq.where(userIdEquals);
        
        Query query = getEntityManager().createQuery(cq);
        query.setFirstResult(0);
        query.setMaxResults(0);
        
        try {
            return (Account) query.getSingleResult();
        } catch (NoResultException ex) {
            // TODO メッセージを考える
            throw new SQLException("", ex);
        }
        
        
        
    }
    
    
}
