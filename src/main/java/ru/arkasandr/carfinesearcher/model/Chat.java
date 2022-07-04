package ru.arkasandr.carfinesearcher.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "CHAT")
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long chatId;

    private String firstName;

    private String lastName;

    private String userName;

    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL)
    Set<Car> chatCars;


}
