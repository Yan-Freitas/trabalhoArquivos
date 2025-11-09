
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
	Scanner scanner = new Scanner(System.in);
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
	for (File file : listadeDisciplinas) {
    	Path path = FileSystems.getDefault().getPath(diretorioAbsoluto+"/disciplinas", file.getName());
    	try {
    		//Esse mimetype é pra checar o tipo do arquivo
			String mimeType = Files.probeContentType(path);
			try {
				int index = 0;
				//Isso aqui é pra checar se é um arquivo de texto
				if(mimeType.equalsIgnoreCase("text/plain")) {
					Scanner leitor = new Scanner(file);
					disciplinas.add(new Disciplina(file.getName()));
					//Isso aqui é pra adicionar os alunos em cada disciplina encontrada.
					while(leitor.hasNextLine()) {
						String data = leitor.nextLine();
						disciplinas.get(index).adicionarAluno(data.substring(11),data.substring(0, 9));
					}
				}
				index++;
			}catch(NullPointerException e) {
			}
		} catch (IOException e) {
			e.printStackTrace();
		}    
	}
	//Essa aqui é a lógica geral do programa, ainda falta eu colocar um while aqui porque por enquanto tá só em fase de teste
	//Se quiser coloca um while aí
	System.out.println("Escolha uma das seguintes opções:\n 1 -- Adicionar uma disciplina\n 2 -- Adicionar alunos a uma disciplina\n 3 -- Gerar o resultado de uma disciplina\n 4 -- Ver disciplinas;");
	switch(scanner.nextInt()){
	//Esse case aqui é pra adicionar uma disciplina, aí ela cria um arquivo de texto e ao mesmo tempo adiciona no arraylist de disciplinas
		case 1:
			int qtdDisciplinas;
			System.out.println("Gostaria de criar quantas disciplinas?");
			qtdDisciplinas = scanner.nextInt();
			for(int i = 0;i<qtdDisciplinas;i++) {
				System.out.println("Escolha o nome da disciplina");
				String disciplinaNome = scanner.next()+".txt";
				if(Disciplina.criarDisciplina(diretorioAbsoluto, disciplinaNome)) {
					disciplinas.add(new Disciplina(disciplinaNome));			
				}
			}
			break;
	//Esse case aqui é pra adicionar alunos.
		case 2:
			//Esse negócio de pattern é para checar se a resposta contém APENAS v ou f ou ambas na string por meio do regex
			Pattern pattern = Pattern.compile("[v|f]", Pattern.CASE_INSENSITIVE);
			Matcher matcher;
			System.out.println("Escolha a disciplina que quer adicionar o aluno");
			for(Disciplina disciplina : disciplinas) {
				System.out.println(disciplinas.indexOf(disciplina)+" "+"--"+" "+disciplina.getNome());
			}
			int disciplinaIndex = scanner.nextInt();
			System.out.println("Escolha quantos alunos quer adicionar");
			int qtdAlunos = scanner.nextInt();
			for(int i =0;i<qtdAlunos;i++) {
				System.out.println("Escolha o nome do aluno");
				scanner.nextLine();
				String nome = scanner.nextLine();
				System.out.println(nome);
				System.out.println("Agora digite as respostas dele");
				String respostas = scanner.next();
				matcher = pattern.matcher(respostas);
				//A lógica do pattern matcher sendo invocada ai
				while(!(respostas.length()==10)||!matcher.find()) {
					System.out.println("O aluno precisa ter exatamente 10 respostas e podem ser só V ou F");
					respostas = scanner.next();
					matcher = pattern.matcher(respostas);
				}
				disciplinas.get(disciplinaIndex).adicionarAluno(diretorioAbsoluto, nome, respostas);
			}
			break;
		//Expandir para gerar o resultado dos alunos
		//Esse case aí é pra gerar o gabarito, como dito acima ainda falta gerar o arquivo dos alunos, por enquanto gera só o gabarito
		case 3:
			String respostas;
			pattern = Pattern.compile("[v|f]", Pattern.CASE_INSENSITIVE);
			System.out.println("Escolha a disciplina que quer criar o resultado");
			for(Disciplina disciplina : disciplinas) {
				System.out.println(disciplinas.indexOf(disciplina)+" "+"--"+" "+disciplina.getNome());
			}
			disciplinaIndex = scanner.nextInt();
			System.out.println("Escreva o gabarito");
			respostas = scanner.next();
			matcher = pattern.matcher(respostas);
			//A mesma lógica do pattern matcher
			while(!(respostas.length()==10)||!matcher.find()) {
				System.out.println("O gabarito precisa ter exatamente 10 respostas e podem ser só V ou F");
				respostas = scanner.next();
				matcher = pattern.matcher(respostas);
			}
			//Até agora só o método de gerar gabarito tá sendo invocado porque ele tá prestando
			disciplinas.get(disciplinaIndex).gerarGabarito(diretorioAbsoluto,respostas);
			break;
		//Esse case aí é pra ver os alunos da disciplina que o cara escolher, sinceramente só fiz pra checar se os negócios estavam sendo salvos direitos no arraylist
		case 4:
			System.out.println("Escolha a disciplina que quer ver");
			for(Disciplina disciplina : disciplinas) {
				System.out.println(disciplinas.indexOf(disciplina)+" "+"--"+" "+disciplina.getNome());
			}
			disciplinaIndex = scanner.nextInt();
			for(Aluno aluno : disciplinas.get(disciplinaIndex).getAlunos()) {
				System.out.println(aluno.getNome()+" "+aluno.getRespostas());
			}
			break;
		}
	}
}
