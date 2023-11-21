package logan.tillman.ecom.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryDTO {
    Integer categoryId;
    String name;
}
