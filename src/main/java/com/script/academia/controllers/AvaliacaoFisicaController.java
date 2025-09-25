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
import com.script.academia.repository.AvaliacaoFisicaRepository;
import com.script.academia.security.UsuarioDetalhes;
import jakarta.validation.Valid;

@Controller
public class AvaliacaoFisicaController {

	@Autowired
	private AvaliacaoFisicaRepository avaliacaoFisicaRepository;

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

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UsuarioDetalhes detalhes = (UsuarioDetalhes) auth.getPrincipal();
		Usuario professorLogado = detalhes.getUsuario();
		avaliacaoFisica.setProfessor(professorLogado);

		avaliacaoFisicaRepository.save(avaliacaoFisica);
		attributes.addFlashAttribute("Mensagem", "Avaliação cadastrada com sucesso!");
		return "redirect:/cadastrarAvaliacaoFisica";
	}

	@GetMapping("/listarAvaliacoesFisica")
	public String listarAvaliacoesFisica(Model model) {
		adicionarNomeUsuario(model);
		List<AvaliacaoFisica> avaliacoes = avaliacaoFisicaRepository.findAll();
		model.addAttribute("avaliacoes", avaliacoes);
		return "avaliacoes/listarAvaliacoesFisica";
	}

	@GetMapping("/avaliacoesFisica/{id}")
	public String buscarPorId(@PathVariable Long id, Model model) {
		Optional<AvaliacaoFisica> avaliacao = avaliacaoFisicaRepository.findById(id);
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
		List<AvaliacaoFisica> avaliacoes = avaliacaoFisicaRepository.findByAluno_NomeContainingIgnoreCase(nome);
		model.addAttribute("avaliacoes", avaliacoes);
		return "avaliacoes/listarAvaliacoesFisica";
	}

	@GetMapping("/editarAvaliacaoFisica/{id}")
	public String exibirFormularioEdicao(@PathVariable Long id, Model model) {
		Optional<AvaliacaoFisica> avaliacao = avaliacaoFisicaRepository.findById(id);
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

	    Optional<AvaliacaoFisica> existente = avaliacaoFisicaRepository.findById(id);
	    if (existente.isPresent()) {
	        AvaliacaoFisica avaliacao = existente.get();
	        // Atualiza campos da avaliação
			avaliacao.setData(nova.getData());
			avaliacao.setDataTroca(nova.getDataTroca());
			avaliacao.setObjetivo(nova.getObjetivo());
			avaliacao.setTorax(nova.getTorax());
			avaliacao.setAbdominal(nova.getAbdominal());
			avaliacao.setQuadril(nova.getQuadril());
			avaliacao.setAntebraco(nova.getAntebraco());
			avaliacao.setBracorelaxado(nova.getBracorelaxado());
			avaliacao.setBracocontraido(nova.getBracocontraido());
			avaliacao.setCoxasuperior(nova.getCoxasuperior());
			avaliacao.setPanturilha(nova.getPanturilha());
			avaliacao.setTricipal(nova.getTricipal());
			avaliacao.setSubescapular(nova.getSubescapular());
			avaliacao.setPeitoral(nova.getPeitoral());
			avaliacao.setAbdomen(nova.getAbdomen());
			avaliacao.setSuprailiaca(nova.getSuprailiaca());
			avaliacao.setCoxa(nova.getCoxa());
			avaliacao.setAxialmedia(nova.getAxialmedia());
			avaliacao.setPeso(nova.getPeso());
			avaliacao.setAltura(nova.getAltura());
			avaliacao.setImc(nova.getImc());
			avaliacao.setPercentualgordura(nova.getPercentualgordura());
			avaliacao.setMassamagra(nova.getMassamagra());
			avaliacao.setMassagorda(nova.getMassagorda());
			avaliacao.setProblemaArticular(nova.getProblemaArticular());
			avaliacao.setCirurgia(nova.getCirurgia());
			avaliacao.setColuna(nova.getColuna());
			avaliacao.setCardiopata(nova.getCardiopata());
			avaliacao.setHipertenso(nova.getHipertenso());
			avaliacao.setDiabetico(nova.getDiabetico());
			avaliacao.setFuma(nova.getFuma());
			avaliacao.setBebe(nova.getBebe());
			avaliacao.setToxico(nova.getToxico());
			avaliacao.setJaFezMusculacao(nova.getJaFezMusculacao());
			avaliacao.setTempoMusculacao(nova.getTempoMusculacao());
			avaliacao.setInativoHa(nova.getInativoHa());
			avaliacao.setFrequenciaSemanal(nova.getFrequenciaSemanal());
			avaliacao.setTempoDisponivel(nova.getTempoDisponivel());
			avaliacao.setObservacao(nova.getObservacao());

			 // Atualiza aluno
	        if (nova.getAluno() != null && nova.getAluno().getId() != null) {
	            Aluno aluno = alunoRepository.findById(nova.getAluno().getId()).orElse(null);
	            avaliacao.setAluno(aluno);
	        }

	        // Atualiza professor logado
	        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	        UsuarioDetalhes detalhes = (UsuarioDetalhes) auth.getPrincipal();
	        Usuario professorLogado = detalhes.getUsuario();
	        avaliacao.setProfessor(professorLogado);

	        avaliacaoFisicaRepository.save(avaliacao);
	        attributes.addFlashAttribute("Mensagem", "Avaliação atualizada com sucesso!");
	    } else {
	        attributes.addFlashAttribute("Mensagem", "Avaliação não encontrada");
	    }

	    return "redirect:/listarAvaliacoesFisica";
	}

	@GetMapping("/deletarAvaliacaoFisica/{id}")
	public String deletarAvaliacao(@PathVariable Long id, RedirectAttributes attributes) {
		if (avaliacaoFisicaRepository.existsById(id)) {
			avaliacaoFisicaRepository.deleteById(id);
			attributes.addFlashAttribute("Mensagem", "Avaliação deletada com sucesso!");
		} else {
			attributes.addFlashAttribute("Mensagem", "Avaliação não encontrada");
		}

		return "redirect:/listarAvaliacoesFisica";
	}

	@GetMapping("/todasAvaliacoes/{alunoId}")
	public String visualizarTodasAvaliacoesDoAluno(@PathVariable Long alunoId, Model model) {
		adicionarNomeUsuario(model);
		Aluno aluno = alunoRepository.findById(alunoId).orElse(null);
		List<AvaliacaoFisica> avaliacoes = avaliacaoFisicaRepository.findByAlunoIdOrderByDataDesc(alunoId);
		model.addAttribute("aluno", aluno);
		model.addAttribute("avaliacoes", avaliacoes);
		return "avaliacoes/todasAvaliacoesDoAluno";
	}
}
