package br.com.zup.propostaDeCartao.viagem.requests;

import java.time.LocalDate;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ViagemRequest {

	@NotBlank
	private String destino;

	@Future
	@NotNull
	@JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
	private LocalDate dataTerminoViagem;

	public ViagemRequest(@NotBlank String destino, @Future @NotNull LocalDate dataTerminoViagem) {
		this.destino = destino;
		this.dataTerminoViagem = dataTerminoViagem;
	}

	public String getDestino() {
		return destino;
	}

	public LocalDate getDataTerminoViagem() {
		return dataTerminoViagem;
	}
}
