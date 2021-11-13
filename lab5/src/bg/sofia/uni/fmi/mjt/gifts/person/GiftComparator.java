package bg.sofia.uni.fmi.mjt.gifts.person;

import bg.sofia.uni.fmi.mjt.gifts.gift.Gift;

import java.util.Comparator;

public class GiftComparator implements Comparator<Gift<?>> {

    @Override
    public int compare(Gift<?> gift1, Gift<?> gift2) {
        return Double.compare(gift1.getPrice(), gift2.getPrice());
    }
}
