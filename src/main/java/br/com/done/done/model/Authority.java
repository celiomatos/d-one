package br.com.done.done.model;

import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Setter
@Entity
@Table(name = "oauth_authority", schema = "oauth")
public class Authority implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "authority_id", nullable = false)
    private Long authorityId;

    @JoinColumn(name = "mod_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Module module;

    @JoinColumn(name = "perm_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Permission permission;

    @Override
    public String getAuthority() {
        return module.getName() + "_" + permission.getName();
    }

}
