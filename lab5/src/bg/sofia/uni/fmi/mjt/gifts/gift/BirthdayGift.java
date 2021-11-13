package bg.sofia.uni.fmi.mjt.gifts.gift;

import bg.sofia.uni.fmi.mjt.gifts.person.Person;

import java.util.Collection;

public class BirthdayGift<T extends Priceable> implements Gift<T> {
    public static final String ARGS_CANNOT_BE_NULL_MSG = "Arguments cannot be null.";

    private Person<?> sender;
    private Person<?> receiver;
    Collection<T> items;

    public BirthdayGift(Person<?> sender, Person<?> receiver, Collection<T> items) {
        this.sender = sender;
        this.receiver = receiver;
        this.items = items;
    }

    @Override
    public Person<?> getSender() {
        return sender;
    }

    @Override
    public Person<?> getReceiver() {
        return receiver;
    }

    @Override
    public double getPrice() {
        double totalPrice = 0.0;

        for (T item : items) {
            totalPrice += item.getPrice();
        }

        return totalPrice;
    }

    @Override
    public void addItem(T t) {
        if (t == null) {
            throw new IllegalArgumentException(ARGS_CANNOT_BE_NULL_MSG);
        }

        items.add(t);
    }

    @Override
    public boolean removeItem(T t) {
        if (!items.contains(t)) {
            return false;
        }

        items.remove(t);
        return true;
    }

    @Override
    public Collection<T> getItems() {
        return items;
    }

    @Override
    public T getMostExpensiveItem() {
        if (items.isEmpty()) {
            return null;
        }

        T mostExpensiveItem = null;
        double maxPrice = 0.0;
        for (T item : items) {
            if (item.getPrice() > maxPrice) {
                maxPrice = item.getPrice();
                mostExpensiveItem = item;
            }
        }

        return mostExpensiveItem;
    }
}
