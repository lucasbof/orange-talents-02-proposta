package br.com.zup.propostaDeCartao.biometria.controllers;

import java.net.URI;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.zup.propostaDeCartao.biometria.modelo.Biometria;
import br.com.zup.propostaDeCartao.biometria.requests.CriaBiometriaRequest;
import br.com.zup.propostaDeCartao.biometria.responses.BiometriaResponse;
import br.com.zup.propostaDeCartao.cartao.modelo.Cartao;

@RestController
@RequestMapping("/biometrias")
public class BiometriaController {

	@PersistenceContext
	private EntityManager manager;

	@PostMapping("/{idCartao}")
	@Transactional
	public ResponseEntity<?> cria(@PathVariable("idCartao") Long idCartao,
			@RequestBody @Valid CriaBiometriaRequest criaBiometriaRequest) {
		Cartao cartao = manager.find(Cartao.class, idCartao);
		if (cartao == null) {
			return ResponseEntity.notFound().build();
		}
		Biometria biometria = criaBiometriaRequest.toModel(cartao);
		manager.persist(biometria);
		String uri = ServletUriComponentsBuilder.fromCurrentRequest().toUriString();
		uri = uri.substring(0, uri.length() - ("/" + idCartao).length());
		uri += "/" + biometria.getId();
		return ResponseEntity.created(URI.create(uri)).build();
	}
	
	@GetMapping("/{id}")
	@Transactional(readOnly = true)
	public ResponseEntity<?> getPorId(@PathVariable("id") Long id) {
		Biometria biometria = manager.find(Biometria.class, id);
		if(biometria == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok().body(new BiometriaResponse(biometria));
	}
}
