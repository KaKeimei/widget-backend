package com.huobi.jp.widget.backend.controller;

import com.huobi.jp.widget.backend.manager.BitflyerPriceManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Created by heqiming on 2019/6/12
 */
@RestController
public class BitflyerPriceController {
    @Resource
    private BitflyerPriceManager bitflyerPriceManager;

    @GetMapping("bitflyer/price")
    public String getBitflyerPrice(@RequestParam String ticker) {
        return bitflyerPriceManager.getBitflyerPrices(ticker);
    }
}
