package br.com.done.done.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Setter
@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "parametro", schema = "scraper")
public class Parametro implements Serializable {

    @Id
    @Column(name = "par_id", nullable = false)
    private long id;

    @Column(name = "par_descricao", nullable = false, length = 100)
    private String descricao;

    @Column(name = "par_atual", length = 2048)
    private String atual;

    @Column(name = "par_padrao", length = 2048)
    private String padrao;
}
