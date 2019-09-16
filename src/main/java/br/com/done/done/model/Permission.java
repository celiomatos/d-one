package br.com.done.done.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "oauth_permission", schema = "oauth")
public class Permission implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "perm_id", nullable = false)
    private Long id;

    @Column(name = "perm_name", nullable = false)
    private String name;
}
