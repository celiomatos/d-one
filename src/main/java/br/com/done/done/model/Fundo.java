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
@Table(name = "fundo", schema = "scraper")
public class Fundo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fun_id", nullable = false)
    private long id;

    @Column(name = "fun_nome", nullable = false, length = 150)
    private String nome;

    @Column(name = "fun_sigla", nullable = false, length = 30)
    private String sigla;

    public Fundo(long id) {
        this.id = id;
    }
}
