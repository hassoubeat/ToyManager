
package com.hassoubeat.toymanager.rest.resources.entity;

import java.util.Date;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author hassoubeat
 */
@XmlRootElement
public class RestCalenderEvent {
    
    @Getter
    @Setter
    private int id;
    
    @Getter
    @Setter
    private String title;
    
    @Getter
    @Setter
    private Date start;
    
    @Getter
    @Setter
    private Date end;
    
    @Getter
    @Setter
    private String color;
    
    @Getter
    @Setter
    private String borderColor;
    
    @Getter
    @Setter
    private List<String> className;
    

    public RestCalenderEvent() {
        this.borderColor = "#b1b1b1";
    }

    @Override
    public String toString() {
        return "RestCallenderEvent{" + "id=" + id + ", title=" + title + ", start=" + start + ", end=" + end + ", color=" + color + ", borderColor=" + borderColor + ", className=" + className + '}';
    }
    
    

    
    
   
    
    
    
    
}
