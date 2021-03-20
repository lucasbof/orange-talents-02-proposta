package br.com.zup.propostaDeCartao.bloqueioCartao.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import br.com.zup.propostaDeCartao.bloqueioCartao.modelo.BloqueioCartao;
import br.com.zup.propostaDeCartao.cartao.enums.SituacaoCartao;
import br.com.zup.propostaDeCartao.cartao.modelo.Cartao;
import br.com.zup.propostaDeCartao.compartilhado.apis.cartoes.clients.InformaBloqueioClient;
import br.com.zup.propostaDeCartao.compartilhado.apis.cartoes.responses.VerificaCartaoExistenteApiResponse;
import br.com.zup.propostaDeCartao.proposta.modelo.Endereco;
import br.com.zup.propostaDeCartao.proposta.modelo.Proposta;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@WithMockUser
class BloqueioCartaoControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@PersistenceContext
	private EntityManager manager;

	@MockBean
	private InformaBloqueioClient informaBloqueioClient;

	@BeforeEach
	void setUp() throws Exception {
		when(informaBloqueioClient.informaBloqueioCartao(eq("5209-1622-1164-6666"), any()))
		.thenReturn(new VerificaCartaoExistenteApiResponse("5959-4545-5455-5343", LocalDateTime.now(), "Lucas",
				new BigDecimal(1500), "1"));
	}

	@Test
	@DisplayName("Deveria bloquear cartão e retornar 200")
	void teste1() throws Exception {
		Endereco endereco = new Endereco("Rua dos Testes", "3500", "25015301");
		Proposta proposta = new Proposta("Lucas", "lucas@gmail.com", endereco, "hdX7KhLixySiWxDGMIxZ9g==",
				new BigDecimal(1500));

		manager.persist(proposta);

		Cartao cartao = new Cartao("5209-1622-1164-6666", "Lucas", LocalDateTime.now(), proposta);

		manager.persist(cartao);

		mockMvc.perform(post("/bloqueios/{cartaoId}", cartao.getId())
				.header(HttpHeaders.USER_AGENT, "User-Agent")
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());

		BloqueioCartao result = manager.find(BloqueioCartao.class, 1L);
		Assertions.assertNotNull(result);
		Assertions.assertEquals("User-Agent", ReflectionTestUtils.getField(result, "userAgent"));
	}

	@Test
	@DisplayName("Deveria retornar 404 quando cartão não encontrado")
	@WithMockUser
	void teste2() throws Exception {

		mockMvc.perform(post("/bloqueios/{cartaoId}", 1000L).header(HttpHeaders.USER_AGENT, "User-Agent")
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());

		BloqueioCartao result = manager.find(BloqueioCartao.class, 1000L);
		Assertions.assertNull(result);
	}

	@Test
	@DisplayName("Deveria retornar 422 quando cartão já estiver bloqueado")
	@WithMockUser
	void teste3() throws Exception {
		
		Endereco endereco = new Endereco("Rua dos Testes", "3500", "25015301");
		Proposta proposta = new Proposta("Lucas", "lucas@gmail.com", endereco, "hdX7KhLixySiWxDGMIxZ9g==",
				new BigDecimal(1500));

		manager.persist(proposta);

		Cartao cartao = new Cartao("5209-1622-1164-6666", "Lucas", LocalDateTime.now(), proposta);

		manager.persist(cartao);
		
		BloqueioCartao bloqueio = new BloqueioCartao(cartao, "192.168.1.0", "User-Agent");
	    manager.persist(bloqueio);
	    cartao.atualizaSituacao(SituacaoCartao.BLOQUEADO);
	    
	    System.out.println(cartao.getSituacao());

	    mockMvc.perform(post("/bloqueios/{cartaoId}", cartao.getId())
				.header(HttpHeaders.USER_AGENT, "User-Agent")
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isUnprocessableEntity());
	    
	    

	}

}
