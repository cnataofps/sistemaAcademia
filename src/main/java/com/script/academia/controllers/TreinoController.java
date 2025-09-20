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
import com.script.academia.entities.Exercicios;
import com.script.academia.entities.FichaDeTreinamento;
import com.script.academia.entities.Treino;
import com.script.academia.repository.AlunoRepository;
import com.script.academia.repository.FichaDeTreinamentoRepository;
import com.script.academia.repository.TreinoRepository;
import com.script.academia.security.UsuarioDetalhes;
import jakarta.validation.Valid;

@Controller
public class TreinoController {

	

	@Autowired
	private AlunoRepository alunoRepository;

	@Autowired
	private TreinoRepository treinoRepository;
	
	@Autowired
	private FichaDeTreinamentoRepository fichaDeTreinamentoRepository;

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

	@GetMapping("/cadastrarTreino")
	public String exibirFormularioTreino(Model model) {
	    model.addAttribute("treino", new Treino());
	    model.addAttribute("alunos", alunoRepository.findAll());
	    return "treinos/cadastrarTreino";
	}

	@GetMapping("/cadastrarTreinos")
	public String cadastrarTreino(Model model) {
		adicionarNomeUsuario(model);
		model.addAttribute("treino", new Treino());
		return "treinos/cadastrarTreinos";
	}

	@PostMapping("/cadastrarFichaCompleta")
	public String salvarFicha(@ModelAttribute FichaDeTreinamento fichaDeTreinamento,
	                          @RequestParam("alunoId") Long alunoId,
	                          RedirectAttributes attributes) {

		Aluno aluno = alunoRepository.findById(alunoId).orElseThrow();
		fichaDeTreinamento.setAluno(aluno);

		// 🔗 Vincula o aluno e a ficha aos treinos e exercícios
		if (fichaDeTreinamento.getTreinos() != null) {
		    for (Treino treino : fichaDeTreinamento.getTreinos()) {
		        treino.setAluno(aluno);
		        treino.setFicha(fichaDeTreinamento);

		        if (treino.getExercicios() != null) {
		            for (Exercicios exercicio : treino.getExercicios()) {
		                exercicio.setTreino(treino); // 🔗 vínculo direto
		            }
		        }
		    }
		}

		// ✅ Salva tudo em cascata
		fichaDeTreinamentoRepository.save(fichaDeTreinamento);

		attributes.addFlashAttribute("Mensagem", "Ficha cadastrada com sucesso!");
		return "redirect:/cadastrarFicha";
	}
	@GetMapping("/listarTreinos")
	public String listarTreinos(Model model) {
		adicionarNomeUsuario(model);
		List<Treino> treinos = treinoRepository.findAll();
		model.addAttribute("treinos", treinos);
		return "treinos/listarTreinos";
	}

	@GetMapping("/treinos/{id}")
	public String buscarPorId(@PathVariable Long id, Model model) {
		Optional<Treino> treino = treinoRepository.findById(id);
		if (treino.isPresent()) {
			adicionarNomeUsuario(model);
			model.addAttribute("treino", treino.get());
			return "treinos/listarTreinos";
		}
		model.addAttribute("Mensagem", "Treino não encontrado");
		return "redirect:/listarTreinos";
	}

	@GetMapping("/buscarTreinos")
	public String buscarPorNome(@RequestParam("nome") String nome, Model model) {
		adicionarNomeUsuario(model);
		List<Treino> treinos = treinoRepository.findByNomeContainingIgnoreCase(nome);
		model.addAttribute("treinos", treinos);
		return "treinos/listarTreinos";
	}

	@GetMapping("/editarTreinos/{id}")
	public String exibirFormularioEdicao(@PathVariable Long id, Model model) {
		Optional<Treino> treino = treinoRepository.findById(id);
		if (treino.isPresent()) {
			adicionarNomeUsuario(model);
			model.addAttribute("treino", treino.get());
			return "treinos/editarTreinos";
		}
		return "redirect:/listarTreinos";
	}

	@PostMapping("/editarTreinos/{id}")
	public String editarTreino(@PathVariable Long id, @Valid Treino treinoAtualizado, BindingResult result,
			RedirectAttributes attributes) {
		if (result.hasErrors()) {
			attributes.addFlashAttribute("Mensagem", "Verifique os campos...");
			return "redirect:/editarTreinos/" + id;
		}

		Optional<Treino> treinoExistente = treinoRepository.findById(id);
		if (treinoExistente.isPresent()) {
			Treino treino = treinoExistente.get();
			treino.setNome(treinoAtualizado.getNome());
			// Adicione outros campos conforme necessário

			treinoRepository.save(treino);
			attributes.addFlashAttribute("Mensagem", "Treino atualizado com sucesso!");
		} else {
			attributes.addFlashAttribute("Mensagem", "Treino não encontrado");
		}

		return "redirect:/listarTreinos";
	}

	@GetMapping("/deletarTreino/{id}")
	public String deletarTreino(@PathVariable Long id, RedirectAttributes attributes) {
		if (treinoRepository.existsById(id)) {
			treinoRepository.deleteById(id);
			attributes.addFlashAttribute("Mensagem", "Treino deletado com sucesso!");
		} else {
			attributes.addFlashAttribute("Mensagem", "Treino não encontrado");
		}

		return "redirect:/listarTreinos";
	}
}
