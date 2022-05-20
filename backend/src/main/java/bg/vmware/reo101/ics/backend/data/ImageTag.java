package bg.vmware.reo101.ics.backend.data;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "ImageTag")
@Table(name = "images_tags", indexes = {
        @Index(columnList = "image_id", name = "tags_by_image_id"),
        @Index(columnList = "tag_id", name = "images_by_tag_id")
})
@IdClass(ImageTagId.class)
public class ImageTag implements Serializable {

    @Id
    @JsonIgnore
    private Long imageId;

    @Id
    @JsonIgnore
    private Long tagId;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @MapsId("image_id")
    @JoinColumn(name = "image_id", referencedColumnName = "id")
    @JsonIgnore
    private Image image;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @MapsId("tag_id")
    @JoinColumn(name = "tag_id", referencedColumnName = "id")
    private Tag tag;

    @Column(name = "certainty")
    private Float certainty;

    @JsonProperty("tag")
    public String getTagName() {
        return this.tag.getName();
    }
}
