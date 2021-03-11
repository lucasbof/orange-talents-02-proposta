package br.com.zup.propostaDeCartao.proposta.responses;

import br.com.zup.propostaDeCartao.proposta.modelo.Endereco;

public class EnderecoResponse {

	private String logradouro;
	private String numero;
	private String cep;

	public EnderecoResponse(Endereco endereco) {
		this.logradouro = endereco.getLogradouro();
		this.numero = endereco.getNumero();
		this.cep = endereco.getCep();
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
