package bg.vmware.reo101.ics.backend.repository.jpa;

import bg.vmware.reo101.ics.backend.data.Image;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaImageRepository extends JpaRepository<Image, Long> {

    Optional<Image> getImageById(Long id);

    Optional<Image> getImageByUrl(String url);
}
