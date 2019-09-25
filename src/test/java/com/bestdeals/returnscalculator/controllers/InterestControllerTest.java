package com.bestdeals.returnscalculator.controllers;

import com.bestdeals.returnscalculator.Application;
import com.bestdeals.returnscalculator.exceptions.BadRequestException;
import com.bestdeals.returnscalculator.model.Client;
import com.bestdeals.returnscalculator.model.Deal;
import com.bestdeals.returnscalculator.model.Proposal;
import com.bestdeals.returnscalculator.persistence.ClientRepository;
import com.bestdeals.returnscalculator.persistence.DealRepository;
import com.bestdeals.returnscalculator.services.CompoundInterestCalculator;
import com.bestdeals.returnscalculator.services.SimpleInterestCalculator;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class InterestControllerTest {

    //System under test
    private InterestController interestController;

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
    private SimpleInterestCalculator simpleInterestCalculator;

    @Autowired
    private CompoundInterestCalculator compoundInterestCalculator;

    @Before
    public void setup() {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
        interestController = new InterestController();

        ReflectionTestUtils.setField(interestController, "simpleInterestCalculator", simpleInterestCalculator);
        ReflectionTestUtils.setField(interestController, "compoundInterestCalculator", compoundInterestCalculator);
        ReflectionTestUtils.setField(interestController, "clientRepository", clientRepository);
        ReflectionTestUtils.setField(interestController, "dealRepository", dealRepository);

        Arrays.asList(1, 2, 3).stream().map(integer -> new Client()).forEach(clientRepository::save);
    }

    @Test
    public void persistAndReturnSimpleInterestDeal() throws Exception, BadRequestException {

        Proposal proposal = new Proposal();
        proposal.setClientId(1);
        proposal.setPrincipal(100000);
        proposal.setInterestRate("0.01");
        proposal.setTimesApplied(1);
        proposal.setYears(5);
        Deal deal = interestController.simple(proposal);

        assertNotNull(deal);
        assertNotNull(dealRepository.findOne(deal.getId()));
    }

    @Test
    public void returnStatusOkFromValidSimpleInterestPostCall() throws Exception {
        mockMvc.perform(post("/simple")
                .content("{\n" +
                        "  \t\"clientId\": 1,\n" +
                        "  \t\"principal\": 100000,\n" +
                        "    \"interestRate\": \"0.01\",\n" +
                        "    \"timesApplied\": 1,\n" +
                        "    \"years\": 5\n" +
                        "}")
                .contentType(contentType))
                .andExpect(status().isOk());
    }

    @Test
    public void returnDealJsonFromValidSimpleInterestPostCall() throws Exception {

        MvcResult result = mockMvc.perform(post("/simple")
                .content("{\n" +
                        "  \t\"clientId\": 1,\n" +
                        "  \t\"principal\": 100000,\n" +
                        "    \"interestRate\": \"0.01\",\n" +
                        "    \"timesApplied\": 1,\n" +
                        "    \"years\": 5\n" +
                        "}")
                .contentType(contentType))
                .andReturn();

        assertEquals("{\"id\":6,\"returnAmount\":5000,\"client\":{\"id\":1}}", result.getResponse().getContentAsString());
    }

    @Test
    public void notKnownClientIdShouldThrowAppropriateBadRequestException()  {
        Proposal proposal = new Proposal();
        proposal.setClientId(9999);
        proposal.setPrincipal(100000);
        proposal.setInterestRate("0.01");
        proposal.setTimesApplied(1);
        proposal.setYears(5);
        try {
            interestController.simple(proposal);
        } catch (BadRequestException bre) {
            assertEquals("Not a known clientId: '9999'", bre.getMessage());
        }
    }

    @Test
    public void negativeInputValuesShouldThrowAppropriateBadRequestException() {
        Proposal proposal = new Proposal();
        proposal.setClientId(2);
        proposal.setPrincipal(-100000);
        proposal.setInterestRate("0.01");
        proposal.setTimesApplied(1);
        proposal.setYears(5);
        try {
            interestController.simple(proposal);
        } catch (BadRequestException bre) {
            assertEquals("Input values cannot be less than or equal to zero", bre.getMessage());
        }

        proposal.setPrincipal(100000);
        proposal.setInterestRate("-0.01");
        try {
            interestController.simple(proposal);
        } catch (BadRequestException bre) {
            assertEquals("Input values cannot be less than or equal to zero", bre.getMessage());
        }

        proposal.setInterestRate("0.01");
        proposal.setTimesApplied(-1);
        try {
            interestController.simple(proposal);
        } catch (BadRequestException bre) {
            assertEquals("Input values cannot be less than or equal to zero", bre.getMessage());
        }

        proposal.setTimesApplied(1);
        proposal.setYears(-5);
        try {
            interestController.simple(proposal);
        } catch (BadRequestException bre) {
            assertEquals("Input values cannot be less than or equal to zero", bre.getMessage());
        }
    }

    @Test
    public void persistAndReturnCompoundInterestDeal() throws BadRequestException {
        Proposal proposal = new Proposal();
        proposal.setClientId(1);
        proposal.setPrincipal(100000);
        proposal.setInterestRate("0.01");
        proposal.setTimesApplied(1);
        proposal.setYears(5);
        Deal deal = interestController.compound(proposal);

        assertNotNull(deal);
        assertNotNull(dealRepository.findOne(deal.getId()));
    }

    @Test
    public void returnStatusOkFromValidCompoundInterestPostCall() throws Exception {
        mockMvc.perform(post("/compound")
                .content("{\n" +
                        "  \t\"clientId\": 1,\n" +
                        "  \t\"principal\": 100000,\n" +
                        "    \"interestRate\": \"0.01\",\n" +
                        "    \"timesApplied\": 1,\n" +
                        "    \"years\": 5\n" +
                        "}")
                .contentType(contentType))
                .andExpect(status().isOk());
    }

    @Test
    public void returnDealJsonFromValidCompoundInterestPostCall() throws Exception {

        MvcResult result = mockMvc.perform(post("/compound")
                .content("{\n" +
                        "  \t\"clientId\": 1,\n" +
                        "  \t\"principal\": 100000,\n" +
                        "    \"interestRate\": \"0.01\",\n" +
                        "    \"timesApplied\": 1,\n" +
                        "    \"years\": 5\n" +
                        "}")
                .contentType(contentType))
                .andReturn();

        assertEquals("{\"id\":4,\"returnAmount\":5101,\"client\":{\"id\":1}}", result.getResponse().getContentAsString());
    }

    @Test
    public void notKnownClientIdForCompoundCallShouldThrowAppropriateBadRequestException()  {
        Proposal proposal = new Proposal();
        proposal.setClientId(9999);
        proposal.setPrincipal(100000);
        proposal.setInterestRate("0.01");
        proposal.setTimesApplied(1);
        proposal.setYears(5);
        try {
            interestController.compound(proposal);
        } catch (BadRequestException bre) {
            assertEquals("Not a known clientId: '9999'", bre.getMessage());
        }
    }

    @Test
    public void negativeInputValuesForCompoundCallShouldThrowAppropriateBadRequestException() {
        Proposal proposal = new Proposal();
        proposal.setClientId(2);
        proposal.setPrincipal(-100000);
        proposal.setInterestRate("0.01");
        proposal.setTimesApplied(1);
        proposal.setYears(5);
        try {
            interestController.compound(proposal);
        } catch (BadRequestException bre) {
            assertEquals("Input values cannot be less than or equal to zero", bre.getMessage());
        }

        proposal.setPrincipal(100000);
        proposal.setInterestRate("-0.01");
        try {
            interestController.compound(proposal);
        } catch (BadRequestException bre) {
            assertEquals("Input values cannot be less than or equal to zero", bre.getMessage());
        }

        proposal.setInterestRate("0.01");
        proposal.setTimesApplied(-1);
        try {
            interestController.compound(proposal);
        } catch (BadRequestException bre) {
            assertEquals("Input values cannot be less than or equal to zero", bre.getMessage());
        }

        proposal.setTimesApplied(1);
        proposal.setYears(-5);
        try {
            interestController.compound(proposal);
        } catch (BadRequestException bre) {
            assertEquals("Input values cannot be less than or equal to zero", bre.getMessage());
        }
    }
}
