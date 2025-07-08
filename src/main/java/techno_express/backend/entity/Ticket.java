package techno_express.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity(name="tickets")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "sender_id")
    private User sender;

    private String title;
    private String description;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
    private LocalDateTime closed_at;
    private String image;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "attendant_id")
    private User attendant;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "status")
    private TicketStatus status;
}
