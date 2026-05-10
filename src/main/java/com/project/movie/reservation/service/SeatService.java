package com.project.movie.reservation.service;

import com.project.movie.reservation.entity.Seat;
import com.project.movie.reservation.entity.Show;
import com.project.movie.reservation.enums.SeatStatus;
import com.project.movie.reservation.exception.ShowNotFoundException;
import com.project.movie.reservation.repository.SeatRepository;
import com.project.movie.reservation.repository.ShowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.project.movie.reservation.constant.Messages.SHOW_NOT_FOUND;

@Service
public class SeatService {

    private final SeatRepository seatRepository;
    private final RedisService redisService;
    private final ShowRepository showRepository;

    @Autowired
    public SeatService(SeatRepository seatRepository, RedisService redisService, ShowRepository showRepository) {
        this.seatRepository = seatRepository;
        this.redisService = redisService;
        this.showRepository = showRepository;
    }

    public List<Seat> createSeatsWithGivenPrice(int count, double price, String area) {
        List<Seat> seats = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            seats.add(Seat.builder()
                    .number(i)
                    .area(area)
                    .price(price)
                    .status(SeatStatus.UNBOOKED)
                    // Don't call seatRepository.save() here!
                    .build());
        }
        return seats;
    }


    public List<Seat> getSeatsByShow(Long showId) {

        Show show = showRepository.findById(showId)
                .orElseThrow(()->  new ShowNotFoundException(SHOW_NOT_FOUND, HttpStatus.NOT_FOUND) );


        String key = "seats:show:" + showId;

        // Check Redis
        List<Seat> cachedSeats = redisService.get(key, List.class);
        if (cachedSeats != null) return cachedSeats;

        // This now matches the name your Controller/Service used before
        List<Seat> seats = seatRepository.findByShowId(showId);

        if (!seats.isEmpty()) {
            redisService.set(key, seats, 10L);
        }
        return seats;
    }
}
