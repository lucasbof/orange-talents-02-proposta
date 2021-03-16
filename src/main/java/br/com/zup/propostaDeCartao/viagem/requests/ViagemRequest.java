package br.com.zup.propostaDeCartao.viagem.requests;

import java.time.LocalDate;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ViagemRequest {

	@NotBlank
	@JsonProperty("destino")
	private String destino;

	@Future
	@NotNull
	@JsonProperty("dataTerminoViagem")
	@JsonFormat(pattern = "dd/MM/yyyy", shape = JsonFormat.Shape.STRING)
	private LocalDate dataTerminoViagem;

	public String getDestino() {
		return destino;
	}

	public LocalDate getDataTerminoViagem() {
		return dataTerminoViagem;
	}
}
