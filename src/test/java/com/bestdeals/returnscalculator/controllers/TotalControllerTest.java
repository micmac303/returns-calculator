package com.bestdeals.returnscalculator.controllers;

import com.bestdeals.returnscalculator.Application;
import com.bestdeals.returnscalculator.model.Client;
import com.bestdeals.returnscalculator.model.Deal;
import com.bestdeals.returnscalculator.persistence.ClientRepository;
import com.bestdeals.returnscalculator.persistence.DealRepository;
import com.bestdeals.returnscalculator.services.ExchangeRateService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Map;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class TotalControllerTest {

    //System under test
    private TotalController totalController;

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private DealRepository dealRepository;

    @Autowired
    private ExchangeRateService exchangeRateService;

    @Before
    public void setup() {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
        totalController = new TotalController();

        dealRepository.deleteAllInBatch();

        ReflectionTestUtils.setField(totalController, "dealRepository", dealRepository);
        ReflectionTestUtils.setField(totalController, "exchangeRateService", exchangeRateService);

        Arrays.asList(1, 2, 3).stream().map(integer -> new Client()).forEach(clientRepository::save);
    }

    @Test
    public void returnTotalOfZeroForAllInterestWhenNoDealsYetExist() throws Exception {

        Map<String, Long> total = totalController.total();

        assertNotNull(total);
        assertTrue(0 == total.get("total"));
    }

    @Test
    public void returnCalculatedTotalInCentsForAllInterestWhenSomeDealsExist() throws Exception {

        Client client = clientRepository.getOne((long)1);
        Deal deal = new Deal();
        deal.setClient(client);
        deal.setReturnAmount(5000);
        dealRepository.save(deal);

        Map<String, Long> total = totalController.total();

        assertNotNull(total);
        assertTrue(7500 == total.get("total"));

        client = clientRepository.getOne((long)2);
        deal = new Deal();
        deal.setClient(client);
        deal.setReturnAmount(5000);
        dealRepository.save(deal);

        total = totalController.total();

        assertNotNull(total);
        assertTrue(15000 == total.get("total"));
    }

    @Test
    public void returnStatusOkFromValidTotalGetCall() throws Exception {
        mockMvc.perform(post("/total")
                .contentType(contentType))
                .andExpect(status().isOk());
    }

    @Test
    public void returnTotalJsonFromValidTotalGetCall() throws Exception {

        MvcResult result = mockMvc.perform(post("/total")
                .contentType(contentType))
                .andReturn();

        assertEquals("{\"total\":0}", result.getResponse().getContentAsString());

        Client client = clientRepository.getOne((long)3);
        Deal deal = new Deal();
        deal.setClient(client);
        deal.setReturnAmount(5000);
        dealRepository.save(deal);

        result = mockMvc.perform(post("/total")
                .contentType(contentType))
                .andReturn();

        assertEquals("{\"total\":7500}", result.getResponse().getContentAsString());
    }
}