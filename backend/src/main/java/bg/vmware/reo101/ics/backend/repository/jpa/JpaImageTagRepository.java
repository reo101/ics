package bg.vmware.reo101.ics.backend.repository.jpa;

import bg.vmware.reo101.ics.backend.data.ImageTag;
import bg.vmware.reo101.ics.backend.data.ImageTagId;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaImageTagRepository extends JpaRepository<ImageTag, ImageTagId> {

    ImageTag getById(ImageTagId id);
}
