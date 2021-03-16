package br.com.zup.propostaDeCartao.carteira.modelo;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import br.com.zup.propostaDeCartao.cartao.modelo.Cartao;
import br.com.zup.propostaDeCartao.carteira.enums.TiposCarteiraEnum;

@Entity
@Table(name = "tb_carteira_cartao")
public class CarteiraCartao {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	private TiposCarteiraEnum tipoCarteira;

	private String email;

	private LocalDateTime criadoEm = LocalDateTime.now();

	@OneToOne
	@JoinColumn(name = "cartao_id")
	private Cartao cartao;

	@Deprecated
	public CarteiraCartao() {
	}

	public CarteiraCartao(TiposCarteiraEnum tipoCarteira, String email, Cartao cartao) {
		this.tipoCarteira = tipoCarteira;
		this.email = email;
		this.cartao = cartao;
	}

	public Long getId() {
		return id;
	}

	public TiposCarteiraEnum getTipoCarteira() {
		return tipoCarteira;
	}

	public String getEmail() {
		return email;
	}

	public LocalDateTime getCriadoEm() {
		return criadoEm;
	}

	public Cartao getCartao() {
		return cartao;
	}

}
