package br.com.zup.propostaDeCartao.proposta.modelo;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import br.com.zup.propostaDeCartao.compartilhado.apis.solicitacaodeanalise.requests.SolicitacaoDeAnaliseRequest;
import br.com.zup.propostaDeCartao.compartilhado.apis.solicitacaodeanalise.responses.SolicitacaoDeAnaliseResponse;
import br.com.zup.propostaDeCartao.proposta.enums.StatusCartao;

@Entity
@Table(name = "tb_proposta")
public class Proposta implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nome;
	private String email;
	private String documento;

	@Enumerated(EnumType.STRING)
	private StatusCartao statusCartao;
	private BigDecimal salario;

	@Embedded
	private Endereco endereco;

	private String numeroCartao;

	@Deprecated
	public Proposta() {
	}

	public Proposta(String nome, String email, Endereco endereco, String documento, BigDecimal salario) {
		this.nome = nome;
		this.email = email;
		this.endereco = endereco;
		this.documento = documento;
		this.salario = salario;
	}

	public Long getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public String getEmail() {
		return email;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public String getDocumento() {
		return documento;
	}

	public BigDecimal getSalario() {
		return salario;
	}

	public StatusCartao getStatusCartao() {
		return statusCartao;
	}

	public String getNumeroCartao() {
		return numeroCartao;
	}

	public void atualizaStatusCartao(SolicitacaoDeAnaliseResponse response) {
		this.statusCartao = StatusCartao.getStatusCartaoDeResultadoSolcitacao(response.getResultadoSolicitacao());
	}

	public void atualizaNumeroCartao(String numero) {
		this.numeroCartao = numero;
	}

	public SolicitacaoDeAnaliseRequest getSolicitacaoDeAnaliseRequest() {
		return new SolicitacaoDeAnaliseRequest(documento, nome, id.toString());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Proposta other = (Proposta) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
