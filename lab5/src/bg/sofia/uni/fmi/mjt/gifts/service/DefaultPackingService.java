package bg.sofia.uni.fmi.mjt.gifts.service;

import bg.sofia.uni.fmi.mjt.gifts.gift.BirthdayGift;
import bg.sofia.uni.fmi.mjt.gifts.gift.Gift;
import bg.sofia.uni.fmi.mjt.gifts.gift.Priceable;
import bg.sofia.uni.fmi.mjt.gifts.person.Person;

import java.util.ArrayList;
import java.util.Collection;

public class DefaultPackingService<T extends Priceable> implements PackingService<T> {
    public static final String ARGS_CANNOT_BE_NULL_MSG = "Arguments cannot be null.";

    @Override
    public Gift<T> pack(Person<?> sender, Person<?> receiver, T item) {
        if (sender == null || receiver == null || item == null) {
            throw new IllegalArgumentException(ARGS_CANNOT_BE_NULL_MSG);
        }

        Gift<T> wrappedGift = new BirthdayGift<>(sender, receiver, new ArrayList<>());
        wrappedGift.addItem(item);
        return wrappedGift;
    }

    @Override
    public Gift<T> pack(Person<?> sender, Person<?> receiver, T... items) {
        if (sender == null || receiver == null || items == null) {
            throw new IllegalArgumentException(ARGS_CANNOT_BE_NULL_MSG);
        }

        Gift<T> wrappedGift = new BirthdayGift<>(sender, receiver, new ArrayList<>());
        for (T item : items) {
            wrappedGift.addItem(item);
        }

        return wrappedGift;
    }

    @Override
    public Collection<T> unpack(Gift<T> gift) {
        if (gift == null) {
            throw new IllegalArgumentException(ARGS_CANNOT_BE_NULL_MSG);
        }

        return gift.getItems();
    }
}
