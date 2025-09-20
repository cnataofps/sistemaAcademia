package com.script.academia.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.script.academia.entities.Perfil;
import com.script.academia.entities.Usuario;
import com.script.academia.repository.UsuarioRepository;
import com.script.academia.security.UsuarioDetalhes;

@Service
public class UsuarioService implements UserDetailsService, CommandLineRunner {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
	    return usuarioRepository.findByEmail(email)
	        .map(UsuarioDetalhes::new)
	        .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
	}

	@Override
	public void run(String... args) throws Exception {
		if (usuarioRepository.findByEmail("admin@academia.com").isEmpty()) {
			Usuario admin = new Usuario();
			admin.setEmail("admin@academia.com");
			admin.setSenha(passwordEncoder.encode("123456"));
			admin.setNome("Administrador");
			admin.setPerfil(Perfil.ADMIN);
			usuarioRepository.save(admin);
			System.out.println("✅ Usuário ADMIN criado com sucesso.");
		}
	}
}