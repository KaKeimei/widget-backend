package com.huobi.jp.widget.backend.manager;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * Created by heqiming on 2019/6/12
 */
@Service
public class ExchangePriceManager {
    private OkHttpClient okHttpClient;

    @PostConstruct
    public void init() {
        okHttpClient = new OkHttpClient();
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
                return response.body().string();
            }
        } catch (Exception e) {
            // donothing
        }
        return "error";
    }
}
