package com.script.academia.controllers;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.script.academia.entities.Aluno;
import com.script.academia.entities.AvaliacaoFisica;
import com.script.academia.entities.FichaDeTreinamento;
import com.script.academia.entities.Usuario;
import com.script.academia.repository.AvaliacaoFisicaRepository;
import com.script.academia.repository.FichaDeTreinamentoRepository;
import com.script.academia.repository.UsuarioRepository;
import com.script.academia.security.UsuarioDetalhes;
import com.script.academia.services.AlunoService;
import jakarta.validation.Valid;

@Controller
public class AlunoController {

	@Autowired
	private AlunoService alunoService;
	@Autowired
	private UsuarioRepository usuarioRepository;
	@Autowired
	private FichaDeTreinamentoRepository fichaDeTreinamentoRepository;
	@Autowired
	private AvaliacaoFisicaRepository avaliacaoFisicaRepository;

	private void adicionarNomeUsuario(Model model) {
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			UsuarioDetalhes detalhes = (UsuarioDetalhes) auth.getPrincipal();
			model.addAttribute("nome", detalhes.getUsuario().getNome());
		} catch (Exception e) {
			model.addAttribute("nome", "Usuário");
		}
	}

	@GetMapping("/cadastrarAlunos")
	public String cadastrarAluno(Model model) {
		adicionarNomeUsuario(model);
		model.addAttribute("aluno", new Aluno());
		return "alunos/cadastrarAlunos";
	}

	@PostMapping("/cadastrarAlunos")
	public String salvarAluno(@Valid Aluno aluno, BindingResult result, @RequestParam("senha") String senha,
			RedirectAttributes attributes) {

		if (result.hasErrors() || senha == null || senha.isBlank()) {
			attributes.addFlashAttribute("erro", "Preencha os campos corretamente.");
			return "redirect:/cadastrarAlunos";
		}

		alunoService.salvarAluno(aluno, senha);
		attributes.addFlashAttribute("Mensagem", "Aluno e usuário criados com sucesso!");
		return "redirect:/cadastrarAlunos";
	}

	@GetMapping("/listarAlunos")
	public String listarAlunos(Model model) {
		adicionarNomeUsuario(model);
		model.addAttribute("alunos", alunoService.listarTodos());
		return "alunos/listarAlunos";
	}

	@GetMapping("/alunos/{id}")
	public String buscarPorId(@PathVariable Long id, Model model) {
		Optional<Aluno> aluno = alunoService.buscarPorId(id);
		if (aluno.isPresent()) {
			adicionarNomeUsuario(model);
			model.addAttribute("alunos", List.of(aluno.get()));
			model.addAttribute("aluno", aluno.get());
			return "alunos/listarAlunos";
		}
		return "redirect:/listarAlunos";
	}

	@GetMapping("/buscarAlunos")
	public String buscarPorNome(@RequestParam("nome") String nome, Model model) {
		adicionarNomeUsuario(model);
		model.addAttribute("alunos", alunoService.buscarPorNome(nome));
		return "alunos/listarAlunos";
	}

	@PostMapping("/editarAlunos/{id}")
	public String editarAluno(@PathVariable Long id, @Valid Aluno alunoAtualizado, BindingResult result,
			RedirectAttributes attributes) {

		if (result.hasErrors()) {
			attributes.addFlashAttribute("erro", "Verifique os campos...");
			return "redirect:/editarAlunos/" + id;
		}

		alunoService.editarAluno(id, alunoAtualizado);
		attributes.addFlashAttribute("Mensagem", "Aluno atualizado com sucesso!");
		return "redirect:/listarAlunos";
	}

	@GetMapping("/editarAlunos/{id}")
	public String exibirFormularioEdicao(@PathVariable Long id, Model model) {
		Optional<Aluno> aluno = alunoService.buscarPorId(id);
		if (aluno.isPresent()) {
			adicionarNomeUsuario(model);
			model.addAttribute("aluno", aluno.get());
			return "alunos/editarAlunos";
		}
		return "redirect:/listarAlunos";
	}

	@GetMapping("/deletarAluno/{id}")
	public String deletarAluno(@PathVariable Long id, RedirectAttributes attributes) {
		boolean deletado = alunoService.deletarAluno(id);
		if (deletado) {
			attributes.addFlashAttribute("Mensagem", "Aluno deletado com sucesso!");
		} else {
			attributes.addFlashAttribute("erro", "Aluno não encontrado");
		}
		return "redirect:/listarAlunos";
	}

	@GetMapping("/minhaFicha")
	@PreAuthorize("hasRole('ALUNO')")
	public String minhaFicha(Model model, Principal principal) {
		adicionarNomeUsuario(model);
		Usuario usuario = usuarioRepository.findByEmail(principal.getName()).orElseThrow();
		Aluno aluno = usuario.getAluno();
		List<FichaDeTreinamento> fichas = fichaDeTreinamentoRepository.findByAlunoOrderByDataCriacaoDesc(aluno);
		model.addAttribute("aluno", aluno);
		model.addAttribute("fichas", fichas);
		return "minhaFicha";
	}

	@GetMapping("/minhaAvaliacao")
	@PreAuthorize("hasRole('ALUNO')")
	public String minhaAvaliacao(Model model, Principal principal) {
		adicionarNomeUsuario(model);
		Usuario usuario = usuarioRepository.findByEmail(principal.getName()).orElseThrow();
		Aluno aluno = usuario.getAluno();
		List<AvaliacaoFisica> avaliacoes = avaliacaoFisicaRepository.findByAlunoOrderByDataDesc(aluno);
		model.addAttribute("aluno", aluno);
		model.addAttribute("avaliacoes", avaliacoes);
		return "minhaAvaliacao";
	}
}
