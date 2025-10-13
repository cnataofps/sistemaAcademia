package com.script.academia.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.script.academia.entities.Aluno;
import com.script.academia.entities.Exercicios;
import com.script.academia.entities.FichaDeTreinamento;
import com.script.academia.entities.Treino;
import com.script.academia.entities.Usuario;
import com.script.academia.repository.AlunoRepository;
import com.script.academia.repository.FichaDeTreinamentoRepository;
import com.script.academia.repository.UsuarioRepository;

@Service
public class FichaDeTreinamentoService {

	@Autowired
	private FichaDeTreinamentoRepository fichaRepository;

	@Autowired
	private AlunoRepository alunoRepository;

	@Autowired
	private UsuarioRepository usuarioRepository;

	public void salvarFicha(Long alunoId, FichaDeTreinamento ficha, Long professorId) {
		ficha.setDataCriacao(LocalDate.now());

		Aluno aluno = alunoRepository.findById(alunoId).orElse(null);
		ficha.setAluno(aluno);

		Usuario professor = usuarioRepository.findById(professorId).orElse(null);
		ficha.setProfessor(professor);

		if (ficha.getTreinos() != null) {
			for (Treino treino : ficha.getTreinos()) {
				treino.setFicha(ficha);
				treino.setAluno(aluno);
				if (treino.getExercicios() != null) {
					for (Exercicios exercicio : treino.getExercicios()) {
						exercicio.setTreino(treino);
					}
				}
			}
		}

		fichaRepository.save(ficha);
	}

	public List<FichaDeTreinamento> listarTodas() {
		return fichaRepository.findAll();
	}

	public Optional<FichaDeTreinamento> buscarPorId(Long id) {
		return fichaRepository.findById(id);
	}

	public List<FichaDeTreinamento> buscarPorNomeAluno(String nome) {
		return fichaRepository.findByAluno_NomeContainingIgnoreCase(nome);
	}

	public void atualizarFicha(Long id, FichaDeTreinamento atualizada, Long professorId) {
		Optional<FichaDeTreinamento> fichaOpt = fichaRepository.findById(id);
		if (fichaOpt.isPresent()) {
			FichaDeTreinamento ficha = fichaOpt.get();
			ficha.setObjetivo(atualizada.getObjetivo());
			ficha.setDataCriacao(atualizada.getDataCriacao());
			ficha.setDataTroca(atualizada.getDataTroca());
			ficha.setAluno(atualizada.getAluno());

			Usuario professor = usuarioRepository.findById(professorId).orElse(null);
			ficha.setProfessor(professor);

			List<Treino> novosTreinos = atualizada.getTreinos();
			Map<Long, Treino> mapaTreinos = ficha.getTreinos().stream()
					.collect(Collectors.toMap(Treino::getId, t -> t));

			ficha.getTreinos().clear();

			if (novosTreinos != null) {
				for (Treino novo : novosTreinos) {
					Treino treino = mapaTreinos.getOrDefault(novo.getId(), new Treino());
					treino.setNome(novo.getNome());
					treino.setFicha(ficha);
					treino.setAluno(atualizada.getAluno());

					Map<Long, Exercicios> mapaExercicios = Optional.ofNullable(treino.getExercicios())
							.orElse(new ArrayList<>()).stream().collect(Collectors.toMap(Exercicios::getId, e -> e));

					List<Exercicios> novosExercicios = new ArrayList<>();
					if (novo.getExercicios() != null) {
						for (Exercicios novoEx : novo.getExercicios()) {
							Exercicios ex = mapaExercicios.getOrDefault(novoEx.getId(), new Exercicios());
							ex.setNome(novoEx.getNome());
							ex.setSerie(novoEx.getSerie());
							ex.setRepeticoes(novoEx.getRepeticoes());
							ex.setCarga(novoEx.getCarga());
							ex.setObservacao(novoEx.getObservacao());
							ex.setTreino(treino);
							novosExercicios.add(ex);
						}
					}

					treino.setExercicios(novosExercicios);
					ficha.getTreinos().add(treino);
				}
			}

			fichaRepository.save(ficha);
		}
	}

	public boolean deletarFicha(Long id) {
		if (fichaRepository.existsById(id)) {
			fichaRepository.deleteById(id);
			return true;
		}
		return false;
	}

	public List<FichaDeTreinamento> buscarPorAlunoId(Long alunoId) {
		return fichaRepository.findByAlunoIdOrderByDataCriacaoDesc(alunoId);
	}
}
