package bg.sofia.uni.fmi.mjt.investment.wallet;

import bg.sofia.uni.fmi.mjt.investment.wallet.acquisition.Acquisition;
import bg.sofia.uni.fmi.mjt.investment.wallet.acquisition.AssetAcquisition;
import bg.sofia.uni.fmi.mjt.investment.wallet.asset.Asset;
import bg.sofia.uni.fmi.mjt.investment.wallet.exception.InsufficientResourcesException;
import bg.sofia.uni.fmi.mjt.investment.wallet.exception.OfferPriceException;
import bg.sofia.uni.fmi.mjt.investment.wallet.exception.UnknownAssetException;
import bg.sofia.uni.fmi.mjt.investment.wallet.exception.WalletException;
import bg.sofia.uni.fmi.mjt.investment.wallet.quote.QuoteService;

import java.time.LocalDateTime;
import java.util.*;

public class InvestmentWallet implements Wallet {
    public static final String CASH_CANNOT_BE_NEGATIVE_MSG = "The cash cannot be negative.";
    public static final String INSUFFICIENT_RESOURCES_MSG = "Not enough resources.";
    public static final String INVALID_ARGS_MSG = "Invalid argument values.";
    public static final String UNKNOWN_ASSET_MSG =
        "There is no quote for this asset or this asset is not present in the wallet.";
    public static final String ASK_PRICE_IS_TOO_HIGH_MSG = "The ask price is higher than the max price.";
    public static final String BID_PRICE_IS_TOO_SMALL_MSG = "The bid price is smaller than the min price.";

    private double money;
    private final QuoteService quoteService;
    private final Map<Asset, Integer> assets;
    private final List<Acquisition> acquisitions;

    public InvestmentWallet(QuoteService quoteService) {
        this.quoteService = quoteService;
        assets = new HashMap<>();
        acquisitions = new ArrayList<>();
        money = 0.0;
    }

    private void addNewAsset(Asset asset, int quantity) {
        if (!assets.containsKey(asset)) {
            assets.put(asset, 0);
        }

        assets.put(asset, assets.get(asset) + quantity);
    }

    private void removeAsset(Asset asset, int quantity) {
        assets.put(asset, assets.get(asset) - quantity);

        if (assets.get(asset) == 0) {
            assets.remove(asset);
        }
    }

    @Override
    public double deposit(double cash) {
        if (cash < 0.0) {
            throw new IllegalArgumentException(CASH_CANNOT_BE_NEGATIVE_MSG);
        }

        money += cash;
        return money;
    }

    @Override
    public double withdraw(double cash) throws InsufficientResourcesException {
        if (cash < 0.0) {
            throw new IllegalArgumentException(CASH_CANNOT_BE_NEGATIVE_MSG);
        }

        if (cash > money) {
            throw new InsufficientResourcesException(INSUFFICIENT_RESOURCES_MSG);
        }

        money -= cash;
        return money;
    }

    @Override
    public Acquisition buy(Asset asset, int quantity, double maxPrice) throws WalletException {
        if (asset == null || quantity < 0 || maxPrice < 0.0) {
            throw new IllegalArgumentException(INVALID_ARGS_MSG);
        }

        if (quoteService.getQuote(asset) == null) {
            throw new UnknownAssetException(UNKNOWN_ASSET_MSG);
        }

        double askPrice = quoteService.getQuote(asset).askPrice();
        if (askPrice > maxPrice) {
            throw new OfferPriceException(ASK_PRICE_IS_TOO_HIGH_MSG);
        }

        double moneyForTransaction = askPrice * quantity;
        if (moneyForTransaction > money) {
            throw new InsufficientResourcesException(INSUFFICIENT_RESOURCES_MSG);
        }

        withdraw(moneyForTransaction);
        Acquisition newAcquisition = new AssetAcquisition(asset, askPrice, quantity, LocalDateTime.now());
        acquisitions.add(newAcquisition);
        addNewAsset(asset, quantity);
        return newAcquisition;
    }

    @Override
    public double sell(Asset asset, int quantity, double minPrice) throws WalletException {
        if (asset == null || quantity < 0 || minPrice < 0.0) {
            throw new IllegalArgumentException(INVALID_ARGS_MSG);
        }

        if (quoteService.getQuote(asset) == null) {
            throw new UnknownAssetException(UNKNOWN_ASSET_MSG);
        }

        double bidPrice = quoteService.getQuote(asset).bidPrice();
        if (bidPrice < minPrice) {
            throw new OfferPriceException(BID_PRICE_IS_TOO_SMALL_MSG);
        }

        int quantityInTheWallet = 0;
        if (assets.containsKey(asset)) {
            quantityInTheWallet = assets.get(asset);
            removeAsset(asset, quantity);
        }

        if (quantityInTheWallet < quantity) {
            throw new InsufficientResourcesException(INSUFFICIENT_RESOURCES_MSG);
        }

        double earnedMoney = quantity * bidPrice;
        money += earnedMoney;
        return earnedMoney;
    }

    @Override
    public double getValuation() {
        double totalValuation = 0.0;

        for (Asset asset : assets.keySet()) {
            try {
                totalValuation += getValuation(asset);
            } catch (UnknownAssetException e) {
                e.printStackTrace();
            }
        }

        return totalValuation;
    }

    @Override
    public double getValuation(Asset asset) throws UnknownAssetException {
        if (asset == null) {
            throw new IllegalArgumentException(INVALID_ARGS_MSG);
        }

        if (!assets.containsKey(asset) || quoteService.getQuote(asset) == null) {
            throw new UnknownAssetException(UNKNOWN_ASSET_MSG);
        }

        int currentQuantity = assets.get(asset);
        return quoteService.getQuote(asset).bidPrice() * currentQuantity;
    }

    @Override
    public Asset getMostValuableAsset() {
        double mostValuableAssetPrice = 0.0;
        Asset mostValuableAsset = null;

        for (Asset asset : assets.keySet()) {
            try {
                double currentAssetValuation = getValuation(asset);
                if (currentAssetValuation > mostValuableAssetPrice) {
                    mostValuableAsset = asset;
                    mostValuableAssetPrice = currentAssetValuation;
                }
            } catch (UnknownAssetException e) {
                e.printStackTrace();
            }
        }

        return mostValuableAsset;
    }

    @Override
    public Collection<Acquisition> getAllAcquisitions() {
        return Set.copyOf(acquisitions);
    }

    @Override
    public Set<Acquisition> getLastNAcquisitions(int n) {
        if (n < 0) {
            throw new IllegalArgumentException(INVALID_ARGS_MSG);
        }

        int acquisitionsCount = acquisitions.size();
        if (acquisitionsCount <= n) {
            return Set.copyOf(acquisitions);
        }

        return new HashSet<>(acquisitions.subList(acquisitionsCount - n, acquisitionsCount));
    }
}
