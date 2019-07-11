package registraponto.api.controllers;

import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import registraponto.api.dtos.ColaboradorDto;
import registraponto.api.entities.Colaborador;
import registraponto.api.response.Response;
import registraponto.api.services.ColaboradorService;
import registraponto.api.utils.PasswordUtils;

@RestController
@RequestMapping("/api/colaboradors")
@CrossOrigin(origins = "*")
public class ColaboradorController {

	private static final Logger log = LoggerFactory.getLogger(ColaboradorController.class);

	@Autowired
	private ColaboradorService colaboradorService;

	public ColaboradorController() {
	}

	/**
	 * Atualiza os dados de um funcionário.
	 * 
	 * @param id
	 * @param colaboradorDto
	 * @param result
	 * @return ResponseEntity<Response<ColaboradorDto>>
	 * @throws NoSuchAlgorithmException
	 */
	@PutMapping(value = "/{id}")
	public ResponseEntity<Response<ColaboradorDto>> atualizar( @PathVariable("id") Long id,
                                                               @Valid @RequestBody ColaboradorDto colaboradorDto, BindingResult result) throws NoSuchAlgorithmException {
		log.info("Atualizando funcionário: {}", colaboradorDto.toString());
		Response<ColaboradorDto> response = new Response<ColaboradorDto>();

		Optional<Colaborador> colaborador = this.colaboradorService.getById(id);
		if (!colaborador.isPresent()) {
			result.addError(new ObjectError("colaborador", "Funcionário não encontrado."));
		}

		this.atualizarDadosColaborador(colaborador.get(), colaboradorDto, result);

		if (result.hasErrors()) {
			log.error("Erro validando funcionário: {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}

		this.colaboradorService.persistir(colaborador.get());
		response.setData(this.converterColaboradorDto(colaborador.get()));

		return ResponseEntity.ok(response);
	}

	/**
	 * Atualiza os dados do funcionário com base nos dados encontrados no DTO.
	 * 
	 * @param colaborador
	 * @param colaboradorDto
	 * @param result
	 * @throws NoSuchAlgorithmException
	 */
	private void atualizarDadosColaborador(Colaborador colaborador, ColaboradorDto colaboradorDto, BindingResult result)
			throws NoSuchAlgorithmException {
		colaborador.setNome(colaboradorDto.getNome());

		if (!colaborador.getEmail().equals(colaboradorDto.getEmail())) {
			this.colaboradorService.getByEmail(colaboradorDto.getEmail())
					.ifPresent(func -> result.addError(new ObjectError("email", "Email já existente.")));
			colaborador.setEmail(colaboradorDto.getEmail());
		}

		colaborador.setQtdHorasAlmoco(null);
		colaboradorDto.getQtdHorasAlmoco()
				.ifPresent(qtdHorasAlmoco -> colaborador.setQtdHorasAlmoco(Float.valueOf(qtdHorasAlmoco)));

		colaborador.setQtdHorasTrabalhoDia(null);
		colaboradorDto.getQtdHorasTrabalhoDia()
				.ifPresent(qtdHorasTrabDia -> colaborador.setQtdHorasTrabalhoDia(Float.valueOf(qtdHorasTrabDia)));

		colaborador.setValorHora(null);
		colaboradorDto.getValorHora().ifPresent(valorHora -> colaborador.setValorHora(new BigDecimal(valorHora)));

		if (colaboradorDto.getSenha().isPresent()) {
			colaborador.setSenha(PasswordUtils.gerarBCrypt(colaboradorDto.getSenha().get()));
		}
	}

	/**
	 * Retorna um DTO com os dados de um funcionário.
	 * 
	 * @param colaborador
	 * @return ColaboradorDto
	 */
	private ColaboradorDto converterColaboradorDto(Colaborador colaborador) {
		ColaboradorDto colaboradorDto = new ColaboradorDto();
		colaboradorDto.setId(colaborador.getId());
		colaboradorDto.setEmail(colaborador.getEmail());
		colaboradorDto.setNome(colaborador.getNome());
		colaborador.getQtdHorasAlmocoOpt().ifPresent(
				qtdHorasAlmoco -> colaboradorDto.setQtdHorasAlmoco(Optional.of(Float.toString(qtdHorasAlmoco))));
		colaborador.getQtdHorasTrabalhoDiaOpt().ifPresent(
				qtdHorasTrabDia -> colaboradorDto.setQtdHorasTrabalhoDia(Optional.of(Float.toString(qtdHorasTrabDia))));
		colaborador.getValorHoraOpt()
				.ifPresent(valorHora -> colaboradorDto.setValorHora(Optional.of(valorHora.toString())));

		return colaboradorDto;
	}

}
