package org.paasta.servicebroker.sourcecontrol.service.impl;

import org.openpaas.servicebroker.model.Catalog;
import org.openpaas.servicebroker.model.ServiceDefinition;
import org.openpaas.servicebroker.service.CatalogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Catalog
 * Created by lena on 2017-05-16.
 */
@Service
public class SourceControlCatalogService implements CatalogService {

    private Catalog catalog;
    private Map<String,ServiceDefinition> serviceDefs = new HashMap<String,ServiceDefinition>();
    private static final Logger logger = LoggerFactory.getLogger(SourceControlCatalogService.class);

    /**
     * Instantiates a new Source control catalog service.
     *
     * @param catalog the catalog
     */
    @Autowired
    public SourceControlCatalogService(Catalog catalog) {
        this.catalog = catalog;
        initializeMap();
    }

    private void initializeMap() {
        for (ServiceDefinition def: catalog.getServiceDefinitions()) {
            serviceDefs.put(def.getId(), def);
        }
    }
    @Override
    public Catalog getCatalog() {
        return catalog;
    }

    @Override
    public ServiceDefinition getServiceDefinition(String serviceId) {
        return serviceDefs.get(serviceId);
    }
}
