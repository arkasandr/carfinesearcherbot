package ru.arkasandr.carfinesearcher.model;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "CAR")
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @ToString.Include
    private Long id;

    @ToString.Include
    private String registrationNumber;

    @ToString.Include
    private String certificateNumber;

    @ManyToOne
    @JoinColumn(name = "chat_id")
    private Chat chat;

    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL)
    private Set<Fine> carFines;

    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL)
    private Set<GibddRequest> request;
}
