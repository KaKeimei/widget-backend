package com.huobi.jp.widget.backend.controller;

import com.huobi.jp.widget.backend.manager.ExchangePriceManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Created by heqiming on 2019/6/12
 */
@RestController
@RequestMapping(value = "price")
public class ExchangePriceController {
    @Resource
    private ExchangePriceManager exchangePriceManager;

    @GetMapping("bitflyer")
    public String getBitflyerPrice(@RequestParam String ticker) {
        return exchangePriceManager.getBitflyerPrices(ticker);
    }

    @GetMapping("coincheck")
    public String getCoincheckPrice() {
        return exchangePriceManager.getCoincheckPrices();
    }

    @GetMapping("gmo")
    public String getGMOPrice() {
        return exchangePriceManager.getGMOPrices();
    }

    @GetMapping("zaif")
    public String getZaifPrice() {
        return exchangePriceManager.getZaifPrices();
    }

    @GetMapping("hello")
    public String helloWorld() {
        return "hello world, elastic beanstalk...";
    }
}
