package bg.sofia.uni.fmi.mjt.gifts.person;

import bg.sofia.uni.fmi.mjt.gifts.exception.WrongReceiverException;
import bg.sofia.uni.fmi.mjt.gifts.gift.Gift;

import java.util.*;

public class DefaultPerson<I> implements Person<I> {
    public static final String ARGS_CANNOT_BE_BULL_MSG = "Arguments cannot be null.";
    public static final String WRONG_RECEIVER_MSG = "Wrong receiver.";

    private I id;
    List<Gift<?>> gifts = new ArrayList<>();

    public DefaultPerson(I id) {
        this.id = id;
    }

    @Override
    public Collection<Gift<?>> getNMostExpensiveReceivedGifts(int n) {
        if (n < 0) {
            throw new IllegalArgumentException(ARGS_CANNOT_BE_BULL_MSG);
        }

        if (n == 0) {
            return Collections.unmodifiableList(gifts);
        }

        List<Gift<?>> mostExpensive = new ArrayList<>();
        Collections.sort(gifts, new GiftComparator());

        return Collections.unmodifiableList(gifts.subList(n - 1, gifts.size()));
    }

    @Override
    public Collection<Gift<?>> getGiftsBy(Person<?> person) {
        if (person == null) {
            throw new IllegalArgumentException(ARGS_CANNOT_BE_BULL_MSG);
        }

        List<Gift<?>> giftsFromSender = new ArrayList<>();
        for (Gift<?> gift : gifts) {
            if (gift.getSender().equals(person)) {
                giftsFromSender.add(gift);
            }
        }

        return Collections.unmodifiableList(giftsFromSender);
    }

    @Override
    public I getId() {
        return id;
    }

    @Override
    public void receiveGift(Gift<?> gift) {
        if (gift == null) {
            throw new IllegalArgumentException(ARGS_CANNOT_BE_BULL_MSG);
        }

        if (!gift.getReceiver().equals(this)) {
            throw new WrongReceiverException(WRONG_RECEIVER_MSG);
        }

        gifts.add(gift);
    }
}
