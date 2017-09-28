package org.paasta.servicebroker.sourcecontrol.repository;

import org.paasta.servicebroker.sourcecontrol.model.JpaScUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by lena on 2017-05-16.
 */

@Repository
public interface JpaScUserRepository extends JpaRepository<JpaScUser, String> {
}
