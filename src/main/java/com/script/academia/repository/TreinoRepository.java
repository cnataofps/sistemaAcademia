package com.script.academia.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.script.academia.entities.Aluno;
import com.script.academia.entities.Treino;


@Repository
public interface TreinoRepository extends JpaRepository<Treino, Long> {
    List<Treino> findByNomeContainingIgnoreCase(String nome);
    List<Treino> findByAluno(Aluno aluno);
}