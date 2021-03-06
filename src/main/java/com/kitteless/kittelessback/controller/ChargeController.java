package com.kitteless.kittelessback.controller;

import com.kitteless.kittelessback.model.ChargeList;
import com.kitteless.kittelessback.service.ChargeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChargeController {
    @Autowired
    ChargeService chargeService;

    @PostMapping("/charge")
    @ResponseStatus(HttpStatus.OK)
    public ChargeList getChargeList() {
        ChargeList chargeList = new ChargeList();
        chargeList.setChargeResponseList(chargeService.getChargeList());
        return chargeList;
    }
}
