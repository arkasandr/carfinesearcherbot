package ru.arkasandr.carfinesearcher.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "CAR")
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String registrationNumber;

    private String certificateNumber;

    @ManyToOne
    @JoinColumn(name = "chat_id")
    private Chat chat;

    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL)
    Set<Fine> carFines;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gibdd_request_id")
    private GibddRequest request;

}
