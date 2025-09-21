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
import com.script.academia.entities.Exercicios;
import com.script.academia.entities.FichaDeTreinamento;
import com.script.academia.entities.Treino;
import com.script.academia.repository.AlunoRepository;
import com.script.academia.repository.FichaDeTreinamentoRepository;
import com.script.academia.security.UsuarioDetalhes;
import jakarta.validation.Valid;

@Controller
public class FichaDeTreinamentoController {

	@Autowired
	private FichaDeTreinamentoRepository fichaDeTreinamentoRepository;

	@Autowired
	private AlunoRepository alunoRepository;

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

	@GetMapping("/cadastrarFicha")
	public String exibirFormularioCadastro(Model model) {
		adicionarNomeUsuario(model);
		model.addAttribute("ficha", new FichaDeTreinamento());
		model.addAttribute("alunos", alunoRepository.findAll());
		return "fichas/cadastrarFicha";
	}

	@PostMapping("/cadastrarFicha")
	public String salvarFicha(@Valid FichaDeTreinamento ficha, BindingResult result, RedirectAttributes attributes) {
		if (result.hasErrors()) {
			attributes.addFlashAttribute("Mensagem", "Preencher todos os campos necessário...");
			return "redirect:/cadastrarFicha";
		}

		if (ficha.getTreinos() != null) {
			for (Treino treino : ficha.getTreinos()) {
				treino.setFicha(ficha);
				treino.setAluno(ficha.getAluno()); // ✅ vincula o aluno ao treino

				if (treino.getExercicios() != null) {
					for (Exercicios exercicio : treino.getExercicios()) {
						exercicio.setTreino(treino);
					}
				}
			}
		}

		fichaDeTreinamentoRepository.save(ficha);
		attributes.addFlashAttribute("Mensagem", "Ficha cadastrada com sucesso!");
		return "redirect:/cadastrarFicha";
	}

	@GetMapping("/listarFichas")
	public String listarFichas(Model model) {
		adicionarNomeUsuario(model);
		List<FichaDeTreinamento> fichas = fichaDeTreinamentoRepository.findAll();
		model.addAttribute("fichas", fichas);
		return "fichas/listarFichas";
	}

	@GetMapping("/fichas/{id}")
	public String buscarPorId(@PathVariable Long id, Model model) {
		Optional<FichaDeTreinamento> fichaDeTreinamento = fichaDeTreinamentoRepository.findById(id);
		if (fichaDeTreinamento.isPresent()) {
			adicionarNomeUsuario(model);
			model.addAttribute("fichas", List.of(fichaDeTreinamento.get()));
			return "fichas/listarFichas";
		}
		model.addAttribute("Mensagem", "Ficha não encontrada");
		return "redirect:/listarFichas";
	}

	@GetMapping("/buscarFichas")
	public String buscarPorNome(@RequestParam("nome") String nome, Model model) {
		adicionarNomeUsuario(model);
		List<FichaDeTreinamento> fichas = fichaDeTreinamentoRepository.findByAluno_NomeContainingIgnoreCase(nome);
		model.addAttribute("fichas", fichas);
		return "fichas/listarFichas";
	}

	@GetMapping("/editarFicha/{id}")
	public String exibirFormularioEdicao(@PathVariable Long id, Model model) {
		Optional<FichaDeTreinamento> fichaDeTreinamento = fichaDeTreinamentoRepository.findById(id);
		if (fichaDeTreinamento.isPresent()) {
			adicionarNomeUsuario(model);
			model.addAttribute("ficha", fichaDeTreinamento.get());
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

		Optional<FichaDeTreinamento> fichaOpt = fichaDeTreinamentoRepository.findById(id);
		if (fichaOpt.isPresent()) {
			FichaDeTreinamento fichaExistente = fichaOpt.get();

			fichaExistente.setObjetivo(fichaAtualizada.getObjetivo());
			fichaExistente.setDataCriacao(fichaAtualizada.getDataCriacao());
			fichaExistente.setDataTroca(fichaAtualizada.getDataTroca());
			fichaExistente.setAluno(fichaAtualizada.getAluno());

			// Atualiza treinos
			List<Treino> treinosAtualizados = fichaAtualizada.getTreinos();
			List<Treino> treinosExistentes = fichaExistente.getTreinos();

			// Mapeia treinos existentes por ID
			java.util.Map<Long, Treino> mapaTreinos = new java.util.HashMap<>();
			for (Treino t : treinosExistentes) {
				mapaTreinos.put(t.getId(), t);
			}

			treinosExistentes.clear();

			if (treinosAtualizados != null) {
				for (Treino treinoNovo : treinosAtualizados) {
					Treino treinoFinal;

					if (treinoNovo.getId() != null && mapaTreinos.containsKey(treinoNovo.getId())) {
						// Atualiza treino existente
						treinoFinal = mapaTreinos.get(treinoNovo.getId());
					} else {
						// Novo treino
						treinoFinal = new Treino();
					}

					treinoFinal.setNome(treinoNovo.getNome());
					treinoFinal.setFicha(fichaExistente);
					treinoFinal.setAluno(fichaAtualizada.getAluno());

					// Atualiza exercícios
					List<Exercicios> exerciciosAtualizados = treinoNovo.getExercicios();
					List<Exercicios> exerciciosExistentes = treinoFinal.getExercicios() != null
							? treinoFinal.getExercicios()
							: new java.util.ArrayList<>();

					java.util.Map<Long, Exercicios> mapaExercicios = new java.util.HashMap<>();
					for (Exercicios e : exerciciosExistentes) {
						mapaExercicios.put(e.getId(), e);
					}

					exerciciosExistentes.clear();

					if (exerciciosAtualizados != null) {
						for (Exercicios exercicioNovo : exerciciosAtualizados) {
							Exercicios exercicioFinal;

							if (exercicioNovo.getId() != null && mapaExercicios.containsKey(exercicioNovo.getId())) {
								exercicioFinal = mapaExercicios.get(exercicioNovo.getId());
							} else {
								exercicioFinal = new Exercicios();
							}

							exercicioFinal.setNome(exercicioNovo.getNome());
							exercicioFinal.setSerie(exercicioNovo.getSerie());
							exercicioFinal.setRepeticoes(exercicioNovo.getRepeticoes());
							exercicioFinal.setCarga(exercicioNovo.getCarga());
							exercicioFinal.setObservacao(exercicioNovo.getObservacao());
							exercicioFinal.setTreino(treinoFinal);

							exerciciosExistentes.add(exercicioFinal);
						}
					}

					treinoFinal.setExercicios(exerciciosExistentes);
					treinosExistentes.add(treinoFinal);
				}
			}

			fichaDeTreinamentoRepository.save(fichaExistente);
			attributes.addFlashAttribute("Mensagem", "Ficha de Treino atualizada com sucesso!");
		} else {
			attributes.addFlashAttribute("Mensagem", "Ficha de Treino não encontrada");
		}

		return "redirect:/listarFichas";
	}

	@GetMapping("/deletarFicha/{id}")
	public String deletarFicha(@PathVariable Long id, RedirectAttributes attributes) {
		if (fichaDeTreinamentoRepository.existsById(id)) {
			fichaDeTreinamentoRepository.deleteById(id);
			attributes.addFlashAttribute("Mensagem", "Ficha de Treino deletado com sucesso!");
		} else {
			attributes.addFlashAttribute("Mensagem", "Ficha de Treino não encontrado");
		}

		return "redirect:/listarFichas";
	}
}
