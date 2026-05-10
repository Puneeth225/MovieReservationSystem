package com.project.movie.reservation.controller;

import com.project.movie.reservation.dto.ApiResponseDto;
import com.project.movie.reservation.dto.PagedApiResponseDto;
import com.project.movie.reservation.dto.ShowRequestDto;
import com.project.movie.reservation.entity.Seat;
import com.project.movie.reservation.entity.Show;
import com.project.movie.reservation.entity.Theatre;
import com.project.movie.reservation.exception.ShowTimingClashException;
import com.project.movie.reservation.repository.ShowRepository;
import com.project.movie.reservation.repository.TheatreRepository;
import com.project.movie.reservation.service.ShowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import static com.project.movie.reservation.constant.Messages.TIMING_CLASH;

@RestController
@RequestMapping("/api/v1/shows")
public class ShowController {

    private final ShowService showService;
    private final TheatreRepository theatreRespository;
    private final ShowRepository showRepository;

    @Autowired
    public ShowController(ShowService showService, TheatreRepository theatreRespository, ShowRepository showRepository) {
        this.showService = showService;
        this.theatreRespository = theatreRespository;
        this.showRepository = showRepository;
    }


    @GetMapping("/all")
    public ResponseEntity<PagedApiResponseDto> getAllShows(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        Page<Show> showPage = showService.getllShows(page, size);
        return ResponseEntity.ok(
                PagedApiResponseDto.builder()
                        .currentCount(showPage.getNumberOfElements())
                        .currentPageData(showPage.getContent())
                        .totalElements(showPage.getTotalElements())
                        .totalPages(showPage.getTotalPages())
                        .build()
        );
    }

    @GetMapping("/{showId}/seats")
    public ResponseEntity<ApiResponseDto> getShowSeatStructure(@PathVariable long showId) {
        // Fetch seats (this should check Redis first, then DB)
        List<Seat> seats = showService.getSeatsByShowId(showId);

        return ResponseEntity.ok(
                ApiResponseDto.builder()
                        .message("Seat structure fetched for show: " + showId)
                        .data(seats)
                        .build()
        );
    }

    @GetMapping("/filter")
    public ResponseEntity<PagedApiResponseDto> filterShows(
            @RequestParam(required = false) Long theaterId,
            @RequestParam(required = false) Long movieId,
            @RequestParam(required = false) String showDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        Page<Show> showPage = showService.filterShowsByTheaterIdAndMovieId(theaterId, movieId, PageRequest.of(page, size));
        return ResponseEntity.ok(
                PagedApiResponseDto.builder()
                        .currentCount(showPage.getNumberOfElements())
                        .currentPageData(showPage.getContent())
                        .totalElements(showPage.getTotalElements())
                        .totalPages(showPage.getTotalPages())
                        .build()
        );
    }

    @Secured({"ROLE_ADMIN", "ROLE_SUPER_ADMIN"})
    @GetMapping("/show/{showId}")
    public ResponseEntity<ApiResponseDto> getShowById(@PathVariable long showId){
        Show show = showService.getShowById(showId);
        return ResponseEntity.ok(
                ApiResponseDto.builder()
                        .data(show)
                        .message("Fetched show with id: " + show.getId())
                        .build()
        );
    }

    @Secured({"ROLE_ADMIN", "ROLE_SUPER_ADMIN"})
    @PostMapping("/show/create")
    public ResponseEntity<ApiResponseDto> createShow(@RequestBody ShowRequestDto showRequestDto){
        Theatre theatre = theatreRespository.findById(showRequestDto.getTheatreId())
                .orElseThrow(() -> new RuntimeException("Theatre not found"));

        //fix this exists by and all

        boolean isClashing = showRepository.existsByTheatreAndStartTimeBeforeAndEndTimeAfter(
                theatre,
                LocalDateTime.parse(showRequestDto.getEndTime()),
                LocalDateTime.parse(showRequestDto.getStartTime())
        );

        if (isClashing) {
            throw new ShowTimingClashException(TIMING_CLASH, HttpStatus.BAD_REQUEST);
        }
        Show show = showService.createNewShow(showRequestDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        ApiResponseDto.builder()
                                .message("Show created with id: " + show.getId())
                                .data(show)
                                .build()
                );
    }

    @Secured({"ROLE_ADMIN", "ROLE_SUPER_ADMIN"})
    @DeleteMapping("/show/delete/{showId}")
    public ResponseEntity<?> deleteShowById(@PathVariable long showId) {
        showService.deleteShowById(showId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
