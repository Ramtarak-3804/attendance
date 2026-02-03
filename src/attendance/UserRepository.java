package attendance;

import java.util.List;

public interface UserRepository {
    void add(User user);

    User findById(String id);

    List<User> listAll();
}
