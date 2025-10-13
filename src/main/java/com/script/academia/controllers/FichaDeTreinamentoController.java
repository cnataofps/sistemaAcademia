package com.script.academia.controllers;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.script.academia.entities.FichaDeTreinamento;
import com.script.academia.repository.AlunoRepository;
import com.script.academia.security.UsuarioDetalhes;
import com.script.academia.services.FichaDeTreinamentoService;
import jakarta.validation.Valid;

@Controller
public class FichaDeTreinamentoController {

	@Autowired
	private FichaDeTreinamentoService fichaService;

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

	@GetMapping("/cadastrarFicha")
	public String exibirFormularioCadastro(Model model) {
		adicionarNomeUsuario(model);
		model.addAttribute("ficha", new FichaDeTreinamento());
		model.addAttribute("alunos", alunoRepository.findAll());
		return "fichas/cadastrarFicha";
	}

	@PostMapping("/cadastrarFicha")
	public String salvarFicha(@RequestParam("alunoId") Long alunoId, @Valid FichaDeTreinamento ficha,
			BindingResult result, RedirectAttributes attributes) {
		if (result.hasErrors()) {
			attributes.addFlashAttribute("Mensagem", "Preencher todos os campos necessário...");
			return "redirect:/cadastrarFicha";
		}

		Long professorId = ((UsuarioDetalhes) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
				.getUsuario().getId();

		fichaService.salvarFicha(alunoId, ficha, professorId);
		attributes.addFlashAttribute("Mensagem", "Ficha cadastrada com sucesso!");
		return "redirect:/cadastrarFicha";
	}

	@GetMapping("/listarFichas")
	public String listarFichas(Model model) {
		adicionarNomeUsuario(model);
		model.addAttribute("fichas", fichaService.listarTodas());
		return "fichas/listarFichas";
	}

	@GetMapping("/fichas/{id}")
	public String buscarPorId(@PathVariable Long id, Model model) {
		Optional<FichaDeTreinamento> ficha = fichaService.buscarPorId(id);
		if (ficha.isPresent()) {
			adicionarNomeUsuario(model);
			model.addAttribute("fichas", List.of(ficha.get()));
			return "fichas/listarFichas";
		}
		model.addAttribute("Mensagem", "Ficha não encontrada");
		return "redirect:/listarFichas";
	}

	@GetMapping("/buscarFichas")
	public String buscarPorNome(@RequestParam("nome") String nome, Model model) {
		adicionarNomeUsuario(model);
		model.addAttribute("fichas", fichaService.buscarPorNomeAluno(nome));
		return "fichas/listarFichas";
	}

	@GetMapping("/editarFicha/{id}")
	public String exibirFormularioEdicao(@PathVariable Long id, Model model) {
		Optional<FichaDeTreinamento> ficha = fichaService.buscarPorId(id);
		if (ficha.isPresent()) {
			adicionarNomeUsuario(model);
			model.addAttribute("ficha", ficha.get());
			model.addAttribute("alunos", alunoRepository.findAll());
			return "fichas/editarFicha";
		}
		return "redirect:/listarFichas";
	}

	@PostMapping("/editarFicha/{id}")
	public String editarFicha(@PathVariable Long id, @Valid FichaDeTreinamento fichaAtualizada, BindingResult result,
			RedirectAttributes attributes) {
		if (result.hasErrors()) {
			attributes.addFlashAttribute("Mensagem", "Verifique os campos...");
			return "redirect:/editarFicha/" + id;
		}

		Long professorId = ((UsuarioDetalhes) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
				.getUsuario().getId();

		fichaService.atualizarFicha(id, fichaAtualizada, professorId);
		attributes.addFlashAttribute("Mensagem", "Ficha de Treino atualizada com sucesso!");
		return "redirect:/listarFichas";
	}

	@GetMapping("/deletarFicha/{id}")
	public String deletarFicha(@PathVariable Long id, RedirectAttributes attributes) {
		boolean deletado = fichaService.deletarFicha(id);
		attributes.addFlashAttribute("Mensagem",
				deletado ? "Ficha de Treino deletada com sucesso!" : "Ficha de Treino não encontrada");
		return "redirect:/listarFichas";
	}

	@GetMapping("/todasFichas/{alunoId}")
	public String visualizarTodasFichasDoAluno(@PathVariable Long alunoId, Model model) {
		adicionarNomeUsuario(model);
		List<FichaDeTreinamento> fichas = fichaService.buscarPorAlunoId(alunoId);
		model.addAttribute("fichas", fichas);
		return "minhaFicha";
	}
}
