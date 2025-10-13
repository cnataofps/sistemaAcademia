package com.script.academia.services;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.script.academia.entities.MensagemSuporte;
import com.script.academia.entities.Usuario;
import com.script.academia.repository.MensagemSuporteRepository;

@Service
public class SuporteService {

	@Autowired
	private MensagemSuporteRepository suporteRepository;

	public void enviarMensagem(String assunto, String mensagem, Usuario usuario) {
		MensagemSuporte nova = new MensagemSuporte();
		nova.setAssunto(assunto);
		nova.setMensagem(mensagem);
		nova.setDataEnvio(LocalDateTime.now());
		nova.setUsuario(usuario);
		suporteRepository.save(nova);
	}

	public List<MensagemSuporte> listarTodas() {
		return suporteRepository.findAll();
	}
}
