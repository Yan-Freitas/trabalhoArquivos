package trabalhoArquivo;

public class Aluno {
	//Esse negócio só presta pra ter o construtor
	private String nome;
	private String respostas;
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
}
