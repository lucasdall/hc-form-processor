package life.heartcare.formprocessor.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import life.heartcare.formprocessor.dto.enums.Results;
import lombok.Data;

@Data
public class FormResponseResultDTO {

	/**
	 * 
	 */
	public static final long serialVersionUID = 803807158146010646L;

	private Long idFormResponse;

	private String email;

	private Date submittedAt;

	private Date savedAt;

	private String payload;

	private Results result;

	private String name;
	
	private List<String> symptoms = new ArrayList<>(0);
	
	private String channel;
	
	private DetailInfoDTO detail;
	
	private String link;
	
	public void loadDetail() {
		switch (result) {
		case TYPE_01_SymptomaticHighSuspicionOfCovid19:
			detail = DetailInfoDTO.builder()
				.linkAlertImg("https://covid.heartcare.life/assets/mail/assets/images/results-alarm-red.png")
				.showSymptoms(true)
				.disclaimer("seus sintomas e atividades nos relatam ALTA PROBABILIDADE de estar infectado pelo COVID-19.")
				.description("Os sintomas identificados por você são considerados graves para a COVID-19, principalmente os seguintes:")
				.recommendation(RecomendationDTO.builder()
						.title("Suporte médico")
						.recommendation("Entre em contato, por telefone, com as unidades de atendimento da COVID-19 na sua localidade, para relatar seus sintomas e pedir orientação específica. Se perceber qualquer aumento em seus sintomas, procure com urgência o atendimento hospitalar.")
						.recommendation("Conforme a orientação do profissional que lhe atender, decida quando você deverá procurar atendimento e em qual local de triagem ou atendimento da sua cidade.")
						.build())
				.recommendation(RecomendationDTO.builder()
						.title("Distanciamento social mais rígido")
						.recommendation("Se possível, fique mais isolado em sua casa, e evite sair à rua por mais alguns dias, até conseguir observar a evolução de seus sintomas. Evitando, assim, contaminar outras pessoas caso você realmente esteja infectado.")
						.build())
				.recommendation(RecomendationDTO.builder()
						.title("Medidas de proteção")
						.recommendation("Lave suas mãos e rosto com frequência, a cada 2 horas, mesmo quando estiver em casa.")
						.recommendation("Utilize sempre máscara, para proteção sua e das pessoas que convivem com você. Tome cuidado ao manusear este equipamento de proteção, evitando tocar nas faces da máscara quando colocá-la, retirá-la e ensacá-la para descarte.")
						.build())
				.recommendation(RecomendationDTO.builder()
						.title("Atenção às novas orientações e à evolução de seu caso")
						.recommendation("Fique atualizado sobre a evolução e o controle da pandemia através dos sites das agências de saúde e dos nossos conteúdos em <a href='https://covid.heartcare.life'>covid.heartcare.life</a>")
						.build()
				).build();
			break;
		case TYPE_02_SymptomaticLowerSuspicionOfCovid19:
			detail = DetailInfoDTO.builder()
				.linkAlertImg("https://covid.heartcare.life/assets/mail/assets/images/results-alarm-yellow.png")
				.showSymptoms(true)
				.disclaimer("seus sintomas e atividades demonstram PROBABILIDADE BAIXA de estar infectado pelo SARS-CoV-2, o vírus que causa a COVID-19. Caso se intensifique, poderá ser um caso da doença.")
				.description("Embora você tenha percebido algum sintoma da COVID-19, suas respostas não foram suficientes para classificar o seu caso como uma possível infecção, mas continue atento à evolução de seus sintomas, principalmente os seguintes:")
				.recommendation(RecomendationDTO.builder()
						.title("Suporte médico")
						.recommendation("O quadro de sintomas que você reportou não demonstra necessidade de suporte médico, neste momento.")
						.recommendation("Continue atento às alterações de seus sintomas, principalmente a Falta de Ar, a Febre, e a Tosse.")
						.recommendation("Caso estes sintomas evoluam, com relação à sua percepção nos dias anteriores, procure ajuda médica primeiro por suporte telefônico, antes de dirigir-se à uma unidade de atendimento de COVID-19.")
						.build())
				.recommendation(RecomendationDTO.builder()
						.title("Distanciamento social")
						.recommendation("Se possível, evite contato pessoal mesmo dentro de casa e evite sair à rua por mais alguns dias, pelo menos até conseguir observar a redução de seus sintomas. Evitando, assim, contaminar outras pessoas caso você realmente esteja infectado.")
						.build())
				.recommendation(RecomendationDTO.builder()
						.title("Medidas de proteção")
						.recommendation("Lave suas mãos e rosto com frequência, a cada 2 horas, mesmo quando estiver em casa.")
						.recommendation("Em ambientes públicos utilize sempre máscara, para proteção sua e das pessoas que com você interage. Tome cuidado ao manusear este equipamento de proteção, evitando tocar nas faces da máscara quando colocá-la, retirá-la e ensacá-la para descarte.")
						.build())
				.recommendation(RecomendationDTO.builder()
						.title("Atenção às novas orientações e à evolução de seu caso")
						.recommendation("Fique atualizado sobre a evolução e o controle da pandemia através dos sites das agências de saúde e dos nossos conteúdos em <a href='https://covid.heartcare.life'>covid.heartcare.life</a>")
						.build()
				).build();
			break;
		case TYPE_03_AsymptomaticLowerSuspicion:
			detail = DetailInfoDTO.builder()
				.linkAlertImg("https://covid.heartcare.life/assets/mail/assets/images/results-alarm-yellow.png")
				.disclaimer("as informações que você nos relatou demonstram PROBABILIDADE BAIXA de estar infectado pelo SARS-CoV-2, o vírus que causa a COVID-19.")
				.description("Embora você não tenha identificado qualquer sintoma relacionado à COVID-19, você indicou ter convivido com pessoas que estão ou estavam infectadas. E, por isso, existe a suspeita baixa de você ser um caso de contaminação, mas assintomático.")
				.recommendation(RecomendationDTO.builder()
						.title("Suporte médico")
						.recommendation("No seu estado atual, você definitivamente não necessita suporte médico com relação ao COVID-19.")
						.recommendation("Se precisar atendimento por algum outro motivo não vá diretamente aos hospitais e procure suporte telefônico inicialmente, evitando, assim, o contato com locais de grande potencial infeccioso.")
						.build())
				.recommendation(RecomendationDTO.builder()
						.title("Distanciamento social")
						.recommendation("Por não demonstrar sintomas da COVID-19, ou qualquer indisposição, não está afastada totalmente a possibilidade de você ter tido contato com o vírus, e estar infectado. Por isso, observe as regras oficiais de distanciamento social aplicáveis à sua região.")
						.build())
				.recommendation(RecomendationDTO.builder()
						.title("Medidas de proteção")
						.recommendation("Lave suas mãos e rosto com frequência, a cada 2 horas, mesmo quando estiver em casa.")
						.recommendation("Sempre que utilizar máscara, para proteção sua e das pessoas que convivem com você, tome cuidado ao manusear este equipamento de proteção. Evitando tocar nas faces da máscara quando colocá-la, retirá-la e ensacá-la para descarte.")
						.build())
				.recommendation(RecomendationDTO.builder()
						.title("Atenção às novas orientações e à evolução de seu caso")
						.recommendation("Fique atualizado sobre a evolução e o controle da pandemia através dos sites das agências de saúde e dos nossos conteúdos em <a href='https://covid.heartcare.life'>covid.heartcare.life</a>")
						.build()
				).build();
			break;
		case TYPE_04_AsymptomaticHighSuspicion:
			detail = DetailInfoDTO.builder()
				.linkAlertImg("https://covid.heartcare.life/assets/mail/assets/images/results-alarm-red.png")
				.disclaimer("suas atividades demonstram PROBABILIDADE ALTA de estar infectado pelo SARS-CoV-2, o vírus que causa a COVID-19, apesar de ter reportado não perceber qualquer sintoma.")
				.description("Você deve continuar atento aos sintomas e sinais do seu corpo, isso porque você teve contato com pessoas sabidamente infectadas.")
				.description("Pelo alto nível de transmissibilidade do vírus, e por você não ter conseguido seguir idealmente as orientações preventivas de proteção e higiene, você pode ter sido infectado também, apesar de não parecer.")
				.recommendation(RecomendationDTO.builder()
						.title("Suporte médico")
						.recommendation("No seu estado atual, você definitivamente não necessita suporte médico com relação ao COVID-19.")
						.recommendation("Se precisar atendimento por algum outro motivo não vá diretamente aos hospitais e procure suporte telefônico inicialmente, evitando, assim, o contato com locais de grande potencial infeccioso.")
						.recommendation("Continue atento aos sintomas principais da COVID-19, que são: Falta de Ar, Febre Persistente e Tosse Seca.")
						.build())
				.recommendation(RecomendationDTO.builder()
						.title("Distanciamento social mais rígido")
						.recommendation("Por não demonstrar sintomas da COVID-19, ou qualquer indisposição, não está afastada totalmente a possibilidade de você ter tido contato com o vírus, e estar infectado. Por isso, observe as regras oficiais de distanciamento social aplicáveis à sua região.")
						.build())
				.recommendation(RecomendationDTO.builder()
						.title("Medidas de proteção")
						.recommendation("Lave suas mãos e rosto com frequência, a cada 2 horas, mesmo quando estiver em casa.")
						.recommendation("Sempre que utilizar máscara, para proteção sua e das pessoas que convivem com você, tome cuidado ao manusear este equipamento de proteção. Evitando tocar nas faces da máscara quando colocá-la, retirá-la e ensacá-la para descarte.")
						.build())
				.recommendation(RecomendationDTO.builder()
						.title("Atenção às novas orientações e à evolução de seu caso")
						.recommendation("Fique atualizado sobre a evolução e o controle da pandemia através dos sites das agências de saúde e dos nossos conteúdos em <a href='https://covid.heartcare.life'>covid.heartcare.life</a>")
						.build()
				).build();
			break;
		case TYPE_05_SymptomaticLowerSuspicion:
			detail = DetailInfoDTO.builder()
				.linkAlertImg("https://covid.heartcare.life/assets/mail/assets/images/results-alarm-grey.png")
				.disclaimer("seus sintomas e atividades demonstram PROBABILIDADE BAIXA de estar infectado pelo SARS-CoV-2, o vírus que causa a COVID-19.")
				.description("Embora você tenha percebido algum sintoma da COVID-19, suas respostas não foram suficientes para classificar o seu caso como uma possível infecção, mas continue atento à evolução de seus sintomas.")
				.description("Se eles se intensificarem poderá ser um caso de COVID-19.")
				.recommendation(RecomendationDTO.builder()
						.title("Suporte médico")
						.recommendation("No seu estado atual, o quadro de sintomas reportado por você não demonstra necessidade de suporte médico.")
						.recommendation("Continue atento aos sintomas principais da COVID-19, que são: Falta de Ar, Febre Persistente e Tosse Seca.")
						.recommendation("Caso estes sintomas evoluam com relação à sua percepção nos dias anteriores, procure ajuda médica primeiro por suporte telefônico, antes de se dirigir a uma unidade de atendimento de COVID-19.")
						.build())
				.recommendation(RecomendationDTO.builder()
						.title("Distanciamento social rígido")
						.recommendation("Se possível, evite contato pessoal mesmo dentro de casa e evite sair à rua por mais alguns dias, pelo menos até conseguir observar a redução de seus sintomas. Evitando, assim, contaminar outras pessoas caso você realmente esteja infectado.")
						.build())
				.recommendation(RecomendationDTO.builder()
						.title("Medidas de proteção")
						.recommendation("Lave suas mãos e rosto com frequência, a cada 2 horas, mesmo quando estiver em casa.")
						.recommendation("Em ambientes públicos utilize sempre máscara, para proteção sua e das pessoas que com você interage. Tome cuidado ao manusear este equipamento de proteção, evitando tocar nas faces da máscara quando colocá-la, retirá-la e ensacá-la para descarte.")
						.build())
				.recommendation(RecomendationDTO.builder()
						.title("Atenção às novas orientações e à evolução de seu caso")
						.recommendation("Fique atualizado sobre a evolução e o controle da pandemia através dos sites das agências de saúde e dos nossos conteúdos em <a href='https://covid.heartcare.life'>covid.heartcare.life</a>")
						.build()
				).build();
			break;
		case TYPE_06_SymptomaticFluSuspicion:
			detail = DetailInfoDTO.builder()
				.linkAlertImg("https://covid.heartcare.life/assets/mail/assets/images/results-alarm-grey.png")
				.disclaimer("seus sintomas e atividades e sintomas nos relatam PROBABILIDADE BAIXÍSSIMA de estar com COVID-19.")
				.description("Embora você tenha reportado algum sintoma que poderia parecer do novo coronavírus, suas respostas não foram suficientes para classificar o seu caso como uma possível contaminação pelo COVID-19.")
				.description("Seu caso tem MAIOR PROBABILIDADE de se tratar de outras gripes ou viroses, de qualquer modo fique atento a qualquer evolução dos sintomas abaixo relatados por você ou para o surgimento repentino de algum outro sintoma.")
				.recommendation(RecomendationDTO.builder()
						.title("Suporte médico")
						.recommendation("No seu estado atual, o quadro de sintomas reportado por você não demonstra necessidade de suporte médico.")
						.recommendation("Continue atento aos sintomas principais da COVID-19, que são: Falta de Ar, Febre Persistente e Tosse Seca.")
						.recommendation("Caso estes sintomas evoluam com relação à sua percepção nos dias anteriores, procure ajuda médica primeiro por suporte telefônico, antes de se dirigir a uma unidade de atendimento de COVID-19.")
						.build())
				.recommendation(RecomendationDTO.builder()
						.title("Distanciamento social rígido")
						.recommendation("Se possível, evite contato pessoal mesmo dentro de casa e evite sair à rua por mais alguns dias, pelo menos até conseguir observar a redução de seus sintomas. Evitando, assim, contaminar outras pessoas caso você realmente esteja infectado.")
						.build())
				.recommendation(RecomendationDTO.builder()
						.title("Medidas de proteção")
						.recommendation("Lave suas mãos e rosto com frequência, a cada 2 horas, mesmo quando estiver em casa.")
						.recommendation("Pelos seus sintomas reportados, seu sistema imunológico está sendo exigido por estágios iniciais de uma gripe, e, por isso, não está em plena capacidade de defesa.")
						.recommendation("Mantenha uma dieta saudável e que ajude a fortalecer seu sistema imunológico, além de manter o hábito de tomar sol, e fisicamente ativo, obedecendo o distanciamento social recomendado.")
						.recommendation("Em ambientes públicos utilize sempre máscara, para proteção sua e das pessoas que com você interage. Tome cuidado ao manusear este equipamento de proteção, evitando tocar nas faces da máscara quando colocá-la, retirá-la e ensacá-la para descarte.")
						.build())
				.recommendation(RecomendationDTO.builder()
						.title("Atenção às novas orientações e à evolução de seu caso")
						.recommendation("Fique atualizado sobre a evolução e o controle da pandemia através dos sites das agências de saúde e dos nossos conteúdos em <a href='https://covid.heartcare.life'>covid.heartcare.life</a>")
						.build()
				).build();
			break;
		case TYPE_07_HasNothingJustCurious:
			detail = DetailInfoDTO.builder()
				.linkAlertImg("https://covid.heartcare.life/assets/mail/assets/images/results-alarm-grey.png")
				.disclaimer("a ausência de qualquer sintoma, e as suas atividades, nos relatam PROBABILIDADE REMOTA de estar infectado com COVID-19.")
				.description("Não podemos indicar probabilidade inexistente, em função do alto nível de transmissibilidade do novo coronavírus, e pelo comportamento pandêmico da doença. E também, por ser conhecido pela ciência que, muitas pessoas não ficam doentes ou sentem qualquer sintoma em caso de infecção.")
				.recommendation(RecomendationDTO.builder()
						.title("Suporte médico")
						.recommendation("Em relação à COVID-19 você não tem qualquer necessidade de atenção médica.")
						.recommendation("Continue atento aos sintomas principais da COVID-19, que são: Falta de Ar, Febre Persistente e Tosse Seca.")
						.recommendation("Se precisar atendimento por algum outro motivo não vá diretamente aos hospitais e procure suporte telefônico inicialmente, evitando, assim, o contato com locais de grande potencial infeccioso.")
						.build())
				.recommendation(RecomendationDTO.builder()
						.title("Distanciamento social")
						.recommendation("Se possível, evite contato pessoal mesmo dentro de casa e respeite as regras de distanciamento social da sua localidade.")
						.build())
				.recommendation(RecomendationDTO.builder()
						.title("Medidas de proteção")
						.recommendation("Lave suas mãos e rosto com frequência, a cada 2 horas, mesmo quando estiver em casa.")
						.recommendation("Em ambientes públicos utilize sempre máscara, para proteção sua e das pessoas que com você interage. Tome cuidado ao manusear este equipamento de proteção, evitando tocar nas faces da máscara quando colocá-la, retirá-la e ensacá-la para descarte.")
						.build())
				.recommendation(RecomendationDTO.builder()
						.title("Acompanhe também as novas orientações sobre a pandemia")
						.recommendation("Fique atualizado sobre a evolução e o controle da pandemia através dos sites das agências de saúde e dos nossos conteúdos em <a href='https://covid.heartcare.life'>covid.heartcare.life</a>")
						.build()
				).build();
			break;
		case TYPE_08_DiagnosedNotCuredSymptomatic:
			detail = DetailInfoDTO.builder()
				.linkAlertImg("https://covid.heartcare.life/assets/mail/assets/images/results-alarm-red.png")
				.showSymptoms(true)
				.disclaimer("de acordo com suas respostas, você já foi DIAGNOSTICADO POSITIVO para COVID-19, e percebe sintomas desta doença.")
				.description("Por não estar ainda curado, você carrega o vírus no seu corpo, podendo contagiar outras pessoas e contaminar superfícies.")
				.description("Seu quadro requer muito cuidado, e, se você não estiver em tratamento, a qualquer sinal de evolução dos sintomas procure novamente o seu médico ou hospital.")
				.description("Fique bastante atento aos sintomas aqui relatados por você:")
				.recommendation(RecomendationDTO.builder()
						.title("Suporte médico")
						.recommendation("Pelo diagnóstico positivo para COVID-19, caso ainda não tenha feito, você deve procurar atendimento médico com urgência.")
						.recommendation("Para isso, informe seus sinais e sintomas para receber as orientações específicas e aplicáveis ao seu atendimento.")
						.recommendation("Primeiro em contato telefônico com unidade de atendimento de COVID-19 na sua cidade ou com o serviço público de atendimento de emergência.")
						.recommendation("Continue muito atento à evolução dos sintomas principais da COVID-19, que são: Febre Persistente, Tosse Seca e Falta de Ar.")
						.build())
				.recommendation(RecomendationDTO.builder()
						.title("Distanciamento social")
						.recommendation("Até que você seja diagnóstico curado pelos seus médicos, mantenha todas as ações de prevenção recomendadas na sua região, principalmente o distanciamento social.")
						.recommendation("Evite contato pessoal mesmo dentro de casa e evite sair à rua por mais alguns dias, pelo menos até conseguir observar a redução de seus sintomas. Evitando, assim, contaminar outras pessoas caso você realmente esteja infectado.")
						.build())
				.recommendation(RecomendationDTO.builder()
						.title("Medidas de proteção")
						.recommendation("Lave suas mãos e rosto com frequência, a cada 2 horas, mesmo quando estiver em casa.")
						.recommendation("Também lave com maior frequência suas roupas, roupas de cama e toalhas. Não use a mesma toalha de rosto que outras pessoas.")
						.recommendation("Em casa e em ambientes públicos utilize sempre máscara, para proteção sua e das pessoas que com você interage. Tome cuidado ao manusear este equipamento de proteção, evitando tocar nas faces da máscara quando colocá-la, retirá-la e ensacá-la para descarte.")
						.build())
				.recommendation(RecomendationDTO.builder()
						.title("Atenção às novas orientações e à evolução de seu caso")
						.recommendation("Fique atualizado sobre a evolução e o controle da pandemia através dos sites das agências de saúde e dos nossos conteúdos em <a href='https://covid.heartcare.life'>covid.heartcare.life</a>")
						.build()
				).build();
			break;
		case TYPE_09_DiagnosedNotCuredAsymptomatic:
			detail = DetailInfoDTO.builder()
				.linkAlertImg("https://covid.heartcare.life/assets/mail/assets/images/results-alarm-red.png")
				.disclaimer("de acordo com suas respostas, você já foi DIAGNOSTICADO POSITIVO para COVID-19, apesar de não perceber os sintomas desta doença.")
				.description("Por não estar ainda curado, você carrega o vírus no seu corpo mesmo não tendo sua saúde afetada por ele, e pode contagiar outras pessoas e contaminar superfícies.")
				.description("Seu quadro requer cuidado. A qualquer sinal do surgimento de algum dos sintomas principais da COVID-19, procure novamente o seu médico ou hospital para uma nova avaliação.")
				.recommendation(RecomendationDTO.builder()
						.title("Suporte médico")
						.recommendation("Pelo diagnóstico positivo para COVID-19, caso ainda não tenha feito, você deve procurar atendimento médico.")
						.recommendation("Para isso, informe seus sinais e sintomas para receber as orientações específicas e aplicáveis ao seu atendimento.  Primeiro em contato telefônico com unidade de atendimento de COVID-19 na sua cidade ou com o serviço público de atendimento de emergência.")
						.recommendation("Continue muito atento ao surgimento dos sintomas pritncipais da COVID-19, que são: Falta de Ar, Febre Persistente e Tosse Seca.")
						.build())
				.recommendation(RecomendationDTO.builder()
						.title("Distanciamento social")
						.recommendation("Até que você seja diagnóstico curado pelos seus médicos, mantenha todas as ações de prevenção recomendadas na sua região, principalmente o distanciamento social.")
						.recommendation("Evite contato pessoal mesmo dentro de casa e evite sair à rua por mais alguns dias, pelo menos até conseguir observar a redução de seus sintomas. Evitando, assim, contaminar outras pessoas caso você realmente esteja infectado.")
						.build())
				.recommendation(RecomendationDTO.builder()
						.title("Medidas de proteção")
						.recommendation("Lave suas mãos e rosto com frequência, a cada 2 horas, mesmo quando estiver em casa.")
						.recommendation("Também lave com maior frequência suas roupas, roupas de cama e toalhas. Não use a mesma toalha de rosto que outras pessoas.")
						.recommendation("Em casa e em ambientes públicos utilize sempre máscara, para proteção sua e das pessoas que com você interage. Tome cuidado ao manusear este equipamento de proteção, evitando tocar nas faces da máscara quando colocá-la, retirá-la e ensacá-la para descarte.")
						.build())
				.recommendation(RecomendationDTO.builder()
						.title("Atenção às novas orientações e à evolução de seu caso")
						.recommendation("Fique atualizado sobre a evolução e o controle da pandemia através dos sites das agências de saúde e dos nossos conteúdos em <a href='https://covid.heartcare.life'>covid.heartcare.life</a>")
						.build()
				).build();
			break;
		case TYPE_10_DiagnosedCured:
			detail = DetailInfoDTO.builder()
				.linkAlertImg("https://covid.heartcare.life/assets/mail/assets/images/results-alarm-grey.png")
				.disclaimer("ficamos muito felizes em ver que você se já superou a COVID-19, e está DIAGNOSTICADO COMO CURADO!")
				.description("Você faz parte de um seleto grupo de pessoas que já contraiu essa infecção respiratória e que o organismo já combateu o vírus, e produziu anticorpos para anular a sua ação sobre as células saudáveis do seu corpo.")
				.recommendation(RecomendationDTO.builder()
						.title("Suporte médico")
						.recommendation("Em relação à COVID-19 você não tem qualquer necessidade de atenção médica. De qualquer forma continue atento aos sintomas principais da COVID-19, que são: Febre Persistente, Tosse Seca e Falta de Ar.")
						.recommendation("Não existe notícia de pessoas que tenham sido infectadas após criarem seus próprios anticorpos, mas a atenção aos sinais de seu corpo continuam sendo importantes.")
						.build())
				.recommendation(RecomendationDTO.builder()
						.title("Distanciamento social")
						.recommendation("Por estar curado, você não transmite mais o vírus. Portanto poderá retomar a vida normal, independente de distanciamento social.")
						.recommendation("Caso em sua cidade exista determinação de alguma forma de distanciamento social, procure se informar com a administração local como você pode retomar suas atividades normais, sendo uma pessoa que não mais transmite o vírus SARS-CoV-2.")
						.build())
				.recommendation(RecomendationDTO.builder()
						.title("Colaboração social e medidas de proteção")
						.recommendation("Você faz parte do pequeno número de pessoas sabidamente imunes ao vírus.")
						.recommendation("Por isso você pode contribuir com toda a comunidade doando sangue e ajudando a repor os estoques de sangue dos hemocentros. Você faz parte do pequeno grupo de pessoas que pode também ser doador de sangue para separação do seu plasma que está sendo utilizado em pesquisas clínicas e tratamentos experimentais para a COVID-19.")
						.recommendation("Ajude outras pessoas a também combater esse vírus.")
						.recommendation("Por fim, mantenha na sua vida estes hábitos de higiene amplamente divulgados como prevenção da COVID-19.")
						.build())
				.recommendation(RecomendationDTO.builder()
						.title("Acompanhe também as novas orientações sobre a pandemia")
						.recommendation("Fique atualizado sobre a evolução e o controle da pandemia através dos sites das agências de saúde e dos nossos conteúdos em <a href='https://covid.heartcare.life'>covid.heartcare.life</a>")
						.build()
				).build();
			break;
		case TYPE_11_Unknown:
			detail = DetailInfoDTO.builder()
				.linkAlertImg("https://covid.heartcare.life/assets/mail/assets/images/results-alarm-grey.png")
				.disclaimer("tivemos alguma inconsistência e não conseguimos identificar o seu caso para lhe passar um resultado.")
				.description("Pedimos que faça este autoteste novamente. Se esta resposta aparecer outra vez, queremos ouvir você para melhorar este serviço.")
				.description("Neste caso, por favor, nos informe no e-mail: covid@heartcare.life")
				.description("Obrigado por participar, mas volte!")
				.description("Queremos ajudar você a identificar sua probabilidade de infecção com a COVID-19.")
				.build();
			break;

		default:
			break;
		}
	}

}
