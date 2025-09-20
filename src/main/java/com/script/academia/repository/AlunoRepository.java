package com.script.academia.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.script.academia.entities.Aluno;
import com.script.academia.entities.Usuario;

@Repository
public interface AlunoRepository extends JpaRepository<Aluno, Long> {
	List<Aluno> findByNomeContainingIgnoreCase(String nome);
	Optional<Aluno>findByEmail(String email);
	Optional<Aluno> findByUsuario(Usuario usuario);
	
	

}
