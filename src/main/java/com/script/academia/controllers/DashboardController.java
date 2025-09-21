package com.script.academia.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.script.academia.security.UsuarioDetalhes;

@Controller
public class DashboardController {
	
	// Obtém o objeto de autenticação do usuário atualmente logado no sistema.
	// Isso permite acessar informações como nome, email e perfil do usuário.

	private void adicionarNomeUsuario(Model model) {
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			UsuarioDetalhes detalhes = (UsuarioDetalhes) auth.getPrincipal();
			model.addAttribute("nome", detalhes.getUsuario().getNome());
		} catch (Exception e) {
			model.addAttribute("nome", "Usuário");
		}
	}

	@GetMapping("/")
	public String redirecionarParaDashboardPrincipal(Model model) {
		adicionarNomeUsuario(model); 
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (auth.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"))) {
			return "dashboard/admin";
		} else if (auth.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("ROLE_PROFESSOR"))) {
			return "dashboard/professor";
		} else {
			return "dashboard/aluno";
		}
	}

	@GetMapping("/configuracoes")
	public String configuracoes(Model model) {
		adicionarNomeUsuario(model);
		return "usuario/configuracoesUsuario";
	}

	@GetMapping("/controleAluno")
	public String controleAluno(Model model) {
		adicionarNomeUsuario(model);
		return "controleAluno";
	}

	@GetMapping("/controleDeFicha")
	public String controleDeFicha(Model model) {
		adicionarNomeUsuario(model);
		return "controleDeFicha";
	}

	@GetMapping("/controleAvaliacaoFisica")
	public String controleDeAvaliacaoFisica(Model model) {
		adicionarNomeUsuario(model);
		return "controleAvaliacaoFisica";
	}
	
	@GetMapping("/controleUsuario")
	public String controleUsuario(Model model) {
		adicionarNomeUsuario(model);
		return "controleUsuario";
	}

	@GetMapping("/suporte")
	public String suporte(Model model) {
		adicionarNomeUsuario(model);
		return "suporte";
	}

	@GetMapping("/atualizacoes")
	public String atualizacoes(Model model) {
		adicionarNomeUsuario(model);
		return "atualizacoes";
	}

	@GetMapping("/dashboard")
	public String redirecionarDashboardPorURL(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UsuarioDetalhes detalhes = (UsuarioDetalhes) auth.getPrincipal();
		model.addAttribute("nome", detalhes.getUsuario().getNome());

		if (auth.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"))) {
			return "dashboard/admin";
		} else if (auth.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("ROLE_PROFESSOR"))) {
			return "dashboard/professor";
		} else if (auth.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("ROLE_ALUNO"))) {
			return "dashboard/aluno";
		} else {
			return "redirect:/login";
		}
	}

	
}