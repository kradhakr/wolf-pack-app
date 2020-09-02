package com.spring.wolves.packs.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "wolf")
public class Wolf extends AuditModel{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private long id;

	@Column(name = "wolf_name", nullable = false)
	private String wolfName;

	@Column(name = "wolf_gender", nullable = false)
	private String gender;

	@JsonFormat(pattern="yyyy-MM-dd")
	@Column(name = "wolf_birth_date", nullable = false)
	private Date birthDate;

	@Column(name = "wolf_location", nullable = false)
	private String location;
}
