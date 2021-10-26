package vn.edu.fpt.traffic_license.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.Map;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GeneralResponse<D> {

    @JsonProperty("success")
    private boolean success;

    @JsonProperty("message")
    private String message;

    @JsonProperty("data")
    private D data;

    public static <T> Object paginated(PaginationMetadata paginationMetadata, T items) {
        Map<String, Object> data = new HashMap<>();
        data.put("page_data", items);
        data.put("pagination", paginationMetadata);
        return data;
    }

    @Getter
    @Accessors(chain = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PaginationMetadata {
        private final int limit;
        private final long totalElements;
        private final int totalPages;
        private final int currentPage;

        public PaginationMetadata(int limit, long totalElements, int totalPages, int currentPage) {
            this.limit = limit;
            this.totalElements = totalElements;
            this.totalPages = totalPages;
            this.currentPage = currentPage;
        }
    }

}
