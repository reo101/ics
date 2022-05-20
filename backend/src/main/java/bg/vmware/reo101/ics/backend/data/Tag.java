package bg.vmware.reo101.ics.backend.data;

import java.io.Serializable;
import java.util.Objects;
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

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "Tag")
@Table(name = "tags")
public class Tag implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @JsonIgnore
    private Long id;

    @Column(name = "name", unique = true)
    private String name;

    //@formatter:off
    @OneToMany(mappedBy = "id",
               cascade = CascadeType.ALL)
    //@formatter:on
    @JsonIgnore
    private Set<Image> images;

    public Tag(String name) {
        this.name = name;
    }

    // @Override
    // public boolean equals(Object obj) {
    //     if (this == obj) {
    //         return true;
    //     }
    //
    //     if (obj == null || this.getClass() != obj.getClass()) {
    //         return false;
    //     }
    //
    //     Tag tag = (Tag) obj;
    //
    //     return Objects.equals(this.name, tag.name);
    // }
}
