package com.spring.wolves.packs.model;

import lombok.*;
import javax.persistence.*;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "packs")
public class Packs extends AuditModel{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private long id;

	@Column(name = "pack_name", nullable = false)
	private String packName;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "pw_fid", referencedColumnName = "id")
	private Set<Wolf> wolfList;

}
