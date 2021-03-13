package br.com.zup.propostaDeCartao.biometria.responses;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.zup.propostaDeCartao.proposta.modelo.Cartao;

public class CartaoBiometriaResponse {

	private String titular;

	@JsonFormat(pattern = "dd/MM/yyyy hh:MM:ss")
	private LocalDateTime emitidoEm;

	public CartaoBiometriaResponse(Cartao cartao) {
		this.titular = cartao.getTitular();
		this.emitidoEm = cartao.getEmitidoEm();
	}

	public String getTitular() {
		return titular;
	}

	public LocalDateTime getEmitidoEm() {
		return emitidoEm;
	}

}
