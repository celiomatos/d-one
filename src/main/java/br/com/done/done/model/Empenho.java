package br.com.done.done.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Setter
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "empenho", schema = "scraper")
public class Empenho implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "emp_id", nullable = false)
    private long id;

    @Column(name = "emp_nota", length = 11, nullable = false)
    private String nota;

    @Column(name = "emp_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date data;

    @Column(name = "emp_tipo" ,length = 255)
    private String tipo;

    @Column(name = "emp_programa", length = 255)
    private String programa;

    @Column(name = "emp_funcao", length = 255)
    private String funcao;

    @Column(name = "emp_sub_funcao", length = 255)
    private String subFuncao;

    @Column(name = "emp_licitacao", length = 255)
    private String licitacao;

    @Column(name = "emp_referencia", length = 255)
    private String referencia;

    @Column(name = "emp_processo", length = 255)
    private String processo;

    @Column(name = "emp_descricao", length = 100000)
    private String descricao;

    @Column(name = "emp_lancamento", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date lancamento;

    @JoinColumn(name = "emp_cre_id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Credor credor;

    @JoinColumn(name = "emp_fon_id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Fonte fonte;

    @JoinColumn(name = "emp_org_id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Orgao orgao;
}
