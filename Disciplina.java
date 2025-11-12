import java.io.BufferedReader;
import java.io.BufferedWriter;
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
			System.out.println("Aluno(a) " + nome + " adicionado(a)!");
		}catch (IOException e) {
		      System.out.println("Ocorreu um erro");
		      e.printStackTrace();
	    }
	}

	//Isso aqui era pra gerar o resultado do gabarito que ficaria salvo na pasta resultados, só que ainda não tá completa
	//FINALIZAR MÉTODO
	public void gerarResultados(String diretorioAbsoluto) {
		String respostasCorretas=null;
		try {
			FileReader fr = new FileReader(diretorioAbsoluto+"/disciplinas/gabaritos/"+"Gabarito"+" "+nome);
			BufferedReader br = new BufferedReader(fr);
			respostasCorretas = br.readLine();
			br.close();
			fr.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}catch (IOException e){
			e.printStackTrace();
		}
		for(int i = 0;i<alunos.size();i++) {
			alunos.get(i).gerarNota(respostasCorretas);
		}
		ArrayList<Aluno> alunosAlfabetico = new ArrayList<>(alunos);
		ArrayList<Aluno> alunosDecrescente = new ArrayList<>(alunos);
		int quantidadeAlunos=alunos.size();
		for (int i=0;i<quantidadeAlunos-1;i++){
			for (int j=0;j<quantidadeAlunos-i-1;j++){
				//Ordenação Alfabética
				if((alunosAlfabetico.get(j).getNome().compareToIgnoreCase(alunosAlfabetico.get(j+1).getNome()))>0){
					Aluno aux2 = alunosAlfabetico.get(j+1);
					alunosAlfabetico.set(j+1,alunosAlfabetico.get(j));
					alunosAlfabetico.set(j,aux2);
				}
				//Ordenação Decrescente
				if(alunosDecrescente.get(j+1).getPontuacao()>alunosDecrescente.get(j).getPontuacao()){
					Aluno aux1 = alunosDecrescente.get(j+1);
					alunosDecrescente.set(j+1,alunosDecrescente.get(j));
					alunosDecrescente.set(j,aux1);
				}
			}
		}
		File resultadoAlfabetico = new File(diretorioAbsoluto+"/disciplinas/resultados/alfabetico/"+"Resultado "+nome);
		File resultadoDecrescente = new File(diretorioAbsoluto+"/disciplinas/resultados/decrescente/"+ "Resultado "+nome);
		try{
			FileWriter fw1 = new FileWriter(resultadoAlfabetico);
			FileWriter fw2 = new FileWriter(resultadoDecrescente);
			BufferedWriter bw1 = new BufferedWriter(fw1);
			BufferedWriter bw2 = new BufferedWriter(fw2);
			for (int i=0;i<quantidadeAlunos;i++){
				bw1.write(alunosAlfabetico.get(i).getNome()+"	"+alunosAlfabetico.get(i).getPontuacao());
				bw1.newLine();
				bw2.write(alunosDecrescente.get(i).getNome()+"	"+alunosDecrescente.get(i).getPontuacao());
				bw2.newLine();
			}
			bw1.close();
			bw2.close();
			fw1.close();
			fw2.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	public void mostrarResultados(String diretorioAbsoluto, int opcao){
		String path;
		double mediaTurma=0;
		int alunos=0;
		if(opcao==1){
			path = diretorioAbsoluto+"/disciplinas/resultados/alfabetico/"+"Resultado "+nome;
		}else{
			path = diretorioAbsoluto+"/disciplinas/resultados/decrescente/"+"Resultado "+nome;
		}
		try(BufferedReader br = new BufferedReader(new FileReader(path))){
			String linha = br.readLine();
			while (linha!=null){
				String[] dados=linha.split("\t");
				System.out.println("Nome: "+dados[0]);
				System.out.println("Pontuação: "+dados[1]);
				System.out.println();
				linha = br.readLine();
				mediaTurma=mediaTurma+(Integer.parseInt(dados[1]));
				alunos++;
			}
			br.close();
		}catch(IOException e){
			e.printStackTrace();
		}
		if (alunos!=0){
			mediaTurma=mediaTurma/alunos;
			System.out.printf("Média da Turma: %.2f%n", mediaTurma);
		}else{
			System.out.println("Essa turma ainda não possui alunos!");
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
