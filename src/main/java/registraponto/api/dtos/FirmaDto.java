package registraponto.api.dtos;

public class FirmaDto {
	
	private Long id;
	private String razaoSocial;
	private String cnpj;

	public FirmaDto() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRazaoSocial() {
		return razaoSocial;
	}

	public void setRazaoSocial(String razaoSocial) {
		this.razaoSocial = razaoSocial;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	@Override
	public String toString() {
		return "FirmaDto [id=" + id + ", razaoSocial=" + razaoSocial + ", cnpj=" + cnpj + "]";
	}

}
