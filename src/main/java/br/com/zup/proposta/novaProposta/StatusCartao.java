package br.com.zup.proposta.novaProposta;

import br.com.zup.proposta.compartilhado.apis.solicitacaodeanalise.ResultadoSolicitacao;

public enum StatusCartao {

	NAO_ELEGIVEL(ResultadoSolicitacao.COM_RESTRICAO), ELEGIVEL(ResultadoSolicitacao.SEM_RESTRICAO);

	private final ResultadoSolicitacao solicitacao;

	StatusCartao(ResultadoSolicitacao solicitacao) {
		this.solicitacao = solicitacao;
	}

	public ResultadoSolicitacao getSolicitacao() {
		return solicitacao;
	}

	public static StatusCartao getStatusCartaoDeResultadoSolcitacao(ResultadoSolicitacao rs) {
		for (StatusCartao status : StatusCartao.values()) {
			if (rs.equals(status.getSolicitacao())) {
				return status;
			}
		}
		return null;
	}
}
