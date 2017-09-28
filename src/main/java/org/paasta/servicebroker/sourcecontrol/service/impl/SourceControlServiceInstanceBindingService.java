package org.paasta.servicebroker.sourcecontrol.service.impl;

import org.openpaas.servicebroker.exception.ServiceBrokerException;
import org.openpaas.servicebroker.exception.ServiceInstanceBindingExistsException;
import org.openpaas.servicebroker.model.CreateServiceInstanceBindingRequest;
import org.openpaas.servicebroker.model.DeleteServiceInstanceBindingRequest;
import org.openpaas.servicebroker.model.ServiceInstanceBinding;
import org.openpaas.servicebroker.service.ServiceInstanceBindingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Created by lena on 2017-05-16.
 */
@Service
public class SourceControlServiceInstanceBindingService implements ServiceInstanceBindingService {

	private static final Logger logger = LoggerFactory.getLogger(SourceControlServiceInstanceBindingService.class);

	@Override
	public ServiceInstanceBinding createServiceInstanceBinding(
			CreateServiceInstanceBindingRequest request)
			throws ServiceInstanceBindingExistsException, ServiceBrokerException {

		logger.debug("ScmManagerServiceInstanceBindingService CLASS createServiceInstanceBinding");
		logger.debug("ServiceInstanceBinding not supported.");

		throw new ServiceBrokerException("Not Supported");

	}

	@Override
	public ServiceInstanceBinding deleteServiceInstanceBinding(DeleteServiceInstanceBindingRequest request)
			throws ServiceBrokerException {
		logger.debug("ScmManagerServiceInstanceBindingService CLASS deleteServiceInstanceBinding");
		logger.debug("ServiceInstanceBinding not supported");

		throw new ServiceBrokerException("Not Supported");

	}

}
