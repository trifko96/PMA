package com.pma.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DailySummary {

    private Date day;
    private float kcalIn;
    private float kcalOut;

}
