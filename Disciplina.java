
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
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

	public void setGabarito(String gabarito) {
		this.gabarito = gabarito;
	}

	//Isso daqui retorna a quantidade de alunos que tem no arraylist de alunos, criei isso pra usar em outro método aqui;
	public int getTamanho() {
		return alunos.size();
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
		String respostasCorretas=null;
		try {
			FileReader fileReader = new FileReader(diretorioAbsoluto+"/disciplinas/gabaritos/"+"Gabarito"+" "+nome);
			BufferedReader bf = new BufferedReader(fileReader);
			respostasCorretas = bf.readLine();
			bf.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}catch (IOException e){
			e.printStackTrace();
		}
		System.out.println(respostasCorretas);
		for(int i = 0;i<alunos.size();i++) {
			alunos.get(i).gerarNota(respostasCorretas);
		}
		ArrayList<Aluno> alunosAlfabetico = new ArrayList<>(alunos);
		ArrayList<Aluno> alunosDecrescente = new ArrayList<>(alunos);
		int quantidadeAlunos=alunos.size();
		for (int i=0;i<quantidadeAlunos-2;i++){
			for (int j=i;j<quantidadeAlunos-1;j++){
				Aluno aux = alunosDecrescente.get(j+1);
				//Ordenação Decrescente
				if(alunosDecrescente.get(j+1).getPontuacao()>alunosDecrescente.get(j).getPontuacao()){
					alunosDecrescente.set(j+1,alunosDecrescente.get(j));
					alunosDecrescente.set(j,aux);
				}
				//Ordenação Alfabética
				if(alunosAlfabetico.get(j).getNome().compareToIgnoreCase(alunosAlfabetico.get(j+1).getNome())>0){
					alunosAlfabetico.set(j+1,alunosAlfabetico.get(j));
					alunosAlfabetico.set(j,aux);
				}
			}
		}
		for(int i = 0;i<quantidadeAlunos;i++) {
			try {
				FileWriter filewriter;
				File resultadoAlfabetico = new File(diretorioAbsoluto+"/disciplinas/resultados/alfabetico/"+alunosAlfabetico.get(i).getNome());
				filewriter = new FileWriter(resultadoAlfabetico);
				filewriter.write(alunosAlfabetico.get(i).getRespostas().toUpperCase()+"	"+alunosAlfabetico.get(i).getNome()+"	"+alunosAlfabetico.get(i).getPontuacao()+" pontuacao");
				filewriter.close();
				File resultadoDecrescente = new File(diretorioAbsoluto+"/disciplinas/resultados/decrescente/"+ alunosDecrescente.get(i).getNome());
				filewriter = new FileWriter(resultadoDecrescente);
				filewriter.write(alunosDecrescente.get(i).getRespostas().toUpperCase()+"	"+alunosDecrescente.get(i).getNome()+"	"+alunosDecrescente.get(i).getPontuacao()+" pontuacao");
				filewriter.close();
		}catch(IOException e){
				System.out.println("Ocorreu um erro");
				e.printStackTrace();
		}
		}
		
	}
	//Isso aqui é pra achar o diretório no qual os arquivos estão e possibilitar a criação dos arquivos em qualquer máquina
	public static String acharDiretorioAbsoluto(){
		File arquivoTemp = new File("arquivotemp.txt");
		File arquivoTemp2 = arquivoTemp.getAbsoluteFile().getParentFile();
		arquivoTemp.deleteOnExit();
		arquivoTemp2.deleteOnExit();
		return arquivoTemp2.getAbsolutePath();
	}
}
