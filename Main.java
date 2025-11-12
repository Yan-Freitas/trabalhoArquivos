
//MUITOS IMPORTS, TODOS SENDO USADOS
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
	public static void main(String args[]) {
		//Isso aqui é pra guardar as disciplinas pra deixar mais organizado
		ArrayList<Disciplina> disciplinas = new ArrayList<Disciplina>();
		//Isso aqui é pra funcionar em qualquer máquina sem precisar especificar o caminho do diretório, consulta lá na classe disciplina pra ver detalhes
		String diretorioAbsoluto = Disciplina.acharDiretorioAbsoluto();
		//Isso aqui tudo é a inicialização de todas as pastas
		new File(diretorioAbsoluto+"/disciplinas").mkdir();
		new File(diretorioAbsoluto+"/disciplinas/gabaritos").mkdir();
		new File(diretorioAbsoluto+"/disciplinas/resultados").mkdir();
		new File(diretorioAbsoluto+"/disciplinas/resultados/alfabetico").mkdir();
		new File(diretorioAbsoluto+"/disciplinas/resultados/decrescente").mkdir();
		//Esse processo aqui é pra checar caso já existam disciplinas, aí ela pega os dados delas no arquivo de texto e adiciona na arraylist
		File pasta = new File(diretorioAbsoluto+"/disciplinas");
		File[] listadeDisciplinas = pasta.listFiles();
		//Esse for pega cada arquivo na pasta de disciplinas que estão salvos em um array de files acima
		int index = 0;
		for (File file : listadeDisciplinas) {
			Path path = FileSystems.getDefault().getPath(diretorioAbsoluto+"/disciplinas", file.getName());
			try {
				//Esse fileType é pra checar o tipo do arquivo
				String fileType = Files.probeContentType(path);
				try {
					//Isso aqui é pra checar se é um arquivo de texto
					if(fileType.equalsIgnoreCase("text/plain")) {
						Scanner leitor = new Scanner(file);
						disciplinas.add(new Disciplina(file.getName()));
						//Isso aqui é pra adicionar os alunos em cada disciplina encontrada.
						while(leitor.hasNextLine()) {
							String data = leitor.nextLine();
							disciplinas.get(index).adicionarAluno(data.substring(11),data.substring(0, 10));
						}
						leitor.close();
					}
					index++;
				}catch(NullPointerException e) {
				}
			} catch (IOException e) {
				System.out.println("Ocorreu um erro!");
				e.printStackTrace();
			}    
		}
		//Essa aqui é a lógica geral do programa, ainda falta eu colocar um while aqui porque por enquanto tá só em fase de teste
		//Se quiser coloca um while aí
		boolean sair=false;
		Scanner teclado=new Scanner(System.in);
		do{
			int opcao;
			System.out.println("-----Menu Inicial-----");
			System.out.println("1) Nova Disciplina");
			System.out.println("2) Cadastrar Alunos");
			System.out.println("3) Gerar Resultado");
			System.out.println("4) Sair");
			do{
				System.out.print("Insira uma opção válida: ");
				opcao=teclado.nextInt();
			}while(opcao!=1 && opcao!=2 && opcao!=3 && opcao!=4);
			Pattern pattern;
			Matcher matcher;
			switch(opcao){
				//Cria o arquivo da disciplina(Onde o nome dos alunos e seu gabarito serão armazenados) e o arquivo do gabarito oficial
				case 1:
					pattern = Pattern.compile("^[vf]{10}$", Pattern.CASE_INSENSITIVE);
					String disciplinaNome;
					do{
						System.out.print("Disciplina: ");
						disciplinaNome=teclado.next()+".txt";
					}while(!Disciplina.criarDisciplina(diretorioAbsoluto, disciplinaNome));
					disciplinas.add(new Disciplina(disciplinaNome));
					String respostas;
					System.out.print("Respostas: ");
					respostas=teclado.next();
					matcher = pattern.matcher(respostas);
					while(!matcher.matches()||respostas.toUpperCase().equals("VVVVVVVVVV")||respostas.toUpperCase().equals("FFFFFFFFFF")) {
						if (!(respostas.length()==10)){
							System.out.println("O gabarito deve ter exatamente 10 respostas!");
						}
						if (!matcher.matches()){
							System.out.println("As respostas só podem ser V ou F!");
						}
						if (respostas.toUpperCase().equals("VVVVVVVVVV")||respostas.toUpperCase().equals("FFFFFFFFFF")){
							System.out.println("As respostas não podem ser todas iguais!");
						}
						System.out.print("Respostas: ");
						respostas = teclado.next();
						matcher = pattern.matcher(respostas);
					}
					disciplinas.getLast().gerarGabarito(diretorioAbsoluto, respostas);
					break;
				//Adiciona alunos no arquivo da disciplina correspondente
				case 2:
					pattern = Pattern.compile("^[vf]{10}$", Pattern.CASE_INSENSITIVE);
					//Esse negócio de pattern é para checar se a resposta contém APENAS v ou f ou ambas na string por meio do regex
					System.out.println("-----Disciplinas-----");
					int cont=0;
					for(Disciplina disciplina : disciplinas) {
						System.out.println((disciplinas.indexOf(disciplina)+1)+" "+")"+" "+disciplina.getNome().substring(0,disciplina.getNome().indexOf(".")));
						cont++;
					}
					int disciplinaIndex;
					do{
						System.out.print("Insira uma opção válida: ");
						disciplinaIndex = teclado.nextInt()-1;
					}while(disciplinaIndex<0 || disciplinaIndex>=cont);
					System.out.print("Quantidade de Alunos: ");
					int qtdAlunos = teclado.nextInt();
					System.out.println("-----Cadastro de Alunos-----");
					for(int i =0;i<qtdAlunos;i++) {
						System.out.print("Nome: ");
						teclado.nextLine();
						String nome = teclado.nextLine();
						System.out.print("Respostas: ");
						respostas = teclado.next();
						matcher = pattern.matcher(respostas);
						//A lógica do pattern matcher sendo invocada ai
						while(!matcher.matches()) {
							if (!(respostas.length()==10)){
								System.out.println("O gabarito deve ter exatamente 10 respostas!");
							}
							if (!matcher.matches()){
								System.out.println("As respostas só podem ser V ou F!");
							}
							System.out.print("Respostas: ");
							respostas = teclado.next();
							matcher = pattern.matcher(respostas);
						}
						disciplinas.get(disciplinaIndex).adicionarAluno(diretorioAbsoluto, nome, respostas);
					}
					break;
				//Gera e imprime as pontuações dos alunos e a média da turma
				case 3:
					System.out.println("-----Disciplinas-----");
					for(Disciplina disciplina : disciplinas) {
						System.out.println((disciplinas.indexOf(disciplina)+1)+" "+")"+" "+disciplina.getNome().substring(0,disciplina.getNome().indexOf(".")));
					}
					do{
						System.out.print("Insira uma opção válida: ");
						disciplinaIndex = teclado.nextInt()-1;
					}while(disciplinaIndex<0 || disciplinaIndex>=disciplinas.size());
					disciplinas.get(disciplinaIndex).gerarResultados(diretorioAbsoluto);
					System.out.println("Resultado Gerado!");
					int opcao3=0;
					System.out.println("-----Visualização-----");
					System.out.println("1) Ordem Alfabética");
					System.out.println("2) Ordem Decrescente");
					do{
						System.out.print("Insira uma opção válida: ");
						opcao3=teclado.nextInt();
					}while(opcao3!=1 && opcao3!=2);
					System.out.println("-----Resultados-----");
					disciplinas.get(disciplinaIndex).mostrarResultados(diretorioAbsoluto,opcao3);
					break;
				//Sai do sistema
				case 4:
					sair = true;
					break;
			}
		}while(!sair);
		teclado.close();
	}
	
}
