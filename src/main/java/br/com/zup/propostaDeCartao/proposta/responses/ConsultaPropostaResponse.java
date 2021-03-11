package br.com.zup.propostaDeCartao.proposta.responses;

import java.math.BigDecimal;

import br.com.zup.propostaDeCartao.proposta.enums.StatusCartao;
import br.com.zup.propostaDeCartao.proposta.modelo.Proposta;

public class ConsultaPropostaResponse {

	private String nome;
	private String email;
	private String documento;
	private StatusCartao statusCartao;
	private BigDecimal salario;
	private EnderecoResponse endereco;
	private String numeroCartao;

	public ConsultaPropostaResponse(Proposta proposta) {
		this.nome = proposta.getNome();
		this.email = proposta.getEmail();
		this.documento = proposta.getDocumento();
		this.statusCartao = proposta.getStatusCartao();
		this.salario = proposta.getSalario();
		this.endereco = new EnderecoResponse(proposta.getEndereco());
		this.numeroCartao = proposta.getNumeroCartao();
	}

	public String getNome() {
		return nome;
	}

	public String getEmail() {
		return email;
	}

	public String getDocumento() {
		return documento;
	}

	public StatusCartao getStatusCartao() {
		return statusCartao;
	}

	public BigDecimal getSalario() {
		return salario;
	}

	public EnderecoResponse getEndereco() {
		return endereco;
	}

	public String getNumeroCartao() {
		return numeroCartao;
	}

}
