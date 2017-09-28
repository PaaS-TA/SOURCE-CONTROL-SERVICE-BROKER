package org.paasta.servicebroker.sourcecontrol.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;
import java.util.Map;

/**
 * Created by lena on 2017-06-30.
 */
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE)
@Data
@Builder @NoArgsConstructor @AllArgsConstructor
public class User {

    @NotEmpty
    @JsonSerialize
    @JsonProperty("active")
    private boolean active;

    @NotEmpty
    @JsonSerialize
    @JsonProperty("admin")
    private boolean admin;

    @NotEmpty
    @JsonSerialize
    @JsonProperty("displayName")
    private String displayName;

    @NotEmpty
    @JsonSerialize
    @JsonProperty("name")
    private String name;

    @NotEmpty
    @JsonSerialize
    @JsonProperty("password")
    private String password;

    @NotEmpty
    @JsonSerialize
    @JsonProperty("type")
    private String type;

    @JsonSerialize
    @JsonProperty("mail")
    private String mail;

    @JsonSerialize
    @JsonProperty("properties")
    private List<Map<String, String>> properties;

    @JsonSerialize
    @JsonProperty("creationDate")
    private long creationDate;

    @JsonSerialize
    @JsonProperty("lastModified")
    private long lastModified;

}
