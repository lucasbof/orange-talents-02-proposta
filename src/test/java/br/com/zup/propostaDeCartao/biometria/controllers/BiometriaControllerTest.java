package br.com.zup.propostaDeCartao.biometria.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

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
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.zup.propostaDeCartao.biometria.modelo.Biometria;
import br.com.zup.propostaDeCartao.biometria.requests.CriaBiometriaRequest;
import br.com.zup.propostaDeCartao.proposta.modelo.Cartao;
import br.com.zup.propostaDeCartao.proposta.modelo.Endereco;
import br.com.zup.propostaDeCartao.proposta.modelo.Proposta;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@WithMockUser
class BiometriaControllerTest {
	
	@Autowired
	private MockMvc mockMvc;

	@PersistenceContext
	private EntityManager manager;

	@Autowired
	private ObjectMapper objectMapper;

	@BeforeEach
	void setUp() throws Exception {
		manager.createQuery("DELETE FROM Proposta").executeUpdate();
		manager.createQuery("DELETE FROM Biometria").executeUpdate();
		manager.createQuery("DELETE FROM Cartao").executeUpdate();
	}

	@Test
	@DisplayName("cria deveria inserir biometria e retornar 201 quando os dados são válidos")
	void test1() throws Exception {
		CriaBiometriaRequest request = new CriaBiometriaRequest(Base64.getEncoder().encodeToString("meu dedo".getBytes()));
		Endereco endereco = new Endereco("Rua 8", "434", "13230442");
		Proposta proposta = new Proposta("Lucas", "lucas@gmail.com", endereco, "27249752006", new BigDecimal(1000));
		
		manager.persist(proposta);
		Cartao cartao = new Cartao("9595-4454-3433-3546", "Lucas", LocalDateTime.now(), proposta);
		manager.persist(cartao);
		
		ResultActions result = fazRequisicaoDeCriacao(request, cartao.getId());
		result.andExpect(status().isCreated());
		assertNotNull(result.andReturn().getResponse().getHeader("Location"));
		
	}
	
	@ParameterizedTest
	@CsvSource({",", "''", "'    '", "meu dedo"})
	@DisplayName("cria não deveria inserir biometria e retornar 400 quando o fingerprint é nulo, vazio, só espaços ou não estar no formato base 64")
	void test2(String fingerprint) throws Exception {
		CriaBiometriaRequest request = new CriaBiometriaRequest(fingerprint);
		Endereco endereco = new Endereco("Rua 8", "434", "13230442");
		Proposta proposta = new Proposta("Lucas", "lucas@gmail.com", endereco, "27249752006", new BigDecimal(1000));
		
		manager.persist(proposta);
		Cartao cartao = new Cartao("9595-4454-3433-3546", "Lucas", LocalDateTime.now(), proposta);
		manager.persist(cartao);
		
		ResultActions result = fazRequisicaoDeCriacao(request, cartao.getId());
		result.andExpect(status().isBadRequest());

		Long count = manager.createQuery("SELECT COUNT(*) FROM Biometria", Long.class).getSingleResult();
		assertEquals(0L, count);
		
	}
	
	@Test
	@DisplayName("cria não deveria inserir biometria e retornar 404 quando o ID do cartão não existe")
	void test3() throws Exception {
		CriaBiometriaRequest request = new CriaBiometriaRequest(Base64.getEncoder().encodeToString("meu dedo".getBytes()));
		Endereco endereco = new Endereco("Rua 8", "434", "13230442");
		Proposta proposta = new Proposta("Lucas", "lucas@gmail.com", endereco, "27249752006", new BigDecimal(1000));
		
		manager.persist(proposta);
		
		ResultActions result = fazRequisicaoDeCriacao(request, 10000000L);
		result.andExpect(status().isNotFound());
		
		Long count = manager.createQuery("SELECT COUNT(*) FROM Biometria", Long.class).getSingleResult();
		assertEquals(0L, count);
	}
	
	@Test
	@DisplayName("getPorId deveria buscar biometria e retornar 200 quando o ID do cartão existir")
	void test4() throws Exception {
		Endereco endereco = new Endereco("Rua 8", "434", "13230442");
		Proposta proposta = new Proposta("Lucas", "lucas@gmail.com", endereco, "27249752006", new BigDecimal(1000));
		
		manager.persist(proposta);
		
		Cartao cartao = new Cartao("9595-4454-3433-3546", "Lucas", LocalDateTime.now(), proposta);
		manager.persist(cartao);
		
		Biometria biometria = new Biometria(Base64.getEncoder().encodeToString("meu dedo".getBytes()), cartao);
		manager.persist(biometria);
		
		ResultActions result = fazRequisicaoDeBusca(biometria.getId());
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.fingerprint").value(biometria.getFingerprint()));
		result.andExpect(jsonPath("$.cartao.titular").value(biometria.getCartao().getTitular()));
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:MM:ss");
		result.andExpect(jsonPath("$.cartao.emitidoEm").value(biometria.getCartao().getEmitidoEm().format(formatter)));
		
	}
	
	@Test
	@DisplayName("getPorId deveria retornar 404 quando o ID do cartão não existir")
	void test5() throws Exception {
		ResultActions result = fazRequisicaoDeBusca(1000000L);
		result.andExpect(status().isNotFound());
	}

	private ResultActions fazRequisicaoDeCriacao(CriaBiometriaRequest criaBiometriaRequest, Long cartaoId) throws Exception {
		return mockMvc
				.perform(post("/biometrias/{cartaoId}", cartaoId)
				.content(toJson(criaBiometriaRequest))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));
	}
	
	private ResultActions fazRequisicaoDeBusca(Long cartaoId) throws Exception {
		return mockMvc
				.perform(get("/biometrias/{cartaoId}", cartaoId)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));
	}

	private String toJson(Object obj) throws Exception {
		return objectMapper.writeValueAsString(obj);
	}

}
