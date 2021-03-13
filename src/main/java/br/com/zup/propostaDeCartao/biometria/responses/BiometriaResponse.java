package br.com.zup.propostaDeCartao.biometria.responses;

import br.com.zup.propostaDeCartao.biometria.modelo.Biometria;

public class BiometriaResponse {

	private String fingerprint;
	private CartaoBiometriaResponse cartao;

	public BiometriaResponse(Biometria biometria) {
		this.fingerprint = biometria.getFingerprint();
		this.cartao = new CartaoBiometriaResponse(biometria.getCartao());
	}

	public String getFingerprint() {
		return fingerprint;
	}

	public CartaoBiometriaResponse getCartao() {
		return cartao;
	}

}
