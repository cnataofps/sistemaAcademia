package com.script.academia.entities;

import java.time.LocalDate;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;




@Entity
public class Aluno {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String matricula;
	@NotEmpty
	private String nome;
	private String cpf;
	private LocalDate dataNasc;
	private String profissao;
	private String endereco;
	@Column(unique = true)
	@NotEmpty
	private String email;
	@OneToOne
	@JoinColumn(name = "usuario_id")
	private Usuario usuario;
	

	
		
	// CONSTRUTOR VAZIO
	public Aluno() {
		
	}




	public Aluno(Long id, @NotEmpty String matricula, @NotEmpty String nome, @NotEmpty String cpf,
			@NotNull LocalDate dataNasc, @NotNull String profissao, @NotEmpty String endereco, @NotEmpty String email,
			Usuario usuario) {
		super();
		this.id = id;
		this.matricula = matricula;
		this.nome = nome;
		this.cpf = cpf;
		this.dataNasc = dataNasc;
		this.profissao = profissao;
		this.endereco = endereco;
		this.email = email;
		this.usuario = usuario;
	}




	@Override
	public String toString() {
		return "Aluno [id=" + id + ", matricula=" + matricula + ", nome=" + nome + ", cpf=" + cpf + ", dataNasc="
				+ dataNasc + ", profissao=" + profissao + ", endereco=" + endereco + ", email=" + email + ", usuario="
				+ usuario + "]";
	}




	public Long getId() {
		return id;
	}




	public void setId(Long id) {
		this.id = id;
	}




	public String getMatricula() {
		return matricula;
	}




	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}




	public String getNome() {
		return nome;
	}




	public void setNome(String nome) {
		this.nome = nome;
	}




	public String getCpf() {
		return cpf;
	}




	public void setCpf(String cpf) {
		this.cpf = cpf;
	}




	public LocalDate getDataNasc() {
		return dataNasc;
	}




	public void setDataNasc(LocalDate dataNasc) {
		this.dataNasc = dataNasc;
	}




	public String getProfissao() {
		return profissao;
	}




	public void setProfissao(String profissao) {
		this.profissao = profissao;
	}




	public String getEndereco() {
		return endereco;
	}




	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}




	public String getEmail() {
		return email;
	}




	public void setEmail(String email) {
		this.email = email;
	}




	public Usuario getUsuario() {
		return usuario;
	}




	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}




	

}