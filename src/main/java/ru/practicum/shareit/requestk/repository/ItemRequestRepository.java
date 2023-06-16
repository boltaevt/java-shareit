package ru.practicum.shareit.requestk.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.requestk.model.ItemRequest;
import ru.practicum.shareit.userk.model.User;

import java.util.Collection;

@Repository
public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {
    Collection<ItemRequest> findAllByRequestor(User requestor);

    Page<ItemRequest> findAllByRequestorIdNot(Long requestorId, Pageable pageable);
}