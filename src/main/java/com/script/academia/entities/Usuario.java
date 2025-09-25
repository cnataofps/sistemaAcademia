package com.script.academia.entities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotEmpty;

@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotEmpty
    private String nome;

    @Column(name = "email", unique = true)
    private String email;

    @NotEmpty
    @JsonIgnore // evita que a senha apareça no JSON
    private String senha;

    @Enumerated(EnumType.STRING)
    private Perfil perfil;

    @OneToOne
    @JoinColumn(name = "aluno_id")
    private Aluno aluno;
    
    @OneToMany(mappedBy = "professor")
    @JsonIgnore
    private List<FichaDeTreinamento> fichasDeTreinamento;

    public Usuario() {}

	public Usuario(long id, @NotEmpty String nome, String email, @NotEmpty String senha, Perfil perfil, Aluno aluno,
			List<FichaDeTreinamento> fichasDeTreinamento) {
		super();
		this.id = id;
		this.nome = nome;
		this.email = email;
		this.senha = senha;
		this.perfil = perfil;
		this.aluno = aluno;
		this.fichasDeTreinamento = fichasDeTreinamento;
	}

	@Override
	public String toString() {
		return "Usuario [id=" + id + ", nome=" + nome + ", email=" + email + ", senha=" + senha + ", perfil=" + perfil
				+ ", aluno=" + aluno + ", fichasDeTreinamento=" + fichasDeTreinamento + "]";
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public Perfil getPerfil() {
		return perfil;
	}

	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}

	public Aluno getAluno() {
		return aluno;
	}

	public void setAluno(Aluno aluno) {
		this.aluno = aluno;
	}

	public List<FichaDeTreinamento> getFichasDeTreinamento() {
		return fichasDeTreinamento;
	}

	public void setFichasDeTreinamento(List<FichaDeTreinamento> fichasDeTreinamento) {
		this.fichasDeTreinamento = fichasDeTreinamento;
	}

   
}