package liquid.user.db.repository;

import liquid.user.domain.GroupMember;
import org.springframework.data.repository.CrudRepository;
import org.springframework.jca.cci.core.InteractionCallback;

import java.util.Collection;

/**
 * Created by Tao Ma on 3/18/15.
 */
public interface GroupMemberRepository extends CrudRepository<GroupMember, Integer> {
    GroupMember findByUsername(String username);

    Collection<GroupMember> findByGroupId(Integer id);
}
