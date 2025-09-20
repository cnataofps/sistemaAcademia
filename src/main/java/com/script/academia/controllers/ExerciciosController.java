package com.script.academia.controllers;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.script.academia.entities.Exercicios;
import com.script.academia.entities.Treino;
import com.script.academia.repository.ExerciciosRepository;
import com.script.academia.repository.TreinoRepository;
import com.script.academia.security.UsuarioDetalhes;
import jakarta.validation.Valid;

@Controller
public class ExerciciosController {
	
	@Autowired
    private TreinoRepository treinoRepository;

    @Autowired
    private ExerciciosRepository exerciciosRepository;

    // Método auxiliar para adicionar o nome do usuário logado ao modelo
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
    public String salvarExercicio(@Valid Exercicios exercicio, BindingResult result, @RequestParam Long treinoId, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            attributes.addFlashAttribute("Mensagem", "Preencha os campos corretamente.");
            return "redirect:/cadastrarExercicio";
        }

        Treino treino = treinoRepository.findById(treinoId).orElseThrow();
        exercicio.setTreino(treino); // 🔗 vínculo direto
        exerciciosRepository.save(exercicio);

        attributes.addFlashAttribute("Mensagem", "Exercício cadastrado com sucesso!");
        return "redirect:/cadastrarExercicio";
    }
    

    @GetMapping("/listarExercicios")
    public String listarExercicios(Model model) {
        adicionarNomeUsuario(model);
        List<Exercicios> exercicios = exerciciosRepository.findAll();
        model.addAttribute("exercicios", exercicios);
        return "exercicios/listarExercicios";
    }

    @GetMapping("/exercicios/{id}")
    public String buscarPorId(@PathVariable Long id, Model model) {
        Optional<Exercicios> exercicio = exerciciosRepository.findById(id);
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
        List<Exercicios> exercicios = exerciciosRepository.findByNomeContainingIgnoreCase(nome);
        model.addAttribute("exercicios", exercicios);
        return "exercicios/listarExercicios";
    }

    @PostMapping("/editarExercicios/{id}")
    public String editarExercicio(@PathVariable Long id, @Valid Exercicios exercicioAtualizado, BindingResult result,
            RedirectAttributes attributes) {
        if (result.hasErrors()) {
            attributes.addFlashAttribute("Mensagem", "Verifique os campos...");
            return "redirect:/exercicios/" + id;
        }

        Optional<Exercicios> exerciciosExistente = exerciciosRepository.findById(id);
        if (exerciciosExistente.isPresent()) {
            Exercicios exercicios = exerciciosExistente.get();
            exercicios.setNome(exercicioAtualizado.getNome());
            exercicios.setSerie(exercicioAtualizado.getSerie());
            exercicios.setRepeticoes(exercicioAtualizado.getRepeticoes());
            exercicios.setCarga(exercicioAtualizado.getCarga());
            exercicios.setObservacao(exercicioAtualizado.getObservacao());

            exerciciosRepository.save(exercicios);
            attributes.addFlashAttribute("Mensagem", "Exercício atualizado com sucesso!");
        } else {
            attributes.addFlashAttribute("Mensagem", "Exercício não encontrado");
        }

        return "redirect:/listarExercicios";
    }

    @GetMapping("/editarExercicios/{id}")
    public String exibirFormularioEdicao(@PathVariable Long id, Model model) {
        Optional<Exercicios> exercicio = exerciciosRepository.findById(id);
        if (exercicio.isPresent()) {
            adicionarNomeUsuario(model);
            model.addAttribute("exercicio", exercicio.get());
            return "exercicios/editarExercicios";
        }
        return "redirect:/listarExercicios";
    }

    @GetMapping("/deletarExercicio/{id}")
    public String deletarExercicio(@PathVariable Long id, RedirectAttributes attributes) {
        if (exerciciosRepository.existsById(id)) {
            exerciciosRepository.deleteById(id);
            attributes.addFlashAttribute("Mensagem", "Exercício deletado com sucesso!");
        } else {
            attributes.addFlashAttribute("Mensagem", "Exercício não encontrado");
        }

        return "redirect:/listarExercicios";
    }
}
