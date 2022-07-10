package ru.arkasandr.carfinesearcher.model;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = "request")
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

    @OneToOne(mappedBy = "car", cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private GibddRequest request;

    @Override
    public String toString() {
        return "Car{"
                + "id=" + id
                + ", registrationNumber='" + registrationNumber + '\''
                + ", certificateNumber='" + certificateNumber + '\''
                + '}';
    }
}
