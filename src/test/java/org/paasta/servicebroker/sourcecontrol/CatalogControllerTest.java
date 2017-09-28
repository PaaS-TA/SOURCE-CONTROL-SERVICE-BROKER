package org.paasta.servicebroker.sourcecontrol;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.openpaas.servicebroker.controller.CatalogController;
import org.openpaas.servicebroker.model.Catalog;
import org.openpaas.servicebroker.service.CatalogService;
import org.paasta.servicebroker.sourcecontrol.config.CatalogConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Base64;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by user on 2017-09-12.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@TestPropertySource("classpath:test.properties")
public class CatalogControllerTest {

    @InjectMocks
    CatalogController controller;

    @Mock
    CatalogService catalogService;

    @Spy
    CatalogConfig catalogConfig;

    @Value("${broker.api.version}") String broker_api_version;
    @Value("${broker.auth.user}") String broker_auth_user;
    @Value("${broker.auth.pwd}") String broker_auth_pwd;

    private MockMvc mockMvc;

    private Catalog catalog;

    private String basicAuth;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        catalog = catalogConfig.catalog();

        basicAuth = "Basic " + (Base64.getEncoder().encodeToString((broker_auth_user + ":" + broker_auth_pwd).getBytes()));
        // CatalogController 를 MockMvC 객체로 만듬.
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void getCatalog() throws Exception {

        when(catalogService.getCatalog()).thenReturn(catalog);

        MvcResult result = this.mockMvc.perform(get(CatalogController.BASE_PATH)
                .header(TestConstants.Headers_KEY_API_VERSION, broker_api_version)
                .header(TestConstants.Headers_KEY_AUTHORIZATION, basicAuth)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.services", hasSize(1)))
                .andExpect(jsonPath("$.services[*].id", containsInAnyOrder(catalog.getServiceDefinitions().get(0).getId())))
                .andExpect(jsonPath("$.services[*].name", containsInAnyOrder(catalog.getServiceDefinitions().get(0).getName())))
                .andDo(print())
                .andReturn();
    }
}
