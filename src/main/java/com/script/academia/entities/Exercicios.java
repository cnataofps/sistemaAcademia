package com.script.academia.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;



@Entity
public class Exercicios {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nome;
    private String serie;
    private String repeticoes;
    private String carga;
    private String observacao;
    @ManyToOne
    @JoinColumn(name = "treino_id")
    private Treino treino;
   
    
    
    
    public Exercicios() {
		
	}


	public Exercicios(Long id, String nome, String serie, String repeticoes, String carga, String observacao,
			Treino treino) {
		super();
		this.id = id;
		this.nome = nome;
		this.serie = serie;
		this.repeticoes = repeticoes;
		this.carga = carga;
		this.observacao = observacao;
		this.treino = treino;
	}


	@Override
	public String toString() {
		return "Exercicios [id=" + id + ", nome=" + nome + ", serie=" + serie + ", repeticoes=" + repeticoes
				+ ", carga=" + carga + ", observacao=" + observacao + ", treino=" + treino + "]";
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


	public String getSerie() {
		return serie;
	}


	public void setSerie(String serie) {
		this.serie = serie;
	}


	public String getRepeticoes() {
		return repeticoes;
	}


	public void setRepeticoes(String repeticoes) {
		this.repeticoes = repeticoes;
	}


	public String getCarga() {
		return carga;
	}


	public void setCarga(String carga) {
		this.carga = carga;
	}


	public String getObservacao() {
		return observacao;
	}


	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}


	public Treino getTreino() {
		return treino;
	}


	public void setTreino(Treino treino) {
		this.treino = treino;
	}

}