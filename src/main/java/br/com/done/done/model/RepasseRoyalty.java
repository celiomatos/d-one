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
@Table(name = "repasse_royalty", schema = "scraper")
public class RepasseRoyalty implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "roy_id", nullable = false)
    private long id;

    @Column(name = "roy_royalty", nullable = false, length = 45)
    private String royalty;

    @Column(name = "roy_dtcredito", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date dtcredito;

    @Column(name = "roy_valor_bruto", nullable = false)
    private BigDecimal valorBruto;

    @Column(name = "roy_pasep", nullable = false)
    private BigDecimal pasep;

    @Column(name = "roy_dtlancamento", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtlancamento;

    @Column(name = "roy_removido", nullable = false)
    private boolean removido;

    @JoinColumn(name = "roy_rep_id")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Repasse repasse;
}
