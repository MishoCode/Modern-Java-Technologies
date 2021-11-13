package bg.sofia.uni.fmi.mjt.gifts.gift;

public abstract class BasePriceableItem implements Priceable {
    private final double price;

    public BasePriceableItem(double price) {
        this.price = price;
    }

    @Override
    public double getPrice() {
        return price;
    }
}
