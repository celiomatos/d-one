package br.com.done.done.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Setter
@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "calha", schema = "main")
public class Calha implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cal_id", nullable = false)
    private long id;

    @Column(name = "cal_nome", nullable = false, length = 60)
    private String nome;
}
