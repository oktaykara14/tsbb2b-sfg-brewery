package guru.springframework.brewery.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.*;

import jakarta.persistence.*;
import java.sql.Timestamp;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@MappedSuperclass
public class BaseEntity {

    public BaseEntity(UUID id, Long version, Timestamp createdDate, Timestamp lastModifiedDate) {
        this.id = id;
        this.version = version;
        this.createdDate = createdDate;
        this.lastModifiedDate = lastModifiedDate;
    }

    @Id
    @GeneratedValue(generator = "UUID")
    @Column(length = 36, columnDefinition = "varchar", updatable = false, nullable = false )
    private UUID id;

    @Version
    private Long version;

    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp createdDate;

    @UpdateTimestamp
    private Timestamp lastModifiedDate;
}
