package com.script.academia.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.script.academia.entities.Aluno;
import com.script.academia.entities.Perfil;
import com.script.academia.entities.Usuario;
import com.script.academia.repository.AlunoRepository;
import com.script.academia.security.UsuarioDetalhes;
import com.script.academia.services.UsuarioService;

@Controller
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private AlunoRepository alunoRepository;

    private void adicionarNomeUsuario(Model model) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            UsuarioDetalhes detalhes = (UsuarioDetalhes) auth.getPrincipal();
            model.addAttribute("nome", detalhes.getUsuario().getNome());
        } catch (Exception e) {
            model.addAttribute("nome", "Usuário");
        }
    }

    @GetMapping("/cadastrarUsuario")
    public String cadastro() {
        return "login/cadastro";
    }

    @PostMapping("/cadastrarUsuario")
    public String cadastrar(@ModelAttribute Usuario usuario, @RequestParam("perfil") String perfilStr) {
        if (usuarioService.emailJaExiste(usuario.getEmail())) {
            return "redirect:/cadastrarUsuario?erro=email";
        }

        usuarioService.cadastrarUsuario(usuario, perfilStr);
        return "redirect:/login?cadastro=sucesso";
    }

    @PostMapping("/usuario/alterar")
    public String alterarDados(@RequestParam String nome,
                                @RequestParam String email,
                                @RequestParam(required = false) String endereco,
                                @RequestParam(required = false) String profissao,
                                @RequestParam(required = false) String novaSenha,
                                @RequestParam(required = false) String confirmarSenha,
                                @AuthenticationPrincipal UsuarioDetalhes usuarioDetalhes,
                                RedirectAttributes redirectAttributes) {

        if (usuarioDetalhes == null || usuarioDetalhes.getUsuario() == null) {
            redirectAttributes.addFlashAttribute("erro", "Usuário não autenticado.");
            return "redirect:/alterar";
        }

        Usuario usuario = usuarioDetalhes.getUsuario();
        String erro = usuarioService.alterarDados(usuario, nome, email, endereco, profissao, novaSenha, confirmarSenha);

        if (erro != null) {
            redirectAttributes.addFlashAttribute("erro", erro);
            return "redirect:/alterar";
        }

        redirectAttributes.addFlashAttribute("sucesso", "Dados atualizados com sucesso!");
        return "redirect:/alterarDados";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/listarUsuario")
    public String listarUsuarios(Model model) {
        adicionarNomeUsuario(model);
        model.addAttribute("mensagens", usuarioService.listarTodos());
        return "usuario/listarUsuario";
    }

    @GetMapping("/alterarDados")
    public String exibirFormularioAlteracao(Model model, @AuthenticationPrincipal UsuarioDetalhes usuarioDetalhes) {
        adicionarNomeUsuario(model);

        if (usuarioDetalhes == null || usuarioDetalhes.getUsuario() == null) {
            model.addAttribute("erro", "Usuário não autenticado.");
            return "redirect:/login";
        }

        Usuario usuario = usuarioDetalhes.getUsuario();
        model.addAttribute("usuario", usuario);

        if (usuario.getPerfil() == Perfil.PROFESSOR) {
            model.addAttribute("tipo", "professor");
            return "usuario/alterarProfessor";
        } else {
            Aluno aluno = alunoRepository.findByUsuario(usuario).orElse(null);
            model.addAttribute("aluno", aluno);
            model.addAttribute("tipo", "aluno");
            return "usuario/alterarAluno";
        }
    }
}
