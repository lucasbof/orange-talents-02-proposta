package br.com.zup.propostaDeCartao.proposta.requests;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import br.com.zup.propostaDeCartao.proposta.modelo.Endereco;

public class EnderecoRequest implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@NotBlank
	private String logradouro;
	
	@NotBlank
	private String numero;
	
	@NotBlank
	private String cep;
	
	public EnderecoRequest(String logradouro, String numero, String cep) {
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

	public Endereco toModel() {
		return new Endereco(logradouro, numero, cep);
	}
}
