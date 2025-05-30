package techno_express.backend.manager;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity(name="ticket_status")
public class TicketStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;
}
