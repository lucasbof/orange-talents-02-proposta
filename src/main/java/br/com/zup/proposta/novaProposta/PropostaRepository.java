package br.com.zup.proposta.novaProposta;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PropostaRepository extends JpaRepository<Proposta, Long> {

	boolean existsByDocumento(String documento);

	List<Proposta> findByStatusCartaoAndNumeroCartaoIsNull(StatusCartao elegivel);

}
