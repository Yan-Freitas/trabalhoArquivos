public class Aluno {
	//Esse negócio só presta pra ter o construtor
	private String nome;
	private String respostas;
	private int pontuacao;
	public Aluno(String nome, String respostas) {
		this.setNome(nome);
		this.setRespostas(respostas);
		this.pontuacao=0;
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
	public int getPontuacao() {
		return pontuacao;
	}
	public void setPontuacao(int pontuacao) {
		this.pontuacao = pontuacao;
	}
	public void gerarNota(String respostasCorretas){
		pontuacao=0;
		for (int i=0;i<10;i++){
			if (respostas.charAt(i)==respostasCorretas.charAt(i)){
				pontuacao++;
			}
		}
		if (respostas.toUpperCase().equals("VVVVVVVVVV") || respostas.toUpperCase().equals("FFFFFFFFFF")){
			pontuacao=0;
		}
	}
}
