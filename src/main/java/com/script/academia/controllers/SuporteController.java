package com.script.academia.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.script.academia.entities.Usuario;
import com.script.academia.security.UsuarioDetalhes;
import com.script.academia.services.SuporteService;

@Controller
public class SuporteController {

    @Autowired
    private SuporteService suporteService;

    private void adicionarNomeUsuario(Model model) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            UsuarioDetalhes detalhes = (UsuarioDetalhes) auth.getPrincipal();
            model.addAttribute("nome", detalhes.getUsuario().getNome());
        } catch (Exception e) {
            model.addAttribute("nome", "Usuário");
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/listarSuporte")
    public String listarMensagensDeSuporte(Model model) {
        adicionarNomeUsuario(model);
        model.addAttribute("mensagens", suporteService.listarTodas());
        return "suporte/listarMensagens";
    }

    @PreAuthorize("hasAnyRole('PROFESSOR', 'ADMIN', 'ALUNO')")
    @PostMapping("/suporte/enviar")
    public String enviarSuporte(@RequestParam("assunto") String assunto,
                                @RequestParam("mensagem") String mensagem,
                                RedirectAttributes attributes) {

        Usuario usuarioLogado = ((UsuarioDetalhes) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal()).getUsuario();

        suporteService.enviarMensagem(assunto, mensagem, usuarioLogado);
        attributes.addFlashAttribute("Mensagem", "Sua mensagem foi enviada com sucesso!");
        return "redirect:/suporte";
    }
}

