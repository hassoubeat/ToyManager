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
import javax.persistence.CascadeType;
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author hassoubeat
 */
@Entity
@Table(name = "toy")
@NamedQueries({
    @NamedQuery(name = "Toy.findAll", query = "SELECT t FROM Toy t")
    , @NamedQuery(name = "Toy.findById", query = "SELECT t FROM Toy t WHERE t.id = :id")
    , @NamedQuery(name = "Toy.findByRotNum", query = "SELECT t FROM Toy t WHERE t.rotNum = :rotNum")
    , @NamedQuery(name = "Toy.findByName", query = "SELECT t FROM Toy t WHERE t.name = :name")
    , @NamedQuery(name = "Toy.findByPictureUrl", query = "SELECT t FROM Toy t WHERE t.pictureUrl = :pictureUrl")
    , @NamedQuery(name = "Toy.findByAccessToken", query = "SELECT t FROM Toy t WHERE t.accessToken = :accessToken")
    , @NamedQuery(name = "Toy.findByAccessTokenSalt", query = "SELECT t FROM Toy t WHERE t.accessTokenSalt = :accessTokenSalt")
    , @NamedQuery(name = "Toy.findByAccessTokenLifecycle", query = "SELECT t FROM Toy t WHERE t.accessTokenLifecycle = :accessTokenLifecycle")
    , @NamedQuery(name = "Toy.findByIsDeleted", query = "SELECT t FROM Toy t WHERE t.isDeleted = :isDeleted")
    , @NamedQuery(name = "Toy.findByLastSyncDate", query = "SELECT t FROM Toy t WHERE t.lastSyncDate = :lastSyncDate")
    , @NamedQuery(name = "Toy.findByCreateDate", query = "SELECT t FROM Toy t WHERE t.createDate = :createDate")
    , @NamedQuery(name = "Toy.findByEditDate", query = "SELECT t FROM Toy t WHERE t.editDate = :editDate")})
@XmlRootElement
public class Toy implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "rot_num")
    private int rotNum;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "name")
    private String name;
    @Size(max = 300)
    @Column(name = "picture_url")
    private String pictureUrl;
    @Size(max = 300)
    @Column(name = "access_token")
    private String accessToken;
    @Size(max = 150)
    @Column(name = "access_token_salt")
    private String accessTokenSalt;
    @Column(name = "access_token_lifecycle")
    @Temporal(TemporalType.TIMESTAMP)
    private Date accessTokenLifecycle;
    @Basic(optional = false)
    @NotNull
    @Column(name = "is_deleted")
    private boolean isDeleted;
    @Column(name = "last_sync_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastSyncDate;
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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "toyId")
    private List<ToyFacet> toyFacetList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "toyId")
    private List<ToyWebapiAccessFilter> toyWebapiAccessFilterList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "toyId")
    private List<DiffSyncEvent> diffSyncEventList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "toyId")
    private List<Event> eventList;
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    @ManyToOne
    private Account accountId;
    @JoinColumn(name = "toy_type_id", referencedColumnName = "id")
    @ManyToOne
    private ToyType toyTypeId;
    @JoinColumn(name = "toy_version_id", referencedColumnName = "version")
    @ManyToOne
    private ToyVersion toyVersionId;
    @JoinColumn(name = "toy_voice_type_id", referencedColumnName = "id")
    @ManyToOne
    private ToyVoiceType toyVoiceTypeId;

    public Toy() {
    }

    public Toy(Integer id) {
        this.id = id;
    }

    public Toy(Integer id, int rotNum, String name, boolean isDeleted, Date createDate, Date editDate) {
        this.id = id;
        this.rotNum = rotNum;
        this.name = name;
        this.isDeleted = isDeleted;
        this.createDate = createDate;
        this.editDate = editDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getRotNum() {
        return rotNum;
    }

    public void setRotNum(int rotNum) {
        this.rotNum = rotNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessTokenSalt() {
        return accessTokenSalt;
    }

    public void setAccessTokenSalt(String accessTokenSalt) {
        this.accessTokenSalt = accessTokenSalt;
    }

    public Date getAccessTokenLifecycle() {
        return accessTokenLifecycle;
    }

    public void setAccessTokenLifecycle(Date accessTokenLifecycle) {
        this.accessTokenLifecycle = accessTokenLifecycle;
    }

    public boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Date getLastSyncDate() {
        return lastSyncDate;
    }

    public void setLastSyncDate(Date lastSyncDate) {
        this.lastSyncDate = lastSyncDate;
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

    @XmlTransient
    public List<ToyFacet> getToyFacetList() {
        return toyFacetList;
    }

    public void setToyFacetList(List<ToyFacet> toyFacetList) {
        this.toyFacetList = toyFacetList;
    }

    @XmlTransient
    public List<ToyWebapiAccessFilter> getToyWebapiAccessFilterList() {
        return toyWebapiAccessFilterList;
    }

    public void setToyWebapiAccessFilterList(List<ToyWebapiAccessFilter> toyWebapiAccessFilterList) {
        this.toyWebapiAccessFilterList = toyWebapiAccessFilterList;
    }

    @XmlTransient
    public List<DiffSyncEvent> getDiffSyncEventList() {
        return diffSyncEventList;
    }

    public void setDiffSyncEventList(List<DiffSyncEvent> diffSyncEventList) {
        this.diffSyncEventList = diffSyncEventList;
    }

    @XmlTransient
    public List<Event> getEventList() {
        return eventList;
    }

    public void setEventList(List<Event> eventList) {
        this.eventList = eventList;
    }

    public Account getAccountId() {
        return accountId;
    }

    public void setAccountId(Account accountId) {
        this.accountId = accountId;
    }

    public ToyType getToyTypeId() {
        return toyTypeId;
    }

    public void setToyTypeId(ToyType toyTypeId) {
        this.toyTypeId = toyTypeId;
    }

    public ToyVersion getToyVersionId() {
        return toyVersionId;
    }

    public void setToyVersionId(ToyVersion toyVersionId) {
        this.toyVersionId = toyVersionId;
    }

    public ToyVoiceType getToyVoiceTypeId() {
        return toyVoiceTypeId;
    }

    public void setToyVoiceTypeId(ToyVoiceType toyVoiceTypeId) {
        this.toyVoiceTypeId = toyVoiceTypeId;
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
        if (!(object instanceof Toy)) {
            return false;
        }
        Toy other = (Toy) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.hassoubeat.toymanager.web.backingbean.admin.Toy[ id=" + id + " ]";
    }
    
}
