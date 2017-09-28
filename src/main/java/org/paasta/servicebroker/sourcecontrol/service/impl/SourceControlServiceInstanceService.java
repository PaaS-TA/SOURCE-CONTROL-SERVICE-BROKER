package org.paasta.servicebroker.sourcecontrol.service.impl;


import org.openpaas.servicebroker.exception.ServiceBrokerException;
import org.openpaas.servicebroker.exception.ServiceInstanceDoesNotExistException;
import org.openpaas.servicebroker.exception.ServiceInstanceExistsException;
import org.openpaas.servicebroker.exception.ServiceInstanceUpdateNotSupportedException;
import org.openpaas.servicebroker.model.CreateServiceInstanceRequest;
import org.openpaas.servicebroker.model.DeleteServiceInstanceRequest;
import org.openpaas.servicebroker.model.ServiceInstance;
import org.openpaas.servicebroker.model.UpdateServiceInstanceRequest;
import org.openpaas.servicebroker.service.ServiceInstanceService;
import org.paasta.servicebroker.sourcecontrol.model.JpaScInstanceUser;
import org.paasta.servicebroker.sourcecontrol.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by lena on 2017-05-16.
 */
@Service
public class SourceControlServiceInstanceService implements ServiceInstanceService {

	private static final Logger logger = LoggerFactory.getLogger(SourceControlServiceInstanceService.class);

	@Autowired
	private SourceControlAdminService sourceControlAdminService;

	/**
	 * The dashbord url
	 */
	@Value("${dashboard.url}")
	String dashboard_url;

	/**
	 * Request parameter key : owner
	 */
	@Value("${param.key.owner}")
	String param_owner;

	/**
	 * Request parameter key : orgname
	 */
	@Value("${param.key.orgname}")
	String param_orgname;

	/**
	 * Provision -  PUT /v2/service_instances/:instance_id
	 *
	 * step1.
	 * */
	@Override
	@Transactional
	public ServiceInstance createServiceInstance(CreateServiceInstanceRequest request) 
			throws ServiceInstanceExistsException, ServiceBrokerException {

		logger.debug("ScmManagerServiceInstanceService : Provision (Create) createServiceInstance");

		// [ 유효성 체크 ]=================================================================================================
		// 파라미터 체크
		if (request.getParameters() == null || request.getParameters().isEmpty() ||
				!(request.getParameters().containsKey(param_owner) && request.getParameters().containsKey(param_orgname))) {
			throw new ServiceBrokerException("Required "+param_owner+" and "+param_orgname+" parameter.");
		}

		// 서비스 인스턴스 Guid Check
		ServiceInstance serviceInstance = sourceControlAdminService.findById(request.getServiceInstanceId());

		if(serviceInstance != null) {
			logger.error("ServiceInstance : {} OR OrgGuid : {} is exist.", request.getServiceInstanceId(), request.getOrganizationGuid());
			throw new ServiceInstanceExistsException(new ServiceInstance(request));
		}

		// 조직 GUID check (방침 : space 구분 없이 조직별 1개만 존재)
		ServiceInstance serviceInstanceOrg = sourceControlAdminService.findByOrgGuid(request.getOrganizationGuid());
		if(serviceInstanceOrg != null) {
			logger.error("ServiceInstance already exists in your organization: OrganizationGuid : {}, spaceId : {}", request.getOrganizationGuid(), request.getSpaceGuid());
			throw new ServiceBrokerException("ServiceInstance already exists in your organization.");
		}

		// [ 내부 DB Access ]=================================================================================================
		// [내부 DB] - 사용자 계정 Check : 신규 생성 / 사용자 정보 변경(해당없음)
		List<User> users = sourceControlAdminService.isExistUser(request);
		if (users.isEmpty()) {
			sourceControlAdminService.createUser(request);
		}

		// [내부 DB] - ServiceInstance <-> 사용자 계정 매핑
		sourceControlAdminService.createScInstanceUser(request);

		// dashboard_url 수정
		String dashboardUrl = dashboard_url.replace("{instanceId}", request.getServiceInstanceId());
		//dashboardUrl = dashboardUrl.replace("{userId}", (String) request.getParameters().get("user_id"));

		// [내부 DB] - 서비스 인스턴스 정보 저장
		ServiceInstance instance = new ServiceInstance(request).withDashboardUrl(dashboardUrl);
		sourceControlAdminService.createServiceInstance(instance, request);

		// [ API Access ]=================================================================================================
		// step3. 사용자 계정 check
		if (users.isEmpty()) {
			sourceControlAdminService.createUserApi(request);
		}

		return instance;
	}

	@Override
	public ServiceInstance getServiceInstance(String id) {

		return sourceControlAdminService.findById(id);
	}

	@Override
	@Transactional
	public ServiceInstance deleteServiceInstance(DeleteServiceInstanceRequest request) throws ServiceBrokerException {

		// ServiceInstanceId로 ServiceInstance 정보 조회
		ServiceInstance serviceInstance = sourceControlAdminService.findById(request.getServiceInstanceId());

		if (serviceInstance == null) { return null; }

		// [ 내부 DB Access ]=================================================================================================

		// 조회된 ServiceInstance 정보로 해당 형상관리 서비스인스턴스_사용자_관계 : SC_INSTANCE_USER 정보, 삭제할 서비스인스턴스에만 매핑되어 있는 사용자 정보 삭제
		List<String> deleteUser = sourceControlAdminService.deleteScInstanceUser(request.getServiceInstanceId());

		// 해당 인스턴스의 Repository 정보 삭제
		sourceControlAdminService.deleteRepositories(request.getServiceInstanceId());
		// 조회된 ServiceInstance 정보로 해당 ServiceInstance 정보 삭제
		sourceControlAdminService.deleteServiceInstance(request.getServiceInstanceId());

		// [ API Access ]=================================================================================================
		// 해당 인스턴스의 사용자 삭제
		sourceControlAdminService.deleteUserApi(deleteUser);
		// 해당 인스턴스의 Repository 정보 삭제
		sourceControlAdminService.deleteRepositoriesApi(request.getServiceInstanceId());

		return serviceInstance;
	}

	@Override
	public ServiceInstance updateServiceInstance(UpdateServiceInstanceRequest request)
			throws ServiceInstanceUpdateNotSupportedException, ServiceBrokerException, ServiceInstanceDoesNotExistException {

		// ServiceInstanceId로 ServiceInstance 정보 조회
		// ServiceInstance 없을 경우 예외처리
		// 변경사항 반영 (planId 등등)
		throw new ServiceBrokerException("Not Supported");

	}

}