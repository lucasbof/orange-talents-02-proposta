package br.com.zup.propostaDeCartao.compartilhado.apis.solicitacaodeanalise.responses;

import br.com.zup.propostaDeCartao.compartilhado.apis.solicitacaodeanalise.requests.ResultadoSolicitacaoApiRequest;

public class SolicitacaoDeAnaliseApiResponse {

	private ResultadoSolicitacaoApiRequest resultadoSolicitacao;
	
	@Deprecated
	public SolicitacaoDeAnaliseApiResponse() {
	}

	public SolicitacaoDeAnaliseApiResponse(ResultadoSolicitacaoApiRequest resultadoSolicitacao) {
		this.resultadoSolicitacao = resultadoSolicitacao;
	}

	public ResultadoSolicitacaoApiRequest getResultadoSolicitacao() {
		return resultadoSolicitacao;
	}

}
