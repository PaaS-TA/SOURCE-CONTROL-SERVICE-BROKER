package org.paasta.servicebroker.sourcecontrol.model;

import lombok.Data;

import javax.persistence.*;

/**
 * Created by lena on 2017-06-15.
 */
@Data
@Entity
@Table(name = "REPO_PERMITION")
public class JpaRepoPermission {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "NO")
    private int no;
    @Column(name = "REPO_NO")
    private int repoNo;
    @Column(name = "USER_ID")
    private String userId;
    @Column(name = "PERMISSION")
    private String permission;

}
