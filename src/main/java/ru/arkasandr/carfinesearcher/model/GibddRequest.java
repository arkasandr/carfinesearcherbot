package ru.arkasandr.carfinesearcher.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.arkasandr.carfinesearcher.model.enums.RequestStatus;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "gibdd_request")
public class GibddRequest {

    @Id
    private Long id;

    private LocalDateTime requestDate;

    private LocalDateTime responseDate;

    private LocalDateTime createDate;

    @Basic
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private RequestStatus status;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private Car car;

    @Override
    public String toString() {
        return "GibddRequest{"
                + "id=" + id
                + ", requestDate=" + requestDate
                + ", responseDate=" + responseDate
                + ", createDate=" + createDate
                + ", status=" + status
                + '}';
    }
}
