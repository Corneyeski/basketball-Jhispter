package com.mycompany.myapp.domain.DTO;

import com.mycompany.myapp.domain.Game;

/**
 * Created by Alan on 13/01/2017.
 */
public class GameDTO {

    private Game game;
    private Double count;

    public GameDTO(Game game, Double count) {
        this.game = game;
        this.count = count;
    }

    public GameDTO() {}

    public Game getgame() {
        return game;
    }

    public void setgame(Game game) {
        this.game = game;
    }

    public Double getCount() {
        return count;
    }

    public void setCount(Double count) {
        this.count = count;
    }
}
