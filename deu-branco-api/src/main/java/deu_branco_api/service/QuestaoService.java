package deu_branco_api.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import deu_branco_api.model.Disciplina;
import deu_branco_api.model.Instituicao;
import deu_branco_api.model.Questao;
import deu_branco_api.repository.QuestaoRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class QuestaoService {

    private final QuestaoRepository questaoRepository;

    public QuestaoService(QuestaoRepository questaoRepository) {
        this.questaoRepository = questaoRepository;
    }

    @Transactional
    public Questao criar(Questao questao) {
        prepararParaCriacao(questao);

        if (questaoRepository.existsByEnunciadoAndInstituicao(questao.getEnunciado(), questao.getInstituicao())) {
            throw new IllegalArgumentException("Ja existe uma pergunta cadastrada para este enunciado e instituicao.");
        }

        return questaoRepository.save(questao);
    }

    @Transactional(readOnly = true)
    public Questao buscarPorId(Long id) {
        return questaoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pergunta nao encontrada com id: " + id));
    }

    @Transactional(readOnly = true)
    public Page<Questao> listar(String disciplina, String instituicao, Boolean ativo, Pageable pageable) {
        Disciplina disciplinaNormalizada = normalizarDisciplinaOpcional(disciplina);
        Instituicao instituicaoNormalizada = normalizarInstituicaoOpcional(instituicao);

        return questaoRepository.buscarComFiltros(disciplinaNormalizada, instituicaoNormalizada, ativo, pageable);
    }

    @Transactional
    public Questao atualizar(Long id, Questao dadosAtualizados) {
        Questao questao = buscarPorId(id);
        prepararParaAtualizacao(questao, dadosAtualizados);

        if (questaoRepository.existsByEnunciadoAndInstituicaoAndIdNot(
                questao.getEnunciado(), questao.getInstituicao(), questao.getId())) {
            throw new IllegalArgumentException("Ja existe uma pergunta cadastrada para este enunciado e instituicao.");
        }

        return questaoRepository.save(questao);
    }

    @Transactional
    public void remover(Long id) {
        Questao questao = buscarPorId(id);
        questao.setAtivo(false);
        questaoRepository.save(questao);
    }

    private void prepararParaCriacao(Questao questao) {
        if (questao == null) {
            throw new IllegalArgumentException("Questao nao pode ser nula.");
        }

        questao.setId(null);
        questao.setEnunciado(normalizarTextoObrigatorio(questao.getEnunciado(), "Enunciado da pergunta e obrigatorio."));
        questao.setOpcaoA(normalizarTextoObrigatorio(questao.getOpcaoA(), "Opcao A e obrigatoria."));
        questao.setOpcaoB(normalizarTextoObrigatorio(questao.getOpcaoB(), "Opcao B e obrigatoria."));
        questao.setOpcaoC(normalizarTextoObrigatorio(questao.getOpcaoC(), "Opcao C e obrigatoria."));
        questao.setOpcaoD(normalizarTextoObrigatorio(questao.getOpcaoD(), "Opcao D e obrigatoria."));
        questao.setOpcaoE(normalizarTextoObrigatorio(questao.getOpcaoE(), "Opcao E e obrigatoria."));
        questao.setAlternativaCorreta(normalizarAlternativaCorreta(questao.getAlternativaCorreta()));
        questao.setDisciplina(normalizarDisciplinaObrigatoria(questao.getDisciplina()));
        questao.setInstituicao(normalizarInstituicaoObrigatoria(questao.getInstituicao()));
        questao.setImagens(normalizarImagens(questao.getImagens()));

        if (questao.getAtivo() == null) {
            questao.setAtivo(true);
        }
    }

    private void prepararParaAtualizacao(Questao questao, Questao dadosAtualizados) {
        if (dadosAtualizados == null) {
            throw new IllegalArgumentException("Dados da pergunta nao podem ser nulos.");
        }

        questao.setEnunciado(normalizarTextoObrigatorio(
                dadosAtualizados.getEnunciado(), "Enunciado da pergunta e obrigatorio."));
        questao.setOpcaoA(normalizarTextoObrigatorio(dadosAtualizados.getOpcaoA(), "Opcao A e obrigatoria."));
        questao.setOpcaoB(normalizarTextoObrigatorio(dadosAtualizados.getOpcaoB(), "Opcao B e obrigatoria."));
        questao.setOpcaoC(normalizarTextoObrigatorio(dadosAtualizados.getOpcaoC(), "Opcao C e obrigatoria."));
        questao.setOpcaoD(normalizarTextoObrigatorio(dadosAtualizados.getOpcaoD(), "Opcao D e obrigatoria."));
        questao.setOpcaoE(normalizarTextoObrigatorio(dadosAtualizados.getOpcaoE(), "Opcao E e obrigatoria."));
        questao.setAlternativaCorreta(normalizarAlternativaCorreta(dadosAtualizados.getAlternativaCorreta()));
        questao.setDisciplina(normalizarDisciplinaObrigatoria(dadosAtualizados.getDisciplina()));
        questao.setInstituicao(normalizarInstituicaoObrigatoria(dadosAtualizados.getInstituicao()));
        questao.setImagens(normalizarImagens(dadosAtualizados.getImagens()));

        if (dadosAtualizados.getAtivo() != null) {
            questao.setAtivo(dadosAtualizados.getAtivo());
        }
    }

    private String normalizarAlternativaCorreta(String alternativaCorreta) {
        String valor = normalizarTextoObrigatorio(alternativaCorreta, "Alternativa correta e obrigatoria.").toUpperCase();

        if (valor.length() != 1) {
            throw new IllegalArgumentException("Alternativa correta deve possuir apenas um caractere.");
        }

        if (!Set.of("A", "B", "C", "D", "E").contains(valor)) {
            throw new IllegalArgumentException("Alternativa correta deve ser A, B, C, D ou E.");
        }

        return valor;
    }

    private Disciplina normalizarDisciplinaObrigatoria(Disciplina disciplina) {
        if (disciplina == null) {
            throw new IllegalArgumentException("Disciplina da pergunta e obrigatoria.");
        }

        return disciplina;
    }

    private Disciplina normalizarDisciplinaOpcional(String disciplina) {
        if (disciplina == null || disciplina.isBlank()) {
            return null;
        }

        return Disciplina.from(disciplina);
    }

    private Instituicao normalizarInstituicaoObrigatoria(Instituicao instituicao) {
        if (instituicao == null) {
            throw new IllegalArgumentException("Instituicao da pergunta e obrigatoria.");
        }

        return instituicao;
    }

    private Instituicao normalizarInstituicaoOpcional(String instituicao) {
        if (instituicao == null || instituicao.isBlank()) {
            return null;
        }

        return Instituicao.from(instituicao);
    }

    private List<String> normalizarImagens(List<String> imagens) {
        if (imagens == null || imagens.isEmpty()) {
            return new ArrayList<>();
        }

        return imagens.stream()
                .map(this::normalizarTextoOpcional)
                .filter(imagem -> imagem != null)
                .toList();
    }

    private String normalizarTextoObrigatorio(String texto, String mensagem) {
        String valor = normalizarTextoOpcional(texto);

        if (valor == null) {
            throw new IllegalArgumentException(mensagem);
        }

        return valor;
    }

    private String normalizarTextoOpcional(String texto) {
        if (texto == null || texto.isBlank()) {
            return null;
        }

        return texto.trim();
    }

}
