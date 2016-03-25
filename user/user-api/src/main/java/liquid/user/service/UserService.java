package liquid.user.service;

import liquid.user.domain.Group;
import liquid.user.domain.GroupMember;
import liquid.user.model.User;

import java.util.Collection;
import java.util.List;

/**
 * Created by Tao Ma on 3/19/15.
 */
public interface UserService {
    Group addGroup(Group group);

    Collection<Group> findGroups();

    void assignToGroup(String userId, Integer groupId);

    GroupMember findByUsername(String userId);

    void register(User user);

    User find(String userId);

    Collection<User> findAll();

    void lock(String userId);

    void unlock(String userId);

    void edit(User user);

    void changePassword(String userId, String password);

    /**
     * Find usernames by groupId.
     * @param groupId
     * @return
     */
    List<String> findByGroup(String groupId);
}
