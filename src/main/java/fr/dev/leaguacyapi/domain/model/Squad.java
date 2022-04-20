package fr.dev.leaguacyapi.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.UUID;

import static javax.persistence.TemporalType.TIMESTAMP;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Squad {
  
    @Id
    @GeneratedValue
    @Type(type = "uuid-char")
    private UUID uuidSquad;

    @NotEmpty(message = "Le nom de l'équipe est obligatoire.")
    private String squadName;

    @Temporal(TIMESTAMP)
    private Date created;

    @Temporal(TIMESTAMP)
    private Date updated;

    @PrePersist
    protected void onCreate() {
        updated = created = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        updated = new Date();
    }
}


