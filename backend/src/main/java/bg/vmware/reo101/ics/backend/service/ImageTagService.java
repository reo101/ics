package bg.vmware.reo101.ics.backend.service;

import java.util.List;

import bg.vmware.reo101.ics.backend.data.ImageTag;
import bg.vmware.reo101.ics.backend.data.ImageTagId;

public interface ImageTagService {

    ImageTag get(ImageTagId id);

    List<ImageTag> getAll();

    ImageTag create(ImageTag imageTag);

    void delete(ImageTagId id);
}
