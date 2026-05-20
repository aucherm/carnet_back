package com.ma.carnet.journal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReadingSheetWithBookRequest {
    private String title;
    private String author;
    private String isbn;
    private String cover;
    private String status;
    private Float grade;
    private String review;
    private String quote;
}