package bg.vmware.reo101.ics.backend.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bg.vmware.reo101.ics.backend.data.Image;
import bg.vmware.reo101.ics.backend.data.ImageTag;
import bg.vmware.reo101.ics.backend.data.Tag;
import bg.vmware.reo101.ics.backend.repository.jpa.JpaImageRepository;
import bg.vmware.reo101.ics.backend.service.ImageService;

@Service
public class ImageServiceImpl implements ImageService {

    private final JpaImageRepository repository;

    @Autowired
    public ImageServiceImpl(JpaImageRepository repository) {
        this.repository = repository;
    }

    @Override
    public Image getById(Long id) throws EntityNotFoundException {
        Optional<Image> image = this.repository.getImageById(id);

        return image.orElseThrow(() -> {
            return new EntityNotFoundException("Image not found");
        });
    }

    @Override
    public Image getByUrl(String url) throws EntityNotFoundException {
        Optional<Image> image = this.repository.getImageByUrl(url);

        return image.orElseThrow(() -> {
            return new EntityNotFoundException("Image not found");
        });
    }

    @Override
    public Image create(Image image) {
        return image = repository.saveAndFlush(image);
    }

    @Override
    public List<Image> getAll() {
        return repository.findAll();
    }

    @Override
    public List<Image> getByTags(Set<Tag> tags) {
        if (tags.size() == 0) {
            return this.getAll();
        }

        // System.out.printf("Searching for: %s\nDifferences:\n",
        //         tags.stream()
        //                 .map((Tag tag) -> {
        //                     return String.format("Id: %d, Name: %s",
        //                             tag.getId(),
        //                             tag.getName());
        //                 })
        //                 .collect(Collectors.joining(", ", "[", "]")));

        // PERF: middleman problems
        // TODO: findAll() -> Page<Image>
        return repository.findAll().stream()
                // .parallel()
                .filter((Image image) -> {
                    return image.getImageTags().stream()
                            .map(ImageTag::getTag)
                            .collect(Collectors.toUnmodifiableSet())
                            .containsAll(tags);
                })
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public void flush() {
        repository.flush();
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
