package br.com.zup.propostaDeCartao.compartilhado.apis.cartoes.responses;

public class VerificaCartaoApiResponse {

	private String situacao;

	public VerificaCartaoApiResponse(String situacao) {
		this.situacao = situacao;
	}

	public String getSituacao() {
		return situacao;
	}
}
