package com.huobi.jp.widget.backend.manager.model;

import java.math.BigDecimal;

/**
 * Created by heqiming on 2019/6/14
 */
public class ZaifPriceModel {
    private BigDecimal bidPrice;

    private BigDecimal askPrice;

    private BigDecimal mediumPrice;

    private String ticker;

    private String feeRate;

    public BigDecimal getBidPrice() {
        return bidPrice;
    }

    public void setBidPrice(BigDecimal bidPrice) {
        this.bidPrice = bidPrice;
    }

    public BigDecimal getAskPrice() {
        return askPrice;
    }

    public void setAskPrice(BigDecimal askPrice) {
        this.askPrice = askPrice;
    }

    public BigDecimal getMediumPrice() {
        return mediumPrice;
    }

    public void setMediumPrice(BigDecimal mediumPrice) {
        this.mediumPrice = mediumPrice;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public String getFeeRate() {
        return feeRate;
    }

    public void setFeeRate(String feeRate) {
        this.feeRate = feeRate;
    }
}
