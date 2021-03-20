package br.com.zup.propostaDeCartao.carteira.controllers;

import java.net.URI;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.zup.propostaDeCartao.cartao.modelo.Cartao;
import br.com.zup.propostaDeCartao.carteira.modelo.CarteiraCartao;
import br.com.zup.propostaDeCartao.carteira.requests.CarteiraCartaoRequest;
import br.com.zup.propostaDeCartao.compartilhado.apis.cartoes.clients.AssociaCarteiraClient;

@RestController
@RequestMapping("/carteiras")
public class CarteiraCartaoController {
	
	@PersistenceContext
	private EntityManager manager;
	
	@Autowired
	private AssociaCarteiraClient carteiraClient;

	@PostMapping("/{cartaoId}")
	@Transactional
	public ResponseEntity<?> associa(@RequestBody @Valid CarteiraCartaoRequest request, @PathVariable("cartaoId") Long idCartao) {
		Cartao cartao = manager.find(Cartao.class, idCartao);
		if (cartao == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		
		TypedQuery<CarteiraCartao> query = manager.createQuery("SELECT c FROM CarteiraCartao c WHERE c.cartao = :cartao", CarteiraCartao.class);
		query.setParameter("cartao", cartao);
		List<CarteiraCartao> carteiras = query.getResultList();
		
		if(carteiras.size() > 0) {
			return ResponseEntity.unprocessableEntity().build();
		}
		
		carteiraClient.associa(cartao.getNumeroCartao(), request);
		
		CarteiraCartao carteira = request.toModel(cartao);
		
		manager.persist(carteira);
		
		String uri = ServletUriComponentsBuilder.fromCurrentRequest().toUriString();
		uri = uri.substring(0, uri.length() - ("/" + idCartao).length());
		uri += "/" + carteira.getId();
		return ResponseEntity.created(URI.create(uri)).build();
	}
}
