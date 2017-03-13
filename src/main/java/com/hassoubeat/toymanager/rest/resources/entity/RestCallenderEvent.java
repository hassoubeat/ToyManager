
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
public class RestCallenderEvent {
    
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
    

    public RestCallenderEvent() {
        this.borderColor = "#b1b1b1";
    }

    
    
   
    
    
    
    
}
