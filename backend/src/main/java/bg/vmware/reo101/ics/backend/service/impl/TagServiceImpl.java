package bg.vmware.reo101.ics.backend.service.impl;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bg.vmware.reo101.ics.backend.data.Tag;
import bg.vmware.reo101.ics.backend.repository.jpa.JpaTagRepository;
import bg.vmware.reo101.ics.backend.service.TagService;

@Service
public class TagServiceImpl implements TagService {

    private final JpaTagRepository repository;

    @Autowired
    public TagServiceImpl(JpaTagRepository repository) {
        this.repository = repository;
    }

    @Override
    public Tag getById(Long id) throws EntityNotFoundException {
        Optional<Tag> tag = this.repository.getTagById(id);

        return tag.orElseThrow(() -> {
            return new EntityNotFoundException("Tag not found");
        });
    }

    @Override
    public Tag getByName(String name) throws EntityNotFoundException {
        Optional<Tag> tag = this.repository.getTagByName(name);

        return tag.orElseThrow(() -> {
            return new EntityNotFoundException("Tag not found");
        });
    }

    @Override
    public Tag create(Tag tag) {
        return repository.saveAndFlush(tag);
    }

    @Override
    public Tag createIfMissing(String name) {
        Tag tag;

        try {
            tag = this.getByName(name);
        } catch (EntityNotFoundException e) {
            tag = this.create(new Tag(name));
        }

        return tag;
    }

    @Override
    public List<Tag> getAll() {
        return repository.findAll();
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
