package br.com.done.done.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Setter
@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "repasse", schema = "scraper")
public class Repasse implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rep_id", nullable = false)
    private long id;

    @Column(name = "rep_referencia")
    @Temporal(TemporalType.DATE)
    private Date referencia;

    @JoinColumn(name = "rep_mun_id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Municipio municipio;

}
