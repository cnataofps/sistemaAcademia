package com.script.academia.controllers;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.script.academia.entities.Aluno;
import com.script.academia.entities.AvaliacaoFisica;
import com.script.academia.entities.Usuario;
import com.script.academia.repository.AlunoRepository;
import com.script.academia.security.UsuarioDetalhes;
import com.script.academia.services.AvaliacaoFisicaService;

import jakarta.validation.Valid;

@Controller
public class AvaliacaoFisicaController {

	@Autowired
	private AvaliacaoFisicaService avaliacaoFisicaService;

	@Autowired
	private AlunoRepository alunoRepository;

	private void adicionarNomeUsuario(Model model) {
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			UsuarioDetalhes detalhes = (UsuarioDetalhes) auth.getPrincipal();
			model.addAttribute("nome", detalhes.getUsuario().getNome());
		} catch (Exception e) {
			model.addAttribute("nome", "Usuário");
		}
	}

	@GetMapping("/cadastrarAvaliacaoFisica")
	public String cadastrarAvaliacaoFisica(Model model) {
		adicionarNomeUsuario(model);
		model.addAttribute("avaliacaoFisica", new AvaliacaoFisica());
		model.addAttribute("alunos", alunoRepository.findAll());
		return "avaliacoes/cadastrarAvaliacaoFisica";
	}

	@PostMapping("/cadastrarAvaliacaoFisica")
	public String salvarAvaliacaoFisica(@Valid AvaliacaoFisica avaliacaoFisica, BindingResult result,
			RedirectAttributes attributes) {
		if (result.hasErrors()) {
			attributes.addFlashAttribute("Mensagem", "Preencher todos os campos necessário...");
			return "redirect:/cadastrarAvaliacaoFisica";
		}

		Usuario professor = ((UsuarioDetalhes) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
				.getUsuario();
		avaliacaoFisicaService.salvarAvaliacao(avaliacaoFisica, professor);
		attributes.addFlashAttribute("Mensagem", "Avaliação cadastrada com sucesso!");
		return "redirect:/cadastrarAvaliacaoFisica";
	}

	@GetMapping("/listarAvaliacoesFisica")
	public String listarAvaliacoesFisica(Model model) {
		adicionarNomeUsuario(model);
		model.addAttribute("avaliacoes", avaliacaoFisicaService.listarTodas());
		return "avaliacoes/listarAvaliacoesFisica";
	}

	@GetMapping("/avaliacoesFisica/{id}")
	public String buscarPorId(@PathVariable Long id, Model model) {
		Optional<AvaliacaoFisica> avaliacao = avaliacaoFisicaService.buscarPorId(id);
		if (avaliacao.isPresent()) {
			adicionarNomeUsuario(model);
			model.addAttribute("avaliacoes", List.of(avaliacao.get()));
			model.addAttribute("avaliacaoFisica", avaliacao.get());
			return "avaliacoes/listarAvaliacoesFisica";
		}
		model.addAttribute("Mensagem", "Avaliação não encontrada");
		return "redirect:/listarAvaliacoesFisica";
	}

	@GetMapping("/buscarAvaliacoesFisica")
	public String buscarPorNomeAluno(@RequestParam("nome") String nome, Model model) {
		adicionarNomeUsuario(model);
		model.addAttribute("avaliacoes", avaliacaoFisicaService.buscarPorNomeAluno(nome));
		return "avaliacoes/listarAvaliacoesFisica";
	}

	@GetMapping("/editarAvaliacaoFisica/{id}")
	public String exibirFormularioEdicao(@PathVariable Long id, Model model) {
		Optional<AvaliacaoFisica> avaliacao = avaliacaoFisicaService.buscarPorId(id);
		if (avaliacao.isPresent()) {
			adicionarNomeUsuario(model);
			model.addAttribute("avaliacaoFisica", avaliacao.get());
			model.addAttribute("alunos", alunoRepository.findAll());
			return "avaliacoes/editarAvaliacaoFisica";
		}
		return "redirect:/listarAvaliacoesFisica";
	}

	@PostMapping("/editarAvaliacaoFisica/{id}")
	public String editarAvaliacao(@PathVariable Long id, @Valid AvaliacaoFisica nova, BindingResult result,
			RedirectAttributes attributes) {
		if (result.hasErrors()) {
			attributes.addFlashAttribute("Mensagem", "Verifique os campos...");
			return "redirect:/editarAvaliacaoFisica/" + id;
		}

		Usuario professor = ((UsuarioDetalhes) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
				.getUsuario();
		avaliacaoFisicaService.atualizarAvaliacao(id, nova, professor);
		attributes.addFlashAttribute("Mensagem", "Avaliação atualizada com sucesso!");
		return "redirect:/listarAvaliacoesFisica";
	}

	@GetMapping("/deletarAvaliacaoFisica/{id}")
	public String deletarAvaliacao(@PathVariable Long id, RedirectAttributes attributes) {
		boolean deletado = avaliacaoFisicaService.deletar(id);
		attributes.addFlashAttribute("Mensagem",
				deletado ? "Avaliação deletada com sucesso!" : "Avaliação não encontrada");
		return "redirect:/listarAvaliacoesFisica";
	}

	@GetMapping("/todasAvaliacoes/{alunoId}")
	public String visualizarTodasAvaliacoesDoAluno(@PathVariable Long alunoId, Model model) {
		adicionarNomeUsuario(model);
		Aluno aluno = alunoRepository.findById(alunoId).orElse(null);
		model.addAttribute("aluno", aluno);
		model.addAttribute("avaliacoes", avaliacaoFisicaService.buscarPorAlunoId(alunoId));
		return "avaliacoes/todasAvaliacoesDoAluno";
	}
}
