package ru.practicum.shareit.itemk.model;

import lombok.*;
import ru.practicum.shareit.bookingk.model.BookingShort;
import ru.practicum.shareit.requestk.model.ItemRequest;
import ru.practicum.shareit.userk.model.User;

import javax.persistence.*;

@Entity
@Table(name = "ITEMS")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "IS_AVAILABLE")
    private Boolean available;

    @ManyToOne
    @JoinColumn(name = "OWNER_ID")
    private User owner;

    @ManyToOne
    @JoinColumn(name = "REQUEST_ID")
    private ItemRequest request;

    @Transient
    private BookingShort lastBooking;

    @Transient
    private BookingShort nextBooking;
}