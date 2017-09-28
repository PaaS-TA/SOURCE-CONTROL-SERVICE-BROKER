package org.paasta.servicebroker.sourcecontrol.model;

import org.openpaas.servicebroker.model.*;
import org.paasta.servicebroker.sourcecontrol.TestConstants;

import java.util.HashMap;
import java.util.Map;

public class RequestFixture {


    public static Map<String, Object> getParameters(String num) {

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("owner", "testuser"+num);
        parameters.put("org_name", "org_name_"+num);
        return parameters;
    }

    public static PreviousValues getPreviousValues() {

        CreateServiceInstanceRequest createServiceInstanceRequest = getCreateServiceInstanceRequest();

        return new PreviousValues(createServiceInstanceRequest.getPlanId(),
                createServiceInstanceRequest.getServiceDefinitionId(),
                createServiceInstanceRequest.getOrganizationGuid(),
                createServiceInstanceRequest.getSpaceGuid());

    }

    public static CreateServiceInstanceRequest getCreateServiceInstanceRequest() {

        ServiceDefinition service = ServiceDefinitionFixture.getService();

        return new CreateServiceInstanceRequest(
                service.getId(),
                service.getPlans().get(0).getId(),
                TestConstants.ORG_GUID_001,
                TestConstants.SPACE_GUID_001,
                getParameters("001")
        );
    }

    public static CreateServiceInstanceRequest getCreateServiceInstanceRequest2() {

        ServiceDefinition service = ServiceDefinitionFixture.getService();

        return new CreateServiceInstanceRequest(
                service.getId(),
                service.getPlans().get(0).getId(),
                TestConstants.ORG_GUID_002,
                TestConstants.SPACE_GUID_002,
                getParameters("002")
        );
    }

    public static CreateServiceInstanceBindingRequest getCreateServiceInstanceBindingRequest() {

        ServiceDefinition service = ServiceDefinitionFixture.getService();

        return new CreateServiceInstanceBindingRequest(
                service.getId(),
                service.getPlans().get(0).getId(),
                "app_guid");
    }

    public static DeleteServiceInstanceRequest getDeleteServiceInstanceRequest() {

        ServiceInstance service = ServiceInstanceFixture.getServiceInstance();

        return new DeleteServiceInstanceRequest(
                service.getServiceInstanceId(),
                service.getServiceDefinitionId(),
                service.getPlanId());
    }

    public static UpdateServiceInstanceRequest getUpdateServiceInstanceRequest() {

        ServiceInstance service = ServiceInstanceFixture.getServiceInstance();

        return new UpdateServiceInstanceRequest(
                service.getPlanId(),
                service.getServiceDefinitionId());
    }

    public static DeleteServiceInstanceBindingRequest getDeleteServiceInstanceBindingRequest() {

        ServiceInstance service = ServiceInstanceFixture.getServiceInstance();

        return new DeleteServiceInstanceBindingRequest(
                "test_bindingId",
                service,
                service.getServiceDefinitionId(),
                service.getPlanId());
    }

}
