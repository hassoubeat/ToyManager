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
@Table(name = "toy_voice_type")
@NamedQueries({
    @NamedQuery(name = "ToyVoiceType.findAll", query = "SELECT t FROM ToyVoiceType t")
    , @NamedQuery(name = "ToyVoiceType.findById", query = "SELECT t FROM ToyVoiceType t WHERE t.id = :id")
    , @NamedQuery(name = "ToyVoiceType.findByName", query = "SELECT t FROM ToyVoiceType t WHERE t.name = :name")
    , @NamedQuery(name = "ToyVoiceType.findByNote", query = "SELECT t FROM ToyVoiceType t WHERE t.note = :note")
    , @NamedQuery(name = "ToyVoiceType.findByJsayCommand", query = "SELECT t FROM ToyVoiceType t WHERE t.jsayCommand = :jsayCommand")
    , @NamedQuery(name = "ToyVoiceType.findBySampleVoice1Url", query = "SELECT t FROM ToyVoiceType t WHERE t.sampleVoice1Url = :sampleVoice1Url")
    , @NamedQuery(name = "ToyVoiceType.findBySampleVoice2Url", query = "SELECT t FROM ToyVoiceType t WHERE t.sampleVoice2Url = :sampleVoice2Url")
    , @NamedQuery(name = "ToyVoiceType.findBySampleVoice3Url", query = "SELECT t FROM ToyVoiceType t WHERE t.sampleVoice3Url = :sampleVoice3Url")
    , @NamedQuery(name = "ToyVoiceType.findBySampleVoice4Url", query = "SELECT t FROM ToyVoiceType t WHERE t.sampleVoice4Url = :sampleVoice4Url")
    , @NamedQuery(name = "ToyVoiceType.findBySampleVoice5Url", query = "SELECT t FROM ToyVoiceType t WHERE t.sampleVoice5Url = :sampleVoice5Url")
    , @NamedQuery(name = "ToyVoiceType.findByCreateDate", query = "SELECT t FROM ToyVoiceType t WHERE t.createDate = :createDate")
    , @NamedQuery(name = "ToyVoiceType.findByEditDate", query = "SELECT t FROM ToyVoiceType t WHERE t.editDate = :editDate")})
@XmlRootElement
public class ToyVoiceType implements Serializable {

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
    @Size(max = 600)
    @Column(name = "note")
    private String note;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "jsay_command")
    private String jsayCommand;
    @Size(max = 300)
    @Column(name = "sample_voice1_url")
    private String sampleVoice1Url;
    @Size(max = 300)
    @Column(name = "sample_voice2_url")
    private String sampleVoice2Url;
    @Size(max = 300)
    @Column(name = "sample_voice3_url")
    private String sampleVoice3Url;
    @Size(max = 300)
    @Column(name = "sample_voice4_url")
    private String sampleVoice4Url;
    @Size(max = 300)
    @Column(name = "sample_voice5_url")
    private String sampleVoice5Url;
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
    @OneToMany(mappedBy = "toyVoiceTypeId")
    private List<Toy> toyList;

    public ToyVoiceType() {
    }

    public ToyVoiceType(Integer id) {
        this.id = id;
    }

    public ToyVoiceType(Integer id, String name, String jsayCommand, Date createDate, Date editDate) {
        this.id = id;
        this.name = name;
        this.jsayCommand = jsayCommand;
        this.createDate = createDate;
        this.editDate = editDate;
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getJsayCommand() {
        return jsayCommand;
    }

    public void setJsayCommand(String jsayCommand) {
        this.jsayCommand = jsayCommand;
    }

    public String getSampleVoice1Url() {
        return sampleVoice1Url;
    }

    public void setSampleVoice1Url(String sampleVoice1Url) {
        this.sampleVoice1Url = sampleVoice1Url;
    }

    public String getSampleVoice2Url() {
        return sampleVoice2Url;
    }

    public void setSampleVoice2Url(String sampleVoice2Url) {
        this.sampleVoice2Url = sampleVoice2Url;
    }

    public String getSampleVoice3Url() {
        return sampleVoice3Url;
    }

    public void setSampleVoice3Url(String sampleVoice3Url) {
        this.sampleVoice3Url = sampleVoice3Url;
    }

    public String getSampleVoice4Url() {
        return sampleVoice4Url;
    }

    public void setSampleVoice4Url(String sampleVoice4Url) {
        this.sampleVoice4Url = sampleVoice4Url;
    }

    public String getSampleVoice5Url() {
        return sampleVoice5Url;
    }

    public void setSampleVoice5Url(String sampleVoice5Url) {
        this.sampleVoice5Url = sampleVoice5Url;
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
        if (!(object instanceof ToyVoiceType)) {
            return false;
        }
        ToyVoiceType other = (ToyVoiceType) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.hassoubeat.toymanager.web.backingbean.admin.ToyVoiceType[ id=" + id + " ]";
    }
    
}
