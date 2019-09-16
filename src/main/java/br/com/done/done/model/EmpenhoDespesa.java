package br.com.done.done.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Setter
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "empenho_despesa", schema = "scraper")
public class EmpenhoDespesa implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "des_id", nullable = false)
    private long id;

    @Column(name = "des_ano", length = 4, nullable = false)
    private String ano;

    @Column(name = "des_valor", nullable = false)
    private BigDecimal valor;

    @JoinColumn(name = "des_cla_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Classificacao natureza;

    @Column(name = "des_lancamento", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date lancamento;

    @JoinColumn(name = "des_emp_id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Empenho empenho;

}
