package vn.hoidanit.jobhunter.domain.DTO;


public class RestLoginDTO {
    private String access_token;

    public RestLoginDTO(String access_token) {
        this.access_token = access_token;
    }

    public RestLoginDTO() {
        //TODO Auto-generated constructor stub
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }


}
