package br.com.done.done.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Setter
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "orgao", schema = "scraper")
public class Orgao implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "org_id", nullable = false)
    private long id;

    @Column(name = "org_codigo", length = 6)
    private String codigo;

    @Column(name = "org_sigla", length = 30)
    private String sigla;

    @Column(name = "org_orgao", length = 150, nullable = false)
    private String nome;

    @Column(name = "org_nome", length = 150)
    private String orgName;

    @Column(name = "org_esfera", nullable = false)
    private short esfera;
}
