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
@Table(name = "toy_webapi_access_filter")
@NamedQueries({
    @NamedQuery(name = "ToyWebapiAccessFilter.findAll", query = "SELECT t FROM ToyWebapiAccessFilter t")
    , @NamedQuery(name = "ToyWebapiAccessFilter.findById", query = "SELECT t FROM ToyWebapiAccessFilter t WHERE t.id = :id")
    , @NamedQuery(name = "ToyWebapiAccessFilter.findByMacAddress", query = "SELECT t FROM ToyWebapiAccessFilter t WHERE t.macAddress = :macAddress")
    , @NamedQuery(name = "ToyWebapiAccessFilter.findByIsAuthenticated", query = "SELECT t FROM ToyWebapiAccessFilter t WHERE t.isAuthenticated = :isAuthenticated")
    , @NamedQuery(name = "ToyWebapiAccessFilter.findByCreateDate", query = "SELECT t FROM ToyWebapiAccessFilter t WHERE t.createDate = :createDate")
    , @NamedQuery(name = "ToyWebapiAccessFilter.findByEditDate", query = "SELECT t FROM ToyWebapiAccessFilter t WHERE t.editDate = :editDate")})
public class ToyWebapiAccessFilter implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "mac_address")
    private String macAddress;
    @Basic(optional = false)
    @NotNull
    @Column(name = "is_authenticated")
    private boolean isAuthenticated;
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
    @JoinColumn(name = "toy_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Toy toyId;
//    @OneToMany(mappedBy = "toyWebapiAccessFilterId")
//    private List<Toy> toyList;

    public ToyWebapiAccessFilter() {
    }

    public ToyWebapiAccessFilter(Integer id) {
        this.id = id;
    }

    public ToyWebapiAccessFilter(Integer id, String macAddress, boolean isAuthenticated, Date createDate, Date editDate) {
        this.id = id;
        this.macAddress = macAddress;
        this.isAuthenticated = isAuthenticated;
        this.createDate = createDate;
        this.editDate = editDate;
    }
    
    @PrePersist
    public void prePersist(){
        // 未認証で初期化して登録する
        this.setIsAuthenticated(false);
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

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public boolean getIsAuthenticated() {
        return isAuthenticated;
    }

    public void setIsAuthenticated(boolean isAuthenticated) {
        this.isAuthenticated = isAuthenticated;
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

    public Toy getToyId() {
        return toyId;
    }

    public void setToyId(Toy toyId) {
        this.toyId = toyId;
    }

//    public List<Toy> getToyList() {
//        return toyList;
//    }
//
//    public void setToyList(List<Toy> toyList) {
//        this.toyList = toyList;
//    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ToyWebapiAccessFilter)) {
            return false;
        }
        ToyWebapiAccessFilter other = (ToyWebapiAccessFilter) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.hassoubeat.toymanager.service.entity.ToyWebapiAccessFilter[ id=" + id + " ]";
    }
    
}
