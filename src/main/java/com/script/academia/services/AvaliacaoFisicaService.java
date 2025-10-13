package com.script.academia.services;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.script.academia.entities.Aluno;
import com.script.academia.entities.AvaliacaoFisica;
import com.script.academia.entities.Usuario;
import com.script.academia.repository.AlunoRepository;
import com.script.academia.repository.AvaliacaoFisicaRepository;

@Service
public class AvaliacaoFisicaService {

    @Autowired
    private AvaliacaoFisicaRepository avaliacaoFisicaRepository;

    @Autowired
    private AlunoRepository alunoRepository;

    public void salvarAvaliacao(AvaliacaoFisica avaliacao, Usuario professor) {
        avaliacao.setProfessor(professor);
        avaliacaoFisicaRepository.save(avaliacao);
    }

    public List<AvaliacaoFisica> listarTodas() {
        return avaliacaoFisicaRepository.findAll();
    }

    public Optional<AvaliacaoFisica> buscarPorId(Long id) {
        return avaliacaoFisicaRepository.findById(id);
    }

    public List<AvaliacaoFisica> buscarPorNomeAluno(String nome) {
        return avaliacaoFisicaRepository.findByAluno_NomeContainingIgnoreCase(nome);
    }

    public void atualizarAvaliacao(Long id, AvaliacaoFisica nova, Usuario professor) {
        Optional<AvaliacaoFisica> existente = avaliacaoFisicaRepository.findById(id);
        if (existente.isPresent()) {
            AvaliacaoFisica avaliacao = existente.get();
            // Atualiza todos os campos (pode ser extraído para um método auxiliar)
            BeanUtils.copyProperties(nova, avaliacao, "id", "aluno", "professor");

            if (nova.getAluno() != null && nova.getAluno().getId() != null) {
                Aluno aluno = alunoRepository.findById(nova.getAluno().getId()).orElse(null);
                avaliacao.setAluno(aluno);
            }

            avaliacao.setProfessor(professor);
            avaliacaoFisicaRepository.save(avaliacao);
        }
    }

    public boolean deletar(Long id) {
        if (avaliacaoFisicaRepository.existsById(id)) {
            avaliacaoFisicaRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<AvaliacaoFisica> buscarPorAlunoId(Long alunoId) {
        return avaliacaoFisicaRepository.findByAlunoIdOrderByDataDesc(alunoId);
    }
}
