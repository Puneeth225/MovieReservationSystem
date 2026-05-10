package com.project.movie.reservation.service;

import com.project.movie.reservation.dto.TheatreRequestDto;
import com.project.movie.reservation.entity.Theatre;
import com.project.movie.reservation.exception.TheatreNotFoundException;
import com.project.movie.reservation.repository.TheatreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import static com.project.movie.reservation.constant.Messages.THEATRE_NOT_FOUND;

@Service
public class TheatreService {

    private final TheatreRepository theatreRepository;

    @Autowired
    public TheatreService(TheatreRepository theatreRepository) {
        this.theatreRepository = theatreRepository;
    }


    public Page<Theatre> getAllTheatres(int page, int size) {
        return theatreRepository.findAll(PageRequest.of(page, size));
    }


    public Page<Theatre> getAllTheatresByLocation(int page, int size, String location) {

        return theatreRepository.findAllByLocation(location,  PageRequest.of(page, size));
    }


    public Theatre getTheatreById(long theaterId) {
        return theatreRepository.findById(theaterId)
                .orElseThrow(() -> new TheatreNotFoundException(THEATRE_NOT_FOUND, HttpStatus.NOT_FOUND));
    }

    public Theatre createNewTheatre(TheatreRequestDto theaterRequestDto) {
        Theatre theater = Theatre.builder()
                .name(theaterRequestDto.getName())
                .location(theaterRequestDto.getLocation())
                .build();
        return theatreRepository.save(theater);
    }

    public Theatre updateTheatreById(long theaterId, TheatreRequestDto theaterRequestDto) {
        return theatreRepository.findById(theaterId)
                .map(theater -> {
                    theater.setName(theaterRequestDto.getName());
                    theater.setLocation(theater.getLocation());
                    return theatreRepository.save(theater);
                })
                .orElseThrow(() -> new TheatreNotFoundException(THEATRE_NOT_FOUND, HttpStatus.NOT_FOUND));
    }

    public void deleteTheatreById(long theaterId) {
        theatreRepository.deleteById(theaterId);
        return;
    }
}
