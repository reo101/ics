package bg.vmware.reo101.ics.backend.service;

import java.util.List;
import java.util.Set;

import bg.vmware.reo101.ics.backend.data.Image;
import bg.vmware.reo101.ics.backend.data.Tag;

public interface ImageService {

    Image getById(Long id);

    Image create(Image image);

    List<Image> getAll();

    List<Image> getByTags(Set<Tag> tags);

    void delete(Long id);
}
