package bg.sofia.uni.fmi.mjt.gifts.service;

import bg.sofia.uni.fmi.mjt.gifts.exception.WrongReceiverException;
import bg.sofia.uni.fmi.mjt.gifts.gift.Gift;
import bg.sofia.uni.fmi.mjt.gifts.person.Person;

public class DefaultDeliveryService implements DeliveryService {
    public static final String ARGS_CANNOT_BE_NULL_MSG = "Arguments cannot be null.";
    public static final String WRONG_RECEIVER_MSG = "Wrong receiver.";

    @Override
    public void send(Person<?> receiver, Gift<?> gift) {
        if (receiver == null || gift == null) {
            throw new IllegalArgumentException(ARGS_CANNOT_BE_NULL_MSG);
        }

        if (!receiver.equals(gift.getReceiver())) {
            throw new WrongReceiverException(WRONG_RECEIVER_MSG);
        }

        receiver.receiveGift(gift);
    }
}
