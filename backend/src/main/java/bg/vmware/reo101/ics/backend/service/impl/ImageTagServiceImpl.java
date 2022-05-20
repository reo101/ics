package bg.vmware.reo101.ics.backend.service.impl;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bg.vmware.reo101.ics.backend.data.ImageTag;
import bg.vmware.reo101.ics.backend.data.ImageTagId;
import bg.vmware.reo101.ics.backend.repository.jpa.JpaImageTagRepository;
import bg.vmware.reo101.ics.backend.service.ImageTagService;

@Service
public class ImageTagServiceImpl implements ImageTagService {

    private final JpaImageTagRepository repository;

    @Autowired
    public ImageTagServiceImpl(JpaImageTagRepository repository) {
        this.repository = repository;
    }

    @Override
    public ImageTag get(ImageTagId id) throws EntityNotFoundException{
        return this.repository.getById(id);
    }

    @Override
    public List<ImageTag> getAll() {
        return this.repository.findAll();
    }

    @Override
    public ImageTag create(ImageTag imageTag) {
        return this.repository.saveAndFlush(imageTag);
    }

    @Override
    public void delete(ImageTagId id) {
        this.repository.deleteById(id);
    }
}
