package br.com.zup.propostaDeCartao.proposta.controllers;

import java.net.URI;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.zup.propostaDeCartao.compartilhado.apis.solicitacaodeanalise.clients.NovaSolicitacaoClient;
import br.com.zup.propostaDeCartao.compartilhado.apis.solicitacaodeanalise.requests.ResultadoSolicitacaoApiRequest;
import br.com.zup.propostaDeCartao.compartilhado.apis.solicitacaodeanalise.responses.SolicitacaoDeAnaliseApiResponse;
import br.com.zup.propostaDeCartao.proposta.modelo.Proposta;
import br.com.zup.propostaDeCartao.proposta.repositorios.PropostaRepository;
import br.com.zup.propostaDeCartao.proposta.requests.CriaPropostaRequest;
import br.com.zup.propostaDeCartao.proposta.responses.ConsultaPropostaResponse;

@RestController
@RequestMapping("/propostas")
public class PropostaController {

	@Autowired
	private PropostaRepository repository;
	
	@Autowired
	private NovaSolicitacaoClient novaSolicitacaoClient;	

	@PostMapping
	@Transactional
	public ResponseEntity<?> cria(@RequestBody @Valid CriaPropostaRequest request) {
		if (repository.existsByDocumento(request.getDocumento())) {
			return ResponseEntity.unprocessableEntity().body("Já existe uma proposta cadastrada para o documento " + request.getDocumento());
		}
		Proposta proposta = request.toModel();
		proposta = repository.save(proposta);
		
		SolicitacaoDeAnaliseApiResponse response = novaSolicitacaoClient.fazSolicitacaoDeCartao(proposta.getSolicitacaoDeAnaliseRequest());

		proposta.atualizaStatusCartao(response);

		if (response.getResultadoSolicitacao().equals(ResultadoSolicitacaoApiRequest.COM_RESTRICAO)) {
			return ResponseEntity.unprocessableEntity().body("O usuário não é elegível a ter um cartão");
		}

		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(proposta.getId())
				.toUri();
		return ResponseEntity.created(uri).build();
	}
	
	@GetMapping("/{id}")
	@Transactional(readOnly = true)
	public ResponseEntity<?> consultaProposta(@PathVariable("id") Long id) {
		Optional<Proposta> proposta = repository.findById(id);
		
		if(proposta.isPresent())
			return ResponseEntity.ok(new ConsultaPropostaResponse(proposta.get()));
		
		return ResponseEntity.notFound().build();
	}
}
