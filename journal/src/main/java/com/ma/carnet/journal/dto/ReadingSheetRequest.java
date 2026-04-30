package com.ma.carnet.journal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReadingSheetRequest {
    private String status;
    private Float grade;
    private String review;
    private String quote;
}