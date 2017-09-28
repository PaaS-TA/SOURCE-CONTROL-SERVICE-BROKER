package org.paasta.servicebroker.sourcecontrol.model;

import org.openpaas.servicebroker.model.CreateServiceInstanceRequest;
import org.openpaas.servicebroker.model.ServiceInstance;
import org.paasta.servicebroker.sourcecontrol.TestConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2017-09-14.
 */
public class JpaRepositoryFixture {

    public static JpaServiceInstance getJpaServiceInstance() {

        CreateServiceInstanceRequest createServiceInstanceRequest = RequestFixture.getCreateServiceInstanceRequest();
        ServiceInstance serviceInstance = ServiceInstanceFixture.getServiceInstance();

        JpaServiceInstance jpaServiceInstance = new JpaServiceInstance();

        new JpaServiceInstance(serviceInstance.getServiceInstanceId(),
                serviceInstance.getOrganizationGuid(),
                (String)createServiceInstanceRequest.getParameters().get("org_name"),
                createServiceInstanceRequest.getPlanId(),
                createServiceInstanceRequest.getServiceDefinitionId(),
                createServiceInstanceRequest.getSpaceGuid(),
                (String)createServiceInstanceRequest.getParameters().get("owner"),
                "20170901");

        return jpaServiceInstance;

    }

    public static JpaScUser getJpaScUser() {
        CreateServiceInstanceRequest createServiceInstanceRequest = RequestFixture.getCreateServiceInstanceRequest();

        return JpaScUser.builder()
                .userId((String)createServiceInstanceRequest.getParameters().get("owner"))
                .userName(null)
                .userMail((String)createServiceInstanceRequest.getParameters().get("owner"))
                .userDesc(null)
                .build();


    }

    public static List<JpaScInstanceUser> getJpaScInstanceUsers() {

        List<JpaScInstanceUser> jpaScInstanceUsers = new ArrayList<>();
        jpaScInstanceUsers.add(getJpaScInstanceUser());
        jpaScInstanceUsers.add(getJpaScInstanceUser2());

        return jpaScInstanceUsers;
    }

    public static JpaScInstanceUser getJpaScInstanceUser() {
        CreateServiceInstanceRequest createServiceInstanceRequest = RequestFixture.getCreateServiceInstanceRequest();
        ServiceInstance serviceInstance = ServiceInstanceFixture.getServiceInstance();

        return JpaScInstanceUser.builder()
                .instanceId(serviceInstance.getServiceInstanceId())
                .userId((String)createServiceInstanceRequest.getParameters().get("owner"))
                .repoRole("owner")
                .createrYn("Y").build();
    }

    public static JpaScInstanceUser getJpaScInstanceUser2() {
        CreateServiceInstanceRequest createServiceInstanceRequest = RequestFixture.getCreateServiceInstanceRequest2();
        ServiceInstance serviceInstance = ServiceInstanceFixture.getServiceInstance();

        return JpaScInstanceUser.builder()
                .instanceId(serviceInstance.getServiceInstanceId())
                .userId((String)createServiceInstanceRequest.getParameters().get("owner"))
                .repoRole("owner")
                .createrYn("Y").build();
    }

    public static List<JpaScRepository> getJpaScRepositories() {
        List<JpaScRepository> JpaScRepositories = new ArrayList<>();
        JpaScRepositories.add(getJpaScRepository());
        JpaScRepositories.add(getJpaScRepository2());

        return JpaScRepositories;
    }

    public static JpaScRepository getJpaScRepository() {

        return JpaScRepository.builder()
                .repoNo(1)
                .repoScmId("REPO_SCM_ID_001")
                .repoName("REPO_NAME_001")
                .repoDesc("REPO_DESC_001")
                .instanceId(TestConstants.SV_INSTANCE_ID_001)
                .ownerUserId("testowner_001")
                .createUserId("testuser_001").build();
    }

    public static JpaScRepository getJpaScRepository2() {

        return JpaScRepository.builder()
                .repoNo(2)
                .repoScmId("REPO_SCM_ID_002")
                .repoName("REPO_NAME_002")
                .repoDesc("REPO_DESC_002")
                .instanceId(TestConstants.SV_INSTANCE_ID_001)
                .ownerUserId("testowner_002")
                .createUserId("testuser_002").build();
    }

}