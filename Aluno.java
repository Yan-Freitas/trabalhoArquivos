
public class Aluno {
	//Esse negócio só presta pra ter o construtor
	private String nome;
	private String respostas;
	private String gabarito;
	public Aluno(String nome, String respostas) {
		this.setNome(nome);
		this.setRespostas(respostas);
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getRespostas() {
		return respostas;
	}
	public void setRespostas(String respostas) {
		this.respostas = respostas;
	}

	public void setGabarito(String gabarito) {

	}

	public int gerarNota() {
		if (gabarito == null)
			return 0;
			
		if (respostas.chars().allMatch(c -> c == respostas.charAt(0))) {
			return 0;
		}
		
		int acertos = 0;
		for(int x = 0;x<10;x++) {
			if(getRespostas().charAt(x)==gabarito.charAt(x)) {
				acertos++;
			}
		}
		
		return acertos;
	}
}
