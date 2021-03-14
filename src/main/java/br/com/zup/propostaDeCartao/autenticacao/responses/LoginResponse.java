package br.com.zup.propostaDeCartao.autenticacao.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginResponse {

	@JsonProperty("token_type")
	private String tipo;
	
	@JsonProperty("access_token")
	private String token;
	
	@JsonProperty("expires_in")
	private Integer expiraEm;

	public LoginResponse(String tipo, String token, Integer expiraEm) {
		this.tipo = tipo;
		this.token = token;
		this.expiraEm = expiraEm;
	}

	public String getTipo() {
		return tipo;
	}

	public String getToken() {
		return token;
	}

	public Integer getExpiraEm() {
		return expiraEm;
	}
}
