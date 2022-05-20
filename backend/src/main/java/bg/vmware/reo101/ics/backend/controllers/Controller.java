package bg.vmware.reo101.ics.backend.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;
import javax.swing.ImageIcon;

import com.google.gson.Gson;

import org.hibernate.type.LocalDateType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
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
    public ResponseEntity<Void> addImage(@RequestBody Image image,
            @RequestParam(name = "noCache", defaultValue = "false") Boolean skipCache) {
        try {
            URL imageUrl = new URL(image.getUrl());

            java.awt.Image image2 = new ImageIcon(imageUrl).getImage();

            image.setWidth(image2.getWidth(null));
            image.setHeight(image2.getHeight(null));
        } catch (MalformedURLException e1) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Image imageFromDatabase;

        if (skipCache) {
            // TODO: query new tags from external API
        }

        List<TagCouple> tagCouples;

        try {
            tagCouples = queryTagCouples(image.getUrl());
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // NOTE: Automatically attaches a valid ID to image
        this.imageService.create(image);

        image.setAddedOn(LocalDateTime.now());

        tagCouples.stream()
                .forEach((TagCouple tagCouple) -> {
                    Tag tag = this.tagService.createIfMissing(
                            tagCouple.getTag().getEn());
                    this.imageTagService.create(
                            new ImageTag(
                                    image.getId(), tag.getId(),
                                    image, tag,
                                    tagCouple.getConfidence()));
                });

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private List<TagCouple> queryTagCouples(String imageUrl) throws MalformedURLException, IOException {
        String requestUrl = this.imaggaEndpoint + imageUrl;
        URL urlObject = new URL(requestUrl);

        HttpURLConnection connection = (HttpURLConnection) urlObject.openConnection();

        connection.setRequestProperty("Authorization", this.imaggaAuth);

        BufferedReader connectionInput = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        String jsonResponse = connectionInput.readLine();

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
