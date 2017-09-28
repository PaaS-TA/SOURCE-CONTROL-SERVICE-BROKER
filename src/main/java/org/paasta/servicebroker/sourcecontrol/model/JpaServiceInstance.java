package org.paasta.servicebroker.sourcecontrol.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


/**
 * Created by lena on 2017-05-16.
 */
@Data
@Builder @NoArgsConstructor @AllArgsConstructor
@Entity
@Table(name = "SERVICE_INSTANCE")
public class JpaServiceInstance {

    @Id
    @Column(name = "INSTANCE_ID")
    private String instanceId;
    @Column(name = "ORGANIZATION_GUID")
    private String organizationGuid;
    @Column(name = "ORGANIZATION_NAME", nullable =true)
    private String organizationName;
    @Column(name = "PLAN_ID")
    private String planId;
    @Column(name = "SERVICE_ID")
    private String serviceId;
    @Column(name = "SPACE_GUID")
    private String spaceGuid;
    @Column(name = "CREATE_USER_ID", nullable =true)
    private String createUserId;
    @Column(name = "CREATED_TIME")
    private String createdTime;

}