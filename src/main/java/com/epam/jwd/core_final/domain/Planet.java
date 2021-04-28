package com.epam.jwd.core_final.domain;

import java.util.Objects;

/**
 * Expected fields:
 * <p>
 * location could be a simple class Point with 2 coordinates
 */
public class Planet extends AbstractBaseEntity {
    private final Point location;

    public Planet(String name, Point location) {
        super(name);
        this.location = location;
    }

    public Planet(Long id, String name, Point location) {
        super(id, name);
        this.location = location;
    }

    public Point getLocation() {
        return location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Planet planet = (Planet) o;

        return Objects.equals(location, planet.location);
    }

    @Override
    public int hashCode() {
        return location != null ? location.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Planet{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", location=" + location +
                '}';
    }
}
