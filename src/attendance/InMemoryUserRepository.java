package attendance;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryUserRepository implements UserRepository {
    private final Map<String, User> users = new ConcurrentHashMap<>();

    @Override
    public void add(User user) {
        Objects.requireNonNull(user, "user");
        users.put(user.getId(), user);
    }

    @Override
    public User findById(String id) {
        Objects.requireNonNull(id, "id");
        return users.get(id);
    }

    @Override
    public List<User> listAll() {
        return new ArrayList<>(users.values());
    }
}
