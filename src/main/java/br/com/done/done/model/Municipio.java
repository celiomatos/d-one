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
@Table(name = "municipio", schema = "main")
public class Municipio implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mun_id", nullable = false)
    private long id;

    @Column(name = "mun_nome", nullable = false, length = 100)
    private String nome;

    @JoinColumn(name = "mun_cal_id", referencedColumnName = "cal_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Calha calha;

    @JoinColumn(name = "mun_uf_id")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Uf uf;

    public Municipio(long id) {
        this.id = id;
    }
}
