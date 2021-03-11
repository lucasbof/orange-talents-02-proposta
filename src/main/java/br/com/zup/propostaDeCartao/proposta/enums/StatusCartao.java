package br.com.zup.propostaDeCartao.proposta.enums;

import br.com.zup.propostaDeCartao.compartilhado.apis.solicitacaodeanalise.requests.ResultadoSolicitacaoRequest;

public enum StatusCartao {

	NAO_ELEGIVEL(ResultadoSolicitacaoRequest.COM_RESTRICAO), ELEGIVEL(ResultadoSolicitacaoRequest.SEM_RESTRICAO);

	private final ResultadoSolicitacaoRequest solicitacao;

	StatusCartao(ResultadoSolicitacaoRequest solicitacao) {
		this.solicitacao = solicitacao;
	}

	public ResultadoSolicitacaoRequest getSolicitacao() {
		return solicitacao;
	}

	public static StatusCartao getStatusCartaoDeResultadoSolcitacao(ResultadoSolicitacaoRequest rs) {
		for (StatusCartao status : StatusCartao.values()) {
			if (rs.equals(status.getSolicitacao())) {
				return status;
			}
		}
		return null;
	}
}
