/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hassoubeat.toymanager.service.entity;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author hassoubeat
 */
@Entity
@Table(name = "facet_event")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FacetEvent.findAll", query = "SELECT f FROM FacetEvent f")
    , @NamedQuery(name = "FacetEvent.findById", query = "SELECT f FROM FacetEvent f WHERE f.id = :id")
    , @NamedQuery(name = "FacetEvent.findByName", query = "SELECT f FROM FacetEvent f WHERE f.name = :name")
    , @NamedQuery(name = "FacetEvent.findByContent", query = "SELECT f FROM FacetEvent f WHERE f.content = :content")
    , @NamedQuery(name = "FacetEvent.findByNote", query = "SELECT f FROM FacetEvent f WHERE f.note = :note")
    , @NamedQuery(name = "FacetEvent.findByStartDate", query = "SELECT f FROM FacetEvent f WHERE f.startDate = :startDate")
    , @NamedQuery(name = "FacetEvent.findByEndDate", query = "SELECT f FROM FacetEvent f WHERE f.endDate = :endDate")
    , @NamedQuery(name = "FacetEvent.findByColorCode", query = "SELECT f FROM FacetEvent f WHERE f.colorCode = :colorCode")
    , @NamedQuery(name = "FacetEvent.findByRoop", query = "SELECT f FROM FacetEvent f WHERE f.roop = :roop")
    , @NamedQuery(name = "FacetEvent.findByRoopEndDate", query = "SELECT f FROM FacetEvent f WHERE f.roopEndDate = :roopEndDate")
    , @NamedQuery(name = "FacetEvent.findByIsTalking", query = "SELECT f FROM FacetEvent f WHERE f.isTalking = :isTalking")
    , @NamedQuery(name = "FacetEvent.findByIsDeleted", query = "SELECT f FROM FacetEvent f WHERE f.isDeleted = :isDeleted")
    , @NamedQuery(name = "FacetEvent.findByCreateDate", query = "SELECT f FROM FacetEvent f WHERE f.createDate = :createDate")
    , @NamedQuery(name = "FacetEvent.findByEditDate", query = "SELECT f FROM FacetEvent f WHERE f.editDate = :editDate")})
public class FacetEvent implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "name")
    private String name;
    @Size(max = 200)
    @Column(name = "content")
    private String content;
    @Size(max = 200)
    @Column(name = "note")
    private String note;
    @Basic(optional = false)
    @NotNull
    @Column(name = "start_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;
    @Column(name = "end_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;
    @Size(max = 10)
    @Column(name = "color_code")
    private String colorCode;
    @Column(name = "roop")
    private Integer roop;
    @Column(name = "roop_end_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date roopEndDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "priority")
    private Integer priority;
    @Basic(optional = false)
    @NotNull
    @Column(name = "is_talking")
    private boolean isTalking;
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
    @JoinColumn(name = "facet_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Facet facetId;

    public FacetEvent() {
    }

    public FacetEvent(Integer id) {
        this.id = id;
    }

    public FacetEvent(Integer id, String name, Date startDate, boolean isTalking, boolean isDeleted, Date createDate, Date editDate) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.isTalking = isTalking;
        this.isDeleted = isDeleted;
        this.createDate = createDate;
        this.editDate = editDate;
    }
    
    @PrePersist
    public void prePersist(){
        // デフォルトの優先度をセットする
        this.setPriority(10);
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    public Integer getRoop() {
        return roop;
    }

    public void setRoop(Integer roop) {
        this.roop = roop;
    }

    public Date getRoopEndDate() {
        return roopEndDate;
    }

    public void setRoopEndDate(Date roopEndDate) {
        this.roopEndDate = roopEndDate;
    }
    
    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
    
    public boolean getIsTalking() {
        return isTalking;
    }

    public void setIsTalking(boolean isTalking) {
        this.isTalking = isTalking;
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

    public Facet getFacetId() {
        return facetId;
    }

    public void setFacetId(Facet facetId) {
        this.facetId = facetId;
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
        if (!(object instanceof FacetEvent)) {
            return false;
        }
        FacetEvent other = (FacetEvent) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.hassoubeat.toymanager.service.entity.FacetEvent[ id=" + id + " ]";
    }
    
}
