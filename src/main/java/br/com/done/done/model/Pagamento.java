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
@Table(name = "pagamento", schema = "scraper")
public class Pagamento implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pag_id", nullable = false)
    private long id;

    @Column(name = "pag_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date data;

    @Column(name = "pag_nr_ob", length = 15)
    private String nrOb;

    @Column(name = "pag_nr_nl", length = 15)
    private String nrNl;

    @Column(name = "pag_nr_ne", length = 15)
    private String nrNe;

    @Column(name = "pag_valor", nullable = false)
    private BigDecimal valor;

    @Column(name = "pag_lancamento", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date lancamento;

    @Column(name = "pag_removido", nullable = false)
    private boolean removido;

    @JoinColumn(name = "pag_cla_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Classificacao classificacao;

    @JoinColumn(name = "pag_cre_id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Credor credor;

    @JoinColumn(name = "pag_fon_id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Fonte fonte;

    @JoinColumn(name = "pag_org_id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Orgao orgao;

}
