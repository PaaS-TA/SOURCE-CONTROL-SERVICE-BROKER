package org.paasta.servicebroker.sourcecontrol.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * Created by lena on 2017-05-16.
 */
@Data
@Builder @NoArgsConstructor @AllArgsConstructor
@Entity
@Table(name = "SC_USER")
public class JpaScUser {
    @Id
    @Column(name="USER_ID")
    private String userId;
    @Column(name = "USER_NAME", nullable =true)
    private String userName;
    @Column(name = "USER_MAIL", nullable =true)
    private String userMail;
    @Column(name = "USER_DESC", nullable =true)
    private String userDesc;
}