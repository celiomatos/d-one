package br.com.done.done.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Setter
@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "repasse_federal", schema = "scraper")
public class RepasseFederal implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ref_id", nullable = false)
    private long id;

    @Column(name = "ref_dia", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date dia;

    @Column(name = "ref_parcela", nullable = false, length = 150)
    private String parcela;

    @Column(name = "ref_valor", nullable = false)
    private BigDecimal valor;

    @Column(name = "ref_tipo", nullable = false, length = 1)
    private String tipo;

    @JoinColumn(name = "ref_fun_id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Fundo fundo;

    @JoinColumn(name = "ref_mun_id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Municipio municipio;
}
