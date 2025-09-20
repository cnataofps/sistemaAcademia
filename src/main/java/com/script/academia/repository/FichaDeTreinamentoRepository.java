package com.script.academia.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.script.academia.entities.Aluno;
import com.script.academia.entities.FichaDeTreinamento;



@Repository
public interface FichaDeTreinamentoRepository extends JpaRepository<FichaDeTreinamento, Long> {
	List<FichaDeTreinamento> findByAluno_NomeContainingIgnoreCase(String nome);
	List<FichaDeTreinamento> findByAluno(Aluno aluno);
	FichaDeTreinamento findTopByAlunoOrderByDataCriacaoDesc(Aluno aluno);

}
