package br.com.zup.propostaDeCartao.carteira.requests;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import br.com.zup.propostaDeCartao.cartao.modelo.Cartao;
import br.com.zup.propostaDeCartao.carteira.enums.TiposCarteiraEnum;
import br.com.zup.propostaDeCartao.carteira.modelo.CarteiraCartao;

public class CarteiraCartaoRequest {

	@NotBlank
	@Email
	private String email;

	@NotNull
	private TiposCarteiraEnum carteira;

	public CarteiraCartaoRequest(@NotBlank @Email String email, @NotNull TiposCarteiraEnum carteira) {
		this.email = email;
		this.carteira = carteira;
	}

	public String getEmail() {
		return email;
	}

	public TiposCarteiraEnum getCarteira() {
		return carteira;
	}

	public CarteiraCartao toModel(Cartao cartao) {
		return new CarteiraCartao(carteira, email, cartao);
	}

}
