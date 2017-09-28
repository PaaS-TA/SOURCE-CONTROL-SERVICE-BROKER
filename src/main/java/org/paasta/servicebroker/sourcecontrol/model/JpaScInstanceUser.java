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
@Builder @NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "SC_INSTANCE_USER")
public class JpaScInstanceUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="NO")
    private int no;
    @Column(name = "INSTANCE_ID")
    private String instanceId;
    @Column(name = "USER_ID")
    private String userId;
    @Column(name = "REPO_ROLE", nullable =true)
    private String repoRole;
    @Column(name = "CREATER_YN")
    private String createrYn;
}