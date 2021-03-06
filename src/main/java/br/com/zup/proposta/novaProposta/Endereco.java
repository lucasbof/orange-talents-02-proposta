package br.com.zup.proposta.novaProposta;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;

@Embeddable
public class Endereco implements Serializable {

	private static final long serialVersionUID = 1L;

	private String logradouro;
	private String numero;
	private String cep;
	
	@Deprecated
	public Endereco() {
	}

	public Endereco(@NotBlank String logradouro, @NotBlank String numero, @NotBlank String cep) {
		this.logradouro = logradouro;
		this.numero = numero;
		this.cep = cep;
	}

	public String getLogradouro() {
		return logradouro;
	}

	public String getNumero() {
		return numero;
	}

	public String getCep() {
		return cep;
	}
}
