package life.heartcare.formprocessor.dto.mailchimp.enums;

public enum Language {

	// https://mailchimp.com/help/view-and-edit-contact-languages/

	PT("pt"), EN("en"), ES("es");

	private String languageCode;

	Language(String languageCode) {
		this.languageCode = languageCode;
	}

	@Override
	public String toString() {
		return languageCode;
	}

}
