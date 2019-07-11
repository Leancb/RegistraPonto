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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import registraponto.api.dtos.CadastroPFDto;
import registraponto.api.entities.Firma;
import registraponto.api.entities.Colaborador;
import registraponto.api.enums.PerfilEnum;
import registraponto.api.response.Response;
import registraponto.api.services.FirmaService;
import registraponto.api.services.ColaboradorService;
import registraponto.api.utils.PasswordUtils;

@RestController
@RequestMapping("/api/cadastrar-pf")
@CrossOrigin(origins = "*")
public class CadastroPFController {

	private static final Logger log = LoggerFactory.getLogger(CadastroPFController.class);
	
	@Autowired
	private FirmaService firmaService;
	
	@Autowired
	private ColaboradorService colaboradorService;

	public CadastroPFController() {
	}

	/**
	 * Cadastra um funcionário pessoa física no sistema.
	 * 
	 * @param cadastroPFDto
	 * @param result
	 * @return ResponseEntity<Response<CadastroPFDto>>
	 * @throws NoSuchAlgorithmException
	 */
	@PostMapping
	public ResponseEntity<Response<CadastroPFDto>> cadastrar( @Valid @RequestBody CadastroPFDto cadastroPFDto,
															  BindingResult result) throws NoSuchAlgorithmException {
		log.info("Cadastrando PF: {}", cadastroPFDto.toString());
		Response<CadastroPFDto> response = new Response<CadastroPFDto>();

		validarDadosExistentes(cadastroPFDto, result);
		Colaborador colaborador = this.converterDtoParaColaborador(cadastroPFDto, result);

		if (result.hasErrors()) {
			log.error("Erro validando dados de cadastro PF: {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}
		
		Optional<Firma> firma = this.firmaService.getByCnpj(cadastroPFDto.getCnpj());
		firma.ifPresent(emp -> colaborador.setFirma(emp));
		this.colaboradorService.persistir(colaborador);

		response.setData(this.converterCadastroPFDto(colaborador));
		return ResponseEntity.ok(response);
	}

	/**
	 * Verifica se a firma está cadastrada e se o funcionário não existe na base de dados.
	 * 
	 * @param cadastroPFDto
	 * @param result
	 */
	private void validarDadosExistentes(CadastroPFDto cadastroPFDto, BindingResult result) {
		Optional<Firma> firma = this.firmaService.getByCnpj(cadastroPFDto.getCnpj());
		if (!firma.isPresent()) {
			result.addError(new ObjectError("firma", "Firma não cadastrada."));
		}
		
		this.colaboradorService.getByCpf(cadastroPFDto.getCpf())
			.ifPresent(func -> result.addError(new ObjectError("colaborador", "CPF já existente.")));

		this.colaboradorService.getByEmail(cadastroPFDto.getEmail())
			.ifPresent(func -> result.addError(new ObjectError("colaborador", "Email já existente.")));
	}

	/**
	 * Converte os dados do DTO para funcionário.
	 * 
	 * @param cadastroPFDto
	 * @param result
	 * @return Colaborador
	 * @throws NoSuchAlgorithmException
	 */
	private Colaborador converterDtoParaColaborador(CadastroPFDto cadastroPFDto, BindingResult result)
			throws NoSuchAlgorithmException {
		Colaborador colaborador = new Colaborador();
		colaborador.setNome(cadastroPFDto.getNome());
		colaborador.setEmail(cadastroPFDto.getEmail());
		colaborador.setCpf(cadastroPFDto.getCpf());
		colaborador.setPerfil(PerfilEnum.ROLE_USUARIO);
		colaborador.setSenha(PasswordUtils.gerarBCrypt(cadastroPFDto.getSenha()));
		cadastroPFDto.getQtdHorasAlmoco()
				.ifPresent(qtdHorasAlmoco -> colaborador.setQtdHorasAlmoco(Float.valueOf(qtdHorasAlmoco)));
		cadastroPFDto.getQtdHorasTrabalhoDia()
				.ifPresent(qtdHorasTrabDia -> colaborador.setQtdHorasTrabalhoDia(Float.valueOf(qtdHorasTrabDia)));
		cadastroPFDto.getValorHora().ifPresent(valorHora -> colaborador.setValorHora(new BigDecimal(valorHora)));

		return colaborador;
	}

	/**
	 * Popula o DTO de cadastro com os dados do funcionário e firma.
	 * 
	 * @param colaborador
	 * @return CadastroPFDto
	 */
	private CadastroPFDto converterCadastroPFDto(Colaborador colaborador) {
		CadastroPFDto cadastroPFDto = new CadastroPFDto();
		cadastroPFDto.setId(colaborador.getId());
		cadastroPFDto.setNome(colaborador.getNome());
		cadastroPFDto.setEmail(colaborador.getEmail());
		cadastroPFDto.setCpf(colaborador.getCpf());
		cadastroPFDto.setCnpj(colaborador.getFirma().getCnpj());
		colaborador.getQtdHorasAlmocoOpt().ifPresent(qtdHorasAlmoco -> cadastroPFDto
				.setQtdHorasAlmoco(Optional.of(Float.toString(qtdHorasAlmoco))));
		colaborador.getQtdHorasTrabalhoDiaOpt().ifPresent(
				qtdHorasTrabDia -> cadastroPFDto.setQtdHorasTrabalhoDia(Optional.of(Float.toString(qtdHorasTrabDia))));
		colaborador.getValorHoraOpt()
				.ifPresent(valorHora -> cadastroPFDto.setValorHora(Optional.of(valorHora.toString())));

		return cadastroPFDto;
	}

}
