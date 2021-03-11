package br.com.zup.propostaDeCartao.proposta.requests;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import br.com.zup.propostaDeCartao.compartilhado.validacoes.CpfOuCnpj;
import br.com.zup.propostaDeCartao.proposta.modelo.Proposta;

public class CriaPropostaRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotBlank
	private String nome;

	@NotBlank
	@Email
	private String email;

	@NotNull
	@Valid
	private EnderecoRequest endereco;

	@NotBlank
	@CpfOuCnpj
	private String documento;

	@NotNull
	@Positive
	private BigDecimal salario;
	
	public CriaPropostaRequest(@NotBlank String nome, @NotBlank @Email String email,
			@NotNull @Valid EnderecoRequest endereco, @NotBlank String documento,
			@NotNull @Positive BigDecimal salario) {
		super();
		this.nome = nome;
		this.email = email;
		this.endereco = endereco;
		this.documento = documento;
		this.salario = salario;
	}

	public String getNome() {
		return nome;
	}

	public String getEmail() {
		return email;
	}

	public EnderecoRequest getEndereco() {
		return endereco;
	}

	public String getDocumento() {
		return documento;
	}

	public BigDecimal getSalario() {
		return salario;
	}

	public Proposta toModel() {
		return new Proposta(nome, email, endereco.toModel(), documento, salario);
	}
}
