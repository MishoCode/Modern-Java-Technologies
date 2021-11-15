package bg.sofia.uni.fmi.mjt.investment.wallet.acquisition;

import bg.sofia.uni.fmi.mjt.investment.wallet.asset.Asset;

import java.time.LocalDateTime;

public class AssetAcquisition implements Acquisition {
    private final Asset asset;
    private final double price;
    private final int quantity;
    LocalDateTime timestamp;

    public AssetAcquisition(Asset asset, double price, int quantity, LocalDateTime timestamp) {
        this.asset = asset;
        this.price = price;
        this.quantity = quantity;
        this.timestamp = timestamp;
    }

    @Override
    public double getPrice() {
        return price;
    }

    @Override
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public int getQuantity() {
        return quantity;
    }

    @Override
    public Asset getAsset() {
        return asset;
    }
}
