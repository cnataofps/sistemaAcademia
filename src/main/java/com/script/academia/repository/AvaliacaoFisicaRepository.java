package com.script.academia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.script.academia.entities.Aluno;
import com.script.academia.entities.AvaliacaoFisica;
import java.util.List;

@Repository
public interface AvaliacaoFisicaRepository extends JpaRepository<AvaliacaoFisica, Long> {
	List<AvaliacaoFisica> findByAluno_NomeContainingIgnoreCase(String nome);
	AvaliacaoFisica findTopByAlunoOrderByDataDesc(Aluno aluno);
	List<AvaliacaoFisica> findByAlunoOrderByDataDesc(Aluno aluno);

}
