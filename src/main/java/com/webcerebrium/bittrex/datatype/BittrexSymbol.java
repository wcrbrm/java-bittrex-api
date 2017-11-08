package com.webcerebrium.bittrex.datatype;

import com.google.common.base.Strings;
import com.webcerebrium.bittrex.api.BittrexApiException;

public class BittrexSymbol {

    String symbol = "";

    public BittrexSymbol(String symbol)  throws BittrexApiException {
        // sanitizing symbol, preventing from common user-input errors
        if (Strings.isNullOrEmpty(symbol)) {
            throw new BittrexApiException("Symbol cannot be empty. Example: BTC-BAT");
        }
        if (symbol.contains(" ")) {
            throw new BittrexApiException("Symbol cannot contain spaces. Example: BTC-BAT");
        }
        if (!symbol.startsWith("BTC-") &&
                !symbol.startsWith("ETH-") &&
                !symbol.startsWith("USDT-")) {
            throw new BittrexApiException("Market Symbol should be starting with BTC-, ETH- or USDT-. Example: BTC-BAT. Provided: " + symbol );
        }
        this.symbol = symbol.toUpperCase();
    }

    public String get(){ return this.symbol; }

    public String getSymbol(){ return this.symbol; }

    public String getCoin(){
        if (symbol.equals("BTC-ETH")) return "ETH";
        return this.symbol.replace("BTC-", "").replace("ETH-", "").replace("USDT-", "");
    }
    public String getBaseCoin(){
        // in case of NEO-to-ETH and NEO-to-BTC we are selling NEO
        if ((symbol.startsWith("BTC-") || symbol.startsWith("ETH-"))) return symbol.substring(4);
        if (symbol.equals("ETH-BTC")) return "BTC";

        if (symbol.endsWith("BTC-")) return "BTC";
        if (symbol.endsWith("ETH-")) return "ETH";
        if (symbol.endsWith("USDT-")) return "USDT";
        return "";
    }

    public String getOpposite(String coin) {
        if (symbol.startsWith(coin + "-")) {
            return symbol.substring((coin + "-").length());
        }
        if (symbol.endsWith("-" + coin)) {
            int index = symbol.length() - ("-" + coin).length();
            return symbol.substring(0, index);
        }
        return "";
    }

    public String toString() { return this.get(); }

    public static BittrexSymbol valueOf(String s) throws BittrexApiException {
        return new BittrexSymbol(s);
    }

    public static BittrexSymbol BTC(String pair) throws BittrexApiException {
        return BittrexSymbol.valueOf("BTC-" + pair.toUpperCase());
    }

    public static BittrexSymbol ETH(String pair) throws BittrexApiException {
        return BittrexSymbol.valueOf("ETH-" + pair.toUpperCase());
    }

    public static BittrexSymbol BTC_ETH() throws BittrexApiException {
        return BittrexSymbol.valueOf("BTC-ETC");
    }

    public static BittrexSymbol USDT(String pair) throws BittrexApiException {
        return BittrexSymbol.valueOf("USDT-" + pair.toUpperCase());
    }


    public boolean contains(String coin) {
        return (symbol.endsWith("-" + coin.toUpperCase())) || (symbol.startsWith(coin.toUpperCase() + "-"));
    }
}
