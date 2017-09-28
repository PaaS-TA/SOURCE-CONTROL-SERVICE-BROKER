package org.paasta.servicebroker.sourcecontrol.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Created by lena on 2017-06-15.
 */
@Data
@Builder @NoArgsConstructor @AllArgsConstructor
@Entity
@Table(name = "SC_REPOSITORY")
public class JpaScRepository {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "REPO_NO")
    private int repoNo;
    @Column(name = "REPO_SCM_ID")
    private String repoScmId;
    @Column(name = "REPO_NAME")
    private String repoName;
    @Column(name = "REPO_DESC", nullable =true)
    private String repoDesc;
    @Column(name = "INSTANCE_ID")
    private String instanceId;
    @Column(name = "OWNER_USER_ID")
    private String ownerUserId;
    @Column(name = "CREATE_USER_ID")
    private String createUserId;
    @Column(name = "CREATED_TIME")
    private String createTime;

}
