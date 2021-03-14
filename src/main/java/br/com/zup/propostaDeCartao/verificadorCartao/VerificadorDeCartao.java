package br.com.zup.propostaDeCartao.verificadorCartao;

import java.util.List;

import javax.persistence.LockModeType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import br.com.zup.propostaDeCartao.compartilhado.apis.cartoes.clients.CartoesClient;
import br.com.zup.propostaDeCartao.compartilhado.apis.cartoes.responses.CartaoApiResponse;
import br.com.zup.propostaDeCartao.proposta.enums.StatusCartao;
import br.com.zup.propostaDeCartao.proposta.modelo.Proposta;
import br.com.zup.propostaDeCartao.proposta.repositorios.PropostaRepository;

@Component
public class VerificadorDeCartao {
	
	private static Logger logger = LoggerFactory.getLogger(VerificadorDeCartao.class);
	
	@Autowired
	private PropostaRepository propostaRepository;
	
	@Autowired
	private CartoesClient cartoesClient;

	@Scheduled(fixedDelay=30000)
	@Lock(LockModeType.PESSIMISTIC_READ)
	@Async
	@Transactional
	public void verificaPropostasComCartoesDisponiveis() {
		List<Proposta> lista = propostaRepository.findByStatusCartaoAndCartaoIsNull(StatusCartao.ELEGIVEL);
		for(Proposta proposta : lista) {
			try {
				CartaoApiResponse response = cartoesClient.verificaCartaoExistente(proposta.getId().toString());
				proposta.atualizaCartao(response.toModel(proposta));
				propostaRepository.save(proposta);
				logger.info("Proposta de ID " + proposta.getId() + " teve o número de cartão atualizado");
			} 
			catch (Exception e) {
				logger.error(e.getLocalizedMessage(), e);
			}
		}
	}
}
