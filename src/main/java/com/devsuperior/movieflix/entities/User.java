package com.devsuperior.movieflix.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "tb_user")
public class User implements UserDetails, Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private String name;
	
	@Column(unique = true)
	private String email;
	private String password;
	
	@ManyToMany(fetch = FetchType.EAGER)  //Exigência do Spring Security, quando for fazer autenticação de usuário.
	@JoinTable(name = "tb_user_role",
		joinColumns = @JoinColumn(name = "user_id"),
		inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<>();
	
	@OneToMany(mappedBy = "user")
	private List<Review> reviews = new ArrayList<>();
	
	public User() {
	}

	public User(Long id, String name, String email, String password) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public List<Review> getReviews() {
		return reviews;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		return Objects.equals(id, other.id);
	}
	
	// Os metodos abaixo são de implementação obrigatória por causa da interface UserDetails do Spring Security.

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		
		// Este metodo percorre a lista de roles através de um metodo lambda para converter cada elemento role da lista para um GrantedAuthority.
		return roles.stream().map(role -> new SimpleGrantedAuthority(role.getAuthority())).collect(Collectors.toList());
	}

	@Override
	public String getUsername() {
		
		return email;
	}

	// Metodo para verificar se a conta do usuário não está EXPIRADA. 
	// Por padrão colocamos true para nunca estar expirada, caso contrário deverá ser implementada uma lógica para verificar se a conta está expirada.
	@Override
	public boolean isAccountNonExpired() {
		
		return true;
	}

	// Metodo para verificar se a conta do usuário não está BLOQUEADA. 
	// Por padrão colocamos true para nunca estar bloqueada, caso contrário deverá ser implementada uma lógica para verificar se a conta está bloqueada.
	@Override
	public boolean isAccountNonLocked() {
		
		return true;
	}

	// Metodo para verificar se as credenciais do usuário não estão EXPIRADAS. 
	// Por padrão colocamos true para nunca estarem expiradas, caso contrário deverá ser implementada uma lógica para verificar se as credenciais estão expiradas.
		
	@Override
	public boolean isCredentialsNonExpired() {
		
		return true;
	}

	// Metodo para verificar se a conta do usuário está HABILITADA. 
	// Por padrão colocamos true para deixar semprer habilitada, caso contrário deverá ser implementada uma lógica para verificar se a conta está desabilitada.
	@Override
	public boolean isEnabled() {
		
		return true;
	}
	
	public boolean hasRole(String roleName) {
		for(Role role : roles) {
			if(role.getAuthority().equals(roleName)) {
				return true;
			}
		}
		return false;
	}
}
