package br.com.zup.propostaDeCartao.biometria.requests;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.springframework.util.Assert;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.zup.propostaDeCartao.biometria.modelo.Biometria;
import br.com.zup.propostaDeCartao.proposta.modelo.Cartao;

public class CriaBiometriaRequest {

	@NotBlank
	@JsonProperty("fingerprint")
	@Pattern(regexp = "^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)?$", message = "Não é base 64")
	private String fingerprint;
	
	public CriaBiometriaRequest() {
	}
	
	public CriaBiometriaRequest(String fingerprint) {
		this.fingerprint = fingerprint;
	}
	
	public String getFingerprint() {
		return fingerprint;
	}

	public Biometria toModel(Cartao cartao) {
		Assert.notNull(cartao, "O cartao informado é nulo!");
		return new Biometria(fingerprint, cartao);
	}

}
