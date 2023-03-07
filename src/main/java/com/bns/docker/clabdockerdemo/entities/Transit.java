package com.bns.docker.clabdockerdemo.entities;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Objects;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Transit {

    @Id
    @GeneratedValue
    private Long id;
    private String transitNumber;
    private String transitType;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transit transit = (Transit) o;
        return Objects.equals(id, transit.id) && Objects.equals(transitNumber, transit.transitNumber) && Objects.equals(transitType, transit.transitType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, transitNumber, transitType);
    }
}
