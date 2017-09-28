package org.paasta.servicebroker.sourcecontrol.model;

import org.openpaas.servicebroker.model.Plan;
import org.openpaas.servicebroker.model.ServiceDefinition;
import org.paasta.servicebroker.sourcecontrol.TestConstants;

import java.util.*;

public class ServiceDefinitionFixture {

    public static ServiceDefinition getService() {

        return new ServiceDefinition(
                TestConstants.SERVICEDEFINITION_ID,
                TestConstants.SERVICEDEFINITION_NAME,
                "A paasta source control service for application development.provision parameters : parameters {owner_id : {ownerid}, org_name:{organizationName}}",
                false,
                false,
                Arrays.asList(
                        new Plan(TestConstants.SERVICEDEFINITION_PLAN_ID,
                                "Default",
                                "This is a default service plan. All services are created equally.",
                                null)),
                Arrays.asList("paasta-sourcecontrol"),
                getMetadata(),
                null,
                null);
    }

    /* Service Metadata */
    private static Map<String,Object> getMetadata() {
        // Service Metadata
        Map<String,Object> metadata = new HashMap<String,Object>();
        metadata.put("displayName", TestConstants.SERVICEDEFINITION_NAME);
        metadata.put("imageUrl","imageUrl");
        metadata.put("longDescription","longDescription");
        metadata.put("providerDisplayName","providerDisplayName");
        metadata.put("documentationUrl","documentationUrl");
        metadata.put("supportUrl","supportUrl");
        return metadata;
    }

    public static List<ServiceDefinition> getCatalog() {
        List<ServiceDefinition> result = new ArrayList<>();
        result.add(getService());

        return result;
    }
}
