package com.webcerebrium.bittrex.datatype;

import com.google.gson.JsonObject;
import com.webcerebrium.bittrex.api.BittrexApiException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.Date;

@Slf4j
@Data
public class BittrexMarket {

    String marketCurrency;
    String baseCurrency;
    String marketCurrencyLong;
    String baseCurrencyLong;
    BigDecimal minTradeSize;
    BittrexSymbol symbol;
    boolean isActive;
    Date created = null;

    public BittrexMarket() {
    }

    public BittrexMarket(JsonObject obj) throws BittrexApiException {

        marketCurrency = obj.get("MarketCurrency").getAsString();
        baseCurrency = obj.get("BaseCurrency").getAsString();
        marketCurrencyLong = obj.get("MarketCurrencyLong").getAsString();
        baseCurrencyLong = obj.get("BaseCurrencyLong").getAsString();
        minTradeSize = obj.get("MinTradeSize").getAsBigDecimal();

        symbol = BittrexSymbol.valueOf(obj.get("MarketName").getAsString());
        isActive = obj.get("IsActive").getAsBoolean();
    }


}
