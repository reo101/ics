package bg.vmware.reo101.ics.backend.data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @Column(name = "url", unique = true)
    private String url;

    @Column(name = "added_on")
    private LocalDateTime addedOn;

    // @Column(name = "service")
    // private Service service;

    //@formatter:off
    @OneToMany(mappedBy = "imageId",
               cascade = CascadeType.ALL)
    @JsonProperty(value = "tags",
                  access = JsonProperty.Access.READ_ONLY)
    //@formatter:on
    private Set<ImageTag> imageTags;

    // //@formatter:on
    // @OneToMany(cascade = CascadeType.ALL)
    // //@formatter:off
    // @JsonIgnore
    // private Set<Tag> tags;

    // @OneToMany(mappedBy = "tag_name")
    // @JsonIgnore
    // private Set<String> tagNames;

    @Column(name = "width")
    private Integer width;

    @Column(name = "height")
    private Integer height;
}
