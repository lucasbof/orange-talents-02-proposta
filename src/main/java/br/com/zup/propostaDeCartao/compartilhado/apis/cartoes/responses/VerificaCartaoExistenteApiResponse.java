package br.com.zup.propostaDeCartao.compartilhado.apis.cartoes.responses;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.zup.propostaDeCartao.cartao.modelo.Cartao;
import br.com.zup.propostaDeCartao.proposta.modelo.Proposta;

public class VerificaCartaoExistenteApiResponse {

	@JsonProperty("id")
	private String numeroCatao;
	private LocalDateTime emitidoEm;
	private String titular;
	private BigDecimal limite;
	private String idProposta;

	public VerificaCartaoExistenteApiResponse(String numeroCatao, LocalDateTime emitidoEm, String titular, BigDecimal limite,
			String idProposta) {
		this.numeroCatao = numeroCatao;
		this.emitidoEm = emitidoEm;
		this.titular = titular;
		this.limite = limite;
		this.idProposta = idProposta;
	}

	public String getNumeroCatao() {
		return numeroCatao;
	}

	public LocalDateTime getEmitidoEm() {
		return emitidoEm;
	}

	public String getTitular() {
		return titular;
	}

	public BigDecimal getLimite() {
		return limite;
	}

	public String getIdProposta() {
		return idProposta;
	}

	public Cartao toModel(Proposta proposta) {
		return new Cartao(numeroCatao, titular, emitidoEm, proposta);
	}

}
