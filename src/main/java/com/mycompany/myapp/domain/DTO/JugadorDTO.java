package com.mycompany.myapp.domain.DTO;

import com.mycompany.myapp.domain.Player;

/**
 * Created by Alan on 20/12/2016.
 */
public class JugadorDTO {

    private Player player;
    private Long count;

    public JugadorDTO(Player player, Long count) {
        this.player = player;
        this.count = count;
    }

    public JugadorDTO() {}

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "JugadorDTO{" +
            "player=" + player +
            ", count=" + count +
            '}';
    }
}
