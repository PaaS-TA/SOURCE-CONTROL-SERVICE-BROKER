package org.paasta.servicebroker.sourcecontrol;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Spy;
import org.openpaas.servicebroker.model.Catalog;
import org.openpaas.servicebroker.model.ServiceDefinition;
import org.paasta.servicebroker.sourcecontrol.model.ServiceDefinitionFixture;
import org.paasta.servicebroker.sourcecontrol.service.impl.SourceControlCatalogService;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by user on 2017-09-20.
 */
public class SourceControlCatalogServiceTest {

    @Spy
    SourceControlCatalogService sourceControlCatalogService;

    @Mock
    Catalog catalog;


    @Test
    public void test_getCatalog() throws Exception {
        catalog = new Catalog(ServiceDefinitionFixture.getCatalog());
        sourceControlCatalogService = new SourceControlCatalogService(catalog);

        Catalog result = sourceControlCatalogService.getCatalog();

        assertThat(result.getServiceDefinitions().get(0).getId(), is(TestConstants.SERVICEDEFINITION_ID));
        assertThat(result.getServiceDefinitions().get(0).getName(), is(TestConstants.SERVICEDEFINITION_NAME));
        assertThat(result.getServiceDefinitions().get(0).isBindable(), is(false));
        assertThat(result.getServiceDefinitions().get(0).isPlanUpdatable(), is(false));

        ServiceDefinition serviceDefinition = sourceControlCatalogService.getServiceDefinition(TestConstants.SERVICEDEFINITION_ID);
        assertThat(serviceDefinition.getId(), is(TestConstants.SERVICEDEFINITION_ID));
        assertThat(serviceDefinition.getName(), is(TestConstants.SERVICEDEFINITION_NAME));
        assertThat(serviceDefinition.isBindable(), is(false));
        assertThat(serviceDefinition.isPlanUpdatable(), is(false));

    }
}




