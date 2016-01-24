/*
	Эта программа и язык Aist созданы Александром Русакевичем(MrDarkStorm)
	Если у вас возникнут вопросы, то обращайтесь:
	Skype: Pro100SanyaRusakevich
*/
import java.io.*;
import java.util.*;
import java.math.*;

public class Aist{
	
	//Список значений строковых переменных
	static List<String> String_Variables = new ArrayList<String>();
	//Список имён строковых переменных
	static List<String> String_Names = new ArrayList<String>();
	//Список значений целочисленных переменных
	static List<Integer> Int_Variables = new ArrayList<Integer>();
	//Список имён целочисленных переменных
	static List<String> Int_Names = new ArrayList<String>();
	//Список значений дробных переменных
	static List<Double> Real_Variables = new ArrayList<Double>();
	//Список имён дробных переменных
	static List<String> Real_Names = new ArrayList<String>();
	
	//Операторы Aist
	static final String[] Aist_opeators = new String[]{"var","variable","int","integer","str","string","real"};
	
    public static void main(String[] args){
		//Проверяем правильность количества параметров
        if(args.length!=1){
            System.out.println("ParametersError: Wrong number of command line parameters");
			System.exit(1);
        }
		//Узнать версию программы
		if(args[0].equalsIgnoreCase("-version")){
			System.out.println("Aist version: alpha 0.1");
			System.exit(1);
		}
		//Узнать версию программы
		if(args[0].equalsIgnoreCase("-author")){
			System.out.println("The author of the program and language Aist - Alexander Rusakevich(MrDarkStorm)\n"+
				"If You have any questions, please contact me:\nSkype: Pro100SanyaRusakevich");
			System.exit(1);
		}
		
		List<String> Lines = new ArrayList<String>();
		//Проверяем, всё ли в порядке с файлом. Если да - то считываем данные в файл
		try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(args[0]),"UTF-8"))){
			String s;
			while((s=br.readLine())!=null){
				Lines.add(s.replaceAll("^\\s+", ""));
			}
		}catch(Exception e){
			System.out.println("OpeningFileError: Cannot open file - no access To it or it does not exist");
			System.exit(1);
		}
		
		//Реализация интерпретации комманд
		for(int line_num=0;line_num<Lines.size();line_num++){
			if(Lines.get(line_num).charAt(0)==';'||Lines.get(line_num).trim()=="") continue;
			
			//Создание новой переменной============================== +
			if(Lines.get(line_num).split("\\s+")[0].equals("variable")||Lines.get(line_num).split("\\s+")[0].equals("var")){
				if(Lines.get(line_num).split("\\s+").length>3){
					System.out.println("ParameterError: Were found the extra parameters at the end of the line. Line "+Integer.toString(line_num+1));
					System.exit(1);
				}
				if(Lines.get(line_num).split("\\s+").length<3){
					System.out.println("ParameterError: Too few parameters. Line "+Integer.toString(line_num+1));
					System.exit(1);
				}
				//Проверить, твует ли уже такая переменная
				if(String_Names.contains(Lines.get(line_num).split("\\s+")[2])||Int_Names.contains(Lines.get(line_num).split("\\s+")[2])||
					Real_Names.contains(Lines.get(line_num).split("\\s+")[2])){
						System.out.println("NameError: The object named \'"+Lines.get(line_num).split("\\s+")[2]
							+"\' already exists. Line "+Integer.toString(line_num+1));
						System.exit(1);
					}
				//Проверить, является ли имя переменной ключевым словом
				for(int i=0;i<Aist_opeators.length;i++){
					if(Lines.get(line_num).split("\\s+")[2].equals(Aist_opeators[i])){
						System.out.println("NameError: The object named \'"+Lines.get(line_num).split("\\s+")[2]
							+"\' is reserved by the interpreter. Line "+Integer.toString(line_num+1));
						System.exit(1);
					}
				}
				//Создать новую строковую переменную
				if(Lines.get(line_num).split("\\s+")[1].equals("str")||Lines.get(line_num).split("\\s+")[1].equals("string")){
					String_Names.add(Lines.get(line_num).split("\\s+")[2]);
					String_Variables.add("");
				}
				//Создание новой целочисленной переменной
				else if(Lines.get(line_num).split("\\s+")[1].equals("int")||Lines.get(line_num).split("\\s+")[1].equals("integer")){
					Int_Names.add(Lines.get(line_num).split("\\s+")[2]);
					Int_Variables.add(0);
				}
				//Создание новой дроби
				else if(Lines.get(line_num).split("\\s+")[1].equals("real")){
					Real_Names.add(Lines.get(line_num).split("\\s+")[2]);
					Real_Variables.add(0.0);
				//Ошибка "Неизвестный тип данных"
				}else{
					System.out.println("TypeError: Unknown data type \'"+Lines.get(line_num).split("\\s+")[1]+"\'. Line "+Integer.toString(line_num+1));
					System.exit(1);
				}
			}
			//=======================================================
		}
    }
}