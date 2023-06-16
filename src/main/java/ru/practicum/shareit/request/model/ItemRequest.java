package ru.practicum.shareit.request.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "REQUESTS")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "DESCRIPTION")
    private String description;

    @ManyToOne
    @JoinColumn(name = "REQUESTOR_ID")
    private User requestor;

    @CreationTimestamp
    @Column(name = "CREATED")
    private LocalDateTime created;
}