package bg.sofia.uni.fmi.mjt.twitch.user.service;

import bg.sofia.uni.fmi.mjt.twitch.user.User;

import java.util.Map;

public class UserServiceImpl implements UserService {
    private final Map<String, User> users;

    public UserServiceImpl(Map<String, User> users) {
        this.users = users;
    }

    @Override
    public Map<String, User> getUsers() {
        return users;
    }
}
