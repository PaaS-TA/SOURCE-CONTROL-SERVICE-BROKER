package org.paasta.servicebroker.sourcecontrol.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


/**
 * Created by lena on 2017-05-16.
 */

@Configuration
@EnableJpaRepositories("org.paasta.servicebroker.sourcecontrol.repository")
@EntityScan(value = "org.paasta.servicebroker.sourcecontrol.model")
@ComponentScan(basePackages = { "org.openpaas.servicebroker", "org.paasta.servicebroker"})
public class BrokerConfig {

}
