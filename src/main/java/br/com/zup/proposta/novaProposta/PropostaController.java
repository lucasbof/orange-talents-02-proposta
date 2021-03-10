package br.com.zup.proposta.novaProposta;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.zup.proposta.compartilhado.apis.solicitacaodeanalise.NovaSolicitacaoClient;
import br.com.zup.proposta.compartilhado.apis.solicitacaodeanalise.ResultadoSolicitacao;
import br.com.zup.proposta.compartilhado.apis.solicitacaodeanalise.SolicitacaoDeAnaliseResponse;
import br.com.zup.proposta.compartilhado.exceptions.ApiErroException;

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
			throw new ApiErroException(HttpStatus.UNPROCESSABLE_ENTITY, "Já existe uma proposta cadastrada para o documento " + request.getDocumento());
		}
		Proposta proposta = request.toModel();
		proposta = repository.save(proposta);
		
		SolicitacaoDeAnaliseResponse response = novaSolicitacaoClient.fazSolicitacaoDeCartao(proposta.getSolicitacaoDeAnaliseRequest());

		proposta.atualizaStatusCartao(response);

		if (response.getResultadoSolicitacao().equals(ResultadoSolicitacao.COM_RESTRICAO)) {
			return ResponseEntity.unprocessableEntity().body("O usuário não é elegível a ter um cartão");
		}

		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(proposta.getId())
				.toUri();
		return ResponseEntity.created(uri).build();
	}
}
