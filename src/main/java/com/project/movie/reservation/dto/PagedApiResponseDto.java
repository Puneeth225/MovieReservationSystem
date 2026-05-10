package com.project.movie.reservation.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PagedApiResponseDto {

    int totalPages;
    long totalElements;
    List<?> currentPageData;
    int currentCount;
}
