package br.com.zup.propostaDeCartao.proposta.enums;

import br.com.zup.propostaDeCartao.compartilhado.apis.solicitacaodeanalise.requests.ResultadoSolicitacaoApiRequest;

public enum StatusCartao {

	NAO_ELEGIVEL(ResultadoSolicitacaoApiRequest.COM_RESTRICAO), ELEGIVEL(ResultadoSolicitacaoApiRequest.SEM_RESTRICAO);

	private final ResultadoSolicitacaoApiRequest solicitacao;

	StatusCartao(ResultadoSolicitacaoApiRequest solicitacao) {
		this.solicitacao = solicitacao;
	}

	public ResultadoSolicitacaoApiRequest getSolicitacao() {
		return solicitacao;
	}

	public static StatusCartao getStatusCartaoDeResultadoSolcitacao(ResultadoSolicitacaoApiRequest rs) {
		for (StatusCartao status : StatusCartao.values()) {
			if (rs.equals(status.getSolicitacao())) {
				return status;
			}
		}
		return null;
	}
}
