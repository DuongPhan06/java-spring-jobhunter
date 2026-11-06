package vn.hoidanit.jobhunter.domain.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
public class Meta {
    private int Page;
    private int PageSize;
    private int TotalPages;
    private long TotalElements;
}
