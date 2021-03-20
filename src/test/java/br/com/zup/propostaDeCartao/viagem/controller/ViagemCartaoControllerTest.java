package br.com.zup.propostaDeCartao.viagem.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.zup.propostaDeCartao.cartao.modelo.Cartao;
import br.com.zup.propostaDeCartao.compartilhado.apis.cartoes.clients.InformaViagemClient;
import br.com.zup.propostaDeCartao.compartilhado.apis.cartoes.responses.InformaViagemApiResponse;
import br.com.zup.propostaDeCartao.proposta.modelo.Endereco;
import br.com.zup.propostaDeCartao.proposta.modelo.Proposta;
import br.com.zup.propostaDeCartao.viagem.modelo.ViagemCartao;
import br.com.zup.propostaDeCartao.viagem.requests.ViagemRequest;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@WithMockUser
class ViagemCartaoControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@PersistenceContext
	private EntityManager manager;

	@MockBean
	private InformaViagemClient informaViagemClient;

	private String jsonBody;

	@BeforeEach
	void setUp() throws Exception {
		when(informaViagemClient.informaViagem(eq("5209-1622-1164-6666"), any()))
				.thenReturn(new InformaViagemApiResponse("CRIADO"));
	}

	@Test
	@DisplayName("deveria retornar 200 quando tudo Ok")
	void teste1() throws Exception {

		ViagemRequest request = new ViagemRequest("Teste", LocalDate.MAX);
		jsonBody = objectMapper.writeValueAsString(request);

		Endereco endereco = new Endereco("Rua dos Testes", "3500", "25015301");
		Proposta proposta = new Proposta("Lucas", "lucas@gmail.com", endereco, "hdX7KhLixySiWxDGMIxZ9g==",
				new BigDecimal(1500));

		manager.persist(proposta);

		Cartao cartao = new Cartao("5209-1622-1164-6666", "Lucas", LocalDateTime.now(), proposta);

		manager.persist(cartao);

		mockMvc.perform(post("/viagens/{cartaoId}", cartao.getId())
				.header(HttpHeaders.USER_AGENT, "User-Agent")
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());

		ViagemCartao result = manager.find(ViagemCartao.class, 1L);
		assertNotNull(result);
		assertEquals("Teste", result.getDestino());
		assertEquals("User-Agent", result.getUserAgentCliente());
		assertEquals(LocalDate.MAX, result.getDataTerminoViagem());
	}

	@Test
	@DisplayName("deveria retornar 404 quando cartão não encontrado")
	void teste2() throws Exception {

		ViagemRequest request = new ViagemRequest("Teste", LocalDate.MAX);
		jsonBody = objectMapper.writeValueAsString(request);

		mockMvc.perform(post("/viagens/{cartaoId}", 1000L)
				.header(HttpHeaders.USER_AGENT, "User-Agent")
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isNotFound());

		ViagemCartao result = manager.find(ViagemCartao.class, 1000L);
		assertNull(result);
	}

	@ParameterizedTest
	@DisplayName("deveria retornar 400 quando algum dado for inválido ou não informado")
	@CsvSource({ ", 2999-03-15", "'', 2999-03-15", "'    ', 2999-03-15", "Brasil,", "Brasil, 2000-01-01"})
	void teste3(String destino, LocalDate dataTerminoViagem) throws Exception {
		ViagemRequest request = new ViagemRequest(destino, dataTerminoViagem);
		jsonBody = objectMapper.writeValueAsString(request);
		
		Endereco endereco = new Endereco("Rua dos Testes", "3500", "25015301");
		Proposta proposta = new Proposta("Lucas", "lucas@gmail.com", endereco, "hdX7KhLixySiWxDGMIxZ9g==",
				new BigDecimal(1500));

		manager.persist(proposta);

		Cartao cartao = new Cartao("5209-1622-1164-6666", "Lucas", LocalDateTime.now(), proposta);

		manager.persist(cartao);
		

		mockMvc.perform(post("/viagens/{cartaoId}", cartao.getId())
				.header(HttpHeaders.USER_AGENT, "User-Agent")
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isBadRequest());

		ViagemCartao result = manager.find(ViagemCartao.class, 1L);
		assertNull(result);
	}
}
