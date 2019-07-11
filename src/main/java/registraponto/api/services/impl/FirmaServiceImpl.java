package registraponto.api.services.impl;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import registraponto.api.entities.Firma;
import registraponto.api.repositories.FirmaRepository;
import registraponto.api.services.FirmaService;

@Service
public class FirmaServiceImpl implements FirmaService {

	private static final Logger log = LoggerFactory.getLogger(FirmaServiceImpl.class);

	@Autowired
	private FirmaRepository firmaRepository;

	@Override
	public Optional<Firma> getByCnpj(String cnpj) {
		log.info("Buscando uma firma para o CNPJ {}", cnpj);
		return Optional.ofNullable(firmaRepository.findByCnpj(cnpj));
	}

	@Override
	public Firma persistir(Firma firma) {
		log.info("Persistindo firma: {}", firma);
		return this.firmaRepository.save(firma);
	}

}
