package com.Clover.prueba.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class FormatterFechas {
    public FormatterFechas (){}
    public static String formatDate(String date, String formatoFinal, boolean numeroFormato) {
        String fechaFinal;
        LocalDate fecha;
        LocalDateTime fechaS;
        if (numeroFormato) {
            fecha = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            fechaFinal = fecha.format(DateTimeFormatter.ofPattern(formatoFinal, Locale.getDefault()));
        }else {
            fechaS = LocalDateTime.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            fechaFinal = fechaS.format(DateTimeFormatter.ofPattern(formatoFinal, Locale.getDefault()));
        }
        return fechaFinal;
    }
}
