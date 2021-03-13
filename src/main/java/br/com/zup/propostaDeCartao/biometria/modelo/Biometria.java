package br.com.zup.propostaDeCartao.biometria.modelo;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.com.zup.propostaDeCartao.proposta.modelo.Cartao;

@Entity
@Table(name = "tb_biometrias")
public class Biometria {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String fingerprint;

	@ManyToOne
	@JoinColumn(name = "card_id")
	private Cartao cartao;

	@Column(updatable = false)
	private LocalDateTime criadoEm = LocalDateTime.now();
	
	@Deprecated
	public Biometria() {
	}

	public Biometria(String fingerprint, Cartao cartao) {
		this.fingerprint = fingerprint;
		this.cartao = cartao;
	}

	public Long getId() {
		return id;
	}

	public String getFingerprint() {
		return fingerprint;
	}

	public Cartao getCartao() {
		return cartao;
	}

	public LocalDateTime getCriadoEm() {
		return criadoEm;
	}

}
