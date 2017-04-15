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
import com.hassoubeat.toymanager.service.entity.ToyFacet;
import java.util.Date;
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
     * 引数で受け取ったToyIdのEventを全件取得するメソッド
     * @param toyId 検索したいToyId(Entity)
     * @return ToyIDに紐づくEventの一覧
     */
    public List<Event> findByToyId(Toy toyId) {
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = builder.createQuery();
        Root<Event> root = cq.from(Event.class);
        cq.select(root);
        
        // WHERE条件の型と検索条件名(Entityの要素名)を指定する
        Predicate isEqual = builder.equal(root.get(Event_.toyId), toyId);
        cq.where(isEqual);
        
        return getEntityManager().createQuery(cq).getResultList();
    }
    
    /**
     * 引数で受け取ったToyIdと開始時刻と終了時刻の間の非ループEventを全件取得するメソッド
     * @param toyId 検索したいToyId(Entity)
     * @param startDate
     * @param endDate
     * @return ToyIDに紐づくEventの一覧
     */
    public List<Event> findStandardEventByToyId(Toy toyId, Date startDate, Date endDate) {
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = builder.createQuery();
        Root<Event> root = cq.from(Event.class);
        cq.select(root);
        
        // WHERE条件の型と検索条件名(Entityの要素名)を指定する
        Predicate isToyIdEqual = builder.equal(root.get(Event_.toyId), toyId);
        Predicate isStandardEvent = builder.equal(root.get(Event_.roop), 0);
        Predicate isStartDateBetween = builder.between(root.get(Event_.startDate), startDate, endDate);
        cq.where(isToyIdEqual, isStandardEvent, isStartDateBetween);
        
        return getEntityManager().createQuery(cq).getResultList();
    }
    
    /**
     * 引数で受け取ったToyIdとループ終了時刻が指定した日にちより未来のループEventを全件取得するメソッド
     * @param toyId 検索したいToyId(Entity)
     * @param startDate
     * @return ToyIDに紐づくEventの一覧
     */
    public List<Event> findRoopEventByToyId(Toy toyId, Date startDate) {
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = builder.createQuery();
        Root<Event> root = cq.from(Event.class);
        cq.select(root);
        
        // WHERE条件の型と検索条件名(Entityの要素名)を指定する
        Predicate isToyIdEqual = builder.equal(root.get(Event_.toyId), toyId);
        Predicate isStandardEvent = builder.notEqual(root.get(Event_.roop), 0);
        Predicate isRoopEndDate = builder.greaterThanOrEqualTo(root.get(Event_.roopEndDate), startDate);
        cq.where(isToyIdEqual, isStandardEvent, isRoopEndDate);
        
        return getEntityManager().createQuery(cq).getResultList();
    }
    
    /**
     * 引数で受け取ったToyIdと開始時刻と終了時刻の間の非ループEventを全件取得するメソッド
     * ToyTalk用のため、ToyTalkフラグがTrueの情報のみ取得する
     * @param toyId 検索したいToyId(Entity)
     * @param startDate
     * @param endDate
     * @return ToyIDに紐づくEventの一覧
     */
    public List<Event> findStandardEventByToyIdForToyTalk(Toy toyId, Date startDate, Date endDate) {
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = builder.createQuery();
        Root<Event> root = cq.from(Event.class);
        cq.select(root);
        
        // WHERE条件の型と検索条件名(Entityの要素名)を指定する
        Predicate isToyIdEqual = builder.equal(root.get(Event_.toyId), toyId);
        Predicate isStandardEvent = builder.equal(root.get(Event_.roop), 0);
        Predicate isTalking = builder.equal(root.get(Event_.isTalking), true);
        Predicate isStartDateBetween = builder.between(root.get(Event_.startDate), startDate, endDate);
        cq.where(isToyIdEqual, isStandardEvent, isTalking,isStartDateBetween);
        
        return getEntityManager().createQuery(cq).getResultList();
    }
    
    /**
     * 引数で受け取ったToyIdとループ終了時刻が指定した日にちより未来のループEventを全件取得するメソッド
     * ToyTalk用のため、ToyTalkフラグがTrueの情報のみ取得する
     * @param toyId 検索したいToyId(Entity)
     * @param startDate
     * @return ToyIDに紐づくEventの一覧
     */
    public List<Event> findRoopEventByToyIdForToyTalk(Toy toyId, Date startDate) {
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = builder.createQuery();
        Root<Event> root = cq.from(Event.class);
        cq.select(root);
        
        // WHERE条件の型と検索条件名(Entityの要素名)を指定する
        Predicate isToyIdEqual = builder.equal(root.get(Event_.toyId), toyId);
        Predicate isStandardEvent = builder.notEqual(root.get(Event_.roop), 0);
        Predicate isTalking = builder.equal(root.get(Event_.isTalking), true);
        Predicate isRoopEndDate = builder.greaterThanOrEqualTo(root.get(Event_.roopEndDate), startDate);
        cq.where(isToyIdEqual, isStandardEvent, isTalking, isRoopEndDate);
        
        return getEntityManager().createQuery(cq).getResultList();
    }
    
    /**
     * 引数で受け取ったAccountIdのEventを全件取得するメソッド
     * @param accountId 検索したいAccoutId(Entity)
     * @return アカウントIDに紐づくEventの一覧
     */
    public List<Event> findByAccountId(Account accountId) {
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = builder.createQuery();
        Root<Event> root = cq.from(Event.class);
        cq.select(root);
        
        // WHERE条件の型と検索条件名(Entityの要素名)を指定する
        Predicate isEqual = builder.equal(root.get(Event_.accountId), accountId);
        cq.where(isEqual);
        
        return getEntityManager().createQuery(cq).getResultList();
    }
    
    /**
     * 引数で受け取ったAccountIdと開始時刻と終了時刻の間の非ループEventを全件取得するメソッド
     * @param accountId 検索したいAccountId(Entity)
     * @param startDate
     * @param endDate
     * @return ToyIDに紐づくEventの一覧
     */
    public List<Event> findStandardEventByAccountId(Account accountId, Date startDate, Date endDate) {
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = builder.createQuery();
        Root<Event> root = cq.from(Event.class);
        cq.select(root);
        
        // WHERE条件の型と検索条件名(Entityの要素名)を指定する
        Predicate isToyIdEqual = builder.equal(root.get(Event_.accountId), accountId);
        Predicate isStandardEvent = builder.equal(root.get(Event_.roop), 0);
        Predicate isStartDateBetween = builder.between(root.get(Event_.startDate), startDate, endDate);
        cq.where(isToyIdEqual, isStandardEvent, isStartDateBetween);
        
        return getEntityManager().createQuery(cq).getResultList();
    }
    
    /**
     * 引数で受け取ったAccountIdとループ終了時刻が指定した日にちより未来のループEventを全件取得するメソッド
     * @param accountId 検索したいAccountId(Entity)
     * @param startDate
     * @return ToyIDに紐づくEventの一覧
     */
    public List<Event> findRoopEventByAccountId(Account accountId, Date startDate) {
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = builder.createQuery();
        Root<Event> root = cq.from(Event.class);
        cq.select(root);
        
        // WHERE条件の型と検索条件名(Entityの要素名)を指定する
        Predicate isToyIdEqual = builder.equal(root.get(Event_.accountId), accountId);
        Predicate isStandardEvent = builder.notEqual(root.get(Event_.roop), 0);
        Predicate isRoopEndDate = builder.greaterThanOrEqualTo(root.get(Event_.roopEndDate), startDate);
        cq.where(isToyIdEqual, isStandardEvent, isRoopEndDate);
        
        return getEntityManager().createQuery(cq).getResultList();
    }
    
    /**
     * 引数で受け取ったAccountIdと開始時刻と終了時刻の間の非ループEventを全件取得するメソッド
     * ToyTalk用のため、ToyTalkフラグがTrueの情報のみ取得する
     * @param accountId 検索したいAccountId(Entity)
     * @param startDate
     * @param endDate
     * @return ToyIDに紐づくEventの一覧
     */
    public List<Event> findStandardEventByAccountIdForToyTalk(Account accountId, Date startDate, Date endDate) {
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = builder.createQuery();
        Root<Event> root = cq.from(Event.class);
        cq.select(root);
        
        // WHERE条件の型と検索条件名(Entityの要素名)を指定する
        Predicate isToyIdEqual = builder.equal(root.get(Event_.accountId), accountId);
        Predicate isStandardEvent = builder.equal(root.get(Event_.roop), 0);
        Predicate isTalking = builder.equal(root.get(Event_.isTalking), true);
        Predicate isStartDateBetween = builder.between(root.get(Event_.startDate), startDate, endDate);
        cq.where(isToyIdEqual, isStandardEvent, isTalking,isStartDateBetween);
        
        return getEntityManager().createQuery(cq).getResultList();
    }
    
    /**
     * 引数で受け取ったAccountIdとループ終了時刻が指定した日にちより未来のループEventを全件取得するメソッド
     * ToyTalk用のため、ToyTalkフラグがTrueの情報のみ取得する
     * @param accountId 検索したいAccountId(Entity)
     * @param startDate
     * @return ToyIDに紐づくEventの一覧
     */
    public List<Event> findRoopEventByAccountIdForToyTalk(Account accountId, Date startDate) {
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = builder.createQuery();
        Root<Event> root = cq.from(Event.class);
        cq.select(root);
        
        // WHERE条件の型と検索条件名(Entityの要素名)を指定する
        Predicate isToyIdEqual = builder.equal(root.get(Event_.accountId), accountId);
        Predicate isStandardEvent = builder.notEqual(root.get(Event_.roop), 0);
        Predicate isTalking = builder.equal(root.get(Event_.isTalking), true);
        Predicate isRoopEndDate = builder.greaterThanOrEqualTo(root.get(Event_.roopEndDate), startDate);
        cq.where(isToyIdEqual, isStandardEvent, isTalking, isRoopEndDate);
        
        return getEntityManager().createQuery(cq).getResultList();
    }
    
    /**
     * 引数で受け取ったToyFacetIdのEventを全件取得するメソッド
     * @param toyFacetId 検索したいToyFacetId(Entity)
     * @return ToyFacetIDに紐づくEventの一覧
     */
    public List<Event> findByToyFacetId(ToyFacet toyFacetId) {
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = builder.createQuery();
        Root<Event> root = cq.from(Event.class);
        cq.select(root);
        
        // WHERE条件の型と検索条件名(Entityの要素名)を指定する
        Predicate isEqual = builder.equal(root.get(Event_.toyFacetId), toyFacetId);
        cq.where(isEqual);
        
        return getEntityManager().createQuery(cq).getResultList();
    }
    
    /**
     * 引数で受け取ったToyFacetIdと開始時刻と終了時刻の間の非ループEventを全件取得するメソッド
     * @param toyFacetId 検索したいToyFacetId(Entity)
     * @param startDate
     * @param endDate
     * @return ToyFacetIDに紐づくEventの一覧
     */
    public List<Event> findStandardEventByToyFacetId(ToyFacet toyFacetId, Date startDate, Date endDate) {
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = builder.createQuery();
        Root<Event> root = cq.from(Event.class);
        cq.select(root);
        
        // WHERE条件の型と検索条件名(Entityの要素名)を指定する
        Predicate isToyFacetIdEqual = builder.equal(root.get(Event_.toyFacetId), toyFacetId);
        Predicate isStandardEvent = builder.equal(root.get(Event_.roop), 0);
        Predicate isStartDateBetween = builder.between(root.get(Event_.startDate), startDate, endDate);
        cq.where(isToyFacetIdEqual, isStandardEvent, isStartDateBetween);
        
        return getEntityManager().createQuery(cq).getResultList();
    }
    
    /**
     * 引数で受け取ったToyFacetIdとループ終了時刻が指定した日にちより未来のループEventを全件取得するメソッド
     * @param toyFacetId 検索したいToyFacetId(Entity)
     * @param startDate
     * @return ToyIDに紐づくEventの一覧
     */
    public List<Event> findRoopEventByToyFacetId(ToyFacet toyFacetId, Date startDate) {
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = builder.createQuery();
        Root<Event> root = cq.from(Event.class);
        cq.select(root);
        
        // WHERE条件の型と検索条件名(Entityの要素名)を指定する
        Predicate isToyFacetIdEqual = builder.equal(root.get(Event_.toyFacetId), toyFacetId);
        Predicate isStandardEvent = builder.notEqual(root.get(Event_.roop), 0);
        Predicate isRoopEndDate = builder.greaterThanOrEqualTo(root.get(Event_.roopEndDate), startDate);
        cq.where(isToyFacetIdEqual, isStandardEvent, isRoopEndDate);
        
        return getEntityManager().createQuery(cq).getResultList();
    }
    
    /**
     * 引数で受け取ったToyFacetIdと開始時刻と終了時刻の間の非ループEventを全件取得するメソッド
     * ToyTalk用のため、ToyTalkフラグがTrueの情報のみ取得する
     * @param toyFacetId 検索したいToyFacetId(Entity)
     * @param startDate
     * @param endDate
     * @return ToyFacetIDに紐づくEventの一覧
     */
    public List<Event> findStandardEventByToyFacetIdForToyTalk(ToyFacet toyFacetId, Date startDate, Date endDate) {
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = builder.createQuery();
        Root<Event> root = cq.from(Event.class);
        cq.select(root);
        
        // WHERE条件の型と検索条件名(Entityの要素名)を指定する
        Predicate isToyFacetIdEqual = builder.equal(root.get(Event_.toyFacetId), toyFacetId);
        Predicate isStandardEvent = builder.equal(root.get(Event_.roop), 0);
        Predicate isTalking = builder.equal(root.get(Event_.isTalking), true);
        Predicate isStartDateBetween = builder.between(root.get(Event_.startDate), startDate, endDate);
        cq.where(isToyFacetIdEqual, isStandardEvent, isTalking,isStartDateBetween);
        
        return getEntityManager().createQuery(cq).getResultList();
    }
    
    /**
     * 引数で受け取ったToyFacetIdとループ終了時刻が指定した日にちより未来のループEventを全件取得するメソッド
     * ToyTalk用のため、ToyTalkフラグがTrueの情報のみ取得する
     * @param toyFacetId 検索したいToyFacetId(Entity)
     * @param startDate
     * @return ToyIDに紐づくEventの一覧
     */
    public List<Event> findRoopEventByToyFacetIdForToyTalk(ToyFacet toyFacetId, Date startDate) {
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = builder.createQuery();
        Root<Event> root = cq.from(Event.class);
        cq.select(root);
        
        // WHERE条件の型と検索条件名(Entityの要素名)を指定する
        Predicate isToyFacetIdEqual = builder.equal(root.get(Event_.toyFacetId), toyFacetId);
        Predicate isStandardEvent = builder.notEqual(root.get(Event_.roop), 0);
        Predicate isTalking = builder.equal(root.get(Event_.isTalking), true);
        Predicate isRoopEndDate = builder.greaterThanOrEqualTo(root.get(Event_.roopEndDate), startDate);
        cq.where(isToyFacetIdEqual, isStandardEvent, isTalking, isRoopEndDate);
        
        return getEntityManager().createQuery(cq).getResultList();
    }
    
    /**
     * 
     * @param toyFacetId 
     */
    public void removeByToyFacetId(ToyFacet toyFacetId) {
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaDelete<Event> delete = builder.createCriteriaDelete(Event.class);
        Root<Event> root = delete.from(Event.class);

        delete.where(builder.equal(root.get(Event_.toyFacetId), toyFacetId));

        Query q = getEntityManager().createQuery(delete);
        q.executeUpdate();
        
        this.flush();
        this.clear();
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
