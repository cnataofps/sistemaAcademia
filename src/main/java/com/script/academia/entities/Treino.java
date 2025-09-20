package com.script.academia.entities;

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
public class Treino {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    @ManyToOne
    @JoinColumn(name = "ficha_id")
    private FichaDeTreinamento ficha;
    @OneToMany(mappedBy = "treino", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Exercicios> exercicios;
    @ManyToOne
    @JoinColumn(name = "aluno_id")
    private Aluno aluno;
    

    public Treino() {
		
	}


	public Treino(Long id, String nome, FichaDeTreinamento ficha, List<Exercicios> exercicios, Aluno aluno) {
		super();
		this.id = id;
		this.nome = nome;
		this.ficha = ficha;
		this.exercicios = exercicios;
		this.aluno = aluno;
	}


	@Override
	public String toString() {
		return "Treino [id=" + id + ", nome=" + nome + ", ficha=" + ficha + ", exercicios=" + exercicios + ", aluno="
				+ aluno + "]";
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getNome() {
		return nome;
	}


	public void setNome(String nome) {
		this.nome = nome;
	}


	public FichaDeTreinamento getFicha() {
		return ficha;
	}


	public void setFicha(FichaDeTreinamento ficha) {
		this.ficha = ficha;
	}


	public List<Exercicios> getExercicios() {
		return exercicios;
	}


	public void setExercicios(List<Exercicios> exercicios) {
		this.exercicios = exercicios;
	}


	public Aluno getAluno() {
		return aluno;
	}


	public void setAluno(Aluno aluno) {
		this.aluno = aluno;
	}


	
    
    
}
