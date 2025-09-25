package com.script.academia.entities;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;

@Entity
public class AvaliacaoFisica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate data;

    @NotNull(message = "Data de troca é obrigatória")
    private LocalDate dataTroca;

    private String objetivo;

    @Column(columnDefinition = "TEXT")
    private String observacao;

    // PERIMETRIA
    private String torax;
    private String abdominal;
    private String quadril;
    private String antebraco;
    private String bracorelaxado;
    private String bracocontraido;
    private String coxasuperior;
    private String panturilha;

    // ANTROPOMETRIA
    private String tricipal;
    private String subescapular;
    private String peitoral;
    private String abdomen;
    private String suprailiaca;
    private String coxa;
    private String axialmedia;

    private Double peso;
    private Double altura;
    private Double imc;
    private String percentualgordura;
    private String massamagra;
    private String massagorda;

    // HISTÓRICO PESSOAL
    private String problemaArticular;
    private String cirurgia;
    private String coluna;

    // HISTÓRICO FAMILIAR
    private String cardiopata;
    private String hipertenso;
    private String diabetico;

    // HÁBITOS PESSOAIS
    private String fuma;
    private String bebe;
    private String toxico;

    // ATIVIDADE FÍSICA
    private String jaFezMusculacao;
    private String tempoMusculacao;
    private String inativoHa;
    private String frequenciaSemanal;
    private String tempoDisponivel;

    @ManyToOne(optional = false)
    @JoinColumn(name = "aluno_id")
    private Aluno aluno;

    @ManyToOne
    @JoinColumn(name = "professor_id")
    @JsonIgnoreProperties({"senha", "perfil", "email"})
    private Usuario professor;
	
	
	// CONSTRUTOR VAZIO.
	public AvaliacaoFisica() {
		
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public LocalDate getData() {
		return data;
	}


	public void setData(LocalDate data) {
		this.data = data;
	}


	public LocalDate getDataTroca() {
		return dataTroca;
	}


	public void setDataTroca(LocalDate dataTroca) {
		this.dataTroca = dataTroca;
	}


	public String getObjetivo() {
		return objetivo;
	}


	public void setObjetivo(String objetivo) {
		this.objetivo = objetivo;
	}


	public String getObservacao() {
		return observacao;
	}


	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}


	public String getTorax() {
		return torax;
	}


	public void setTorax(String torax) {
		this.torax = torax;
	}


	public String getAbdominal() {
		return abdominal;
	}


	public void setAbdominal(String abdominal) {
		this.abdominal = abdominal;
	}


	public String getQuadril() {
		return quadril;
	}


	public void setQuadril(String quadril) {
		this.quadril = quadril;
	}


	public String getAntebraco() {
		return antebraco;
	}


	public void setAntebraco(String antebraco) {
		this.antebraco = antebraco;
	}


	public String getBracorelaxado() {
		return bracorelaxado;
	}


	public void setBracorelaxado(String bracorelaxado) {
		this.bracorelaxado = bracorelaxado;
	}


	public String getBracocontraido() {
		return bracocontraido;
	}


	public void setBracocontraido(String bracocontraido) {
		this.bracocontraido = bracocontraido;
	}


	public String getCoxasuperior() {
		return coxasuperior;
	}


	public void setCoxasuperior(String coxasuperior) {
		this.coxasuperior = coxasuperior;
	}


	public String getPanturilha() {
		return panturilha;
	}


	public void setPanturilha(String panturilha) {
		this.panturilha = panturilha;
	}


	public String getTricipal() {
		return tricipal;
	}


	public void setTricipal(String tricipal) {
		this.tricipal = tricipal;
	}


	public String getSubescapular() {
		return subescapular;
	}


	public void setSubescapular(String subescapular) {
		this.subescapular = subescapular;
	}


	public String getPeitoral() {
		return peitoral;
	}


	public void setPeitoral(String peitoral) {
		this.peitoral = peitoral;
	}


	public String getAbdomen() {
		return abdomen;
	}


	public void setAbdomen(String abdomen) {
		this.abdomen = abdomen;
	}


	public String getSuprailiaca() {
		return suprailiaca;
	}


	public void setSuprailiaca(String suprailiaca) {
		this.suprailiaca = suprailiaca;
	}


	public String getCoxa() {
		return coxa;
	}


	public void setCoxa(String coxa) {
		this.coxa = coxa;
	}


	public String getAxialmedia() {
		return axialmedia;
	}


	public void setAxialmedia(String axialmedia) {
		this.axialmedia = axialmedia;
	}


	public Double getPeso() {
		return peso;
	}


	public void setPeso(Double peso) {
		this.peso = peso;
	}


	public Double getAltura() {
		return altura;
	}


	public void setAltura(Double altura) {
		this.altura = altura;
	}


	public Double getImc() {
		return imc;
	}


	public void setImc(Double imc) {
		this.imc = imc;
	}


	public String getPercentualgordura() {
		return percentualgordura;
	}


	public void setPercentualgordura(String percentualgordura) {
		this.percentualgordura = percentualgordura;
	}


	public String getMassamagra() {
		return massamagra;
	}


	public void setMassamagra(String massamagra) {
		this.massamagra = massamagra;
	}


	public String getMassagorda() {
		return massagorda;
	}


	public void setMassagorda(String massagorda) {
		this.massagorda = massagorda;
	}


	public String getProblemaArticular() {
		return problemaArticular;
	}


	public void setProblemaArticular(String problemaArticular) {
		this.problemaArticular = problemaArticular;
	}


	public String getCirurgia() {
		return cirurgia;
	}


	public void setCirurgia(String cirurgia) {
		this.cirurgia = cirurgia;
	}


	public String getColuna() {
		return coluna;
	}


	public void setColuna(String coluna) {
		this.coluna = coluna;
	}


	public String getCardiopata() {
		return cardiopata;
	}


	public void setCardiopata(String cardiopata) {
		this.cardiopata = cardiopata;
	}


	public String getHipertenso() {
		return hipertenso;
	}


	public void setHipertenso(String hipertenso) {
		this.hipertenso = hipertenso;
	}


	public String getDiabetico() {
		return diabetico;
	}


	public void setDiabetico(String diabetico) {
		this.diabetico = diabetico;
	}


	public String getFuma() {
		return fuma;
	}


	public void setFuma(String fuma) {
		this.fuma = fuma;
	}


	public String getBebe() {
		return bebe;
	}


	public void setBebe(String bebe) {
		this.bebe = bebe;
	}


	public String getToxico() {
		return toxico;
	}


	public void setToxico(String toxico) {
		this.toxico = toxico;
	}


	public String getJaFezMusculacao() {
		return jaFezMusculacao;
	}


	public void setJaFezMusculacao(String jaFezMusculacao) {
		this.jaFezMusculacao = jaFezMusculacao;
	}


	public String getTempoMusculacao() {
		return tempoMusculacao;
	}


	public void setTempoMusculacao(String tempoMusculacao) {
		this.tempoMusculacao = tempoMusculacao;
	}


	public String getInativoHa() {
		return inativoHa;
	}


	public void setInativoHa(String inativoHa) {
		this.inativoHa = inativoHa;
	}


	public String getFrequenciaSemanal() {
		return frequenciaSemanal;
	}


	public void setFrequenciaSemanal(String frequenciaSemanal) {
		this.frequenciaSemanal = frequenciaSemanal;
	}


	public String getTempoDisponivel() {
		return tempoDisponivel;
	}


	public void setTempoDisponivel(String tempoDisponivel) {
		this.tempoDisponivel = tempoDisponivel;
	}


	public Aluno getAluno() {
		return aluno;
	}


	public void setAluno(Aluno aluno) {
		this.aluno = aluno;
	}


	public Usuario getProfessor() {
		return professor;
	}


	public void setProfessor(Usuario professor) {
		this.professor = professor;
	}
	
	public String getNomeDoProfessor() {
        return professor != null ? professor.getNome() : null;
    }


	@Override
	public String toString() {
		return "AvaliacaoFisica [id=" + id + ", data=" + data + ", dataTroca=" + dataTroca + ", objetivo=" + objetivo
				+ ", observacao=" + observacao + ", torax=" + torax + ", abdominal=" + abdominal + ", quadril="
				+ quadril + ", antebraco=" + antebraco + ", bracorelaxado=" + bracorelaxado + ", bracocontraido="
				+ bracocontraido + ", coxasuperior=" + coxasuperior + ", panturilha=" + panturilha + ", tricipal="
				+ tricipal + ", subescapular=" + subescapular + ", peitoral=" + peitoral + ", abdomen=" + abdomen
				+ ", suprailiaca=" + suprailiaca + ", coxa=" + coxa + ", axialmedia=" + axialmedia + ", peso=" + peso
				+ ", altura=" + altura + ", imc=" + imc + ", percentualgordura=" + percentualgordura + ", massamagra="
				+ massamagra + ", massagorda=" + massagorda + ", problemaArticular=" + problemaArticular + ", cirurgia="
				+ cirurgia + ", coluna=" + coluna + ", cardiopata=" + cardiopata + ", hipertenso=" + hipertenso
				+ ", diabetico=" + diabetico + ", fuma=" + fuma + ", bebe=" + bebe + ", toxico=" + toxico
				+ ", jaFezMusculacao=" + jaFezMusculacao + ", tempoMusculacao=" + tempoMusculacao + ", inativoHa="
				+ inativoHa + ", frequenciaSemanal=" + frequenciaSemanal + ", tempoDisponivel=" + tempoDisponivel
				+ ", aluno=" + aluno + ", professor=" + professor + ", professor=" + getNomeDoProfessor() + "]";
	}


	
	
	


			
}
