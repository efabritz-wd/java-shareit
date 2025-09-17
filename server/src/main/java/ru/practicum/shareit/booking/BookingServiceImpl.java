package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResultDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingMapper bookingMapper;
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;

    private Sort sort = Sort.by(Sort.Direction.DESC, "startDate");

    @Override
    public BookingResultDto addBooking(ItemDto itemDto, UserDto userDto, BookingRequestDto bookingRequestDto) {
        if (!itemDto.getAvailable()) {
            throw new ValidationException("Item not available for booking");
        }

        if (bookingRequestDto.getStart().isAfter(bookingRequestDto.getEnd())) {
            throw new ValidationException("Booking. End date should be after start date");
        }

        if (itemDto.getOwnerId().equals(userDto.getId())) {
            throw new ValidationException("Error owner - booker");
        }

        Booking booking = bookingMapper.requestToBooking(bookingRequestDto, itemDto, userDto, BookingStatus.WAITING);
        Booking createdBooking = bookingRepository.save(booking);
        BookingResultDto res = bookingMapper.fromBookingToDto(createdBooking);

        return res;
    }

    @Override
    public BookingResultDto approveBooking(Long userId, Boolean approved, Long bookingId) {
        if (bookingRepository.findById(bookingId).isEmpty()) {
            throw new NotFoundException("Booking not found for id: " + bookingId);
        }
        Booking booking = bookingRepository.findById(bookingId).get();

        if (!booking.getItem().getOwner().getId().equals(userId)) {
            throw new ForbiddenException("Owner does not correspond to approving user");
        }

        if (booking.getStatus().equals(BookingStatus.APPROVED)) {
            throw new ValidationException("Booking for id: " + booking.getId() + " already approved");
        }

        BookingStatus status = approved ? BookingStatus.APPROVED : BookingStatus.REJECTED;

        booking.setStatus(status);

        Booking bookingWithStatus = bookingRepository.save(booking);
        return bookingMapper.fromBookingToDto(bookingWithStatus);
    }

    @Override
    public BookingResultDto getBookingByIdAndUserId(Long bookingId, Long userId) {
        if (bookingRepository.findById(bookingId).isEmpty()) {
            throw new NotFoundException("Booking not found");
        }

        Booking booking = bookingRepository.findById(bookingId).get();

        boolean isOwnerOrBooker = userId.equals(booking.getItem().getOwner().getId())
                || userId.equals(booking.getBooker().getId());

        if (!isOwnerOrBooker) {
            throw new ForbiddenException("Booking available for booker or owner only");
        }

        return bookingMapper.fromBookingToDto(booking);
    }

    @Override
    public List<BookingResultDto> getBookingsByUserIdAndState(Long userId, String stateStr) {
        if (!State.isValid(stateStr)) {
            throw new ValidationException("State parameter is not valid");
        }

        State state = State.from(stateStr).get();
        LocalDateTime currentTime = LocalDateTime.now();

        List<Booking> bookings;

        switch (state) {
            case CURRENT:
                bookings = bookingRepository.findByBookerIdAndStartIsBeforeAndEndIsAfter(
                        userId, currentTime, currentTime, sort);
                break;
            case PAST:
                bookings = bookingRepository.findByBookerIdAndEndIsBefore(
                        userId, currentTime, Sort.by(Sort.Direction.DESC, "endDate"));
                break;
            case FUTURE:
                bookings = bookingRepository.findByBookerIdAndStartIsAfter(
                        userId, currentTime, sort);
                break;
            case WAITING:
                bookings = bookingRepository.findByBookerIdAndStatus(
                        userId, BookingStatus.WAITING, sort);
                break;
            case REJECTED:
                bookings = bookingRepository.findByBookerIdAndStatus(
                        userId, BookingStatus.REJECTED, sort);
                break;
            case ALL:
            default:
                bookings = bookingRepository.findByBookerId(userId);
                break;
        }

        return bookingMapper.fromBookingToDto(bookings);
    }

    @Override
    public List<BookingResultDto> getBookingsByOwnerIdAndState(Long userId, String stateStr) {
        if (!State.isValid(stateStr)) {
            throw new ValidationException("State parameter is not valid");
        }

        State state = State.from(stateStr).get();

        LocalDateTime currentTime = LocalDateTime.now();
        List<Booking> bookings;


        List<Long> itemIds = new ArrayList<>();
        try {
            itemIds = itemRepository.findItemIdsByOwnerId(userId);
        } catch (Exception e) {
            System.err.println("Fehler beim Abrufen der Item-IDs: " + e.getMessage());
        }

        switch (state) {
            case CURRENT:
                bookings = bookingRepository.findByItemIdInAndStartIsBeforeAndEndIsAfter(
                        itemIds, currentTime, currentTime, sort);
                break;
            case PAST:
                bookings = bookingRepository.findByItemIdInAndEndIsBefore(
                        itemIds, currentTime, Sort.by(Sort.Direction.DESC, "endDate"));
                break;
            case FUTURE:
                bookings = bookingRepository.findByItemIdInAndStartIsAfter(
                        itemIds, currentTime, sort);
                break;
            case WAITING:
                bookings = bookingRepository.findByItemIdInAndStatus(
                        itemIds, BookingStatus.WAITING, sort);
                break;
            case REJECTED:
                bookings = bookingRepository.findByItemIdInAndStatus(
                        itemIds, BookingStatus.REJECTED, sort);
                break;
            case ALL:
            default:
                bookings = bookingRepository.findByItemIdIn(itemIds, sort);
                break;
        }

        return bookingMapper.fromBookingToDto(bookings);
    }


}
