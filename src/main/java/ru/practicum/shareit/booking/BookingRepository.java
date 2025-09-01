package ru.practicum.shareit.booking;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("SELECT b FROM Booking b " +
            "JOIN Item i ON b.item.id = i.id " +
            "WHERE b.booker.id = ?1 " +
            "ORDER BY b.start DESC")
    List<Booking> findByBookerId(Long bookerId);

    List<Booking> findByBookerIdAndStatus(Long bookerId, String status, Sort sort);

    //current
    List<Booking> findByBookerIdAndStartDateIsBeforeAndEndDateIsAfter(Long bookerId, LocalDateTime start, LocalDateTime end, Sort sort);

    //past
    List<Booking> findByBookerIdAndEndDateIsBefore(Long bookerId, LocalDateTime end, Sort sort);

    //future
    List<Booking> findByBookerIdAndStartDateIsAfter(Long bookerId, LocalDateTime start, Sort sort);

    List<Booking> findByItemIdIn(List<Long> itemIds, Sort sort);

    List<Booking> findByItemIdInAndStatus(List<Long> itemIds, String status, Sort sort);

    //current
    List<Booking> findByItemIdInAndStartDateIsBeforeAndEndDateIsAfter(List<Long> itemIds, LocalDateTime start, LocalDateTime end, Sort sort);

    //past
    List<Booking> findByItemIdInAndEndDateIsBefore(List<Long> itemIds, LocalDateTime end, Sort sort);

    //future
    List<Booking> findByItemIdInAndStartDateIsAfter(List<Long> itemIds, LocalDateTime start, Sort sort);

    @Query("SELECT  b  FROM Booking b JOIN b.item i " +
            "WHERE i.id = :itemId AND b.end < :currentTime" +
            " ORDER BY b.end ASC LIMIT 1")
    Booking getLastBooking(Long itemId, LocalDateTime currentTime);

    @Query("SELECT b FROM Booking b JOIN b.item i " +
            "WHERE i.id = :itemId AND b.start > :currentTime AND b.status != 'REJECTED'" +
            " ORDER BY b.start ASC LIMIT 1")
    Booking getNextBooking(Long itemId, LocalDateTime currentTime);

    @Query("SELECT b FROM Booking b  " +
            "WHERE b.booker.id =:bookerId " +
            "AND b.item.id =:itemId " +
            "AND b.end< :currentTime")
    List<Booking> findCompletedBookingsByUser(
            @Param("bookerId") Long bookerId,
            @Param("itemId") Long itemId,
            @Param("currentTime") LocalDateTime currentTime);

}
