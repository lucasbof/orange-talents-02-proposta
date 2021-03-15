package br.com.zup.propostaDeCartao.bloqueioCartao.controllers;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.zup.propostaDeCartao.bloqueioCartao.modelo.BloqueioCartao;
import br.com.zup.propostaDeCartao.cartao.enums.SituacaoCartao;
import br.com.zup.propostaDeCartao.cartao.modelo.Cartao;
import br.com.zup.propostaDeCartao.compartilhado.apis.cartoes.clients.InformaBloqueioClient;
import br.com.zup.propostaDeCartao.compartilhado.apis.cartoes.requests.InformaBloqueioCartaoApiRequest;

@RestController
@RequestMapping("/bloqueios")
public class BloqueioCartaoController {

	@PersistenceContext
	private EntityManager manager;
	
	@Autowired
	private InformaBloqueioClient informaBloqueioClient;

	@PostMapping("/{cartaoId}")
	@Transactional
	public ResponseEntity<?> cria(@RequestHeader(HttpHeaders.USER_AGENT) String userAgent, HttpServletRequest request,
			@PathVariable("cartaoId") Long cartaoId) {
		Cartao cartao = manager.find(Cartao.class, cartaoId);
		if (cartao == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}

		String ipAddress = request.getHeader("X-Forward-For");
		if (ipAddress == null) {
			ipAddress = request.getRemoteAddr();
		}

		if (userAgent == null || userAgent.isBlank() || ipAddress == null || ipAddress.isBlank()) {
			return ResponseEntity.badRequest().build();
		}

		if (cartao.getBloqueio() != null) {
			return ResponseEntity.unprocessableEntity().build();
		}
		
		informaBloqueioClient.informaBloqueioCartao(cartao.getNumeroCartao(), new InformaBloqueioCartaoApiRequest("API_PROPOSTA"));
        BloqueioCartao bloqueio = new BloqueioCartao(cartao, ipAddress, userAgent);
        manager.persist(bloqueio);
        cartao.atualizaSituacao(SituacaoCartao.BLOQUEADO);
        

		return ResponseEntity.ok().build();
	}
}
