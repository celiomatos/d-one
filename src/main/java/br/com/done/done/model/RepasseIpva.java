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
@Table(name = "repasse_ipva", schema = "scraper")
public class RepasseIpva implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ipv_id", nullable = false)
    private long id;

    @Column(name = "ipv_dtarrecadacao_inicial", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date dtarrecadacaoInicial;

    @Column(name = "ipv_dtarrecadacao_final", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date dtarrecadacaoFinal;

    @Column(name = "ipv_valor_repasse_ipva", nullable = false)
    private BigDecimal valorRepasseIpva;

    @Column(name = "ipv_dtlancamento", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtlancamento;

    @Column(name = "ipv_removido", nullable = false)
    private boolean removido;

    @JoinColumn(name = "ipv_rep_id")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Repasse repasse;

}
