package org.paasta.servicebroker.sourcecontrol;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.openpaas.servicebroker.model.CreateServiceInstanceRequest;
import org.openpaas.servicebroker.model.ServiceInstance;
import org.paasta.servicebroker.sourcecontrol.model.*;
import org.paasta.servicebroker.sourcecontrol.repository.*;
import org.paasta.servicebroker.sourcecontrol.service.impl.SourceControlAdminService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Created by user on 2017-09-13.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@TestPropertySource("classpath:test.properties")
public class SourceControlAdminServiceTest {

    @Mock
    JpaServiceInstanceRepository jpaServiceInstanceRepository;
    @Mock
    JpaScUserRepository jpaScUserRepository;
    @Mock
    JpaScInstanceUserRepository jpaScInstanceUserRepository;
    @Mock
    JpaScRepositoryRepository jpaScRepositoryRepository;
    @Mock
    JpaScRepoPermissionRepository jpaScRepoPermissionRepository;

    @InjectMocks
    SourceControlAdminService sourceControlAdminService;

    @Value("${api.base}") String apiBase;
    @Value("${api.users}") String apiUsers;
    @Value("${api.repo}") String apiRepo;
    @Value("${admin.id}") String adminId;
    @Value("${admin.pwd}") String adminPwd;

    private static String spyTestUser = null;

    @Before
    public void setup() throws Exception {

        MockitoAnnotations.initMocks(this);

        ReflectionTestUtils.setField(sourceControlAdminService, "adminId", adminId);
        ReflectionTestUtils.setField(sourceControlAdminService, "adminPwd", adminPwd);
        ReflectionTestUtils.setField(sourceControlAdminService, "apiBase", apiBase);
        ReflectionTestUtils.setField(sourceControlAdminService, "apiRepo", apiRepo);
        ReflectionTestUtils.setField(sourceControlAdminService, "apiUsers", apiUsers);
        ReflectionTestUtils.setField(sourceControlAdminService, "paramOwner", TestConstants.PARAM_KEY_OWNER);
        ReflectionTestUtils.setField(sourceControlAdminService, "paramOrgname", TestConstants.PARAM_KEY_ORGNAME);

        Random rnd = new Random();
        StringBuilder sb = new StringBuilder();

        for(int i=0;i<10;i++) {
            sb.append(String.valueOf((char)((int)(rnd.nextInt(26)) + 97)));
        }
        spyTestUser = "mokito_"+sb.toString() + "@" + sb.toString() +".com";
    }

    @Test
    public void test_findById() throws Exception{

        JpaServiceInstance jpaServiceInstance = JpaRepositoryFixture.getJpaServiceInstance();

        when(jpaServiceInstanceRepository.findOne(anyString())).thenReturn(jpaServiceInstance);

        ServiceInstance serviceInstance = sourceControlAdminService.findById(jpaServiceInstance.getInstanceId());

        assertThat(serviceInstance.getServiceInstanceId(), is(jpaServiceInstance.getInstanceId()));
        assertThat(serviceInstance.getPlanId(), is(jpaServiceInstance.getPlanId()));
        assertThat(serviceInstance.getOrganizationGuid(), is(jpaServiceInstance.getOrganizationGuid()));
        assertThat(serviceInstance.getSpaceGuid(), is(jpaServiceInstance.getSpaceGuid()));
    }

    @Test
    public void test_findById_case_null() throws Exception{

        when(jpaServiceInstanceRepository.findOne(anyString())).thenReturn(null);

        ServiceInstance serviceInstance = sourceControlAdminService.findById(TestConstants.SV_INSTANCE_ID_001);

        assertThat(serviceInstance, is(nullValue()));
    }

    @Test
    public void test_findByOrgGuid() throws Exception{

        JpaServiceInstance jpaServiceInstance = JpaRepositoryFixture.getJpaServiceInstance();

        when(jpaServiceInstanceRepository.findDistinctFirstByOrganizationGuid(anyString())).thenReturn(jpaServiceInstance);

        ServiceInstance serviceInstance = sourceControlAdminService.findByOrgGuid(jpaServiceInstance.getOrganizationGuid());

        assertThat(serviceInstance.getServiceInstanceId(), is(jpaServiceInstance.getInstanceId()));
        assertThat(serviceInstance.getPlanId(), is(jpaServiceInstance.getPlanId()));
        assertThat(serviceInstance.getOrganizationGuid(), is(jpaServiceInstance.getOrganizationGuid()));
        assertThat(serviceInstance.getSpaceGuid(), is(jpaServiceInstance.getSpaceGuid()));
    }

    @Test
    public void test_findByOrgGuid_case_null() throws Exception{

        when(jpaServiceInstanceRepository.findDistinctFirstByOrganizationGuid(anyString())).thenReturn(null);

        ServiceInstance serviceInstance = sourceControlAdminService.findByOrgGuid(TestConstants.SV_INSTANCE_ID_001);

        assertThat(serviceInstance, is(nullValue()));
    }

    @Test
    public void test_createUser() throws Exception{

        JpaScUser jpaScUser = JpaRepositoryFixture.getJpaScUser();

        when(jpaScUserRepository.save(any(JpaScUser.class))).thenReturn(jpaScUser);

        sourceControlAdminService.createUser(RequestFixture.getCreateServiceInstanceRequest());

        verify(jpaScUserRepository).save(any(JpaScUser.class));

    }

    @Test
    public void test_createScInstanceUser() throws Exception{
        JpaScInstanceUser jpaScInstanceUser = JpaRepositoryFixture.getJpaScInstanceUser();

        when(jpaScInstanceUserRepository.save(any(JpaScInstanceUser.class))).thenReturn(jpaScInstanceUser);

        sourceControlAdminService.createScInstanceUser(RequestFixture.getCreateServiceInstanceRequest());

        verify(jpaScInstanceUserRepository).save(any(JpaScInstanceUser.class));

    }

    @Test
    public void test_createServiceInstance() throws Exception{

        JpaServiceInstance jpaServiceInstance = JpaRepositoryFixture.getJpaServiceInstance();

        when(jpaServiceInstanceRepository.save(any(JpaServiceInstance.class))).thenReturn(jpaServiceInstance);

        sourceControlAdminService.createServiceInstance(ServiceInstanceFixture.getServiceInstance(),
                RequestFixture.getCreateServiceInstanceRequest());

        verify(jpaServiceInstanceRepository).save(any(JpaServiceInstance.class));

    }

    @Test
    public void test_deleteScInstanceUser() throws Exception{

        List<JpaScInstanceUser> jpaScInstanceUsers = JpaRepositoryFixture.getJpaScInstanceUsers();

        when(jpaScInstanceUserRepository.selectDeleteScInstanceUser(anyString())).thenReturn(jpaScInstanceUsers);

        when(jpaScInstanceUserRepository.deleteAllByInstanceId(anyString())).thenReturn(2);

        List<String> deleteUserList = sourceControlAdminService.deleteScInstanceUser(RequestFixture.getCreateServiceInstanceRequest().getServiceInstanceId());

        assertThat(deleteUserList.size(), is(2));
        assertThat(deleteUserList.get(0), is(RequestFixture.getCreateServiceInstanceRequest().getParameters().get("owner")));
        assertThat(deleteUserList.get(1), is(RequestFixture.getCreateServiceInstanceRequest2().getParameters().get("owner")));

    }

    @Test
    public void test_deleteServiceInstance() throws Exception{

        doNothing().when(jpaServiceInstanceRepository).delete(TestConstants.SV_INSTANCE_ID_001);

        sourceControlAdminService.deleteServiceInstance(TestConstants.SV_INSTANCE_ID_001);

        verify(jpaServiceInstanceRepository).delete(TestConstants.SV_INSTANCE_ID_001);

    }

    @Test
    public void test_deleteRepositories() throws Exception{

        List<JpaScRepository> scRepositories = JpaRepositoryFixture.getJpaScRepositories();

        when(jpaScRepositoryRepository.findAllByInstanceId(TestConstants.SV_INSTANCE_ID_001)).thenReturn(scRepositories);

        doNothing().when(jpaScRepoPermissionRepository).deleteAllByRepoNo(anyInt());

        doNothing().when(jpaScRepositoryRepository).deleteAllByInstanceId(TestConstants.SV_INSTANCE_ID_001);

        sourceControlAdminService.deleteRepositories(TestConstants.SV_INSTANCE_ID_001);

        verify(jpaScRepositoryRepository).findAllByInstanceId(TestConstants.SV_INSTANCE_ID_001);
        verify(jpaScRepoPermissionRepository).deleteAllByRepoNo(1);
        verify(jpaScRepoPermissionRepository).deleteAllByRepoNo(2);
        verify(jpaScRepositoryRepository).deleteAllByInstanceId(TestConstants.SV_INSTANCE_ID_001);
    }

    @Test
    public void test_userApi() throws Exception{

        CreateServiceInstanceRequest request = RequestFixture.getCreateServiceInstanceRequest();
        request.getParameters().put("owner", spyTestUser);

        SourceControlAdminService service = spy(new SourceControlAdminService());

        // createUserApi
        sourceControlAdminService.createUserApi(request);

        // isExistUser
        List<User> userList = sourceControlAdminService.isExistUser(request);
        assertThat(userList.size()>0, is(true));

        // deleteUserApi
        List<String> delUserList = new ArrayList<>();
        userList.forEach(e-> delUserList.add(e.getName()));

        sourceControlAdminService.deleteUserApi(delUserList);

        userList = sourceControlAdminService.isExistUser(request);
        assertThat(userList.size()==0, is(true));

    }

    @Test
    public void test_deleteRepositoriesApi() throws Exception{

        CreateServiceInstanceRequest request = RequestFixture.getCreateServiceInstanceRequest();

        SourceControlAdminService service = spy(new SourceControlAdminService());

        sourceControlAdminService.deleteRepositoriesApi(TestConstants.SV_INSTANCE_ID_001);

    }

}
