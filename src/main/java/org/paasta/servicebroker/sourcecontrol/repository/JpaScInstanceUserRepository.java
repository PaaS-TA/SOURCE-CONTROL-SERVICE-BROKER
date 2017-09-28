package org.paasta.servicebroker.sourcecontrol.repository;

import org.paasta.servicebroker.sourcecontrol.model.JpaScInstanceUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by lena on 2017-05-16.
 */

@Repository
public interface JpaScInstanceUserRepository extends JpaRepository<JpaScInstanceUser, Integer> {

    @Transactional
    int deleteAllByInstanceId(String instanceId);


    @Query(value = "select t from JpaScInstanceUser t where t.instanceId= :instanceId " +
            "and t.userId not in (select s.userId from JpaScInstanceUser s where s.instanceId <> :instanceId) group by t.instanceId, t.userId")
    List<JpaScInstanceUser> selectDeleteScInstanceUser(@Param("instanceId") String instanceId);

}
