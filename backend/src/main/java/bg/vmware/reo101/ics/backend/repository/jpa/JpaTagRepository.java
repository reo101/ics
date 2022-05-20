package bg.vmware.reo101.ics.backend.repository.jpa;

import bg.vmware.reo101.ics.backend.data.Tag;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaTagRepository extends JpaRepository<Tag, Long> {

    Optional<Tag> getTagById(Long id);

    Optional<Tag> getTagByName(String name);
}
