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
@Table(name = "destinatario", schema = "main")
public class Destinatario implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "des_id", nullable = false)
    private Long id;

    @Column(name = "des_grupo", nullable = false)
    private String grupo;

    @Column(name = "des_mail", nullable = false)
    private String email;
}
