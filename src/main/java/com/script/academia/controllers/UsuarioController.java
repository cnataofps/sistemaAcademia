package com.script.academia.controllers;

import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.script.academia.entities.Aluno;
import com.script.academia.entities.Perfil;
import com.script.academia.entities.Usuario;
import com.script.academia.repository.AlunoRepository;
import com.script.academia.repository.UsuarioRepository;
import com.script.academia.security.UsuarioDetalhes;

@Controller
public class UsuarioController {

	@Autowired
	private AlunoRepository alunoRepository;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	private void adicionarNomeUsuario(Model model) {
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			UsuarioDetalhes detalhes = (UsuarioDetalhes) auth.getPrincipal();
			model.addAttribute("nome", detalhes.getUsuario().getNome());
		} catch (Exception e) {
			model.addAttribute("nome", "Usuário");
		}
	}

	@GetMapping("/cadastrarUsuario")
	public String cadastro() {
		return "login/cadastro";
	}

	@PostMapping("/cadastrarUsuario")
	public String cadastrar(@ModelAttribute Usuario usuario, @RequestParam("perfil") String perfilStr) {
		if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
			return "redirect:/cadastrarUsuario?erro=email";
		}

		usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
		usuario.setPerfil(Perfil.valueOf(perfilStr));
		usuarioRepository.save(usuario);

		return "redirect:/login?cadastro=sucesso";
	}

	@PostMapping("/usuario/alterar")
	public String alterarDados(
			@RequestParam String nome,
			@RequestParam String email,
			@RequestParam(required = false) String endereco,
			@RequestParam(required = false) String profissao,
			@RequestParam(required = false) String novaSenha,
			@RequestParam(required = false) String confirmarSenha,
			@AuthenticationPrincipal UsuarioDetalhes usuarioDetalhes, RedirectAttributes redirectAttributes) {

		try {
			if (usuarioDetalhes == null || usuarioDetalhes.getUsuario() == null) {
				redirectAttributes.addFlashAttribute("erro", "Usuário não autenticado.");
				return "redirect:/alterar";
			}

			Usuario usuario = usuarioDetalhes.getUsuario();

			// Verifica se o novo e-mail já está em uso por outro usuário

			Optional<Usuario> existente = usuarioRepository.findByEmail(email);
			if (existente.isPresent() && !Objects.equals(existente.get().getId(), usuario.getId())) {
				redirectAttributes.addFlashAttribute("erro", "E-mail já está em uso por outro usuário.");
				return "redirect:/alterar";
			}

			// Atualiza apenas os campos editáveis

			usuario.setNome(nome);
			usuario.setEmail(email);

			if (novaSenha != null && !novaSenha.isBlank()) {
				if (!novaSenha.equals(confirmarSenha)) {
					redirectAttributes.addFlashAttribute("erro", "As senhas não coincidem.");
					return "redirect:/alterar";
				}
				usuario.setSenha(passwordEncoder.encode(novaSenha));
			}

			usuarioRepository.save(usuario);

			// Busca o aluno vinculado e atualiza apenas os campos editáveis

			if (usuario.getPerfil() == Perfil.ALUNO) {
				Aluno aluno = alunoRepository.findByUsuario(usuario).orElseThrow();
				aluno.setNome(nome);
				aluno.setEmail(email);
				aluno.setEndereco(endereco);
				aluno.setProfissao(profissao);
				alunoRepository.save(aluno);
			}

			redirectAttributes.addFlashAttribute("sucesso", "Dados atualizados com sucesso!");
			return "redirect:/alterarDados";

		} catch (Exception e) {
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("erro", "Erro inesperado: " + e.getMessage());
			return "redirect:/alterar";
		}
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/listarUsuario")
	public String listarUsuarios(org.springframework.ui.Model model) {
		adicionarNomeUsuario(model);
		model.addAttribute("mensagens", usuarioRepository.findAll());
		return "usuario/listarUsuario";
	}

	@GetMapping("/alterarDados")
	public String exibirFormularioAlteracao(Model model, @AuthenticationPrincipal UsuarioDetalhes usuarioDetalhes) {
		adicionarNomeUsuario(model);

		if (usuarioDetalhes == null || usuarioDetalhes.getUsuario() == null) {
			model.addAttribute("erro", "Usuário não autenticado.");
			return "redirect:/login";
		}

		Usuario usuario = usuarioDetalhes.getUsuario();
		model.addAttribute("usuario", usuario);

		if (usuario.getPerfil() == Perfil.PROFESSOR) {
			model.addAttribute("tipo", "professor");
			return "usuario/alterarProfessor";
		} else {
			Aluno aluno = alunoRepository.findByUsuario(usuario).orElse(null);
			model.addAttribute("aluno", aluno);
			model.addAttribute("tipo", "aluno");
			return "usuario/alterarAluno";
		}
	}

}
