/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hassoubeat.toymanager.service.logic;

import com.hassoubeat.toymanager.constant.MessageConst;
import com.hassoubeat.toymanager.service.dao.AccountFacade;
import com.hassoubeat.toymanager.service.dao.DiffSyncEventFacade;
import com.hassoubeat.toymanager.service.dao.EventFacade;
import com.hassoubeat.toymanager.service.dao.ToyFacade;
import com.hassoubeat.toymanager.service.entity.Account;
import com.hassoubeat.toymanager.service.entity.Event;
import com.hassoubeat.toymanager.service.entity.Toy;
import com.hassoubeat.toymanager.web.backingbean.session.SessionBean;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;

/**
 *
 * @author hassoubeat
 */
@Stateless
public class ToyLogic{
    
    @Inject
    Logger logger;
    
    @Inject
    SessionBean sessionBean;
    
    @EJB
    AccountFacade accountFacade;
    
    @EJB
    ToyFacade toyFacade;
    
    @EJB
    EventFacade eventFacade;
    
    @EJB
    DiffSyncEventFacade diffSyncEventFacade;

    public ToyLogic() {
    }
    
    /**
     * Toyのアカウント紐付け処理を実行する
     * 
     * @param toy 紐付け処理をするToyエンティティ
     */
    public void accountTying(Toy toy) {
        
        // アクセストークンの生成(アカウントID + ToyID + 現在時刻をソルトでハッシュ化)
        toy = accessTokenGen(toy);
        
        toyFacade.edit(toy);

        // TODO 差分イベントの扱いが決定した後に差分イベントの登録処理を実装
        
        logger.info("{}:{}, USER_ID:{}, TOY_ID:{}, {}", MessageConst.SUCCESS_TOY_ACCOUNT_TYING.getId(), MessageConst.SUCCESS_TOY_ACCOUNT_TYING.getMessage(), sessionBean.getUserId(), toy.getId(), this.getClass().getName() + "." + this.getClass());
        
    }
    
    /**
     * Toyのアカウント紐付け解除処理を実行する
     * 
     * @param toy 紐付け解除処理をするToyエンティティ
     */
    public void accountTyingCancel(Toy toy) {
        
        // Toyのパラメータをセット
        toy.setName("");
        toy.setAccessToken(null);
        toy.setAccessTokenSalt(null);
        toy.setAccessTokenLifecycle(null);
        toy.setLastSyncDate(null);
        toy.setAccountId(null);
        // TODO Toyのデフォルトの画像に初期化する処理を追加
//        toy.setPictureUrl(null);
        // TODO Toyのデフォルトのボイスタイプに初期化する処理を追加
//        toy.setToyVoiceTypeId(null);
        
        
        Toy editToy = toyFacade.edit(toy);
        
        // Toyに紐づくイベントを全て削除する
        for (Event targetEvent: editToy.getEventList()) {
            eventFacade.remove(targetEvent);
        }
        
        // TODO 差分イベントの削除
//        for (DiffSyncEvent targetDiffSyncEvent: editToy.getDiffSyncEventList()) {
//            diffSyncEventFacade.remove(targetDiffSyncEvent);
//        }
        
        // セッションの現在選択中ToyIdと最終選択ToyIDをクリアする
        sessionBean.setSelectedToyId(0);
        Account targetAccount = accountFacade.find(sessionBean.getId());
        targetAccount.setLastSelectedToyId(0);
        accountFacade.edit(targetAccount);
        
        
        
        logger.info("{}:{}, USER_ID:{}, TOY_ID:{}, {}", MessageConst.SUCCESS_TOY_ACCOUNT_TYING_CANCEL.getId(), MessageConst.SUCCESS_TOY_ACCOUNT_TYING_CANCEL.getMessage(), sessionBean.getUserId(), toy.getId(), this.getClass().getName() + "." + this.getClass());
        
    }
    
    /**
     * イベントの変更処理を実行する
     * 
     * @param event 変更するイベントエンティティ
     */
    public void edit(Event event) {        
//        // 日付間隔/曜日設定のチェック(指定なしの時に値を初期化する)
//        if (bitLogic.bitCheck(event.getRoop(), erpConst.IS_ROOP) ) {
//            // ループが有効な場合、ループ値のチェックを行う
//            event = this.eventCheck(event);
//        }
//        
//        // イベントの登録
//        Event editEvent = eventFacade.edit(event);
//        
//        
//        // TODO 差分イベント登録処理
//        
//        // 既に今回変更したイベントIDのカラムがあった場合は、変更カウントで数える
//        // TODO 変更前通常イベント→変更後アカウント共有イベント
//        // TODO 変更前アカウント共有イベント→変更後通常イベント
//        // TODO countByEventIdメソッドではアカウント共有イベントを特定できないため、合わせてToyIDも利用して検索する処理を設定する
//        int count = diffSyncEventFacade.countByEventId(editEvent);
//        DiffSyncEvent editDiffEvent = new DiffSyncEvent();
//        if (count > 0) {
//            // Diffイベントがあった場合(新しく作成したEventがまだToy側に同期されていなかった場合)
//            editDiffEvent = diffSyncEventFacade.findByEventId(event).get(0);
//        } else {
//            // Diffイベントがなかった(既にToy側に同期されていた場合)
//            // 差分イベントの登録
//            DiffSyncEvent diffEvent = new DiffSyncEvent();
//            diffEvent.setToyId(toyFacade.find(sessionBean.getSelectedToyId()));
//            diffEvent.setEventId(editEvent);
//            diffEvent.setMethodType(MethodTypeConst.UPDATE.toString());
//            editDiffEvent  = diffSyncEventFacade.create(diffEvent);
//        }
//        
//        logger.info("{} : USER_ID:{}, EVENT_ID:{}, DIFF_SYNC_EVENT_ID:{} {}.{}", MessageConst.SUCCESS_EVENT_EDIT.getId() + ":" + MessageConst.SUCCESS_EVENT_EDIT.getMessage(), sessionBean.getUserId(), editEvent.getId(), editDiffEvent.getId(),this.getClass().getName() + "." + this.getClass());
    }
    
    /**
     * イベントの削除処理(場合によっては論理削除)を行う
     * 
     * @param event 削除するイベントエンティティ
     * @return 
     */
    public MessageConst remove(Event event) {
//        
//        // 既に今回論理削除したイベントIDのカラムが存在するか確認する
//        int count = diffSyncEventFacade.countByEventId(event);
//        if (count > 0) {
//            // Diffイベントがあった場合(新しく作成したEventがまだToy側に同期されていなかった場合)
//            
//            // イベントの削除(一緒に差分イベントも消える)
//            Event removeEvent = eventFacade.remove(event);            
//        
//            logger.info("{} : USER_ID:{}, EVENT_ID:{}, DIFF_SYNC_EVENT_ID:{} {}.{}", MessageConst.SUCCESS_EVENT_REMOVE.getId() + ":" + MessageConst.SUCCESS_EVENT_REMOVE.getMessage(), sessionBean.getUserId(), removeEvent.getId(), removeEvent.getDiffSyncEventList().get(0).getId(),this.getClass().getName() + "." + this.getClass());
//            
//            return MessageConst.SUCCESS_EVENT_REMOVE;
//            
//        } else {
//            // Diffイベントがなかった(既にToy側に同期されていた場合)
//            
//            // イベントの論理削除(Toy側の削除の完了を受けた際に完全な削除を実施する)
//            event.setIsDeleted(true);
//            Event logicRemoveEvent = eventFacade.edit(event);
//            
//            // 差分イベントの登録
//            DiffSyncEvent diffEvent = new DiffSyncEvent();
//            diffEvent.setToyId(toyFacade.find(sessionBean.getSelectedToyId()));
//            diffEvent.setEventId(logicRemoveEvent);
//            diffEvent.setMethodType(MethodTypeConst.REMOVE.toString());
//            DiffSyncEvent removeDiffEvent = diffSyncEventFacade.create(diffEvent);
//            
//            // TODO アカウント共有イベントの削除パターンを追加
//            // TODO アカウントに紐づくToyの数分だけ差分イベントを登録する。
//            
//            logger.info("{} : USER_ID:{}, EVENT_ID:{}, DIFF_SYNC_EVENT_ID:{} {}.{}", MessageConst.SUCCESS_EVENT_LOGIC_REMOVE.getId() + ":" + MessageConst.SUCCESS_EVENT_LOGIC_REMOVE.getMessage(), sessionBean.getUserId(), logicRemoveEvent.getId(), removeDiffEvent.getId(),this.getClass().getName() + "." + this.getClass());
//            
//            return MessageConst.SUCCESS_EVENT_LOGIC_REMOVE;
//        }
//        
        return null;
    }
    
    /**
     * 引数で受けとったToyにアクセストークンを生成、セットして返却するメソッド
     * 
     */
    private Toy accessTokenGen(Toy toy) {
        
        String accessTokenPlane = "";
        accessTokenPlane += toy.getAccountId().getId().toString();
        accessTokenPlane += toy.getId().toString();
        LocalDateTime now = LocalDateTime.now();
        accessTokenPlane += now.toString();
        
        // ソルトの生成
        String salt = BCrypt.gensalt(12);
        String accessTokenHashed = BCrypt.hashpw(accessTokenPlane, salt);
        
        // 生成した値のセット
        toy.setAccessToken(accessTokenHashed);
        toy.setAccessTokenSalt(salt);
        toy.setAccessTokenLifecycle(Date.from(now.plusYears(1).atZone(ZoneId.systemDefault()).toInstant())); // 一年後の日時をセット
        
        return toy;
    }
    
}