package org.paasta.servicebroker.sourcecontrol.service.impl;

import org.openpaas.servicebroker.model.CreateServiceInstanceRequest;
import org.openpaas.servicebroker.model.ServiceInstance;
import org.paasta.servicebroker.sourcecontrol.exception.SourceControlServiceException;
import org.paasta.servicebroker.sourcecontrol.model.*;
import org.paasta.servicebroker.sourcecontrol.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * SCM Manager Server 접속 및 사용자/Service Instance 제어
 * Created by lena on 2017-05-16.
 */
@Service
public class SourceControlAdminService {

	/**
	 * The Admin id
	 */
	@Value("${admin.id}")
	String adminId;

	/**
	 * The Admin pwd.
	 */
	@Value("${admin.pwd}")
	String adminPwd;

	/**
	 * The Api Repositories.
	 */
	@Value("${api.base}")
	String apiBase;

	/**
	 * The Api Repositories.
	 */
	@Value("${api.repo}")
	String apiRepo;

	/**
	 * The Api users.
	 */
	@Value("${api.users}")
	String apiUsers;

	/**
	 * Request parameter key : owner
	 */
	@Value("${param.key.owner}")
	String paramOwner;

	/**
	 * Request parameter key : orgname
	 */
	@Value("${param.key.orgname}")
	String paramOrgname;

	/**
	 * The Jpa service instance repository.
	 */
	@Autowired
	JpaServiceInstanceRepository jpaServiceInstanceRepository;
	/**
	 * The Jpa sc user repository.
	 */
	@Autowired
	JpaScUserRepository jpaScUserRepository;

	/**
	 * The Jpa sc instance user repository.
	 */
	@Autowired
	JpaScInstanceUserRepository jpaScInstanceUserRepository;

	/**
	 * The Jpa sc repository repository.
	 */
	@Autowired
	JpaScRepositoryRepository jpaScRepositoryRepository;

	/**
	 * The Jpa sc repo permission repository.
	 */
	@Autowired
	JpaScRepoPermissionRepository jpaScRepoPermissionRepository;

	private static final Logger logger = LoggerFactory.getLogger(SourceControlAdminService.class);

	// [ 내부 DB Access ]=================================================================================================

	/**
	 * Find by id service instance.
	 *
	 * @param instanceId the instance id
	 * @return the service instance
	 */
	public ServiceInstance findById(String instanceId) {

		JpaServiceInstance jpaServiceInstance = jpaServiceInstanceRepository.findOne(instanceId);
		ServiceInstance serviceInstance = null;

		if (jpaServiceInstance != null) {
			serviceInstance = new ServiceInstance(new CreateServiceInstanceRequest(jpaServiceInstance.getInstanceId(),
					jpaServiceInstance.getPlanId(), jpaServiceInstance.getOrganizationGuid(), jpaServiceInstance.getSpaceGuid()).withServiceInstanceId(jpaServiceInstance.getInstanceId()));
		}
		return serviceInstance;
	}

	/**
	 * Find by org guid service instance.
	 *
	 * @param orgGuId the org gu id
	 * @return the service instance
	 */
	public ServiceInstance findByOrgGuid(String orgGuId) {
		JpaServiceInstance jpaServiceInstanceOrg = jpaServiceInstanceRepository.findDistinctFirstByOrganizationGuid(orgGuId);

		ServiceInstance serviceInstance = null;

		if (jpaServiceInstanceOrg != null) {
			serviceInstance = new ServiceInstance(new CreateServiceInstanceRequest(jpaServiceInstanceOrg.getInstanceId(),
					jpaServiceInstanceOrg.getPlanId(), jpaServiceInstanceOrg.getOrganizationGuid(), jpaServiceInstanceOrg.getSpaceGuid()).withServiceInstanceId(jpaServiceInstanceOrg.getInstanceId()));
		}
		return serviceInstance;
	}

	/**
	 * Create user.
	 *
	 * @param request the request
	 * @throws SourceControlServiceException the source control service exception
	 */
	public void createUser(CreateServiceInstanceRequest request) throws SourceControlServiceException {

		// [내부 DB] "형상관리 사용자 : SC_USER" 테이블에 데이터 생성
		String owner = (String) request.getParameters().get(paramOwner);
		jpaScUserRepository.save(JpaScUser.builder().userId(owner).userName(null).userMail(owner).userDesc(null).build());
	}

	/**
	 * Create sc instance user.
	 *
	 * @param request the request
	 * @throws SourceControlServiceException the source control service exception
	 */
	public void createScInstanceUser(CreateServiceInstanceRequest request) throws SourceControlServiceException {

		String owner = (String) request.getParameters().get(paramOwner);

		// 내부 DB "형상관리 서비스인스턴스_사용자_관계 : SC_INSTANCE_USER" 테이블에 데이터 생성
		JpaScInstanceUser jpaScInstanceUser = JpaScInstanceUser.builder()
				.instanceId(request.getServiceInstanceId())
				.userId(owner)
				.repoRole("owner")
				.createrYn("Y").build();

		jpaScInstanceUserRepository.save(jpaScInstanceUser);
	}

	/**
	 * Create service instance.
	 *
	 * @param serviceInstance the service instance
	 * @param request         the request
	 */
	public void createServiceInstance(ServiceInstance serviceInstance, CreateServiceInstanceRequest request) {

		String orgname = (String) request.getParameters().get(paramOrgname);
		String owner = (String) request.getParameters().get(paramOwner);

		JpaServiceInstance jpaServiceInstance = JpaServiceInstance.builder()
				.instanceId(serviceInstance.getServiceInstanceId())
				.organizationGuid(serviceInstance.getOrganizationGuid())
				.organizationName(orgname)
				.planId(serviceInstance.getPlanId())
				.serviceId(serviceInstance.getServiceDefinitionId())
				.spaceGuid(serviceInstance.getSpaceGuid())
				.createUserId(owner)
				.createdTime(getCurrentDateTime()).build();

		jpaServiceInstanceRepository.save(jpaServiceInstance);
	}

	/**
	 * Delete sc instance user.
	 *
	 * @param serviceInstanceId the service instance id
	 * @return the list
	 * @throws SourceControlServiceException the source control service exception
	 */
	public List<String> deleteScInstanceUser(String serviceInstanceId) throws SourceControlServiceException {

		// 삭제 대상 사용자 조회 (삭제할 서비스인스턴스에만 매핑되어 있는 사용자)
		List<JpaScInstanceUser> jpaScInstanceUsers = jpaScInstanceUserRepository.selectDeleteScInstanceUser(serviceInstanceId);
		List<String> deleteUserList = new ArrayList<>();

		// [내부 DB] "형상관리 서비스인스턴스_사용자_관계 : SC_INSTANCE_USER" 테이블에 데이터 삭제
		int count = jpaScInstanceUserRepository.deleteAllByInstanceId(serviceInstanceId);
		logger.info("[ServiceBroker : deleteScInstanceUser : instance_id ={}, count={}", serviceInstanceId, count);

		// [내부 DB] "형상관리 사용자 : SC_USER" 테이블에 데이터 삭제
		jpaScInstanceUsers.stream().forEach(e -> {
			logger.info("[ServiceBroker : selectScInstanceUser : instanceId ={}, userId ={}", e.getUserId(), e.getInstanceId());
			jpaScUserRepository.delete(e.getUserId());
			deleteUserList.add(e.getUserId());
		});

		return deleteUserList;

	}

	/**
	 * Delete service instance.
	 *
	 * @param serviceInstanceId the service instance id
	 */
	public void deleteServiceInstance(String serviceInstanceId) {
		jpaServiceInstanceRepository.delete(serviceInstanceId);
		logger.info("[ServiceBroker : deleteServiceInstance : instance_id ={}", serviceInstanceId);
	}

	/**
	 * Delete repositories.
	 *
	 * @param instanceid the instanceid
	 */
	public void deleteRepositories(String instanceid) {

		// ------------- [내부 DB] Repository 정보 삭제
		List<JpaScRepository> scRepositories = jpaScRepositoryRepository.findAllByInstanceId(instanceid);
		// Repository Permission 정보 삭제
		scRepositories.forEach(e -> {
			jpaScRepoPermissionRepository.deleteAllByRepoNo(e.getRepoNo());
		});
		// Repository 정보 삭제
		jpaScRepositoryRepository.deleteAllByInstanceId(instanceid);
	}

	// [ API Access ]=================================================================================================

	/**
	 * Is exist user list.
	 *
	 * @param request the request
	 * @return the list
	 * @throws SourceControlServiceException the source control service exception
	 */
	public List<User> isExistUser(CreateServiceInstanceRequest request) throws SourceControlServiceException {

		// scm manager api 호출 - 사용자 정보 조회
		return isExistUserByApi(request);
	}

	/**
	 * Create user api.
	 *
	 * @param request the request
	 * @throws SourceControlServiceException the source control service exception
	 */
	public void createUserApi(CreateServiceInstanceRequest request) throws SourceControlServiceException {
		// [API 호출] - 사용자 신규 생성
		createUserByApi(request);
	}

	/**
	 * Delete User api.
	 *
	 * @param deleteUser the deleteUserList
	 */
	public void deleteUserApi(List<String> deleteUser) {
		// ------------- [API 호출] User 정보 삭제
		deleteUser.forEach(e -> deleteUserByApi(e));
	}

	/**
	 * Delete repositories api.
	 *
	 * @param instanceid the instanceid
	 */
	public void deleteRepositoriesApi(String instanceid) {
		// ------------- [API 호출] Repository 정보 삭제
		// Service Instance Repository 검색
		List<Repository> repositories = instanceRepositories(instanceid);
		// Repository 삭제
		repositories.forEach(e -> {deleteRepositoryApi(e.getId());});
	}

	// [ private Method ]=================================================================================================
	private List<User> isExistUserByApi(CreateServiceInstanceRequest request) throws SourceControlServiceException {
		RestTemplate restTemplate = new RestTemplate();
		String owner = (String) request.getParameters().get(paramOwner);

		List<User> users = new ArrayList<User>();

		ParameterizedTypeReference<List<User>> responseType = new ParameterizedTypeReference<List<User>>() {};
		HttpEntity<Object> entity = restCommonHeaders(null);

		try {
			ResponseEntity<List<User>> response = restTemplate.exchange(apiBase + apiUsers, HttpMethod.GET, entity, responseType);
			response.getBody().forEach(e -> {if (owner.equals(e.getName())) {users.add(e);}});

		} catch (HttpServerErrorException e) {
			e.printStackTrace();
			throw new SourceControlServiceException("User ID Check Error : User ID [ "+owner+"] : " + e.getStatusCode()+e.getResponseBodyAsString());
		} catch (Exception e) {
			throw new SourceControlServiceException("User ID Check Error : User ID [ "+owner+"] : " + e.getMessage());
		}

		return  users;
	}

	private void createUserByApi(CreateServiceInstanceRequest request) throws SourceControlServiceException {

		RestTemplate restTemplate = new RestTemplate();
		String owner = (String) request.getParameters().get(paramOwner);

		User user = User.builder()
				.name(owner)
				.displayName(owner)
				.admin(true)
				.active(false)
				.type("xml")
				.properties(new ArrayList<Map<String, String>>())
				.build();

		try {
			HttpEntity<Object> entity = restCommonHeaders(user);

			ResponseEntity<Map> response = restTemplate.exchange(apiBase + apiUsers, HttpMethod.POST, entity, Map.class);
		} catch (HttpServerErrorException e) {
			e.printStackTrace();
			throw new SourceControlServiceException("User creation failed. : User ID [ "+owner+"] : "+e.getStatusCode()+e.getResponseBodyAsString());
		} catch (Exception e) {
			e.printStackTrace();
			throw new SourceControlServiceException("User creation failed.: User ID [ "+owner+"] "+ e.getMessage());
		}

	}

	private void deleteUserByApi(String userId){

		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<Object> entity = restCommonHeaders(null);
		String url = apiBase + apiUsers+"/"+userId;
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.DELETE, entity, String.class);
	}

	private List<Repository> instanceRepositories(String instanceId) {
		// 모든 Repository 조회
		// GET : /api/rest/repositories
		HttpEntity<Object> entity = restCommonHeaders(null);

		ParameterizedTypeReference<List<Repository>> responseType = new ParameterizedTypeReference<List<Repository>>() {};
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<List<Repository>> response = null;

		response = restTemplate.exchange(apiBase + apiRepo, HttpMethod.GET, entity, responseType);
		List<Repository> repositories = response.getBody();

		List<Repository> instanceRepositories = new ArrayList<Repository>();

		repositories.forEach(e -> {
			if (e.getProperties() != null && e.getProperties().stream().filter(
					pr -> "instance_id".equals(pr.get("key")) && instanceId.equals(pr.get("value"))).count() > 0) {
				instanceRepositories.add(e);
			}
		});
		return instanceRepositories;
	}

	private void deleteRepositoryApi(String id) {

		// scm-manager delete repository
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<Object> entity = restCommonHeaders(null);
		String url = apiBase + apiRepo+"/"+id;
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.DELETE, entity, String.class);
	}



	// scmmanager HttpHeaders common
	private HttpEntity<Object> restCommonHeaders(Object param) {

		String basicAuth = "Basic " + (Base64.getEncoder().encodeToString((adminId + ":" + adminPwd).getBytes()));

		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", basicAuth);
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

		HttpEntity<Object> entity = new HttpEntity<Object>(param, headers);

		return entity;
	}

	private String getCurrentDateTime() {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm", Locale.KOREA);
		String currentDateTime = sdf.format(new Date());

		return currentDateTime;
	}

}
