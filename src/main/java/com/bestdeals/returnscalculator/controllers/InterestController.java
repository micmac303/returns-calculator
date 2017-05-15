package com.bestdeals.returnscalculator.controllers;

import com.bestdeals.returnscalculator.exceptions.BadRequestException;
import com.bestdeals.returnscalculator.model.Client;
import com.bestdeals.returnscalculator.model.Deal;
import com.bestdeals.returnscalculator.model.Proposal;
import com.bestdeals.returnscalculator.persistence.ClientRepository;
import com.bestdeals.returnscalculator.persistence.DealRepository;
import com.bestdeals.returnscalculator.services.CompoundInterestCalculator;
import com.bestdeals.returnscalculator.services.SimpleInterestCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class InterestController {

    @Autowired
    private SimpleInterestCalculator simpleInterestCalculator;
    @Autowired
    private CompoundInterestCalculator compoundInterestCalculator;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private DealRepository dealRepository;

    @RequestMapping(value = "/simple", method = RequestMethod.POST)
    public @ResponseBody Deal simple(@RequestBody Proposal proposal) throws BadRequestException {

        this.validateProposal(proposal);
        long interest = simpleInterestCalculator.calculateInterest(proposal.getPrincipal(), proposal.getInterestRate(), proposal.getTimesApplied(), proposal.getYears());
        return persistedDeal(proposal.getClientId(), interest);
    }

    @RequestMapping(value = "/compound", method = RequestMethod.POST)
    public @ResponseBody Deal compound(@RequestBody Proposal proposal) throws BadRequestException {

        this.validateProposal(proposal);
        long interest = compoundInterestCalculator.calculateInterest(proposal.getPrincipal(), proposal.getInterestRate(), proposal.getTimesApplied(), proposal.getYears());
        return persistedDeal(proposal.getClientId(), interest);
    }

    private void validateProposal(Proposal proposal) throws BadRequestException {

        if (proposal.getPrincipal() <= 0 || proposal.getInterestRate() <= 0 || proposal.getTimesApplied() <= 0 || proposal.getYears() <= 0) {
            throw new BadRequestException("Input values cannot be less than or equal to zero");
        }
    }

    private Deal persistedDeal(long clientId, long interest) throws BadRequestException {

        Client client = clientRepository.findOne(clientId);
        if (client == null) {
            throw new BadRequestException("Not a known clientId: '" + clientId + "'");
        }

        Deal deal = new Deal();
        deal.setClient(client);
        deal.setReturnAmount(interest);

        dealRepository.save(deal);
        return deal;
    }
}
