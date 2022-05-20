package bg.vmware.reo101.ics.backend.service;

import java.util.List;

import bg.vmware.reo101.ics.backend.data.Tag;

public interface TagService {

    Tag getById(Long id);

    Tag getByName(String name);

    Tag create(Tag tag);

    Tag createIfMissing(String name);

    List<Tag> getAll();

    void delete(Long id);
}
