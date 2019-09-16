package br.com.done.done.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "oauth_module", schema = "oauth")
public class Module implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mod_id", nullable = false)
    private Long id;

    @Column(name = "mod_name", nullable = false)
    private String name;

}
