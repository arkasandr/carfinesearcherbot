package ru.arkasandr.carfinesearcher.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "FINE")
public class Fine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double value;

    private LocalDateTime receiptDate;

    private String name;

    private String violationDateAndTime;

    private String koapPoint;

    private String gibddDepartment;

    private String gibddResolution;

    private String requiredAmount;

    @ManyToOne
    @JoinColumn(name = "car_id")
    private Car car;

    @Override
    public String toString() {
        return "Fine{"
                + "id=" + id
                + ", value=" + value
                + ", receiptDate=" + receiptDate
                + '}';
    }
}
