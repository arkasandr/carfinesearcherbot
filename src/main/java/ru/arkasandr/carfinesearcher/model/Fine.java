package ru.arkasandr.carfinesearcher.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@Table(name = "FINE")
public class Fine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @ToString.Include
    private Long id;

    @ToString.Include
    private Double value;

    @ToString.Include
    private LocalDateTime receiptDate;

    @ToString.Include
    private String name;

    @ToString.Include
    private String violationDateAndTime;

    @ToString.Include
    private String koapPoint;

    @ToString.Include
    private String gibddDepartment;

    @ToString.Include
    private String gibddResolution;

    @ToString.Include
    private String requiredAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_id")
    private Car car;

}
