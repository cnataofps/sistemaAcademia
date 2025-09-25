package com.script.academia.controllers;

import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.script.academia.entities.MensagemSuporte;
import com.script.academia.entities.Usuario;
import com.script.academia.repository.MensagemSuporteRepository;
import com.script.academia.security.UsuarioDetalhes;

@Controller
public class SuporteController {

	@Autowired
	private MensagemSuporteRepository suporteRepository;

	// Método auxiliar para adicionar o nome do usuário logado ao modelo

	private void adicionarNomeUsuario(Model model) {
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			UsuarioDetalhes detalhes = (UsuarioDetalhes) auth.getPrincipal();
			model.addAttribute("nome", detalhes.getUsuario().getNome());
		} catch (Exception e) {
			model.addAttribute("nome", "Usuário");
		}
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/listarSuporte")
	public String listarMensagensDeSuporte(Model model) {
		adicionarNomeUsuario(model);
		model.addAttribute("mensagens", suporteRepository.findAll());
		return "suporte/listarMensagens";
	}

	@PreAuthorize("hasAnyRole('PROFESSOR', 'ADMIN', 'ALUNO')")
	@PostMapping("/suporte/enviar")
	public String enviarSuporte(@RequestParam("assunto") String assunto, @RequestParam("mensagem") String mensagem,
			RedirectAttributes attributes) {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UsuarioDetalhes detalhes = (UsuarioDetalhes) auth.getPrincipal();
		Usuario usuarioLogado = detalhes.getUsuario();

		MensagemSuporte novaMensagem = new MensagemSuporte();
		novaMensagem.setAssunto(assunto);
		novaMensagem.setMensagem(mensagem);
		novaMensagem.setDataEnvio(LocalDateTime.now());
		novaMensagem.setUsuario(usuarioLogado);

		suporteRepository.save(novaMensagem);
		attributes.addFlashAttribute("Mensagem", "Sua mensagem foi enviada com sucesso!");
		return "redirect:/suporte";
	}
}
