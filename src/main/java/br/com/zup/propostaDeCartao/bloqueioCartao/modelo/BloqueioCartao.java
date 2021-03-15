package br.com.zup.propostaDeCartao.bloqueioCartao.modelo;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import br.com.zup.propostaDeCartao.cartao.modelo.Cartao;


@Entity
@Table(name = "tb_bloqueio_cartao")
public class BloqueioCartao {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne
	@JoinColumn(name = "cartao_id")
	private Cartao cartao;

	private String usuarioIp;

	private String userAgent;

	private LocalDateTime criadoEm = LocalDateTime.now();

	@Deprecated
	public BloqueioCartao() {
	}

	public BloqueioCartao(Cartao cartao, String usuarioIp, String userAgent) {
		this.cartao = cartao;
		this.usuarioIp = usuarioIp;
		this.userAgent = userAgent;
	}

	public Long getId() {
		return id;
	}

	public Cartao getCartao() {
		return cartao;
	}

	public String getUsuarioIp() {
		return usuarioIp;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public LocalDateTime getCriadoEm() {
		return criadoEm;
	}

}
