package br.com.zup.propostaDeCartao.compartilhado.apis.solicitacaodeanalise.clients;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.zup.propostaDeCartao.compartilhado.apis.solicitacaodeanalise.clients.NovaSolicitacaoClient.NovaSolicitacaoFallback;
import br.com.zup.propostaDeCartao.compartilhado.apis.solicitacaodeanalise.requests.SolicitacaoDeAnaliseApiRequest;
import br.com.zup.propostaDeCartao.compartilhado.apis.solicitacaodeanalise.responses.SolicitacaoDeAnaliseApiResponse;
import br.com.zup.propostaDeCartao.compartilhado.exceptions.ApiErroException;
import feign.FeignException;

@FeignClient(url = "${apis.novasolicitacao.host}", name = "novaSolicitacao", fallbackFactory = NovaSolicitacaoFallback.class)
public interface NovaSolicitacaoClient {

	@PostMapping("/api/solicitacao")
	SolicitacaoDeAnaliseApiResponse fazSolicitacaoDeCartao(SolicitacaoDeAnaliseApiRequest request);
	
	
	@Component
	static class NovaSolicitacaoFallback implements FallbackFactory<NovaSolicitacaoClient> {

		Logger logger = LoggerFactory.getLogger(NovaSolicitacaoFallback.class);
		
		@Autowired
		private ObjectMapper om;
		
		@Override
		public NovaSolicitacaoClient create(Throwable cause) {
			return new NovaSolicitacaoClient() {
				
				@Override
				public SolicitacaoDeAnaliseApiResponse fazSolicitacaoDeCartao(SolicitacaoDeAnaliseApiRequest request) {
					if(cause instanceof FeignException) {
						FeignException err = (FeignException) cause;
						if(err.status() == 422) {
							return (SolicitacaoDeAnaliseApiResponse) jsonStringParaObject(err.contentUTF8(), SolicitacaoDeAnaliseApiResponse.class);
						}
					}
					logger.error(cause.getMessage(), cause);
					throw new ApiErroException(HttpStatus.BAD_REQUEST, "Erro ao fazer chamada da API para a solicitação de um novo cartão");
				}
				private Object jsonStringParaObject(String jsonString, Class<?> clazz) {
					try {
						return om.readValue(jsonString, clazz);
					} catch (Exception e) {
						throw new ApiErroException(HttpStatus.BAD_REQUEST, "Erro ao fazer parse para objeto");
					}
				}
			};
		}
		
		
	}
}
