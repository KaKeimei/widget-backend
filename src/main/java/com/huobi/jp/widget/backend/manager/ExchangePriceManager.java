package com.huobi.jp.widget.backend.manager;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.huobi.jp.widget.backend.manager.model.ZaifPriceModel;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by heqiming on 2019/6/12
 * @author heqiming
 */
@Service
public class ExchangePriceManager {

    private static Logger logger = LoggerFactory.getLogger(ExchangePriceManager.class);

    private OkHttpClient okHttpClient;

    private WebSocketClient webSocketClient;

    private List<ZaifPriceModel> zaifPriceListCache = new ArrayList<>();

    @PostConstruct
    public void init() throws URISyntaxException {
        okHttpClient = new OkHttpClient();
        // setup websocket for zaif
        webSocketClient = new WebSocketClient(new URI("wss://ws.zaif.jp:8888/ws?currency_pair=instant_exchange"),new Draft_6455()) {
            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                logger.info("Connection opened...");
            }

            @Override
            public void onMessage(String s) {
                try {
                    processZaifPrices(s);
                } catch (Exception e) {
                    logger.error("exception while parsing zaif message...", e);
                }
            }

            @Override
            public void onClose(int i, String s, boolean b) {
                logger.info("Connection closed...");

            }

            @Override
            public void onError(Exception e) {
                logger.error("Connection error...", e);
            }
        };
        webSocketClient.connect();
    }

    @PreDestroy
    public void destroy() {
        webSocketClient.close();
    }

    /**
     * @description: convert websocket message into List
     * @date 2019/6/14
     **/
    private void processZaifPrices(String message) throws Exception {
        if (StringUtils.isEmpty(message)) {
            return;
        }
        List<ZaifPriceModel> zaifPriceList = new ArrayList<>();
        zaifPriceList.add(calculateZaifPrice(message, "btc"));
        zaifPriceList.add(calculateZaifPrice(message, "xem"));
        zaifPriceList.add(calculateZaifPrice(message, "mona"));
        zaifPriceList.add(calculateZaifPrice(message, "eth"));
        this.zaifPriceListCache = zaifPriceList;
    }

    private ZaifPriceModel calculateZaifPrice(String json, String ticker) throws Exception {
        if (StringUtils.isEmpty(json) || StringUtils.isEmpty(ticker)) {
            return null;
        }
        ZaifPriceModel zaifPriceModel = new ZaifPriceModel();
        String lowerCaseTicker = ticker.toLowerCase();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode node = objectMapper.readTree(json);

        String buyPricePath = String.format("%s.buy", lowerCaseTicker);
        String sellPricePath = String.format("%s.sell", lowerCaseTicker);
        BigDecimal buy = new BigDecimal(node.path(buyPricePath).asText());
        BigDecimal sell = new BigDecimal(node.path(sellPricePath).asText());
        BigDecimal mediumPrice = buy.add(sell).divide(new BigDecimal(2), BigDecimal.ROUND_HALF_DOWN);
        float feeRate = 100 * (sell.floatValue() - mediumPrice.floatValue()) / mediumPrice.floatValue();
        String feeRateString = new DecimalFormat(".00").format(feeRate) + "%";
        zaifPriceModel.setAskPrice(buy);
        zaifPriceModel.setBidPrice(sell);
        zaifPriceModel.setMediumPrice(mediumPrice);
        zaifPriceModel.setFeeRate(feeRateString);
        zaifPriceModel.setTicker(ticker.toUpperCase());
        return zaifPriceModel;
    }

    public String getBitflyerPrices(String ticker) {
        String url = String.format("https://bitflyer.com/api/app/market/price?product_code=%s_JPY", ticker);
        Request request = new Request.Builder().url(url).build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                return response.body().string();
            }
        } catch (Exception e) {
            // donothing
        }
        return "error";
    }

    public String getGMOPrices() {
        String url = "https://coin.z.com/api/v1/master/getCurrentRate.json";
        Request request = new Request.Builder().url(url).build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                return response.body().string();
            }
        } catch (Exception e) {
            // donothing
        }
        return "error";
    }

    public String getCoincheckPrices() {
        String url = "https://coincheck.com/api/rate/all";
        Request request = new Request.Builder().url(url).build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                assert response.body() != null;
                return response.body().string();
            }
        } catch (Exception e) {
            // donothing
        }
        return "error";
    }

    public String getZaifPrices() {
        try {
            return new ObjectMapper().writeValueAsString(this.zaifPriceListCache);
        } catch (JsonProcessingException e) {
            return "error";
        }
    }
}
