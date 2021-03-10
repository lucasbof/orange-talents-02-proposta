package br.com.zup.proposta.verificadorCartao;

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

import br.com.zup.proposta.compartilhado.apis.cartoes.CartaoResponse;
import br.com.zup.proposta.compartilhado.apis.cartoes.CartoesClient;
import br.com.zup.proposta.novaProposta.Proposta;
import br.com.zup.proposta.novaProposta.PropostaRepository;
import br.com.zup.proposta.novaProposta.StatusCartao;

@Component
public class VerificadorDeCartao {
	
	private static Logger logger = LoggerFactory.getLogger(VerificadorDeCartao.class);
	
	@Autowired
	private PropostaRepository propostaRepository;
	
	@Autowired
	private CartoesClient cartoesClient;

	@Scheduled(fixedDelay=5000)
	@Lock(LockModeType.PESSIMISTIC_READ)
	@Async
	@Transactional
	public void verificaPropostasComCartoesDisponiveis() {
		List<Proposta> lista = propostaRepository.findByStatusCartaoAndNumeroCartaoIsNull(StatusCartao.ELEGIVEL);
		for(Proposta proposta : lista) {
			try {
				CartaoResponse response = cartoesClient.verificaCartaoExistente(proposta.getId().toString());
				proposta.atualizaNumeroCartao(response.getId());
				propostaRepository.save(proposta);
				logger.info("Proposta de ID " + proposta.getId() + " teve o número de cartão atualizado");
			} 
			catch (Exception e) {
				logger.error(e.getLocalizedMessage(), e);
			}
		}
	}
}
