package com.mycompany.myapp.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Game.
 */
@Entity
@Table(name = "game")
public class Game implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "localp")
    private Integer localp;

    @Column(name = "localv")
    private Integer localv;

    @Column(name = "times")
    private ZonedDateTime times;

    @Column(name = "timef")
    private ZonedDateTime timef;

    @ManyToOne
    private Team gameLocalTeam;

    @ManyToOne
    private Team gameVisitorTeam;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getLocalp() {
        return localp;
    }

    public Game localp(Integer localp) {
        this.localp = localp;
        return this;
    }

    public void setLocalp(Integer localp) {
        this.localp = localp;
    }

    public Integer getLocalv() {
        return localv;
    }

    public Game localv(Integer localv) {
        this.localv = localv;
        return this;
    }

    public void setLocalv(Integer localv) {
        this.localv = localv;
    }

    public ZonedDateTime getTimes() {
        return times;
    }

    public Game times(ZonedDateTime times) {
        this.times = times;
        return this;
    }

    public void setTimes(ZonedDateTime times) {
        this.times = times;
    }

    public ZonedDateTime getTimef() {
        return timef;
    }

    public Game timef(ZonedDateTime timef) {
        this.timef = timef;
        return this;
    }

    public void setTimef(ZonedDateTime timef) {
        this.timef = timef;
    }

    public Team getGameLocalTeam() {
        return gameLocalTeam;
    }

    public Game gameLocalTeam(Team team) {
        this.gameLocalTeam = team;
        return this;
    }

    public void setGameLocalTeam(Team team) {
        this.gameLocalTeam = team;
    }

    public Team getGameVisitorTeam() {
        return gameVisitorTeam;
    }

    public Game gameVisitorTeam(Team team) {
        this.gameVisitorTeam = team;
        return this;
    }

    public void setGameVisitorTeam(Team team) {
        this.gameVisitorTeam = team;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Game game = (Game) o;
        if (game.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, game.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Game{" +
            "id=" + id +
            ", localp='" + localp + "'" +
            ", localv='" + localv + "'" +
            ", times='" + times + "'" +
            ", timef='" + timef + "'" +
            '}';
    }
}
