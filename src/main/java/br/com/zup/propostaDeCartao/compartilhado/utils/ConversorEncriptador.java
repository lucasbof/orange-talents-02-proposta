package br.com.zup.propostaDeCartao.compartilhado.utils;

import java.security.Key;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.persistence.AttributeConverter;

import org.springframework.beans.factory.annotation.Value;

public class ConversorEncriptador implements AttributeConverter<String, String> {
	
	private Key chave;

	private Cipher cipher;

	public ConversorEncriptador(@Value("${conversorencriptador.chavecriptografica}") String chaveCriptografica) {
		try {
			chave = new SecretKeySpec(chaveCriptografica.getBytes(), "AES");
			cipher = Cipher.getInstance("AES");
		} catch (Exception e) {
			throw new IllegalArgumentException("Erro de conversão criptográfica");
		}
	}


	@Override
	public String convertToDatabaseColumn(String attribute) {
		try {
			cipher.init(Cipher.ENCRYPT_MODE, chave);
			return Base64.getEncoder().encodeToString(cipher.doFinal(attribute.getBytes()));
		} catch (Exception e) {
			throw new IllegalArgumentException("Erro de conversão criptográfica");
		}
	}

	@Override
	public String convertToEntityAttribute(String dbData) {
		try {
			cipher.init(Cipher.DECRYPT_MODE, chave);
			return new String(cipher.doFinal(Base64.getDecoder().decode(dbData)));
		} catch (Exception e) {
			throw new IllegalArgumentException("Erro de conversão criptográfica");
		}
	}

}
