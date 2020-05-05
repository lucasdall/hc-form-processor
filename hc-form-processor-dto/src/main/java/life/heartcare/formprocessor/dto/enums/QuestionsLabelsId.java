package life.heartcare.formprocessor.dto.enums;

public enum QuestionsLabelsId {

	HC_TEST("hc-test"), HC_SYMPTOMS_TYPE("hc-symptoms-type"), HC_SYMPTOMS_CRITICAL("hc-symptoms-critical"), HC_SYMPTOMS_OTHERS("hc-symptoms-others"),
	HC_CONTACT_INFECTED("hc-contact-infected"), HC_SYMPTOMS_BREATHE("hc-symptoms-breathe"), HC_PROTECTED_PARTNERS("hc-protect-partners"),
	HC_COVID_RECOVERED("hc-covid-recovered"), HC_EMAIL("hc-email"), HC_NAME("hc-name"), HC_PHONE("hc-phone"), HC_COUNTRY("hc-country"),
	HC_BIRTHDAY("hc-birthday"), HC_HEIGHT("hc-height"), HC_WEIGHT("hc-weight"), HC_HEALTH_HISTORY("hc-health-history"), HC_SMOKE("hc-smoke")
	;

	QuestionsLabelsId(String labelId) {
		this.labelId = labelId;
	}

	private String labelId;

	@Override
	public String toString() {
		return labelId;
	}
	
}
