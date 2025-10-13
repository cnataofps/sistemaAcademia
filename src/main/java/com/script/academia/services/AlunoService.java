package com.script.academia.services;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.script.academia.entities.Aluno;
import com.script.academia.entities.Perfil;
import com.script.academia.entities.Usuario;
import com.script.academia.repository.AlunoRepository;
import com.script.academia.repository.UsuarioRepository;

@Service
public class AlunoService {

	@Autowired
	private AlunoRepository alunoRepository;
	@Autowired
	private UsuarioRepository usuarioRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;

	public void salvarAluno(Aluno aluno, String senha) {
		alunoRepository.save(aluno);

		Usuario usuario = new Usuario();
		usuario.setNome(aluno.getNome());
		usuario.setEmail(aluno.getEmail());
		usuario.setSenha(passwordEncoder.encode(senha));
		usuario.setPerfil(Perfil.ALUNO);
		usuario.setAluno(aluno);

		usuarioRepository.save(usuario);

		aluno.setUsuario(usuario);
		alunoRepository.save(aluno);
	}

	public List<Aluno> listarTodos() {
		return alunoRepository.findAll();
	}

	public Optional<Aluno> buscarPorId(Long id) {
		return alunoRepository.findById(id);
	}

	public List<Aluno> buscarPorNome(String nome) {
		return alunoRepository.findByNomeContainingIgnoreCase(nome);
	}

	public void editarAluno(Long id, Aluno alunoAtualizado) {
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
		}
	}

	public boolean deletarAluno(Long id) {
		if (alunoRepository.existsById(id)) {
			alunoRepository.deleteById(id);
			return true;
		}
		return false;
	}
}
