package com.webcerebrium.bittrex.datatype;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.webcerebrium.bittrex.api.BittrexApiException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Data
@Slf4j
public class BittrexMarketStats {

    List<BittrexMarket> listMarkets = new LinkedList<>();

    public BittrexMarketStats() {
    }

    public BittrexMarketStats(JsonObject response) throws BittrexApiException {
        if (!response.has("result")) {
            throw new BittrexApiException("Missing data in response object while trying to read trading stats");
        }
        JsonArray result = response.get("result").getAsJsonArray();
        listMarkets.clear();
        for (JsonElement element: result) {
            BittrexMarket symbol = new BittrexMarket(element.getAsJsonObject());
            listMarkets.add(symbol);
        }
    }

    public List<BittrexMarket> getMarketsOf(String coin) {
        List<BittrexMarket> result = new LinkedList<>();
        for (int i = 0; i < listMarkets.size(); i++ ) {
            BittrexMarket marketInfo = listMarkets.get(i);
            if (!marketInfo.isActive()) continue;
            if (marketInfo.getSymbol().contains(coin)) {
                result.add(marketInfo);
            }
        }
        return result;
    }

    public Set<BittrexSymbol> getSymbolsOf(String coin) throws BittrexApiException {
        List<BittrexMarket> coins = getMarketsOf(coin);
        Set<BittrexSymbol> result = new HashSet<>();
        for (BittrexMarket sym: coins) {
            result.add(sym.getSymbol());
        }
        return result;
    }

    public Set<String> getCoinsOf(String coin) throws BittrexApiException {
        List<BittrexMarket> coins = getMarketsOf(coin);
        Set<String> result = new TreeSet<>();
        for (BittrexMarket sym: coins) {
            result.add(sym.getSymbol().getOpposite(coin));
        }
        return result;
    }
}
