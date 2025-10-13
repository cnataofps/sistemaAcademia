package com.script.academia.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.script.academia.entities.Exercicios;
import com.script.academia.security.UsuarioDetalhes;
import com.script.academia.services.ExerciciosService;

import jakarta.validation.Valid;
@Controller
public class ExerciciosController {

    @Autowired
    private ExerciciosService exerciciosService;

    private void adicionarNomeUsuario(Model model) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            UsuarioDetalhes detalhes = (UsuarioDetalhes) auth.getPrincipal();
            model.addAttribute("nome", detalhes.getUsuario().getNome());
        } catch (Exception e) {
            model.addAttribute("nome", "Usuário");
        }
    }

    @GetMapping("/cadastrarExercicios")
    public String cadastrarExercicio(Model model) {
        adicionarNomeUsuario(model);
        model.addAttribute("exercicio", new Exercicios());
        return "exercicios/cadastrarExercicios";
    }

    @PostMapping("/cadastrarExercicio")
    public String salvarExercicio(@Valid Exercicios exercicio, BindingResult result,
                                  @RequestParam Long treinoId, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            attributes.addFlashAttribute("Mensagem", "Preencha os campos corretamente.");
            return "redirect:/cadastrarExercicio";
        }

        exerciciosService.salvar(exercicio, treinoId);
        attributes.addFlashAttribute("Mensagem", "Exercício cadastrado com sucesso!");
        return "redirect:/cadastrarExercicio";
    }

    @GetMapping("/listarExercicios")
    public String listarExercicios(Model model) {
        adicionarNomeUsuario(model);
        model.addAttribute("exercicios", exerciciosService.listarTodos());
        return "exercicios/listarExercicios";
    }

    @GetMapping("/exercicios/{id}")
    public String buscarPorId(@PathVariable Long id, Model model) {
        Optional<Exercicios> exercicio = exerciciosService.buscarPorId(id);
        if (exercicio.isPresent()) {
            adicionarNomeUsuario(model);
            model.addAttribute("exercicio", exercicio.get());
            return "exercicios/listarExercicios";
        }
        model.addAttribute("Mensagem", "Exercício não encontrado");
        return "redirect:/listarExercicios";
    }

    @GetMapping("/buscarExercicios")
    public String buscarPorNome(@RequestParam("nome") String nome, Model model) {
        adicionarNomeUsuario(model);
        model.addAttribute("exercicios", exerciciosService.buscarPorNome(nome));
        return "exercicios/listarExercicios";
    }

    @PostMapping("/editarExercicios/{id}")
    public String editarExercicio(@PathVariable Long id, @Valid Exercicios exercicioAtualizado,
                                  BindingResult result, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            attributes.addFlashAttribute("Mensagem", "Verifique os campos...");
            return "redirect:/exercicios/" + id;
        }

        boolean atualizado = exerciciosService.editar(id, exercicioAtualizado);
        attributes.addFlashAttribute("Mensagem", atualizado ? "Exercício atualizado com sucesso!" : "Exercício não encontrado");
        return "redirect:/listarExercicios";
    }

    @GetMapping("/editarExercicios/{id}")
    public String exibirFormularioEdicao(@PathVariable Long id, Model model) {
        Optional<Exercicios> exercicio = exerciciosService.buscarPorId(id);
        if (exercicio.isPresent()) {
            adicionarNomeUsuario(model);
            model.addAttribute("exercicio", exercicio.get());
            return "exercicios/editarExercicios";
        }
        return "redirect:/listarExercicios";
    }

    @GetMapping("/deletarExercicio/{id}")
    public String deletarExercicio(@PathVariable Long id, RedirectAttributes attributes) {
        boolean deletado = exerciciosService.deletar(id);
        attributes.addFlashAttribute("Mensagem", deletado ? "Exercício deletado com sucesso!" : "Exercício não encontrado");
        return "redirect:/listarExercicios";
    }
}
