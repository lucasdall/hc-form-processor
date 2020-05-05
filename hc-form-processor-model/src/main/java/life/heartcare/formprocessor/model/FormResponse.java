package life.heartcare.formprocessor.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import life.heartcare.formprocessor.dto.enums.ComorbiditiesScore;
import life.heartcare.formprocessor.dto.enums.Results;
import lombok.Data;

@Data
@Entity
@Table(name = "form_response", indexes = { @Index(name = "idx_form_response", columnList = "email", unique = false) })
public class FormResponse {

	/**
	 * 
	 */
	public static final long serialVersionUID = 803807158146010646L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_form_response")
	private Long idFormResponse;

	private String email;

	private String name;
	
	private String phone;
	
	private String country;

	@Column(name = "event_id")
	private String eventId;

	@Column(name = "event_type")
	private String eventType;

	@Column(name = "form_id")
	private String formId;

	@Column(name = "content_type")
	private String contentType;

	@Column(name = "submitted_at")
	@Temporal(TemporalType.TIMESTAMP)
	private Date submittedAt;

	@Column(name = "saved_at")
	@Temporal(TemporalType.TIMESTAMP)
	private Date savedAt;

	@Lob
	private String payload;

	@Enumerated(EnumType.STRING)
	private Results result;

	@Column(name = "mailchimp_id")
	private String mailchimpId;

	@Enumerated(EnumType.STRING)
	@Column(name = "comorbidities_score")
	private ComorbiditiesScore comorbiditiesScore;
	
}
