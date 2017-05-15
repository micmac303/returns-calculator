package com.bestdeals.returnscalculator.controllers;

import com.bestdeals.returnscalculator.model.Client;
import com.bestdeals.returnscalculator.persistence.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class PopulateDataController {

    @Autowired
    private ClientRepository clientRepository;

    @RequestMapping(value = "/populate", method = RequestMethod.GET)
    public @ResponseBody List<Long> populate() {

        Arrays.asList(1, 2, 3).stream().map(integer -> new Client()).forEach(clientRepository::save);
        return clientRepository.findAll().stream().map(Client::getId).collect(Collectors.toList());
    }
}
