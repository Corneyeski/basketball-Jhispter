package com.mycompany.myapp.domain.DTO;

import com.mycompany.myapp.domain.Player;

import java.util.List;

/**
 * Created by Alan on 20/01/2017.
 */
public class EvolutionGeneraljDTO {

    private Player player;
    private List<EvolutionjDTO> evolutionGeneral;

    public EvolutionGeneraljDTO() {}

    public EvolutionGeneraljDTO(Player player, List<EvolutionjDTO> evolutionGeneral) {
        this.player = player;
        this.evolutionGeneral = evolutionGeneral;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public List<EvolutionjDTO> getEvolutionGeneral() {
        return evolutionGeneral;
    }

    public void setEvolutionGeneral(List<EvolutionjDTO> evolutionGeneral) {
        this.evolutionGeneral = evolutionGeneral;
    }

    @Override
    public String toString() {
        return "EvolutionGeneraljDTO{" +
            "player=" + player +
            ", evolutionGeneral=" + evolutionGeneral +
            '}';
    }
}
