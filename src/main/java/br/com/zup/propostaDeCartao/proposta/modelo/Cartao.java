package br.com.zup.propostaDeCartao.proposta.modelo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import br.com.zup.propostaDeCartao.biometria.modelo.Biometria;

@Entity
@Table(name = "tb_cartoes")
public class Cartao {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String numeroCartao;
	private String titular;
	private LocalDateTime emitidoEm;
	
	@OneToMany(mappedBy = "cartao")
	private List<Biometria> biometrias = new ArrayList<>();

	@OneToOne(mappedBy = "cartao")
	private Proposta proposta;

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
}
