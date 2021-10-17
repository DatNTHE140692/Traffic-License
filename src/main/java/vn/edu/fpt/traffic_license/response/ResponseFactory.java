package vn.edu.fpt.traffic_license.response;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import vn.edu.fpt.traffic_license.constants.ResponseStatusCodeConst;

@Component
public class ResponseFactory {

    public <D> ResponseEntity<Object> success(D data) {
        GeneralResponse<D> generalResponse = new GeneralResponse<>();
        generalResponse.setSuccess(true);
        generalResponse.setData(data);
        return ResponseEntity.ok().body(data);
    }

    public <D> ResponseEntity<Object> fail(D data, ResponseStatusCodeConst statusCode) {
        GeneralResponse<D> generalResponse = new GeneralResponse<>();
        generalResponse.setData(data);
        generalResponse.setSuccess(false);
        generalResponse.setMessage(statusCode.name());
        return ResponseEntity.status(statusCode.getHttpCode()).body(generalResponse);
    }

}
