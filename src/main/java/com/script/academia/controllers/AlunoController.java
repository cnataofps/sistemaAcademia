package com.script.academia.controllers;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.script.academia.entities.Aluno;
import com.script.academia.entities.AvaliacaoFisica;
import com.script.academia.entities.FichaDeTreinamento;
import com.script.academia.entities.Perfil;
import com.script.academia.entities.Treino;
import com.script.academia.entities.Usuario;
import com.script.academia.repository.AlunoRepository;
import com.script.academia.repository.AvaliacaoFisicaRepository;
import com.script.academia.repository.FichaDeTreinamentoRepository;
import com.script.academia.repository.TreinoRepository;
import com.script.academia.repository.UsuarioRepository;
import com.script.academia.security.UsuarioDetalhes;
import jakarta.validation.Valid;

@Controller
public class AlunoController {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private TreinoRepository treinoRepository;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private FichaDeTreinamentoRepository fichaDeTreinamentoRepository;

	@Autowired
	private AvaliacaoFisicaRepository avaliacaoFisicaRepository;

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

	@GetMapping("/cadastrarAlunos")
	public String cadastrarAluno(Model model) {
		adicionarNomeUsuario(model);
		model.addAttribute("aluno", new Aluno());
		return "alunos/cadastrarAlunos";
	}

	@PostMapping("/cadastrarAlunos")
	public String salvarAluno(@Valid Aluno aluno, BindingResult result, @RequestParam("senha") String senha,
			RedirectAttributes attributes) {

		if (result.hasErrors()) {
			attributes.addFlashAttribute("erro", "Preencha todos os campos corretamente.");
			return "redirect:/cadastrarAlunos";
		}

		if (senha == null || senha.isBlank()) {
			attributes.addFlashAttribute("erro", "A senha não pode estar vazia.");
			return "redirect:/cadastrarAlunos";
		}

		alunoRepository.save(aluno);

		Usuario usuario = new Usuario();
		usuario.setNome(aluno.getNome());
		usuario.setEmail(aluno.getEmail());
		usuario.setSenha(passwordEncoder.encode(senha));
		usuario.setPerfil(Perfil.ALUNO);
		usuario.setAluno(aluno);

		usuarioRepository.save(usuario);

		aluno.setUsuario(usuario); // vínculo reverso
		alunoRepository.save(aluno); // atualiza com usuario_id

		attributes.addFlashAttribute("Mensagem", "Aluno e usuário criados com sucesso!");
		return "redirect:/cadastrarAlunos";
	}

	@GetMapping("/listarAlunos")
	public String listarAlunos(Model model) {
		adicionarNomeUsuario(model);
		List<Aluno> alunos = alunoRepository.findAll();
		model.addAttribute("alunos", alunos);
		return "alunos/listarAlunos";
	}

	@GetMapping("/alunos/{id}")
	public String buscarPorId(@PathVariable Long id, Model model) {
		Optional<Aluno> aluno = alunoRepository.findById(id);
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
		List<Aluno> alunos = alunoRepository.findByNomeContainingIgnoreCase(nome);
		model.addAttribute("alunos", alunos);
		return "alunos/listarAlunos";
	}

	@PostMapping("/editarAlunos/{id}")
	public String editarAluno(@PathVariable Long id, @Valid Aluno alunoAtualizado, BindingResult result,
			RedirectAttributes attributes) {

		if (result.hasErrors()) {
			attributes.addFlashAttribute("erro", "Verifique os campos...");
			return "redirect:/editarAlunos/" + id;
		}

		Optional<Aluno> alunoExistente = alunoRepository.findById(id);
		if (alunoExistente.isPresent()) {
			Aluno aluno = alunoExistente.get();
			aluno.setMatricula(alunoAtualizado.getMatricula());
			aluno.setNome(alunoAtualizado.getNome());
			aluno.setCpf(alunoAtualizado.getCpf());
			aluno.setDataNasc(alunoAtualizado.getDataNasc());
			aluno.setProfissao(alunoAtualizado.getProfissao());
			aluno.setEndereco(alunoAtualizado.getEndereco());

			alunoRepository.save(aluno);

			Usuario usuario = aluno.getUsuario();
			if (usuario != null) {
				usuario.setNome(aluno.getNome());
				usuario.setEmail(aluno.getEmail());
				usuarioRepository.save(usuario);
			}

			attributes.addFlashAttribute("Mensagem", "Aluno atualizado com sucesso!");
		} else {
			attributes.addFlashAttribute("erro", "Aluno não encontrado");
		}

		return "redirect:/listarAlunos";
	}

	@GetMapping("/editarAlunos/{id}")
	public String exibirFormularioEdicao(@PathVariable Long id, Model model) {
		Optional<Aluno> aluno = alunoRepository.findById(id);
		if (aluno.isPresent()) {
			adicionarNomeUsuario(model);
			model.addAttribute("aluno", aluno.get());
			return "alunos/editarAlunos";
		}
		return "redirect:/listarAlunos";
	}

	@GetMapping("/deletarAluno/{id}")
	public String deletarAluno(@PathVariable Long id, RedirectAttributes attributes) {
		if (alunoRepository.existsById(id)) {
			alunoRepository.deleteById(id);
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

		FichaDeTreinamento ficha = fichaDeTreinamentoRepository.findTopByAlunoOrderByDataCriacaoDesc(aluno);
		List<Treino> treinos = treinoRepository.findByAluno(aluno);

		model.addAttribute("aluno", aluno);
		model.addAttribute("ficha", ficha);
		model.addAttribute("treinos", treinos);

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
