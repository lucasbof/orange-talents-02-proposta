package br.com.zup.propostaDeCartao.proposta.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.zup.propostaDeCartao.proposta.enums.StatusCartao;
import br.com.zup.propostaDeCartao.proposta.modelo.Proposta;

public interface PropostaRepository extends JpaRepository<Proposta, Long> {

	boolean existsByDocumento(String documento);

	List<Proposta> findByStatusCartaoAndNumeroCartaoIsNull(StatusCartao elegivel);

}
