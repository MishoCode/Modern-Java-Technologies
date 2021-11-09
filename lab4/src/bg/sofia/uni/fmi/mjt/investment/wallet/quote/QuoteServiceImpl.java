package bg.sofia.uni.fmi.mjt.investment.wallet.quote;

import bg.sofia.uni.fmi.mjt.investment.wallet.asset.Asset;
import bg.sofia.uni.fmi.mjt.investment.wallet.asset.AssetType;

import java.util.EnumMap;
import java.util.Map;

public class QuoteServiceImpl implements QuoteService {
    public static final String ARGS_CANNOT_BE_NULL_MSG = "Arguments cannot be null.";

    private Map<AssetType, Quote> quoteMap = new EnumMap<>(AssetType.class);

    private void initializeQuotes() {
        quoteMap.put(AssetType.CRYPTO, new Quote(20.5, 25.5));
        quoteMap.put(AssetType.FIAT, new Quote(10.5, 8.2));
        quoteMap.put(AssetType.GOLD, new Quote(11.0, 2.4));
        quoteMap.put(AssetType.STOCK, new Quote(21.5, 22.6));
    }

    public QuoteServiceImpl() {
        initializeQuotes();
    }

    @Override
    public Quote getQuote(Asset asset) {
        if (asset.equals(null)) {
            throw new IllegalArgumentException(ARGS_CANNOT_BE_NULL_MSG);
        }

        if (quoteMap.containsKey(asset.getType())) {
            return quoteMap.get(asset.getType());
        }

        return null;
    }
}
