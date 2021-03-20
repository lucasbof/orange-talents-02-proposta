package br.com.zup.propostaDeCartao.compartilhado.apis.cartoes.responses;

public class AssociaCarteiraApiResponse {

	private String resultado;
	
	public AssociaCarteiraApiResponse() {
	}
	
	public AssociaCarteiraApiResponse(String resultado) {
		this.resultado = resultado;
	}

	public String getResultado() {
		return resultado;
	}
}
