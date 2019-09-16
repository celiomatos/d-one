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
@Table(name = "empenho_original", schema = "scraper")
public class EmpenhoOriginal implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ori_id", nullable = false)
    private long id;

    @Column(name = "ori_evento", length = 15, nullable = false)
    private String evento;

    @Column(name = "ori_valor")
    private BigDecimal valor;

    @Column(name = "ori_descricao", length = 100000)
    private String descricao;

    @Column(name = "ori_lancamento", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date lancamento;

    @JoinColumn(name = "ori_emp_ref_id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Empenho reforco;

    @JoinColumn(name = "ori_emp_ori_id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Empenho original;

}
