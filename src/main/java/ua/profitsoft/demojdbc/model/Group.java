package ua.profitsoft.demojdbc.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Group  {

	private Long id;
	private String name;
	private int course;

}
