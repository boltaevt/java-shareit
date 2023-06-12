package ru.practicum.shareit.request.model;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "REQUESTS")
@Getter
@Setter
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