package br.com.zup.propostaDeCartao.compartilhado.apis.cartoes.requests;

public class InformaBloqueioCartaoApiRequest {

	private String sistemaResponsavel;

	public InformaBloqueioCartaoApiRequest(String sistemaResponsavel) {
		this.sistemaResponsavel = sistemaResponsavel;
	}

	public String getSistemaResponsavel() {
		return sistemaResponsavel;
	}

}
