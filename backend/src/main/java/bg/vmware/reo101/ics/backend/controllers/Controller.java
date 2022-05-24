package bg.vmware.reo101.ics.backend.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;
import javax.swing.ImageIcon;

import com.google.gson.Gson;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import bg.vmware.reo101.ics.backend.controllers.Controller.Root.Result.TagCouple;
import bg.vmware.reo101.ics.backend.data.Image;
import bg.vmware.reo101.ics.backend.data.ImageTag;
import bg.vmware.reo101.ics.backend.data.Tag;
import bg.vmware.reo101.ics.backend.service.ImageService;
import bg.vmware.reo101.ics.backend.service.ImageTagService;
import bg.vmware.reo101.ics.backend.service.TagService;
import lombok.Getter;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*", exposedHeaders = "If-Match")
@RequestMapping("/images")
public class Controller {

    private Gson GSON = new Gson();

    private ImageService imageService;
    private ImageTagService imageTagService;
    private TagService tagService;

    @Value("${api.imagga.endpoint}")
    private String imaggaEndpoint;
    @Value("${api.imagga.auth.key}")
    private String imaggaKey;
    @Value("${api.imagga.auth.secret}")
    private String imaggaSecret;
    @Value("${api.imagga.auth.auth}")
    private String imaggaAuth;

    @Autowired
    public Controller(ImageService imageService, ImageTagService imageTagService, TagService tagService) {
        this.imageService = imageService;
        this.imageTagService = imageTagService;
        this.tagService = tagService;
    }

    @GetMapping
    public ResponseEntity<Collection<Image>> getImages(
            @RequestParam(name = "tags") Optional<List<String>> tagNames) {
        Set<Tag> tags;

        try {
            tags = tagNames
                    .orElseGet(List::<String>of)
                    .stream()
                    // .parallel()
                    .map(this.tagService::getByName)
                    .collect(Collectors.toSet());
        } catch (EntityNotFoundException e) {
            // NOTE: Tag not found
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(this.imageService.getByTags(tags), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Image> getImageById(@PathVariable final Long id) {
        try {
            return new ResponseEntity<>(this.imageService.getById(id), HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(value = "/add")
    @Transactional(propagation = Propagation.NESTED)
    public ResponseEntity<Image> addImage(@RequestBody Image image,
            @RequestParam(name = "noCache", defaultValue = "false") Boolean skipCache) {
        // // Check for existance of same url
        // this.imageService.getByUrl(image.getUrl());

        // Image imageFromDatabase;

        if (skipCache) {
            // TODO: query new tags from external API
        }

        image.setAddedOn(LocalDateTime.now());

        try {
            URL imageUrl = new URL(image.getUrl());

            java.awt.Image awtImage = new ImageIcon(imageUrl).getImage();

            image.setWidth(awtImage.getWidth(null));
            image.setHeight(awtImage.getHeight(null));
        } catch (MalformedURLException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        List<TagCouple> tagCouples;

        try {
            tagCouples = queryTagCouples(image.getUrl());
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // NOTE: Automatically attaches a valid ID to image
        this.imageService.create(image);

        Set<ImageTag> imageTags = new HashSet<>();

        tagCouples.stream()
                .sequential()
                .forEach((TagCouple tagCouple) -> {
                    Tag tag = this.tagService.createIfMissing(
                            tagCouple.getTag().getEn());
                    ImageTag imageTag = new ImageTag(
                                    image.getId(), tag.getId(),
                                    image, tag,
                                    tagCouple.getConfidence());
                    this.imageTagService.create(imageTag);
                    imageTags.add(imageTag);
                });

        // Manually set imageTags before the DB transaction is made
        image.setImageTags(imageTags);

        return new ResponseEntity<>(image, HttpStatus.OK);
    }

    private List<TagCouple> queryTagCouples(String imageUrl) throws MalformedURLException, IOException {
        String requestUrl = this.imaggaEndpoint + imageUrl;
        URL urlObject = new URL(requestUrl);

        HttpURLConnection connection = (HttpURLConnection) urlObject.openConnection();

        connection.setRequestProperty("Authorization", this.imaggaAuth);

        BufferedReader connectionInput = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        String jsonResponse = connectionInput.readLine();

        connectionInput.close();

        return GSON.fromJson(jsonResponse, Root.class).getResult().getTags();
    }

    @Getter
    public static class Root {
        @Getter
        public static class Result {
            @Getter
            public static class TagCouple {
                @Getter
                public static class Name {
                    private String en;
                }

                private Float confidence;
                private Name tag;
            }

            private List<TagCouple> tags;
        }

        @Getter
        public static class Status {
            private String text;
            private String type;
        }

        private Result result;
        private Status status;
    }
}
