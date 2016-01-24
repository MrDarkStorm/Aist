/*
	Эта программа и язык Aist созданы Александром Русакевичем(MrDarkStorm)
	Если у вас возникнут вопросы, то обращайтесь:
	Skype: Pro100SanyaRusakevich
*/
import java.io.*;
import java.util.*;
import java.math.*;

public class Aist{
	
	static int line_num;
	
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
	static final String[] Aist_opeators = new String[]{"var","variable","int","integer","str","string","real","set"};
	
	//Отпарсить строку
	static String Aist_ParseString(String s){
		String RetString = "";
		for(int i=0;i<s.length();i++){
			//Escape последовательности
			if(s.charAt(i)=='\\'){
				i++;
				if(i==s.length()){
					System.out.println("EscapeSequenceError: Met by the end of the string, but expected control character Escape sequence. Line "
						+Integer.toString(line_num+1));
						System.exit(1);
				}
				if(s.charAt(i)=='\\'){
					RetString += s.charAt(i);
				}
				else if(s.charAt(i)=='n'){
					RetString += "\n";
				}
				else{
					System.out.println("EscapeSequenceError: Unknown Escape sequence. Line "
						+Integer.toString(line_num+1));
						System.exit(1);
				}
			}else{
				RetString += s.charAt(i);
			}
		}
		return RetString;
	}
	
	static int Aist_ParseInteger(String s){
		int Rets = 0;
		try{
			Rets = Integer.parseInt(s.replaceAll(" ",""));
		}catch(Exception e){
			System.out.println("TypeError: This object is not a number - \'"+s+"\'. Line "
						+Integer.toString(line_num+1));
						System.exit(1);
		}
		return Rets;
	}
	
	static double Aist_ParseReal(String s){
		double Rets = 0;
		try{
			Rets = Double.parseDouble(s.replaceAll(" ","").replace(',','.'));
		}catch(Exception e){
			System.out.println("TypeError: This object is not a number - \'"+s+"\'. Line "
						+Integer.toString(line_num+1));
						System.exit(1);
		}
		return Rets;
	}
	
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
		for(line_num=0;line_num<Lines.size();line_num++){
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
				//Проверить, существует ли уже такая переменная
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
				if(Lines.get(line_num).contains("=")){
					System.out.println("NameError: Wrong name of variable - \'"+Lines.get(line_num).split("\\s+")[2]
							+"\'. Line "+Integer.toString(line_num+1));
						System.exit(1);
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
			
			
			
			
			//Установить значение переменной
			else if(Lines.get(line_num).split("\\s+")[0].equals("set")){
				if(!Lines.get(line_num).contains("=")){
					System.out.println("SyntaxError: The symbol '=' must be in SET. Line "+Integer.toString(line_num+1));
					System.exit(1);
				}
				String First_Part = Lines.get(line_num).split("=")[0].trim();
				if(First_Part.split("\\s+").length!=2){
					System.out.println("ParameterError: SET must contain a variable name. Line "+Integer.toString(line_num+1));
					System.exit(1);
				}
				String Var_Name = First_Part.split("\\s+")[1];
				//Проверить существует ли переменная
				if(!String_Names.contains(Var_Name)&&!Int_Names.contains(Var_Name)&&
					!Real_Names.contains(Var_Name)){
						System.out.println("NameError: Variable does not exist - \'"+Var_Name
							+"\'. Line "+Integer.toString(line_num+1));
						System.exit(1);
					}
				String Set_Value = Lines.get(line_num).substring(Lines.get(line_num).indexOf('=')+1,Lines.get(line_num).length());
				//Присваивание значения строковой переменной
				if(String_Names.contains(Var_Name)){
					String Set_String = Aist_ParseString(Set_Value);
					String_Variables.set(String_Names.indexOf(Var_Name),Set_String);
				}
				//Присваивание значения целочисленной переменной
				else if(Int_Names.contains(Var_Name)){
					int Set_Int = Aist_ParseInteger(Set_Value);
					Int_Variables.set(Int_Names.indexOf(Var_Name),Set_Int);
				}
				//Присваивание значения дроби
				else if(Real_Names.contains(Var_Name)){
					double Set_Real = Aist_ParseReal(Set_Value);
					Real_Variables.set(Real_Names.indexOf(Var_Name),Set_Real);
				}
			}
			
			
			
			//Если имя функции неправильное, то...
			else{
				System.out.println("SyntaxError: Unknown function - \'"+Lines.get(line_num).split("\\s+")[0]+"\'. Line "+Integer.toString(line_num+1));
				System.exit(1);
			}
			//==============================
		}
    }
}