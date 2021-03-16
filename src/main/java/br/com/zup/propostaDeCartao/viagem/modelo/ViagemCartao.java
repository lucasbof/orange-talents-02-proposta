package br.com.zup.propostaDeCartao.viagem.modelo;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tb_viagem_cartao")
public class ViagemCartao {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String destino;
	
	private LocalDate dataTerminoViagem;
	
	private LocalDateTime dataComecoViagem = LocalDateTime.now();
	
	private String ipCliente;
	
	private String userAgentCliente;

	public ViagemCartao(String destino, LocalDate dataTerminoViagem, String ipCliente, String userAgentCliente) {
		this.destino = destino;
		this.dataTerminoViagem = dataTerminoViagem;
		this.ipCliente = ipCliente;
		this.userAgentCliente = userAgentCliente;
	}

	public Long getId() {
		return id;
	}

	public String getDestino() {
		return destino;
	}

	public LocalDate getDataTerminoViagem() {
		return dataTerminoViagem;
	}

	public LocalDateTime getDataComecoViagem() {
		return dataComecoViagem;
	}

	public String getIpCliente() {
		return ipCliente;
	}

	public String getUserAgentCliente() {
		return userAgentCliente;
	}	
}
