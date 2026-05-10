package com.project.movie.reservation.service;

import com.project.movie.reservation.dto.MovieRequestDto;
import com.project.movie.reservation.entity.Movie;
import com.project.movie.reservation.entity.Show;
import com.project.movie.reservation.enums.MovieType;
import com.project.movie.reservation.exception.MovieNotFoundException;
import com.project.movie.reservation.repository.MovieRepository;
import com.project.movie.reservation.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static com.project.movie.reservation.constant.Messages.MOVIE_NOT_FOUND;

@Service
public class MovieService {

    private final MovieRepository movieRepository;
    private final RedisService redisService;
    private final ReservationRepository reservationRepository;

    @Autowired
    public MovieService(MovieRepository movieRepository, RedisService redisService, ReservationRepository reservationRepository){
        this.movieRepository = movieRepository;
        this.redisService = redisService;
        this.reservationRepository = reservationRepository;
    }

    public Page<Movie> getAllMovies(int page, int pageSize) {
        String cacheKey = "movies:all:p" + page + ":s" + pageSize;

        List<Movie> cachedContent = redisService.get(cacheKey, List.class);

        if (cachedContent != null) {
            System.out.println("Movies frm redis");
            return new PageImpl<>(cachedContent, PageRequest.of(page, pageSize), cachedContent.size());
        }

        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Movie> moviePage = movieRepository.findAll(pageable);

        if (!moviePage.isEmpty()) {
            redisService.set(cacheKey, moviePage.getContent(), 60L);
        }

        return moviePage;
    }

    public Movie getMovieById(long movieId) {
        String cacheKey = "movie:" + movieId;

        // 1. Try to get from Redis
        Movie cachedMovie = redisService.get(cacheKey, Movie.class);
        if (cachedMovie != null){
            System.out.println("Data from redis");
            return cachedMovie;
        }

        // 2. If not in Redis, get from DB
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new MovieNotFoundException(MOVIE_NOT_FOUND, HttpStatus.NOT_FOUND));

        // 3. Put in Redis for next time (expires in 60 mins)
        redisService.set(cacheKey, movie, 60L);

        return movie;

    }

    public Movie createNewMovie(MovieRequestDto movieRequestDto) {

        Movie movie = Movie.builder()
                .movieLanguage(movieRequestDto.getMovieLanguage())
                .movieLength(movieRequestDto.getMovieLength())
                .genre(movieRequestDto.getGenre().stream().map(g -> MovieType.valueOf(g.toUpperCase()))
                        .toList())
                .movieName(movieRequestDto.getMovieName())
                .releaseDate(LocalDate.parse(movieRequestDto.getReleaseDate()))
                .build();
        redisService.deleteByPattern("movies:all:");
        return movieRepository.save(movie);
    }
    public Movie updateMovieById(long movieId, MovieRequestDto movieRequestDto) {


        Movie updatedMovie = movieRepository.findById(movieId)
                .map(movieInDb -> {
                    movieInDb.setMovieName(movieRequestDto.getMovieName());
                    movieInDb.setGenre(movieRequestDto.getGenre().stream().map(MovieType::valueOf).toList());
                    movieInDb.setMovieLanguage(movieRequestDto.getMovieLanguage());
                    movieInDb.setReleaseDate(LocalDate.parse(movieRequestDto.getReleaseDate()));
                    movieInDb.setMovieLength(movieRequestDto.getMovieLength());

                    return movieRepository.save(movieInDb);
                }).orElseThrow(() -> new MovieNotFoundException(MOVIE_NOT_FOUND, HttpStatus.NOT_FOUND));

        redisService.deleteByPattern("movies:all:");
        redisService.delete("movie:" + movieId); // individual cache clearance

        return updatedMovie;
    }


    @Transactional
    public void deleteMovieById(long movieId) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new MovieNotFoundException(MOVIE_NOT_FOUND, HttpStatus.NOT_FOUND));

        // 1. CLEAR REDIS
        redisService.deleteByPattern("movies:all:");
        redisService.delete("movie:" + movieId);
        if (movie.getShows() != null) {
            movie.getShows().forEach(show -> redisService.delete("seats:show:" + show.getId()));
        }

        // 2. MANUALLY CLEAN RESERVATIONS (If not using CascadeType.ALL)
        if (movie.getShows() != null) {
            movie.getShows().forEach(show -> {
                reservationRepository.deleteByShowId(show.getId());
            });
        }

        // 3. Physical deletion
        movieRepository.delete(movie);
    }

    public List<Show> getShowsByMovieId(long movieId) {
        return movieRepository.findById(movieId)
                .map(Movie::getShows)
                .orElseThrow(() -> new MovieNotFoundException(MOVIE_NOT_FOUND,HttpStatus.NOT_FOUND ));

    }
}
