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
@Table(name = "repasse_ipi", schema = "scraper")
public class RepasseIpi implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ipi_id", nullable = false)
    private long id;

    @Column(name = "ipi_ipi", nullable = false, length = 100)
    private String ipi;

    @Column(name = "ipi_dtcredito", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date dtcredito;

    @Column(name = "ipi_valor_bruto", nullable = false)
    private BigDecimal valorBruto;

    @Column(name = "ipi_fundo", nullable = false)
    private BigDecimal fundo;

    @Column(name = "ipi_pasep", nullable = false)
    private BigDecimal pasep;

    @Column(name = "ipi_dtlancamento", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtlancamento;

    @Column(name = "ipi_removido", nullable = false)
    private boolean removido;

    @JoinColumn(name = "ipi_rep_id")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Repasse repasse;
}
