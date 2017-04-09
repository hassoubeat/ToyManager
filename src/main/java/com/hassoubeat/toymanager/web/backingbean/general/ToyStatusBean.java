/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hassoubeat.toymanager.web.backingbean.general;

import com.hassoubeat.toymanager.annotation.AuthGeneralInterceptor;
import com.hassoubeat.toymanager.annotation.ErrorInterceptor;
import com.hassoubeat.toymanager.annotation.LogInterceptor;
import com.hassoubeat.toymanager.constant.MessageConst;
import com.hassoubeat.toymanager.service.dao.AccountFacade;
import com.hassoubeat.toymanager.service.entity.Toy;
import com.hassoubeat.toymanager.service.dao.ToyFacade;
import com.hassoubeat.toymanager.service.entity.ToyWebapiAccessFilter;
import com.hassoubeat.toymanager.service.logic.ToyLogic;
import com.hassoubeat.toymanager.service.logic.ToyWebApiAccessFilterLogic;
import com.hassoubeat.toymanager.util.ToastMessage;
import com.hassoubeat.toymanager.web.backingbean.session.SessionBean;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;

/**
 *
 * @author hassoubeat
 */
@Named(value = "toyStatusBean")
@ViewScoped
public class ToyStatusBean implements Serializable{
    
    @Inject
    Logger logger;
    
    @Inject
    SessionBean sessionBean;
    
    @EJB
    AccountFacade accountFacade;
    
    @EJB
    ToyFacade toyFacade;
    
    @EJB
    ToyLogic toyLogic;
    
    @EJB
    ToyWebApiAccessFilterLogic toyWebApiAccessFilterLogic;
    
    @Getter
    @Setter
    private Toy targetToy;

    /**
     * Creates a new instance of ToyMenuBean
     */
    
    public ToyStatusBean() {
    }
    
    /**
     * Toy情報を取得しなおすメソッド
     */
    @LogInterceptor
    private void toyReload() {
        // ログイン中のユーザに紐づくToyを取得し、フィールドにセットする
        if (sessionBean.getSelectedToyId() != 0) {
            // Toyが選択されている場合、対象のToyをセットする
            this.setTargetToy(toyFacade.find(sessionBean.getSelectedToyId()));
        }
    }
    
    /**
     * Toyのアカウント紐付けを解除するメソッド
     * @return 遷移先
     */
    @ErrorInterceptor
    @AuthGeneralInterceptor
    @LogInterceptor
    public String accountTyingCancel(){
        // 選択中のToyを取得し、アカウント紐付けから解除する
        if (sessionBean.getSelectedToyId() != 0) {
            toyLogic.accountTyingCancel(toyFacade.find(sessionBean.getSelectedToyId()));
            
            // 紐付け解除完了のメッセージをリダイレクト先で表示するように、フラッシュメッセージに格納する
            ToastMessage toastMessage = new ToastMessage();
            toastMessage.setRender(true);
            toastMessage.setHeading(MessageConst.SUCCESS_TOY_ACCOUNT_TYING_CANCEL.getMessage());
            FacesContext facesContext = FacesContext.getCurrentInstance();
            facesContext.addMessage("", new FacesMessage(toastMessage.genList()));
            facesContext.getExternalContext().getFlash().setKeepMessages(true);
            return "/auth/index?faces-redirect=true";
        }
        
        // 元の画面に戻る
        return "";
    }
    
    /**
     * Toyのアクセストークンを再生成するメソッド
     * @return 遷移先
     */
    @ErrorInterceptor
    @AuthGeneralInterceptor
    @LogInterceptor
    public String accessTokenRegenerate() {
        if (sessionBean.getSelectedToyId() != 0) {
            Toy accessTokenReGenerateToy = toyLogic.accessTokenGen(toyFacade.find(sessionBean.getSelectedToyId()));
            toyLogic.edit(accessTokenReGenerateToy);
            
            // 紐付け解除完了のメッセージをリダイレクト先で表示するように、フラッシュメッセージに格納する
            ToastMessage toastMessage = new ToastMessage();
            toastMessage.setRender(true);
            toastMessage.setHeading(MessageConst.SUCCESS_ACCESS_TOKEN_GENERATE.getMessage());
            toastMessage.setText("現在利用しているToyにアクセストークンの再設定を行って下さい。");
            toastMessage.setHideAfter(7000);
            FacesContext facesContext = FacesContext.getCurrentInstance();
            facesContext.addMessage("", new FacesMessage(toastMessage.genList()));
            facesContext.getExternalContext().getFlash().setKeepMessages(true);
        }
        
        // 元の画面にリダイレクトして遷移する(bookmarkableを動作させる)
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        return request.getPathInfo() + "?faces-redirect=true";
    }
    
    /**
     * アクセスフィルターを承認するメソッド
     * @param targetToyWebapiAccessFilter
     * @return 
     */
    @ErrorInterceptor
    @AuthGeneralInterceptor
    @LogInterceptor
    public String webApiAccessFilterApproval(ToyWebapiAccessFilter targetToyWebapiAccessFilter) {
        if (sessionBean.getSelectedToyId() != 0) {
            toyWebApiAccessFilterLogic.approval(targetToyWebapiAccessFilter);
            
            // 紐付け解除完了のメッセージをリダイレクト先で表示するように、フラッシュメッセージに格納する
            ToastMessage toastMessage = new ToastMessage();
            toastMessage.setRender(true);
            toastMessage.setHeading(MessageConst.SUCCESS_ACCESS_FILTER_APPROVAL.getMessage());
            toastMessage.setText("Toyからアクセスが行えるようになりました。");
            toastMessage.setHideAfter(5000);
            FacesContext facesContext = FacesContext.getCurrentInstance();
            facesContext.addMessage("", new FacesMessage(toastMessage.genList()));
            facesContext.getExternalContext().getFlash().setKeepMessages(true);
        }
        
        // 元の画面にリダイレクトして遷移する(bookmarkableを動作させる)
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        return request.getPathInfo() + "?faces-redirect=true";
    }
    
    /**
     * アクセスフィルターを拒否するメソッド
     * @param targetToyWebapiAccessFilter
     * @return 
     */
    @ErrorInterceptor
    @AuthGeneralInterceptor
    @LogInterceptor
    public String webApiAccessFilterReject(ToyWebapiAccessFilter targetToyWebapiAccessFilter) {
        if (sessionBean.getSelectedToyId() != 0) {
            toyWebApiAccessFilterLogic.reject(targetToyWebapiAccessFilter);
            
            // 紐付け解除完了のメッセージをリダイレクト先で表示するように、フラッシュメッセージに格納する
            ToastMessage toastMessage = new ToastMessage();
            toastMessage.setRender(true);
            toastMessage.setHeading(MessageConst.SUCCESS_ACCESS_FILTER_REJECT.getMessage());
            toastMessage.setHideAfter(5000);
            FacesContext facesContext = FacesContext.getCurrentInstance();
            facesContext.addMessage("access-filter-reject", new FacesMessage(toastMessage.genList()));
            facesContext.getExternalContext().getFlash().setKeepMessages(true);
        }
        
        // 元の画面にリダイレクトして遷移する(bookmarkableを動作させる)
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        return request.getPathInfo() + "?faces-redirect=true";
    }
    
    @ErrorInterceptor
    @AuthGeneralInterceptor
    @LogInterceptor
    public void bookmarkable(){
        this.toyReload();
    }
    
}
