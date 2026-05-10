package com.project.movie.reservation.repository;

import com.project.movie.reservation.entity.Theatre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TheatreRepository extends JpaRepository<Theatre, Long> {
    Page<Theatre> findAllByLocation(String location, Pageable pageable);
}
