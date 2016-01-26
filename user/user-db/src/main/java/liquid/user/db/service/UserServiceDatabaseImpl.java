package liquid.user.db.service;

import liquid.user.db.repository.GroupMemberRepository;
import liquid.user.db.repository.GroupRepository;
import liquid.user.db.repository.UserProfileRepository;
import liquid.user.db.repository.UserRepository;
import liquid.user.domain.Group;
import liquid.user.domain.GroupMember;
import liquid.user.domain.UserProfile;
import liquid.user.model.User;
import liquid.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Tao Ma on 3/19/15.
 */
public class UserServiceDatabaseImpl implements UserService {
    @Autowired
    private AuthenticationManagerBuilder authenticationManagerBuilder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private GroupMemberRepository groupMemberRepository;

    @Transactional
    public Group find(Integer id) {
        Group group = groupRepository.findOne(id);
        if (null != group) {
            group.getMembers().size();
        }
        authenticationManagerBuilder.getDefaultUserDetailsService();
        return group;
    }

    public Group save(Group group) {
        return groupRepository.save(group);
    }

    @Override
    public Group addGroup(Group group) {
        return null;
    }

    @Override
    public Collection<Group> findGroups() {
        return groupRepository.findAll();
    }

    @Override
    public void assignToGroup(String username, Integer groupId) {
        GroupMember member = findByUsername(username);
        if (null == member) {
            synchronized (UserService.class) {
                member = findByUsername(username);
                if (null == member) {
                    member = new GroupMember(username, new Group(groupId));
                } else {
                    member.setGroup(new Group(groupId));
                }
            }
        } else {
            member.setGroup(new Group(groupId));
        }
        groupMemberRepository.save(member);
    }

    @Override
    public GroupMember findByUsername(String username) {
        return groupMemberRepository.findByUsername(username);
    }

    @Transactional("userTransactionManager")
    @Override
    public void register(User user) {
        User oldOne = find(user.getUid());
        if(oldOne != null)
            throw new RuntimeException();

        UserProfile profile = convertTo(user);

        profile.setPassword(encodePassword(user.getPassword()));
        profile.setEnabled(false);
        userProfileRepository.save(profile);

        GroupMember groupMember = new GroupMember();
        groupMember.setGroup(new Group(Integer.valueOf(user.getGroup())));
        groupMember.setUsername(user.getUid());
        groupMemberRepository.save(groupMember);
    }

    @Override
    public User find(String uid) {
        UserProfile profile = userProfileRepository.findOne(uid);
        User user = convertTo(profile);
        return user;
    }

    @Override
    public Collection<User> findAll() {
        List<User> users = new ArrayList<User>();
        Iterable<UserProfile> profiles = userProfileRepository.findAll();
        for (UserProfile profile : profiles) {
            User user = convertTo(profile);
            users.add(user);
        }
        return users;
    }

    @Override
    public void lock(String uid) {
        liquid.user.domain.User entity = userRepository.findOne(uid);
        entity.setEnabled(false);
        userRepository.save(entity);
    }

    @Override
    public void unlock(String uid) {
        liquid.user.domain.User entity = userRepository.findOne(uid);
        entity.setEnabled(true);
        userRepository.save(entity);
    }

    @Override
    public void edit(User user) {
        UserProfile profile = userProfileRepository.findOne(user.getUid());
        assignTo(profile, user);
        userProfileRepository.save(profile);
    }

    @Override
    public void changePassword(String userId, String password) {
        UserProfile profile = userProfileRepository.findOne(userId);
        profile.setPassword(encodePassword(password));
        userProfileRepository.save(profile);
    }

    private String encodePassword(String plain) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(plain);
    }

    private UserProfile convertTo(User user) {
        UserProfile profile = new UserProfile();
        assignTo(profile, user);
        return profile;
    }

    private void assignTo(UserProfile profile, User user) {
        profile.setUsername(user.getUid());
        profile.setFirstName(user.getGivenName());
        profile.setLastName(user.getSurname());
        profile.setEmail(user.getEmail());
        profile.setCell(user.getCell());
        profile.setPhone(user.getPhone());
    }

    private User convertTo(UserProfile profile) {
        User user = new User();
        user.setUid(profile.getUsername());
        user.setEnabled(profile.getEnabled());
        user.setGivenName(profile.getFirstName());
        user.setSurname(profile.getLastName());
        user.setEmail(profile.getEmail());
        user.setCell(profile.getCell());
        user.setPhone(profile.getPhone());
        return user;
    }

}
