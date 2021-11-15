package bg.sofia.uni.fmi.mjt.investment.wallet.quote;

import bg.sofia.uni.fmi.mjt.investment.wallet.asset.Asset;
import bg.sofia.uni.fmi.mjt.investment.wallet.asset.AssetType;

import java.util.EnumMap;
import java.util.Map;

public class QuoteServiceImpl implements QuoteService {
    public static final String ARGS_CANNOT_BE_NULL_MSG = "Arguments cannot be null.";
    public static final double ASK_PRICE_1 = 20.5;
    public static final double ASK_PRICE_2 = 10.5;
    public static final double ASK_PRICE_3 = 11.0;
    public static final double ASK_PRICE_4 = 21.5;
    public static final double BID_PRICE_1 = 28.5;
    public static final double BID_PRICE_2 = 8.2;
    public static final double BID_PRICE_3 = 2.4;
    public static final double BID_PRICE_4 = 22.6;


    private final Map<AssetType, Quote> quoteMap = new EnumMap<>(AssetType.class);

    private void initializeQuotes() {
        quoteMap.put(AssetType.CRYPTO, new Quote(ASK_PRICE_1, BID_PRICE_1));
        quoteMap.put(AssetType.FIAT, new Quote(ASK_PRICE_2, BID_PRICE_2));
        quoteMap.put(AssetType.GOLD, new Quote(ASK_PRICE_3, BID_PRICE_3));
        quoteMap.put(AssetType.STOCK, new Quote(ASK_PRICE_4, BID_PRICE_4));
    }

    public QuoteServiceImpl() {
        initializeQuotes();
    }

    @Override
    public Quote getQuote(Asset asset) {
        if (asset == null) {
            throw new IllegalArgumentException(ARGS_CANNOT_BE_NULL_MSG);
        }

        if (quoteMap.containsKey(asset.getType())) {
            return quoteMap.get(asset.getType());
        }

        return null;
    }
}
