package ru.arkasandr.carfinesearcher.model;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import ru.arkasandr.carfinesearcher.model.enums.RequestStatus;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@ToString(exclude = "car")
@EqualsAndHashCode(exclude = "car")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "gibdd_request")
public class GibddRequest {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;

    private LocalDateTime requestDate;

    private LocalDateTime responseDate;

    private LocalDateTime createDate;

    @Lob
    private byte[] captchaPic;

    private Long captchaCode;

    @Basic
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private RequestStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_id")
    private Car car;

}
