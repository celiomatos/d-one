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
@Table(name = "repasse_icms", schema = "scraper")
public class RepasseIcms implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "icm_id", nullable = false)
    private long id;

    @Column(name = "icm_dtarrecadacao_inicial", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date dtarrecadacaoInicial;

    @Column(name = "icm_dtarrecadacao_final", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date dtarrecadacaoFinal;

    @Column(name = "icm_dtcredito", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date dtcredito;

    @Column(name = "icm_valor_repasse_icms", nullable = false)
    private BigDecimal valorRepasseIcms;

    @Column(name = "icm_dtlancamento", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtlancamento;

    @Column(name = "icm_removido", nullable = false)
    private boolean removido;

    @JoinColumn(name = "icm_rep_id")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Repasse repasse;
}
