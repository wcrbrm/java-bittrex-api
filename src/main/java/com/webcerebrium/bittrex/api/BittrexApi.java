package com.webcerebrium.bittrex.api;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableSet;
import com.google.common.escape.Escaper;
import com.google.common.net.UrlEscapers;
import com.google.gson.JsonObject;
import com.webcerebrium.bittrex.datatype.BittrexMarketStats;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;


@Data
@Slf4j
public class BittrexApi {


    /* Actual API key and Secret Key that will be used */
    public String apiKey;
    public String secretKey;

    public BittrexConfig config = new BittrexConfig();

    /**
     * API Base URL
     */
    public String baseUrl = "https://bittrex.com/api/";

    /**
     * Guava Class Instance for escaping
     */
    private Escaper esc = UrlEscapers.urlFormParameterEscaper();


    /**
     * Constructor of API when you exactly know the keys
     * @param apiKey Public API Key
     * @param secretKey Secret API Key
     * @throws BittrexApiException in case of any error
     */
    public BittrexApi(String apiKey, String secretKey) throws BittrexApiException {

        this.apiKey = apiKey;
        this.secretKey = secretKey;
        validateCredentials();
    }

    /**
     * Constructor of API - keys are loaded from VM options, environment variables, resource files
     * @throws BittrexApiException in case of any error
     */
    public BittrexApi() {
        this.apiKey = config.getVariable("BITTREX_API_KEY");
        this.secretKey = config.getVariable("BITTREX_SECRET_KEY");
    }

    /**
     * Validation we have API keys set up
     * @throws BittrexApiException in case of any error
     */
    protected void validateCredentials() throws BittrexApiException {
        String humanMessage = "Please check environment variables or VM options";
        if (Strings.isNullOrEmpty(this.getApiKey()))
            throw new BittrexApiException("Missing BITTREX_API_KEY. " + humanMessage);
        if (Strings.isNullOrEmpty(this.getSecretKey()))
            throw new BittrexApiException("Missing BITTREX_SECRET_KEY. " + humanMessage);
    }

    // ======= ======= ======= ======= ======= =======
    // READING MARKET INFORMATION
    // ======= ======= ======= ======= ======= =======

    public BittrexMarketStats getMarkets() throws BittrexApiException {
        JsonObject jsonObject = new BittrexRequest(baseUrl + "v1.1/public/getmarkets").read().asJsonObject();
        return new BittrexMarketStats(jsonObject);
    }

    public Set<String> getCoinsOf(String coin) {
        try {
            BittrexMarketStats stats = getMarkets();
            log.info("TRADED SYMBOLS = {}", stats.getSymbolsOf(coin));
            return stats.getCoinsOf(coin.toUpperCase());
        } catch (Exception e) {
            log.error("Bittrex UNCAUGHT EXCEPTION {}", e);
        } catch (BittrexApiException e) {
            log.warn("Bittrex ERROR {}", e.getMessage());
        }
        return ImmutableSet.of();
    }
}
