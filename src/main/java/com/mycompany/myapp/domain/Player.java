package com.mycompany.myapp.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import com.mycompany.myapp.domain.enumeration.Position;

/**
 * A Player.
 */
@Entity
@Table(name = "player")
public class Player implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "birthdate")
    private LocalDate birthdate;

    @Column(name = "nbaskets")
    private Integer nbaskets;

    @Column(name = "nassists")
    private Integer nassists;

    @Column(name = "nrebots")
    private Integer nrebots;

    @Enumerated(EnumType.STRING)
    @Column(name = "pos")
    private Position pos;

    @ManyToOne
    private Team team;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Player name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public Player surname(String surname) {
        this.surname = surname;
        return this;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public Player birthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
        return this;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public Integer getNbaskets() {
        return nbaskets;
    }

    public Player nbaskets(Integer nbaskets) {
        this.nbaskets = nbaskets;
        return this;
    }

    public void setNbaskets(Integer nbaskets) {
        this.nbaskets = nbaskets;
    }

    public Integer getNassists() {
        return nassists;
    }

    public Player nassists(Integer nassists) {
        this.nassists = nassists;
        return this;
    }

    public void setNassists(Integer nassists) {
        this.nassists = nassists;
    }

    public Integer getNrebots() {
        return nrebots;
    }

    public Player nrebots(Integer nrebots) {
        this.nrebots = nrebots;
        return this;
    }

    public void setNrebots(Integer nrebots) {
        this.nrebots = nrebots;
    }

    public Position getPos() {
        return pos;
    }

    public Player pos(Position pos) {
        this.pos = pos;
        return this;
    }

    public void setPos(Position pos) {
        this.pos = pos;
    }

    public Team getTeam() {
        return team;
    }

    public Player team(Team team) {
        this.team = team;
        return this;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Player player = (Player) o;
        if (player.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, player.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Player{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", surname='" + surname + "'" +
            ", birthdate='" + birthdate + "'" +
            ", nbaskets='" + nbaskets + "'" +
            ", nassists='" + nassists + "'" +
            ", nrebots='" + nrebots + "'" +
            ", pos='" + pos + "'" +
            '}';
    }
}
