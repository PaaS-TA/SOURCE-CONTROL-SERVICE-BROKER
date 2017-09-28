package org.paasta.servicebroker.sourcecontrol;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.openpaas.servicebroker.exception.ServiceBrokerException;
import org.openpaas.servicebroker.model.CreateServiceInstanceBindingRequest;
import org.openpaas.servicebroker.model.DeleteServiceInstanceBindingRequest;
import org.paasta.servicebroker.sourcecontrol.model.RequestFixture;
import org.paasta.servicebroker.sourcecontrol.service.impl.SourceControlServiceInstanceBindingService;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Created by user on 2017-09-20.
 */
public class SourceControlServiceInstanceBindingServiceTest {

    @InjectMocks
    SourceControlServiceInstanceBindingService sourceControlServiceInstanceBindingService;


    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test_createServiceInstanceBinding() throws Exception {

        CreateServiceInstanceBindingRequest request = RequestFixture.getCreateServiceInstanceBindingRequest();

        assertThatThrownBy(() -> sourceControlServiceInstanceBindingService.createServiceInstanceBinding(request))
                .isInstanceOf(ServiceBrokerException.class).hasMessageContaining("Not Supported");
    }

    @Test
    public void test_deleteServiceInstanceBinding() throws Exception {

        DeleteServiceInstanceBindingRequest request = RequestFixture.getDeleteServiceInstanceBindingRequest();

        assertThatThrownBy(() -> sourceControlServiceInstanceBindingService.deleteServiceInstanceBinding(request))
                .isInstanceOf(ServiceBrokerException.class).hasMessageContaining("Not Supported");
    }


}
