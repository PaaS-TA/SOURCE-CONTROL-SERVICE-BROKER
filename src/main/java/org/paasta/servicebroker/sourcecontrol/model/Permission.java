package org.paasta.servicebroker.sourcecontrol.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * Created by lena on 2017-06-16.
 */
@Data
public class Permission {

    @NotEmpty
    @JsonSerialize
    @JsonProperty("name")
    private String name;

    @NotEmpty
    @JsonSerialize
    @JsonProperty("type")
    private String type;

    @NotNull
    @JsonSerialize
    @JsonProperty("groupPermission")
    private boolean groupPermission;

   public Permission(String name, String type){
       this.name = name;
       this.type = type;
       this.groupPermission = false;
   }
    public Permission(){
   }

}
