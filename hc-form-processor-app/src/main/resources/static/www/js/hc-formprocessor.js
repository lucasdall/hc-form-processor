import * as typeformEmbed from '@typeform/embed'
	
//setTimeout(function(){
//  reference.close()
//}, 10000)

const openForm = () => {
	const reference = typeformEmbed.makePopup(
			  'https://heartcare.typeform.com/to/tYurhr', 
					  {
					    mode: 'popup',
					    autoClose: 3000,
					    hideHeaders: true,
					    hideFooters: true,
					    onSubmit: function () {
					      console.log('Typeform successfully submitted')
					    }
					  }
					)
}

export {openForm}