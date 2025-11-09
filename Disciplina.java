
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Disciplina {
	//Essa classe contém praticamente todos os métodos usados
	//Criação dos atributos;
	private String nome;
	private ArrayList<Aluno> alunos = new ArrayList<Aluno>();
	private String gabarito;
	
	public Disciplina(String nome) {
		this.setNome(nome);
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String getGabarito() {
		return gabarito;
	}
	//Isso daqui retorna a quantidade de alunos que tem no arraylist de alunos, criei isso pra usar em outro método aqui;
	public int getTamanho() {
		return alunos.size();
	}
	public void setGabarito(String gabarito) {
		this.gabarito = gabarito;
	}
	//Isso daqui retorna um array de alunos, eu criei esse método pra deixar outro processo lá no main mais bonito
	public Aluno[] getAlunos() {
		Aluno[] turma = new Aluno[getTamanho()];
		for(int i = 0;i<getTamanho();i++) {
			turma[i] = alunos.get(i);
		}
		return turma;
	}
	//Adiciona um aluno no arraylist
	public void adicionarAluno(String nome, String respostas) {
		alunos.add(new Aluno(nome,respostas));
	}
	//Isso daqui adiciona um aluno no arraylist e também dá um filewrite na disciplina que adicionou o aluno
	public void adicionarAluno(String diretorioAbsoluto, String nome, String respostas) {
		try {
			alunos.add(new Aluno(nome,respostas));
			File disciplinaArquivo = new File(diretorioAbsoluto+"/disciplinas/"+this.nome);
			FileWriter filewriter;
			//Isso aqui é pra checar se já tem algo escrito no arquivo de texto, funciona
			if(disciplinaArquivo.length()==0){
				filewriter = new FileWriter(diretorioAbsoluto+"/disciplinas/"+this.nome);
				filewriter.write(respostas.toUpperCase()+"	"+nome.substring(0, 1).toUpperCase() + nome.substring(1));
			}
			else {
				filewriter = new FileWriter(diretorioAbsoluto+"/disciplinas/"+this.nome,true);
				filewriter.append("\n"+respostas.toUpperCase()+"	"+nome.substring(0, 1).toUpperCase() + nome.substring(1));
			}
			filewriter.close();
			System.out.println("Aluno " + nome + " adicionado!");
		}catch (IOException e) {
		      System.out.println("Ocorreu um erro");
		      e.printStackTrace();
	    }
	}
	//Isso aqui tá estático porque não depende de objeto e na verdade é pra criar o arquivo de texto da disciplina, então posso só invocar pela classe
	public static boolean criarDisciplina(String diretorioAbsoluto, String nome) {
		try {
			File disciplina = new File(diretorioAbsoluto+"/disciplinas/"+nome);
			//Isso aqui é pra checar se o arquivo já existe
			if(disciplina.createNewFile()) {
				System.out.println("Disciplina "+disciplina.getName()+" criada!");
				return true;
			}else {
				System.out.println("Disciplina já existe");
				return false;
			}
		}catch(IOException e) {
			System.out.println("Um erro ocorreu");
		    e.printStackTrace();
		}
		return true;
	}
	//Isso aqui gera o gabarito da disciplina que fica salvo na pasta de gabaritos
	public void gerarGabarito(String diretorioAbsoluto, String respostas) {
		try {
			File disciplinaGabarito = new File(diretorioAbsoluto+"/disciplinas/gabaritos/"+"Gabarito"+" "+nome);
			FileWriter filewriter;
			gabarito = respostas;
			//Isso aqui é pra checar se o arquivo já existe
			if(disciplinaGabarito.createNewFile()) {
				filewriter = new FileWriter(diretorioAbsoluto+"/disciplinas/gabaritos/"+"Gabarito"+" "+nome);
				filewriter.write(respostas.toUpperCase());
				System.out.println("Gabarito gerado!");
			}
			else {
				System.out.println("Gabarito já existe, anexando novas respostas ao gabarito existente");
				filewriter = new FileWriter(diretorioAbsoluto+"/disciplinas/gabaritos/"+"Gabarito"+" "+nome);
				filewriter.write(respostas.toUpperCase());
			}
			filewriter.close();
		}catch(IOException e){
		  System.out.println("Ocorreu um erro");
	      e.printStackTrace();
		}
	}
	//Isso aqui era pra gerar o resultado do gabarito que ficaria salvo na pasta resultados, só que ainda não tá completa
	//FINALIZAR MÉTODO
	public void gerarResultados(String diretorioAbsoluto) {
		for(int i = 0;i<alunos.size();i++) {
			try {
				int acertos = 0;
				//Isso aqui de checar os acertos já tá prestando
				for(int x = 0;x<10;x++) {
					if(alunos.get(i).getRespostas().charAt(x)==gabarito.charAt(x)) {
						acertos++;
					}
				}
				//Ajeitar esse negócio pra escrever em ordem alfabética e decrescente
				//Como foi dito acima precisa ajeitar pra escrever em ordem alfabética e decrescente
				FileWriter filewriter;
				File resultadoAlfabetico = new File(diretorioAbsoluto+"/disciplinas/resultados/alfabetico/"+alunos.get(i).getNome());
				filewriter = new FileWriter(diretorioAbsoluto+"/disciplinas/gabaritos/"+"resultados/alfabetico/"+alunos.get(i).getNome());
				filewriter.write(alunos.get(i).getRespostas().toUpperCase()+"	"+alunos.get(i).getNome()+" -- "+acertos+" acertos");
				filewriter.close();
				File resultadoDecrescente = new File(diretorioAbsoluto+"/disciplinas/resultados/decrescente/"+ alunos.get(i).getNome());
				filewriter = new FileWriter(diretorioAbsoluto+"/disciplinas/gabaritos/"+"resultados/decrescente/"+alunos.get(i).getNome());
				filewriter.close();
			}catch(IOException e){
				  System.out.println("Ocorreu um erro");
			      e.printStackTrace();
			}
		}
	}
	//Isso aqui é pra achar o diretório no qual os arquivos estão e possibilitar a criação dos arquivos em qualquer máquina
	public static String acharDiretorioAbsoluto(){
		File arquivoTemp;
		File arquivoTemp2;
		arquivoTemp = new File("arquivotemp.txt");
		arquivoTemp2 = arquivoTemp.getAbsoluteFile().getParentFile();
		arquivoTemp2.deleteOnExit();
		arquivoTemp.deleteOnExit();
		return arquivoTemp2.getAbsolutePath();
	}
}
