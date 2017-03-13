/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hassoubeat.toymanager.service.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author hassoubeat
 */
@Entity
@Table(name = "account")
@NamedQueries({
    @NamedQuery(name = "Account.findAll", query = "SELECT a FROM Account a")
    , @NamedQuery(name = "Account.findById", query = "SELECT a FROM Account a WHERE a.id = :id")
    , @NamedQuery(name = "Account.findByUserId", query = "SELECT a FROM Account a WHERE a.userId = :userId")
    , @NamedQuery(name = "Account.findByPasswordHash", query = "SELECT a FROM Account a WHERE a.passwordHash = :passwordHash")
    , @NamedQuery(name = "Account.findByPasswordSalt", query = "SELECT a FROM Account a WHERE a.passwordSalt = :passwordSalt")
    , @NamedQuery(name = "Account.findByIsAuthenticated", query = "SELECT a FROM Account a WHERE a.isAuthenticated = :isAuthenticated")
    , @NamedQuery(name = "Account.findByIsDeleted", query = "SELECT a FROM Account a WHERE a.isDeleted = :isDeleted")
    , @NamedQuery(name = "Account.findByCreateDate", query = "SELECT a FROM Account a WHERE a.createDate = :createDate")
    , @NamedQuery(name = "Account.findByEditDate", query = "SELECT a FROM Account a WHERE a.editDate = :editDate")
    , @NamedQuery(name = "Account.findByLastSelectedToyId", query = "SELECT a FROM Account a WHERE a.lastSelectedToyId = :lastSelectedToyId")})
public class Account implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 256)
    @Column(name = "user_id")
    private String userId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 300)
    @Column(name = "password_hash")
    private String passwordHash;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "password_salt")
    private String passwordSalt;
    @Basic(optional = false)
    @NotNull
    @Column(name = "is_authenticated")
    private boolean isAuthenticated;
    @Basic(optional = false)
    @NotNull
    @Column(name = "is_deleted")
    private boolean isDeleted;
    @Basic(optional = false)
    @NotNull
    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "edit_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date editDate;
    @Column(name = "last_selected_toy_id")
    private Integer lastSelectedToyId;
    @OneToMany(mappedBy = "accountId")
    private List<Event> eventList;
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Role roleId;
    @OneToMany(mappedBy = "accountId")
    private List<Toy> toyList;

    public Account() {
    }

    public Account(Integer id) {
        this.id = id;
    }

    public Account(Integer id, String userId, String passwordHash, String passwordSalt, boolean isAuthenticated, boolean isDeleted, Date createDate, Date editDate) {
        this.id = id;
        this.userId = userId;
        this.passwordHash = passwordHash;
        this.passwordSalt = passwordSalt;
        this.isAuthenticated = isAuthenticated;
        this.isDeleted = isDeleted;
        this.createDate = createDate;
        this.editDate = editDate;
    }
    
    @PrePersist
    public void prePersist(){
        // TODO メール認証完了フラグを設定する　そもそもメール認証完了フラグいらないかも
        this.setIsAuthenticated(true);
        // 削除フラグを入力する
        this.setIsDeleted(false);
        // 登録日時と更新日時に現在日時を設定する
        Date now = new Date();
        this.setCreateDate(now);
        this.setEditDate(now);
    }
    
    @PreUpdate
    public void preUpdate(){
        // 更新日時を更新する
        Date now = new Date();
        this.setEditDate(now);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getPasswordSalt() {
        return passwordSalt;
    }

    public void setPasswordSalt(String passwordSalt) {
        this.passwordSalt = passwordSalt;
    }

    public boolean getIsAuthenticated() {
        return isAuthenticated;
    }

    public void setIsAuthenticated(boolean isAuthenticated) {
        this.isAuthenticated = isAuthenticated;
    }

    public boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getEditDate() {
        return editDate;
    }

    public void setEditDate(Date editDate) {
        this.editDate = editDate;
    }

    public Integer getLastSelectedToyId() {
        return lastSelectedToyId;
    }

    public void setLastSelectedToyId(Integer lastSelectedToyId) {
        this.lastSelectedToyId = lastSelectedToyId;
    }

    public List<Event> getEventList() {
        return eventList;
    }

    public void setEventList(List<Event> eventList) {
        this.eventList = eventList;
    }

    public Role getRoleId() {
        return roleId;
    }

    public void setRoleId(Role roleId) {
        this.roleId = roleId;
    }

    public List<Toy> getToyList() {
        return toyList;
    }

    public void setToyList(List<Toy> toyList) {
        this.toyList = toyList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Account)) {
            return false;
        }
        Account other = (Account) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.hassoubeat.toymanager.service.entity.Account[ id=" + id + " ]";
    }
    
}
