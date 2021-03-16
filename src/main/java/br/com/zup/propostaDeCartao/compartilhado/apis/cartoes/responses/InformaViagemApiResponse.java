package br.com.zup.propostaDeCartao.compartilhado.apis.cartoes.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

public class InformaViagemApiResponse {

	@JsonProperty("resultado")
	private String resultado;

	public void setResultado(String resultado) {
		this.resultado = resultado;
	}

	public String getResultado() {
		return resultado;
	}

}
