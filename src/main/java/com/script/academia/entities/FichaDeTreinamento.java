package com.script.academia.entities;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;




@Entity
public class FichaDeTreinamento {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String objetivo;
    private LocalDate dataCriacao;
    private LocalDate dataTroca;
    @ManyToOne
    @JoinColumn(name = "aluno_id")
    private Aluno aluno;
    @OneToMany(mappedBy = "ficha", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Treino> treinos;

    	
	
	// CONSTRUTOR VAZIO.
	public FichaDeTreinamento() {
		
	}



	public FichaDeTreinamento(Long id, String objetivo, LocalDate dataCriacao, LocalDate dataTroca, Aluno aluno,
			List<Treino> treinos) {
		super();
		this.id = id;
		this.objetivo = objetivo;
		this.dataCriacao = dataCriacao;
		this.dataTroca = dataTroca;
		this.aluno = aluno;
		this.treinos = treinos;
	}



	@Override
	public String toString() {
		return "FichaDeTreinamento [id=" + id + ", objetivo=" + objetivo + ", dataCriacao=" + dataCriacao
				+ ", dataTroca=" + dataTroca + ", aluno=" + aluno + "]";
	}



	public Long getId() {
		return id;
	}



	public void setId(Long id) {
		this.id = id;
	}



	public String getObjetivo() {
		return objetivo;
	}



	public void setObjetivo(String objetivo) {
		this.objetivo = objetivo;
	}



	public LocalDate getDataCriacao() {
		return dataCriacao;
	}



	public void setDataCriacao(LocalDate dataCriacao) {
		this.dataCriacao = dataCriacao;
	}



	public LocalDate getDataTroca() {
		return dataTroca;
	}



	public void setDataTroca(LocalDate dataTroca) {
		this.dataTroca = dataTroca;
	}



	public Aluno getAluno() {
		return aluno;
	}



	public void setAluno(Aluno aluno) {
		this.aluno = aluno;
	}



	public List<Treino> getTreinos() {
		return treinos;
	}



	public void setTreinos(List<Treino> treinos) {
		this.treinos = treinos;
	}



	
	

}
