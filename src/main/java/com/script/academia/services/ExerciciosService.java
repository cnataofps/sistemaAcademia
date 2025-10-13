package com.script.academia.services;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.script.academia.entities.Exercicios;
import com.script.academia.entities.Treino;
import com.script.academia.repository.ExerciciosRepository;
import com.script.academia.repository.TreinoRepository;

@Service
public class ExerciciosService {

	@Autowired
	private ExerciciosRepository exerciciosRepository;

	@Autowired
	private TreinoRepository treinoRepository;

	public void salvar(Exercicios exercicio, Long treinoId) {
		Treino treino = treinoRepository.findById(treinoId).orElseThrow();
		exercicio.setTreino(treino);
		exerciciosRepository.save(exercicio);
	}

	public List<Exercicios> listarTodos() {
		return exerciciosRepository.findAll();
	}

	public Optional<Exercicios> buscarPorId(Long id) {
		return exerciciosRepository.findById(id);
	}

	public List<Exercicios> buscarPorNome(String nome) {
		return exerciciosRepository.findByNomeContainingIgnoreCase(nome);
	}

	public boolean editar(Long id, Exercicios atualizado) {
		Optional<Exercicios> existente = exerciciosRepository.findById(id);
		if (existente.isPresent()) {
			Exercicios exercicio = existente.get();
			exercicio.setNome(atualizado.getNome());
			exercicio.setSerie(atualizado.getSerie());
			exercicio.setRepeticoes(atualizado.getRepeticoes());
			exercicio.setCarga(atualizado.getCarga());
			exercicio.setObservacao(atualizado.getObservacao());
			exerciciosRepository.save(exercicio);
			return true;
		}
		return false;
	}

	public boolean deletar(Long id) {
		if (exerciciosRepository.existsById(id)) {
			exerciciosRepository.deleteById(id);
			return true;
		}
		return false;
	}
}
