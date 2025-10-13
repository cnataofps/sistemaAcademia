package com.script.academia.services;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.script.academia.entities.Aluno;
import com.script.academia.entities.Exercicios;
import com.script.academia.entities.FichaDeTreinamento;
import com.script.academia.entities.Treino;
import com.script.academia.repository.AlunoRepository;
import com.script.academia.repository.FichaDeTreinamentoRepository;
import com.script.academia.repository.TreinoRepository;

@Service
public class TreinoService {

    @Autowired
    private TreinoRepository treinoRepository;

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private FichaDeTreinamentoRepository fichaRepository;

    public void salvarFichaCompleta(FichaDeTreinamento ficha, Long alunoId) {
        Aluno aluno = alunoRepository.findById(alunoId).orElseThrow();
        ficha.setAluno(aluno);

        if (ficha.getTreinos() != null) {
            for (Treino treino : ficha.getTreinos()) {
                treino.setAluno(aluno);
                treino.setFicha(ficha);
                if (treino.getExercicios() != null) {
                    for (Exercicios exercicio : treino.getExercicios()) {
                        exercicio.setTreino(treino);
                    }
                }
            }
        }

        fichaRepository.save(ficha);
    }

    public List<Treino> listarTodos() {
        return treinoRepository.findAll();
    }

    public Optional<Treino> buscarPorId(Long id) {
        return treinoRepository.findById(id);
    }

    public List<Treino> buscarPorNome(String nome) {
        return treinoRepository.findByNomeContainingIgnoreCase(nome);
    }

    public boolean editarTreino(Long id, Treino atualizado) {
        Optional<Treino> treinoOpt = treinoRepository.findById(id);
        if (treinoOpt.isPresent()) {
            Treino treino = treinoOpt.get();
            treino.setNome(atualizado.getNome());
            // Adicione outros campos conforme necessário
            treinoRepository.save(treino);
            return true;
        }
        return false;
    }

    public boolean deletarTreino(Long id) {
        if (treinoRepository.existsById(id)) {
            treinoRepository.deleteById(id);
            return true;
        }
        return false;
    }
}