package com.mycompany.myapp.domain.DTO;

import java.time.LocalDate;

/**
 * Created by Alan on 19/01/2017.
 */
public class EvolutionjDTO {

    private LocalDate time;
    private Long count;

    public EvolutionjDTO() {}

    public EvolutionjDTO(LocalDate time, Long count) {
        this.time = time;
        this.count = count;
    }

    public LocalDate getTime() {
        return time;
    }

    public void setTime(LocalDate time) {
        this.time = time;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}


//List <EvolutionPlayer>
//EvolutionPlayer: Jugador, List<EvolutionjDTO>
