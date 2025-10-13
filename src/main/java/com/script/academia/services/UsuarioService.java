package com.script.academia.services;

import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.script.academia.entities.Aluno;
import com.script.academia.entities.Perfil;
import com.script.academia.entities.Usuario;
import com.script.academia.repository.AlunoRepository;
import com.script.academia.repository.UsuarioRepository;
import com.script.academia.security.UsuarioDetalhes;

@Service
public class UsuarioService implements UserDetailsService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private AlunoRepository alunoRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	// 🔐 Necessário para autenticação do Spring Security
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		return usuarioRepository.findByEmail(email).map(UsuarioDetalhes::new)
				.orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
	}

	// ✅ Métodos de negócio
	public boolean emailJaExiste(String email) {
		return usuarioRepository.findByEmail(email).isPresent();
	}

	public void cadastrarUsuario(Usuario usuario, String perfilStr) {
		usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
		usuario.setPerfil(Perfil.valueOf(perfilStr));
		usuarioRepository.save(usuario);
	}

	public String alterarDados(Usuario usuario, String nome, String email, String endereco, String profissao,
			String novaSenha, String confirmarSenha) {

		Optional<Usuario> existente = usuarioRepository.findByEmail(email);
		if (existente.isPresent() && !Objects.equals(existente.get().getId(), usuario.getId())) {
			return "E-mail já está em uso por outro usuário.";
		}

		usuario.setNome(nome);
		usuario.setEmail(email);

		if (novaSenha != null && !novaSenha.isBlank()) {
			if (!novaSenha.equals(confirmarSenha)) {
				return "As senhas não coincidem.";
			}
			usuario.setSenha(passwordEncoder.encode(novaSenha));
		}

		usuarioRepository.save(usuario);

		if (usuario.getPerfil() == Perfil.ALUNO) {
			Aluno aluno = alunoRepository.findByUsuario(usuario).orElseThrow();
			aluno.setNome(nome);
			aluno.setEmail(email);
			aluno.setEndereco(endereco);
			aluno.setProfissao(profissao);
			alunoRepository.save(aluno);
		}

		return null; // sucesso
	}

	public Iterable<Usuario> listarTodos() {
		return usuarioRepository.findAll();
	}
}
