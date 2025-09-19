package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findAllByOwnerId(long userId);

    Item findByIdAndOwnerId(long id, long userId);

    @Query("select i from Item i " +
            "where (upper(i.name) like upper(concat('%', ?1, '%')) " +
            " or upper(i.description) like upper(concat('%', ?1, '%')))" +
            "and i.available = true")
    List<Item> searchByText(@Param("text") String text);

    @Query("select i.id from Item i where i.owner.id = ?1")
    List<Long> findItemIdsByOwnerId(Long ownerId);
}
