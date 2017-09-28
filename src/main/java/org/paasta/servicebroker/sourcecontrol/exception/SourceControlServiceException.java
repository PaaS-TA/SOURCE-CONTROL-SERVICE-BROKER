package org.paasta.servicebroker.sourcecontrol.exception;

import org.openpaas.servicebroker.exception.ServiceBrokerException;


/**
 * SCM Manager ServiceBrokerException
 * Created by lena on 2017-05-16.
 */
public class SourceControlServiceException extends ServiceBrokerException {

	private static final long serialVersionUID = 8667141725171626000L;

	public SourceControlServiceException(String message) {
		super(message);
	}
	public SourceControlServiceException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
