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
@Table(name = "event")
@NamedQueries({
    @NamedQuery(name = "Event.findAll", query = "SELECT e FROM Event e")
    , @NamedQuery(name = "Event.findById", query = "SELECT e FROM Event e WHERE e.id = :id")
    , @NamedQuery(name = "Event.findByName", query = "SELECT e FROM Event e WHERE e.name = :name")
    , @NamedQuery(name = "Event.findByContent", query = "SELECT e FROM Event e WHERE e.content = :content")
    , @NamedQuery(name = "Event.findByStartDate", query = "SELECT e FROM Event e WHERE e.startDate = :startDate")
    , @NamedQuery(name = "Event.findByEndDate", query = "SELECT e FROM Event e WHERE e.endDate = :endDate")
    , @NamedQuery(name = "Event.findByColorCode", query = "SELECT e FROM Event e WHERE e.colorCode = :colorCode")
    , @NamedQuery(name = "Event.findByRoop", query = "SELECT e FROM Event e WHERE e.roop = :roop")
    , @NamedQuery(name = "Event.findByRoopEndDate", query = "SELECT e FROM Event e WHERE e.roopEndDate = :roopEndDate")
    , @NamedQuery(name = "Event.findByIsTalking", query = "SELECT e FROM Event e WHERE e.isTalking = :isTalking")
    , @NamedQuery(name = "Event.findByIsDeleted", query = "SELECT e FROM Event e WHERE e.isDeleted = :isDeleted")
    , @NamedQuery(name = "Event.findByCreateDate", query = "SELECT e FROM Event e WHERE e.createDate = :createDate")
    , @NamedQuery(name = "Event.findByEditDate", query = "SELECT e FROM Event e WHERE e.editDate = :editDate")})
public class Event implements Serializable, Cloneable {

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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "eventId")
    private List<DiffSyncEvent> diffSyncEventList;
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    @ManyToOne
    private Account accountId;
    @JoinColumn(name = "event_type_id", referencedColumnName = "id")
    @ManyToOne
    private EventType eventTypeId;
    @JoinColumn(name = "toy_facet_id", referencedColumnName = "id")
    @ManyToOne
    private ToyFacet toyFacetId;
    @JoinColumn(name = "toy_id", referencedColumnName = "id")
    @ManyToOne
    private Toy toyId;

    public Event() {
    }

    public Event(Integer id) {
        this.id = id;
    }

    public Event(Integer id, String name, Date startDate, boolean isTalking, boolean isDeleted, Date createDate, Date editDate) {
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
        this.setPriority(5);
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

    public List<DiffSyncEvent> getDiffSyncEventList() {
        return diffSyncEventList;
    }

    public void setDiffSyncEventList(List<DiffSyncEvent> diffSyncEventList) {
        this.diffSyncEventList = diffSyncEventList;
    }

    public Account getAccountId() {
        return accountId;
    }

    public void setAccountId(Account accountId) {
        this.accountId = accountId;
    }

    public EventType getEventTypeId() {
        return eventTypeId;
    }

    public void setEventTypeId(EventType eventTypeId) {
        this.eventTypeId = eventTypeId;
    }

    public ToyFacet getToyFacetId() {
        return toyFacetId;
    }

    public void setToyFacetId(ToyFacet toyFacetId) {
        this.toyFacetId = toyFacetId;
    }

    public Toy getToyId() {
        return toyId;
    }

    public void setToyId(Toy toyId) {
        this.toyId = toyId;
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
        if (!(object instanceof Event)) {
            return false;
        }
        Event other = (Event) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.hassoubeat.toymanager.service.entity.Event[ id=" + id + " ]";
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone(); //To change body of generated methods, choose Tools | Templates.
    }
    
    

}
