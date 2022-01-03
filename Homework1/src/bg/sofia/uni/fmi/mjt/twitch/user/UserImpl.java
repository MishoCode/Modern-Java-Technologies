package bg.sofia.uni.fmi.mjt.twitch.user;

public class UserImpl implements User {
    private final String username;
    private UserStatus userStatus;

    public UserImpl(String username) {
        this.username = username;
        this.userStatus = UserStatus.OFFLINE;
    }

    @Override
    public String getName() {
        return username;
    }

    @Override
    public UserStatus getStatus() {
        return userStatus;
    }

    @Override
    public void setStatus(UserStatus status) {
        this.userStatus = status;
    }
}
