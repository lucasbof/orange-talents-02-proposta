package br.com.zup.propostaDeCartao.cartao.modelo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import br.com.zup.propostaDeCartao.biometria.modelo.Biometria;
import br.com.zup.propostaDeCartao.bloqueioCartao.modelo.BloqueioCartao;
import br.com.zup.propostaDeCartao.cartao.enums.SituacaoCartao;
import br.com.zup.propostaDeCartao.proposta.modelo.Proposta;

@Entity
@Table(name = "tb_cartoes")
public class Cartao {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String numeroCartao;
	private String titular;
	private LocalDateTime emitidoEm;

	@Enumerated(EnumType.STRING)
	private SituacaoCartao situacao = SituacaoCartao.ATIVO;

	@OneToMany(mappedBy = "cartao")
	private List<Biometria> biometrias = new ArrayList<>();

	@OneToOne(mappedBy = "cartao")
	private Proposta proposta;

	@OneToOne(mappedBy = "cartao")
	private BloqueioCartao bloqueio;

	@Deprecated
	public Cartao() {
	}

	public Cartao(String numeroCartao, String titular, LocalDateTime emitidoEm, Proposta proposta) {
		this.numeroCartao = numeroCartao;
		this.titular = titular;
		this.emitidoEm = emitidoEm;
		this.proposta = proposta;
	}

	public Long getId() {
		return id;
	}

	public String getNumeroCartao() {
		return numeroCartao;
	}

	public String getTitular() {
		return titular;
	}

	public Proposta getProposta() {
		return proposta;
	}

	public LocalDateTime getEmitidoEm() {
		return emitidoEm;
	}

	public List<Biometria> getBiometrias() {
		return biometrias;
	}

	public BloqueioCartao getBloqueio() {
		return bloqueio;
	}

	public SituacaoCartao getSituacao() {
		return situacao;
	}

	public void atualizaSituacao(SituacaoCartao situacao) {
		this.situacao = situacao;
	}

}
