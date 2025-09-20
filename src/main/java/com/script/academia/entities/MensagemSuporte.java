package com.script.academia.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class MensagemSuporte {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String assunto;
	@Column(columnDefinition = "TEXT")
	private String mensagem;
	private LocalDateTime dataEnvio;
	@ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
	

	public MensagemSuporte() {
		
	}


	public MensagemSuporte(Long id, String assunto, String mensagem, LocalDateTime dataEnvio, Usuario usuario) {
		super();
		this.id = id;
		this.assunto = assunto;
		this.mensagem = mensagem;
		this.dataEnvio = dataEnvio;
		this.usuario = usuario;
	}


	@Override
	public String toString() {
		return "MensagemSuporte [id=" + id + ", assunto=" + assunto + ", mensagem=" + mensagem + ", dataEnvio="
				+ dataEnvio + ", usuario=" + usuario + "]";
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getAssunto() {
		return assunto;
	}


	public void setAssunto(String assunto) {
		this.assunto = assunto;
	}


	public String getMensagem() {
		return mensagem;
	}


	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}


	public LocalDateTime getDataEnvio() {
		return dataEnvio;
	}


	public void setDataEnvio(LocalDateTime dataEnvio) {
		this.dataEnvio = dataEnvio;
	}


	public Usuario getUsuario() {
		return usuario;
	}


	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	
	
	
	

}