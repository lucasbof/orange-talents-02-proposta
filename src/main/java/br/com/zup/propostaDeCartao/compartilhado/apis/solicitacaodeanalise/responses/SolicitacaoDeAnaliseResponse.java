package br.com.zup.propostaDeCartao.compartilhado.apis.solicitacaodeanalise.responses;

import br.com.zup.propostaDeCartao.compartilhado.apis.solicitacaodeanalise.requests.ResultadoSolicitacaoRequest;

public class SolicitacaoDeAnaliseResponse {

	private ResultadoSolicitacaoRequest resultadoSolicitacao;
	
	@Deprecated
	public SolicitacaoDeAnaliseResponse() {
	}

	public SolicitacaoDeAnaliseResponse(ResultadoSolicitacaoRequest resultadoSolicitacao) {
		this.resultadoSolicitacao = resultadoSolicitacao;
	}

	public ResultadoSolicitacaoRequest getResultadoSolicitacao() {
		return resultadoSolicitacao;
	}

}
