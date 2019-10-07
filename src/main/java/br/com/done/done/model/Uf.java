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
@Table(name = "uf", schema = "main")
public class Uf implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "uf_id", nullable = false)
    private long id;

    @Column(name = "uf_sigla", nullable = false, length = 2)
    private String sigla;

    @Column(name = "uf_nome", nullable = false, length = 45)
    private String nome;
}
