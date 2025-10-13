package com.script.academia.controllers;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.script.academia.entities.FichaDeTreinamento;
import com.script.academia.entities.Treino;
import com.script.academia.repository.AlunoRepository;
import com.script.academia.security.UsuarioDetalhes;
import com.script.academia.services.TreinoService;

import jakarta.validation.Valid;

@Controller
public class TreinoController {

    @Autowired
    private TreinoService treinoService;

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

    @GetMapping("/cadastrarTreino")
    public String exibirFormularioTreino(Model model) {
        model.addAttribute("treino", new Treino());
        model.addAttribute("alunos", alunoRepository.findAll());
        return "treinos/cadastrarTreino";
    }

    @GetMapping("/cadastrarTreinos")
    public String cadastrarTreino(Model model) {
        adicionarNomeUsuario(model);
        model.addAttribute("treino", new Treino());
        return "treinos/cadastrarTreinos";
    }

    @PostMapping("/cadastrarFichaCompleta")
    public String salvarFicha(@ModelAttribute FichaDeTreinamento ficha,
                              @RequestParam("alunoId") Long alunoId,
                              RedirectAttributes attributes) {
        treinoService.salvarFichaCompleta(ficha, alunoId);
        attributes.addFlashAttribute("Mensagem", "Ficha cadastrada com sucesso!");
        return "redirect:/cadastrarFicha";
    }

    @GetMapping("/listarTreinos")
    public String listarTreinos(Model model) {
        adicionarNomeUsuario(model);
        model.addAttribute("treinos", treinoService.listarTodos());
        return "treinos/listarTreinos";
    }

    @GetMapping("/treinos/{id}")
    public String buscarPorId(@PathVariable Long id, Model model) {
        Optional<Treino> treino = treinoService.buscarPorId(id);
        if (treino.isPresent()) {
            adicionarNomeUsuario(model);
            model.addAttribute("treino", treino.get());
            return "treinos/listarTreinos";
        }
        model.addAttribute("Mensagem", "Treino não encontrado");
        return "redirect:/listarTreinos";
    }

    @GetMapping("/buscarTreinos")
    public String buscarPorNome(@RequestParam("nome") String nome, Model model) {
        adicionarNomeUsuario(model);
        model.addAttribute("treinos", treinoService.buscarPorNome(nome));
        return "treinos/listarTreinos";
    }

    @GetMapping("/editarTreinos/{id}")
    public String exibirFormularioEdicao(@PathVariable Long id, Model model) {
        Optional<Treino> treino = treinoService.buscarPorId(id);
        if (treino.isPresent()) {
            adicionarNomeUsuario(model);
            model.addAttribute("treino", treino.get());
            return "treinos/editarTreinos";
        }
        return "redirect:/listarTreinos";
    }

    @PostMapping("/editarTreinos/{id}")
    public String editarTreino(@PathVariable Long id, @Valid Treino treinoAtualizado,
                                BindingResult result, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            attributes.addFlashAttribute("Mensagem", "Verifique os campos...");
            return "redirect:/editarTreinos/" + id;
        }

        boolean atualizado = treinoService.editarTreino(id, treinoAtualizado);
        attributes.addFlashAttribute("Mensagem", atualizado ? "Treino atualizado com sucesso!" : "Treino não encontrado");
        return "redirect:/listarTreinos";
    }

    @GetMapping("/deletarTreino/{id}")
    public String deletarTreino(@PathVariable Long id, RedirectAttributes attributes) {
        boolean deletado = treinoService.deletarTreino(id);
        attributes.addFlashAttribute("Mensagem", deletado ? "Treino deletado com sucesso!" : "Treino não encontrado");
        return "redirect:/listarTreinos";
    }
}

