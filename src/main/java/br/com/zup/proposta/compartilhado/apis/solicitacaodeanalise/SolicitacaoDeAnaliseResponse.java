package br.com.zup.proposta.compartilhado.apis.solicitacaodeanalise;

public class SolicitacaoDeAnaliseResponse {

	private String documento;
	private String nome;
	private ResultadoSolicitacao resultadoSolicitacao;
	private String idProposta;
	
	public SolicitacaoDeAnaliseResponse(String documento, String nome, ResultadoSolicitacao resultadoSolicitacao,
			String idProposta) {
		this.documento = documento;
		this.nome = nome;
		this.resultadoSolicitacao = resultadoSolicitacao;
		this.idProposta = idProposta;
	}

	public String getDocumento() {
		return documento;
	}

	public String getNome() {
		return nome;
	}

	public ResultadoSolicitacao getResultadoSolicitacao() {
		return resultadoSolicitacao;
	}

	public String getIdProposta() {
		return idProposta;
	}

	@Override
	public String toString() {
		return "SolicitacaoDeAnaliseResponse [documento=" + documento + ", nome=" + nome + ", resultadoSolicitacao="
				+ resultadoSolicitacao + ", idProposta=" + idProposta + "]";
	}
}
