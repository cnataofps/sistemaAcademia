package com.script.academia.controllers;

import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
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
	public String alterarDados(@RequestParam String nome, @RequestParam String email, @RequestParam String endereco,
			@RequestParam String profissao, @RequestParam(required = false) String novaSenha,
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
			
			Aluno aluno = alunoRepository.findByUsuario(usuario).orElseThrow();

			aluno.setNome(nome);
			aluno.setEmail(email);
			aluno.setEndereco(endereco);
			aluno.setProfissao(profissao);

			// ❌ NÃO atualiza CPF, matrícula ou data de nascimento — preserva os dados
			// antigos
			
			alunoRepository.save(aluno);

			redirectAttributes.addFlashAttribute("sucesso", "Dados atualizados com sucesso!");
			return "redirect:/alterarDados";

		} catch (Exception e) {
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("erro", "Erro inesperado: " + e.getMessage());
			return "redirect:/alterar";
		}
	}
}
