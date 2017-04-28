/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hassoubeat.toymanager.service.dao;

import com.hassoubeat.toymanager.service.entity.Account;
import com.hassoubeat.toymanager.service.entity.Account_;
import com.hassoubeat.toymanager.service.entity.Event;
import com.hassoubeat.toymanager.service.entity.Event_;
import com.hassoubeat.toymanager.service.entity.Toy;
import com.hassoubeat.toymanager.service.entity.ToyWebapiAccessFilter;
import com.hassoubeat.toymanager.service.entity.ToyWebapiAccessFilter_;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 *
 * @author hassoubeat
 */
@Stateless
public class ToyWebapiAccessFilterFacade extends AbstractFacade<ToyWebapiAccessFilter> {

    @PersistenceContext(unitName = "com.hassoubeat_ToyManager_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ToyWebapiAccessFilterFacade() {
        super(ToyWebapiAccessFilter.class);
    }
    
    /**
     * 引数で受け取ったToyIdのエンティティを全件取得するメソッド
     * @param toyId 検索したいToyId(Entity)
     * @return ToyIDに紐づくエンティティの一覧
     */
    public List<Event> findByToyId(Toy toyId) {
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = builder.createQuery();
        Root<ToyWebapiAccessFilter> root = cq.from(ToyWebapiAccessFilter.class);
        cq.select(root);
        
        // WHERE条件の型と検索条件名(Entityの要素名)を指定する
        Predicate isEqual = builder.equal(root.get(ToyWebapiAccessFilter_.toyId), toyId);
        cq.where(isEqual);
        
        return getEntityManager().createQuery(cq).getResultList();
    }
    
    /**
     * 引数で受け取ったMacAdressのカラムが存在するかをチェックするメソッド
     * @param macAddress
     * @return 合致件数
     */
    public boolean isExistByMacAddress(String macAddress) {
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = builder.createQuery();
        Root<ToyWebapiAccessFilter> root = cq.from(ToyWebapiAccessFilter.class);
        cq.select(builder.count(root));
        
        Predicate userIdEquals = builder.equal(root.get(ToyWebapiAccessFilter_.macAddress), macAddress);
        cq.where(userIdEquals);
        
        Query query = getEntityManager().createQuery(cq);
        
        if(((Long) query.getSingleResult()).intValue() > 0) {
            // データが存在した場合
            return true;
        } else {
            // データが存在しなかった場合
            return false;
        }
         
    }
    
   /**
    * Toyに紐づくするフィルターを削除する
    * 
    * @param toyId 
    */
    public void removeByToyId(Toy toyId) {
       CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
       CriteriaDelete<ToyWebapiAccessFilter> delete = builder.createCriteriaDelete(ToyWebapiAccessFilter.class);
       Root<ToyWebapiAccessFilter> root = delete.from(ToyWebapiAccessFilter.class);

       delete.where(builder.equal(root.get(ToyWebapiAccessFilter_.toyId), toyId));

       Query q = getEntityManager().createQuery(delete);
       q.executeUpdate();

       this.flush();
       this.clear();
   }
}
