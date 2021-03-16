package br.com.zup.propostaDeCartao.compartilhado.apis.cartoes.requests;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public class InformaViagemApiRequest {

	@JsonProperty("destino")
	private String destino;

	@JsonProperty("validoAte")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate validoAte;

	public InformaViagemApiRequest(String destino, LocalDate validoAte) {
		this.destino = destino;
		this.validoAte = validoAte;
	}

	public String getDestino() {
		return destino;
	}

	public LocalDate getValidoAte() {
		return validoAte;
	}

}
