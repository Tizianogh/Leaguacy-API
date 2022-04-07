package fr.dev.leaguacyapi.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Squad extends AbstractTimestampEntity {
    
    @Id
    @GeneratedValue
    @Column(columnDefinition = "BINARY(16)")
    private UUID uuidSquad;

    @NotEmpty(message = "Le nom de l'équipe est obligatoire.")
    private String squadName;
}
