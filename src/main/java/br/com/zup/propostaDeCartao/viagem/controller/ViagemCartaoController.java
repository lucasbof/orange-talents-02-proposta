package br.com.zup.propostaDeCartao.viagem.controller;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.zup.propostaDeCartao.cartao.modelo.Cartao;
import br.com.zup.propostaDeCartao.compartilhado.apis.cartoes.clients.InformaViagemClient;
import br.com.zup.propostaDeCartao.compartilhado.apis.cartoes.requests.InformaViagemApiRequest;
import br.com.zup.propostaDeCartao.viagem.modelo.ViagemCartao;
import br.com.zup.propostaDeCartao.viagem.requests.ViagemRequest;

@RestController
@RequestMapping("/viagens")
public class ViagemCartaoController {

	@PersistenceContext
	private EntityManager manager;

	@Autowired
	private InformaViagemClient informaViagemClient;

	@PostMapping("/{cartaoId}")
	@Transactional
	public ResponseEntity<?> cria(@RequestBody @Valid ViagemRequest viagemRequest,
			@PathVariable("cartaoId") Long cartaoId, @RequestHeader(HttpHeaders.USER_AGENT) String userAgent,
			HttpServletRequest request) {
		userAgent = userAgent == null ? "undefined" : userAgent;
		Cartao cartao = manager.find(Cartao.class, cartaoId);
		if (cartao == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		String enderecoIp = request.getHeader("X-Forward-For");
		if (enderecoIp == null) {
			enderecoIp = request.getRemoteAddr() == null ? "undefined" : request.getRemoteAddr();
		}

		InformaViagemApiRequest clientRequest = new InformaViagemApiRequest(viagemRequest.getDestino(), viagemRequest.getDataTerminoViagem());
		
		informaViagemClient.informaViagem(cartao.getNumeroCartao(), clientRequest);

		ViagemCartao viagem = new ViagemCartao(viagemRequest.getDestino(), viagemRequest.getDataTerminoViagem(),
				enderecoIp, userAgent);

		manager.persist(viagem);
		return ResponseEntity.ok().build();
	}
}
