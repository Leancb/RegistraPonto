package registraponto.api.controllers;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import registraponto.api.dtos.FirmaDto;
import registraponto.api.entities.Firma;
import registraponto.api.response.Response;
import registraponto.api.services.FirmaService;

@RestController
@RequestMapping("/api/firmas")
@CrossOrigin(origins = "*")
public class FirmaController {

	private static final Logger log = LoggerFactory.getLogger(FirmaController.class);

	@Autowired
	private FirmaService firmaService;

	public FirmaController() {
	}

	/**
	 * Retorna uma firma dado um CNPJ.
	 * 
	 * @param cnpj
	 * @return ResponseEntity<Response<FirmaDto>>
	 */
	@GetMapping(value = "/cnpj/{cnpj}")
	public ResponseEntity<Response<FirmaDto>> getByCnpj( @PathVariable("cnpj") String cnpj) {
		log.info("Buscando firma por CNPJ: {}", cnpj);
		Response<FirmaDto> response = new Response<FirmaDto>();
		Optional<Firma> firma = firmaService.getByCnpj(cnpj);

		if (!firma.isPresent()) {
			log.info("Firma não encontrada para o CNPJ: {}", cnpj);
			response.getErrors().add("Firma não encontrada para o CNPJ " + cnpj);
			return ResponseEntity.badRequest().body(response);
		}

		response.setData(this.converterFirmaDto(firma.get()));
		return ResponseEntity.ok(response);
	}

	/**
	 * Popula um DTO com os dados de uma firma.
	 * 
	 * @param firma
	 * @return FirmaDto
	 */
	private FirmaDto converterFirmaDto(Firma firma) {
		FirmaDto firmaDto = new FirmaDto();
		firmaDto.setId(firma.getId());
		firmaDto.setCnpj(firma.getCnpj());
		firmaDto.setRazaoSocial(firma.getRazaoSocial());

		return firmaDto;
	}

}
