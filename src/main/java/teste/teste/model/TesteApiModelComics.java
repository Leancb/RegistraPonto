package teste.teste.model;

public class TesteApiModelComics {

    private String code;
    private String status;

    public TesteApiModelComics( String code, String status ) {
        this.code = code;
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode( String code ) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus( String status ) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "TesteApiModelComics{" +
                "code='" + code + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

}
