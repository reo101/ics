package bg.vmware.reo101.ics.backend.data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "Image")
@Table(name = "images")
public class Image implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @Column(name = "url") //, unique = true)
    private String url;

    @Column(name = "added_on")
    private LocalDateTime addedOn;

    // TODO: Service Factory (with select dropdown in frontend)
    // @Column(name = "service")
    // private Service service;

    //@formatter:off
    @OneToMany(mappedBy = "imageId",
               fetch = FetchType.EAGER,
               // cascade = CascadeType.ALL, // TODO: uncomment
               orphanRemoval = true)
    @JsonProperty(value = "tags",
                  access = JsonProperty.Access.READ_ONLY)
    //@formatter:on
    private Set<ImageTag> imageTags;

    // @OneToOne(mappedBy = "imageId")
    // @JsonProperty(value = "hash",
    //               access = JsonProperty.Access.READ_ONLY)
    // private Hash hash;

    @Column(name = "width")
    private Integer width;

    @Column(name = "height")
    private Integer height;

    @JsonProperty("tags")
    public Map<String, Float> getTagPairs() {
        return this.imageTags.stream()
            .collect(Collectors.toMap(ImageTag::getTagName, ImageTag::getCertainty));
    }
}
