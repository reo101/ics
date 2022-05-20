package bg.vmware.reo101.ics.backend.data;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;

@Getter
@Setter
@EqualsAndHashCode
@Embeddable
public class ImageTagId implements Serializable {

    @Column(name = "image_id")
    private Long imageId;

    @Column(name = "tag_id")
    private Long tagId;
}
