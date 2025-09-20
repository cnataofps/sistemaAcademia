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
import com.script.academia.entities.FichaDeTreinamento;
import com.script.academia.entities.Treino;
import com.script.academia.repository.AlunoRepository;
import com.script.academia.repository.FichaDeTreinamentoRepository;
import com.script.academia.security.UsuarioDetalhes;
import jakarta.validation.Valid;

@Controller
public class FichaDeTreinamentoController {

    @Autowired
    private FichaDeTreinamentoRepository fichaDeTreinamentoRepository;

    @Autowired
    private AlunoRepository alunoRepository;

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

    @GetMapping("/cadastrarFicha")
    public String exibirFormularioCadastro(Model model) {
        adicionarNomeUsuario(model);
        model.addAttribute("ficha", new FichaDeTreinamento());
        model.addAttribute("alunos", alunoRepository.findAll());
        return "fichas/cadastrarFicha";
    }

    @PostMapping("/cadastrarFicha")
    public String salvarFicha(@Valid FichaDeTreinamento ficha, BindingResult result, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            attributes.addFlashAttribute("Mensagem", "Preencher todos os campos necessário...");
            return "redirect:/cadastrarFicha";
        }

        if (ficha.getTreinos() != null) {
            for (Treino treino : ficha.getTreinos()) {
                treino.setFicha(ficha);
                if (treino.getExercicios() != null) {
                    for (Exercicios exercicios : treino.getExercicios()) {
                        exercicios.setTreino(treino);
                    }
                }
            }
        }

        fichaDeTreinamentoRepository.save(ficha);
        attributes.addFlashAttribute("Mensagem", "Ficha cadastrada com sucesso!");
        return "redirect:/cadastrarFicha";
    }

    @GetMapping("/listarFichas")
    public String listarFichas(Model model) {
        adicionarNomeUsuario(model);
        List<FichaDeTreinamento> fichas = fichaDeTreinamentoRepository.findAll();
        model.addAttribute("fichas", fichas);
        return "fichas/listarFichas";
    }

    @GetMapping("/fichas/{id}")
    public String buscarPorId(@PathVariable Long id, Model model) {
        Optional<FichaDeTreinamento> fichaDeTreinamento = fichaDeTreinamentoRepository.findById(id);
        if (fichaDeTreinamento.isPresent()) {
            adicionarNomeUsuario(model);
            model.addAttribute("fichas", List.of(fichaDeTreinamento.get()));
            return "fichas/listarFichas";
        }
        model.addAttribute("Mensagem", "Ficha não encontrada");
        return "redirect:/listarFichas";
    }

    @GetMapping("/buscarFichas")
    public String buscarPorNome(@RequestParam("nome") String nome, Model model) {
        adicionarNomeUsuario(model);
        List<FichaDeTreinamento> fichas = fichaDeTreinamentoRepository.findByAluno_NomeContainingIgnoreCase(nome);
        model.addAttribute("fichas", fichas);
        return "fichas/listarFichas";
    }

    @GetMapping("/editarFicha/{id}")
    public String exibirFormularioEdicao(@PathVariable Long id, Model model) {
        Optional<FichaDeTreinamento> fichaDeTreinamento = fichaDeTreinamentoRepository.findById(id);
        if (fichaDeTreinamento.isPresent()) {
            adicionarNomeUsuario(model);
            model.addAttribute("ficha", fichaDeTreinamento.get());
            model.addAttribute("alunos", alunoRepository.findAll());
            return "fichas/editarFicha";
        }
        return "redirect:/listarFichas";
    }

    @PostMapping("/editarFicha/{id}")
    public String editarFicha(@PathVariable Long id, @Valid FichaDeTreinamento fichaAtualizada, BindingResult result,
            RedirectAttributes attributes) {
        if (result.hasErrors()) {
            attributes.addFlashAttribute("Mensagem", "Verifique os campos...");
            return "redirect:/editarFicha/" + id;
        }

        Optional<FichaDeTreinamento> fichaDeTreinamentoExistente = fichaDeTreinamentoRepository.findById(id);
        if (fichaDeTreinamentoExistente.isPresent()) {
            FichaDeTreinamento fichaDeTreinamento = fichaDeTreinamentoExistente.get();
            fichaDeTreinamento.setObjetivo(fichaAtualizada.getObjetivo());
            fichaDeTreinamento.setDataCriacao(fichaAtualizada.getDataCriacao());
            fichaDeTreinamento.setDataTroca(fichaAtualizada.getDataTroca());

            fichaDeTreinamentoRepository.save(fichaDeTreinamento);
            attributes.addFlashAttribute("Mensagem", "Ficha de Treino atualizado com sucesso!");
        } else {
            attributes.addFlashAttribute("Mensagem", "Ficha de Treino não encontrado");
        }

        return "redirect:/listarFichas";
    }

    @GetMapping("/deletarFicha/{id}")
    public String deletarFicha(@PathVariable Long id, RedirectAttributes attributes) {
        if (fichaDeTreinamentoRepository.existsById(id)) {
            fichaDeTreinamentoRepository.deleteById(id);
            attributes.addFlashAttribute("Mensagem", "Ficha de Treino deletado com sucesso!");
        } else {
            attributes.addFlashAttribute("Mensagem", "Ficha de Treino não encontrado");
        }

        return "redirect:/listarFichas";
    }
}
