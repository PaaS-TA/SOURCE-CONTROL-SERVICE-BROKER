package org.paasta.servicebroker.sourcecontrol.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;
import java.util.Map;

/**
 * Created by lena on 2017-06-16.
 */
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE)
@Data
@ToString
public class Repository {

    @NotEmpty
    @JsonSerialize
    @JsonProperty("id")
    private String id;

    @NotEmpty
    @JsonSerialize
    @JsonProperty("name")
    private String name;

    @JsonSerialize
    @JsonProperty("description")
    private String description;

    @NotEmpty
    @JsonSerialize
    @JsonProperty("type")
    private String type;

    @NotEmpty
    @JsonSerialize
    @JsonProperty("url")
    private String url;

    @JsonSerialize
    @JsonProperty("public")
    private boolean public_;

    @JsonSerialize
    @JsonProperty("archived")
    private boolean archived;

    @JsonSerialize
    @JsonProperty("contact")
    private String contact;

    @NotEmpty
    @JsonSerialize
    @JsonProperty("permissions")
    private List<Permission> permissions;

    @NotEmpty
    @JsonSerialize
    @JsonProperty("properties")
    private List<Map<String, String>> properties;

    @NotEmpty
    @JsonSerialize
    @JsonProperty("creationDate")
    private long creationDate;

    @JsonSerialize
    @JsonProperty("lastModified")
    private long lastModified;

}