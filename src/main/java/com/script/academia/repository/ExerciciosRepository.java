package com.script.academia.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.script.academia.entities.Aluno;
import com.script.academia.entities.Exercicios;

@Repository
public interface ExerciciosRepository extends JpaRepository<Exercicios, Long> {
	List<Exercicios> findByNomeContainingIgnoreCase(String nome);

	@Query("SELECT e FROM Exercicios e WHERE e.treino.aluno = :aluno")
	List<Exercicios> findByAluno(@Param("aluno") Aluno aluno);

}
