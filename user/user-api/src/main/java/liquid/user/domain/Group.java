package liquid.user.domain;


import javax.persistence.*;
import java.util.Collection;

/**
 * Created by Tao Ma on 3/17/15.
 */
@Entity(name = "groups")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "group_name")
    private String name;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "group")
    private Collection<GroupMember> members;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "group")
    private Collection<GroupAuthority> authorities;

    public Group() { }

    public Group(Integer id) {
        this.id = id;
    }

    public Group(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<GroupMember> getMembers() {
        return members;
    }

    public void setMembers(Collection<GroupMember> members) {
        this.members = members;
    }

    public Collection<GroupAuthority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Collection<GroupAuthority> authorities) {
        this.authorities = authorities;
    }
}
