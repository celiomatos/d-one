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
@Table(name = "classificacao", schema = "scraper")
public class Classificacao implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cla_id", nullable = false)
    private long id;

    @Column(name = "cla_codigo", length = 10)
    private String codigo;

    @Column(name = "cla_nome", length = 100)
    private String nome;

    public Classificacao(Long id) {
        this.id = id;
    }
}
